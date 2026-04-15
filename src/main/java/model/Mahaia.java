package model;

public class Mahaia {

    private int id;
    private String izena;
    private String egoera; 

    public Mahaia(int id, String izena, String egoera) {
        this.id = id;
        this.izena = izena;
        this.egoera = egoera;
    }

    public int getId() { return id; }
    public String getIzena() { return izena; }
    public String getEgoera() { return egoera; }

    public void setId(int id) { this.id = id; }
    public void setIzena(String izena) { this.izena = izena; }
    public void setEgoera(String egoera) { this.egoera = egoera; }
}
