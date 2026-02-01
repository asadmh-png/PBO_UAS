import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final Path LOG_FILE = Paths.get("log.txt");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void log(String action, String message) {
        try {
            String timestamp = DATE_FORMAT.format(new Date());
            String logEntry = String.format("[%s] [%s] %s", timestamp, action, message);

            Files.write(LOG_FILE, (logEntry + "\n").getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            System.out.println("LOG: " + logEntry); // Juga tampilkan di console
        } catch (IOException e) {
            System.err.println("Gagal menulis log: " + e.getMessage());
        }
    }

    // Helper methods untuk action spesifik
    public static void logTambahMenu(String menuId, String menuNama) {
        log("TAMBAH MENU", String.format("Menambahkan menu dengan ID: %s, Nama: %s", menuId, menuNama));
    }

    public static void logEditMenu(String menuId, String menuNama) {
        log("EDIT MENU", String.format("Mengedit menu dengan ID: %s, Nama: %s", menuId, menuNama));
    }

    public static void logHapusMenu(String menuId, String menuNama) {
        log("HAPUS MENU", String.format("Menghapus menu dengan ID: %s, Nama: %s", menuId, menuNama));
    }

    public static void logTambahPesanan(String orderId, String customerName, int noMeja, double total) {
        log("TAMBAH PESANAN", String.format(
                "Menambahkan pesanan dengan ID: %s, Pelanggan: %s, Meja: %d, Total: Rp %,.0f",
                orderId, customerName, noMeja, total));
    }

    public static void logEditPesanan(String orderId, String oldStatus, String newStatus) {
        log("EDIT PESANAN", String.format(
                "Mengedit pesanan dengan ID: %s, Status berubah dari %s menjadi %s",
                orderId, oldStatus, newStatus));
    }

    public static void logHapusPesanan(String orderId, String customerName) {
        log("HAPUS PESANAN", String.format(
                "Menghapus pesanan dengan ID: %s, Pelanggan: %s", orderId, customerName));
    }

    public static void logTambahPelanggan(String customerId, String customerName, int noMeja) {
        log("TAMBAH PELANGGAN", String.format(
                "Menambahkan pelanggan dengan ID: %s, Nama: %s, Meja: %d",
                customerId, customerName, noMeja));
    }

    public static void login(String username) {
        log("LOGIN", String.format("User %s login ke sistem", username));
    }

    public static void logout(String username) {
        log("LOGOUT", String.format("User %s logout dari sistem", username));
    }
}