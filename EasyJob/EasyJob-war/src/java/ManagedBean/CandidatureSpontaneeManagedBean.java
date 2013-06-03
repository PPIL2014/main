/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.EmployeurLocal;
import interfaces.EntrepriseLocal;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.primefaces.model.UploadedFile;
import persistence.Candidat;
import persistence.CandidatureSpontanee;
import persistence.Employeur;
import persistence.Entreprise;
import persistence.NotificationCandidatureS;

/**
 *
 * @author Marie
 */
@ManagedBean(name = "candidatureSpontaneeBean")
@RequestScoped
public class CandidatureSpontaneeManagedBean {
    
    private UploadedFile lm ;
    private UploadedFile cv ;
    private String next ;
    
    @Inject
    private EntrepriseLocal entrepriseEJB ;
    
    @Inject
    private EmployeurLocal employeurEJB;

    /**
     * Creates a new instance of CandidatureSpontaneeManagedBean
     */
    public CandidatureSpontaneeManagedBean() {
    }
    
    @PostConstruct
    public void initialisation() {

    }
    
    public void deposer(ActionEvent a) {
        String suiv = "candidatureSpontanee" ;
        FacesContext fc = FacesContext.getCurrentInstance();
        Candidat cand = ((RegisterManagedBean)fc.getExternalContext().getSessionMap().get("connexionBean")).getCandidat() ;
        CandidatureSpontanee spont = new CandidatureSpontanee() ;
        spont.setCandidat(cand);
        spont.setDateCreation(new Date());
        try {
            if (!lm.getFileName().endsWith(".pdf") || !cv.getFileName().endsWith(".pdf")) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur :", "Les fichiers sont à déposer uniquement au format pdf !"));
            } else {
                File destFile = File.createTempFile("tempLM", ".pdf");
                FileUtils.copyInputStreamToFile(getLm().getInputstream(), destFile);
                spont.setLettre(destFile);
                destFile.deleteOnExit();
                destFile = File.createTempFile("tempCV", ".pdf") ;
                FileUtils.copyInputStreamToFile(getCv().getInputstream(), destFile) ;
                spont.setCv(destFile) ;
                destFile.deleteOnExit();
            
                Entreprise ent = (Entreprise)a.getComponent().getAttributes().get("ent") ;
                spont.setEmployeurs(ent.getEmployeurs());
                entrepriseEJB.creerCandidatureSpontanee(spont, ent);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info :", "Votre candidature a bien été déposée."));
                
                NotificationCandidatureS notification;
                for(Employeur empl : ent.getEmployeurs()) {
                    notification = new NotificationCandidatureS() ;
                    notification.setCandidature(spont);
                    notification.setDestinataire(empl) ;
                    notification.setMessage(cand.getPrenom()+" "+cand.getNom()+" a déposé une candidature spontanée auprès de votre entreprise.") ;
                    employeurEJB.addNotificationCandidatureSpontanee(notification, empl) ;
                }                
                
  
                suiv = "vitrineEntreprise" ;
            }
        } catch (IOException ex) {
            Logger.getLogger(PostulerManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        setNext(suiv) ;
    }

    /**
     * @return the lm
     */
    public UploadedFile getLm() {
        return lm;
    }

    /**
     * @param lm the lm to set
     */
    public void setLm(UploadedFile lm) {
        this.lm = lm;
    }

    /**
     * @return the cv
     */
    public UploadedFile getCv() {
        return cv;
    }

    /**
     * @param cv the cv to set
     */
    public void setCv(UploadedFile cv) {
        this.cv = cv;
    }

    /**
     * @return the next
     */
    public String getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(String next) {
        this.next = next;
    }

    
}
