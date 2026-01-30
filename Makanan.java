public class Makanan extends Menu {
    private String kategori;
    private Boolean isPedas;

    public Makanan(String id, String nama, double harga, String kategori, Boolean isPedas){
        super(id, nama, harga);
        this.kategori = kategori;
        this.isPedas = isPedas;
    }

    @Override
    public void tampilkanInfo(){
        super.tampilkanInfo();
        System.out.println("Kategori: " + kategori + ", Status Pedas: " + getStatusPedas());
    }

    @Override
    public String getKategori(){
        return kategori;
    }
    public String getStatusPedas(){
        return isPedas ? "Pedas" : "Tidak Pedas";
    }
    public Boolean getIsPedas(){
        return isPedas;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
    public void setIsPedas(Boolean isPedas){
        this.isPedas = isPedas;
    }
}
