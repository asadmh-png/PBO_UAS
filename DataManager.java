import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataManager {
    private static final String MENU_FILE = "menu_data.txt";
    private static final String CUSTOMER_FILE = "customer_data.txt";
    private static final String ORDER_FILE = "order_data.txt";

    public static void saveReport(String reportContent) {
        try {
            Files.writeString(Path.of("report_data.txt"), reportContent, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Laporan berhasil disimpan ke report_data.txt");
        } catch (IOException e) {
            System.err.println("Gagal menyimpan laporan: " + e.getMessage());
        }
    }

    public static void appendReport(String reportContent) {
        try {
            Files.writeString(Path.of("report_data.txt"), reportContent, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Laporan berhasil ditambahkan ke report_data.txt");
        } catch (IOException e) {
            System.err.println("Gagal menambahkan laporan: " + e.getMessage());
        }
    }

    public static String readReport() {
        try {
            List<String> lines = Files.readAllLines(Path.of("report_data.txt"), StandardCharsets.UTF_8);
            return String.join("\n", lines);
        } catch (IOException e) {
            System.err.println("Gagal membaca laporan: " + e.getMessage());
            return "";
        }
    }

    public static List<String> readReportLines() {
        try {
            return Files.readAllLines(Path.of("report_data.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Gagal membaca laporan: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public static void saveMenuToFile(String content) {
        try {
            Files.writeString(Path.of(MENU_FILE), content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Data menu berhasil disimpan ke " + MENU_FILE);
        } catch (IOException e) {
            System.err.println("Gagal menyimpan data menu: " + e.getMessage());
        }
    }

    public static List<Menu> loadMenuFromFile() {
        List<Menu> menu = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Path.of(MENU_FILE), StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String id = parts[0];
                    String name = parts[1];
                    double harga = Double.parseDouble(parts[2]);
                    String type = parts[3];
                    if (type.equals("Makanan") && parts.length == 6) {
                        String category = parts[4];
                        menu.add(new Makanan(id, name, harga, category));
                    } else if (type.equals("Minuman") && parts.length == 6) {
                        String category = parts[4];
                        menu.add(new Minuman(id, name, harga, category));
                    } else if (type.equals("Snack") && parts.length == 6) {
                        String category = parts[4];
                        menu.add(new Snack(id, name, harga, category));
                    }
                }
            }
            System.out.println("Data menu berhasil dimuat dari " + MENU_FILE);
        } catch (IOException e) {
            System.err.println("Gagal memuat data menu: " + e.getMessage());
        }
        return menu;
    }

    public static void saveCustomersToFile(String content) {
        try {
            Files.writeString(Path.of(CUSTOMER_FILE), content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Data pelanggan berhasil disimpan ke " + CUSTOMER_FILE);
        } catch (IOException e) {
            System.err.println("Gagal menyimpan data pelanggan: " + e.getMessage());
        }
    }

    public static List<Pelanggan> loadCustomersFromFile() {
        List<Pelanggan> customers = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Path.of(CUSTOMER_FILE), StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String id = parts[0];
                    String name = parts[1];
                    Integer noMeja = Integer.valueOf(parts[2]);
                    customers.add(new Pelanggan(id, name, noMeja));
                }
            }
            System.out.println("Data pelanggan berhasil dimuat dari " + CUSTOMER_FILE);
        } catch (IOException e) {
            System.err.println("Gagal memuat data pelanggan: " + e.getMessage());
        }
        return customers;
    }

    public static void saveOrdersToFile(String content) {
        try {
            Files.writeString(Path.of(ORDER_FILE), content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Data order berhasil disimpan ke " + ORDER_FILE);
        } catch (IOException e) {
            System.err.println("Gagal menyimpan data order: " + e.getMessage());
        }
    }

    public static List<Order> loadOrdersFromFile() {
        List<Order> orders = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Path.of(ORDER_FILE), StandardCharsets.UTF_8);
            for (String line : lines) {
                // Parsing logic for orders can be implemented here
                // This is a placeholder as the order structure is complex
            }
            System.out.println("Data order berhasil dimuat dari " + ORDER_FILE);
        } catch (IOException e) {
            System.err.println("Gagal memuat data order: " + e.getMessage());
        }
        return orders;
    }
}
