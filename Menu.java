public abstract class Menu {
    private String id;
    private String nama;
    private Double harga;

    public Menu(String id, String nama, Double harga) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }

    public abstract String getKategori();

    public void tampilkanInfo() {
        System.out.println("ID: " + id + ", Nama: " + nama + ", Harga: " + harga);
    }

    public String getId() {
        return id;
    }
    public String getNama(){
        return nama;

    }
    public Double getHarga() {
        return harga;
    }

    public void setId(String id){
        this.id = id;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setHarga(Double harga){
        this.harga = harga;
    }
}
