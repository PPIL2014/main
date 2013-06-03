/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.CandidatLocal;
import interfaces.EmployeurLocal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import persistence.Candidat;
import persistence.Employeur;
import persistence.NotificationEmployeur;
import persistence.SuggestionCandidat;


/**
 *
 * @author verica
 */
@ManagedBean(name = "suggCandBean")
@ViewScoped
public class SuggestionCandidatManagedBean implements Serializable {

    private Employeur employeur;
    private String nomEtr;
    private String prenomEntr;
    private boolean multiple;
    private Candidat candidat;
    private Employeur selected;
    private List<Employeur> listEmpl;
    
    @Inject
    private CandidatLocal candidatEJB; 
    
    @Inject
    private EmployeurLocal employeurEJB;


    @PostConstruct
    public void initialisation() {
        multiple = false;
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("candidat");
        if(id!=null) {
            candidat = candidatEJB.getCandidatById(Integer.parseInt(id));
        }
    }

    public String valider() {
        listEmpl = employeurEJB.getEmployeurByNom(nomEtr, prenomEntr);
        if(listEmpl.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "L'employeur n'existe pas"));  
        }else if (listEmpl.size()==1) {
             FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
            Employeur emetteur = rmb.getEmployeur();
            if(employeurEJB.aSuggere(emetteur, candidat, listEmpl.get(0))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Vous avez déjà suggéré ce candidat à cet employeur"));  
            } else {                                                                
                nouvelleSuggestion(listEmpl.get(0));                
                return "vitrineCandidat";
            }
        } else {
            multiple = true;
        }
        return null;        
    }
    
    private void nouvelleSuggestion(Employeur destinataire) {
        SuggestionCandidat sc = new SuggestionCandidat();
        sc.setDateCreation(new Date());
        sc.setCandidat(candidat);
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        Employeur emetteur = rmb.getEmployeur();
        sc.setDestinataire(destinataire);
        sc.setEmetteur(emetteur);
        employeurEJB.addSuggestionCandidat(sc, destinataire);
        if(emetteur != null) {
            String message = "L'employeur "+emetteur.getNom()+" "+emetteur.getPrenom()+" vous suggère un candidat. ";
            NotificationEmployeur notification = new NotificationEmployeur();
            notification.setDestinataire(destinataire);
            notification.setMessage(message);
            employeurEJB.addNotificationEmp(destinataire, notification);
        }
    }
    
    public String choisir(String id1) {
        candidat = candidatEJB.getCandidatById(Integer.parseInt(id1));
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        Employeur emetteur = rmb.getEmployeur();
        if(employeurEJB.aSuggere(emetteur, candidat, selected)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Vous avez déjà suggéré ce candidat à cet employeur"));  
                return null;
        } else {                                                                
            nouvelleSuggestion(selected);                
            return "vitrineCandidat";
            }
    }
    
    public String retour() {
        multiple = false;
        listEmpl = null;
        return null;
    }

    /**
     * @return the employeur
     */
    public Employeur getEmployeur() {
        return employeur;
    }


    /**
     * @param employeur the employeur to set
     */
    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    /**
     * @return the listEmpl
     */
    public List<Employeur> getListEmpl() {
        return listEmpl;
    }

    /**
     * @param listEmpl the listEmpl to set
     */
    public void setListEmpl(List<Employeur> listEmpl) {
        this.listEmpl = listEmpl;
    }

    /**
     * @return the nomEtr
     */
    public String getNomEtr() {
        return nomEtr;
    }

    /**
     * @param nomEtr the nomEtr to set
     */
    public void setNomEtr(String nomEtr) {
        this.nomEtr = nomEtr;
    }

    /**
     * @return the prenomEntr
     */
    public String getPrenomEntr() {
        return prenomEntr;
    }

    /**
     * @param prenomEntr the prenomEntr to set
     */
    public void setPrenomEntr(String prenomEntr) {
        this.prenomEntr = prenomEntr;
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
     * @return the candidat
     */
    public Candidat getCandidat() {
        return candidat;
    }

    /**
     * @param candidat the candidat to set
     */
    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    /**
     * @return the selected
     */
    public Employeur getSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(Employeur selected) {
        this.selected = selected;
    }
}
