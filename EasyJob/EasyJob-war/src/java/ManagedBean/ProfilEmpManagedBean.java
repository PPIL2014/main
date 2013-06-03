/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.EmployeurLocal;
import interfaces.EntrepriseLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import persistence.Annonce;
import persistence.Candidat;
import persistence.Employeur;
import persistence.Entreprise;
import persistence.NotificationCandidatureS;
import persistence.NotificationEmployeur;
import persistence.SuggestionCandidat;

/**
 *
 * @author enis
 */
@ManagedBean(name="profilEmpBean")
@RequestScoped
public class ProfilEmpManagedBean implements Serializable{
    
    private Employeur employeur;

    private String raison;
    private ArrayList<String> listeRaisons;
    private String mdp;
    
    private List<Candidat> listeSuppressionSuivi;
    private ArrayList<NotificationEmployeur> listeSuppressionNotif;
    private ArrayList<SuggestionCandidat> listeSuppressionSuggestion;
    private ArrayList<NotificationCandidatureS> listeSuppressionNotifCS ; 
    
    @Inject
    private EmployeurLocal employeurEJB;
    @Inject
    private EntrepriseLocal entrepriseEJB;

    @PostConstruct
    public void initialisation() {
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        if(rmb!=null) {
            employeur = rmb.getEmployeur();
        } else {
            employeur = null;
        }

	listeRaisons = new ArrayList<String>();
        listeRaisons.add("EasyJob ne répond pas à vos attentes");
        listeRaisons.add("Vous n'avez plus de poste à pourvoir");
        listeRaisons.add("Autre");
    }

    public String supprimerProfil() throws IOException {
        String next = "supprimerEmp";
        if(!employeurEJB.loginEmployeur(employeur.getMail(), mdp)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Mot de passe incorrect"));  
        } else {
            Entreprise e = getEmployeur().getEntreprise();
            employeurEJB.removeEmployeur(getEmployeur());
            int nbEmp = entrepriseEJB.getNbEmployeurs(getEmployeur().getEntreprise());
            if(nbEmp == 0) {
                employeurEJB.remove(e);
            }
            FacesContext fc = FacesContext.getCurrentInstance();
            RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
            rmb.deconnexion();
            next = "accueil";
            fc.getExternalContext().redirect("accueil.xhtml");
        }
        return next;
    }
    
    public void valueChange(ValueChangeEvent e){
        if(e.getNewValue().equals("true")){
            if(getListeSuppressionNotif()==null) {
                setListeSuppressionNotif(new ArrayList<NotificationEmployeur>());
            }
            getListeSuppressionNotif().add((NotificationEmployeur)e.getComponent().getAttributes().get("notif"));
        }
    }
    
    public void valueChangeNotifCS(ValueChangeEvent e) {
        if (e.getNewValue().equals("true")) {
            if (getListeSuppressionNotifCS() == null) {
                setListeSuppressionNotifCS(new ArrayList<NotificationCandidatureS>());
            }
            getListeSuppressionNotifCS().add((NotificationCandidatureS)e.getComponent().getAttributes().get("notif2")) ;
        }
    }
    
    public String suppressionNotification() {
        if(getListeSuppressionNotif()!=null) {
            for(NotificationEmployeur n : getListeSuppressionNotif())
                employeurEJB.removeNotificationEmp(employeur, n);
        }
        if (getListeSuppressionNotifCS()!=null) {
            for (NotificationCandidatureS ncs : getListeSuppressionNotifCS()) {
                employeurEJB.removeNotificationCandidatureSpontanee(ncs, employeur) ;
            }
        }
        return "consulterNotificationEmp";
    }
    
    public void valueChangeSuggestion(ValueChangeEvent e){
        if(e.getNewValue().equals("true")){
            if(getListeSuppressionSuggestion()==null) {
                setListeSuppressionSuggestion(new ArrayList<SuggestionCandidat>());
            }
            getListeSuppressionSuggestion().add((SuggestionCandidat)e.getComponent().getAttributes().get("sugges"));
        }
    }
    
    public String suppressionSuggestion() {
        if(getListeSuppressionSuggestion()!=null) {
            for(SuggestionCandidat s : getListeSuppressionSuggestion())
                employeurEJB.removeSuggestionCandidat(s, employeur);
        }
        return "consulterSuggestionCandidat";
    }
    
    public String ajouterAMaListeSuivi(Candidat c){      
        employeur.getCandidats().add(c);
        employeurEJB.majEmployeur(employeur);  
        return null;
    }
    
    
    public void valueChangeSuppression(ValueChangeEvent e){
        if(e.getNewValue().equals("true")){
            if(this.getListeSuppressionSuivi()==null)this.setListeSuppressionSuivi(new ArrayList<Candidat>());
            this.getListeSuppressionSuivi().add((Candidat)e.getComponent().getAttributes().get("can"));
        }
    }
    
    public String enleverSuivi(){
        if(getListeSuppressionSuivi()!=null){
            int i;
            List<Candidat> le;
            for(Candidat e:getListeSuppressionSuivi()){
                le=this.employeur.getCandidats();
                i=0;
                while(i<le.size()&&!le.get(i).getId().equals(e.getId())){
                    i++;
                }
                le.remove(i);
            }
            employeurEJB.majEmployeur(employeur);
        }
        return "listeSuiviEmployeur";
    }
    
    
    public boolean containsCandidat(Candidat c){
        FacesContext fc = FacesContext.getCurrentInstance();
        Employeur emp =  ((RegisterManagedBean)fc.getExternalContext().getSessionMap().get("connexionBean")).getEmployeur();
        if(emp!=null)emp=employeurEJB.getEmployeurByMail(emp.getMail());
        boolean b=emp!=null&&emp.containsCandidat(c);
        return b;
    }
    
    public List<Annonce> listeAnnonceCreee(){
        return employeurEJB.listeAnnonceCree(employeur);
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
     * @return the raison
     */
    public String getRaison() {
        return raison;
    }

    /**
     * @param raison the raison to set
     */
    public void setRaison(String raison) {
        this.raison = raison;
    }

    /**
     * @return the listeRaisons
     */
    public ArrayList<String> getListeRaisons() {
        return listeRaisons;
    }

    /**
     * @param listeRaisons the listeRaisons to set
     */
    public void setListeRaisons(ArrayList<String> listeRaisons) {
        this.listeRaisons = listeRaisons;
    }

    /**
     * @return the mdp
     */
    public String getMdp() {
        return mdp;
    }

    /**
     * @param mdp the mdp to set
     */
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
    /**
     * @return the listeSuppressionSuivi
     */
    public List<Candidat> getListeSuppressionSuivi() {
        return listeSuppressionSuivi;
    }

    /**
     * @param listeSuppressionSuivi the listeSuppressionSuivi to set
     */
    public void setListeSuppressionSuivi(List<Candidat> listeSuppressionSuivi) {
        this.listeSuppressionSuivi = listeSuppressionSuivi;
    }

    /**
     * @return the listeSuppressionNotif
     */
    public ArrayList<NotificationEmployeur> getListeSuppressionNotif() {
        return listeSuppressionNotif;
    }

    /**
     * @param listeSuppressionNotif the listeSuppressionNotif to set
     */
    public void setListeSuppressionNotif(ArrayList<NotificationEmployeur> listeSuppressionNotif) {
        this.listeSuppressionNotif = listeSuppressionNotif;
    }

    /**
     * @return the listeSuppressionSuggestion
     */
    public ArrayList<SuggestionCandidat> getListeSuppressionSuggestion() {
        return listeSuppressionSuggestion;
    }

    /**
     * @param listeSuppressionSuggestion the listeSuppressionSuggestion to set
     */
    public void setListeSuppressionSuggestion(ArrayList<SuggestionCandidat> listeSuppressionSuggestion) {
        this.listeSuppressionSuggestion = listeSuppressionSuggestion;
    }

    /**
     * @return the listeSuppressionNotifCS
     */
    public ArrayList<NotificationCandidatureS> getListeSuppressionNotifCS() {
        return listeSuppressionNotifCS;
    }

    /**
     * @param listeSuppressionNotifCS the listeSuppressionNotifCS to set
     */
    public void setListeSuppressionNotifCS(ArrayList<NotificationCandidatureS> listeSuppressionNotifCS) {
        this.listeSuppressionNotifCS = listeSuppressionNotifCS;
    }

    
    
}
