import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Menjalankan aplikasi dengan TabelKasir sebagai window utama
        SwingUtilities.invokeLater(() -> {
            TabelKasir kasir = new TabelKasir();
            kasir.setVisible(true);
        });
    }
}