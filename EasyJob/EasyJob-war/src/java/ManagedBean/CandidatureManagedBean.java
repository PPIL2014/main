/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.AnnonceLocal;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import persistence.Annonce;
import persistence.CandidatureAnnonce;
import persistence.CandidatureSpontanee;

/**
 *
 * @author celie
 */
@ManagedBean(name = "candidatureManagedBean")
@ViewScoped
public class CandidatureManagedBean implements Serializable {

    private CandidatureAnnonce candidatureAnnonce;
    private CandidatureSpontanee candidatureSpontanee;
    private Annonce annonce;
    private StreamedContent cv ;
    private StreamedContent lm ;
    
    @Inject
    private AnnonceLocal annonceEJB;
    
    
    @PostConstruct
    public void initialisation() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if(id!=null) {
            candidatureAnnonce = annonceEJB.getCandidatureAnnonce(Integer.parseInt(id));
        }
        
        id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("annonce");
        if(id!=null) {
            annonce = annonceEJB.getAnnonceById(Integer.parseInt(id));
        }
        
        id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id2") ;
        if (id != null) {
            candidatureSpontanee = annonceEJB.getCandidatureSpontanee(Integer.parseInt(id)) ;
        }
                
        if (candidatureAnnonce != null) {           
            InputStream stream1 ;
            InputStream stream2 ;
            try {         
                if (candidatureAnnonce.getCv() != null && candidatureAnnonce.getLettre() != null) {
                    stream1 = new FileInputStream(candidatureAnnonce.getCv().getCanonicalFile());
                    cv = new DefaultStreamedContent(stream1, "application/pdf", "cv.pdf");
                    stream2 = new FileInputStream(candidatureAnnonce.getLettre().getCanonicalFile()) ;
                    lm = new DefaultStreamedContent(stream2, "application/pdf", "lm.pdf");
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CandidatureManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CandidatureManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }         
        }
        
        if (candidatureSpontanee != null) {
            if (candidatureSpontanee.getCv() != null && candidatureSpontanee.getLettre() != null) {
                try {
                    InputStream stream1 = new FileInputStream(candidatureSpontanee.getCv().getCanonicalFile()) ;
                    cv = new DefaultStreamedContent(stream1, "application/pdf", "cv.pdf");
                    InputStream stream2 = new FileInputStream(candidatureSpontanee.getLettre().getCanonicalFile()) ;
                    lm = new DefaultStreamedContent(stream2, "application/pdf", "lm.pdf");
                } catch (IOException ex) {
                    Logger.getLogger(CandidatureManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public String supprimerCandidatureSpontanee(){
        RegisterManagedBean rmb=(RegisterManagedBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("connexionBean");
        annonceEJB.supprimerCandidatureSpontanee(this.getCandidatureSpontanee(),rmb.getEmployeur());
        return "listeCandidaturesSpontanees";
    }
    
    /**
     * @return the candidatureAnnonce
     */
    public CandidatureAnnonce getCandidatureAnnonce() {
        return candidatureAnnonce;
    }

    /**
     * @param candidatureAnnonce the candidatureAnnonce to set
     */
    public void setCandidatureAnnonce(CandidatureAnnonce candidatureAnnonce) {
        this.candidatureAnnonce = candidatureAnnonce;
    }

    /**
     * @return the candidatureSpontanee
     */
    public CandidatureSpontanee getCandidatureSpontanee() {
        return candidatureSpontanee;
    }

    /**
     * @param candidatureSpontanee the candidatureSpontanee to set
     */
    public void setCandidatureSpontanee(CandidatureSpontanee candidatureSpontanee) {
        this.candidatureSpontanee = candidatureSpontanee;
    }

    /**
     * @return the annonce
     */
    public Annonce getAnnonce() {
        return annonce;
    }

    /**
     * @param annonce the annonce to set
     */
    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    /**
     * @return the cv
     */
    public StreamedContent getCv() {
        return cv;
    }

    /**
     * @param cv the cv to set
     */
    public void setCv(StreamedContent cv) {
        this.cv = cv;
    }

    /**
     * @return the lm
     */
    public StreamedContent getLm() {
        return lm;
    }

    /**
     * @param lm the lm to set
     */
    public void setLm(StreamedContent lm) {
        this.lm = lm;
    }

}
