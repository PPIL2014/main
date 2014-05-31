package control;

import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import model.Affinite;
import model.SessionChat;
import model.Utilisateur;
import org.primefaces.context.RequestContext;

/**
 *
 * @author nicolas
 */
@ManagedBean
@RequestScoped
public class ProfilBean {
/**
     * Creates a new instance of ProfilBean
     */
    public ProfilBean() {
    }
    
     // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "DateRoulettePU" )
    private EntityManager em;

    private Utilisateur utilisateur;


    
    public String getPseudo () {
        return getUtilisateurSession().getPseudo() ;
    }
      
     public String getUrlAvatar () {
        return getUtilisateurSession().getAvatar() == null?"/resources/images/apercu.png":getUtilisateurSession().getAvatar().getUrl() ;
    }
     
     
    
    public Utilisateur getUtilisateur(){
        return utilisateur;
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession;
    }
    
    public ArrayList<Utilisateur> getListeUtilisateurAttente () {
        ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        if(servletContext.getAttribute("listeUtilisateursAttente") == null){
            servletContext.setAttribute("listeUtilisateursAttente", new ArrayList<Utilisateur>());
        }
        return (ArrayList<Utilisateur>)servletContext.getAttribute("listeUtilisateursAttente") ;
    }
    
    public ArrayList<Utilisateur> getListeUtilisateurAttente60s () {
        ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        if(servletContext.getAttribute("listeUtilisateursAttente60s") == null){
            servletContext.setAttribute("listeUtilisateursAttente60s", new ArrayList<Utilisateur>());
        }
        return (ArrayList<Utilisateur>)servletContext.getAttribute("listeUtilisateursAttente60s") ;
    }
    
    public void chatInfo (ActionEvent evt) {
        RequestContext ctx = RequestContext.getCurrentInstance();
        
        Utilisateur u = getUtilisateurSession() ;
        ctx.addCallbackParam("ok", u!=null);
        
        if (u==null)
            return;
        
       if (u.getSessionChatDemarree() == null)
            return ;
        
        Utilisateur u2 = u.getSessionChatDemarree().getUtilisateur2() ;
        
        //si le tcvhat est à l'envers
        if (u.equals(u2))
            u2 = u.getSessionChatDemarree().getUtilisateur1() ;
        
        ctx.addCallbackParam("copain", u2.getPseudo());
    }
    
    public String goChat () {
        return "profil.xhtml" ;
    }
    
    public boolean getSeulEnAttente () {        
        ArrayList<Utilisateur> listeAttente =  getListeUtilisateurAttente () ;
        ArrayList<Utilisateur> listeAttente60s = getListeUtilisateurAttente60s();
        Utilisateur u = getUtilisateurSession () ;
        return ( listeAttente.contains(u) || listeAttente60s.contains(u) ) ;
    }
    
    public void quitterAttente(){
        Utilisateur u = getUtilisateurSession();
        getListeAffinite().remove(u);
        getListeUtilisateurAttente().remove(u);
        getListeUtilisateurAttente60s().remove(u);
    }
    //Affiche un message à l'utilisateur si il est seul en attente. 
    public void addInfo() {  
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Vous êtes en attente !", "Vous serez redirigé(e) automatiquement lorsque quelqu'un d'autre arrivera !"));  
    }
    
    public ArrayList<Affinite> getListeAffinite () {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ArrayList<Affinite>) servletContext.getAttribute("listeAffinite") ;
    }
}
