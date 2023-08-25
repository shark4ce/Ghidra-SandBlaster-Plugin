package sandblasterplugin.backgroundtasks.general;

import java.io.BufferedReader;
import java.io.InputStreamReader;

// 1. CommandRunner
public class CommandRunnerTask extends AbstractTask<String> {
    private final String[] command;

    public CommandRunnerTask(String[] command, LoggerInterface logger) {
        super(logger);
        this.command = command;
    }

    @Override
    protected String doInBackground() throws Exception {
    	
    	publish("Running external command: [ " + String.join(" ", command) + " ]");

        StringBuilder output = new StringBuilder();

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            publish(line);
            output.append(line).append("\n");
        }

        int exitCode = process.waitFor();
        publish("Exit Code: " + exitCode);
        if (exitCode != 0) {
            throw new RuntimeException("Command exited with code " + exitCode);
        }

        return output.toString().trim();
    }
}