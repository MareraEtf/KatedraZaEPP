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
@Table(name = "predavac_kurs")
public class Predavac_kurs implements Serializable {

    int predavac, kurs;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    Integer IDPK;

    public int getPredavac() {
        return predavac;
    }

    public void setPredavac(int predavac) {
        this.predavac = predavac;
    }

    public int getKurs() {
        return kurs;
    }

    public void setKurs(int kurs) {
        this.kurs = kurs;
    }

    public Integer getIDPK() {
        return IDPK;
    }

    public void setIDPK(Integer IDPK) {
        this.IDPK = IDPK;
    }

}
