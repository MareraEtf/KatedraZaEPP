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
@Table(name = "materijal")
public class Materijal implements Serializable {

    String podatak, tip;
    int kurs;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    Integer IDMat;

    public String getPodatak() {
        return podatak;
    }

    public void setPodatak(String podatak) {
        this.podatak = podatak;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getKurs() {
        return kurs;
    }

    public void setKurs(int kurs) {
        this.kurs = kurs;
    }

    public Integer getIDMat() {
        return IDMat;
    }

    public void setIDMat(Integer IDMat) {
        this.IDMat = IDMat;
    }

}
