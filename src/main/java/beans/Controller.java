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

    /**
     * Creates a new instance of Controller
     */
    public Controller() {
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
        session = dbFactory.getFactory().openSession();
        Query q = session.createQuery("FROM Obavestenje");
        obavestenja = q.list();
        session.close();
        
    }

}
