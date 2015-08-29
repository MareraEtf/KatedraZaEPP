/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Korisnik;
import db.Kurs;
import db.Predavac_kurs;
import db.dbFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Marko
 */
@ManagedBean
@SessionScoped

public class NastavniciControler implements Serializable {

    private Session session;
    private Korisnik nastavnik = new Korisnik();
    private Korisnik stariPodaci = new Korisnik();
    private List<Kurs> obavezni = new ArrayList<Kurs>();
    private List<Kurs> izborni = new ArrayList<Kurs>();
    private List<Predavac_kurs> kursevi_drzi = new ArrayList<Predavac_kurs>();
    private List<Kurs> kursevi = new ArrayList<Kurs>();
    private List<Kurs> kurseviSvi = new ArrayList<Kurs>();
    private String poruka;

    /**
     * Creates a new instance of NastavniciControler
     */
    public NastavniciControler() {
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Korisnik getStariPodaci() {
        return stariPodaci;
    }

    public void setStariPodaci(Korisnik stariPodaci) {
        this.stariPodaci = stariPodaci;
    }

    public Korisnik getNastavnik() {
        return nastavnik;
    }

    public void setNastavnik(Korisnik nastavnik) {
        this.nastavnik = nastavnik;
    }

    public List<Kurs> getObavezni() {
        return obavezni;
    }

    public void setObavezni(List<Kurs> obavezni) {
        this.obavezni = obavezni;
    }

    public List<Kurs> getIzborni() {
        return izborni;
    }

    public void setIzborni(List<Kurs> izborni) {
        this.izborni = izborni;
    }

    public List<Predavac_kurs> getKursevi_drzi() {
        return kursevi_drzi;
    }

    public void setKursevi_drzi(List<Predavac_kurs> kursevi_drzi) {
        this.kursevi_drzi = kursevi_drzi;
    }

    public List<Kurs> getKursevi() {
        return kursevi;
    }

    public void setKursevi(List<Kurs> kursevi) {
        this.kursevi = kursevi;
    }

    public List<Kurs> getKurseviSvi() {
        return kurseviSvi;
    }

    public void setKurseviSvi(List<Kurs> kurseviSvi) {
        this.kurseviSvi = kurseviSvi;
    }

    public String detalji(String ime) {

        String[] niz = new String[2];
        if (ime != null && !ime.equals("")) {
            niz = ime.split("_");
            session = dbFactory.getFactory().openSession();
            Query q = session.createQuery("FROM Korisnik WHERE ime=:i AND prezime=:p");
            q.setParameter("i", niz[0]);
            q.setParameter("p", niz[1]);

            List<Korisnik> temp = new ArrayList<Korisnik>();
            temp = q.list();
            session.close();
            if (temp.size() > 0) {
                nastavnik = temp.get(0);
            } else {
                poruka = "Ne postoji stranica za trazenog profesora";
                return "informacijeProfesor.xhtml";
            }
        }
        return "nastavnikDetalji.xhtml";
    }

    public void promeniPodatke(Korisnik korisnik) {

        session = dbFactory.getFactory().openSession();
        session.beginTransaction();
        session.update(korisnik);
        session.getTransaction().commit();
        session.close();

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("korisnik");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("korisnik", korisnik);

        FacesMessage msg = new FacesMessage("Poruka", "Uspešno ste promenili podatke");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void sacuvajStarePodatke() {

        setStariPodaci((Korisnik) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("korisnik"));

    }

    public void dohvKurseve() {

        session = dbFactory.getFactory().openSession();

        Query q = session.createQuery("FROM Predavac_kurs WHERE predavac=:id");
        q.setParameter("id", nastavnik.getIDKor());
        kursevi_drzi = q.list();
        session.close();
        if (kursevi_drzi.size() > 0) {

            dohvSveKurseve();

            for (int i = 0; i < kursevi_drzi.size(); i++) {
                for (int j = 0; j < kurseviSvi.size(); j++) {
                    if (kursevi_drzi.get(i).getKurs() == kurseviSvi.get(j).getIDKurs()) {
                        kursevi.add(kurseviSvi.get(j));
                    }
                }
            }

            for (int i = 0; i < kursevi.size(); i++) {
                if (kursevi.get(i).getTip().equals("izborni")) {
                    izborni.add(kursevi.get(i));
                } else {
                    obavezni.add(kursevi.get(i));
                }
            }
        }

    }

    public void dohvSveKurseve() {
        session = dbFactory.getFactory().openSession();
        Query q = session.createQuery("FROM Kurs");
        kurseviSvi = q.list();
        session.close();
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {

        FacesMessage msg = new FacesMessage("Upload", event.getFile().getFileName() + " je postavljen na server.");
        FacesContext.getCurrentInstance().addMessage(null, msg);

//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addMessage(null, new FacesMessage("Successful", "Your message: "));
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
//        Random r = new Random();
//        int broj = r.nextInt(100000);
//        File result = new File(extContext.getRealPath("//resources//uploads//" + Integer.toString(broj) + event.getFile().getFileName()));      
//        File result = new File("C:\\Users\\Marko\\Desktop\\proba\\" + event.getFile().getFileName());
        File result = new File("C:\\Users\\Marko\\Desktop\\FAX\\Diplomski\\Diplomski\\src\\main\\webapp\\upload\\slikePredavaca\\" + event.getFile().getFileName());
//        File result = new File(extContext.getRealPath("..//..//web//resources//uploads//" + event.getFile().getFileName()));
        UploadedFile file = event.getFile();
        Korisnik kor = (Korisnik) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("korisnik");
        kor.setSlika(event.getFile().getFileName());
        try {

            FileOutputStream fos = new FileOutputStream(result);
            InputStream is = file.getInputstream();
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];
            int a;
            while (true) {
                a = is.read(buffer);
                if (a < 0) {
                    break;
                }
                fos.write(buffer, 0, a);
                fos.flush();
            }
            fos.close();
            is.close();

        } catch (IOException e) {
        }

        session = dbFactory.getFactory().openSession();
        session.beginTransaction();
        session.update(kor);
        session.getTransaction().commit();
        session.close();
    }

    public String povratak() {

        return "promenaPodataka?faces-redirect=true";
    }

}
