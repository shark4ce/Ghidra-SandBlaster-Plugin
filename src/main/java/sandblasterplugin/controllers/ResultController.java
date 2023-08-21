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
import javax.swing.tree.TreeModel;

import sandblasterplugin.Utilities;
import sandblasterplugin.models.ResultModel;
import sandblasterplugin.views.ResultView;

public class ResultController implements PropertyChangeListener{
    private ResultModel model;
    private ResultView view;

    public ResultController(ResultModel model, ResultView view) {
        this.model = model;
        this.view = view;
        initListeners();
    }

    private void initListeners() {
    	model.addPropertyChangeListener(this);
    	
    	view.getDirectoryTree().setCellRenderer(new DefaultTreeCellRenderer());
    	view.getDirectoryTree().addTreeSelectionListener(this::previewFilesAction);
    	
    	
    	view.getOpenDirButton().addActionListener(this::openButtonAction);
    	view.getRefreshButton().addActionListener(this::refreshButtonAction);
    }
    
    
    private void previewFilesAction(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) view.getDirectoryTree().getLastSelectedPathComponent();

        if (node == null || node.isRoot() || !node.isLeaf())
            return;
        
        File nodeFile = (File) node.getUserObject();
        if (!nodeFile.isDirectory()) {
        	String content = Utilities.readTextFile(nodeFile);
            if (content == null) {
            	model.setFileContentString("");
            	view.displayWarning("Warning: The selected file is not a text file.");
                return;
            }
        	model.setFileContentString(content);
        }
    }
    
    private void openButtonAction(ActionEvent e) {
    	JFileChooser fileChooser;
		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose Directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);  // Disable the "All files" option.
		
//		
//	    TreeModel treeModel = view.getDirectoryTree().getModel();
//	    if (treeModel != null) {
//	        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
//	        if (rootNode != null) {
//		        File currentDirectoryFile = (File)rootNode.getUserObject();
//				if (currentDirectoryFile != null) {
//					fileChooser.setCurrentDirectory(currentDirectoryFile);
//				}
//	        }
//			
//	    }
	    
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            model.loadDirectory(selectedFile);        }
    }
    
    private void refreshButtonAction(ActionEvent e) {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) view.getDirectoryTree().getModel().getRoot();
		if (rootNode != null) {
	        File currentDirectoryFile = (File)rootNode.getUserObject();
			if (currentDirectoryFile != null) {
				model.loadDirectory(currentDirectoryFile);
			}
		}
    }
    
	public class DirectoryTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            if (node.getUserObject() instanceof File) {
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
        if ("treeModel".equals(evt.getPropertyName()) && evt.getNewValue() != null) {
//        	JTree directoryTree = view.getDirectoryTree();
        	view.getDirectoryTree().setModel((DefaultTreeModel) evt.getNewValue());

//            directoryTree.setVisible(true);
        } else if ("fileContentString".equals(evt.getPropertyName()) && evt.getNewValue() != null) {
        	view.getFileContentArea().setText((String)evt.getNewValue());
        }
	}
}
