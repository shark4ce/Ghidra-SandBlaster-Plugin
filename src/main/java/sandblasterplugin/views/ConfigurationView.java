package sandblasterplugin.views;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
	private JButton sandboxdFileChooseButton;
	private JButton kernelExtFileChooseButton;
	private JButton kernelCacheFileChooseButton;
	private JButton outDirPathChooseButton;
    private JButton startButton;
    private JButton cancelButton;
    
	private JTextField python2TextField;
	private JTextField python3TextField;
	private JTextField iOSVersionTextField;
	private JTextField kernelExtFilePathTextField;
	private JTextField sandboxdFilePathTextField;
	private JTextField kernelCacheFilePathTextField;
	private JTextField outDirPathTextField;
	
    private JTextArea logTextArea;
    private JProgressBar progressBar;
    private Dimension textFeildsDimension;
    
    public ConfigurationView() {
    	this.textFeildsDimension = new Dimension(400, 25);
    	initGUI();
    }
    
	private void initGUI(){
		//create config panel
		configurationPanel = new JPanel();
		configurationPanel.setLayout(new BoxLayout(configurationPanel, BoxLayout.Y_AXIS));
		
    	addPrerequisitesPanel();
    	addInputDataPanel();
    	addControlButtons();
    	addLogArea();
	}
	
    private void addPrerequisitesPanel() {
    	
    	// Create Important Message Panel
        String htmlContent = 
        		"<html>"
        		+ "<span style='color:red; font-weight:bold;'>Important!</span>"
        		+ "    <p>The SandBlaster Plugin requires the following dependecnies to be installed and configuren on your sistem:</p>"
        		+ "    <ul>"
        		+ "        <li>Python2</li>"
        		+ "        <li>Python3</li>"
        		+ "		   <ul>"
        		+ "        		<li><i>lief</i> package</li>"
        		+ "    		</ul>"
        		+ "    </ul>"
        		+ "</html>";
        
        JEditorPane impInfoPane = new JEditorPane();
        impInfoPane.setContentType("text/html");
        impInfoPane.setEditable(false);
        impInfoPane.setBackground(UIManager.getColor("Panel.background"));
        impInfoPane.setBorder(null);
        impInfoPane.setText(htmlContent);
        JScrollPane scrollInfoPane = new JScrollPane(impInfoPane);
        scrollInfoPane.setBorder(null);
        
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
        python2BinChooseButton = new JButton("Choose");
        python3BinChooseButton = new JButton("Choose");
        
        // Merge rows into a panel
        JPanel pythonPerqJPanel = new JPanel();
        GroupLayout pythonPerqgGoupLayout = new GroupLayout(pythonPerqJPanel);
        pythonPerqJPanel.setLayout(pythonPerqgGoupLayout);
        
        pythonPerqgGoupLayout.setAutoCreateGaps(true);
        pythonPerqgGoupLayout.setAutoCreateContainerGaps(true);
        pythonPerqgGoupLayout.linkSize(SwingConstants.HORIZONTAL, python2BinChooseButton, python3BinChooseButton, autoDetectButton);
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
        
        JPanel perquisitesJPanel = new JPanel();
        perquisitesJPanel.setBorder(createCustomBorder("Perquesits"));
        GroupLayout layout = new GroupLayout(perquisitesJPanel);
        perquisitesJPanel.setLayout(layout);
        
        //add info scroll and others
        layout.setHorizontalGroup(
        		layout.createSequentialGroup()
        		.addGap(6)
        		.addComponent(scrollInfoPane)
        		.addGap(70)
        		.addComponent(pythonPerqJPanel)
        		);
        
        layout.setVerticalGroup(
        		layout.createParallelGroup(Alignment.BASELINE)
        		.addComponent(scrollInfoPane, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
        		.addComponent(pythonPerqJPanel)
        		);
        
        
        configurationPanel.add(perquisitesJPanel);
    }
    
    private void addInputDataPanel() {
    	
    	// input data panel
        JPanel inputDataPanel = new JPanel();
        inputDataPanel.setBorder(createCustomBorder("Input Data"));
        GroupLayout groupLayout = new GroupLayout(inputDataPanel);
        inputDataPanel.setLayout(groupLayout);
    	
        //labels
        JLabel iOSVersionJLabel = new JLabel("Specify a valid iOS version (x.x/x.x.x):");
        JLabel kernelExtFilePathJLabel = new JLabel("Kernel SandBox Extension File Path:");
        JLabel sandboxdFilePatJLabel = new JLabel("Sandboxd File Path:");
        JLabel kernelCacheFilePathJLabel = new JLabel("Kernel Cache File Path:");
        JLabel outDirPathJLabel = new JLabel("Output Directory Path:");
        
        // text fields
        iOSVersionTextField = new JTextField();
        iOSVersionTextField.setHorizontalAlignment(SwingConstants.CENTER);
        iOSVersionTextField.setPreferredSize(textFeildsDimension);
        
        kernelExtFilePathTextField = new JTextField();
        kernelExtFilePathTextField.setPreferredSize(textFeildsDimension);
        kernelExtFilePathTextField.setEditable(false);
        kernelExtFilePathTextField.setEnabled(false);
        
        sandboxdFilePathTextField = new JTextField();
        sandboxdFilePathTextField.setPreferredSize(textFeildsDimension);
        sandboxdFilePathTextField.setEditable(false);
        sandboxdFilePathTextField.setEnabled(false);
        
        kernelCacheFilePathTextField = new JTextField();
        kernelCacheFilePathTextField.setPreferredSize(textFeildsDimension);
        kernelCacheFilePathTextField.setEditable(false);
        kernelCacheFilePathTextField.setEnabled(false);
        
        outDirPathTextField = new JTextField();
        outDirPathTextField.setPreferredSize(textFeildsDimension);
        outDirPathTextField.setEditable(false);
        
        // buttons
        iOSVersionOkButton = new JButton("OK");

        kernelExtFileChooseButton = new JButton("Choose");
        kernelExtFileChooseButton.setEnabled(false);

        sandboxdFileChooseButton = new JButton("Choose");
        sandboxdFileChooseButton.setEnabled(false);
        
        kernelCacheFileChooseButton = new JButton("Choose");
        kernelCacheFileChooseButton.setEnabled(false);
        
        outDirPathChooseButton = new JButton("Choose");
        //set default value

        
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.linkSize(SwingConstants.HORIZONTAL, iOSVersionOkButton, kernelExtFileChooseButton, sandboxdFileChooseButton, kernelCacheFileChooseButton);
        groupLayout.setHorizontalGroup(
        		groupLayout.createSequentialGroup()
//        		.addGap(0, 0, Short.MAX_VALUE)
        		.addGroup(
        				groupLayout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(iOSVersionJLabel)
        				.addComponent(kernelExtFilePathJLabel)
        				.addComponent(sandboxdFilePatJLabel)
        				.addComponent(kernelCacheFilePathJLabel)
        				.addComponent(outDirPathJLabel)

        				)
        		.addGroup(
        				groupLayout.createParallelGroup()
        				.addComponent(iOSVersionTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(kernelExtFilePathTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(sandboxdFilePathTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(kernelCacheFilePathTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(outDirPathTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				)
        		.addGroup(
        				groupLayout.createParallelGroup()
        				.addComponent(iOSVersionOkButton)
        				.addComponent(kernelExtFileChooseButton)
        				.addComponent(sandboxdFileChooseButton)
        				.addComponent(kernelCacheFileChooseButton)
        				.addComponent(outDirPathChooseButton)
        				)
        		.addGap(0, 0, Short.MAX_VALUE)
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
        				.addComponent(kernelExtFilePathJLabel)
        				.addComponent(kernelExtFilePathTextField)
        				.addComponent(kernelExtFileChooseButton)
        				)
        		.addGroup(
        				groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(sandboxdFilePatJLabel)
        				.addComponent(sandboxdFilePathTextField)
        				.addComponent(sandboxdFileChooseButton)
        				)
        		.addGroup(
        				groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(kernelCacheFilePathJLabel)
        				.addComponent(kernelCacheFilePathTextField)
        				.addComponent(kernelCacheFileChooseButton)
        				)
        		.addGroup(
        				groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(outDirPathJLabel)
        				.addComponent(outDirPathTextField)
        				.addComponent(outDirPathChooseButton)
        				)
        		);
        
        configurationPanel.add(inputDataPanel);
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
        						.addGap(0, 0, Short.MAX_VALUE) // This will push buttons to the center
        						.addComponent(startButton)
        						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
        						.addComponent(cancelButton)
        						.addGap(0, 0, Short.MAX_VALUE)) // This will push buttons to the center
        				.addComponent(progressBar))
        		);

        layout.setVerticalGroup(
        		layout.createSequentialGroup()
        		.addGap(30)
        		.addGroup(
        				layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        				.addComponent(startButton)
        				.addComponent(cancelButton)
        				)
        		.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        		.addComponent(progressBar)
        		.addGap(10)
        		);
        
        configurationPanel.add(controlPanel);
    }
    
    private void addLogArea() {
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        
        TitledBorder titledBorder = new TitledBorder(null, "Logs", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        titledBorder.setTitleFont(new Font("AppleMyungjo", Font.BOLD, 15));
        
        JScrollPane logJScrollPane = new JScrollPane();
        logJScrollPane.setBorder(titledBorder);
        logJScrollPane.setViewportView(logTextArea);
        
        configurationPanel.add(logJScrollPane);
    }
    
    // helpers
    private Border createCustomBorder(String title) {

        TitledBorder titledBorder = new TitledBorder(null, title, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        titledBorder.setTitleFont(new Font("AppleMyungjo", Font.BOLD, 15));  // Setting the font of the title
        Border externalBorder = new EmptyBorder(10, 0, 0, 0); // Top, Left, Bottom, Right
        Border interiorBorder = new EmptyBorder(20, 20, 20, 20); // Top, Left, Bottom, Right
        Border compundInteriorBorder = BorderFactory.createCompoundBorder(titledBorder, interiorBorder);
        return BorderFactory.createCompoundBorder(externalBorder, compundInteriorBorder);
    }

	
    public void displayWarning(String message) {
        JOptionPane.showMessageDialog(configurationPanel, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    public void displayError(String title, String message) {
        JOptionPane.showMessageDialog(configurationPanel, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public void displaySuccess(String title, String message) {
        JOptionPane.showMessageDialog(configurationPanel, message, title, JOptionPane.OK_OPTION);
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

	public JButton getSandboxdFileChooseButton() {
		return sandboxdFileChooseButton;
	}

	public JButton getKernelExtFileChooseButton() {
		return kernelExtFileChooseButton;
	}
	
	public JButton getKernelCacheFileChooseButton() {
		return kernelCacheFileChooseButton;
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

	public JTextField getKernelExtFilePathTextField() {
		return kernelExtFilePathTextField;
	}

	public JTextField getSandboxdFilePathTextField() {
		return sandboxdFilePathTextField;
	}

	public JTextField getKernelCacheFilePathTextField() {
		return kernelCacheFilePathTextField;
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
