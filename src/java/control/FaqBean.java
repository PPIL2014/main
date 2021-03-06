package control;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
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
import model.FAQ;
import model.Utilisateur;

/**
 *
 * @author bailloux1u
 */
@ManagedBean
@RequestScoped
public class FaqBean {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    @ManagedProperty(value = "#{question}")
    public String question = "";
    @ManagedProperty(value = "#{reponse}")
    public String reponse = "";
    private FAQ faq = new FAQ();

    /**
     * Creates a new instance of FaqBean
     */
    public FaqBean() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public Collection<FAQ> getFAQ() {

        Query jQuery = em.createQuery("Select f From FAQ f");

        List<FAQ> liste = jQuery.getResultList();

        return liste;
    }

    public String ajouterFAQ() throws InterruptedException {

        try {
            faq = new FAQ();
            faq.setQuestionFAQ(question);
            faq.setReponseFAQ(reponse);

            ut.begin();
            em.persist(faq);
            //em.merge(faq);
            ut.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            Logger.getLogger(FaqBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        Thread.sleep(1000);
        question = null;
        reponse = null;
        return "formulaireFaq.xhtml";

    }

    public String supprimerFAQ(String question) throws InterruptedException {
        try {
            faq = em.find(FAQ.class, question);
            ut.begin();
            FAQ f = em.merge(faq);            
            em.remove(f);
            ut.commit();

        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            Logger.getLogger(FaqBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        Thread.sleep(1000);
        return "faq.xhtml";

    }

    public boolean isAdmin() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        if (session.getAttribute("pseudoUtilisateur") == null) {
            return false;
        }
        Utilisateur u = (Utilisateur) em.find(Utilisateur.class, (String) session.getAttribute("pseudoUtilisateur"));

        if (u == null) {
            return false;
        } else {
            return u.getAdministrateur();
        }
    }

    public boolean estConnecte() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        return session.getAttribute("pseudoUtilisateur") != null;
    }
}
