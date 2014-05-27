/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.util.List;
import javax.annotation.Resource;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Conversation;
import model.Image;
import model.MessageConversation;
import model.Utilisateur;

/**
 *
 * @author Yan
 */
@ManagedBean
@Named(value = "gestionMessagerie")
@ApplicationScoped
public final class GestionMessagerie {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    
    private static GestionMessagerie instance;
    
    /*
    public GestionMessagerie() {
        
    }*/
    
    public static GestionMessagerie getInstance(){
        if(instance == null)
            instance = new GestionMessagerie() ;
        return instance ;
    }
    
   /* @PostConstruct
    public void init() {
        //em = emf.createEntityManager();
        //ut = em.getTransaction();
        try{
        ut.begin();
        Utilisateur u = new Utilisateur();
        u.setPseudo("Micka");
        em.persist(u);
        Utilisateur u2 = new Utilisateur();
        u.setPseudo("Mickey");
        em.persist(u2);
        Utilisateur u3 = new Utilisateur();
        u.setPseudo("Mickael");
        em.persist(u3);
        
        Conversation c = new Conversation();
        c.setExpediteur(u);
        c.setDestinataire(u2);
        
        Conversation c2 = new Conversation();
        c2.setExpediteur(u);
        c2.setDestinataire(u3);
        
        Conversation c3 = new Conversation();
        c3.setExpediteur(u2);
        c3.setDestinataire(u3);
        
        ArrayList<MessageConversation> array =  new ArrayList<>();
        MessageConversation m = new MessageConversation(); 
        m.setExpediteur(u);
        m.setDate(new Date(10022014));
        m.setContenu("I am micka");
        m.setConversation(c);
        array.add(m);
        
        m = new MessageConversation();
        m.setExpediteur(u);
        m.setDate(new Date(10022014));
        m.setContenu("I am mickey");
        m.setConversation(c);
        array.add(m);
        
        c.setMessages(array);
        
        array = new ArrayList<>();
        m = new MessageConversation();
        m.setExpediteur(u);
        m.setDate(new Date(11022014));
        m.setContenu("I am micka");
        m.setConversation(c2);
        array.add(m);
        
        m = new MessageConversation();
        m.setExpediteur(u3);
        m.setDate(new Date(11022014));
        m.setContenu("I am mickael");
        m.setConversation(c2);
        array.add(m);
        
        c2.setMessages(array);
        
        array = new ArrayList<>();
        m = new MessageConversation();
        m.setExpediteur(u2);
        m.setDate(new Date(12022014));
        m.setContenu("I am mickey");
        m.setConversation(c3);
        array.add(m);
        
        m = new MessageConversation();
        m.setExpediteur(u3);
        m.setDate(new Date(12022014));
        m.setContenu("I am mickael");
        m.setConversation(c3);
        array.add(m);
        
        c3.setMessages(array);
                
        em.persist(c);
        em.persist(c2);
        em.persist(c3);
        
        ut.commit();
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
    }*/
    
    public List<Utilisateur> getContacts(Utilisateur user){
        if(user==null)
            return null;
        try{
        ut.begin();
        Query q = em.createQuery("SELECT c.destinataire FROM Conversation c WHERE c.expediteur=:exp");
        q.setParameter("exp", user);
        List<Utilisateur> results = (List<Utilisateur>) q.getResultList();
        ut.commit();
        return results;
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Conversation> getConversations(Utilisateur user){
        try{
            if(user==null)
                return null;
            ut.begin();
            Query q = em.createQuery("SELECT c FROM Conversation c WHERE c.expediteur=:exp AND c.destinataire=:dest");
            q.setParameter("exp", user);
            List<Conversation> results = (List<Conversation>) q.getResultList();
            ut.commit();
            if(!results.isEmpty()){
                if(results.size()>1){
                    System.err.println("WARNING : SEVERAL RESULTS WHERE ONE EXPECTED");
                }
                return results;
            }
            
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
            
        return null;
    }
    
    /**
     * Méthode permettant de récupérer la conversation privée entre 2 utilisateurs
     * @param expediteur l'utilisateur voulant accéder à la conversation
     * @param destinataire l'utilisateur associé à la conversation (utilisateur avec qui l'expediteur veut discuter)
     * @return la (première et en théorie unique) conversation entre les deux utilisateurs
     */
    public Conversation getConversation(Utilisateur expediteur, Utilisateur destinataire) {
        try{
            if(expediteur==null || destinataire==null)
                return null;
            ut.begin();
            Query q = em.createQuery("SELECT c FROM Conversation c WHERE c.expediteur=:exp AND c.destinataire=:dest");
            q.setParameter("exp", expediteur);
            q.setParameter("dest", destinataire);
            List<Conversation> results = (List<Conversation>) q.getResultList();
            ut.commit();
            if(!results.isEmpty()){
                if(results.size()>1){
                    System.err.println("WARNING : SEVERAL RESULTS WHERE ONE EXPECTED");
                }
                return results.get(0);
            }
            
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
           
        return null;
    }
    
    /**
     * Methode permettant de récupérer une liste de messages privés (MessageConversation)
     * @param c la conversation dont on veut récupérer la liste de messages
     * @return la liste des messages privés (MessageConversation)
     */
    public List<MessageConversation> getPrivateMessages(Conversation c) {
        List<MessageConversation> results=null;
        try{
            if(c==null)
                return null;
            ut.begin();
            Query q = em.createQuery("SELECT m FROM MessageConversation m WHERE m.conversation=:conv ORDER BY m.date DESC");
            q.setParameter("conv", c);
            results = (List<MessageConversation>) q.getResultList();
            ut.commit();

        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
        
        return results;
        
    }
    
    /**
     * Méthode permettant de récupérer directement la liste des messages entre deux utilisateurs
     * @param expediteur l'utilisateur voulant accéder à la conversation
     * @param destinataire l'utilisateur avec qui l'expediteur veut discuter
     * @return la liste des messages privés (MessageConversation)
     */
    public List<MessageConversation> getPrivateMessages(Utilisateur expediteur, Utilisateur destinataire){
        List<MessageConversation> results=null;
        try{
            if(expediteur==null || destinataire==null)
                return null;
            ut.begin();
            Query q = em.createQuery("SELECT c.messages FROM Conversation c WHERE c.expediteur=:exp AND c.destinataire=:dest ORDER BY c.messages.date DESC");
            q.setParameter("exp", expediteur);
            q.setParameter("dest", destinataire);
            results = (List<MessageConversation>) q.getResultList();
            ut.commit();
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
        return results;
    }
    
    /**
     * Méthode permettant de récupérer un utilisateur à partir de son id
     * @param id l'id de l'utilisateur à récupérer
     * @return l'utilsiateur associé à cet id
     */
    public Utilisateur getUser(long id) {
        Utilisateur u = null;
        try{
            ut.begin();
            u = em.find(Utilisateur.class, id);
            ut.commit();
       }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
        
        return u;
    }
    
    /**
     * Méthode permettant de récupérer un utilisateur à partir de son id
     * @param pseudo le pseudo de l'utilisateur
     * @return l'utilsiateur associé à cet id
     */
    public Utilisateur getUser(String pseudo) {
        Utilisateur results = null;
        try{
            ut.begin();
            Query q = em.createQuery("SELECT u FROM Utilisateur u WHERE u.pseudo=:pse");
            q.setParameter("pse", pseudo);
            results = (Utilisateur) q.getSingleResult();
            ut.commit();
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
       
        return results;
    }
    
    /**
     * Méthode permettant de récupérer l'avatar d'un utilisateur à partir de son id
     * @param id l'id de l'utilisateur dont on veut récupérer l'avatar
     * @return l'avatar (Image) de l'utilisateur
     */
    public Image getAvatar(long id){
         List<Image> results = null;
         
        try{
            ut.begin();
            Query q = em.createQuery("SELECT u.avatar FROM Utilisateur u WHERE u.id=:id");
            q.setParameter("id", id);
            results= (List<Image>) q.getResultList();
            ut.commit();
        
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
        
        if(results==null || results.isEmpty())
            return null;
        
        return results.get(0);
    }

    
    
    
}
