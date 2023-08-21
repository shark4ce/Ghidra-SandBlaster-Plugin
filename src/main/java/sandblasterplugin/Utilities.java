package sandblasterplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JOptionPane;

import ghidra.util.Msg;

public class Utilities {
	
    public static boolean isIOSVersionValid(String input) {
//        return input.matches("[0-9]\\.([0-9](\\.[0-9])?)?");
        return input.matches("^(\\d+)(\\.\\d+){0,3}$");
    }
    
    
    public static String getMajoriOSVersion(String iOSVersion) {
    	return iOSVersion.split("\\.")[0];
    }
    
    public static boolean isTextFile(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPORT);
        decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
            return true;
        } catch (CharacterCodingException e) {
            return false;
        }
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
    
    public static String runCommand(LoggerUtil logger, String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        StringBuilder output = new StringBuilder();
        
        try {
        	logger.logMessage("Running external command: [ " + String.join(" ", command) + " ]");
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
            	logger.logMessage("* " + line);
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
        	logger.logMessage("* Exit code: " + exitCode);
            return exitCode == 0 ? output.toString().trim() : null;
        } catch (IOException | InterruptedException e) {
        	logger.logMessage("* Error: " + e.toString());
            return null;
        }
    }
}
