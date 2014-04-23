package control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import model.*;

@ManagedBean
@RequestScoped
public class ListeAmisBean {
    
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    
    private String pseudoAmi;
    
    public ListeAmisBean() {
        
    }

    public String getPseudoAmi() {
        return pseudoAmi;
    }

    public void setPseudoAmi(String pseudoAmi) {
        this.pseudoAmi = pseudoAmi;
    }
    
    public String afficherAmis () {
        return "listeAmis.xhtml" ;
    }
    
    public String rechercherAmis() {
        return "rechercherAmis.xhtml";
    }
    
    public Collection<Contact> getContacts () {
        return getUtilisateurSession().getContacts () ;
    }
    
    public boolean estConnecte (Utilisateur u) {
        return u.getSession().getEstConnecte();
    }
    
    public Collection<Utilisateur> rechercherListeAmis() {
        System.out.println(pseudoAmi);
        if(pseudoAmi == null)
            return new ArrayList<>();

        Query query = em.createQuery("SELECT u FROM Utilisateur u where u.pseudo LIKE :pseudo");
        query.setParameter("pseudo", "%"+pseudoAmi+"%");
        return (List<Utilisateur>) query.getResultList();
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class, (String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession ;
    }
}
