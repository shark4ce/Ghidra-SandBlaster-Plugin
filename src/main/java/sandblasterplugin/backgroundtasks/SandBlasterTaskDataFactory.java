package sandblasterplugin.backgroundtasks;

import java.io.IOException;

import sandblasterplugin.controllers.ConfigurationController;
import sandblasterplugin.models.ConfigurationModel;

public class SandBlasterTaskDataFactory {

	public static BaseSandBlasterBackgroundTask createBackgroundTask(ConfigurationController configurationController) throws IOException {
		
		ConfigurationModel configurationModel = configurationController.getConfigurationModel();
		
		String python2FilePathString = configurationModel.getPython2BinPathString();
		String python3FilePathString = configurationModel.getPython3BinPathString();
		String iOSVersionString = configurationModel.getiOSVersionString();
		String kernelExtFilePathString = configurationModel.getKernelExtFilePathString();
		String sandboxdFilePathString = configurationModel.getSandboxdFilePathString();
		String kernelCacheFilePathString = configurationModel.getKernelCacheFilePathString();
		String outDirPathString = configurationModel.getOutDirPathString();
		

		if (!kernelExtFilePathString.equals("")) {
			if (!sandboxdFilePathString.equals("")) {
				return new ReverseMultipleSandBoxProfilesTask(
						configurationController,
						python2FilePathString,
						python3FilePathString,
						iOSVersionString,
						kernelExtFilePathString, 
						sandboxdFilePathString, 
						outDirPathString
						);
			} else {
				return new ReverseBundleSandBoxProfilesTask(
						configurationController,
						python2FilePathString,
						python3FilePathString,
						iOSVersionString,
						kernelExtFilePathString,
						kernelExtFilePathString,
						outDirPathString
						);
			}
		
		} else if (!kernelCacheFilePathString.equals("")) {
			return new ReverseBundleSandBoxProfilesTask(
					configurationController,
					python2FilePathString,
					python3FilePathString,
					iOSVersionString,
					kernelCacheFilePathString,
					kernelCacheFilePathString,
					outDirPathString
					);

		}
		return null;
	}
}
