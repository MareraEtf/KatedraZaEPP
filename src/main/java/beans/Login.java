/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Korisnik;
import db.dbFactory;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Marko
 */
@ManagedBean
@SessionScoped
public class Login implements Serializable {

    /**
     * Creates a new instance of Login
     */
    public Login() {
    }

    private Session session;
    private String user, passIsti, pass1, pass2, who;
    private Korisnik korisnik = new Korisnik();
    List<Korisnik> results = new ArrayList<Korisnik>();
    private boolean zaboravljena;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassIsti() {
        return passIsti;
    }

    public void setPassIsti(String passIsti) {
        this.passIsti = passIsti;
    }

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    public String getPass2() {
        return pass2;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public List<Korisnik> getResults() {
        return results;
    }

    public void setResults(List<Korisnik> results) {
        this.results = results;
    }

    public boolean isZaboravljena() {
        return zaboravljena;
    }

    public void setZaboravljena(boolean zaboravljena) {
        this.zaboravljena = zaboravljena;
    }

    public String uloguj() {
        session = dbFactory.getFactory().openSession();

        session.beginTransaction();
        Query q = session.createQuery("FROM Korisnik AS k WHERE korisnicko_ime=:kor_ime AND sifra=:sif");
        q.setParameter("kor_ime", korisnik.getKorisnicko_ime());
        q.setParameter("sif", korisnik.getSifra());

        results = q.list();

        if (results.size() > 0) {
            korisnik = results.get(0);
            korisnik.setUlogovan(true);
            session.update(korisnik);
            session.getTransaction().commit();
            session.close();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("korisnik", korisnik);
            return "index_ulogovan";
        } else {
            session.close();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Greška", "Pogrešno korisničko ime i šifra"));
        }
        return null;
    }
}
