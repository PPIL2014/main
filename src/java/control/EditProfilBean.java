/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Utilisateur;

/**
 *
 * @author nicolas
 */
@ManagedBean
@RequestScoped
public class EditProfilBean {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;

    private UIComponent adresseText;
    private UIComponent mailText;
    private UIComponent telephoneText;

    private String oldMdp;
    private String newMdp;
    private String confNewMdp;

    private UIComponent oldMdpText;
    private UIComponent newMdpText;
    private UIComponent confNewMdpText;

    private Utilisateur utilisateur;

    public Utilisateur getUtilisateur() {
        if(utilisateur == null)
            utilisateur = em.find(Utilisateur.class, getUtilisateurSession().getPseudo());
        
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession;
    }
    
    public String edit() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean error = false;

        if (utilisateur != null) {
            if (!utilisateur.getMail().matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$")) {
                context.addMessage(this.mailText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Format de l'adresse mail incorrecte !", null));
                error = true;
            }

            if (!utilisateur.getTelephone().matches("[0-9-]{10}")) {
                context.addMessage(this.telephoneText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Format du numéro de téléphone incorrect !", null));
                error = true;
            }

            if (!oldMdp.isEmpty() || !newMdp.isEmpty() || !confNewMdp.isEmpty()) {
                if (oldMdp.isEmpty()) {
                    context.addMessage(this.oldMdpText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ce champ ne doit pas être vide !", null));
                    error = true;
                }

                if (newMdp.isEmpty()) {
                    context.addMessage(this.newMdpText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ce champ ne doit pas être vide !", null));
                    error = true;
                } else if (newMdp.length() < 4) {
                    context.addMessage(this.newMdpText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Le mot de passe est trop court !", null));
                    error = true;
                }

                if (confNewMdp.isEmpty()) {
                    context.addMessage(this.confNewMdpText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ce champ ne doit pas être vide !", null));
                    error = true;
                }
            

                if ((!oldMdp.isEmpty()) && (!utilisateur.getMdp().equals(oldMdp))) {
                    context.addMessage(this.oldMdpText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mot de passe incorrect !", null));
                    error = true;
                }

                if (!newMdp.equals(confNewMdp)) {
                    context.addMessage(this.confNewMdpText.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Les mots de passe ne correspondent pas !", null));
                    error = true;
                }
            }

            if (error) {
                return "";
            }

            if(!newMdp.isEmpty())
                utilisateur.setMdp(newMdp);
            
         
            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            session.setAttribute("utilisateurModifie", utilisateur);

           return "confirmEditProfil";

        }

        return null;
    }
    
    public String confirm(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {

            ut.begin();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            em.merge(session.getAttribute("utilisateurModifie"));
            ut.commit();

        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            Logger.getLogger(EditProfilBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Votre profil a été modifié", null));

        return "profil";

    }

    public String getOldMdp() {
        return oldMdp;
    }

    public void setOldMdp(String oldMdp) {
        this.oldMdp = oldMdp;
    }

    public String getNewMdp() {
        return newMdp;
    }

    public void setNewMdp(String newMdp) {
        this.newMdp = newMdp;
    }

    public String getConfNewMdp() {
        return confNewMdp;
    }

    public void setConfNewMdp(String confNewMdp) {
        this.confNewMdp = confNewMdp;
    }

    public UIComponent getOldMdpText() {
        return oldMdpText;
    }

    public void setOldMdpText(UIComponent oldMdpText) {
        this.oldMdpText = oldMdpText;
    }

    public UIComponent getNewMdpText() {
        return newMdpText;
    }

    public void setNewMdpText(UIComponent newMdpText) {
        this.newMdpText = newMdpText;
    }

    public UIComponent getConfNewMdpText() {
        return confNewMdpText;
    }

    public void setConfNewMdpText(UIComponent confNewMdpText) {
        this.confNewMdpText = confNewMdpText;
    }

    public UIComponent getAdresse() {
        return adresseText;
    }

    public void setAdresse(UIComponent adresse) {
        this.adresseText = adresse;
    }

    public UIComponent getMail() {
        return mailText;
    }

    public void setMail(UIComponent mail) {
        this.mailText = mail;
    }

    public UIComponent getTelephone() {
        return telephoneText;
    }

    public void setTelephone(UIComponent telephone) {
        this.telephoneText = telephone;
    }
    
}

   