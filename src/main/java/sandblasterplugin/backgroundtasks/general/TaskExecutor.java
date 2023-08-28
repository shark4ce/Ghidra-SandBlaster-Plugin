package sandblasterplugin.backgroundtasks.general;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingWorker;

import sandblasterplugin.views.LoadingDialog;

public class TaskExecutor {

    private LoadingDialog loadingDialog = new LoadingDialog();

    public <T> T executeTask(AbstractTask<T> task) {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<T> resultHolder = new AtomicReference<>();

        task.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                loadingDialog.hide();
                try {
                    T result = task.get();
                    resultHolder.set(result);
                } catch (Exception e) {
                    task.logMessage("Error: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        task.execute();
        loadingDialog.show();

        try {
            latch.await();
        } catch (InterruptedException e) {
            task.logMessage("Error waiting for result: " + e.getMessage());
        }

        return resultHolder.get();
    }
    
    public void hideLoadingDialog() {
    	loadingDialog.hide();
    }
}