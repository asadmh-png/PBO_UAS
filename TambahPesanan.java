import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TambahPesanan extends JFrame {
    private JTextField no_meja;
    private JTextField nama;
    private JTextField id;
    private JTable menu;
    private JButton kembali;
    private JButton tambah;
    private JPanel mainPanel;
    private JPanel tabel_pesanan;
    private JTextArea struk;

    // Komponen tambahan
    private DefaultTableModel tableModelMenu;
    private DefaultTableModel tableModelPesanan;
    private List<Menu> daftarMenu;
    private List<Menu> pesananItems;
    private Path menuFile = Paths.get("menu_data.txt");
    private Path orderFile = Paths.get("order_data.txt");
    private Path customerFile = Paths.get("customer_data.txt");
    private JTable tabelPesanan; // Tabel untuk menampilkan pesanan

    public TambahPesanan() {
        // Set frame properties
        setTitle("Tambah Pesanan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setSize(900, 500);

        // Inisialisasi komponen
        initComponents();
        setupTable();
        loadMenuData();

        // Setup listeners
        setupListeners();
    }

    private void initComponents() {
        // Inisialisasi daftar
        daftarMenu = new ArrayList<>();
        pesananItems = new ArrayList<>();

        // Setup struk
        struk.setEditable(false);
        struk.setText("=== STRUK PESANAN ===\n\n");
    }

    private void setupTable() {
        // Setup tabel menu
        String[] colsMenu = {"ID", "Nama", "Harga", "Kategori"};
        tableModelMenu = new DefaultTableModel(colsMenu, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        menu.setModel(tableModelMenu);

        // Setup selection mode
        menu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Setup tabel pesanan (jika ada tabel pesanan terpisah)
        // Karena dari XML hanya ada 1 tabel, kita akan gunakan mouse listener untuk menambahkan item
    }

    private void setupListeners() {
        // Listener untuk button tambah
        tambah.addActionListener(e -> onTambahPesanan());

        // Listener untuk button kembali
        kembali.addActionListener(e -> dispose());

        // Double click pada tabel menu untuk menambahkan item ke pesanan
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double click
                    int row = menu.getSelectedRow();
                    if (row >= 0) {
                        tambahItemKePesanan(row);
                    }
                }
            }
        });

        // Key listener untuk tabel menu (Enter untuk tambah item)
        menu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int row = menu.getSelectedRow();
                    if (row >= 0) {
                        tambahItemKePesanan(row);
                    }
                }
            }
        });
    }

    private void loadMenuData() {
        daftarMenu.clear();
        tableModelMenu.setRowCount(0);

        if (!Files.exists(menuFile)) {
            JOptionPane.showMessageDialog(this, "File menu_data.txt tidak ditemukan!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(menuFile)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String id = parts[0].trim();
                String nama = parts[1].trim();
                double harga = 0;
                try {
                    harga = Double.parseDouble(parts[2].trim());
                } catch (NumberFormatException ignored) {
                    continue;
                }
                String type = parts[3].trim();
                String kategoriSpesifik = parts[4].trim();

                Menu m;
                if ("Makanan".equalsIgnoreCase(type)) {
                    m = new Makanan(id, nama, harga, kategoriSpesifik);
                } else if ("Minuman".equalsIgnoreCase(type)) {
                    m = new Minuman(id, nama, harga, kategoriSpesifik);}
                else if ("Snack".equalsIgnoreCase(type)) {
                    m = new Snack(id, nama, harga, kategoriSpesifik);
                } else {
                    continue;
                }

                daftarMenu.add(m);
                tableModelMenu.addRow(new Object[]{m.getId(), m.getNama(), m.getHarga(), m.getKategori()});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data menu: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tambahItemKePesanan(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= daftarMenu.size()) return;

        Menu item = daftarMenu.get(rowIndex);
        pesananItems.add(item);

        // Update struk
        updateStruk();

        // Tampilkan pesan konfirmasi
        JOptionPane.showMessageDialog(this,
                "Item '" + item.getNama() + "' ditambahkan ke pesanan!",
                "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateStruk() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== STRUK PESANAN ===\n\n");

        sb.append("Data Pelanggan:\n");
        sb.append("ID      : ").append(id.getText()).append("\n");
        sb.append("Nama    : ").append(nama.getText()).append("\n");
        sb.append("No Meja : ").append(no_meja.getText()).append("\n\n");

        sb.append("Daftar Pesanan:\n");
        sb.append("--------------------------------------------------\n");

        double total = 0;
        for (Menu item : pesananItems) {
            sb.append(String.format("%-20s Rp %10.0f\n",
                    item.getNama(), item.getHarga()));
            total += item.getHarga();
        }

        sb.append("--------------------------------------------------\n");
        sb.append(String.format("%-20s Rp %10.0f\n", "TOTAL:", total));
        sb.append("--------------------------------------------------\n");

        struk.setText(sb.toString());
    }
    // Di method onTambahPesanan() - TambahPesanan
    private void onTambahPesanan() {
        // Validasi input
        if (!validasiInput()) return;

        // Validasi ada item pesanan
        if (pesananItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tidak ada item dalam pesanan! Tambahkan item dari menu terlebih dahulu.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Simpan data pelanggan
            simpanDataPelanggan();

            // Buat dan simpan order, dapatkan orderId
            String orderId = simpanDataOrder();

            // Hitung total untuk logging
            double total = 0;
            for (Menu item : pesananItems) {
                total += item.getHarga();
            }

            // LOGGING: Tambah pesanan
            Logger.logTambahPesanan(orderId, nama.getText().trim(),
                    Integer.parseInt(no_meja.getText().trim()), total);

            // Reset form
            resetForm();

            JOptionPane.showMessageDialog(this,
                    "Pesanan berhasil ditambahkan!\nOrder ID: " + orderId,
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Format nomor meja tidak valid!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan pesanan: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private boolean validasiInput() {
        String idText = id.getText().trim();
        String namaText = nama.getText().trim();
        String noMejaText = no_meja.getText().trim();

        // Validasi ID tidak kosong
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "ID Pelanggan tidak boleh kosong!",
                    "Validasi Error", JOptionPane.ERROR_MESSAGE);
            id.requestFocus();
            return false;
        }

        // Validasi nama tidak kosong
        if (namaText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nama Pelanggan tidak boleh kosong!",
                    "Validasi Error", JOptionPane.ERROR_MESSAGE);
            nama.requestFocus();
            return false;
        }

        // Validasi nomor meja
        if (noMejaText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nomor Meja tidak boleh kosong!",
                    "Validasi Error", JOptionPane.ERROR_MESSAGE);
            no_meja.requestFocus();
            return false;
        }

        try {
            int noMeja = Integer.parseInt(noMejaText);
            if (noMeja <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Nomor Meja harus lebih dari 0!",
                        "Validasi Error", JOptionPane.ERROR_MESSAGE);
                no_meja.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Nomor Meja harus berupa angka!",
                    "Validasi Error", JOptionPane.ERROR_MESSAGE);
            no_meja.requestFocus();
            return false;
        }

        return true;
    }

    private void simpanDataPelanggan() throws IOException {
        String idText = id.getText().trim();
        String namaText = nama.getText().trim();
        int noMeja = Integer.parseInt(no_meja.getText().trim());

        // Format: id,nama,noMeja
        String data = String.format("%s,%s,%d\n", idText, namaText, noMeja);

        // Tulis ke file customer
        Files.write(customerFile, data.getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private String simpanDataOrder() throws IOException {
        // Generate order ID
        String orderId = generateOrderId();
        String customerId = id.getText().trim();
        String customerName = nama.getText().trim();
        int noMeja = Integer.parseInt(no_meja.getText().trim());

        // Buat objek pelanggan
        Pelanggan pelanggan = new Pelanggan(customerId, customerName, noMeja);

        // Buat objek order
        Order order = new Order(orderId, pelanggan);

        // Tambahkan semua item ke order
        for (Menu item : pesananItems) {
            order.tambahItem(item);
        }

        // Format untuk disimpan ke file
        // Format: orderId,customerId,customerName,noMeja,total,status,items,timestamp
        StringBuilder sb = new StringBuilder();
        sb.append(orderId).append(",");
        sb.append(customerId).append(",");
        sb.append(customerName).append(",");
        sb.append(noMeja).append(",");
        sb.append(String.format("%.2f", order.getTotal())).append(",");
        sb.append(order.getStatus().name()).append(",");

        // Tambahkan item items
        for (Menu item : pesananItems) {
            sb.append(item.getId()).append(":"); // ID item
            sb.append(item.getNama()).append(":"); // Nama item
            sb.append(String.format("%.2f", item.getHarga())).append(":"); // Harga item
            sb.append(item instanceof Makanan ? "Makanan" :
                    item instanceof Minuman ? "Minuman" : "Snack").append(";"); // Tipe item
        }

        // Hapus separator terakhir jika ada item
        if (pesananItems.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        } else {
            sb.append(" "); // Tambahkan spasi jika tidak ada item
        }

        sb.append(",").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        sb.append("\n");

        // Tulis ke file order
        Files.write(orderFile, sb.toString().getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        return orderId; // Return orderId
    }

    private String generateOrderId() {
        // Format: ORD-YYYYMMDD-RANDOM
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateFormat.format(new Date());
        Random random = new Random();
        int randomNum = random.nextInt(9999);

        return String.format("ORD-%s-%04d", dateStr, randomNum);
    }

    private void resetForm() {
        id.setText("");
        nama.setText("");
        no_meja.setText("");
        pesananItems.clear();
        struk.setText("=== STRUK PESANAN ===\n\n");
    }
    // Main method untuk testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TambahPesanan frame = new TambahPesanan();
            frame.setVisible(true);
        });
    }
}