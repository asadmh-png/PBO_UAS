import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class KelolaMenu extends JFrame {
    // Komponen yang dideklarasikan di form XML
    private JPanel tabel_menu;
    private JTable menu;
    private JTextField id;
    private JTextField nama;
    private JTextField harga;
    private JComboBox<String> kategori;
    private JButton edit;
    private JButton tambah;
    private JButton hapus;
    private JButton kembali;

    // Komponen tambahan yang tidak ada di XML
    private JPanel mainPanel; // Panel utama dari form
    private final List<Menu> menuList = new ArrayList<>();
    private final Path dataFile = Paths.get("menu_data.txt");
    private DefaultTableModel tableModel;

    public KelolaMenu() {
        setTitle("Kelola Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Inisialisasi komponen dari form XML
        initComponents();

        // Setup tabel
        setupTable();

        // Setup listeners
        setupListeners();

        // Load data dari file
        loadFromFile();

        // Set frame properties
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setSize(600, 500); // Ukuran sesuai dengan form
    }

    private void initComponents() {
        // Method ini akan di-generate otomatis oleh IntelliJ
        // Pastikan untuk menjalankan "Build -> Build Project" untuk generate kode ini
    }

    private void setupTable() {
        // Setup model untuk tabel
        String[] cols = {"Id", "Nama", "Harga", "Kategori"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        menu.setModel(tableModel);

        // Setup selection mode
        menu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupListeners() {
        // Setup button listeners
        tambah.addActionListener(e -> onTambah());
        edit.addActionListener(e -> onEdit());
        hapus.addActionListener(e -> onHapus());
        kembali.addActionListener(e -> dispose());

        // Mouse click listener untuk tabel
        menu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = menu.getSelectedRow();
                if (r >= 0) populateFormFromRow(r);
            }
        });

        // Keyboard delete untuk tabel
        menu.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) onHapus();
            }
        });

        // Update text pada button kembali dari XML
        kembali.setText("Kembali");
    }

    private void populateFormFromRow(int row) {
        id.setText(tableModel.getValueAt(row, 0).toString());
        nama.setText(tableModel.getValueAt(row, 1).toString());
        harga.setText(tableModel.getValueAt(row, 2).toString());
        kategori.setSelectedItem(tableModel.getValueAt(row, 3).toString());
    }

    private void onTambah() {
        try {
            Menu m = readFormAsMenu();
            for (Menu existing : menuList) {
                if (existing.getId().equals(m.getId())) {
                    JOptionPane.showMessageDialog(this, "ID sudah ada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            menuList.add(m);
            addMenuToTable(m);
            saveToFile();
            Logger.logTambahMenu(m.getId(), m.getNama());
            clearForm();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onEdit() {
        int row = menu.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih menu untuk diedit.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String oldId = tableModel.getValueAt(row, 0).toString();
        String oldNama = tableModel.getValueAt(row, 1).toString();
        try {
            Menu updatedMenu = readFormAsMenu();
            String idToEdit = tableModel.getValueAt(row, 0).toString();

            for (int i = 0; i < menuList.size(); i++) {
                if (menuList.get(i).getId().equals(idToEdit)) {
                    if (!idToEdit.equals(updatedMenu.getId())) {
                        for (Menu existing : menuList) {
                            if (existing.getId().equals(updatedMenu.getId())) {
                                JOptionPane.showMessageDialog(this, "ID sudah ada.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }
                    menuList.set(i, updatedMenu);
                    refreshTable();
                    saveToFile();
                    Logger.logEditMenu(oldId, oldNama);
                    clearForm();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "ID tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onHapus() {
        int row = menu.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih menu untuk dihapus.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String id = tableModel.getValueAt(row, 0).toString();
        String nama = tableModel.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus menu ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String idToDelete = tableModel.getValueAt(row, 0).toString();
            menuList.removeIf(m -> m.getId().equals(idToDelete));
            refreshTable();
            saveToFile();
            Logger.logHapusMenu(id, nama);
            clearForm();
        }
    }

    private Menu readFormAsMenu() {
        String idText = id.getText().trim();
        String namaText = nama.getText().trim();
        String hargaText = harga.getText().trim();
        String kategoriText = (String) kategori.getSelectedItem();

        if (idText.isEmpty() || namaText.isEmpty() || hargaText.isEmpty()) {
            throw new IllegalArgumentException("Semua field harus diisi.");
        }

        double hargaValue;
        try {
            hargaValue = Double.parseDouble(hargaText);
            if (hargaValue <= 0) {
                throw new IllegalArgumentException("Harga harus lebih dari 0.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Harga harus berupa angka.");
        }

        // Perhatikan: XML form memiliki 3 pilihan kategori (Makanan, Minuman, Snack)
        if (kategoriText.equals("Makanan")) {
            return new Makanan(idText, namaText, hargaValue, kategoriText);
        } else if (kategoriText.equals("Minuman")) {
            return new Minuman(idText, namaText, hargaValue, kategoriText);
        } else if (kategoriText.equals("Snack")) {
            return new Snack(idText, namaText, hargaValue, kategoriText);
        } else {
            throw new IllegalArgumentException("Kategori tidak valid: " + kategoriText);
        }
    }

    private void addMenuToTable(Menu m) {
        tableModel.addRow(new Object[] {
                m.getId(), m.getNama(), m.getHarga(), m.getKategori()
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Menu m : menuList) addMenuToTable(m);
    }

    private void clearForm() {
        id.setText("");
        nama.setText("");
        harga.setText("");
        kategori.setSelectedIndex(0);
    }

    private void saveToFile() {
        try (BufferedWriter bw = Files.newBufferedWriter(dataFile)) {
            for (Menu m : menuList) {
                // Format: id,nama,harga,type,kategoriSpesifik
                String type;
                if (m instanceof Makanan) {
                    type = "Makanan";
                } else if (m instanceof Minuman) {
                    type = "Minuman";
                } else if (m instanceof Snack) {
                    type = "Snack";
                } else {
                    type = "Unknown"; // Fallback
                }

                String line = String.format("%s,%s,%.2f,%s,%s",
                        m.getId(),
                        m.getNama(),
                        m.getHarga(),
                        type,
                        m.getKategoriValue()); // Menggunakan getKategoriValue() untuk kategori spesifik
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFromFile() {
        menuList.clear();
        if (!Files.exists(dataFile)) return;

        try (BufferedReader br = Files.newBufferedReader(dataFile)) {
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
                    continue; // Skip line jika harga tidak valid
                }
                String type = parts[3].trim();
                String kategoriSpesifik = parts[4].trim();

                Menu m;
                if ("Makanan".equalsIgnoreCase(type)) {
                    m = new Makanan(id, nama, harga, kategoriSpesifik);
                } else if ("Minuman".equalsIgnoreCase(type)) {
                    m = new Minuman(id, nama, harga, kategoriSpesifik);
                } else if ("Snack".equalsIgnoreCase(type)) {
                    m = new Snack(id, nama, harga, kategoriSpesifik);
                } else {
                    continue; // Skip line jika type tidak valid
                }
                menuList.add(m);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        refreshTable();
    }

    // Main method untuk testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KelolaMenu k = new KelolaMenu();
            k.setVisible(true);
        });
    }
}