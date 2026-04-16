package model;

public class Produktuak {
    private int id;
    private String izena;
    private double prezioa;
    private int stock;
    private String irudia;
    private int produktuenMotakId;
    private String motaIzena;

    public Produktuak() {}

    public Produktuak(int id, String izena, double prezioa, int stock, String irudia, int produktuenMotakId, String motaIzena) {
        this.id = id;
        this.izena = izena;
        this.prezioa = prezioa;
        this.stock = stock;
        this.irudia = irudia;
        this.produktuenMotakId = produktuenMotakId;
        this.motaIzena = motaIzena;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public double getPrezioa() { return prezioa; }
    public void setPrezioa(double prezioa) { this.prezioa = prezioa; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getIrudia() { return irudia; }
    public void setIrudia(String irudia) { this.irudia = irudia; }

    public int getProduktuenMotakId() { return produktuenMotakId; }
    public void setProduktuenMotakId(int produktuenMotakId) { this.produktuenMotakId = produktuenMotakId; }

    public String getMotaIzena() { return motaIzena; }
    public void setMotaIzena(String motaIzena) { this.motaIzena = motaIzena; }

    @Override
    public String toString() {
        return izena;
    }
}
