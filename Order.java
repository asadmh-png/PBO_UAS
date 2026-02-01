import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private String orderId;
    private Pelanggan pelanggan;
    private List<Menu> items;
    private Date orderDate;
    private StatusPembayaran status;
    private Double total;

    public Order(String orderId, Pelanggan pelanggan){
        this.orderId = orderId;
        this.pelanggan = pelanggan;
        this.items = new ArrayList<>();
        this.orderDate = new Date();
        this.status = StatusPembayaran.BELUM_BAYAR;
        this.total = 0.0;
    }

    public void tambahItem(Menu item){
        items.add(item);
        total();
    }

    public void hapusItem(Menu item){
        items.remove(item);
        total();
    }

    private void total(){
        total = 0.0;
        for (Menu item : items){
            total += item.getHarga();
        }
    }

    public void tampilkanDetailOrder(){
        System.out.println("ID Order: " + orderId);
        System.out.println("Pelanggan: " + pelanggan.getNama());
        System.out.println("Tanggal: " + orderDate);
        System.out.println("Status: " + status.getDeskripsi());
        System.out.println("Total: Rp" + total);
        System.out.println("Items:");
        for (Menu item : items){
            item.tampilkanInfo();
        }
    }

    public String getOrderId(){
        return orderId;
    }
    public Pelanggan getPelanggan() {
        return pelanggan;
    }
    public List<Menu> getItems() {
        return items;
    }
    public Date getOrderDate() {
        return orderDate;
    }
    public StatusPembayaran getStatus() {
        return status;
    }
    public Double getTotal() {
        return total;
    }

    public void setStatus(StatusPembayaran status) {
        this.status = status;
    }
}
