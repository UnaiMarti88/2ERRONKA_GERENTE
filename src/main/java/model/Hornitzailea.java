package model;

public class Hornitzailea {
    private int id;
    private String izena;
    private String helbidea;
    private String telefonoa;

    public Hornitzailea() {}

    public Hornitzailea(int id, String izena, String helbidea, String telefonoa) {
        this.id = id;
        this.izena = izena;
        this.helbidea = helbidea;
        this.telefonoa = telefonoa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public String getHelbidea() { return helbidea; }
    public void setHelbidea(String helbidea) { this.helbidea = helbidea; }

    public String getTelefonoa() { return telefonoa; }
    public void setTelefonoa(String telefonoa) { this.telefonoa = telefonoa; }

    public String getKontaktua() { return telefonoa; }
    public void setKontaktua(String kontaktua) { this.telefonoa = kontaktua; }
}