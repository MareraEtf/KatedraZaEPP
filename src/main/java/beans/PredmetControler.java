/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Kurs;
import db.Obavestenje;
import db.Obavestenje_kurs;
import db.dbFactory;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Marko
 */
@ManagedBean
@SessionScoped
public class PredmetControler implements Serializable {

    /**
     * Creates a new instance of PredmetControler
     */
    private Session session;
    private Kurs kurs = new Kurs();
    private boolean nePostojiPredmet = false;
    private List<Obavestenje_kurs> listaObavestenja = new ArrayList<Obavestenje_kurs>();
    private List<Obavestenje> obavestenja = new ArrayList<Obavestenje>();
    private List<Obavestenje> petObavestenja = new ArrayList<Obavestenje>();
    private int ukupnoStrana, strana = 0;
    private String tipObavestenja;

    public PredmetControler() {
    }

    public String getTipObavestenja() {
        return tipObavestenja;
    }

    public void setTipObavestenja(String tipObavestenja) {
        this.tipObavestenja = tipObavestenja;
    }

    public int getUkupnoStrana() {
        return ukupnoStrana;
    }

    public void setUkupnoStrana(int ukupnoStrana) {
        this.ukupnoStrana = ukupnoStrana;
    }

    public int getStrana() {
        return strana;
    }

    public void setStrana(int strana) {
        this.strana = strana;
    }

    public List<Obavestenje> getPetObavestenja() {
        return petObavestenja;
    }

    public void setPetObavestenja(List<Obavestenje> petObavestenja) {
        this.petObavestenja = petObavestenja;
    }

    public List<Obavestenje> getObavestenja() {
        return obavestenja;
    }

    public void setObavestenja(List<Obavestenje> obavestenja) {
        this.obavestenja = obavestenja;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Kurs getKurs() {
        return kurs;
    }

    public void setKurs(Kurs kurs) {
        this.kurs = kurs;
    }

    public boolean isNePostojiPredmet() {
        return nePostojiPredmet;
    }

    public void setNePostojiPredmet(boolean nePostojiPredmet) {
        this.nePostojiPredmet = nePostojiPredmet;
    }

    public String ucitajPredmet(String nazivPredmeta) {
        session = dbFactory.getFactory().openSession();
        Query q = session.createQuery("FROM Kurs WHERE nazivKursa=:naz");
        q.setParameter("naz", nazivPredmeta);
        List<Kurs> result = q.list();
        session.close();
        if (result.size() > 0) {
            kurs = result.get(0);
        } else {
            nePostojiPredmet = true;
        }

        session = dbFactory.getFactory().openSession();
        q = session.createQuery("FROM Obavestenje_kurs WHERE kurs=:k");
        q.setParameter("k", kurs.getIDKurs());
        listaObavestenja = q.list();
        session.close();

        List<Integer> idObavestenja = new ArrayList<Integer>();
        for (int i = 0; i < listaObavestenja.size(); i++) {
            idObavestenja.add(listaObavestenja.get(i).getObavestenje());
        }

        List<Obavestenje> obavestenjaSva = new ArrayList<Obavestenje>();
        session = dbFactory.getFactory().openSession();
        q = session.createQuery("FROM Obavestenje WHERE arhivirano=0");
        obavestenjaSva = q.list();
        session.close();

        for (int i = 0; i < idObavestenja.size(); i++) {
            for (int j = 0; j < obavestenjaSva.size(); j++) {
                if (idObavestenja.get(i) == obavestenjaSva.get(j).getIDObavestenja()) {
                    obavestenja.add(obavestenjaSva.get(j));
                }
            }
        }

        petObavestenja.clear();
        ukupnoStrana = obavestenja.size() / 5;
        strana = 1;
        if (obavestenja.size() % 5 > 0) {
            ukupnoStrana++;
        }
        if (obavestenja.size() > 5) {
            for (int i = 0; i < 5; i++) {
                petObavestenja.add(obavestenja.get(i));
            }
        } else {
            for (int i = 0; i < obavestenja.size(); i++) {
                petObavestenja.add(obavestenja.get(i));
            }
        }

        return "kurs_index.xhtml";
    }

    public void pretraziObavestenja(ValueChangeEvent e) {

    }

    public void promeniStranu(ValueChangeEvent e) {

    }

}
