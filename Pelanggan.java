public class Pelanggan {
    private String id;
    private String nama;
    private Integer noMeja;

    public Pelanggan(String id, String nama, Integer noMeja){
        this.id = id;
        this.nama = nama;
        this.noMeja = noMeja;
    }

    public String getId(){
        return id;
    }
    public String getNama(){
        return nama;
    }
    public Integer getNoMeja(){
        return noMeja;
    }

    public void setId(String id){
        this.id = id;
    }
    public void setNama(String nama){
        this.nama = nama;
    }
    public void setNoMeja(Integer noMeja){
        if (noMeja > 0) {
            this.noMeja = noMeja;
        }
    }


}
