/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sandblasterplugin;

import java.io.IOException;

import ghidra.MiscellaneousPluginPackage;
import ghidra.app.plugin.PluginCategoryNames;
import ghidra.app.plugin.ProgramPlugin;
import ghidra.framework.model.Project;
import ghidra.framework.options.ToolOptions;
import ghidra.framework.plugintool.*;
import ghidra.framework.plugintool.util.PluginStatus;
import ghidra.program.model.listing.Program;
import ghidra.util.HelpLocation;

/**
 * TODO: Provide class-level documentation that describes what this plugin does.
 */
//@formatter:off
@PluginInfo(
	status = PluginStatus.RELEASED,
	packageName = MiscellaneousPluginPackage.NAME,
	category = PluginCategoryNames.ANALYSIS,
	shortDescription = "SandBlasterPlugin: iOS Sandbox Profile Reverser",
	description = "The SandBlasterPlugin for Ghidra is a specialized tool based on the SandBlaster tool, designed to reverse engineer iOS sandbox profiles "
			+ "from their binary format into a human-readable SBPL (Sandbox Profile Language) format. iOS employs a security feature known as the sandbox, "
			+ "which restricts the capabilities of applications to access certain parts of the system or user data. These restrictions are defined in "
			+ "sandbox profiles, which are often stored in a binary format. The SandBlasterPlugin simplifies the analysis process by converting these "
			+ "binary profiles into the readable SBPL format, aiding researchers and security analysts in understanding and assessing the security "
			+ "implications of these profiles."
)
//@formatter:on
public class SandBlasterPlugin extends ProgramPlugin {

	SandBlasterProvider provider;
	Program program;
	Project project;
	ToolOptions toolOptions;

	/**
	 * Plugin constructor.
	 * 
	 * @param tool The plugin tool that this plugin is added to.
	 * @throws IOException 
	 */
	public SandBlasterPlugin(PluginTool tool) throws IOException {
		super(tool);

		toolOptions = tool.getOptions(this.name);
		project = tool.getProject();
		
		provider = new SandBlasterProvider(this);
	    tool.addComponentProvider(provider, true);

		String topicName = this.getClass().getPackage().getName();
		String anchorName = "";
		provider.setHelpLocation(new HelpLocation(topicName, anchorName));
	}
	
    public String getProjectDirectoryPath() {
		String projectDirPath = project.getProjectLocator().getProjectDir().getAbsolutePath();
		if (projectDirPath != null) {
			return projectDirPath;
		} 
    	return null;
    }
    
    public String getProjecName() {
		String projectName = project.getName();
		if (projectName != null) {
			return projectName;
		} 
    	return null;
    }
    
    public String getCurrentProgramPath() {
    	if (currentProgram != null) {
    		return currentProgram.getExecutablePath();
    	}
    	return null;
    }
    
    public ToolOptions getToolOptions() {
    	return this.toolOptions;
    }

	@Override
	public void init() {
		super.init();
	}
	
    @Override
    protected void programActivated(Program p) {
        program = p;
    }
    
	 @Override
	 protected void dispose() {
		 super.dispose();
		 provider.getConfigurationController().cancelSandBlasterBackgroundTask();
		 tool.removeComponentProvider(provider);
	}

}