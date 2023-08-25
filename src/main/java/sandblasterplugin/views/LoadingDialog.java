package sandblasterplugin.views;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class LoadingDialog {
 private final JDialog dialog;

 public LoadingDialog() {
     dialog = new JDialog();
     dialog.setModal(true);
     dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
     dialog.setTitle("Loading...");
     dialog.setSize(300, 150);
     dialog.setLocationRelativeTo(null);
     
     JLabel loadingLabel = new JLabel("Please wait, retrieving results...");
     loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
     dialog.add(loadingLabel);
 }

 public void show() {
     dialog.setVisible(true);
 }

 public void hide() {
     dialog.setVisible(false);
 }
}
