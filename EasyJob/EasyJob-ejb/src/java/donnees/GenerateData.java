/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package donnees;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import persistence.Adresse;
import persistence.Annonce;
import persistence.Candidat;
import persistence.CandidatureAnnonce;
import persistence.CandidatureSpontanee;
import persistence.Employeur;
import persistence.Entreprise;
import persistence.NotificationCandidat;
import persistence.NotificationCandidatureS;
import persistence.NotificationEmployeur;
import persistence.SuggestionAnnonce;
import persistence.SuggestionCandidat;

/**
 *
 * @author celie
 */

@Singleton
@Startup
public class GenerateData {

    @PersistenceContext(unitName = "EasyJob-PU")
    private EntityManager em;
    private Employeur emp;
    private Employeur provEmp;
    
    private Candidat candTest,candTest2;
    private Annonce annonceTest;
    
    private Employeur empTest;
    

    
    public GenerateData() {      
    }
    
    @PostConstruct
    public void intitialisation() {
        this.addCandidats();
       this.addEmployeurs();
    /*    this.addAnnonce();*/
    }
    
    public void addCandidats() {
        /* Création d'un premier candidat */
        System.out.println("hfuhfuhfruhi");
        GregorianCalendar date = new GregorianCalendar(1968, 0, 7);
        Candidat c = new Candidat("Debrat","Henri","0395864899","1526975694102",date.getTime(),true);
        c.setAdresse(new Adresse("3 rue des tulipes","54000","Nancy","France"));
        c.setDomaineEtudes("Informatique");
        ArrayList<String> ex = new ArrayList<String>();
        ex = new ArrayList<String>();
        ex.add("anglais-moyen");
        c.setLangues(ex);
        c.setMdp("test");
        c.setMail("hd@test.fr");
        c.setNbExperiences(10);
        c.setSitWeb("www.test.fr/hd");
        c.setNiveauEtudes("BAC+7");
        candTest=c;// récupère le candidat test
        em.persist(c);              
        
       
    }
    
    public void addEmployeurs() {
        Entreprise en = new Entreprise("MI6","Enquetes et sécurite","0156917582","SA", new Adresse("3 rue des Géraniums","444719","Londres","UK"));
        en.setSiteWeb("www.mi6.fr");
        en.setDescription("On sauve le monde.");
        em.persist(en);
        
        /* Création d'un premier employeur */
        emp = new Employeur("Mallory","Gareth","gm@mi6.fr","test",true,en);
        en.getEmployeurs().add(emp);
        em.persist(emp);
        provEmp = emp;
        empTest = emp;
             
    }
    
    public void addAnnonce() {
        Annonce an = new Annonce("annonce1","blablabla","CDI tout public","BAC+2",provEmp,1500,35);
        an.setSecteur("Restauration");
        em.persist(an);
        annonceTest=an;
        
        NotificationEmployeur n = new NotificationEmployeur();
        n.setDestinataire(empTest);
        n.setMessage("Un candidat a postuler à une annonce");
        /*CandidatureAnnonce ca = new CandidatureAnnonce();
        ca.setAnnonce(an);
        ca.setCandidat(candTest);
        an.getCandidatures().add(ca);
        File f = new File("~/rapport.pdf");
        ca.setCv(f);
        ca.setLettre(f);
        em.persist(ca);
        n.setCandidature(ca);*/
        em.persist(n);
        empTest.getNotifications().add(n);
        
        
        Candidat c = new Candidat();
        c.setNom("Tata");
        c.setPrenom("Titi");
        em.persist(c);
        n = new NotificationEmployeur();
        n.setDestinataire(empTest);
        n.setMessage("Un candidat a postuler à une annonce");
        /*ca = new CandidatureAnnonce();
        ca.setAnnonce(an);
        ca.setCandidat(c);
        em.persist(ca);
        an.getCandidatures().add(ca);*/
        em.merge(an);
        //n.setCandidature(ca);
        em.persist(n);
        empTest.getNotifications().add(n);
        
        empTest.getCandidats().add(c);
        empTest.getCandidats().add(candTest);
        em.merge(empTest);
        
        SuggestionAnnonce sugges = new SuggestionAnnonce(annonceTest, c, candTest);
        candTest.getSuggestions().add(sugges);
        em.merge(candTest);
        em.persist(sugges);        
        
        
        an = new Annonce("annonce2","blablabla","CDD alternance","BAC+5",emp,2300,35);
        an.setLieu(new Adresse("5 rue des rosiers","54000","Nancy","France"));
        an.setExperience(3);
        an.setSecteur("Culture et production animale, chasse et services annexes");
        Date d = new Date();
        d.setYear(113);
        d.setMonth(7);
        d.setDate(30);
        an.setDateLimite(d);
        em.persist(an);
        
        sugges = new SuggestionAnnonce(an, c, candTest);
        em.persist(sugges);
        candTest.getSuggestions().add(sugges);
        em.merge(candTest);
        
        an = new Annonce("annonce3","blablabla","CDD alternance","BAC+5",emp,2300,35);
        an.setLieu(new Adresse("5 rue des rosiers","54000","Nancy","France"));
        an.setSecteur("Restauration");
        d = new Date();
        d.setYear(113);
        d.setMonth(3);
        d.setDate(30);
        an.setDateLimite(d);
        em.persist(an);
        
    }

    


}
