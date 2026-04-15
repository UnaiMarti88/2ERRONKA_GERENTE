package model;

import java.time.LocalDateTime;

public class Erreserba {
    private int id;
    private LocalDateTime data;
    private int mota;
    private Integer erabiltzaileakId;
    private int mahaiakId;

    public Erreserba() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public int getMota() { return mota; }
    public void setMota(int mota) { this.mota = mota; }

    public Integer getErabiltzaileakId() { return erabiltzaileakId; }
    public void setErabiltzaileakId(Integer erabiltzaileakId) { this.erabiltzaileakId = erabiltzaileakId; }

    public int getMahaiakId() { return mahaiakId; }
    public void setMahaiakId(int mahaiakId) { this.mahaiakId = mahaiakId; }

    
    public String getBezeroIzena() { return "Bezeroa " + (erabiltzaileakId != null ? erabiltzaileakId : "Anonimoa"); }
    public String getTelefonoa() { return "N/A"; }
    public int getPertsonaKopurua() { return 0; }
    public LocalDateTime getEgunaOrdua() { return data; }
    public Double getPrezioTotala() { return 0.0; }
    public boolean isOrdainduta() { return false; }
    public String getFakturaRuta() { return null; }
    public int getLangileaId() { return 0; }
    public int getMahaiaId() { return mahaiakId; }
    public String getLangileaIzena() { return "N/A"; }
    public String getMahaiaLabel() { return "Mahaia " + mahaiakId; }

    public void setBezeroIzena(String s) {}
    public void setTelefonoa(String s) {}
    public void setPertsonaKopurua(int i) {}
    public void setEgunaOrdua(LocalDateTime dt) { this.data = dt; }
    public void setPrezioTotala(Double d) {}
    public void setOrdainduta(boolean b) {}
    public void setFakturaRuta(String s) {}
    public void setLangileaId(int i) {}
    public void setMahaiaId(int i) { this.mahaiakId = i; }
    public void setLangileaIzena(String s) {}
    public void setMahaiaLabel(String s) {}
}