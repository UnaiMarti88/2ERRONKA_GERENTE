package model;

public class Produktuak {
    private int id;
    private String izena;
    private double prezioa;
    private int stock;
    private Integer stockMin;
    private Integer stockMax;
    private String irudia;
    private int produktuenMotakId;

    public Produktuak() {}

    public Produktuak(int id, String izena, double prezioa, int stock, Integer stockMin, Integer stockMax, String irudia, int produktuenMotakId) {
        this.id = id;
        this.izena = izena;
        this.prezioa = prezioa;
        this.stock = stock;
        this.stockMin = stockMin;
        this.stockMax = stockMax;
        this.irudia = irudia;
        this.produktuenMotakId = produktuenMotakId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public double getPrezioa() { return prezioa; }
    public void setPrezioa(double prezioa) { this.prezioa = prezioa; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public Integer getStockMin() { return stockMin; }
    public void setStockMin(Integer stockMin) { this.stockMin = stockMin; }

    public Integer getStockMax() { return stockMax; }
    public void setStockMax(Integer stockMax) { this.stockMax = stockMax; }

    public String getIrudia() { return irudia; }
    public void setIrudia(String irudia) { this.irudia = irudia; }

    public int getProduktuenMotakId() { return produktuenMotakId; }
    public void setProduktuenMotakId(int produktuenMotakId) { this.produktuenMotakId = produktuenMotakId; }
}
