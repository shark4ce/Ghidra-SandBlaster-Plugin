package sandblasterplugin.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ResultModel {
	
    private DefaultTreeModel treeModel;

    private String fileContentString;
	private PropertyChangeSupport support;
	
	public ResultModel () {
        this.treeModel = new DefaultTreeModel(null);
		this.support = new PropertyChangeSupport(this);
	}
	
    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

	public String getFileContentString() {
		return fileContentString;
	}
	
	public void setFileContentString(String fileContentString) {
		String oldFileContentString = this.fileContentString;
		this.fileContentString = fileContentString;
        support.firePropertyChange("fileContentString", oldFileContentString, fileContentString);

	}
	
    public void loadDirectory(File dir) {
    	DefaultTreeModel oldTreeModel = treeModel;
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(dir);
        loadFiles(dir, rootNode);
        treeModel.setRoot(rootNode);
        support.firePropertyChange("treeModel", oldTreeModel, treeModel);
    }
    
    private void loadFiles(File dir, DefaultMutableTreeNode node) {
        for (File file : dir.listFiles()) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file);
            node.add(childNode);
            if (file.isDirectory()) {
                loadFiles(file, childNode);
            }
        }
    }
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

}
