package sandblasterplugin.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import sandblasterplugin.controllers.ConfigurationController;

public class Utilities {
	
    public static boolean isIOSVersionValid(String input) {
//        return input.matches("[0-9]\\.([0-9](\\.[0-9])?)?");
        return input.matches("^(\\d+)(\\.\\d+){0,3}$");
    }
    
    public static String getMajoriOSVersion(String iOSVersion) {
    	return iOSVersion.split("\\.")[0];
    }
    
    public static String getCurrentTime(String dateTimeFormatterString) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatterString);
        String currentTimeAndDate = now.format(formatter);
        
     	return currentTimeAndDate;
    }
    
    public static String readTextFile(File file) {
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
            decoder.onMalformedInput(CodingErrorAction.REPORT);
            String content = decoder.decode(ByteBuffer.wrap(bytes)).toString();
            
            // If decoding succeeds, return the content
            return content;
        } catch (CharacterCodingException e) {
            // The file is probably binary
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String createDir(String path, String dirName) {
		File outDirFile = new File(path, dirName);
        if (!outDirFile.exists()) {
            boolean result = outDirFile.mkdir();
            if (!result) {
            	return null;
            }
        }
        return outDirFile.getAbsolutePath();
	}
    
    public static String runCommand(ConfigurationController configurationController, String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        StringBuilder output = new StringBuilder();
        
        try {
        	configurationController.logMessage("* Running external command: [ " + String.join(" ", command) + " ]");
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            	configurationController.logMessage("* " + line);
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            configurationController.logMessage("* Exit code: " + exitCode);
            configurationController.logMessage("");
            return exitCode == 0 ? output.toString().trim() : null;
        } catch (IOException | InterruptedException e) {
        	configurationController.logMessage("* Error: " + e.toString());
            configurationController.logMessage("");

            return null;
        }
    }
}
