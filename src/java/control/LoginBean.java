package control;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import model.Utilisateur;

@ManagedBean
@RequestScoped
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
    }
    
    public String getPseudo(){
        return this.pseudo;
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
        utilisateur = em.find(Utilisateur.class, pseudo);
        if (utilisateur != null) { 
            if (! utilisateur.getMdp().equals(this.mdp)) {
                setMdp("");
                context.addMessage(this.mdpText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Impossible de se connecter : Mot de passe incorrect !", null)); 
            } else {
                utilisateur = em.find(Utilisateur.class, pseudo);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Vous êtes connecté en tant que " + pseudo + " !", null));
                return "fakeListe.xhtml";
            } 
        } else { 
            setPseudo("");
            setMdp("");
            context.addMessage(this.pseudoText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Impossible de se connecter : Utilisateur introuvable !", null)); 
        } 
        return null; 
    }
    
    public String deconnexion() {
        //Destroy session
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        
        return "index.xhtml";
    }
    
}
