public class Minuman extends Menu {
    private String kategori;
    private Boolean isDingin;

    public Minuman(String id, String nama, Double harga, String kategori, Boolean isDingin){
        super(id, nama, harga);
        this.kategori = kategori;
        this.isDingin = isDingin;
    }

    @Override
    public void tampilkanInfo(){
        super.tampilkanInfo();
        System.out.println("Kategori: " + kategori + ", Status Dingin: " + getStatusDingin());
    }

    @Override
    public String getKategori(){
        return kategori;
    }
    public String getStatusDingin(){
        return isDingin ? "Dingin" : "Hangat";
    }
    public Boolean getIsDingin(){
        return isDingin;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
    public void setIsDingin(Boolean isDingin){
        this.isDingin = isDingin;
    }
}
