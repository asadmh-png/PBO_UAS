public class Makanan extends Menu {
    public Makanan(String id, String nama, double harga, String kategori){
        super(id, nama, harga, kategori);
    }

    @Override
    public String getKategori() {
        return "Makanan";
    }
}
