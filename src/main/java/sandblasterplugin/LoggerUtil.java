package sandblasterplugin;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {
		
	    private JTextArea logTextArea;
	    private PrintWriter fileWriter;
	    

	    public LoggerUtil(JTextArea logTextArea) {
	    	
	    	this.logTextArea = logTextArea;
	        
//	        this.fileWriter = new PrintWriter(new FileWriter(logFile, true))
	        logMessage("Welcome!");
	    }

	    public void logMessage(String message) {
	    	message = getCurrentTime() + " >>> " + message;
	        logToGui(message);
//	        logToFile(message);
	    }
	    
	    private String getCurrentTime() {
	        LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        String currentTimeAndDate = now.format(formatter);
	        
	     	return "(" + currentTimeAndDate + ")";
	    }
	 
	    private void logToGui(String message) {
	    	logTextArea.append(message + "\n");
	        logTextArea.setCaretPosition(logTextArea.getDocument().getLength()); // Scroll to the end
	    }

	    private void logToFile(String message) {
	    	return;
//	        fileWriter.println(message);
//	        fileWriter.flush(); // Ensure it's written immediately
	    }
	    

	    public void close() {
	        fileWriter.close();
	    }
}
