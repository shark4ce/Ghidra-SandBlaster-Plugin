package sandblasterplugin.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import resources.Icons;
import sandblasterplugin.utils.Utilities;


public class ResultView {

	private JPanel resultPanel;
	private JPanel treePanel;
	private JTree directoryTree;
	private JButton refreshButton;
	private JButton openDirButton;
	private JTextArea fileContentArea;
    private File currentDirectory;
    
    
    public ResultView() {
    	initGUI();
    }
    
	private void initGUI() {
		directoryTree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode()));
		directoryTree.setRootVisible(false);
        directoryTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    	
    	treePanel = new JPanel(new BorderLayout());
    	treePanel.add(new JScrollPane(directoryTree), BorderLayout.CENTER);
    	
		// prepare textview area
		fileContentArea = new JTextArea();
        fileContentArea.setEditable(false);
		JScrollPane fileViewScrollPane = new JScrollPane(fileContentArea);
		
		// add all elements to the main pane
        JSplitPane resultSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, fileViewScrollPane);
        resultSplitPane.setDividerLocation(300);
        resultSplitPane.setPreferredSize(new Dimension(800, 600));
        
		// create toolbar and add button
    	openDirButton = new JButton(Icons.OPEN_FOLDER_ICON);
    	openDirButton.setToolTipText("Open");
    	
        refreshButton = new JButton(Icons.REFRESH_ICON);
        refreshButton.setToolTipText("Refresh");
        
        JToolBar toolBar = new JToolBar("My Toolbar");
        toolBar.setBorder(BorderFactory.createEtchedBorder());
        toolBar.setFloatable(false);
        toolBar.addSeparator();
        toolBar.add(openDirButton);
        toolBar.add(refreshButton);
        
		resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.add(toolBar, BorderLayout.PAGE_START);
        resultPanel.add(new JLabel("Results"));
        resultPanel.add(resultSplitPane, BorderLayout.CENTER);
        
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

	public JTextArea getFileContentArea() {
		return fileContentArea;
	}

	public File getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(File currentDirectory) {
		this.currentDirectory = currentDirectory;
	}
}
