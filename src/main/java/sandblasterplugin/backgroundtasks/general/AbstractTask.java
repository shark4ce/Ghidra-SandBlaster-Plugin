package sandblasterplugin.backgroundtasks.general;

import javax.swing.SwingWorker;

abstract class AbstractTask<T> extends SwingWorker<T, String> {
    private final LoggerInterface logger;

    public AbstractTask(LoggerInterface logger) {
        this.logger = logger;
    }

    public LoggerInterface getLogger() {
        return logger;
    }

    @Override
    protected void process(java.util.List<String> chunks) {
        chunks.forEach(this::logMessage);
    }
    
    protected void logMessage(String msg) {
    	if (logger != null) {
    		logger.log("* " + msg);
    	}
    }
}
