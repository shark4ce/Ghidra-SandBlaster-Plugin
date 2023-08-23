package sandblasterplugin.backgroundtasks;

import java.io.File;
import java.io.IOException;

import sandblasterplugin.controllers.ConfigurationController;

public class ReverseBundleSandBoxProfilesTask extends BaseSandBlasterBackgroundTask{

	public ReverseBundleSandBoxProfilesTask(
			ConfigurationController configurationController,
			String python2FilePathString,
			String python3FilePathString,
			String iOSVersionString,
			String sandboxOperationsSourceFilePathString, 
			String sandboxProfilesSourceFilePathString, 
			String outDirPathString
			) throws IOException 
	{
		super(
				configurationController,
				python2FilePathString,
				python3FilePathString,
				iOSVersionString,
				sandboxOperationsSourceFilePathString, 
				sandboxProfilesSourceFilePathString, 
				outDirPathString
				);
	}

	@Override
	protected int reverseSandBoxProfiles() {
    	publish("Reversing and extracting SandBox Profiles from the bundle...");
    	int exitCode = -1;
		String[] command = {
				python2FilePathString, 
				reverseSandBoxProfilesPythonScriptPathString, 
				"-r",
				iOSVersionString,
				"-o",
				extractedSandboxOperationsFilePathString,
				"-d",
				reversedSandBoxProfileDirPathString,
				String.join(File.separator, extractedSandboxProfilesDirPathString, "sandbox_bundle")
				};
		
		exitCode = runCommand(reverseSandBoxProfilesPythonScriptsDirPathString, command);
		publish("Exit code: " + exitCode);
		publish("");
		return exitCode;
	}
}
