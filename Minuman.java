public class Minuman extends Menu {
    public Minuman(String id, String nama, Double harga, String kategori){
        super(id, nama, harga, kategori);
    }

    @Override
    public String getKategori() {
        return "Minuman";
    }
}
