/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.AnnonceLocal;
import interfaces.EmployeurLocal;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
import persistence.Annonce;
import persistence.Candidat;
import persistence.CandidatureAnnonce;
import persistence.NotificationEmployeur;

/**
 *
 * @author Marie
 */
@ManagedBean
@RequestScoped
public class PostulerManagedBean implements Serializable {
    
    private UploadedFile lm ;
    private UploadedFile cv ;
    private NotificationEmployeur notification;
    private String next ;
    
    @Inject
    private AnnonceLocal annonceEJB ;
    @Inject
    private EmployeurLocal employeurEJB;
    
    
    @PostConstruct
    public void initialisation() {

    }
    
    public void postuler(ActionEvent a) {
        String suiv = "postulerAnnonces" ;
        FacesContext fc = FacesContext.getCurrentInstance();
        Candidat cand = ((RegisterManagedBean)fc.getExternalContext().getSessionMap().get("connexionBean")).getCandidat() ;
        CandidatureAnnonce candidAnn = new CandidatureAnnonce() ;
        candidAnn.setCandidat(cand);
        candidAnn.setDateCreation(new Date());
        try {
            if (!lm.getFileName().endsWith(".pdf") || !cv.getFileName().endsWith(".pdf")) {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur :", "Les fichiers sont à déposer uniquement au format pdf !"));
            } else {
                if(cv.getSize()>(5 * 1024 * 1024)){
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur :", "Le CV que vous essayez d'uploader est trop volumineux !"));

                }
                if(lm.getSize()>(5 * 1024 * 1024)){
                   fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur :", "La lettre de motivation que vous essayez d'uploader fichier est trop volumineuse !"));
 
                }
                else{
                    File destFile = File.createTempFile("tempLM", ".pdf");
                    FileUtils.copyInputStreamToFile(getLm().getInputstream(), destFile);
                    candidAnn.setLettre(destFile) ;
                    destFile.deleteOnExit();
                    destFile = File.createTempFile("tempCV", ".pdf") ;
                    FileUtils.copyInputStreamToFile(getCv().getInputstream(), destFile) ;
                    candidAnn.setCv(destFile) ;
                    destFile.deleteOnExit();

                    Annonce ann = (Annonce)a.getComponent().getAttributes().get("annonce") ;
                    candidAnn.setAnnonce(ann);
                    annonceEJB.creerCandidatureAnnonce(candidAnn,ann) ;
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info :", "Votre candidature a bien été prise en compte."));

                    String message = "Le candidat "+candidAnn.getCandidat().getNom()+" a postulé à votre annonce "+ann.getTitre();
                    setNotification(new NotificationEmployeur(ann.getEmployeur(),candidAnn,message));
                    employeurEJB.addNotificationEmp(ann.getEmployeur(), getNotification());

                    suiv = "consulterAnnonce" ;
                }
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
     * @return the notification
     */
    public NotificationEmployeur getNotification() {
        return notification;
    }

    /**
     * @param notification the notification to set
     */
    public void setNotification(NotificationEmployeur notification) {
        this.notification = notification;
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
