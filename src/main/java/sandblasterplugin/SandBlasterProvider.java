package sandblasterplugin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import docking.ComponentProvider;
import sandblasterplugin.controllers.ConfigurationController;
import sandblasterplugin.controllers.ResultController;
import sandblasterplugin.models.ConfigurationModel;
import sandblasterplugin.models.ResultModel;
import sandblasterplugin.views.ConfigurationView;
import sandblasterplugin.views.ResultView;


public class SandBlasterProvider extends ComponentProvider {

	private JPanel mainPanel;
	private JTabbedPane tabbedPane;
	
    private ConfigurationView configurationView;
    private ConfigurationModel configurationModel;
    private ConfigurationController configurationController;
    
    private ResultView resultView;
    private ResultModel resultModel;
    
	public SandBlasterProvider(SandBlasterPlugin sandBlasterPlugin) throws IOException {
		super(sandBlasterPlugin.getTool(), sandBlasterPlugin.getName(), sandBlasterPlugin.getName());
		this.resultModel = new ResultModel();
		this.resultView = new ResultView();
		new ResultController(resultModel, resultView);
		
		this.configurationView = new ConfigurationView();
		this.configurationModel = new ConfigurationModel(configurationView.getLogTextArea(), sandBlasterPlugin);
		this.configurationController = new ConfigurationController(configurationModel, configurationView, sandBlasterPlugin, resultModel);
		
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
    
    public ConfigurationController getConfigurationController() {
    	return configurationController;
    }
}
