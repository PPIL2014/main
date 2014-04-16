package control;

import java.io.Serializable;
import static java.lang.Thread.sleep;
import java.text.DateFormat;
import java.util.ArrayList;
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
import javax.servlet.ServletContext;
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
    
    private Date lastMessageUpdate;
    private Date lastSignOutUpdate;
    private Date lastQuitUpdate;
    
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    
    public EventChatBean() {
        lastMessageUpdate = new Date(0);
        lastSignOutUpdate = new Date(0);
        lastQuitUpdate = new Date(0);
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
        boolean endChat = false;
        boolean utilDeco = false;
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        ArrayList<String> listCo;
        String correspondant;
        if (getChat().getUtilisateur1().getPseudo().compareTo(getUtilisateurSession().getPseudo()) == 0)
            correspondant = getChat().getUtilisateur2().getPseudo();
        else
            correspondant = getChat().getUtilisateur1().getPseudo();
        
        while (m == null && endChat == false && utilDeco == false) 
        {
            //Recherche un eventuelle nouveau message
            m = getChat().getFirstAfter(lastMessageUpdate);
            if (m != null)
                break;           

            //Regarde si le chat est toujours actis*fs
            endChat = !getChat().getEstDemarree();
            if (endChat)
                break;
            
            //Controle si le correspondant est toujours connecté
            listCo = (ArrayList<String>) servletContext.getAttribute("listeUtilisateursConnecte");
            utilDeco = !listCo.contains(correspondant);
            if (utilDeco)
                break;
            
            try {
                 sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChatBean.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        if (m != null)
        {
            lastMessageUpdate = m.getDate();         
            ctx.addCallbackParam("ok", true);
            ctx.addCallbackParam("utilisateurCourant", m.getExpediteur().getPseudo().equals( utilisateurSession.getPseudo()));
            ctx.addCallbackParam("type", "message");
            ctx.addCallbackParam("user", m.getExpediteur().getPseudo());
            DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
            ctx.addCallbackParam("dateSent", shortDateFormat.format(m.getDate())); 
            ctx.addCallbackParam("text", m.getContenu());
        }
        if( endChat == true)
        {
            lastQuitUpdate = new Date();
            ctx.addCallbackParam("ok", true);
            ctx.addCallbackParam("type", "endChat");            
        }
        if (utilDeco == true)
        {
            lastSignOutUpdate = new Date();
            ctx.addCallbackParam("ok", true);
            ctx.addCallbackParam("type", "utilDeco");
        }
    }   
}

