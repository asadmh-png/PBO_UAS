public class Snack extends Menu {
    public Snack(String id, String nama, Double harga, String kategori){
        super(id, nama, harga, kategori);
    }

    @Override
    public String getKategori() {
        return "Snack";
    }
}
