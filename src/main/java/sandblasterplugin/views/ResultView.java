package sandblasterplugin.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import resources.Icons;


public class ResultView {

	private JPanel resultPanel;
	private JPanel treePanel;
	private JTree directoryTree;
	private JButton refreshButton;
	private JButton openDirButton;
	private JTextPane fileContentArea;
    private File currentDirectory;
    
    
    public ResultView() {
    	initGUI();
    }
    
	private void initGUI() {
		// prepare Directory Tree
		
		directoryTree = new JTree((Object[]) null);
        directoryTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    	
    	treePanel = new JPanel(new BorderLayout());
    	treePanel.add(new JScrollPane(directoryTree), BorderLayout.CENTER);
    	
		// create toolbar and add button
        refreshButton = new JButton(Icons.REFRESH_ICON);
        refreshButton.setToolTipText("Refresh"); // This sets the tooltip text
        
    	openDirButton = new JButton(Icons.OPEN_FOLDER_ICON);
    	openDirButton.setToolTipText("Refresh"); // This sets the tooltip text
        
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
        
		resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.add(new JLabel("Results"));
        resultPanel.add(resultSplitPane, BorderLayout.CENTER);
//        resultPanel.setVisible(false);
	}
	
	
    public void displayWarning(String message) {
        JOptionPane.showMessageDialog(resultPanel, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
	public JPanel getResultPanel() {
		return resultPanel;
	}

	public JPanel getTreePanel() {
		return treePanel;
	}

	public void setTreePanel(JPanel treePanel) {
		this.treePanel = treePanel;
	}

	public JTree getDirectoryTree() {
		return directoryTree;
	}

	public void setDirectoryTree(JTree dirTree) {
		this.directoryTree = dirTree;
	}

	public JButton getRefreshButton() {
		return refreshButton;
	}

	public void setRefreshButton(JButton refreshButton) {
		this.refreshButton = refreshButton;
	}

	public JButton getOpenDirButton() {
		return openDirButton;
	}

	public void setOpenDirButton(JButton openDirButton) {
		this.openDirButton = openDirButton;
	}

	public JTextPane getFileContentArea() {
		return fileContentArea;
	}

	public void setFileContentArea(JTextPane fileContentArea) {
		this.fileContentArea = fileContentArea;
	}

	public File getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(File currentDirectory) {
		this.currentDirectory = currentDirectory;
	}
    
}
