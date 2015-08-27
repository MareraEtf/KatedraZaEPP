/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Kurs;
import db.Kurs_stavke;
import db.Materijal;
import db.Obavestenje;
import db.Obavestenje_kurs;
import db.dbFactory;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Material;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
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
    private List<Kurs_stavke> stavkeMenija = new ArrayList<Kurs_stavke>();
    private String poruka;

    //materijali
    private List<Materijal> materijali = new ArrayList<Materijal>();
    private List<Materijal> materijaliPredavanja = new ArrayList<Materijal>();
    private List<Materijal> materijaliVezbe = new ArrayList<Materijal>();

    public PredmetControler() {
    }

    public List<Materijal> getMaterijaliPredavanja() {
        return materijaliPredavanja;
    }

    public void setMaterijaliPredavanja(List<Materijal> materijaliPredavanja) {
        this.materijaliPredavanja = materijaliPredavanja;
    }

    public List<Materijal> getMaterijaliVezbe() {
        return materijaliVezbe;
    }

    public void setMaterijaliVezbe(List<Materijal> materijaliVezbe) {
        this.materijaliVezbe = materijaliVezbe;
    }

    public List<Materijal> getMaterijali() {
        return materijali;
    }

    public void setMaterijali(List<Materijal> materijali) {
        this.materijali = materijali;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
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

    public List<Kurs_stavke> getStavkeMenija() {
        return stavkeMenija;
    }

    public void setStavkeMenija(List<Kurs_stavke> stavkeMenija) {
        this.stavkeMenija = stavkeMenija;
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
            poruka = "Stranica predmeta je u izradi";
            return "informacije.xhtml";
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

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("obavestenjaKurs", obavestenja);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("kurs", kurs);

        petObavestenja.clear();
        ukupnoStrana = obavestenja.size() / 5;
        strana = 1;
        if (obavestenja.size() % 5 > 0) {
            ukupnoStrana++;
        }
        if (obavestenja.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                petObavestenja.add(obavestenja.get(i));
            }
        } else {
            for (int i = 0; i < obavestenja.size(); i++) {
                petObavestenja.add(obavestenja.get(i));
            }
        }

        session = dbFactory.getFactory().openSession();
        q = session.createQuery("FROM Kurs_stavke WHERE kurs=:id");
        q.setParameter("id", kurs.getIDKurs());
        stavkeMenija = q.list();
        session.close();

        return "kurs_index.xhtml";
    }

    public void pretraziObavestenja(ValueChangeEvent e) {

        if (e.getNewValue() != null) {
            List<Obavestenje> obavestenjaKurs = new ArrayList<Obavestenje>();
            if (e.getNewValue().equals("sve")) {
                petObavestenja.clear();
                obavestenjaKurs = (List<Obavestenje>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("obavestenjaKurs");
            } else {
                petObavestenja.clear();
                List<Obavestenje> obavestenjaTemp = (List<Obavestenje>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("obavestenjaKurs");
                for (int i = 0; i < obavestenjaTemp.size(); i++) {
                    if (obavestenjaTemp.get(i).getTip().equals(e.getNewValue())) {
                        obavestenjaKurs.add(obavestenjaTemp.get(i));
                    }
                }
            }
            if (obavestenjaKurs.size() > 0) {
                ukupnoStrana = obavestenjaKurs.size() / 5;
            } else {
                ukupnoStrana = 1;
            }
            strana = 1;
            if (obavestenjaKurs.size() % 5 > 0) {
                ukupnoStrana++;
            }
            if (obavestenjaKurs.size() >= 5) {
                for (int i = 0; i < 5; i++) {
                    petObavestenja.add(obavestenjaKurs.get(i));
                }
            } else {
                for (int i = 0; i < obavestenjaKurs.size(); i++) {
                    petObavestenja.add(obavestenjaKurs.get(i));
                }
            }

            Kurs k = (Kurs) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("kurs");

            session = dbFactory.getFactory().openSession();
            Query q = session.createQuery("FROM Kurs_stavke WHERE kurs=:id");
            q.setParameter("id", k.getIDKurs());
            stavkeMenija = q.list();
            session.close();

        }
    }

    public void promeniStranu(ValueChangeEvent e) {
        List<Obavestenje> obavestenjaKurs = new ArrayList<Obavestenje>();
        if (tipObavestenja == null || tipObavestenja.equals("") || tipObavestenja.equals("sve")) {
            obavestenjaKurs = (List<Obavestenje>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("obavestenjaKurs");
        } else {
            List<Obavestenje> obavestenjaTemp = (List<Obavestenje>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("obavestenjaKurs");
            for (int i = 0; i < obavestenjaTemp.size(); i++) {
                if (obavestenjaTemp.get(i).getTip().equals(e.getNewValue())) {
                    obavestenjaKurs.add(obavestenjaTemp.get(i));
                }
            }
        }
        petObavestenja.clear();
        strana = (int) e.getNewValue();
        if (strana < ukupnoStrana) {
            for (int i = (strana * 5) - 5; i < (strana * 5); i++) {
                petObavestenja.add(obavestenjaKurs.get(i));
            }
        } else {
            for (int i = (strana * 5) - 5; i < obavestenjaKurs.size(); i++) {
                petObavestenja.add(obavestenjaKurs.get(i));
            }
        }

        Kurs k = (Kurs) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("kurs");
        session = dbFactory.getFactory().openSession();
        Query q = session.createQuery("FROM Kurs_stavke WHERE kurs=:id");
        q.setParameter("id", k.getIDKurs());
        stavkeMenija = q.list();
        session.close();

    }

    public void akcija() {

    }

    public String materijali() {
        Kurs k = (Kurs) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("kurs");
        session = dbFactory.getFactory().openSession();
        Query q = session.createQuery("FROM Materijal WHERE kurs=:id");
        q.setParameter("id", k.getIDKurs());
        materijali = q.list();
        session.close();

        for (int i = 0; i < materijali.size(); i++) {
            if (materijali.get(i).getTip().equals("predavanja")) {
                materijaliPredavanja.add(materijali.get(i));
            } else {
                materijaliVezbe.add(materijali.get(i));
            }
        }

        return "kurs_materijali.xhtml";
    }

}
