package sandblasterplugin.controllers;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ghidra.util.Msg;
import sandblasterplugin.LoggerUtil;
import sandblasterplugin.SandBlasterBackgroundTask;
import sandblasterplugin.SandBlasterPlugin;
import sandblasterplugin.enums.PropertyChangeEventNames;
import sandblasterplugin.models.ConfigurationModel;
import sandblasterplugin.models.ResultModel;
import sandblasterplugin.utils.Utilities;
import sandblasterplugin.views.ConfigurationView;


public class ConfigurationController {
	
	private ConfigurationModel configurationModel;
	private ConfigurationView configurationView;
	private LoggerUtil logUtil;
	private SandBlasterBackgroundTask sandBlasterBackgroundTask;
	private SandBlasterPlugin sandBlasterPlugin;
	private ResultModel resultModel;

	
	
    public ConfigurationController(
    		ConfigurationModel configurationModel, 
    		ConfigurationView configurationView, 
    		SandBlasterPlugin sandBlasterPlugin, 
    		ResultModel resultModel
    		) 
    {
    	this.configurationModel = configurationModel;
    	this.configurationView = configurationView;
    	this.sandBlasterPlugin = sandBlasterPlugin;
    	this.resultModel = resultModel;
    	this.logUtil = new LoggerUtil(configurationView.getLogTextArea());

    	initListeners();
    }
    
    public ConfigurationModel getConfigurationModel() {
		return configurationModel;
	}

	public ConfigurationView getConfigurationView() {
		return configurationView;
	}

	public LoggerUtil getLogUtil() {
		return logUtil;
	}
	
	public SandBlasterPlugin getSandBlasterPlugin() {
		return this.sandBlasterPlugin;
	}
	
	
	// button actions
    private void initListeners() {
    	// add property change listener to model
    	configurationModel.addPropertyChangeListener(this::ConfModelPropertyChangeListner);
    	configurationModel.fetchUserPreferences();
		
    	configurationView.getPython2BinChooseButton().addActionListener(this::choosePython2ButtonAction);
    	configurationView.getPython3BinChooseButton().addActionListener(this::choosePython3ButtonAction);
    	configurationView.getAutoDetectButton().addActionListener(this::autoDetectPythonBinsButtonAction);
    	configurationView.getiOSVersionOkButton().addActionListener(this::okButtonAction);
    	
    	configurationView.getKernelExtFileChooseButton().addActionListener(
    			e -> chooseFileButtonAction(
    					e, configurationModel.getKernelExtFilePathString(), configurationModel::setKernelExtFilePathString, false)
    			);
    	configurationView.getSandboxdFileChooseButton().addActionListener(
    			e -> chooseFileButtonAction(
    					e, configurationModel.getSandboxdFilePathString(), configurationModel::setSandboxdFilePathString, false)
    			);
    	
		String projectDirPath = this.sandBlasterPlugin.getProjectDirectoryPath();
		if (projectDirPath != null) {
    		configurationModel.setOutDirPathString(projectDirPath);
		} 
    	configurationView.getOutDirPathChooseButton().addActionListener(
    			e -> chooseFileButtonAction(
    					e, configurationModel.getOutDirPathString(), configurationModel::setOutDirPathString, true)
    			);
    	
    	configurationView.getStartButton().addActionListener(this::startButtonAction);
    	configurationView.getCancelButton().addActionListener(this::cancelButtonAction);
    	
    	// text view actions
    	configurationView.getiOSVersionTextField().addActionListener(this::okButtonAction);
    
    	
    	configurationView.getiOSVersionTextField().getDocument().addDocumentListener(new iOSVersionTextFieldDocumentListener());
    }
    
    private void tryToEnableStartButton() {
        if (
        		!configurationModel.getPython2BinPathString().equals("") &&
        		!configurationModel.getPython3BinPathString().equals("") &&
        		!configurationModel.getiOSVersionString().equals("") &&
        		(
        				!configurationModel.getKernelExtFilePathString().equals("") || 
        				!configurationView.getKernelExtFilePathTextField().isEnabled()
        		) &&
        		(
        				!configurationModel.getSandboxdFilePathString().equals("") || 
        				!configurationView.getSandboxdFilePathTextField().isEnabled()
        		) &&
        		(
        				!configurationModel.getKernelCacheFilePathString().equals("") || 
        				!configurationView.getKernelCacheFilePathTextField().isEnabled()
        		) &&
        		!configurationModel.getOutDirPathString().equals("")
        		
           ) {
        	configurationView.getStartButton().setEnabled(true);
        	configurationView.getCancelButton().setEnabled(true);
        } else {
        	configurationView.getStartButton().setEnabled(false);
        	configurationView.getCancelButton().setEnabled(false);
        }
    }
    
    private void enableRequiredFields(String iOSVersionString) {
    	if (iOSVersionString.equals("")) {
        	configurationView.getKernelExtFilePathTextField().setEnabled(false);
        	configurationView.getKernelExtFileChooseButton().setEnabled(false);
        	
        	configurationView.getSandboxdFilePathTextField().setEnabled(false);
        	configurationView.getSandboxdFileChooseButton().setEnabled(false);
        	
        	configurationView.getKernelCacheFilePathTextField().setEnabled(false);
        	configurationView.getKernelCacheFileChooseButton().setEnabled(false);
    	} else {
            Integer iOSMajorVersionInteger = Integer.parseInt(Utilities.getMajoriOSVersion(iOSVersionString));
            if (iOSMajorVersionInteger >= 5 && iOSMajorVersionInteger <= 8) {
            	configurationView.getKernelExtFilePathTextField().setEnabled(true);
            	configurationView.getKernelExtFileChooseButton().setEnabled(true);
            	
            	configurationView.getSandboxdFilePathTextField().setEnabled(true);
            	configurationView.getSandboxdFileChooseButton().setEnabled(true);
            	
            	configurationView.getKernelCacheFilePathTextField().setEnabled(false);
            	configurationView.getKernelCacheFileChooseButton().setEnabled(false);

            } else if(iOSMajorVersionInteger >= 12) {
            	configurationView.getKernelExtFilePathTextField().setEnabled(false);
            	configurationView.getKernelExtFileChooseButton().setEnabled(false);
            	
            	configurationView.getSandboxdFilePathTextField().setEnabled(false);
            	configurationView.getSandboxdFileChooseButton().setEnabled(false);
            	
            	configurationView.getKernelCacheFilePathTextField().setEnabled(true);
            	configurationView.getKernelCacheFileChooseButton().setEnabled(true);
            } else {
            	configurationView.getKernelExtFilePathTextField().setEnabled(true);
            	configurationView.getKernelExtFileChooseButton().setEnabled(true);
            	
            	configurationView.getSandboxdFilePathTextField().setEnabled(false);
            	configurationView.getSandboxdFileChooseButton().setEnabled(false);
            	
            	configurationView.getKernelCacheFilePathTextField().setEnabled(false);
            	configurationView.getKernelCacheFileChooseButton().setEnabled(false);
            }
    	}
    }
    
    private void ConfModelPropertyChangeListner(PropertyChangeEvent evt) {
        if (PropertyChangeEventNames.PYTHON2_BIN_PATH_UPDATED.getEventName().equals(evt.getPropertyName())) {
        	configurationView.getPython2TextField().setText((String) evt.getNewValue());
        } else if (PropertyChangeEventNames.PYTHON3_BIN_PATH_UPDATED.getEventName().equals(evt.getPropertyName())) {
        	Msg.info(null, "yes ");
        	configurationView.getPython3TextField().setText((String) evt.getNewValue());
        } else if (PropertyChangeEventNames.KERNEL_EXT_FILE_PATH_UPDATED.getEventName().equals(evt.getPropertyName())) {
        	configurationView.getKernelExtFilePathTextField().setText((String) evt.getNewValue());
        } else if (PropertyChangeEventNames.SANDBOXD_FILE_PATH_UPDATED.getEventName().equals(evt.getPropertyName())) {
        	configurationView.getSandboxdFilePathTextField().setText((String) evt.getNewValue());
        } else if (PropertyChangeEventNames.KERNEL_CACHE_FILE_PATH_UPDATED.getEventName().equals(evt.getPropertyName())) {
        	configurationView.getKernelCacheFilePathTextField().setText((String) evt.getNewValue());
        } else if (PropertyChangeEventNames.OUT_DIR_PATH_UPDATED.getEventName().equals(evt.getPropertyName())) {
        	configurationView.getOutDirPathTextField().setText((String) evt.getNewValue());
        } else if (PropertyChangeEventNames.IOS_VERSION_VALUE_UPDATED.getEventName().equals(evt.getPropertyName())) {
        	enableRequiredFields((String) evt.getNewValue());
        }
        
        // Check if both paths are set to enable the Start button
        tryToEnableStartButton();
    }
    
    // buttons listeners
    	// python buttons
    private String getPythonInstallPath(String pythonBinPath) {
    	logUtil.logMessage("Extracting the absolute path of the '" + pythonBinPath + "' installation directory...");
    	return Utilities.runCommand(logUtil, pythonBinPath, "-c", "import sys; print(sys.executable)");
    }
    
    private Boolean isValidPythonBinary(String pythonPath, String pythonName, Boolean showErrorPane) {
    	logUtil.logMessage("Checking if file: '" + pythonPath + "' is a valid " + pythonName + "...");
        String outputCommandString = Utilities.runCommand(logUtil, pythonPath, "--version");
        if (outputCommandString != null && outputCommandString.startsWith(pythonName)) {
        	logUtil.logMessage("The '" + pythonPath + "' is a valid " + pythonName);
        	return true;
        }
        
        String msgString = "The selected file: '" + pythonPath + "' is not a valid " + pythonName + ".";
        logUtil.logMessage(msgString);
        if (showErrorPane) {
    		JOptionPane.showMessageDialog(null, msgString, "Perquesits Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private Boolean isPythonPackageInstalled(String pythonPath, String pythonName, String packageName) {
    	logUtil.logMessage("Checking if " + pythonName + " has the '" + packageName + "' package installed...");
    	
    	String[] command = {pythonPath, "-m", "pip", "list"};
    	logUtil.logMessage("Running external command: [ " + String.join(" ", command)+  " ]");
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        boolean packageIsInstalled = false;
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            	logUtil.logMessage("* " + line);
                if (line.startsWith(packageName)) {
                	packageIsInstalled = true;
                	break;
                }
            }
            int exitCode = process.waitFor();
            logUtil.logMessage("* Exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
        	logUtil.logMessage("* Error: " + e.toString());
        }
        
        if (packageIsInstalled) {
        	logUtil.logMessage(pythonName + " has the required '" + packageName + "' package installed.");
        	return packageIsInstalled;
        }
        
        
        String msgString = "The selected " + pythonName + " does not have required '" + packageName + "' package installed. Please, make sure to install them.";
        logUtil.logMessage(msgString);
        JOptionPane.showMessageDialog(null, msgString, "Python 3 Requiremenets Error", JOptionPane.ERROR_MESSAGE);
            
        // ask user to automatically install
        int response = JOptionPane.showConfirmDialog(null, "Do you want to try to automatically install " + pythonName + " requirements?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
        	
        	// Try to install package
        	logUtil.logMessage("Trying to install '" + packageName + "' package required by " + pythonName + "...");
            String outputCommandString = Utilities.runCommand(logUtil, pythonPath, "-m", "pip", "install", packageName);
            if (outputCommandString != null) {
            	packageIsInstalled = true;
            }else {
            	JOptionPane.showMessageDialog(null, "Unable to install '" + packageName + "' package required by " + pythonName + ". Please, make sure them to install them and try again.", "Perquesits Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
		return packageIsInstalled;
    }
    
    private void choosePython2ButtonAction(ActionEvent e) {
    	JFileChooser fileChooser = new JFileChooser();
    	String currentPython2TextFieldValue = configurationModel.getPython2BinPathString();
    	if (!currentPython2TextFieldValue.equals("")) {
    		fileChooser.setCurrentDirectory(new File(currentPython2TextFieldValue));
    	}
    	
        int returnValue = fileChooser.showOpenDialog(this.configurationView.getConfigurationPanel());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
        	String pythonPathString = fileChooser.getSelectedFile().getAbsolutePath();
            if (isValidPythonBinary(pythonPathString, "Python 2", true)) {
            	configurationModel.setPython2BinPathString(pythonPathString);
            }
        }
    }
    
    private void choosePython3ButtonAction(ActionEvent e) {
    	JFileChooser fileChooser = new JFileChooser();
    	String currentPython3TextFieldValue = configurationModel.getPython3BinPathString();
    	if (!currentPython3TextFieldValue.equals("")) {
    		fileChooser.setCurrentDirectory(new File(currentPython3TextFieldValue));
    	}
    	
        int returnValue = fileChooser.showOpenDialog(this.configurationView.getConfigurationPanel());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
        	String pythonPathString = fileChooser.getSelectedFile().getAbsolutePath();
            if (isValidPythonBinary(pythonPathString, "Python 3", true) && isPythonPackageInstalled(pythonPathString, "Python 3", "lief")) {
            	configurationModel.setPython3BinPathString(pythonPathString);
            }
        }
    }
    
    private void autoDetectPythonBinsButtonAction(ActionEvent e) {
    	logUtil.logMessage("Trying to automatically detect required Python binaries...");

    	String python2Path = getPythonInstallPath("python2");
    	if (python2Path == null) {
        	logUtil.logMessage("Auto-detected Python 2 binary failed.");
    	} else {
            if (isValidPythonBinary(python2Path, "Python 2", false)) {
            	configurationModel.setPython2BinPathString(python2Path);
            }
    	}
    	
    	String python3Path = getPythonInstallPath("python3");
    	if (python3Path ==  null) {
        	logUtil.logMessage("Auto-detected Python 2 binary failed.");
    	} else {
            if (isValidPythonBinary(python3Path, "Python 3", false) && isPythonPackageInstalled(python3Path, "Python 3", "lief")) {
            	configurationModel.setPython3BinPathString(python3Path);
            }
    	}
    	
    	if (python2Path == null && python3Path == null) {
    		JOptionPane.showMessageDialog(null, "Auto-detecting Python binaries failed. Please, provide the required binaries manually.", "Perquesits Error", JOptionPane.ERROR_MESSAGE);
    	} else if (python2Path == null) {
    		JOptionPane.showMessageDialog(null, "Auto-detecting Python 2 binary failed. Please, provide the required binary manually.", "Perquesits Error", JOptionPane.ERROR_MESSAGE);
    	} else if (python3Path == null) {
    		JOptionPane.showMessageDialog(null, "Auto-detecting Python 3 binary failed. Please, provide the required binary manually.", "Perquesits Error", JOptionPane.ERROR_MESSAGE);
    	}
    }
    
    // choose files action
    @FunctionalInterface
    interface StringSetter {
        void set(String value);
    }
    
    private void chooseFileButtonAction(ActionEvent e, String oldValueFilePath, StringSetter setter, Boolean isDir) {
    	JFileChooser fileChooser = new JFileChooser();
		String currentProgramPath = this.sandBlasterPlugin.getCurrentProgramPath();
		logUtil.logMessage(currentProgramPath);
		
    	if (!oldValueFilePath.equals("")) {
    		fileChooser.setCurrentDirectory(new File(oldValueFilePath));
    	} else if (currentProgramPath != null) {
    		File file = new File(currentProgramPath);
    		fileChooser.setCurrentDirectory(file);
		}
    	
		fileChooser.setAcceptAllFileFilterUsed(false);
    	if (isDir) {
    		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	} else {
    		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	}
		
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            setter.set(selectedFile.getAbsolutePath());
        }
    }
    
    // specify iOS Version
    private class iOSVersionTextFieldDocumentListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			configurationModel.setiOSVersionString("");
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
		}
    }
    
    private void okButtonAction(ActionEvent e) {
        String iOSVersionString = configurationView.getiOSVersionTextField().getText();
        
        if (iOSVersionString.equals("")) {
        	logUtil.logMessage("iOS Version is not specified!");
            JOptionPane.showMessageDialog(configurationView.getConfigurationPanel(), "Please, specify a valid iOS version.");
            return;
        }
        
        if (!Utilities.isIOSVersionValid(iOSVersionString)) {
        	logUtil.logMessage("Invalid iOS Version: " + iOSVersionString);
            JOptionPane.showMessageDialog(configurationView.getConfigurationPanel(), "Please, specify a valid iOS version.");
            return;
        }
        
        logUtil.logMessage("iOS Version: " + iOSVersionString);
        configurationModel.setiOSVersionString(iOSVersionString);
    }

    // control buttons
    private void restoreButtons() {
		configurationView.getAutoDetectButton().setEnabled(true);
		
		if (!configurationModel.getPython2BinPathString().equals("")) {
			configurationView.getPython2BinChooseButton().setEnabled(true);
		}
		
		if (!configurationModel.getPython3BinPathString().equals("")) {
			configurationView.getPython3BinChooseButton().setEnabled(true);
		}
		
		if (!configurationModel.getiOSVersionString().equals("")) {
			configurationView.getiOSVersionTextField().setEnabled(true);
			configurationView.getiOSVersionOkButton().setEnabled(true);
		}
		
		if (!configurationModel.getKernelExtFilePathString().equals("")) {
			configurationView.getKernelExtFileChooseButton().setEnabled(true);
		}
		
		if (!configurationModel.getSandboxdFilePathString().equals("")) {
			configurationView.getSandboxdFileChooseButton().setEnabled(true);
		}
		
		if (!configurationModel.getKernelCacheFilePathString().equals("")) {
			configurationView.getKernelCacheFileChooseButton().setEnabled(true);
		}
		
		configurationView.getStartButton().setEnabled(true);
    	configurationView.getCancelButton().setEnabled(false);
    	configurationView.getOutDirPathChooseButton().setEnabled(true);

    }
    
    private void prepareButtons() {
    	configurationView.getAutoDetectButton().setEnabled(false);
    	configurationView.getPython2BinChooseButton().setEnabled(false);
    	configurationView.getPython3BinChooseButton().setEnabled(false);
    	configurationView.getiOSVersionOkButton().setEnabled(false);
    	configurationView.getKernelExtFileChooseButton().setEnabled(false);
    	configurationView.getSandboxdFileChooseButton().setEnabled(false);
    	configurationView.getKernelCacheFileChooseButton().setEnabled(false);
    	configurationView.getStartButton().setEnabled(false);
    	configurationView.getCancelButton().setEnabled(true);
    	configurationView.getOutDirPathChooseButton().setEnabled(false);
    }
    
    private void startButtonAction(ActionEvent e) {
    	
    	SwingUtilities.invokeLater(() -> {
    		prepareButtons();
    		logUtil.logMessage("Process started.....");
            configurationView.getProgressBar().setIndeterminate(true);  // Makes the progress bar indeterminate
            configurationView.getProgressBar().setValue(100);
    	});
    	
		sandBlasterBackgroundTask = new SandBlasterBackgroundTask(this);
        sandBlasterBackgroundTask.execute();
    }
    
    private void cancelButtonAction(ActionEvent e) {
    	if (sandBlasterBackgroundTask != null) {
    		sandBlasterBackgroundTask.cancel(true);
    	}
    	configurationView.getProgressBar().setIndeterminate(false);
    	configurationView.getProgressBar().setValue(0);
    }
    
    public void taskDone(String resultDirPathString) {
    	logMessage("Process completed!");
    	
        configurationView.getProgressBar().setIndeterminate(false);  // Makes the progress bar indeterminate
    	restoreButtons();
    	this.resultModel.loadTreeData(new File(resultDirPathString));
    }
    
    public void logMessage(String msg) {
    	this.logUtil.logMessage(msg);
    }
}
