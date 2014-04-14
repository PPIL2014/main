package control;

import java.util.ArrayList;
import java.util.List;
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
    private List<Utilisateur> listeUtilisateurs;
    
    @ManagedProperty(value="#{pseudo}")
    private String pseudo;
    @ManagedProperty(value="#{mdp}")
    private String mdp;
            
    public LoginBean() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        if(servletContext.getAttribute("listeUtilisateursAttente") == null){
            servletContext.setAttribute("listeUtilisateursAttente", new ArrayList<Utilisateur>());
        }
        if(servletContext.getAttribute("listeUtilisateursConnecte") == null){
            servletContext.setAttribute("listeUtilisateursConnecte", new ArrayList<String>());
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
        return mdp.equals(mdp_bdd) ;
    }
    
    public String connecter() { 
        //if (verifPseudo()) { 
            //if (verifMdp()) {
        System.out.println(pseudo);
                utilisateur = em.find(Utilisateur.class, pseudo);
                        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        ArrayList<String> listeConnecte = (ArrayList<String>)servletContext.getAttribute("listeUtilisateursConnecte");
        listeConnecte.add(utilisateur.getPseudo());
        session.setAttribute("pseudoUtilisateur", utilisateur.getPseudo());
                return "profil"; 
            /*} else {
                setMdp("");
                FacesContext context = FacesContext.getCurrentInstance(); 
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Impossible de se connecter : Mot de passe incorrect !", "Impossible de se connecter : Mot de passe incorrect !")); 
            } 
        } else { 
            setPseudo(""); 
            FacesContext context = FacesContext.getCurrentInstance(); 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Impossible de se connecter : Utilisateur introuvable !", "Impossible de se connecter : Utilisateur introuvable !")); 
        } 
         return "index" ;*/
                
    }
    
    public String deconnexion() {
        //Destroy session
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        
        return "faces/index.xhtml";
    }
    
    public List<Utilisateur> getListeUtilisateurs(){
        if (this.listeUtilisateurs == null || this.listeUtilisateurs.isEmpty()) {
            this.listeUtilisateurs = em.createQuery("SELECT u FROM Utilisateur u").getResultList();
        }
        return this.listeUtilisateurs;
    }    
}