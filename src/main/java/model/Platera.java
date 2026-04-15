package model;

public class Platera {
    private int id;
    private String izena;
    private String mota;
    private double prezioa;
    private int plateraMotakId;

    public Platera() {}

    public Platera(int id, String izena, String mota, double prezioa, int plateraMotakId) {
        this.id = id;
        this.izena = izena;
        this.mota = mota;
        this.prezioa = prezioa;
        this.plateraMotakId = plateraMotakId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public String getMota() { return mota; }
    public void setMota(String mota) { this.mota = mota; }

    public double getPrezioa() { return prezioa; }
    public void setPrezioa(double prezioa) { this.prezioa = prezioa; }

    public int getPlateraMotakId() { return plateraMotakId; }
    public void setPlateraMotakId(int plateraMotakId) { this.plateraMotakId = plateraMotakId; }
}
