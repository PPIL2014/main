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
import javax.persistence.NoResultException;
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
import model.Utilisateur;

@ManagedBean
@SessionScoped
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
    
    private UIComponent pseudoText;
    private UIComponent mdpText;
            
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
    
    public List<Utilisateur> getListeUtilisateurs() {
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
    
    public String connecterList() throws Exception{ 
        //if (verifPseudo()) { 
            //if (verifMdp()) {
                //this.ut.begin();
                utilisateur = em.find(Utilisateur.class, pseudo);
                FacesContext context = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                ArrayList<String> listeConnecte = (ArrayList<String>)servletContext.getAttribute("listeUtilisateursConnecte");
                listeConnecte.add(utilisateur.getPseudo());
                //utilisateur.closeAllChat();
                //this.em.merge(utilisateur);
                session.setAttribute("pseudoUtilisateur", utilisateur.getPseudo());
                session.setAttribute("user", utilisateur);
                //this.ut.commit();
                return "profil"; 
                /*} else {
            setPseudo("");
            setMdp("");
            context.addMessage(this.pseudoText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Impossible de se connecter : Nom d'utilisateur ou mot de passe incorrect !", null)); 
        } 
         return "index" ;*/
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
            }else if(this.utilisateur.getEstBloque()){
                context.addMessage(this.pseudoText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Impossible de se connecter : l'utilisateur est bloqué !", null)); 
            } else {
                try {
                    HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
                    ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                    session.setAttribute("pseudoUtilisateur", this.utilisateur.getPseudo());
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Vous êtes connecté en tant que " + this.utilisateur.getPseudo() + " !", null));
                    this.utilisateur.getSession().setEstConnecte(Boolean.TRUE);
                    this.ut.begin();
                    this.em.merge(this.utilisateur);
                    this.ut.commit();
                    return "fakeListe.xhtml";
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
        if (this.utilisateur != null) {
            try {
                this.utilisateur.getSession().setEstConnecte(false);
                this.ut.begin();
                this.em.merge(this.utilisateur);
                this.ut.commit();
            } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException | NotSupportedException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        this.utilisateur = null;
        return "index.xhtml";
    }
    
    public List<Utilisateur> getListeUtilisateursDeroul(){
        if (this.listeUtilisateurs == null || this.listeUtilisateurs.isEmpty()) {
            this.listeUtilisateurs = em.createQuery("SELECT u FROM Utilisateur u").getResultList();
        }
        return this.listeUtilisateurs;
    }
    
    public String Messagerie(){
        return "Messagerie.xhtml";
    }
}
