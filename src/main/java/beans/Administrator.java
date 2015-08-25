/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Korisnik;
import db.Kurs;
import db.Kurs_stavke;
import db.Materijal;
import db.Obavestenje;
import db.Predavac_kurs;
import db.dbFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;

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

    //obavestenje
    private String nazivObavestenja, tekstObavestenja;
    private Obavestenje obavestenje = new Obavestenje();

    //kurs
    private String nazivKursa, sifra, studije, tip;
    int godina, semestar;
    private Kurs kurs = new Kurs();
    private DualListModel<Korisnik> predavaci;
    private List<Korisnik> predavaciSvi = new ArrayList<Korisnik>();
    private List<Korisnik> predavaciIzabrani = new ArrayList<Korisnik>();
    private String odabraniPredavaci;
    private String[] odabraniPredavaciNiz;

    private DualListModel<String> stavke;
    private List<String> sourceStavke = new ArrayList<String>();
    private List<String> targetStavke = new ArrayList<String>();
    private boolean flag = false;

    public Administrator() {
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public DualListModel<String> getStavke() {
        return stavke;
    }

    public void setStavke(DualListModel<String> stavke) {
        this.stavke = stavke;
    }

    public Kurs getKurs() {
        return kurs;
    }

    public void setKurs(Kurs kurs) {
        this.kurs = kurs;
    }

    public DualListModel<Korisnik> getPredavaci() {
        return predavaci;
    }

    public void setPredavaci(DualListModel<Korisnik> predavaci) {
        this.predavaci = predavaci;
    }

    public List<Korisnik> getPredavaciSvi() {
        return predavaciSvi;
    }

    public void setPredavaciSvi(List<Korisnik> predavaciSvi) {
        this.predavaciSvi = predavaciSvi;
    }

    public List<Korisnik> getPredavaciIzabrani() {
        return predavaciIzabrani;
    }

    public void setPredavaciIzabrani(List<Korisnik> predavaciIzabrani) {
        this.predavaciIzabrani = predavaciIzabrani;
    }

    public String getNazivKursa() {
        return nazivKursa;
    }

    public void setNazivKursa(String nazivKursa) {
        this.nazivKursa = nazivKursa;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getStudije() {
        return studije;
    }

    public void setStudije(String studije) {
        this.studije = studije;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getGodina() {
        return godina;
    }

    public void setGodina(int godina) {
        this.godina = godina;
    }

    public int getSemestar() {
        return semestar;
    }

    public void setSemestar(int semestar) {
        this.semestar = semestar;
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ObaveÅ¡tenje sa istim nazivom veÄ‡ postoji", ""));
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

    public void dodajKurs() {
        session = dbFactory.getFactory().openSession();
        Query q = session.createQuery("FROM Kurs WHERE nazivKursa=:naz");
        q.setParameter("naz", kurs.getNazivKursa());
        List<Kurs> results = q.list();
        if (results.size() > 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kurs sa istim nazivom veÄ‡ postoji", ""));
            session.close();
            return;
        } else {
            session.beginTransaction();
            session.save(kurs);
            session.getTransaction().commit();
            if (predavaci.getTarget().size() > 0) {
                int id;
                Predavac_kurs pk = new Predavac_kurs();
                q = session.createQuery("FROM Kurs WHERE nazivKursa=:naz");
                q.setParameter("naz", kurs.getNazivKursa());
                results = q.list();
                if (results.size() > 0) {
                    id = results.get(0).getIDKurs();
                    odabraniPredavaci = predavaci.getTarget().toString();
                    String subString = odabraniPredavaci.substring(1, odabraniPredavaci.length() - 1);

                    odabraniPredavaciNiz = new String[predavaci.getTarget().size()];
                    odabraniPredavaciNiz = subString.split(", ");

//                    q = session.createQuery("FROM Korisnik WHERE tip=1");
//                    List<Korisnik> res = q.list();
                    for (int i = 0; i < odabraniPredavaciNiz.length; i++) {
                        int idPredavac = Integer.parseInt(odabraniPredavaciNiz[i]);
                        session = dbFactory.getFactory().openSession();
                        session.beginTransaction();
                        pk.setPredavac(idPredavac);
                        pk.setKurs(id);
                        session.save(pk);
                        session.getTransaction().commit();
                        session.close();
                    }
                    Kurs_stavke ks = new Kurs_stavke();
                    if (stavke.getTarget().size() > 0) {

                        for (int i = 0; i < stavke.getTarget().size(); i++) {
                            session = dbFactory.getFactory().openSession();
                            session.beginTransaction();
                            ks.setKurs(id);
                            ks.setStavka(stavke.getTarget().get(i));
                            session.save(ks);
                            session.getTransaction().commit();
                            session.close();
                        }

                    } else {
                        for (int i = 0; i < stavke.getSource().size(); i++) {
                            session = dbFactory.getFactory().openSession();
                            session.beginTransaction();
                            ks.setKurs(id);
                            ks.setStavka(stavke.getSource().get(i));
                            session.save(ks);
                            session.getTransaction().commit();
                            session.close();
                        }
                    }

                }
//                session.close();
            }
            flag = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Uspesno je dodat kurs, sada mozete dodati i materijale za tekuci kurs", ""));
            return;
        }

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
        File result = new File("C:\\Users\\Marko\\Desktop\\FAX\\Diplomski\\Diplomski\\src\\main\\webapp\\upload\\materijali\\" + event.getFile().getFileName());
//        File result = new File(extContext.getRealPath("..//..//web//resources//uploads//" + event.getFile().getFileName()));
        UploadedFile file = event.getFile();
        Materijal materijal = new Materijal();

        session = dbFactory.getFactory().openSession();
        Query q = session.createQuery("FROM Kurs");
        List<Kurs> kursevi = q.list();
        session.close();
        Kurs k = new Kurs();
        if (kursevi.size() > 0) {
            k = kursevi.get(kursevi.size() - 1);
        }

        materijal.setPodatak(event.getFile().getFileName());
        materijal.setKurs(k.getIDKurs());
        materijal.setTip("predavanja");

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
        session.save(materijal);
        session.getTransaction().commit();
        session.close();
    }

    public void handleFileUploadVezbe(FileUploadEvent event) throws IOException {

        FacesMessage msg = new FacesMessage("Upload", event.getFile().getFileName() + " je postavljen na server.");
        FacesContext.getCurrentInstance().addMessage(null, msg);

//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addMessage(null, new FacesMessage("Successful", "Your message: "));
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
//        Random r = new Random();
//        int broj = r.nextInt(100000);
//        File result = new File(extContext.getRealPath("//resources//uploads//" + Integer.toString(broj) + event.getFile().getFileName()));      
//        File result = new File("C:\\Users\\Marko\\Desktop\\proba\\" + event.getFile().getFileName());
        File result = new File("C:\\Users\\Marko\\Desktop\\FAX\\Diplomski\\Diplomski\\src\\main\\webapp\\upload\\materijali\\" + event.getFile().getFileName());
//        File result = new File(extContext.getRealPath("..//..//web//resources//uploads//" + event.getFile().getFileName()));
        UploadedFile file = event.getFile();
        Materijal materijal = new Materijal();

        session = dbFactory.getFactory().openSession();
        Query q = session.createQuery("FROM Kurs");
        List<Kurs> kursevi = q.list();
        session.close();
        Kurs k = new Kurs();
        if (kursevi.size() > 0) {
            k = kursevi.get(kursevi.size() - 1);
        }

        materijal.setPodatak(event.getFile().getFileName());
        materijal.setKurs(k.getIDKurs());
        materijal.setTip("vezbe");

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
        session.save(materijal);
        session.getTransaction().commit();
        session.close();
    }

    public void inicijalizujPredavace() {

        session = dbFactory.getFactory().openSession();
        Query q = session.createQuery("FROM Korisnik WHERE tip=1");
        predavaciSvi = q.list();
        session.close();
        predavaci = new DualListModel<>(predavaciSvi, predavaciIzabrani);

        sourceStavke.add("obavestenja");
        sourceStavke.add("informacije");
        sourceStavke.add("materijali");
        sourceStavke.add("ispitni zadaci");
        sourceStavke.add("laboratorija/projektni zadaci");

        stavke = new DualListModel<>(sourceStavke, targetStavke);

    }

}
