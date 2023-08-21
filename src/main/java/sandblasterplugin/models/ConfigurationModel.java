package sandblasterplugin.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ghidra.framework.model.Project;
import ghidra.framework.plugintool.PluginTool;
import ghidra.program.model.listing.Program;

public class ConfigurationModel {
	
	
	String pluginName;
	private PluginTool tool;
    private Program currentProgram;
    
	private String python2BinPathString;
	private String python3BinPathString;
	
	private String iOSVersionString;
	private String kernelExtFilePathString;
	private String sandboxdFilePathString;
	private String kernelCacheFilePathString;
	private String outDirPathString;
	
    private PropertyChangeSupport support;

	
	public ConfigurationModel(String pluginName) {
		this.pluginName = pluginName;
		this.python2BinPathString = "";
		this.python3BinPathString = "";
		this.iOSVersionString = "";
		this.kernelExtFilePathString = "";
		this.sandboxdFilePathString = "";
		this.kernelCacheFilePathString = "";
		this.outDirPathString = "";
		this.support = new PropertyChangeSupport(this);
	}
    
    public String getPluginName() {
    	return pluginName;
    }
	
    public PluginTool getPluginTool() {
		return tool;
	}
	public void setPluginTool(PluginTool tool) {
		this.tool = tool;
	}
	public Program getCurrentProgram() {
		return currentProgram;
	}
	public void setCurrentProgram(Program currentProgram) {
		this.currentProgram = currentProgram;
	}
	
	public String getPython2BinPathString() {
		return python2BinPathString;
	}
	public void setPython2BinPathString(String python2BinPathString) {
        String oldPath = this.python2BinPathString;
        this.python2BinPathString = python2BinPathString;
        support.firePropertyChange("python2BinPathString", oldPath, python2BinPathString);
	}
	public String getPython3BinPathString() {
		return python3BinPathString;
	}
	public void setPython3BinPathString(String python3BinPathString) {
        String oldPath = this.python3BinPathString;
        this.python3BinPathString = python3BinPathString;
        support.firePropertyChange("python3BinPathString", oldPath, python3BinPathString);
	}
	public String getiOSVersionString() {
		return iOSVersionString;
	}
	public void setiOSVersionString(String iOSVersionString) {
        String oldPath = this.iOSVersionString;
        
        if (!iOSVersionString.equals(oldPath)) {
            this.iOSVersionString = iOSVersionString;
            support.firePropertyChange("iOSVersionString", oldPath, iOSVersionString);
        }
	}
	public String getKernelExtFilePathString() {
		return kernelExtFilePathString;
	}
	public void setKernelExtFilePathString(String kernelExtFilePathString) {
        String oldPath = this.kernelExtFilePathString;
        this.kernelExtFilePathString = kernelExtFilePathString;
        support.firePropertyChange("kernelExtFilePathString", oldPath, kernelExtFilePathString);
	}
	public String getSandboxdFilePathString() {
		return sandboxdFilePathString;
	}
	public void setSandboxdFilePathString(String sandboxdFilePathString) {
        String oldPath = this.sandboxdFilePathString;
        this.sandboxdFilePathString = sandboxdFilePathString;
        support.firePropertyChange("sandboxdFilePathString", oldPath, sandboxdFilePathString);
	}
	
	public String getKernelCacheFilePathString() {
		return kernelCacheFilePathString;
	}


	public void setKernelCacheFilePathString(String kernelCacheFilePathString) {
        String oldPath = this.kernelCacheFilePathString;
        this.kernelCacheFilePathString = kernelCacheFilePathString;
        support.firePropertyChange("kernelCacheFilePathString", oldPath, kernelCacheFilePathString);
	}

	public String getOutDirPathString() {
		return outDirPathString;
	}
	public void setOutDirPathString(String outDirPathString) {
		
        String oldPath = this.outDirPathString;
        this.outDirPathString = outDirPathString;
        support.firePropertyChange("outDirPathString", oldPath, outDirPathString);
	}
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
