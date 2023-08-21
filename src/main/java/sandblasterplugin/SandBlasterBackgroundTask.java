package sandblasterplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import ghidra.framework.Application;

import ghidra.framework.ApplicationConfiguration;
import ghidra.framework.Platform;
import ghidra.framework.plugintool.PluginTool;
import ghidra.util.Msg;
import ghidra.util.SystemUtilities;
import sandblasterplugin.controllers.ConfigurationController;
import sandblasterplugin.models.ConfigurationModel;

public class SandBlasterBackgroundTask extends SwingWorker<Void, String>{
	private ConfigurationController configurationController;
	
	String iOSVersionString;
	String extractSandBoxDataPythonScriptPathString;
	String reverserSandBoxProfilesPythonScriptPathString;
	String python2FilePathString;
	String python3FilePathString;
	String outDirPathString;
	
	String extractedSandboxOperationsFilePathString;
	String extractedSandboxProfilesDirPathString;
	String reversedSandBoxProfileDirPathString;
	
	
	String[] command1;
	String[] command2;
	String[] command3;
	
	public SandBlasterBackgroundTask(ConfigurationController configurationController) {
		this.configurationController = configurationController;

		
		String sandblasterToolPatString = getSandBlasterToolPathString();
		extractSandBoxDataPythonScriptPathString = new File(String.join(File.separator, sandblasterToolPatString, "helpers", "extract_sandbox_data.py")).getAbsolutePath();
		reverserSandBoxProfilesPythonScriptPathString = new File(String.join(File.separator, sandblasterToolPatString, "reverse-sandbox", "reverse_sandbox.py")).getAbsolutePath();
		
		iOSVersionString = configurationController.getConfigurationModel().getiOSVersionString();
		python2FilePathString = configurationController.getConfigurationModel().getPython2BinPathString();
		python3FilePathString = configurationController.getConfigurationModel().getPython3BinPathString();
		outDirPathString = initOutDir(configurationController.getConfigurationModel().getOutDirPathString());
		
		
		extractedSandboxOperationsFilePathString = String.join(File.separator, outDirPathString, iOSVersionString + "-iOSVersion.sandbox_operations", iOSVersionString + "-iOSVersion.sb_ops");
		extractedSandboxProfilesDirPathString = mkdir(outDirPathString, iOSVersionString + "-iOSVersion.sandbox_profiles");
		reversedSandBoxProfileDirPathString = mkdir(outDirPathString, iOSVersionString + "-iOSVersion.reversed_profiles");
		
	}
	 	
	public String getSandBlasterToolPathString() {
	    // Get the directory where Ghidra is installed
	    File ghidraInstallDir = new File(Application.getInstallationDirectory().getAbsolutePath());

	    // Construct the path to your plugin/module's directory within Ghidra
	    String pluginName = this.configurationController.getConfigurationModel().getPluginName();
	    File pluginDir = new File(ghidraInstallDir, String.join(File.separator, "Ghidra", "Features", pluginName));

	    // Construct the path to the os-specific directory within your module
	    String platformDir = Platform.CURRENT_PLATFORM.getDirectoryName();
	    File osSpecificDir = new File(pluginDir, "os" + File.separator + platformDir);

	    return new File(osSpecificDir, "sandblaster").getAbsolutePath();
	}
	
	
	private String initOutDir(String mainOutPath) {
		
		String sandblasterDirPathString = mkdir(mainOutPath, "SandBlaster");
		if (sandblasterDirPathString != null) {
			
	        LocalDateTime now = LocalDateTime.now();
	        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss"));
	        String outDirString = mkdir(sandblasterDirPathString, iOSVersionString + "-iOSVersion_" + formattedDate);
	        if (outDirString != null) {
	        	return outDirString;
	        }
		}
		
		return null;
	}
		
	private String mkdir(String path, String dirName) {
		File outDirFile = new File(path, dirName);
        if (!outDirFile.exists()) {
            boolean result = outDirFile.mkdir();
            if (!result) {
                JOptionPane.showMessageDialog(null, "ERROR: Unable to create output dir.");
            	return null;
            }
        }
        return outDirFile.getAbsolutePath();
	}
	
	private void getFirstCommands() {
		ConfigurationModel configurationModel = this.configurationController.getConfigurationModel();
		
		String kernelExtFilePathString = configurationModel.getKernelExtFilePathString();
		String sandboxdFilePathString = configurationModel.getSandboxdFilePathString();
		String kernelCacheFilePathString = configurationModel.getKernelCacheFilePathString();

		if (!kernelExtFilePathString.equals("")) {
			if (!sandboxdFilePathString.equals("")) {
				
			} else {
				firstCase(kernelExtFilePathString);
			}
		} else if (!kernelCacheFilePathString.equals("")) {
			return;
		}
		return;
	}
	
	
	private void firstCase(String kernelExtFilePathString) {
		
		// extract sandbox operations from kernelcache
		String[] command1 = {
				python3FilePathString, 
				extractSandBoxDataPythonScriptPathString, 
				"-o",
				extractedSandboxOperationsFilePathString,
				kernelExtFilePathString,
				iOSVersionString
				};
		this.command1 = command1;
		
		// extrct binary that contains sandbox profiles from sandboxd
		String[] command2 = {
				python3FilePathString, 
				extractSandBoxDataPythonScriptPathString, 
				"-O",
				extractedSandboxProfilesDirPathString,
				kernelExtFilePathString,
				iOSVersionString
				};
		this.command2 = command2;
		// reverse sandbox profiles
		String[] command3 = {
				python2FilePathString, 
				reverserSandBoxProfilesPythonScriptPathString, 
				"-r",
				iOSVersionString,
				"-o",
				extractedSandboxOperationsFilePathString,
				"-d",
				reversedSandBoxProfileDirPathString,
				String.join(File.separator, extractedSandboxProfilesDirPathString, "sandbox_bundle")
				};
		this.command3 = command3;
	}


    private int runCommand(String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        StringBuilder output = new StringBuilder();
        
        try {
        	publish("Running external command: [ " + String.join(" ", command) + " ]");
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            	publish(line);
            }
            int exitCode = process.waitFor();
            publish("Exit code: " + exitCode);
            return exitCode;
        } catch (IOException | InterruptedException e) {
        	publish("Error: " + e.toString());
        	return 1;
        }
    }
	
	
    @Override
    protected Void doInBackground() throws Exception {
    	
    	getFirstCommands();
    	
    	int exitCode = runCommand(command1);
    	if (exitCode != 0) {
    		return null;
    	} 
    	
    	exitCode = runCommand(command2);
    	if (exitCode != 0) {
    		return null;
    	} 
    	
    	exitCode = runCommand(command3);
    	if (exitCode != 0) {
    		return null;
    	} 

        return null;
    }
    
    @Override
    protected void process(List<String> chunks) {
        for (String chunk : chunks) {
        	configurationController.logMessage("* " + chunk);
        }
    }

    @Override
    protected void done() {
    	configurationController.taskDone();
    }

}
