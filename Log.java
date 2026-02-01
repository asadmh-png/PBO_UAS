import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class Log extends JFrame {
    private JTextArea log;
    private JButton kembali;
    private JPanel mainPanel;
    private JPanel log_panel;
    private JButton refresh;

    private Path logFile = Paths.get("log.txt");

    public Log() {
        setTitle("Log Aktivitas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setSize(1000, 400);

        // Hanya setup listeners dan load data
        setupListeners();
        loadLogData();
    }

    private void setupListeners() {
        kembali.addActionListener(e -> dispose());
        refresh.addActionListener(e -> loadLogData());

        // F5 untuk refresh
        KeyStroke f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(f5, "refresh");
        mainPanel.getActionMap().put("refresh", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadLogData();
            }
        });
    }

    private void loadLogData() {
        SwingUtilities.invokeLater(() -> {
            try {
                if (!Files.exists(logFile)) {
                    log.setText("Belum ada log aktivitas.\nFile log.txt belum dibuat.");
                    return;
                }

                List<String> lines = Files.readAllLines(logFile);
                Collections.reverse(lines);

                StringBuilder sb = new StringBuilder();
                sb.append("=== LOG AKTIVITAS ===\n");
                sb.append("Total entri: ").append(lines.size()).append("\n");
                sb.append("==============================\n\n");

                for (String line : lines) {
                    sb.append(line).append("\n");
                }

                log.setText(sb.toString());

                // Auto-scroll ke atas
                log.setCaretPosition(0);

            } catch (IOException e) {
                log.setText("Error: " + e.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Log frame = new Log();
            frame.setVisible(true);
        });
    }
}