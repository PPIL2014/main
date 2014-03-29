package ul.dateroulette.control;

import java.util.ArrayList;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import ul.dateroulette.model.Image;
import ul.dateroulette.model.Utilisateur;

@ManagedBean
@RequestScoped
public class LoginBean {

    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    
    @ManagedProperty(value="#{pseudo}")
    private String pseudo;
    @ManagedProperty(value="#{avatar}")
    private String avatar;
            
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String connecter() throws Exception{
        this.ut.begin();
        Utilisateur u = new Utilisateur(this.pseudo);
        u.setAvatar(new Image(this.avatar));
        em.persist(u);
        this.ut.commit();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.setAttribute("utilisateur", u);
        return "profil.xhtml" ;
    }
    
}
