package sandblasterplugin.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


public class ConfigurationView {
	
	private JPanel configurationPanel;
	
    private JButton autoDetectButton;
    private JButton python2BinChooseButton;
    private JButton python3BinChooseButton;
    
    private JButton iOSVersionOkButton;
	private JButton sandboxProfilesFileChooseButton;
	private JButton sandboxOperationsFileChooseButton;
	private JButton outDirPathChooseButton;
	
    private JButton startButton;
    private JButton cancelButton;
    
	private JTextField python2TextField;
	private JTextField python3TextField;
	private JTextField iOSVersionTextField;
	private JTextField sandboxOperationsFilePathTextField;
	private JTextField sandboxProfilesFilePathTextField;
	private JTextField outDirPathTextField;
	
    private JTextArea logTextArea;
    private JProgressBar progressBar;
    private Dimension textFeildsDimension;
    private GridBagConstraints gbc;
    
    public ConfigurationView() {
    	this.textFeildsDimension = new Dimension(400, 25);
    	initGUI();
    }
    
	private void initGUI(){
		//create config panel
		configurationPanel = new JPanel();
		configurationPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

		
    	addPrerequisitesPanel();
    	addInputDataPanel();
    	addControlButtons();
    	addLogArea();
	}
	
    private void addPrerequisitesPanel()  {
    	
        // labels
    	JLabel specifyJLabel = new JLabel("Please, specify the path for the following items:");
    	JLabel python2Label = new JLabel("Python2" + ":");
    	JLabel python3Label = new JLabel("Python3" + ":");
    	
    	// Text Fields
        python2TextField = new JTextField();
        python2TextField.setEditable(false);
        python2TextField.setPreferredSize(textFeildsDimension);
        
        python3TextField = new JTextField();
        python3TextField.setEditable(false);
        python3TextField.setPreferredSize(textFeildsDimension);
    	
    	// buttons
        autoDetectButton = new JButton("Auto Detect");
        python2BinChooseButton = new JButton("Select");
        python3BinChooseButton = new JButton("Select");
        
        // Merge rows into a panel
        JPanel pythonPerqJPanel = new JPanel();
        GroupLayout pythonPerqgGoupLayout = new GroupLayout(pythonPerqJPanel);
        pythonPerqJPanel.setLayout(pythonPerqgGoupLayout);
        
        pythonPerqgGoupLayout.setAutoCreateGaps(true);
        pythonPerqgGoupLayout.setAutoCreateContainerGaps(true);
        pythonPerqgGoupLayout.linkSize(SwingConstants.HORIZONTAL, python2BinChooseButton, python3BinChooseButton);
        pythonPerqgGoupLayout.setHorizontalGroup(
        		pythonPerqgGoupLayout.createParallelGroup()
                    .addComponent(specifyJLabel)
                    .addGroup(pythonPerqgGoupLayout.createSequentialGroup()
                        .addComponent(python2Label)
                        .addComponent(python2TextField, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(python2BinChooseButton)
                        .addComponent(autoDetectButton))
                    .addGroup(pythonPerqgGoupLayout.createSequentialGroup()
                        .addComponent(python3Label)
                        .addComponent(python3TextField, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(python3BinChooseButton))
                    .addGap(0, 0, Short.MAX_VALUE)
            );

        pythonPerqgGoupLayout.setVerticalGroup(
        		pythonPerqgGoupLayout.createSequentialGroup()
                    .addComponent(specifyJLabel)
                    .addGap(10)
                    .addGroup(pythonPerqgGoupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(python2Label)
                        .addComponent(python2TextField)
                        .addComponent(python2BinChooseButton)
                        .addComponent(autoDetectButton))
                    .addGroup(pythonPerqgGoupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(python3Label)
                        .addComponent(python3TextField)
                        .addComponent(python3BinChooseButton))
            );
        
        
    	// Create Important Message Panel
        JEditorPane impInfoPane = createaInfoPane("/html/importantMessage.html");
        JPanel perquisitesJPanel = new JPanel(new GridBagLayout());
        perquisitesJPanel.setBorder(createCustomBorder("Perquisites"));
        GridBagConstraints constraints = new GridBagConstraints();

      
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        perquisitesJPanel.add(impInfoPane, constraints);

        constraints.gridx = 1;
        perquisitesJPanel.add(pythonPerqJPanel, constraints);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        configurationPanel.add(perquisitesJPanel, gbc);
    }
    
    private void addInputDataPanel() {
    	
        //labels
        JLabel iOSVersionJLabel = new JLabel("Specify a valid iOS version (x.x/x.x.x):");
        JLabel sandboxOperationsFilePathLabel = new JLabel("SandBox Operations Source File Path:");
        JLabel sandboxProfilesFilePathLabel = new JLabel("SandBox Profiles Source File Path:");
        JLabel outDirPathJLabel = new JLabel("Output Directory Path:");
        
        // text fields
        iOSVersionTextField = new JTextField();
        iOSVersionTextField.setHorizontalAlignment(SwingConstants.CENTER);
        iOSVersionTextField.setPreferredSize(textFeildsDimension);
        
        sandboxOperationsFilePathTextField = new JTextField();
        sandboxOperationsFilePathTextField.setPreferredSize(textFeildsDimension);
        sandboxOperationsFilePathTextField.setEditable(false);
        sandboxOperationsFilePathTextField.setEnabled(false);
        
        sandboxProfilesFilePathTextField = new JTextField();
        sandboxProfilesFilePathTextField.setPreferredSize(textFeildsDimension);
        sandboxProfilesFilePathTextField.setEditable(false);
        sandboxProfilesFilePathTextField.setEnabled(false);
        
        outDirPathTextField = new JTextField();
        outDirPathTextField.setPreferredSize(textFeildsDimension);
        outDirPathTextField.setEditable(false);
        
        // buttons
        iOSVersionOkButton = new JButton("OK");
       
        sandboxOperationsFileChooseButton = new JButton("Select");
        sandboxOperationsFileChooseButton.setEnabled(false);

        sandboxProfilesFileChooseButton = new JButton("Select");
        sandboxProfilesFileChooseButton.setEnabled(false);
        
        outDirPathChooseButton = new JButton("Select");
        
    	// input data panel
        JPanel inputDataFieldsPanel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(inputDataFieldsPanel);
        inputDataFieldsPanel.setLayout(groupLayout);
        
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.linkSize(SwingConstants.HORIZONTAL, iOSVersionOkButton, sandboxOperationsFileChooseButton, sandboxProfilesFileChooseButton, outDirPathChooseButton);
        groupLayout.setHorizontalGroup(
        		groupLayout.createSequentialGroup()
        		.addGroup(
        				groupLayout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(iOSVersionJLabel)
        				.addComponent(sandboxOperationsFilePathLabel)
        				.addComponent(sandboxProfilesFilePathLabel)
        				.addComponent(outDirPathJLabel)

        				)
        		.addGroup(
        				groupLayout.createParallelGroup()
        				.addComponent(iOSVersionTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(sandboxOperationsFilePathTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(sandboxProfilesFilePathTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(outDirPathTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				)
        		.addGroup(
        				groupLayout.createParallelGroup()
        				.addComponent(iOSVersionOkButton)
        				.addComponent(sandboxOperationsFileChooseButton)
        				.addComponent(sandboxProfilesFileChooseButton)
        				.addComponent(outDirPathChooseButton)
        				)
        		);
        
        
        groupLayout.setVerticalGroup(
        		groupLayout.createSequentialGroup()
        		.addGroup(
        				groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(iOSVersionJLabel)
        				.addComponent(iOSVersionTextField)
        				.addComponent(iOSVersionOkButton)
        				)
        		.addGroup(
        				groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(sandboxOperationsFilePathLabel)
        				.addComponent(sandboxOperationsFilePathTextField)
        				.addComponent(sandboxOperationsFileChooseButton)
        				)
        		.addGroup(
        				groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(sandboxProfilesFilePathLabel)
        				.addComponent(sandboxProfilesFilePathTextField)
        				.addComponent(sandboxProfilesFileChooseButton)
        				)
        		.addGroup(
        				groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(outDirPathJLabel)
        				.addComponent(outDirPathTextField)
        				.addComponent(outDirPathChooseButton)
        				)
        		);
        
        
    	// Create Important Message Panel
        JEditorPane impInfoPane = createaInfoPane("/html/descriptionMessage.html");
        JPanel inputDataPanel = new JPanel(new GridBagLayout());
        inputDataPanel.setBorder(createCustomBorder("Input Data"));
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        inputDataPanel.add(inputDataFieldsPanel, constraints);

        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        inputDataPanel.add(impInfoPane, constraints);
        
        gbc.gridy = 1;
        gbc.weighty = 0;
        configurationPanel.add(inputDataPanel, gbc);
    }
    
    private void addControlButtons() {
    	
        // progress bar and start/cancel button
        progressBar = new JProgressBar();
        progressBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        startButton = new JButton("Start");
        startButton.setEnabled(false);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setEnabled(false);
      
        JPanel controlPanel = new JPanel();
        GroupLayout layout = new GroupLayout(controlPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true); 
        controlPanel.setLayout(layout);
        
        layout.setHorizontalGroup(
        		layout.createSequentialGroup()
        		.addGroup(
        				layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        				.addGroup(
        						layout.createSequentialGroup()
        						.addGap(0, 0, Short.MAX_VALUE)
        						.addComponent(startButton)
        						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
        						.addComponent(cancelButton)
        						.addGap(0, 0, Short.MAX_VALUE))
        				.addComponent(progressBar))
        		);

        layout.setVerticalGroup(
        		layout.createSequentialGroup()
        		.addGroup(
        				layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        				.addComponent(startButton)
        				.addComponent(cancelButton)
        				)
        		.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        		.addComponent(progressBar)
        		);
        
        gbc.gridy = 2;
        gbc.weighty = 0;
        configurationPanel.add(controlPanel, gbc);
    }
    
    private void addLogArea() {
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
       
        TitledBorder titledBorder = new TitledBorder(null, "Logs", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        titledBorder.setTitleFont(new Font("AppleMyungjo", Font.BOLD, 14));
        
        JScrollPane logJScrollPane = new JScrollPane();
        logJScrollPane.setBorder(titledBorder);
        logJScrollPane.setViewportView(logTextArea);
        
        gbc.gridy = 3;
        gbc.weighty = 1;
        configurationPanel.add(logJScrollPane, gbc);
    }
    
    // helpers
    private Border createCustomBorder(String title) {

        TitledBorder titledBorder = new TitledBorder(null, title, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        titledBorder.setTitleFont(new Font("AppleMyungjo", Font.BOLD, 14));
        Border externalBorder = new EmptyBorder(8, 0, 0, 0);
        return BorderFactory.createCompoundBorder(externalBorder, titledBorder);
    }
    
    private JEditorPane createaInfoPane(String htmlResourcePath) {
        Color backgroundColor = UIManager.getColor("Panel.background");
        String hexColor = String.format("#%02x%02x%02x", backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue());
        
        InputStream inputStrem = this.getClass().getResourceAsStream(htmlResourcePath);
        try (Scanner scanner = new Scanner(inputStrem).useDelimiter("\\A")) {
			String htmlContent = scanner.hasNext() ? scanner.next() : "";
			htmlContent = htmlContent.replace("<body>", "<body bgcolor='" + hexColor + "'>");
			
			JEditorPane impInfoPane = new JEditorPane();
			impInfoPane.setContentType("text/html");
			impInfoPane.setEditable(false);
			impInfoPane.setBackground(backgroundColor);
			impInfoPane.setBorder(null);
			impInfoPane.setText(htmlContent);
			
			return impInfoPane;
		}
    }

    public void displayWarning(String message) {
        JOptionPane.showMessageDialog(configurationPanel, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    public void displayError(String title, String message) {
        JOptionPane.showMessageDialog(configurationPanel, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public void displaySuccess(String title, String message) {
        JOptionPane.showMessageDialog(configurationPanel, message);
    }
    
    //setters and getters
    public JPanel getConfigurationPanel() {
		return configurationPanel;
	}

	public JButton getAutoDetectButton() {
		return autoDetectButton;
	}

	public JButton getPython2BinChooseButton() {
		return python2BinChooseButton;
	}

	public JButton getPython3BinChooseButton() {
		return python3BinChooseButton;
	}

	public JButton getiOSVersionOkButton() {
		return iOSVersionOkButton;
	}

	public JButton getSandboxProfilesFileChooseButton() {
		return sandboxProfilesFileChooseButton;
	}

	public JButton getSandboxOperationsFileChooseButton() {
		return sandboxOperationsFileChooseButton;
	}
	
	public JButton getOutDirPathChooseButton() {
		return outDirPathChooseButton;
	}

	public JButton getStartButton() {
		return startButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JTextField getPython2TextField() {
		return python2TextField;
	}

	public JTextField getPython3TextField() {
		return python3TextField;
	}

	public JTextField getiOSVersionTextField() {
		return iOSVersionTextField;
	}

	public JTextField getSandboxOperationsFilePathTextField() {
		return sandboxOperationsFilePathTextField;
	}

	public JTextField getSandboxProfilesFilePathTextField() {
		return sandboxProfilesFilePathTextField;
	}

	public JTextField getOutDirPathTextField() {
		return outDirPathTextField;
	}

	public JTextArea getLogTextArea() {
		return logTextArea;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}
}
