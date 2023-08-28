package sandblasterplugin.backgroundtasks;

import java.io.File;
import java.io.IOException;

import sandblasterplugin.controllers.ConfigurationController;

public class ReverseBundleSandBoxProfilesTask extends BaseSandBlasterBackgroundTask{

	public ReverseBundleSandBoxProfilesTask(ConfigurationController configurationController) throws IOException {
		super(configurationController);
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
