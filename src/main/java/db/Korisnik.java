/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Marko
 */
@Entity
@Table(name = "korisnik")
public class Korisnik {

    String ime, prezime, korisnicko_ime, sifra, email;
    int tip;
    boolean ulogovan;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    Integer IDKor;

    public Korisnik() {

    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public boolean isUlogovan() {
        return ulogovan;
    }

    public void setUlogovan(boolean ulogovan) {
        this.ulogovan = ulogovan;
    }

    public Integer getIDKor() {
        return IDKor;
    }

    public void setIDKor(Integer IDKor) {
        this.IDKor = IDKor;
    }
}
