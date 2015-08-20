/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Obavestenje;
import db.dbFactory;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.sound.midi.SysexMessage;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Marko
 */
@ManagedBean
@SessionScoped
public class Administrator implements Serializable {

    /**
     * Creates a new instance of Administrator
     */
    private Session session;
    private String nazivObavestenja, tekstObavestenja;
    private Obavestenje obavestenje = new Obavestenje();

    public Administrator() {
    }

    public Obavestenje getObavestenje() {
        return obavestenje;
    }

    public void setObavestenje(Obavestenje obavestenje) {
        this.obavestenje = obavestenje;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getNazivObavestenja() {
        return nazivObavestenja;
    }

    public void setNazivObavestenja(String nazivObavestenja) {
        this.nazivObavestenja = nazivObavestenja;
    }

    public String getTekstObavestenja() {
        return tekstObavestenja;
    }

    public void setTekstObavestenja(String tekstObavestenja) {
        this.tekstObavestenja = tekstObavestenja;
    }

    public String dodajObavestenje() {

        session = dbFactory.getFactory().openSession();

        Query q = session.createQuery("SELECT naziv FROM Obavestenje WHERE naziv=:naz");
        q.setParameter("naz", obavestenje.getNaziv());
        List results = q.list();

        long vreme = System.currentTimeMillis();

        Calendar datum = Calendar.getInstance();
        datum.setTimeInMillis(vreme);

        if (results.size() > 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Obaveštenje sa istim nazivom već postoji", ""));
            session.close();
            return null;
        } else {
            session.beginTransaction();
            obavestenje.setDatum(datum);
            session.save(obavestenje);
            session.getTransaction().commit();
            session.close();
            return "obavestenja.xtml";
        }
    }

}
