/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package donnees;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import persistence.Adresse;
import persistence.Annonce;
import persistence.Candidat;
import persistence.Employeur;
import persistence.Entreprise;

/**
 *
 * @author celie
 */
//@Stateless
//@LocalBean
@Singleton
@Startup
public class GenerateData {

    @PersistenceContext(unitName = "EasyJob-PU")
    private EntityManager em;
    private Employeur emp;
    
    public GenerateData() {      
    }
    
    @PostConstruct
    public void intitialisation() {
        this.addCandidats();
        this.addEmployeurs();
        this.addAnnonce();
    }
    
    public void addCandidats() {
        /* Création d'un premier candidat */
        GregorianCalendar date = new GregorianCalendar(1978, 0, 7);
        Candidat c = new Candidat("test","test","0395864899","1526975694102",date.getTime(),true);
        c.setAdresse(new Adresse("3 rue des tulipes","75000","Paris","France"));
        c.setDomaineEtudes("informatique");
        ArrayList<String> ex = new ArrayList<String>();
        ex.add("1996-2000 : blabla");
        ex.add("2000-2012 : blabla");
        c.setExperiences(ex);
        ex = new ArrayList<String>();
        ex.add("anglais-moyen");
        c.setLangues(ex);
        c.setMdp("test");
        c.setMail("test@test.fr");
        c.setNbExperiences(10);
        c.setSitWeb("www.test.fr/toto");
        c.setNiveauEtudes("BAC");
        em.persist(c);
        
        /* Création d'un deuxième candidat */
        date = new GregorianCalendar(1965, 4, 23);
        c = new Candidat("Dupond","Jeanne","0395642889","2526558694102",date.getTime(),false);
        c.setAdresse(new Adresse("5 impasse des violettes","54000","Nancy","France"));
        c.setDomaineEtudes("vente");
        ex = new ArrayList<String>();
        ex.add("1976-1988 : blabla");
        ex.add("1990-2011 : blabla");
        c.setExperiences(ex);
        ex = new ArrayList<String>();
        c.setLangues(ex);
        c.setMdp("toto");
        c.setMail("toto@toto.fr");
        c.setNbExperiences(25);  
        c.setNiveauEtudes("BAC+5");  
        em.persist(c);
       
        /* Création d'un troisième candidat */
        date = new GregorianCalendar(1990, 4, 23);
        c = new Candidat("Durant","Paul","0595642889","1426558697102",date.getTime(),false);
        c.setAdresse(new Adresse("5 avenue des camélias","69000","Lyon","France"));
        c.setDomaineEtudes("santé");
        ex = new ArrayList<String>();
        ex.add("2009-2010 : blabla");
        c.setExperiences(ex);
        ex = new ArrayList<String>();
        ex.add("anglais-expert");
        ex.add("espagnol-parlé");
        c.setLangues(ex);
        c.setMdp("tata");
        c.setMail("tata@tata.fr");
        c.setNbExperiences(5); 
        c.setNiveauEtudes("Bac+3");
        em.persist(c);
    }
    
    public void addEmployeurs() {
        Entreprise en = new Entreprise("La bonne place","restauration","0156917582","SA", new Adresse("3 rue des Géraniums","54600","Villers-les-Nancy","France"));
        em.persist(en);
        
        /* Création d'un premier employeur */
        emp = new Employeur("Merter","Laure","laure@test.fr","laure",true,en);
        em.persist(emp);
        
        /* Création d'un deuxième employeur */
        emp = new Employeur("Montpe","Thomas","thomas@test.fr","thomas",false,en);
        emp.setTelephone("0651859635");
        em.persist(emp);
        
        en = new Entreprise("Société Générale","banque","0155687582","SARL", new Adresse("35 allée des Lilas","75000","Paris","France"));
        en.setNbEmployes(32);
        em.persist(en);
        
        /* Création d'un deuxième employeur */
        Employeur emp2 = new Employeur("Ponetier","Etienne","etienne@test.fr","etienne",false,en);
        em.persist(emp2);
    }
    
    public void addAnnonce() {
        Annonce an = new Annonce("annonce1","blablabla","CDI","BAC+2",emp);
        em.persist(an);
        
        an = new Annonce("annonce2","blablabla","CDD","BAC+5",emp);
        an.setLieu(new Adresse("5 rue des rosiers","54000","Nancy","France"));
        an.setExperience(3);
        an.setSalaire(2300);
        Date d = new Date();
        d.setYear(2013);
        d.setMonth(7);
        d.setDate(30);
        em.persist(an);
        
    }


}
