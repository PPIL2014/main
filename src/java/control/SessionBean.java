package control;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
@SessionScoped
public class SessionBean {
    
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    
    Utilisateur utilisateur;
    
    @ManagedProperty(value="#{pseudo}")
    private String pseudo;
    @ManagedProperty(value="#{mdp}")
    private String mdp;
    
    private UIComponent pseudoText;
    private UIComponent mdpText;
          
    public SessionBean() {
 
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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
     
    public Utilisateur getUtilisateurSession () {
        return this.utilisateur;
    }
    
    public List<String> getListeUtilisateurConnecte(){
        Query query = em.createQuery("SELECT u.pseudo FROM Utilisateur u where u.session.estConnecte=true");
        return (List<String>) query.getResultList();
    }
    
    public ArrayList<Utilisateur> getListeAttente(){
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return (ArrayList<Utilisateur>) servletContext.getAttribute("listeUtilisateursAttente");
    }
    
    public String connecter() { 
        FacesContext context = FacesContext.getCurrentInstance(); 
        this.utilisateur = em.find(Utilisateur.class, this.pseudo);
        if (this.utilisateur != null) { 
            if (! this.utilisateur.getMdp().equals(this.mdp)) {
                setMdp("");
                context.addMessage(this.pseudoText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Impossible de se connecter : Nom d'utilisateur ou mot de passe incorrect !", null)); 
            } else {
                try {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Vous êtes connecté en tant que " + this.utilisateur.getPseudo() + " !", null));
                    this.utilisateur.getSession().setEstConnecte(Boolean.TRUE);
                    this.ut.begin();
                    this.em.merge(this.utilisateur);
                    
                    
                    ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                    
                    ArrayList<Utilisateur> listeAttente = new ArrayList<>();
                    
                    servletContext.setAttribute("listeUtilisateursAttente", listeAttente);
                    
                    this.ut.commit();
                    ((HttpSession)context.getExternalContext().getSession(true)).setAttribute("pseudoUtilisateur", this.utilisateur.getPseudo());
                    return "profil";
                } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                    Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
                }
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
        if (this.utilisateur != null) {
            try {
                getListeAttente().remove(this.utilisateur);
                this.utilisateur.getSession().setEstConnecte(Boolean.FALSE);
                this.ut.begin();
                this.utilisateur.closeAllChat();
                this.em.merge(this.utilisateur);
                this.ut.commit();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vous êtes maintenant déconnecté", null)); 
            } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException | NotSupportedException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
        this.utilisateur = null;
        
        return "index.xhtml";
    }
   
   public boolean aChat () {
       Utilisateur u = getUtilisateurSession () ;
       return (u.getSessionChatDemarree()!= null) ;
   }
    public String Messagerie(){
        return "Messagerie.xhtml";
    }
}
