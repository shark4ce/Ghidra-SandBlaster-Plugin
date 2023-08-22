package sandblasterplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import ghidra.framework.Application;

import ghidra.framework.Platform;
import sandblasterplugin.controllers.ConfigurationController;
import sandblasterplugin.models.ConfigurationModel;

public class SandBlasterBackgroundTask extends SwingWorker<Void, String>{
	private ConfigurationController configurationController;
	
	String iOSVersionString;
	String extractSandBoxDataPythonScriptPathString;
	String reverseSandBoxProfilesPythonScriptsDirPathString;
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
		extractSandBoxDataPythonScriptPathString = String.join(File.separator, sandblasterToolPatString, "helpers", "extract_sandbox_data.py");
		reverseSandBoxProfilesPythonScriptsDirPathString = String.join(File.separator, sandblasterToolPatString, "reverse-sandbox");
		
		iOSVersionString = configurationController.getConfigurationModel().getiOSVersionString();
		python2FilePathString = configurationController.getConfigurationModel().getPython2BinPathString();
		python3FilePathString = configurationController.getConfigurationModel().getPython3BinPathString();
		outDirPathString = initOutDir(configurationController.getConfigurationModel().getOutDirPathString());
		
		extractedSandboxOperationsFilePathString = String.join(File.separator, mkdir(outDirPathString, iOSVersionString + "-iOSVersion.sandbox_operations"), iOSVersionString + "-iOSVersion.sb_ops");
		extractedSandboxProfilesDirPathString = mkdir(outDirPathString, iOSVersionString + "-iOSVersion.sandbox_profiles");
		reversedSandBoxProfileDirPathString = mkdir(outDirPathString, iOSVersionString + "-iOSVersion.reversed_profiles");
		
	}
	 	
	public String getSandBlasterToolPathString() {
	    // Get the directory where Ghidra is installed
		
	    File pluginInstallDir = new File(Application.getMyModuleRootDirectory().getAbsolutePath());

	    // Construct the path to the os-specific directory within your module
	    String platformDir = Platform.CURRENT_PLATFORM.getDirectoryName();
	    File osSpecificDir = new File(pluginInstallDir, "os" + File.separator + platformDir);

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
				reverseSandBoxProfilesPythonScriptsDirPathString + File.separator + "reverse_sandbox.py", 
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


    private int runCommand(String executionDirectoryPathString, String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        
        if (executionDirectoryPathString != null) {
            processBuilder.directory(new File(executionDirectoryPathString));
        }
        
        try {
        	publish("\nRunning external command: [ " + String.join(" ", command) + " ]");
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            	publish(line);
            }
            int exitCode = process.waitFor();
            publish("Exit code: " + exitCode + "\n");
            return exitCode;
        } catch (IOException | InterruptedException e) {
        	publish("Error: " + e.toString());
        	return 1;
        }
    }
	
	
    @Override
    protected Void doInBackground() throws Exception {
    	
    	getFirstCommands();
    	int exitCode = runCommand(null, command1);
    	if (exitCode != 0) {
    		return null;
    	} 
    	
    	exitCode = runCommand(null, command2);
    	if (exitCode != 0) {
    		return null;
    	} 
    	
//    	exitCode = runCommand(reverseSandBoxProfilesPythonScriptsDirPathString, command3);
//    	if (exitCode != 0) {
//    		return null;
//    	} 

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
    	configurationController.taskDone(outDirPathString);
    }

}
