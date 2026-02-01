import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataPesanan extends JFrame {
    private JTable pesanan;
    private JButton hapus;
    private JButton kembali;
    private JButton edit;
    private JPanel mainPanel;
    private JPanel data_pesanan;

    // Komponen tambahan
    private DefaultTableModel tableModel;
    private Path orderFile = Paths.get("order_data.txt");
    private List<OrderData> orderList;
    private JPopupMenu contextMenu;

    public DataPesanan() {
        setTitle("Data Pesanan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setSize(800, 500);

        initComponents();
        setupTable();
        loadOrderData();
        setupListeners();
    }

    private void initComponents() {
        orderList = new ArrayList<>();

        // Setup context menu
        contextMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit Status");
        JMenuItem deleteItem = new JMenuItem("Hapus Pesanan");
        JMenuItem viewItem = new JMenuItem("Lihat Detail");

        editItem.addActionListener(e -> onEdit());
        deleteItem.addActionListener(e -> onHapus());
        viewItem.addActionListener(e -> tampilkanDetail());

        contextMenu.add(viewItem);
        contextMenu.add(editItem);
        contextMenu.add(deleteItem);
    }

    private void setupTable() {
        String[] cols = {"Order ID", "Pelanggan", "No Meja", "Total", "Status", "Tanggal"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            // HAPUS method getColumnClass() atau perbaiki
            // @Override
            // public Class<?> getColumnClass(int columnIndex) {
            //     return String.class; // Semua kolom adalah String
            // }
        };
        pesanan.setModel(tableModel);
        pesanan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        pesanan.getColumnModel().getColumn(0).setPreferredWidth(100); // Order ID
        pesanan.getColumnModel().getColumn(1).setPreferredWidth(150); // Pelanggan
        pesanan.getColumnModel().getColumn(2).setPreferredWidth(80);  // No Meja
        pesanan.getColumnModel().getColumn(3).setPreferredWidth(100); // Total
        pesanan.getColumnModel().getColumn(4).setPreferredWidth(100); // Status
        pesanan.getColumnModel().getColumn(5).setPreferredWidth(150); // Tanggal
    }

    private void setupListeners() {
        // Button listeners
        edit.addActionListener(e -> onEdit());
        hapus.addActionListener(e -> onHapus());
        kembali.addActionListener(e -> dispose());

        // Mouse listener untuk tabel (context menu)
        pesanan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // Right click
                    int row = pesanan.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        pesanan.setRowSelectionInterval(row, row);
                        contextMenu.show(pesanan, e.getX(), e.getY());
                    }
                }
            }
        });

        // Double click untuk lihat detail
        pesanan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    tampilkanDetail();
                }
            }
        });

        // Key listener untuk delete key
        pesanan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    onHapus();
                }
            }
        });
    }

    private void loadOrderData() {
        orderList.clear();
        tableModel.setRowCount(0);

        if (!Files.exists(orderFile)) {
            JOptionPane.showMessageDialog(this,
                    "File order_data.txt tidak ditemukan!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(orderFile)) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    OrderData order = parseOrderLine(line);
                    if (order != null) {
                        orderList.add(order);
                        addOrderToTable(order);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memuat data pesanan: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private OrderData parseOrderLine(String line) {
        String[] parts = line.split(",", 8); // Split menjadi maksimal 8 bagian
        if (parts.length < 7) return null;

        OrderData order = new OrderData();
        order.orderId = parts[0].trim();
        order.customerId = parts[1].trim();
        order.customerName = parts[2].trim();
        order.noMeja = parts[3].trim();
        order.total = parts[4].trim();
        order.status = parts[5].trim();
        order.items = parts.length > 6 ? parts[6].trim() : "";
        order.timestamp = parts.length > 7 ? parts[7].trim() : "";

        return order;
    }

    private void addOrderToTable(OrderData order) {
        // Format total sebagai string langsung
        String formattedTotal = "";
        try {
            double totalValue = Double.parseDouble(order.total);
            formattedTotal = String.format("Rp %,.0f", totalValue);
        } catch (NumberFormatException e) {
            formattedTotal = "Rp 0";
        }

        tableModel.addRow(new Object[]{
                order.orderId,
                order.customerName + " (" + order.customerId + ")",
                order.noMeja,
                formattedTotal,  // Sudah diformat sebagai String
                order.status,
                order.timestamp
        });
    }

    private void onEdit() {
        int row = pesanan.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih pesanan yang ingin diedit!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrderData selectedOrder = orderList.get(row);
        String oldStatus = selectedOrder.status;

        // Dialog untuk edit status
        String[] statusOptions = {"BELUM_BAYAR", "LUNAS", "DIBATALKAN", "MENUNGGU_KONFIRMASI"};
        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Edit status untuk pesanan: " + selectedOrder.orderId,
                "Edit Status Pesanan",
                JOptionPane.PLAIN_MESSAGE,
                null,
                statusOptions,
                selectedOrder.status
        );

        if (newStatus != null && !newStatus.equals(selectedOrder.status)) {
            selectedOrder.status = newStatus;
            refreshTable();
            saveAllOrders();

            // LOGGING: Edit pesanan
            Logger.logEditPesanan(selectedOrder.orderId, oldStatus, newStatus);

            JOptionPane.showMessageDialog(this,
                    "Status pesanan berhasil diubah!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onHapus() {
        int row = pesanan.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih pesanan yang ingin dihapus!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrderData selectedOrder = orderList.get(row);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus pesanan:\n" +
                        "Order ID: " + selectedOrder.orderId + "\n" +
                        "Pelanggan: " + selectedOrder.customerName + "\n" +
                        "Total: Rp " + selectedOrder.total,
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // LOGGING: Hapus pesanan
            Logger.logHapusPesanan(selectedOrder.orderId, selectedOrder.customerName);

            orderList.remove(row);
            refreshTable();
            saveAllOrders();

            JOptionPane.showMessageDialog(this,
                    "Pesanan berhasil dihapus!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void tampilkanDetail() {
        int row = pesanan.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih pesanan untuk melihat detail!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrderData selectedOrder = orderList.get(row);

        StringBuilder detail = new StringBuilder();
        detail.append("=== DETAIL PESANAN ===\n\n");
        detail.append("Order ID      : ").append(selectedOrder.orderId).append("\n");
        detail.append("ID Pelanggan  : ").append(selectedOrder.customerId).append("\n");
        detail.append("Nama Pelanggan: ").append(selectedOrder.customerName).append("\n");
        detail.append("No Meja       : ").append(selectedOrder.noMeja).append("\n");

        // Format total
        try {
            double totalValue = Double.parseDouble(selectedOrder.total);
            detail.append("Total         : Rp ").append(String.format("%,.0f", totalValue)).append("\n");
        } catch (NumberFormatException e) {
            detail.append("Total         : Rp 0\n");
        }

        detail.append("Status        : ").append(selectedOrder.status).append("\n");
        detail.append("Tanggal       : ").append(selectedOrder.timestamp).append("\n\n");

        // Parse items
        if (!selectedOrder.items.isEmpty()) {
            detail.append("=== DAFTAR ITEM ===\n");
            String[] items = selectedOrder.items.split(";");
            for (String item : items) {
                String[] itemParts = item.split(":");
                if (itemParts.length >= 4) {
                    detail.append("- ").append(itemParts[1]) // Nama item
                            .append(" (ID: ").append(itemParts[0]).append(")")
                            .append(" - Rp ").append(itemParts[2])
                            .append(" [").append(itemParts[3]).append("]\n");
                }
            }
        }

        JTextArea textArea = new JTextArea(detail.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Detail Pesanan - " + selectedOrder.orderId,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (OrderData order : orderList) {
            addOrderToTable(order);
        }
    }

    private void saveAllOrders() {
        try (BufferedWriter bw = Files.newBufferedWriter(orderFile)) {
            for (OrderData order : orderList) {
                String line = String.join(",",
                        order.orderId,
                        order.customerId,
                        order.customerName,
                        order.noMeja,
                        order.total,
                        order.status,
                        order.items,
                        order.timestamp
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan data pesanan: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Inner class untuk menyimpan data order
    private static class OrderData {
        String orderId;
        String customerId;
        String customerName;
        String noMeja;
        String total;
        String status;
        String items;
        String timestamp;
    }

    // Main method untuk testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataPesanan frame = new DataPesanan();
            frame.setVisible(true);
        });
    }
}