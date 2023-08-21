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

public class Logger {
	
	private JScrollPane logJScrollPane;
    private JTextArea logTextArea;
    private PrintWriter fileWriter;

    public Logger(File logFile) throws IOException {
    	
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);

        TitledBorder titledBorder = new TitledBorder(null, "Logs", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        titledBorder.setTitleFont(new Font("AppleMyungjo", Font.BOLD, 15));
        logJScrollPane = new JScrollPane();
        logJScrollPane.setBorder(titledBorder);
        logJScrollPane.setViewportView(logTextArea);
        
        this.fileWriter = new PrintWriter(new FileWriter(logFile, true)); // Append to file
        logMessage("Welcome!");
    }

    public void logMessage(String message) {
    	message = getCurrentTime() + " >>> " + message;
        logToGui(message);
        logToFile(message);
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
        fileWriter.println(message);
        fileWriter.flush(); // Ensure it's written immediately
    }
    
    public JTextArea getLogTextArea() {
    	 return this.logTextArea;
    }
    
    public JScrollPane getLogScrollPane() {
    	return this.logJScrollPane;
    }

    public void close() {
        fileWriter.close();
    }
}
