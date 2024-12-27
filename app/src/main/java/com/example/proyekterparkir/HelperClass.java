package com.example.proyekterparkir;

public class HelperClass {
    String nama, email, pass, konf_pass, no_hp;

    public HelperClass(String nama, String email, String pass, String konf_pass, String no_hp) {
        this.nama = nama;
        this.email = email;
        this.pass = pass;
        this.konf_pass = konf_pass;
        this.no_hp = no_hp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getKonf_pass() {
        return konf_pass;
    }

    public void setKonf_pass(String konf_pass) {
        this.konf_pass = konf_pass;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }
}
