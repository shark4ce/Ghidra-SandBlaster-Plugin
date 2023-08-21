package sandblasterplugin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import java.io.File;



public class ResultPanel extends JPanel {
	
	private JPanel treePanel;
	private JTree dirTree;
	private JButton refreshButton;
	private JButton openDirButton;
	private JTextPane fileContentArea;
    private DefaultTreeModel treeModel;
    private File currentDirectory;


	
	public ResultPanel(File initialDirectory) {
		initGUI(initialDirectory);
	}
	
	private void initGUI(File initialDirectory) {
		// prepare Directory Tree
		
		treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Root"));
		dirTree = new JTree(treeModel);
        dirTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        dirTree.setCellRenderer(new DirectoryTreeCellRenderer());
    	dirTree.addTreeSelectionListener(this::previewFilesAction);
    	
    	treePanel = new JPanel(new BorderLayout());
    	treePanel.add(new JScrollPane(dirTree), BorderLayout.CENTER);
    	
    	if (initialDirectory != null) {
    		populateTree(initialDirectory);
    	} else {
    		setVisible(false);
    	}
    	
		// prepare toolbar with buttons
//    	ImageIcon refreshButtonIcon = new ImageIcon("images/refresh_icon.png");
        refreshButton = new JButton("Refresh");
        refreshButton.setToolTipText("Refresh"); // This sets the tooltip text
//        refreshButton.setPreferredSize(new Dimension(70, 70)); // 24x24 pixels, adjust as needed
        refreshButton.addActionListener(this::refreshButtonAction);
        
		// prepare toolbar with buttons
//    	ImageIcon refreshButtonIcon = new ImageIcon("images/refresh_icon.png");
    	openDirButton = new JButton("Open");
    	openDirButton.setToolTipText("Refresh"); // This sets the tooltip text
//    	openDirButton.setPreferredSize(new Dimension(70, 70)); // 24x24 pixels, adjust as needed
    	openDirButton.addActionListener(this::openButtonAction);
        
        
        // create toolbar and add button
        JToolBar toolBar = new JToolBar("My Toolbar");
        toolBar.add(openDirButton);
        toolBar.add(refreshButton);
    	treePanel.add(toolBar, BorderLayout.PAGE_START);
    	
		// prepare textview area
		fileContentArea = new JTextPane();
        fileContentArea.setEditable(false);
		JScrollPane fileViewScrollPane = new JScrollPane(fileContentArea);
		

		// add all elements to the main pane
        JSplitPane resultSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, fileViewScrollPane);
        resultSplitPane.setDividerLocation(300);
        resultSplitPane.setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
		add(new JLabel("Results"));
        add(resultSplitPane, BorderLayout.CENTER);
	}
	
    private void populateTree(File dir) {
    	currentDirectory = dir;
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(dir);
        createNodes(root, dir);
        treeModel.setRoot(root);
    }
	
    private void createNodes(DefaultMutableTreeNode node, File file) {
    	
	    // recursively add files
	    if (file.isDirectory()) {
	        for (File child : file.listFiles()) {
	            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
	            node.add(childNode);
	            if (child.isDirectory()) {
	            	createNodes(childNode, child);
	            }
	        }
	    }
	}
    
    private void previewFilesAction(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) dirTree.getLastSelectedPathComponent();

        if (node == null || node.isRoot() || !node.isLeaf())
            return;
        
        File nodeFile = (File) node.getUserObject();
        if (!nodeFile.isDirectory()) {
        	String content = Utilities.readTextFile(nodeFile);
            if (content == null) {
            	fileContentArea.setText("");
                JOptionPane.showMessageDialog(null, "Warning: The selected file is not a text file.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            fileContentArea.setText(content);
        }
    }
    
    private void refreshButtonAction(ActionEvent e) {
        if (currentDirectory != null) {
        	fileContentArea.setText("");
            populateTree(currentDirectory);
        }
    }
    
    private void openButtonAction(ActionEvent e) {
    	JFileChooser fileChooser;
		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose Directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);  // Disable the "All files" option.
		
		if (currentDirectory != null) {
			fileChooser.setCurrentDirectory(currentDirectory);
		}
		
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            populateTree(selectedFile);
        }
    }
    
    public void updateDirTree(File newDirPath) {
    	fileContentArea.setText("");
    	populateTree(newDirPath);
    }
	
	public JTree getDirTree() {
		return dirTree;
	}
	
	public JButton getRefreshButton() {
		return refreshButton;
	}
	
	static class DirectoryTreeCellRenderer extends DefaultTreeCellRenderer {
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

}
