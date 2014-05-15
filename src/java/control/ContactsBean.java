/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.*;

/**
 *
 * @author nicolas
 */
@ManagedBean
@RequestScoped
public class ContactsBean {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    
    public List<Contact> getContactListeAmis() {
        

        String pseudo = (String) getUtilisateurSession().getPseudo();
        Utilisateur u = em.find(Utilisateur.class, pseudo);
        List<Contact> alc = new ArrayList<>();

        for(Contact c : u.getContacts())
        {
            if(c.getType().equals(Contact.Type.AMI) || c.getType().equals(Contact.Type.FAVORI))
            {
                if(rechercheListeAmis != null)
                {
                    if(c.getEstEnContactAvec().getPseudo().matches(rechercheListeAmis+".*"))
                        alc.add(c);
                }
                else
                    alc.add(c);
            }
        }
        
        return alc;
    }
    
    public List<Contact> getContactDemandes() {
        

        String pseudo = (String) getUtilisateurSession().getPseudo();
        Utilisateur u = em.find(Utilisateur.class, pseudo);
        List<Contact> alc = new ArrayList<>();

        for(Contact c : u.getContacts())
        {
            if(c.getType().equals(Contact.Type.DEMANDE) || c.getType().equals(Contact.Type.ENATTENTE) || c.getType().equals(Contact.Type.REFUSE))
            {
                alc.add(c);
            }
        }
        
        return alc;
        
    }
    
     public List<Contact> getContactListeNoire() {
        
    
        String pseudo = (String) getUtilisateurSession().getPseudo();
        Utilisateur u = em.find(Utilisateur.class, pseudo);
        List<Contact> alc = new ArrayList<>();

        for(Contact c : u.getContacts())
        {
            if(c.getType().equals(Contact.Type.BLOQUE))
            {
                if(rechercheListeNoire != null)
                {
                    if(c.getEstEnContactAvec().getPseudo().matches(rechercheListeNoire+".*"))
                        alc.add(c);
                }
                else
                    alc.add(c);
            }
        }
        
        return alc;
        
    }
    
    public void accepter(Contact c){
        
        try {
            
            // accepte le contact recu
            c.setType(Contact.Type.AMI);
            
            ut.begin();
            em.merge(c);
            ut.commit();
            
            // accepte l'autre contact(en attente)
           
            String pseudo = (String) getUtilisateurSession().getPseudo();
            Utilisateur he = em.find(Utilisateur.class, c.getEstEnContactAvec().getPseudo());
             
            for(Contact co : he.getContacts())
            {
                if(co.getEstEnContactAvec().getPseudo().equals(pseudo))
                {
                    co.setType(Contact.Type.AMI);
                    ut.begin();
                    em.merge(co);
                    ut.commit();
                    break;
                }
            }
               
                    
        } catch (NotSupportedException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void bloquer(Contact c){
        
        try {
            
          
            c.setType(Contact.Type.BLOQUE);
            
            ut.begin();
            em.merge(c);
            ut.commit();
            
            
        } catch (NotSupportedException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void debloquer(Contact c){
        
        try {
            
          
            c.setType(Contact.Type.AMI);
            
            ut.begin();
            em.merge(c);
            ut.commit();
            
            
        } catch (NotSupportedException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void refuser(Contact c){
        
        try {
            
          
            c.setType(Contact.Type.REFUSE);
            
            ut.begin();
            em.merge(c);
            ut.commit();
            
            
        } catch (NotSupportedException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void favoriser(Contact c){
        
        try {
            
          
            c.setType(Contact.Type.FAVORI);
            
            ut.begin();
            em.merge(c);
            ut.commit();
            
            
        } catch (NotSupportedException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public void defavoriser(Contact c){
        
        try {
            
          
            c.setType(Contact.Type.AMI);
            
            ut.begin();
            em.merge(c);
            ut.commit();
            
            
        } catch (NotSupportedException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SystemException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RollbackException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicMixedException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeuristicRollbackException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(ContactsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<String> autoCompleteListeAmis(String query) {
        
      
        String pseudo = (String) getUtilisateurSession().getPseudo();
        Utilisateur u = em.find(Utilisateur.class, pseudo);
        
        List<String> als = new ArrayList<>();

        for(Contact c : u.getContacts())
        {
            if(c.getType().equals(Contact.Type.AMI) || c.getType().equals(Contact.Type.FAVORI))
            {
                if(c.getEstEnContactAvec().getPseudo().matches(query+".*"))
                    als.add(c.getEstEnContactAvec().getPseudo());
            }
        }
        
        return als;
    }
     
    private String rechercheListeAmis;

    public String getRechercheListeAmis() {
        return rechercheListeAmis;
    }

    public void setRechercheListeAmis(String rechercheListeAmis) {
        this.rechercheListeAmis = rechercheListeAmis;
    }
    
    public List<String> autoCompleteListeNoire(String query) {
        
       
        String pseudo = (String) getUtilisateurSession().getPseudo();
        Utilisateur u = em.find(Utilisateur.class, pseudo);
        
        List<String> als = new ArrayList<>();

        for(Contact c : u.getContacts())
        {
            if(c.getType().equals(Contact.Type.BLOQUE))
            {
                if(c.getEstEnContactAvec().getPseudo().matches(query+".*"))
                    als.add(c.getEstEnContactAvec().getPseudo());
            }
        }
        
        return als;
    }
     
    private String rechercheListeNoire;

    public String getRechercheListeNoire() {
        return rechercheListeNoire;
    }

    public void setRechercheListeNoire(String rechercheListeNoire) {
        this.rechercheListeNoire = rechercheListeNoire;
    }

    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession;
    }
    
    public boolean isConnected(Contact c){
            return getListeUtilisateurConnecte().contains(c.getEstEnContactAvec().getPseudo()) ;
    }
    
    
    
      public List<String> getListeUtilisateurConnecte(){
        Query query = em.createQuery("SELECT u.pseudo FROM Utilisateur u where u.session.estConnecte=true");
        return (List<String>) query.getResultList();
    }
    
}
