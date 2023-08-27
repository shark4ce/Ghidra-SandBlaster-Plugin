package sandblasterplugin.controllers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import docking.widgets.filechooser.GhidraFileChooser;
import docking.widgets.filechooser.GhidraFileChooserMode;
import sandblasterplugin.backgroundtasks.general.FileReaderTask;
import sandblasterplugin.backgroundtasks.general.TaskExecutor;
import sandblasterplugin.enums.PropertyChangeEventNames;
import sandblasterplugin.models.ResultModel;
import sandblasterplugin.views.ResultView;

public class ResultController implements PropertyChangeListener{
    private ResultModel resultModel;
    private ResultView resultView;
    private TaskExecutor taskExecutor;
    
    public ResultController(ResultModel resultModel, ResultView resultView) {
        this.resultModel = resultModel;
        this.resultView = resultView;
        this.taskExecutor = new TaskExecutor();
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
    	GhidraFileChooser fileChooser = new GhidraFileChooser(null);
		fileChooser.setFileSelectionMode(GhidraFileChooserMode.DIRECTORIES_ONLY);

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) resultView.getDirectoryTree().getModel().getRoot();
        if (rootNode != null && rootNode.getUserObject() instanceof File) {
	        File currentDirectoryFile = (File)rootNode.getUserObject();
			if (currentDirectoryFile != null && currentDirectoryFile.exists()) {
				fileChooser.setCurrentDirectory(currentDirectoryFile);
			}
        }
	    
        File selectedFile = fileChooser.getSelectedFile();
        if (selectedFile != null) {
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
                    setIcon(getClosedIcon());
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
        	resultView.displayWarning("The selected file/directory was deleted. Please, refresh.");
        	return;
	    }
	   
	    
	    if (!nodeFile.isDirectory()) {
        	resultModel.setFileContentString("");
		    String fileContentString =  taskExecutor.executeTask(new FileReaderTask(nodeFile, null));
		    
	        if (fileContentString != null) {
	        	resultModel.setFileContentString(fileContentString);
	        } else {
	        	resultView.displayWarning("Warning: The selected file is not a text file.");
	        }
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
