package control;

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
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Utilisateur;

@ManagedBean
@SessionScoped
public class LoginBean {
    @PersistenceContext(unitName = "DateRoulettePU")
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
            
    public LoginBean() {
        utilisateur = null;
    }
    
    public String getPseudo(){
        return this.pseudo;
    }
    
    public List<Utilisateur> getListeUtilisateurs() {
        if(this.utilisateur == null)
            return null;
        Query q = em.createQuery("SELECT u FROM Utilisateur u WHERE u.pseudo != :pseudo ORDER BY u.session.estConnecte ASC");
        q.setParameter("pseudo", this.utilisateur.getPseudo());
        return q.getResultList();
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    
    public String getMdp(){
        return this.pseudo;
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
                    this.ut.commit();
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
    
    public String deconnexion() {
        FacesContext context = FacesContext.getCurrentInstance(); 
        if (this.utilisateur != null) {
            try {
                this.utilisateur.getSession().setEstConnecte(false);
                this.ut.begin();
                this.em.merge(this.utilisateur);
                this.ut.commit();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vous êtes maintenant déconnecté", null)); 
            } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException | NotSupportedException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        this.utilisateur = null;
        return "index.xhtml";
    }
    
}
