//Main.java - Class utama untuk menjalankan aplikasi
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Menjalankan aplikasi di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel untuk tampilan yang lebih baik
                javax.swing.UIManager.setLookAndFeel(
                        javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Aplikasi Kasir Restoran telah dijalankan!");
            System.out.println("Fitur yang tersedia:");
            System.out.println("1. Kelola Menu (CRUD Menu)");
            System.out.println("2. Transaksi Pesanan");
            System.out.println("3. Pembayaran");
            System.out.println("4. Laporan dan Statistik");
        });
    }
}