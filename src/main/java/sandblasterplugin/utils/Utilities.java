package sandblasterplugin.utils;

import java.awt.Component;
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

import javax.swing.JFrame;

import sandblasterplugin.controllers.ConfigurationController;

public class Utilities {
	
	public static JFrame getFrameFromPanel(Component component) {
	    while (component != null && !(component instanceof JFrame)) {
	        component = component.getParent();
	    }
	    return (JFrame) component;
	}
	
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
}
