/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Obavestenje;
import db.dbFactory;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Marko
 */
@ManagedBean
@Dependent
public class Controller {

    private Session session;
    private List<Obavestenje> obavestenja;
    private String tipObavestenja;

    /**
     * Creates a new instance of Controller
     */
    public Controller() {
    }

    public String getTipObavestenja() {
        return tipObavestenja;
    }

    public void setTipObavestenja(String tipObavestenja) {
        this.tipObavestenja = tipObavestenja;
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

    public void dohvSvaObavestenja() {

        if (tipObavestenja == null) {
            session = dbFactory.getFactory().openSession();
            Query q = session.createQuery("FROM Obavestenje WHERE arhivirano=0");
            obavestenja = q.list();
            session.close();
        }

    }

    public void pretraziObavestenja(ValueChangeEvent e) {

        session = dbFactory.getFactory().openSession();
        Query q = session.createQuery("FROM Obavestenje WHERE tip=:t AND arhivirano=0");
        q.setParameter("t", e.getNewValue());
        obavestenja = q.list();
        session.close();

    }

}
