package sandblasterplugin.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;

import ghidra.app.util.bin.format.dwarf4.DIEAggregate;
import ghidra.util.Msg;
import sandblasterplugin.enums.PropertyChangeEventNames;

public class ResultModel {
	
    private DefaultMutableTreeNode rootNode;
    private String fileContentString;
	private PropertyChangeSupport support;
	
	public ResultModel () {
        this.rootNode = null;
		this.support = new PropertyChangeSupport(this);
	}
	
    public DefaultMutableTreeNode getRootNode() {
        return rootNode;
    }
    
    private void populateRootNode(File dir, DefaultMutableTreeNode node) {
        for (File file : dir.listFiles()) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file);
            node.add(childNode);
            if (file.isDirectory()) {
            	populateRootNode(file, childNode);
            }
        }
    }
    
    public void loadTreeData(File dir) {
        DefaultMutableTreeNode oldRootNode = this.rootNode;
        DefaultMutableTreeNode newRootNode = new DefaultMutableTreeNode(dir);
        if (dir != null) {
            populateRootNode(dir, newRootNode);
            this.rootNode = newRootNode;
        } else {
            this.rootNode = null;
        }
        support.firePropertyChange(PropertyChangeEventNames.TREE_ROOT_NODE_UPDATED.getEventName(), oldRootNode, this.rootNode);
    }
    
	public String getFileContentString() {
		return fileContentString;
	}
	
	public void setFileContentString(String fileContentString) {
		String oldFileContentString = this.fileContentString;
		this.fileContentString = fileContentString;
        support.firePropertyChange(PropertyChangeEventNames.FILE_CONTENT_UPDATED.getEventName(), oldFileContentString, fileContentString);

	}
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
