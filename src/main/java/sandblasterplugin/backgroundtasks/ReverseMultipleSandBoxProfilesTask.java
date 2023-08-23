package sandblasterplugin.backgroundtasks;

import java.io.File;
import java.io.IOException;

import sandblasterplugin.controllers.ConfigurationController;

public class ReverseMultipleSandBoxProfilesTask extends BaseSandBlasterBackgroundTask{

	public ReverseMultipleSandBoxProfilesTask(
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
