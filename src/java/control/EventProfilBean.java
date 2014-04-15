/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.io.Serializable;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import model.Utilisateur;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Florian
 */
@ManagedBean
@RequestScoped
public class EventProfilBean implements Serializable{
    private Utilisateur utilisateurSession;
    
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    /**
     * Creates a new instance of EventProfilBean
     */
    public EventProfilBean() {
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
        Utilisateur u = getUtilisateurSession();
        boolean aUnChat = false;
        
        while(true){
            if (u.getSessionChatDemarree()!=null){
                aUnChat = true;
                break;
            }
            
            try {
                 sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChatBean.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        if (aUnChat){
            ctx.addCallbackParam("ok", "true");
            ctx.addCallbackParam("type", "goChat");
        }
    }    
}
