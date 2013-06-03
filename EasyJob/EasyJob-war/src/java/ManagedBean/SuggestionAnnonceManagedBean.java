/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.AnnonceLocal;
import interfaces.CandidatLocal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import persistence.Annonce;
import persistence.Candidat;
import persistence.NotificationCandidat;
import persistence.SuggestionAnnonce;

/**
 *
 * @author celie
 */
@ManagedBean(name = "suggestionAnnonceBean")
@ViewScoped
public class SuggestionAnnonceManagedBean implements Serializable{

    private Annonce annonce;
    private String nomDest;
    private String prenomDest;
    private boolean multiple;
    private List<Candidat> liste;
    private Candidat selected;
    private NotificationCandidat notification;

    @Inject
    private CandidatLocal candidatEJB;
    
    @Inject
    private AnnonceLocal annonceEJB;

    
    @PostConstruct 
    public void initialisation() {
        multiple = false;
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("annonce");
        if(id!=null) {
            annonce = annonceEJB.getAnnonceById(Integer.parseInt(id));
        }
    }
    
    public String valider() {
        liste = candidatEJB.getCandidatByNom(nomDest, prenomDest);
        if(getListe().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Le candidat n'existe pas"));  
        } else if (getListe().size()==1) {
            FacesContext fc = FacesContext.getCurrentInstance();
            RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
            Candidat emetteur = rmb.getCandidat();
            if(candidatEJB.aSuggere(emetteur, annonce, getListe().get(0))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Vous avez déjà suggéré cette annonce à ce candidat."));  
            } else {
                nouvelleSuggestion(getListe().get(0));
                return "consulterAnnonce";
            }
        } else {
            multiple = true;
        }
        return null;
    }

    private void nouvelleSuggestion(Candidat destinataire) {
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        Candidat emetteur = rmb.getCandidat();
        SuggestionAnnonce suggestion = new SuggestionAnnonce(annonce, emetteur, destinataire);
        suggestion.setDateCreation(new Date());
        candidatEJB.addSuggestion(suggestion, destinataire);
        String message = "Le candidat "+emetteur.getNom()+" "+emetteur.getPrenom()+" vous suggère cette annonce: "+annonce.getTitre();
        setNotification(new NotificationCandidat(destinataire, annonce, annonce.getEmployeur().getEntreprise(),message));
        candidatEJB.addNotification(destinataire, getNotification());
    }
    
    public String choisir(String id2) {
        annonce = annonceEJB.getAnnonceById(Integer.parseInt(id2));
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        Candidat emetteur = rmb.getCandidat();
        if(candidatEJB.aSuggere(emetteur, annonce, selected)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Vous avez déjà suggéré cette annonce à ce candidat."));  
            return null;
        } else {
            nouvelleSuggestion(selected);
            return "consulterAnnonce";
        }
    }
    
    public String retour() {
        multiple = false;
        liste = null;
        return null;
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
     * @return the nomDest
     */
    public String getNomDest() {
        return nomDest;
    }

    /**
     * @param nomDest the nomDest to set
     */
    public void setNomDest(String nomDest) {
        this.nomDest = nomDest;
    }

    /**
     * @return the prenomDest
     */
    public String getPrenomDest() {
        return prenomDest;
    }

    /**
     * @param prenomDest the prenomDest to set
     */
    public void setPrenomDest(String prenomDest) {
        this.prenomDest = prenomDest;
    }

    /**
     * @return the multiple
     */
    public boolean isMultiple() {
        return multiple;
    }

    /**
     * @param multiple the multiple to set
     */
    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    /**
     * @return the liste
     */
    public List<Candidat> getListe() {
        return liste;
    }

    /**
     * @param liste the liste to set
     */
    public void setListe(List<Candidat> liste) {
        this.liste = liste;
    }

    /**
     * @return the selected
     */
    public Candidat getSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(Candidat selected) {
        this.selected = selected;
    }

    /**
     * @return the notification
     */
    public NotificationCandidat getNotification() {
        return notification;
    }

    /**
     * @param notification the notification to set
     */
    public void setNotification(NotificationCandidat notification) {
        this.notification = notification;
    } 
}


