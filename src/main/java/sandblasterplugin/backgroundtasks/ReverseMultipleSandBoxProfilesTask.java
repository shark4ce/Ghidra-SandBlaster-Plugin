package sandblasterplugin.backgroundtasks;

import java.io.File;
import java.io.IOException;

import sandblasterplugin.controllers.ConfigurationController;

public class ReverseMultipleSandBoxProfilesTask extends BaseSandBlasterBackgroundTask{

	public ReverseMultipleSandBoxProfilesTask(ConfigurationController configurationController) throws IOException {
		super(configurationController);
	}

	@Override
	protected int reverseSandBoxProfiles() {
    	publish("Reversing SandBox Profiles from files...");
    	int exitCode = -1;
		File extractedSandboxProfilesDir = new File(extractedSandboxProfilesDirPathString);
		for (File file : extractedSandboxProfilesDir.listFiles()) {
	    	publish("Reversing SandBox Profile: " + file.getName());
			String[] command = {
					python2FilePathString, 
					reverseSandBoxProfilesPythonScriptPathString, 
					"-r",
					iOSVersionString,
					"-o",
					extractedSandboxOperationsFilePathString,
					"-d",
					reversedSandBoxProfileDirPathString,
					file.getAbsolutePath(),
					};
			exitCode = runCommand(reverseSandBoxProfilesPythonScriptsDirPathString, command);
			publish("* Exit code: " + exitCode);
			publish("");
			if (exitCode != 0) {
				return exitCode;
			}
		}
		return exitCode;
	}
}
