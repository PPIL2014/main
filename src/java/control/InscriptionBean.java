/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Session;
import model.Utilisateur;

/**
 *
 * @author laurent84u
 */
@Named(value = "inscriptionBean")
@ManagedBean
@RequestScoped
public class InscriptionBean {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;

    @ManagedProperty(value="#{pseudo}")
    private String pseudo;
    @ManagedProperty(value="#{mdp}")
    private String mdp;
    @ManagedProperty(value="#{mdp2}")
    private String mdp2;
    @ManagedProperty(value="#{mail}")
    private String mail;
    @ManagedProperty(value="#{cdu}")
    private boolean cdu;
    
    private UIComponent pseudoText;
    private UIComponent mdpText;
    private UIComponent mdp2Text;
    private UIComponent mailText;
    private UIComponent cduCheck;
    
    /**
     * Creates a new instance of InscriptionBean
     */
    public InscriptionBean() {
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMdp() {
        return mdp;
    }
    
    public String getMdp2() {
        return mdp2;
    }

    public String getMail() {
        return mail;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
    
    public void setMdp2(String mdp) {
        this.mdp2 = mdp;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public UIComponent getMdp2Text() {
        return mdp2Text;
    }

    public void setMdp2Text(UIComponent mdp2Text) {
        this.mdp2Text = mdp2Text;
    }

    public UIComponent getMailText() {
        return mailText;
    }

    public void setMailText(UIComponent mailText) {
        this.mailText = mailText;
    }
        
    public UIComponent getCduCheck() {
        return cduCheck;
    }

    public void setCduCheck(UIComponent cduCheck) {
        this.cduCheck = cduCheck;
    }

    public boolean isCdu() {
        return cdu;
    }

    public void setCdu(boolean cdu) {
        this.cdu = cdu;
    }
    
    public boolean testPseudo() {
        boolean b = true;
        Query query = em.createQuery("SELECT u FROM Utilisateur u where u.pseudo='" + pseudo + "'");
        try {
            //S'il existe déja un utilisateur ayant le pseudo indiqué, on le lui indique
            query.getSingleResult();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(this.pseudoText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Le pseudo existe déjà, veuillez en choisir un autre !", null));
            b = false;
        } catch (NoResultException e) {
            b = true;
        }
        return b;
    }
    
    public boolean testMail() {
        boolean b = true;
        Query query = em.createQuery("SELECT u FROM Utilisateur u where u.mail='" + mail + "'");
        try {
            query.getSingleResult();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(this.mailText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cette adresse mail est déjà utilisée, veuillez en choisir une autre", null));
            b = false;
        } catch (NoResultException e) {
            b = true;
        }
        return b;
    }
    
    public String inscrire() {   
        System.out.println(this.cdu);
        FacesContext context = FacesContext.getCurrentInstance();
        if(mdp.length() < 4) {
            context.addMessage(this.mdpText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mot de passe trop court (min. 4 caractères)", null));
            return "inscription.xhtml";
        } else if (mdp.compareTo(mdp2) != 0) {
            context.addMessage(this.mdp2Text.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mot de passe différent, veuillez recommencer", null));
            return "inscription.xhtml";
        }
        if (testPseudo() && testMail()) {
            if(! this.cdu) {
                context.addMessage(this.cduCheck.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vous devez accepter les conditions d'utilisation", null));
                return "inscription.xhtml";
            }
            Utilisateur u = new Utilisateur();
            if(! Pattern.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$", mail)) {
                context.addMessage(this.mailText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "L'adresse mail entrée n'est pas valide", null));
                return "inscription.xhtml";
            }
            u.setMail(mail);
            u.setPseudo(pseudo);
            u.setMdp(mdp);
            Session session = new Session();
            session.setEstConnecte(Boolean.FALSE);
            u.setSession(session);
            try {
                ut.begin();
                em.persist(u);
                ut.commit();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Inscription réussie, vous pouvez maintenant vous connecter", null));
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException |
                    HeuristicRollbackException | SecurityException | IllegalStateException nse) {
                Logger.getLogger(InscriptionBean.class.getName()).log(Level.SEVERE, null, nse);
            }
            return "index.xhtml";
        }
        else 
            return "inscription.xhtml";
    }
}
