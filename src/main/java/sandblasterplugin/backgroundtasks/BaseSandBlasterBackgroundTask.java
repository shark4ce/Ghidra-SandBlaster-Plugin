package sandblasterplugin.backgroundtasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import ghidra.framework.Application;
import ghidra.framework.Platform;
import sandblasterplugin.controllers.ConfigurationController;
import sandblasterplugin.utils.Utilities;

public abstract class BaseSandBlasterBackgroundTask  extends SwingWorker<Void, String> {

	protected Process currentProcess;
	protected PrintWriter logPrintWriter;
	protected ConfigurationController configurationController;
	
	protected volatile boolean flag = false;

	protected String extractSandBoxDataPythonScriptPathString;
	protected String reverseSandBoxProfilesPythonScriptPathString;
	protected String reverseSandBoxProfilesPythonScriptsDirPathString;

	protected String python2FilePathString;
	protected String python3FilePathString;
	protected String iOSVersionString;
	protected String sandboxOperationsSourceFilePathString;
	protected String sandboxProfilesSourceFilePathString;
	protected String outDirPathString;
	
	protected String extractedSandboxOperationsFilePathString;
	protected String extractedSandboxProfilesDirPathString;
	protected String reversedSandBoxProfileDirPathString;
    
	
	public BaseSandBlasterBackgroundTask (
			ConfigurationController configurationController,
			String python2FilePathString,
			String python3FilePathString,
			String iOSVersionString,
			String sandboxOperationsSourceFilePathString, 
			String sandboxProfilesSourceFilePathString, 
			String mainOutDirPathString
			) throws IOException 
	{
		this.currentProcess = null;
		this.configurationController = configurationController;
		this.python2FilePathString = python2FilePathString;
		this.python3FilePathString = python3FilePathString;
		this.iOSVersionString = iOSVersionString;
		this.sandboxOperationsSourceFilePathString = sandboxOperationsSourceFilePathString;
		this.sandboxProfilesSourceFilePathString = sandboxProfilesSourceFilePathString;
		this.outDirPathString = initOutDir(mainOutDirPathString);
        this.logPrintWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(this.outDirPathString, iOSVersionString + "-iOSVersion.log" ))));
		
		// get sandblaster resources
		initSandBlasterResources();
		initOutPutDirectories();
	}
	
	private String initOutDir(String mainOutDirPathString) {
		
		//TO DO: set python paths
		
		String sandblasterDirPathString = Utilities.createDir(mainOutDirPathString, "SandBlaster");
		if (sandblasterDirPathString != null) {
	        String currentTimeString = Utilities.getCurrentTime("yyyy-MM-dd'T'HH-mm-ss");
	        String outDirString = Utilities.createDir(sandblasterDirPathString, iOSVersionString + "-iOSVersion_" + currentTimeString);
	        if (outDirString != null) {
	        	return outDirString;
	        }
		}
		
		return null;
	}
	
	private void initSandBlasterResources() {
		
	    // Get the directory where Ghidra is installed
	    String pluginInstallDir = Application.getMyModuleRootDirectory().getAbsolutePath();
	    String platformDir = Platform.CURRENT_PLATFORM.getDirectoryName();
		String sandblasterToolPatString = String.join(File.separator, pluginInstallDir, "os", platformDir, "sandblaster");
		
		extractSandBoxDataPythonScriptPathString = String.join(File.separator, sandblasterToolPatString, "helpers", "extract_sandbox_data.py");
		reverseSandBoxProfilesPythonScriptsDirPathString = String.join(File.separator, sandblasterToolPatString, "reverse-sandbox");
		reverseSandBoxProfilesPythonScriptPathString = String.join(File.separator, reverseSandBoxProfilesPythonScriptsDirPathString, "reverse_sandbox.py");
	}
	
	private void initOutPutDirectories() {
		extractedSandboxOperationsFilePathString = String.join(File.separator, Utilities.createDir(outDirPathString, iOSVersionString + "-iOSVersion.sandbox_operations"), iOSVersionString + "-iOSVersion.sb_ops");
		extractedSandboxProfilesDirPathString = Utilities.createDir(outDirPathString, iOSVersionString + "-iOSVersion.sandbox_profiles");
		reversedSandBoxProfileDirPathString = Utilities.createDir(outDirPathString, iOSVersionString + "-iOSVersion.reversed_profiles");
	}
	
    protected int runCommand(String executionDirectoryPathString, String... command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            
            if (executionDirectoryPathString != null) {
                processBuilder.directory(new File(executionDirectoryPathString));
            }
            
        	publish("* Running external command: [ " + String.join(" ", command) + " ]");
            currentProcess = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(currentProcess.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            	publish("* " + line);
            }
            return currentProcess.waitFor();
        } catch (IOException | InterruptedException e) {
        	publish("* Error: " + e.toString());
        	return 1;
        }
    } 
	
	protected int extractSandBoxOperations() {
		
    	publish("Extracting sandbox operations...");
		String[] command = {
				python3FilePathString, 
				extractSandBoxDataPythonScriptPathString, 
				"-o",
				extractedSandboxOperationsFilePathString,
				sandboxOperationsSourceFilePathString,
				iOSVersionString
				};
		
		return runCommand(null, command);
	}
	
	protected int extractSandBoxProfiles() {
    	publish("Extracting sandbox profiles...");
		String[] command = {
				python3FilePathString, 
				extractSandBoxDataPythonScriptPathString, 
				"-O",
				extractedSandboxProfilesDirPathString,
				sandboxProfilesSourceFilePathString,
				iOSVersionString
				};
		
		return runCommand(null, command);
	}
	
	protected abstract int reverseSandBoxProfiles();
	
	protected void printConfiguration() {
		publish("");
		publish("SandBlaster process started with following configuration:");
		publish("################################################################################################################");
		publish("# Python2 Bin Path: " + python2FilePathString);
		publish("# Python3 Bin Path: " + python3FilePathString);
		publish("#");
		publish("# iOS Version: " + iOSVersionString);
		publish("# SandBox Operations File Source: " + sandboxOperationsSourceFilePathString);
		publish("# SandBox Profiles File Source: " + sandboxOperationsSourceFilePathString);
		publish("# Output Directory: " + outDirPathString);
		publish("################################################################################################################");
		publish("");
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		
		int exitCode;
		
		printConfiguration();
		
		if (!isCancelled()) {
			exitCode = extractSandBoxOperations();
			publish("* Exit code: " + exitCode);
			publish("");
			if (exitCode != 0) {
	            throw new Exception("Process failed with exit code: " + exitCode + ". Check log files.");
			}
		}
		
		if (!isCancelled()) {
			exitCode = extractSandBoxProfiles();
			publish("* Exit code: " + exitCode);
			publish("");
			if (exitCode != 0) {
	            throw new Exception("Process failed with exit code: " + exitCode + ". Check log files.");
			}
		}
		
		if (!isCancelled()) {
			exitCode = reverseSandBoxProfiles();
			if (exitCode != 0) {
	            throw new Exception("Process failed with exit code: " + exitCode + ". Check log files.");
			}
		}
		
		return null;
	}
	
    @Override
    protected void process(List<String> chunks) {
        for (String chunk : chunks) {
        	this.logMessageToAllPipes(chunk);
        }
    }

    @Override
    protected void done() {
        try {
            get(); // Check for any exceptions thrown during doInBackground
        	String msgString = "Process completed! Check the results in the Results tab.";
        	logMessageToAllPipes(msgString);
        	logMessageToAllPipes("Output Directory with reversed SandBox Profiles: " + outDirPathString);
        	configurationController.taskDone(msgString, outDirPathString);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            logMessageToAllPipes(cause.getMessage());
        	configurationController.taskFailed(cause.getMessage());
        } catch (CancellationException e) {
            if (currentProcess != null) {
                currentProcess.destroy();
            }
            logMessageToAllPipes("Process was canceled by the user!");
            configurationController.taskFailed("Process was canceled by the user!");
        } catch (InterruptedException e) {
            Throwable cause = e.getCause();
        	logMessageToAllPipes(cause.getMessage());
        	configurationController.taskFailed(cause.getMessage());
		} finally {
			closeLogPrintWriter();
        }
    }
    
    
    protected void logMessageToCurrentProcessFile(String message) {
    	 this.logPrintWriter.println(message);
    	 this.logPrintWriter.flush();
    }
    
    protected void logMessageToAllPipes(String msg) {
    	configurationController.logMessage(msg);
    	String currentTimeString = Utilities.getCurrentTime("yyyy-MM-dd HH:mm:ss");
    	String message = "(" + currentTimeString + ") >>> " + msg;
    	logMessageToCurrentProcessFile(message);
    }
    
    
    protected void closeLogPrintWriter() {
        if ( this.logPrintWriter != null) {
        	 this.logPrintWriter.close();
        }
    }
}
