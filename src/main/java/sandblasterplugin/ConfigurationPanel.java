package sandblasterplugin;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

public class ConfigurationPanel extends JPanel {
	
	
	private String python2PathString;
	private String python3PathString;
	
	
	JButton chooseKernelExtFileButton;
	JTextField kernelExtFilePathTextField;
	
	JTextField sandboxdFilePathTextField;
    JButton chooseSandboxdFileButton;
	
	
	private JTextField iOSVersionTextField;
	
    private JButton startButton;
    private JButton cancelButton;
    private JButton autoDetectButton;
    
    private JTextArea logTextArea;
    
    private JProgressBar progressBar;
    private SwingWorker<Void, Void> worker;
    Dimension textFeildsDimension;


    public ConfigurationPanel() throws IOException {
    	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	textFeildsDimension = new Dimension(400, 25);
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
        		+ "        <li><i>lief</i> package for Python3</li>"
        		+ "    </ul>"
        		+ "</html>";
        
        JEditorPane impInfoPane = new JEditorPane();
        impInfoPane.setContentType("text/html");
        impInfoPane.setEditable(false);
        impInfoPane.setBackground(getBackground());
        impInfoPane.setBorder(null);
        impInfoPane.setText(htmlContent);
        JScrollPane scrollInfoPane = new JScrollPane(impInfoPane);
        scrollInfoPane.setBorder(null);
        
        // labels
    	JLabel specifyJLabel = new JLabel("Please, specify the path for the following items:");
    	JLabel python2Label = new JLabel("Python2" + ":");
    	JLabel python3Label = new JLabel("Python3" + ":");

    	// Text Fields
        JTextField python2TextField = new JTextField();
        python2TextField.setPreferredSize(textFeildsDimension);
        JTextField python3TextField = new JTextField();
        python3TextField.setPreferredSize(textFeildsDimension);
    	
    	// buttons
        autoDetectButton = new JButton("Auto Detect");
        JButton choosePython2Button = new JButton("Choose");
        choosePython2Button.addActionListener(new ChooseFileButtonListener(python2TextField));
        JButton choosePython3Button = new JButton("Choose");
        choosePython3Button.addActionListener(new ChooseFileButtonListener(python3TextField));
        
        // Merge rows into a panel
        JPanel pythonPerqJPanel = new JPanel();
        GroupLayout pythonPerqgGoupLayout = new GroupLayout(pythonPerqJPanel);
        pythonPerqJPanel.setLayout(pythonPerqgGoupLayout);
        
        pythonPerqgGoupLayout.setAutoCreateGaps(true);
        pythonPerqgGoupLayout.setAutoCreateContainerGaps(true);
        pythonPerqgGoupLayout.linkSize(SwingConstants.HORIZONTAL, choosePython2Button, choosePython3Button, autoDetectButton);
        pythonPerqgGoupLayout.setHorizontalGroup(
        		pythonPerqgGoupLayout.createParallelGroup()
                    .addComponent(specifyJLabel)
                    .addGroup(pythonPerqgGoupLayout.createSequentialGroup()
                        .addComponent(python2Label)
                        .addComponent(python2TextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(choosePython2Button)
                        .addComponent(autoDetectButton))
                    .addGroup(pythonPerqgGoupLayout.createSequentialGroup()
                        .addComponent(python3Label)
                        .addComponent(python3TextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(choosePython3Button))
                    .addGap(0, 0, Short.MAX_VALUE)
            );

        pythonPerqgGoupLayout.setVerticalGroup(
        		pythonPerqgGoupLayout.createSequentialGroup()
                    .addComponent(specifyJLabel)
                    .addGap(10)
                    .addGroup(pythonPerqgGoupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(python2Label)
                        .addComponent(python2TextField)
                        .addComponent(choosePython2Button)
                        .addComponent(autoDetectButton))
                    .addGroup(pythonPerqgGoupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(python3Label)
                        .addComponent(python3TextField)
                        .addComponent(choosePython3Button))
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
        		.addGap(100)
        		.addComponent(pythonPerqJPanel)
        		);
        
        layout.setVerticalGroup(
        		layout.createParallelGroup(Alignment.BASELINE)
        		.addComponent(scrollInfoPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        		.addComponent(pythonPerqJPanel)
        		);
        
    	add(perquisitesJPanel);
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
        
        // text fields
        iOSVersionTextField = new JTextField();
        iOSVersionTextField.setHorizontalAlignment(SwingConstants.CENTER);
        iOSVersionTextField.setPreferredSize(textFeildsDimension);
        iOSVersionTextField.addActionListener(new iOSVersionSelectionListner());
        
        kernelExtFilePathTextField = new JTextField();
        kernelExtFilePathTextField.setPreferredSize(textFeildsDimension);
        kernelExtFilePathTextField.setEnabled(false);
        
        sandboxdFilePathTextField = new JTextField();
        sandboxdFilePathTextField.setPreferredSize(textFeildsDimension);
        sandboxdFilePathTextField.setEnabled(false);
        
        // buttons
        JButton iOSVersionOkButton = new JButton("OK");
        iOSVersionOkButton.addActionListener(new iOSVersionSelectionListner());

        chooseKernelExtFileButton = new JButton("Choose");
        chooseKernelExtFileButton.addActionListener(new ChooseFileButtonListener(kernelExtFilePathTextField));
        chooseKernelExtFileButton.setEnabled(false);

        chooseSandboxdFileButton = new JButton("Choose");
        chooseSandboxdFileButton.addActionListener(new ChooseFileButtonListener(sandboxdFilePathTextField));
        chooseSandboxdFileButton.setEnabled(false);
        
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.linkSize(SwingConstants.HORIZONTAL, iOSVersionOkButton, chooseKernelExtFileButton, chooseSandboxdFileButton);
        groupLayout.setHorizontalGroup(
        		groupLayout.createSequentialGroup()
//        		.addGap(0, 0, Short.MAX_VALUE)
        		.addGroup(
        				groupLayout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(iOSVersionJLabel)
        				.addComponent(kernelExtFilePathJLabel)
        				.addComponent(sandboxdFilePatJLabel)
        				)
        		.addGroup(
        				groupLayout.createParallelGroup()
        				.addComponent(iOSVersionTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(kernelExtFilePathTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(sandboxdFilePathTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				)
        		.addGroup(
        				groupLayout.createParallelGroup()
        				.addComponent(iOSVersionOkButton)
        				.addComponent(chooseKernelExtFileButton)
        				.addComponent(chooseSandboxdFileButton)
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
        				.addComponent(chooseKernelExtFileButton)
        				)
        		.addGroup(
        				groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(sandboxdFilePatJLabel)
        				.addComponent(sandboxdFilePathTextField)
        				.addComponent(chooseSandboxdFileButton)
        				)
        		);
        
        add(inputDataPanel);
    }
    
    private void addControlButtons() {
    	
        // progress bar and start/cancel button
        progressBar = new JProgressBar();
        progressBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        startButton = new JButton("Start");
        startButton.addActionListener(new StartButtonListener());
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelButtonListener());
      
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
        		.addGap(50)
        		.addGroup(
        				layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        				.addComponent(startButton)
        				.addComponent(cancelButton)
        				)
        		.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
        		.addComponent(progressBar)
        		.addGap(20)
        		);
        add(controlPanel);
    }
     
    private void addLogArea() throws IOException {
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        TitledBorder titledBorder = new TitledBorder(null, "Logs", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        titledBorder.setTitleFont(new Font("AppleMyungjo", Font.BOLD, 15));
        logScrollPane.setBorder(titledBorder);
//		this.logger = new Logger(logTextArea, "/Users/sandu/extension2.log");
		add(logScrollPane);
    }
    
    private Border createCustomBorder(String title) {

        TitledBorder titledBorder = new TitledBorder(null, title, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        titledBorder.setTitleFont(new Font("AppleMyungjo", Font.BOLD, 15));  // Setting the font of the title
        Border externalBorder = new EmptyBorder(10, 0, 0, 0); // Top, Left, Bottom, Right
        Border interiorBorder = new EmptyBorder(20, 20, 20, 20); // Top, Left, Bottom, Right
        Border compundInteriorBorder = BorderFactory.createCompoundBorder(titledBorder, interiorBorder);
        return BorderFactory.createCompoundBorder(externalBorder, compundInteriorBorder);
    }
    
//	public void logMessage(String message) {
//        this.logger.logMessage(message);
//    }
    
	// listeners
	private class StartButtonListener implements ActionListener {
    	@Override
        public void actionPerformed(ActionEvent e) {
            progressBar.setIndeterminate(true);  // Makes the progress bar indeterminate
            progressBar.setValue(100);

            worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    // Simulate some task
                    Thread.sleep(5000);

                    return null;
                }

                @Override
                protected void done() {
                    progressBar.setIndeterminate(false);  // Stops indeterminate mode
                    progressBar.setValue(100);
                    
                }
            };

            worker.execute();
        }
    }
    
    private class CancelButtonListener implements ActionListener {
    	@Override
        public void actionPerformed(ActionEvent e) {
            if (worker != null) {
                worker.cancel(true);
            }
            progressBar.setIndeterminate(false);
            progressBar.setValue(0);

        }
    }
    
    private class iOSVersionSelectionListner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String iOSVersionString = iOSVersionTextField.getText();
            
            if (!Utilities.isIOSVersionValid(iOSVersionString)) {
//            	logger.logMessage("Invalid iOS Version: " + iOSVersionString);
                JOptionPane.showMessageDialog(null, "Please enter a valid iOS version.");
                return;
            }
            
//            logger.logMessage("iOS Version: " + iOSVersionString);

            Integer iOSMajorVersionInteger = Integer.parseInt(Utilities.getMajoriOSVersion(iOSVersionString));
            if (iOSMajorVersionInteger >= 5 && iOSMajorVersionInteger <= 8) {
                sandboxdFilePathTextField.setEnabled(true);
                chooseSandboxdFileButton.setEnabled(true);
            } else if(iOSMajorVersionInteger < 11) {
                kernelExtFilePathTextField.setEnabled(true);
                chooseKernelExtFileButton.setEnabled(true);
                sandboxdFilePathTextField.setEnabled(false);
                chooseSandboxdFileButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid iOS version.");
                return;
            }
        }
    }
    
    private class ChooseFileButtonListener implements ActionListener {
    	
    	JTextField filePathTextField;
    	
    	public ChooseFileButtonListener(JTextField filePathTextField) {
    		this.filePathTextField = filePathTextField;
    	}
    	
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                this.filePathTextField.setText(selectedFile.getAbsolutePath());
            }
        }
    }
    
}

