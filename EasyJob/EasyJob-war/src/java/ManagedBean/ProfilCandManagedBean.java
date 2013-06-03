/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.CandidatLocal;
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
import persistence.Candidat;
import persistence.CandidatureAnnonce;
import persistence.CandidatureSpontanee;
import persistence.Entreprise;
import persistence.NotificationCandidat;
import persistence.SuggestionAnnonce;

/**
 *
 * @author enis
 */
@ManagedBean(name="profilcandBean")
@RequestScoped
public class ProfilCandManagedBean implements Serializable{
    
    private Candidat candidat;
    
    private String raison;
    private ArrayList<String> listeRaisons;
    private String mdp;
    
    private ArrayList<Entreprise> listeSuppressionSuivi;
    private ArrayList<NotificationCandidat> listeSuppressionNotif;
    private ArrayList<SuggestionAnnonce> listeSuppressionSuggestion;
    
    @Inject
    private CandidatLocal candidatEJB;  
    
    @Inject
    private EntrepriseLocal entrepriseEJB;
    
    
    @PostConstruct
    public void initialisation() {
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        if(rmb!=null) {
            candidat = rmb.getCandidat();
            candidat = candidatEJB.getCandidatByMail(candidat.getMail());
        } else {
            candidat = null;
        }
        
        listeRaisons = new ArrayList<String>();
        listeRaisons.add("Vous avez trouvé un emploi via EasyJob");
        listeRaisons.add("Vous avez trouvé un emploi via un autre moyen");
        listeRaisons.add("Autre");          
    }
    
    public String supprimerProfil() throws IOException{
        String next = "supprimerCand";
        if(!candidatEJB.loginCandidat(candidat.getMail(), mdp)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Mot de passe incorrect"));  
        } else {
            candidatEJB.removeCandidat(candidat);
            FacesContext fc = FacesContext.getCurrentInstance();
            RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
            rmb.deconnexion();
            next = "accueil";
            fc.getExternalContext().redirect("accueil.xhtml");
        }
        return next;
    }
    
    public List<CandidatureSpontanee> listeCandidatureSpontanee() {
        return candidatEJB.getCandidatureSpontanee(candidat);
    }
    
    public void valueChangeSuggestion(ValueChangeEvent e){      
        if(e.getNewValue().equals("true")){
            if(getListeSuppressionSuggestion()==null) {
                setListeSuppressionSuggestion(new ArrayList<SuggestionAnnonce>());
            }
            getListeSuppressionSuggestion().add((SuggestionAnnonce)e.getComponent().getAttributes().get("sugges"));
        }
    }
    
    public String suppressionSuggestion() {
        if(listeSuppressionSuggestion!=null) {
            for(SuggestionAnnonce s : listeSuppressionSuggestion)
                candidatEJB.removeSuggestion(s, candidat);
        }
        return "consulterSuggestionAnnonce";
    }
    
    public String ajouterSuivi() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("entreprise");
        Entreprise en = entrepriseEJB.getEntrepriseById(Integer.parseInt(id));
        candidat.getEntreprises().add(en);
        candidatEJB.majCandidat(candidat);  
        return null;
    }
    
    public void valueChangeSuppression(ValueChangeEvent e){
        if(e.getNewValue().equals("true")){
            if(getListeSuppressionSuivi()==null) {
                setListeSuppressionSuivi(new ArrayList<Entreprise>());
            }
            getListeSuppressionSuivi().add((Entreprise)e.getComponent().getAttributes().get("ent"));
        }
    }
    
    public String enleverSuivi(){
        if(getListeSuppressionSuivi()!=null){
            int i;
            List<Entreprise> le;
            for(Entreprise e : getListeSuppressionSuivi()){
                le = this.candidat.getEntreprises();
                i = 0;
                while(i<le.size()&&le.get(i).getId()!=e.getId()){
                    i++;
                }
                le.remove(i);
            }
            candidatEJB.majCandidat(candidat);
        }
        return "listeSuiviCandidat";
    }
    
    public String experience(){
        String exp = "";
        int expe = candidat.getNbExperiences();
        switch(expe) {
            case 0: exp = "< 1 an";
                break;
            case 1: exp = "1 an";
                break;
            case 2: exp = "2 ans";
                break;
            case 3: exp = "3 ans";
                break;
            case 4: exp = "4 ans";
                break;
            case 5: exp = "5 à 10 ans";
                break;
            case 10: exp = "+ de 10 ans";
                break;
        }
        return exp;
    }

    public boolean existeEntreprise(NotificationCandidat notif) {
        boolean b = false;
        if(notif.getEntreprise()!=null && notif.getMessage().contains("supprimé"))
            b = true;
        return b;
    }
    
    public void valueChange(ValueChangeEvent e){
        if(e.getNewValue().equals("true")){
            if(getListeSuppressionNotif()==null) {
                setListeSuppressionNotif(new ArrayList<NotificationCandidat>());
            }
            getListeSuppressionNotif().add((NotificationCandidat)e.getComponent().getAttributes().get("notif"));
        }
    }
    
    public String suppressionNotification() {
        if(listeSuppressionNotif!=null) {
            for(NotificationCandidat n : listeSuppressionNotif)
                candidatEJB.removeNtification(candidat, n);
        }
        return "consulterNotificationCand";
    }
    
    public List<CandidatureAnnonce> listeCandidatureAnnocePostule(){
        return candidatEJB.getAnnonceParticipe(candidat);
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
    public ArrayList<Entreprise> getListeSuppressionSuivi() {
        return listeSuppressionSuivi;
    }

    /**
     * @param listeSuppressionSuivi the listeSuppressionSuivi to set
     */
    public void setListeSuppressionSuivi(ArrayList<Entreprise> listeSuppressionSuivi) {
        this.listeSuppressionSuivi = listeSuppressionSuivi;
    }

    /**
     * @return the listeSuppressionNotif
     */
    public ArrayList<NotificationCandidat> getListeSuppressionNotif() {
        return listeSuppressionNotif;
    }

    /**
     * @param listeSuppressionNotif the listeSuppressionNotif to set
     */
    public void setListeSuppressionNotif(ArrayList<NotificationCandidat> listeSuppressionNotif) {
        this.listeSuppressionNotif = listeSuppressionNotif;
    }

    /**
     * @return the listeSuppressionSuggestion
     */
    public ArrayList<SuggestionAnnonce> getListeSuppressionSuggestion() {
        return listeSuppressionSuggestion;
    }

    /**
     * @param listeSuppressionSuggestion the listeSuppressionSuggestion to set
     */
    public void setListeSuppressionSuggestion(ArrayList<SuggestionAnnonce> listeSuppressionSuggestion) {
        this.listeSuppressionSuggestion = listeSuppressionSuggestion;
    }
   
}
