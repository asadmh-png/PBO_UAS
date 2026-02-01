public enum StatusPembayaran {
    BELUM_BAYAR("Belum Bayar"),
    LUNAS("Lunas"),
    DIBATALKAN("Dibatalkan"),
    MENUNGGU_KONFIRMASI("Menunggu Konfirmasi");

    private final String deskripsi;

    StatusPembayaran(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

}