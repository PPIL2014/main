package control;

import java.util.ArrayList;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
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
            
    public LoginBean() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        if(servletContext.getAttribute("listeUtilisateursAttente") == null){
            servletContext.setAttribute("listeUtilisateursAttente", new ArrayList<Utilisateur>());
        }
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

    
    public boolean verifPseudo() { 
        Query query = em.createQuery("select u.pseudo from Utilisateur u where u.pseudo=\"" + pseudo + "\""); 
        boolean res = true; 
        try { 
            query.getSingleResult().toString(); 
        } catch (NoResultException e) { 
            res = false; 
        } 
 
        return res; 
    } 
 
    /** 
     * Verifie si le mot de passe rentre correspond à celui de la BDD 
     * 
     * @return true si correct, false sinon 
     */ 
    public boolean verifMdp() { 
        Query query = em.createQuery("select u.mdp from Utilisateur u where u.pseudo=\"" + pseudo + "\""); 
        String mdp_bdd = query.getSingleResult().toString(); 
        boolean res = false; 
        if (mdp.equals(mdp_bdd)) { 
            res = true; 
        } 
        return res; 
    }
    
    public String connecter() { 
        if (verifPseudo()) { 
            if (verifMdp()) {
                utilisateur = em.find(Utilisateur.class, pseudo);
                return "index"; 
            } else {
                setMdp("");
                FacesContext context = FacesContext.getCurrentInstance(); 
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Impossible de se connecter : Mot de passe incorrect !", "Impossible de se connecter : Mot de passe incorrect !")); 
            } 
        } else { 
            setPseudo(""); 
            FacesContext context = FacesContext.getCurrentInstance(); 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Impossible de se connecter : Utilisateur introuvable !", "Impossible de se connecter : Utilisateur introuvable !")); 
        } 
        return null; 
    }
    
    public String deconnexion() {
        //Destroy session
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        
        return "faces/index.xhtml";
    }
    
}
