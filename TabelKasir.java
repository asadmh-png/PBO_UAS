import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TabelKasir extends JFrame {
    private JButton menu;
    private JButton tambah_pesanan;
    private JButton data_pesanan;
    private JButton log;
    private JLabel kelola_kasir_resto;
    private JPanel mainPanel; // Tambahkan panel utama

    public TabelKasir() {
        setTitle("Sistem Kasir Restoran");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setSize(400, 300);

        // Setup listeners
        setupListeners();
    }

    private void setupListeners() {
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KelolaMenu km = new KelolaMenu();
                km.setVisible(true);
            }
        });

        tambah_pesanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TambahPesanan tp = new TambahPesanan();
                tp.setVisible(true);
            }
        });

        data_pesanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataPesanan dp = new DataPesanan();
                dp.setVisible(true);
            }
        });

        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Log l = new Log();
                l.setVisible(true);
            }
        });
    }

    // Main method untuk langsung menjalankan TabelKasir
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TabelKasir kasir = new TabelKasir();
            kasir.setVisible(true);
        });
    }
}