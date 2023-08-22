package sandblasterplugin.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ghidra.framework.options.ToolOptions;
import sandblasterplugin.enums.PropertyChangeEventNames;
import sandblasterplugin.enums.UserPreferenceKey;

public class ConfigurationModel {
	
	private String python2BinPathString;
	private String python3BinPathString;
	
	private String iOSVersionString;
	private String kernelExtFilePathString;
	private String sandboxdFilePathString;
	private String kernelCacheFilePathString;
	private String outDirPathString;
	
	private ToolOptions toolOptions;
    private PropertyChangeSupport support;

	public ConfigurationModel(ToolOptions toolOptions) {
		this.python2BinPathString = "";
		this.python3BinPathString = "";
		this.iOSVersionString = "";
		this.kernelExtFilePathString = "";
		this.sandboxdFilePathString = "";
		this.kernelCacheFilePathString = "";
		this.outDirPathString = "";
		this.toolOptions = toolOptions;
		this.support = new PropertyChangeSupport(this);
	}
	
	public void fetchUserPreferences() {
		String python2BinPath = this.toolOptions.getString(UserPreferenceKey.PYTHON2_BIN_PATH.getKeyName(), null);
		if (python2BinPath != null) {
			this.setPython2BinPathString(python2BinPath);
		}
		
		String python3BinPath = this.toolOptions.getString(UserPreferenceKey.PYTHON3_BIN_PATH.getKeyName(), null);
		if (python3BinPath != null) {
			this.setPython3BinPathString(python3BinPath);
		}
		
		String outDirPath = this.toolOptions.getString(UserPreferenceKey.OUT_DIR_PATH.getKeyName(), null);
		if (outDirPath != null) {
			this.setOutDirPathString(outDirPath);
		}
	}
    
	public String getPython2BinPathString() {
		return python2BinPathString;
	}
	
	public void setPython2BinPathString(String python2BinPathString) {
        String oldPath = this.python2BinPathString;
        this.python2BinPathString = python2BinPathString;
    	this.toolOptions.setString(UserPreferenceKey.PYTHON2_BIN_PATH.getKeyName(), python2BinPathString);
        this.support.firePropertyChange(PropertyChangeEventNames.PYTHON2_BIN_PATH_UPDATED.getEventName(), oldPath, python2BinPathString);
	}
	
	public String getPython3BinPathString() {
		return python3BinPathString;
	}
	
	public void setPython3BinPathString(String python3BinPathString) {
        String oldPath = this.python3BinPathString;
        this.python3BinPathString = python3BinPathString;
    	this.toolOptions.setString(UserPreferenceKey.PYTHON3_BIN_PATH.getKeyName(), python3BinPathString);
        this.support.firePropertyChange(PropertyChangeEventNames.PYTHON3_BIN_PATH_UPDATED.getEventName(), oldPath, python3BinPathString);
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
	
	public String getKernelExtFilePathString() {
		return kernelExtFilePathString;
	}
	
	public void setKernelExtFilePathString(String kernelExtFilePathString) {
        String oldPath = this.kernelExtFilePathString;
        this.kernelExtFilePathString = kernelExtFilePathString;
        support.firePropertyChange(PropertyChangeEventNames.KERNEL_EXT_FILE_PATH_UPDATED.getEventName(), oldPath, kernelExtFilePathString);
	}
	
	public String getSandboxdFilePathString() {
		return sandboxdFilePathString;
	}
	public void setSandboxdFilePathString(String sandboxdFilePathString) {
        String oldPath = this.sandboxdFilePathString;
        this.sandboxdFilePathString = sandboxdFilePathString;
        support.firePropertyChange(PropertyChangeEventNames.SANDBOXD_FILE_PATH_UPDATED.getEventName(), oldPath, sandboxdFilePathString);
	}
	
	public String getKernelCacheFilePathString() {
		return kernelCacheFilePathString;
	}


	public void setKernelCacheFilePathString(String kernelCacheFilePathString) {
        String oldPath = this.kernelCacheFilePathString;
        this.kernelCacheFilePathString = kernelCacheFilePathString;
        support.firePropertyChange(PropertyChangeEventNames.KERNEL_CACHE_FILE_PATH_UPDATED.getEventName(), oldPath, kernelCacheFilePathString);
	}

	public String getOutDirPathString() {
		return outDirPathString;
	}
	public void setOutDirPathString(String outDirPathString) {
		
        String oldPath = this.outDirPathString;
        this.outDirPathString = outDirPathString;
    	this.toolOptions.setString(UserPreferenceKey.OUT_DIR_PATH.getKeyName(), outDirPathString);
        support.firePropertyChange(PropertyChangeEventNames.OUT_DIR_PATH_UPDATED.getEventName(), oldPath, outDirPathString);
	}
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
