
package model;

public class Erabiltzailea {

    private int id;
    private String izena;
    private String abizena;
    private String erabiltzailea;
    private String pasahitza;
    private String email;
    private String telefonoa;
    private int baimena;
    private Integer mahaiakId;
    private int txatBaimena;

    public Erabiltzailea() {}

    public Erabiltzailea(int id, String izena, String abizena,
                         String erabiltzailea, String pasahitza,
                         String email, String telefonoa,
                         int baimena, Integer mahaiakId, int txatBaimena) {
        this.id = id;
        this.izena = izena;
        this.abizena = abizena;
        this.erabiltzailea = erabiltzailea;
        this.pasahitza = pasahitza;
        this.email = email;
        this.telefonoa = telefonoa;
        this.baimena = baimena;
        this.mahaiakId = mahaiakId;
        this.txatBaimena = txatBaimena;
    }

    public Erabiltzailea(String erabiltzailea, String pasahitza) {
        this.erabiltzailea = erabiltzailea;
        this.pasahitza = pasahitza;
    }

    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public String getAbizena() { return abizena; }
    public void setAbizena(String abizena) { this.abizena = abizena; }

    public String getErabiltzailea() { return erabiltzailea; }
    public void setErabiltzailea(String erabiltzailea) { this.erabiltzailea = erabiltzailea; }

    public String getPasahitza() { return pasahitza; }
    public void setPasahitza(String pasahitza) { this.pasahitza = pasahitza; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefonoa() { return telefonoa; }
    public void setTelefonoa(String telefonoa) { this.telefonoa = telefonoa; }

    public int getBaimena() { return baimena; }
    public void setBaimena(int baimena) { this.baimena = baimena; }

    public Integer getMahaiakId() { return mahaiakId; }
    public void setMahaiakId(Integer mahaiakId) { this.mahaiakId = mahaiakId; }

    public int getTxatBaimena() { return txatBaimena; }
    public void setTxatBaimena(int txatBaimena) { this.txatBaimena = txatBaimena; }

    
    public String getRola() {
        switch (baimena) {
            case 1: return "administratzailea";
            case 0: return "zerbitzaria";
            default: return "Desconocido";
        }
    }
}
