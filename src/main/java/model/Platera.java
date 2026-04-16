package model;

public class Platera {
    private int id;
    private String izena;
    private String mota;
    private double prezioa;

    public Platera() {}

    public Platera(int id, String izena, String mota, double prezioa) {
        this.id = id;
        this.izena = izena;
        this.mota = mota;
        this.prezioa = prezioa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public String getMota() { return mota; }
    public void setMota(String mota) { this.mota = mota; }

    public double getPrezioa() { return prezioa; }
    public void setPrezioa(double prezioa) { this.prezioa = prezioa; }
}
