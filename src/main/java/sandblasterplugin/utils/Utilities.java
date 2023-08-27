package sandblasterplugin.utils;

import java.awt.Component;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;


public class Utilities {
	
	public static JFrame getFrameFromPanel(Component component) {
	    while (component != null && !(component instanceof JFrame)) {
	        component = component.getParent();
	    }
	    return (JFrame) component;
	}
	
    public static boolean isIOSVersionValid(String input) {
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
