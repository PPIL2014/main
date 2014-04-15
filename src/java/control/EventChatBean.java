/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;


import java.io.Serializable;
import static java.lang.Thread.sleep;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import model.MessageChat;
import model.SessionChat;
import model.Utilisateur;
import org.primefaces.context.RequestContext;


@ManagedBean
@ViewScoped
public class EventChatBean implements Serializable {

    /**
     * Creates a new instance of EventChatBean
     */
   /* @ManagedProperty(value="#{utilisateurSession}")
    private Utilisateur utilisateurSession ;*/
    private Utilisateur utilisateurSession;
    
    /*@ManagedProperty(value="#{message}")
    private String message;*/
    
    private Date lastUpdate;
    
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    
    public EventChatBean() {
        lastUpdate = new Date(0);
    }
        public SessionChat getChat() {
        //on recupere le chat de l'utilisateur en session
        Utilisateur u = getUtilisateurSession() ;
        return u.getSessionChatDemarree() ;
    }
    
   public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession ;
    }
   
    public void lastEvent(ActionEvent evt) throws InterruptedException 
    {      
        RequestContext ctx = RequestContext.getCurrentInstance();
        MessageChat m = null;
        while (m == null)
        {
            m = getChat().getFirstAfter(lastUpdate);
            if (m == null)
            {
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChatBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        if (m != null)
        {
            lastUpdate = m.getDate();         
            ctx.addCallbackParam("ok", true);
            ctx.addCallbackParam("type", "message");
            ctx.addCallbackParam("user", m.getExpediteur().getPseudo());
            ctx.addCallbackParam("dateSent", m.getDate().toString()); 
            ctx.addCallbackParam("text", m.getContenu());
        }     
    }   
}
