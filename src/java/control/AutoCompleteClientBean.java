/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Conversation;
import model.MessageConversation;
import model.Utilisateur;

/**
 *
 * @author Faouz
 */
@ManagedBean
@ViewScoped
public class AutoCompleteClientBean implements Serializable {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    
    public Utilisateur user;
    
    private Utilisateur selectedClient;
    
    public AutoCompleteClientBean(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Utilisateur) session.getAttribute("user");
    }
    
    public List<Utilisateur> complete(String query) {
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"complete "+user.toString(),null));
        return user.getContactsByName(query);
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
    
    public List<Conversation> getConversations() throws Exception{
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        //context.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,session.getAttribute("user").toString(),null));
        if(user==null)
            //user = getUser((String) session.getAttribute("user"));
            user = (Utilisateur) session.getAttribute("user");
        //context.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,user.toString(),null));
        ArrayList<Conversation> array = this.getConversations(user);
        return array;
    }
    
    public ArrayList<Conversation> getConversations(Utilisateur user){
        ArrayList<Conversation> array = new ArrayList<>();
        try{
            /*if(user==null)
                return null;*/
            ut.begin();
            Query q = em.createQuery("SELECT c FROM Conversation c WHERE c.expediteur=:exp OR c.destinataire=:exp");
            q.setParameter("exp", user);
            List<Conversation> results = (List<Conversation>) q.getResultList();
            
            array.addAll(results);
            if(results == null || results.isEmpty()){
                Conversation c = new Conversation();
                c.setExpediteur(user);
                c.setDestinataire(getUser("micka"));
                c.setMessages(new ArrayList<MessageConversation>());
                em.persist(c);
                array.add(c);
            }
            ut.commit();
            return array;
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
            
        return null;
    }
    
    
    /**
     * Méthode permettant de récupérer la conversation privée entre 2 utilisateurs*
     * @param id l'id de la conversation
     * @return la (première et en théorie unique) conversation entre les deux utilisateurs
     */
    public Conversation getConversation(String id) {
        try{
            if(id==null)
                return null;
            ut.begin();
            Conversation results = (Conversation) em.find(Conversation.class, id);
            ut.commit();
            return results;
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
           
        return null;
    }
    
    public Utilisateur getDest(Conversation conv){
        if(conv.getExpediteur().getPseudo().equals(user.getPseudo()))
            return conv.getDestinataire();
        else
            return conv.getExpediteur();
    }
    
    public String submit(){
        System.out.println("selected Client : "+selectedClient);
        
        return "MessagerieAsynchrone.xhtml";
    }

    public Utilisateur getSelectedClient() {
        return selectedClient;
    }
    
    public void setSelectedClient(Utilisateur selectedClient) {
        this.selectedClient = selectedClient;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }
    
}
