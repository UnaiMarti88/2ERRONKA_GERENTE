package model;

public class Mahaia {

    private int id;
    private String izena;
    private String erabiltzailea;
    private String pasahitza;
    private int chatBaimena; 

    public Mahaia(int id, String izena, String erabiltzailea, String pasahitza, int chatBaimena) {
        this.id = id;
        this.izena = izena;
        this.erabiltzailea = erabiltzailea;
        this.pasahitza = pasahitza;
        this.chatBaimena = chatBaimena;
    }

    public int getId() { return id; }
    public String getIzena() { return izena; }
    public String getErabiltzailea() { return erabiltzailea; }
    public String getPasahitza() { return pasahitza; }
    public int getChatBaimena() { return chatBaimena; }

    public void setId(int id) { this.id = id; }
    public void setIzena(String izena) { this.izena = izena; }
    public void setErabiltzailea(String erabiltzailea) { this.erabiltzailea = erabiltzailea; }
    public void setPasahitza(String pasahitza) { this.pasahitza = pasahitza; }
    public void setChatBaimena(int chatBaimena) { this.chatBaimena = chatBaimena; }
}
