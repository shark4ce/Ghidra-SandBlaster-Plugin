package sandblasterplugin.controllers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import sandblasterplugin.enums.PropertyChangeEventNames;
import sandblasterplugin.models.ResultModel;
import sandblasterplugin.utils.Utilities;
import sandblasterplugin.views.ResultView;

public class ResultController implements PropertyChangeListener{
    private ResultModel resultModel;
    private ResultView resultView;

    public ResultController(ResultModel resultModel, ResultView resultView) {
        this.resultModel = resultModel;
        this.resultView = resultView;
        initListeners();
    }

    private void initListeners() {
    	resultModel.addPropertyChangeListener(this);
    	
    	resultView.getDirectoryTree().setCellRenderer(new DirectoryTreeCellRenderer());
    	resultView.getDirectoryTree().addTreeSelectionListener(this::previewFilesAction);
    	
    	resultView.getOpenDirButton().addActionListener(this::openButtonAction);
    	resultView.getRefreshButton().addActionListener(this::refreshButtonAction);
    }
    
    private void openButtonAction(ActionEvent e) {
    	JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose Directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) resultView.getDirectoryTree().getModel().getRoot();
        if (rootNode != null && rootNode.getUserObject() instanceof File) {
	        File currentDirectoryFile = (File)rootNode.getUserObject();
			if (currentDirectoryFile != null && currentDirectoryFile.exists()) {
				fileChooser.setCurrentDirectory(currentDirectoryFile);
			}
        }
	    
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            resultModel.loadTreeData(selectedFile);
        }
    }
    
    private void refreshButtonAction(ActionEvent e) {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) resultView.getDirectoryTree().getModel().getRoot();
		if (rootNode != null && rootNode.getUserObject() instanceof File) {
	        File currentDirectoryFile = (File)rootNode.getUserObject();
			if (currentDirectoryFile != null) {
	            resultModel.loadTreeData(currentDirectoryFile);
			}
		}
    }
    
	public class DirectoryTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            
            if (node.getUserObject() instanceof File && ((File)node.getUserObject()).exists()) {
                File file = (File) node.getUserObject();
                if (node.isRoot()) {
                	setText(file.getAbsolutePath());
                } else {
                	setText(file.getName());
                }
                
                if (file.isDirectory()) {
                    setIcon(getClosedIcon()); // Use the directory (closed) icon irrespective of the fact if directory is empty or not
                } else {
                    setIcon(getLeafIcon());
                }
            }
            return this;
        }
    }
    
	private void previewFilesAction(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) resultView.getDirectoryTree().getLastSelectedPathComponent();

        if (node == null || node.isRoot() || !node.isLeaf())
            return;
        
        File nodeFile = (File) node.getUserObject();
	    if (!nodeFile.exists()) {
        	resultView.displayWarning("Warning: The selected file/directory was deleted. Please, refresh.");
        	return;
	    }
	    
	    if (!nodeFile.isDirectory()) {
	    	String content = Utilities.readTextFile(nodeFile);
	        if (content == null) {
	        	resultModel.setFileContentString("");
	        	resultView.displayWarning("Warning: The selected file is not a text file.");
	            return;
	        }
	    	resultModel.setFileContentString(content);
	    }
    }
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
        if (PropertyChangeEventNames.TREE_ROOT_NODE_UPDATED.getEventName().equals(evt.getPropertyName()) && evt.getNewValue() != null) {
        	JTree directoryTree = resultView.getDirectoryTree();
        	((DefaultTreeModel) directoryTree.getModel()).setRoot((DefaultMutableTreeNode) evt.getNewValue());
            if(!directoryTree.isRootVisible()) {
            	directoryTree.setRootVisible(true);
            }
        } else if (PropertyChangeEventNames.FILE_CONTENT_UPDATED.getEventName().equals(evt.getPropertyName()) && evt.getNewValue() != null) {
        	resultView.getFileContentArea().setText((String)evt.getNewValue());
        }
	}
}
