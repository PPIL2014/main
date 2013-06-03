/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.AnnonceLocal;
import interfaces.CandidatLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import persistence.Adresse;
import persistence.Annonce;
import persistence.Candidat;
import persistence.Employeur;
import persistence.NotificationCandidat;

/**
 *
 * @author Yann
 */
@ManagedBean(name="creationAnnonceBean")
@SessionScoped
public class CreationAnnonceBean implements Serializable {

    private String titre;
    private String description;
    private Date dateLimite;
    private String contrat;
    private String secteur;
    private int salaire;
    private String etudes;
    private int experience;
    private String rue;
    private String ville;
    private String codePostal;
    private String pays;
    private int heure;
    
    private Annonce annonceAModifier;
    
    @Inject
    private AnnonceLocal annonceEJB;
    @Inject
    private CandidatLocal candidatEJB;
    
    
    /**
     * Creates a new instance of CreationAnnonceBean
     */
    public CreationAnnonceBean() {
    }

    @PostConstruct
    public void initialisation(){
        resetChamp();
        resetDateDuJour();
    }
    
    public String annuler(){
        resetChamp();
        resetDateDuJour();
        return "profilEmpl";
    }
    
    public void resetDateDuJour(){
        Date date=new Date();
        date.setMonth(date.getMonth()+3);
        setDateLimite(date);
    }
    
    public String versModificationAnnonce(Annonce a){
        // recuperer l'annonce
       
        setTitre(a.getTitre());
        setDescription(a.getDescription());
        setContrat(a.getContrat());
        setSecteur(a.getSecteur());
        setSalaire(a.getSalaire());
       
        Adresse adr = a.getLieu();
        if(adr==null){
            adr = new Adresse();
        }
       
        setRue(adr.getRue());
        setVille(adr.getVille());
        setCodePostal(adr.getCodePostal());
        setPays(adr.getPays());
        setExperience(0);
        setEtudes(a.getEtudes());
        setDateLimite(a.getDateLimite());
        setHeure(a.getHeure());
       
        annonceAModifier = a;
       
        
       
        return "modifierAnnonce";
       
    }
   
    private boolean nonValideString(String s) {
        String str = "";
        for(int i=0; i<s.length(); i++) {
            str = str + " ";
        }
        return s.equals(str);
    }
    
    public String modifierAnnonce(){
        annonceAModifier.setTitre(getTitre());
        annonceAModifier.setDescription(getDescription());
        annonceAModifier.setDateLimite(getDateLimite());
        annonceAModifier.setContrat(getContrat());
        annonceAModifier.setSecteur(getSecteur());
        annonceAModifier.setSalaire(getSalaire());
        annonceAModifier.setEtudes(getEtudes());
        annonceAModifier.setExperience(getExperience());
       
        /*boolean Badr = false;
        Adresse adr = annonceAModifier.getLieu();
        if(adr==null){
            Badr = true;
            adr = new Adresse(getRue(), getCodePostal(), getVille(), getCodePostal());
        } else{
            adr.setRue(getRue());
            adr.setVille(getVille());
            adr.setPays(getPays());
            adr.setCodePostal(getCodePostal());
        }

        annonceAModifier.setLieu(adr);*/
        
        annonceAModifier.setHeure(getHeure());
        
        boolean vide = (rue==null || rue.length()==0) && (ville==null || ville.length()==0) && (codePostal==null || codePostal.length()==0) && (pays==null || pays.length()==0);
        boolean remplie = (rue!=null && rue.length()!=0) && (ville!=null && ville.length()!=0) && (codePostal!=null && codePostal.length()!=0) && (pays!=null && pays.length()!=0);
        
        if(!vide && !remplie) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "L'adresse doit être vide ou remplie complètement"));  
            return null;
        } if(!vide) {
            if(nonValideString(rue) || nonValideString(ville) || nonValideString(pays)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Vous devez renseigner votre adresse"));  
                return  null;
            }
        }

        Adresse adr = annonceAModifier.getLieu();
        
        if(remplie) {
            adr.setRue(getRue());
            adr.setVille(getVille());
            adr.setPays(getPays());
            annonceEJB.miseAJourAdresse(adr);
        } else {
            annonceAModifier.setLieu(null);
        }
 
        /*if(Badr==false) {
            annonceEJB.miseAJourAdresse(adr);
        } else {
            annonceEJB.enregistrerAdresse(adr);
        }*/
       
        annonceEJB.miseAJourAnnonce(annonceAModifier);
       
        List<Candidat> lc = candidatEJB.rechercheCandidatByDomaine(annonceAModifier.getSecteur());

        for(int i = 0; i<lc.size(); i++){
            String message = "L'entreprise <"+annonceAModifier.getEmployeur().getEntreprise().getNomEntreprise()+"> a publié une annonce qui peut vous interesser";
            candidatEJB.addNotification(lc.get(i), new NotificationCandidat(lc.get(i), annonceAModifier, annonceAModifier.getEmployeur().getEntreprise(), message));
            
        }
        resetChamp();
       
        return "consulterAnnonce";
    }
   
    public void resetChamp(){
        setTitre(null);
        setDescription(null);
        setContrat(null);
        setSecteur(null);
        setSalaire(-1);
        setRue(null);
        setVille(null);
        setCodePostal(null);
        setPays(null);
        setExperience(-1);
        setEtudes(null);
        resetDateDuJour();
        setHeure(0);
    }

    public String creerAnnonce(){
        FacesContext fc = FacesContext.getCurrentInstance();
        Employeur emp = ((RegisterManagedBean)fc.getExternalContext().getSessionMap().get("connexionBean")).getEmployeur();
        Annonce annonce = new Annonce();


        boolean vide = (rue==null || rue.length()==0) && (ville==null || ville.length()==0) && (codePostal==null || codePostal.length()==0) && (pays==null || pays.length()==0);
        boolean remplie = (rue!=null && rue.length()!=0) && (ville!=null && ville.length()!=0) && (codePostal!=null && codePostal.length()!=0) && (pays!=null && pays.length()!=0);
        
        if(!vide && !remplie) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "L'adresse doit être vide ou remplie complètement"));  
            return null;
        } 
        if(!vide) {    
            if(nonValideString(rue) || nonValideString(ville) || nonValideString(pays)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Vous devez renseigner votre adresse"));  
                return  null;
            }   
        }

        if(remplie) {
            annonce.setLieu(new Adresse(getRue(), getCodePostal(),getVille(),getPays()));
        }
        
        annonce.setTitre(getTitre());
        annonce.setDescription(getDescription());

        if(getDateLimite()==null){
            Date actuelle = new Date();
            setDateLimite(actuelle);
        }
        annonce.setDateLimite(getDateLimite());

        Date actuelle = new Date();
        annonce.setDateEmission(actuelle);

        annonce.setContrat(getContrat());
        annonce.setSecteur(getSecteur());
        annonce.setSalaire(getSalaire());
        annonce.setEtudes(getEtudes());
        annonce.setExperience(getExperience());        
        annonce.setEmployeur(emp);
        annonce.setHeure(heure);
        annonceEJB.creerAnnonce(annonce,emp);
        
        List<Candidat> lc = candidatEJB.rechercheCandidatByDomaine(annonce.getSecteur());

        for(int i = 0; i<lc.size() ;i++){
                String message = "L'entreprise <"+emp.getEntreprise().getNomEntreprise()+"> a publié une annonce qui peut vous intéresser";
                candidatEJB.addNotification(lc.get(i), new NotificationCandidat(lc.get(i), annonce, annonce.getEmployeur().getEntreprise(), message));
        }
        
        resetChamp();

        return "profilEmpl";
    }

    public List<String> complete(String query) {
        List<String> results = new ArrayList<String>();

        for (int i = 0; i < 10; i++) {
            results.add(query + i);
        }

        return results;
    }

    public boolean validateDate(){
        Date actuel = new Date();
        Date date = (Date)getDateLimite();
        return date.before(actuel);
    }


    /**
     * @return the titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * @param titre the titre to set
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the dateLimite
     */
    public Date getDateLimite() {
        return dateLimite;
    }

    /**
     * @param dateLimite the dateLimite to set
     */
    public void setDateLimite(Date dateLimite) {
        this.dateLimite = dateLimite;
    }

    /**
     * @return the contrat
     */
    public String getContrat() {
        return contrat;
    }

    /**
     * @param contrat the contrat to set
     */
    public void setContrat(String contrat) {
        this.contrat = contrat;
    }

    /**
     * @return the secteur
     */
    public String getSecteur() {
        return secteur;
    }

    /**
     * @param secteur the secteur to set
     */
    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    /**
     * @return the salaire
     */
    public int getSalaire() {
        return salaire;
    }

    /**
     * @param salaire the salaire to set
     */
    public void setSalaire(int salaire) {
        this.salaire = salaire;
    }

    /**
     * @return the etudes
     */
    public String getEtudes() {
        return etudes;
    }

    /**
     * @param etudes the etudes to set
     */
    public void setEtudes(String etudes) {
        this.etudes = etudes;
    }

    /**
     * @return the experience
     */
    public int getExperience() {
        return experience;
    }

    /**
     * @param experience the experience to set
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * @return the rue
     */
    public String getRue() {
        return rue;
    }

    /**
     * @param rue the rue to set
     */
    public void setRue(String rue) {
        this.rue = rue;
    }

    /**
     * @return the ville
     */
    public String getVille() {
        return ville;
    }

    /**
     * @param ville the ville to set
     */
    public void setVille(String ville) {
        this.ville = ville;
    }

    /**
     * @return the codePostal
     */
    public String getCodePostal() {
        return codePostal;
    }

    /**
     * @param codePostal the codePostal to set
     */
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    /**
     * @return the pays
     */
    public String getPays() {
        return pays;
    }

    /**
     * @param pays the pays to set
     */
    public void setPays(String pays) {
        this.pays = pays;
    }

    /**
     * @return the heure
     */
    public int getHeure() {
        return heure;
    }

    /**
     * @param heure the heure to set
     */
    public void setHeure(int heure) {
        this.heure = heure;
    }


}