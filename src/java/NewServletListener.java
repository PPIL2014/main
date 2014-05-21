
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Contact;
import model.SignalementUtilisateur;
import model.Utilisateur;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Web application lifecycle listener.
 *
 * @author nicolas
 */
public class NewServletListener implements ServletContextListener  {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        /*Utilisateur medi = new Utilisateur();
        medi.setPseudo("medi");
        medi.setMdp("mdp");
        medi.setPrenom("medi1");
        medi.setNom("huss");
        medi.setAdresse("3 rue");
     //   medi.setDdn(1990, 10, 01);
        medi.setMail("@coco.fr");
        medi.setSexe("H");
        medi.setTelephone("06");


        Utilisateur aziz = new Utilisateur();
        aziz.setPseudo("aziz");
        aziz.setMdp("mdp");
        aziz.setPrenom("aziz");
        aziz.setNom("aziz");
        aziz.setAdresse("3 rue");
    //    aziz.setDdn(1990, 10, 01);
        aziz.setMail("@mail");
        aziz.setSexe("H");
        aziz.setTelephone("06");
        aziz.setAdministrateur(Boolean.TRUE);
        
        
          Session session = new Session();
            session.setEstConnecte(Boolean.FALSE);
            medi.setSession(session);
            
             Session session2 = new Session();
            session2.setEstConnecte(Boolean.FALSE);
            aziz.setSession(session2);

    
       Contact c1 = new Contact();
       c1.setType(Contact.Type.ENATTENTE);
       c1.setEstEnContactAvec(aziz);
       Collection cc1 = new ArrayList<Contact>(); 
       cc1.add(c1);
       medi.setContacts(cc1);
       
       Contact c2 = new Contact();
       c2.setType(Contact.Type.DEMANDE);
       c2.setEstEnContactAvec(medi);
       Collection cc2 = new ArrayList<Contact>(); 
       cc2.add(c2);
       aziz.setContacts(cc2);

       SignalementUtilisateur s = new SignalementUtilisateur();
       s.setDate(new Date());
       s.setEmetteur(aziz);
       s.setEstTraitee(false);
       s.setUtilisateurSignale(medi);
       s.setMotif("Pas gentil, il embete les autres du site"
               + "");
       
       SignalementUtilisateur s2 = new SignalementUtilisateur();
       s2.setDate(new Date());
       s2.setEmetteur(aziz);
       s2.setEstTraitee(false);
       s2.setUtilisateurSignale(medi);
       s2.setMotif("Pas gentil, l'admin avait pas le droit de me signaler !");

        try {
            
            ut.begin();
            em.persist(medi);
            em.persist(aziz);
            em.persist(s);
            em.persist(s2);
            ut.commit();
            
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            Logger.getLogger(NewServletListener.class.getName()).log(Level.SEVERE, null, ex);
        }*/

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
