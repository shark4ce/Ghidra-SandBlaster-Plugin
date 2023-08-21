package sandblasterplugin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.python.antlr.PythonParser.if_stmt_return;

import docking.ComponentProvider;
import generic.util.Path;
import ghidra.framework.model.Project;
import ghidra.framework.plugintool.PluginTool;
import ghidra.program.model.listing.Program;
import ghidra.util.Msg;
import resources.Icons;
import sandblasterplugin.controllers.ConfigurationController;
import sandblasterplugin.controllers.ResultController;
import sandblasterplugin.models.ConfigurationModel;
import sandblasterplugin.models.ResultModel;
import sandblasterplugin.views.ConfigurationView;
import sandblasterplugin.views.ResultView;


public class SandBlasterProvider extends ComponentProvider {

	private JPanel mainPanel;
	private JTabbedPane tabbedPane;

    private Project project;
    private Program currentProgram;
    private PluginTool tool;
    
    private ConfigurationController configurationController;
    private ConfigurationModel configurationModel;
    private ConfigurationView configurationView;
    
    private ResultController resultController;
    private ResultModel resultModel;
    private ResultView resultView;
    
    String tempString = "/Users/sandu/workspace/disertatie/ghidra-sandblaster-plugin/test-dataset/iPad2,1_5.1.1_9B206";
    
	public SandBlasterProvider(PluginTool tool, String pluginName, Program program) throws IOException {
		super(tool, pluginName, pluginName);
		
		this.tool = tool;
		this.currentProgram = program;
		
		this.configurationModel = new ConfigurationModel(pluginName);
		this.configurationModel.setCurrentProgram(program);
		this.configurationModel.setPluginTool(tool);

		this.configurationView = new ConfigurationView();
		this.configurationController = new ConfigurationController(
				configurationModel, 
				configurationView
				);
		
		this.resultModel = new ResultModel();
		this.resultView = new ResultView();
		this.resultController = new ResultController(resultModel, resultView);
		
    	
        // add to TabbedPane
		tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		tabbedPane.addTab("Configuration", null, configurationView.getConfigurationPanel(),"Run Configuration");
        tabbedPane.addTab("Results", null, resultView.getResultPanel(), "Results Preview");
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tabbedPane);
		
	}
	
    
    @Override
	public JComponent getComponent() {
		return mainPanel;
	}
	
    public void setProgram(Program p) {
    	this.currentProgram = p;
    	this.configurationModel.setCurrentProgram(p);
    }    
    
}
