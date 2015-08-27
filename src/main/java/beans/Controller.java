/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Obavestenje;
import db.dbFactory;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Marko
 */
@ManagedBean
@SessionScoped
public class Controller {

    private Session session;
    private static List<Obavestenje> obavestenjaSva;
    private List<Obavestenje> petObavestenja = new ArrayList<Obavestenje>();
    private String tipObavestenja;
    private int strana;
    private static int ukupnoStrana = 0;

    /**
     * Creates a new instance of Controller
     */
    public Controller() {
    }

    public List<Obavestenje> getPetObavestenja() {
        return petObavestenja;
    }

    public void setPetObavestenja(List<Obavestenje> petObavestenja) {
        this.petObavestenja = petObavestenja;
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

    public String getTipObavestenja() {
        return tipObavestenja;
    }

    public void setTipObavestenja(String tipObavestenja) {
        this.tipObavestenja = tipObavestenja;
    }

    public List<Obavestenje> getObavestenjaSva() {
        return obavestenjaSva;
    }

    public void setObavestenjaSva(List<Obavestenje> obavestenjaSva) {
        this.obavestenjaSva = obavestenjaSva;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String inicijalizujStranicu() {

        if (tipObavestenja == null) {
            petObavestenja.clear();
            session = dbFactory.getFactory().openSession();
            Query q = session.createQuery("FROM Obavestenje WHERE arhivirano=0");
            obavestenjaSva = q.list();
            session.close();
            ukupnoStrana = obavestenjaSva.size() / 5;
            strana = 1;
            if (obavestenjaSva.size() % 5 > 0) {
                ukupnoStrana++;
            }
            for (int i = 0; i < 5; i++) {
                petObavestenja.add(obavestenjaSva.get(i));
            }
            return "obavestenja.xhtml";
        }
        return "obavestenja.xhtml";
    }

    public void dohvSvaObavestenja() {

        if (tipObavestenja == null) {
            session = dbFactory.getFactory().openSession();
            Query q = session.createQuery("FROM Obavestenje WHERE arhivirano=0");
            obavestenjaSva = q.list();
            session.close();
        }

    }

    public void pretraziObavestenja(ValueChangeEvent e) {
        if (e.getNewValue() != null) {
            if (e.getNewValue().equals("sve")) {
                petObavestenja.clear();
                session = dbFactory.getFactory().openSession();
                Query q = session.createQuery("FROM Obavestenje WHERE arhivirano=0");
                obavestenjaSva = q.list();
                session.close();
            } else {
                petObavestenja.clear();
                session = dbFactory.getFactory().openSession();
                Query q = session.createQuery("FROM Obavestenje WHERE tip=:t AND arhivirano=0");
                q.setParameter("t", e.getNewValue());
                obavestenjaSva = q.list();
                session.close();
            }
            ukupnoStrana = obavestenjaSva.size() / 5;
            strana = 1;
            if (obavestenjaSva.size() % 5 > 0) {
                ukupnoStrana++;
            }
            if (obavestenjaSva.size() >= 5) {
                for (int i = 0; i < 5; i++) {
                    petObavestenja.add(obavestenjaSva.get(i));
                }
            } else {
                for (int i = 0; i < obavestenjaSva.size(); i++) {
                    petObavestenja.add(obavestenjaSva.get(i));
                }
            }

        }

    }

    public void promeniStranu(ValueChangeEvent e) {
        if (tipObavestenja == null || tipObavestenja.equals("") || tipObavestenja.equals("sve")) {
            session = dbFactory.getFactory().openSession();
            Query q = session.createQuery("FROM Obavestenje WHERE arhivirano=0");
            obavestenjaSva = q.list();
            session.close();
        } else {
            session = dbFactory.getFactory().openSession();
            Query q = session.createQuery("FROM Obavestenje WHERE tip=:t AND arhivirano=0");
            q.setParameter("t", tipObavestenja);
            obavestenjaSva = q.list();
            session.close();
        }
        petObavestenja.clear();
        strana = (int) e.getNewValue();
        if (strana < ukupnoStrana) {
            int j = 0;
            for (int i = (strana * 5) - 5; i < (strana * 5); i++) {
                petObavestenja.add(obavestenjaSva.get(i));
                j++;
            }
        } else {
            int j = 0;
            for (int i = (strana * 5) - 5; i < obavestenjaSva.size(); i++) {
                petObavestenja.add(obavestenjaSva.get(i));
                j++;
            }
        }
    }

}
