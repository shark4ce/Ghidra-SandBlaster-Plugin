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
import ghidra.app.ExamplesPluginPackage;
import ghidra.app.plugin.PluginCategoryNames;
import ghidra.app.plugin.ProgramPlugin;
import ghidra.framework.model.Project;
import ghidra.framework.options.ToolOptions;
import ghidra.framework.plugintool.*;
import ghidra.framework.plugintool.util.PluginStatus;
import ghidra.program.model.listing.Program;
import ghidra.util.HelpLocation;
import ghidra.util.Msg;

/**
 * TODO: Provide class-level documentation that describes what this plugin does.
 */
//@formatter:off
@PluginInfo(
	status = PluginStatus.STABLE,
	packageName = ExamplesPluginPackage.NAME,
	category = PluginCategoryNames.EXAMPLES,
	shortDescription = "Plugin short description goes here.",
	description = "Plugin long description goes here."
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

		// TODO: Customize provider (or remove if a provider is not desired)
		toolOptions = tool.getOptions(this.name);
		project = tool.getProject();
		provider = new SandBlasterProvider(this);

	    tool.addComponentProvider(provider, true);

		// TODO: Customize help (or remove if help is not desired)
		String topicName = this.getClass().getPackage().getName();
		String anchorName = "HelpAnchor";
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
		Msg.info(null, "dosent exist");
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