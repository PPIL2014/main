package control;

import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import model.Contact;
import model.Utilisateur;

@Named(value = "listeAmisBean")
@RequestScoped
public class ListeAmisBean {

    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    
    public ListeAmisBean() {
        
    }
    
    public String afficherAmis () {
        return "listeAmis.xhtml" ;
    }
    
    public Collection<Contact> getContacts () {
        return getUtilisateurSession ().getContacts () ;
    }
    
    public boolean estConnecte (Utilisateur u) {
        ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        ArrayList<String> listeUserConnect = (ArrayList<String>)servletContext.getAttribute("listeUtilisateursConnecte") ;
        return listeUserConnect.contains(u.getPseudo()) ;
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
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession ;
    }
    
}
