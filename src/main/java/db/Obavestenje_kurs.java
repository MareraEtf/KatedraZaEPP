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
@Table(name = "obavestenje_kurs")
public class Obavestenje_kurs implements Serializable {

    int obavestenje, kurs;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    Integer IDOK;

    public int getObavestenje() {
        return obavestenje;
    }

    public void setObavestenje(int obavestenje) {
        this.obavestenje = obavestenje;
    }

    public int getKurs() {
        return kurs;
    }

    public void setKurs(int kurs) {
        this.kurs = kurs;
    }

    public Integer getIDOK() {
        return IDOK;
    }

    public void setIDOK(Integer IDOK) {
        this.IDOK = IDOK;
    }

}
