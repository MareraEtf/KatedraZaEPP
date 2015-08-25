/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
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
@Table(name = "kurs")
public class Kurs implements Serializable {

    String nazivKursa, tip, studije, sifra;
    int godina, semestar;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    Integer IDKurs;

    public String getNazivKursa() {
        return nazivKursa;
    }

    public void setNazivKursa(String nazivKursa) {
        this.nazivKursa = nazivKursa;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Integer getIDKurs() {
        return IDKurs;
    }

    public void setIDKurs(Integer IDKurs) {
        this.IDKurs = IDKurs;
    }

    public String getStudije() {
        return studije;
    }

    public void setStudije(String studije) {
        this.studije = studije;
    }

    public int getGodina() {
        return godina;
    }

    public void setGodina(int godina) {
        this.godina = godina;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public int getSemestar() {
        return semestar;
    }

    public void setSemestar(int semestar) {
        this.semestar = semestar;
    }

}
