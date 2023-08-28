package sandblasterplugin.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JTextArea;

import ghidra.framework.Application;
import ghidra.framework.options.ToolOptions;
import sandblasterplugin.SandBlasterPlugin;
import sandblasterplugin.enums.PropertyChangeEventNames;
import sandblasterplugin.enums.UserPreferenceKey;
import sandblasterplugin.utils.Utilities;

public class ConfigurationModel {
	
	private String python2BinPathString;
	private String python3BinPathString;
	
	private String iOSVersionString;
	private String sandboxOperationsFilePathString;
	private String sandboxProfilesFilePathString;
	private String outDirPathString;
	private String pluginLogFilePathString;
	
	private JTextArea logTextArea;
	
	private boolean isDataBundle;
	
    private PrintWriter logFileWriter;
    private ToolOptions toolOptions;
    private PropertyChangeSupport support;

	public ConfigurationModel(JTextArea logTextArea, SandBlasterPlugin sandBlasterPlugin) throws IOException {
		this.python2BinPathString = "";
		this.python3BinPathString = "";
		this.iOSVersionString = "";
		this.sandboxOperationsFilePathString = "";
		this.sandboxProfilesFilePathString = "";
		this.outDirPathString = "";
		
		this.logTextArea = logTextArea;
		this.toolOptions = sandBlasterPlugin.getToolOptions();

	    String pluinLogOutDir = Utilities.createDir(Application.getMyModuleRootDirectory().getAbsolutePath(), "logs");
	    this.pluginLogFilePathString = String.join(File.separator, pluinLogOutDir, "sandblaster-project-" + sandBlasterPlugin.getProjecName() + ".log");
        this.logFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(pluginLogFilePathString), true)));

	    this.support = new PropertyChangeSupport(this);
	}
	
    public void logMessage(String msg) {
    	String currentTimeString = Utilities.getCurrentTime("yyyy-MM-dd HH:mm:ss");
    	String message = "(" + currentTimeString + ") >>> " + msg;
    	this.logTextArea.append(message + "\n");
    	this.logTextArea.setCaretPosition(logTextArea.getDocument().getLength()); // Scroll to the end    	
        this.logFileWriter.println(message);
        this.logFileWriter.flush();
        
    }
	
	public void fetchUserPreferences() {
		String python2BinPath = toolOptions.getString(UserPreferenceKey.PYTHON2_BIN_PATH.getKeyName(), null);
		if (python2BinPath != null) {
			this.setPython2BinPathString(python2BinPath);
		}
		
		String python3BinPath = toolOptions.getString(UserPreferenceKey.PYTHON3_BIN_PATH.getKeyName(), null);
		if (python3BinPath != null) {
			this.setPython3BinPathString(python3BinPath);
		}
		
		String outDirPath = toolOptions.getString(UserPreferenceKey.OUT_DIR_PATH.getKeyName(), null);
		if (outDirPath != null) {
			this.setOutDirPathString(outDirPath);
		}
	}
    
	public boolean isDataBundle() {
		return isDataBundle;
	}
	
	public void setDataBundleFlag(boolean value) {
		isDataBundle = value;
	}
	
	public String getPluginLogFilePathString() {
		return pluginLogFilePathString;
	}
	
	public String getPython2BinPathString() {
		return python2BinPathString;
	}
	
	public void setPython2BinPathString(String python2BinPathString) {
    	String oldPath = this.python2BinPathString;

        if (!python2BinPathString.equals(oldPath)) {
	        this.python2BinPathString = python2BinPathString;
	    	this.toolOptions.setString(UserPreferenceKey.PYTHON2_BIN_PATH.getKeyName(), python2BinPathString);
	        this.support.firePropertyChange(PropertyChangeEventNames.PYTHON2_BIN_PATH_UPDATED.getEventName(), oldPath, python2BinPathString);
        }
	}
	
	public String getPython3BinPathString() {
		return python3BinPathString;
	}
	
	public void setPython3BinPathString(String python3BinPathString) {
        String oldPath = this.python3BinPathString;
        
        if (!python3BinPathString.equals(oldPath)) {
	        this.python3BinPathString = python3BinPathString;
	    	this.toolOptions.setString(UserPreferenceKey.PYTHON3_BIN_PATH.getKeyName(), python3BinPathString);
	        this.support.firePropertyChange(PropertyChangeEventNames.PYTHON3_BIN_PATH_UPDATED.getEventName(), oldPath, python3BinPathString);
        }
	}
	
	public String getiOSVersionString() {
		return iOSVersionString;
	}
	
	public void setiOSVersionString(String iOSVersionString) {
        String oldPath = this.iOSVersionString;
        
        if (!iOSVersionString.equals(oldPath)) {
            this.iOSVersionString = iOSVersionString;
            support.firePropertyChange(PropertyChangeEventNames.IOS_VERSION_VALUE_UPDATED.getEventName(), oldPath, iOSVersionString);
        }
	}
	
	public String getSandboxOperationsFilePathString() {
		return sandboxOperationsFilePathString;
	}
	
	public void setSandboxOperationsFilePathString(String sandboxOperationsFilePathString) {
        String oldPath = this.sandboxOperationsFilePathString;
        
        
        this.sandboxOperationsFilePathString = sandboxOperationsFilePathString;
        support.firePropertyChange(PropertyChangeEventNames.SANDBOX_OPERATIONS_FILE_PATH_UPDATED.getEventName(), oldPath, sandboxOperationsFilePathString);
	}
	
	public String getSandboxProfilesFilePathString() {
		return sandboxProfilesFilePathString;
	}
	
	public void setSandboxProfilesFilePathString(String sandboxProfilesFilePathString) {
        String oldPath = this.sandboxProfilesFilePathString;
        
        if (!sandboxProfilesFilePathString.equals(oldPath)) {
	        this.sandboxProfilesFilePathString = sandboxProfilesFilePathString;
	        support.firePropertyChange(PropertyChangeEventNames.SANDBOX_PROFILES_FILE_PATH_UPDATED.getEventName(), oldPath, sandboxProfilesFilePathString);
        }
	}

	public String getOutDirPathString() {
		return outDirPathString;
	}
	
	public void setOutDirPathString(String outDirPathString) {
		
        String oldPath = this.outDirPathString;
        
        if (!outDirPathString.equals(oldPath)) {
	        this.outDirPathString = outDirPathString;
	    	this.toolOptions.setString(UserPreferenceKey.OUT_DIR_PATH.getKeyName(), outDirPathString);
	        support.firePropertyChange(PropertyChangeEventNames.OUT_DIR_PATH_UPDATED.getEventName(), oldPath, outDirPathString);
        }
	}
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
