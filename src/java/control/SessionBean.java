package control;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.*;


@ManagedBean
@RequestScoped
public class SessionBean {
    
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    
    @ManagedProperty(value="#{pseudo}")
    private String pseudo;
    @ManagedProperty(value="#{mdp}")
    private String mdp;
    
    private UIComponent pseudoText;
    private UIComponent mdpText;
          
    public SessionBean() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        ArrayList<String> listeConnecte = (ArrayList<String>)servletContext.getAttribute("listeUtilisateursConnecte");
        if(listeConnecte == null){
            servletContext.setAttribute("listeUtilisateursConnecte", new ArrayList<String>() );
        }
        if (servletContext.getAttribute("listeAffinite") == null) {
            servletContext.setAttribute("listeAffinite", new ArrayList<Affinite>());
        }
    }

     public Utilisateur getUtilisateurSession () {
       FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession ;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public UIComponent getPseudoText() {
        return pseudoText;
    }

    public void setPseudoText(UIComponent pseudoText) {
        this.pseudoText = pseudoText;
    }

    public UIComponent getMdpText() {
        return mdpText;
    }

    public void setMdpText(UIComponent mdpText) {
        this.mdpText = mdpText;
    }

    public ArrayList<String> getListeUtilisateurConnecte(){
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ArrayList<String>) servletContext.getAttribute("listeUtilisateursConnecte");
    }
    
    public ArrayList<Utilisateur> getListeAttente(){
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ArrayList<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttente");
    }
    
    public ArrayList<Utilisateur> getListeAttente60s(){
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ArrayList<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttente60s");
    }
    
   public String connecter() { 
        FacesContext context = FacesContext.getCurrentInstance();
        Utilisateur u = em.find(Utilisateur.class, this.pseudo);
        if (u != null) { 
            if (! u.getMdp().equals(this.mdp)) {
                setMdp("");
                context.addMessage(this.pseudoText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Impossible de se connecter : Nom d'utilisateur ou mot de passe incorrect !", null)); 
            } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Vous êtes connecté en tant que " + u.getPseudo() + " !", null));
                    HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
                    session.setAttribute("pseudoUtilisateur", u.getPseudo());
                    session.setAttribute("user", u);
                    ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                    ArrayList<String> listeConnecte = (ArrayList<String>)servletContext.getAttribute("listeUtilisateursConnecte");
                    listeConnecte.add(u.getPseudo());                   
                    return "profil";
            }
        } else {
            setPseudo("");
            setMdp("");
            context.addMessage(this.pseudoText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Impossible de se connecter : Nom d'utilisateur ou mot de passe incorrect !", null)); 
        } 
        return null; 
    }
    
   public String deconnecter() throws Exception{
        FacesContext context = FacesContext.getCurrentInstance(); 
        try {
            
            this.ut.begin();
            Utilisateur u= getUtilisateurSession() ;
            SessionChat sc = u.getSessionChatDemarree();
            if (sc != null)
                sc.setEstDemarree(false);
            
            this.em.merge(u);
            this.ut.commit();
            
            ArrayList<String> listeConnecte = getListeUtilisateurConnecte();
            ArrayList<Utilisateur> listeAttente = getListeAttente();
            ArrayList<Utilisateur> listeAttente60s = getListeAttente60s();
            listeConnecte.remove(this.getUtilisateurSession().getPseudo());
            listeAttente.remove(this.getUtilisateurSession());
            listeAttente60s.remove(this.getUtilisateurSession());
            
            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            session.invalidate();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vous êtes maintenant déconnecté", null)); 
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException | NotSupportedException ex) {
            Logger.getLogger(SessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "index?faces-redirect=true";
    }
   
   public boolean aChat () {
       Utilisateur u = getUtilisateurSession () ;
       return (u.getSessionChatDemarree()!= null) ;
   }
    public String Messagerie(){
        return "Messagerie.xhtml";
    }
}