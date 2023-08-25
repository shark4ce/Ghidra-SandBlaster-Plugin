package sandblasterplugin.controllers;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import docking.widgets.filechooser.GhidraFileChooser;
import docking.widgets.filechooser.GhidraFileChooserMode;
import sandblasterplugin.SandBlasterPlugin;
import sandblasterplugin.backgroundtasks.BaseSandBlasterBackgroundTask;
import sandblasterplugin.backgroundtasks.SandBlasterTaskDataFactory;
import sandblasterplugin.backgroundtasks.general.CommandRunnerTask;
import sandblasterplugin.backgroundtasks.general.TaskExecutor;
import sandblasterplugin.enums.PropertyChangeEventNames;
import sandblasterplugin.models.ConfigurationModel;
import sandblasterplugin.models.ResultModel;
import sandblasterplugin.utils.Utilities;
import sandblasterplugin.views.ConfigurationView;



public class ConfigurationController {
	
	private ResultModel resultModel;
	private ConfigurationModel configurationModel;
	private ConfigurationView configurationView;
	private SandBlasterPlugin sandBlasterPlugin;
	private BaseSandBlasterBackgroundTask sandBlasterBackgroundTask;
    private TaskExecutor taskExecutor;


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
    	
    	this.taskExecutor = new TaskExecutor();
    	this.sandBlasterBackgroundTask = null;

    	initListeners();
    	logMessage("Welcome!");
    }
    
    public ConfigurationModel getConfigurationModel() {
		return configurationModel;
	}

	public ConfigurationView getConfigurationView() {
		return configurationView;
	}
	
	public SandBlasterPlugin getSandBlasterPlugin() {
		return this.sandBlasterPlugin;
	}
	
	public BaseSandBlasterBackgroundTask getSandBlasterBackgroundTask() {
		return sandBlasterBackgroundTask;
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
    	this.logMessage("Extracting the absolute path of the '" + pythonBinPath + "' installation directory...");
        CommandRunnerTask cmdRunnerTask = new CommandRunnerTask(new String[] {pythonBinPath, "-c", "import sys; print(sys.executable)"}, this::logMessage);
        return taskExecutor.executeTask(cmdRunnerTask);
    }
    
    private Boolean isValidPythonBinary(String pythonPath, String pythonName, Boolean showErrorPane) {
    	this.logMessage("");
    	this.logMessage("Checking if file: '" + pythonPath + "' is a valid " + pythonName + "...");
        CommandRunnerTask cmdRunnerTask = new CommandRunnerTask(new String[] {pythonPath, "--version"}, this::logMessage);
        String commandOutputString = taskExecutor.executeTask(cmdRunnerTask);
        if(commandOutputString != null) {
        	this.logMessage("The '" + pythonPath + "' is a valid " + pythonName);
        	return true;
        }
        
        String msgString = "The selected file: '" + pythonPath + "' is not a valid " + pythonName + ".";
        this.logMessage(msgString);
        if (showErrorPane) {
        	configurationView.displayError("Perquesits Error", msgString);
        }
        
    	return false;
    }


    private Boolean isPythonPackageInstalled(String pythonPath, String pythonName, String packageName) {
    	this.logMessage("");
    	this.logMessage("Checking if " + pythonName + " has the '" + packageName + "' package installed...");
    	Boolean packageIsInstalled = false;
        CommandRunnerTask cmdRunnerTask = new CommandRunnerTask(new String[] {pythonPath, "-m", "pip", "show", packageName}, this::logMessage);
        String commandOutputString = taskExecutor.executeTask(cmdRunnerTask); 

        
        if (commandOutputString != null && commandOutputString.startsWith("Name: " + packageName)) {
        	this.logMessage(pythonName + " has the required '" + packageName + "' package installed.");
        	return true;
        }
        
        String msgString = "The selected " + pythonName + " does not have required '" + packageName + "' package installed. Please, make sure to install them.";
        this.logMessage(msgString);
        configurationView.displayError("Python 3 Requiremenets Error", msgString);
            
        // ask user to automatically install
        int response = JOptionPane.showConfirmDialog(configurationView.getConfigurationPanel(), "Do you want to try to automatically install " + pythonName + " requirements?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
        	
        	// Try to install package
        	this.logMessage("");
        	this.logMessage("Trying to install '" + packageName + "' package required by " + pythonName + "...");
        	cmdRunnerTask = new CommandRunnerTask(new String[] {pythonPath, "-m", "pip", "install", packageName}, this::logMessage);
            commandOutputString = taskExecutor.executeTask(cmdRunnerTask);
        	
            if (commandOutputString != null) {
            	packageIsInstalled = true;
            }else {
            	String errorMsgString = "Unable to install '" + packageName + "' package required by " + pythonName + ". Please, make sure them to install them and try again.";
                this.logMessage(errorMsgString);
            	configurationView.displayError("Perquesits Error", errorMsgString);
            	packageIsInstalled = false;
            }
        }
        
		return packageIsInstalled;
    }
    
        
    private void choosePython2ButtonAction(ActionEvent e) {
    	GhidraFileChooser fileChooser = new GhidraFileChooser(null);
    	String currentPython2TextFieldValue = configurationModel.getPython2BinPathString();
    	if (!currentPython2TextFieldValue.equals("")) {
    		fileChooser.setCurrentDirectory(new File(currentPython2TextFieldValue));
    	}
    	
        File selectedFile = fileChooser.getSelectedFile();
        if (selectedFile != null) {
        	String pythonPathString = selectedFile.getAbsolutePath();
            if (isValidPythonBinary(pythonPathString, "Python 2", true)) {
            	configurationModel.setPython2BinPathString(pythonPathString);
            }
        }
    }
    
    private void choosePython3ButtonAction(ActionEvent e) {
    	GhidraFileChooser fileChooser = new GhidraFileChooser(null);
    	String currentPython3TextFieldValue = configurationModel.getPython3BinPathString();
    	if (!currentPython3TextFieldValue.equals("")) {
    		fileChooser.setCurrentDirectory(new File(currentPython3TextFieldValue));
    	}
    	
        File selectedFile = fileChooser.getSelectedFile();
        if (selectedFile != null) {
        	String pythonPathString = selectedFile.getAbsolutePath();
            if (isValidPythonBinary(pythonPathString, "Python 3", true) && isPythonPackageInstalled(pythonPathString, "Python 3", "lief")) {
            	configurationModel.setPython3BinPathString(pythonPathString);
            }
        }
    }
    
    private void autoDetectPythonBinsButtonAction(ActionEvent e) {
    	this.logMessage("");
    	this.logMessage("Trying to automatically detect required Python binaries...");

    	
    	// TO DO: change order
    	String python2Path = getPythonInstallPath("python2");
    	if (python2Path != null && isValidPythonBinary(python2Path, "Python 2", false)) {
        	configurationModel.setPython2BinPathString(python2Path);
    	}
    	
    	String python3Path = getPythonInstallPath("python3");
    	if (python3Path !=  null && isValidPythonBinary(python3Path, "Python 3", false) && isPythonPackageInstalled(python3Path, "Python 3", "lief")) {
        	configurationModel.setPython3BinPathString(python3Path);
    	}
    	
    	String msgString = "";
    	if (python2Path == null && python3Path == null) {
    		msgString = "Auto-detecting Python binaries failed. Please, provide the required binaries manually.";
    	} else if (python2Path == null) {
    		msgString = "Auto-detecting Python 2 binary failed. Please, provide the required binary manually.";
    	} else if (python3Path == null) {
    		msgString = "Auto-detecting Python 3 binary failed. Please, provide the required binary manually.";
    	}
    	
    	if (!msgString.equals("")) {
    		this.logMessage(msgString);
    		configurationView.displayError("Perquesits Error", msgString);
    	}
    }
    
    
    // choose files action
    @FunctionalInterface
    interface StringSetter {
        void set(String value);
    }
    
    private void chooseFileButtonAction(ActionEvent e, String oldValueFilePath, StringSetter setter, Boolean isDir) {
    	GhidraFileChooser fileChooser = new GhidraFileChooser(null);
		String currentProgramPath = this.sandBlasterPlugin.getCurrentProgramPath();
		
    	if (!oldValueFilePath.equals("")) {
    		fileChooser.setCurrentDirectory(new File(oldValueFilePath));
    	} else if (currentProgramPath != null) {
    		File file = new File(currentProgramPath);
    		fileChooser.setCurrentDirectory(file);
		}
    	
    	if (isDir) {
    		fileChooser.setFileSelectionMode(GhidraFileChooserMode.DIRECTORIES_ONLY);
    	} else {
    		fileChooser.setFileSelectionMode(GhidraFileChooserMode.FILES_ONLY);
    	}
		
        File selectedFile = fileChooser.getSelectedFile();
        if (selectedFile != null) {
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
			configurationModel.setKernelExtFilePathString("");
			configurationModel.setSandboxdFilePathString("");
			configurationModel.setKernelCacheFilePathString("");
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
		}
    }
    
    private void okButtonAction(ActionEvent e) {
        String iOSVersionString = configurationView.getiOSVersionTextField().getText();
        
    	this.logMessage("");
        if (iOSVersionString.equals("")) {
        	this.logMessage("iOS Version is not specified!");
            JOptionPane.showMessageDialog(configurationView.getConfigurationPanel(), "Please, specify a valid iOS version.");
            return;
        }
        
        if (!Utilities.isIOSVersionValid(iOSVersionString)) {
        	this.logMessage("Invalid iOS Version: " + iOSVersionString);
            JOptionPane.showMessageDialog(configurationView.getConfigurationPanel(), "Please, specify a valid iOS version.");
            return;
        }
        
        this.logMessage("Specified iOS Version: " + iOSVersionString);
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
    	
		try {
			sandBlasterBackgroundTask = SandBlasterTaskDataFactory.createBackgroundTask(this);
			
	    	SwingUtilities.invokeLater(() -> {
	    		prepareButtons();
	    		this.logMessage("Process started.....");
	            configurationView.getProgressBar().setValue(100);
	            configurationView.getProgressBar().setIndeterminate(true);  // Makes the progress bar indeterminate
	    	});
	    	
	        sandBlasterBackgroundTask.execute(); 
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			configurationView.displayError("Error", exception.getMessage());
			logMessage(exception.getCause().getMessage());
		}
    }
    
    private void cancelButtonAction(ActionEvent e) {
        // re-confirm
        int response = JOptionPane.showConfirmDialog(configurationView.getConfigurationPanel(), "Are you sure you want to cancel the process?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
	    	cancelSandBlasterBackgroundTask();
        }
    }
    
    public void cancelSandBlasterBackgroundTask() {
    	if (sandBlasterBackgroundTask != null) {
    		sandBlasterBackgroundTask.cancel(true);
    	}
    }
    
    public void taskDone(String msgString, String resultDirPathString) {
    	restoreButtons();
    	resultModel.loadTreeData(new File(resultDirPathString));
        configurationView.getProgressBar().setIndeterminate(false);  // Makes the progress bar indeterminate
    	configurationView.displaySuccess("Success", msgString);
    }
    
    public void taskFailed(String errorMsgString) {
    	restoreButtons();
    	configurationView.getProgressBar().setIndeterminate(false);
    	configurationView.getProgressBar().setValue(0);
    	configurationView.displayError("Error", errorMsgString);
    }
    
    public void logMessage(String msg) {
    	configurationModel.logMessage(msg);
    }
}
