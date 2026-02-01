public abstract class Menu {
    private String id;
    private String nama;
    private double harga;
    private String kategori;

    public Menu(String id, String nama, double harga, String kategori) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public abstract String getKategori();

    public void tampilkanInfo() {
        System.out.println("ID: " + id + ", Nama: " + nama + ", Harga: " + harga);
    }

    public String getId() {
        return id;
    }
    public String getNama() {
        return nama;
    }
    public double getHarga() {
        return harga;
    }
    public String getKategoriValue() {
        return kategori;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setHarga(double harga) {
        if (harga >= 0) {
            this.harga = harga;
        }
    }
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    @Override
    public String toString() {
        return "Menu{" + "id='" + id + '\'' + ", nama='" + nama + '\'' + ", harga=" + harga + ", kategori='" + kategori + '\'' + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Menu menu = (Menu) obj;
        return id.equals(menu.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
