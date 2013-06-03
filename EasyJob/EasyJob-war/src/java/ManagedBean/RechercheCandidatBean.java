/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.CandidatLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import persistence.Candidat;

/**
 *
 * @author Marc
 */
@ManagedBean(name = "rechercheCandidatBean")
@SessionScoped
public class RechercheCandidatBean implements Serializable{


    private String domaineEtudes;
    private Integer nbExperiences;
    private String niveauEtude;
    private String ville;
    private String pays;
    private String nom;
    private String prenom;
    
    private Candidat candidat;
    
    @Inject
    private CandidatLocal candidatEJB; 
    
    
    @PostConstruct
    public void initialisation() {
        
    }
    
    public boolean candidatTrouve(){
        return !candidatEJB.rechercheCandidat(domaineEtudes, nbExperiences, niveauEtude, ville, pays, nom, prenom).isEmpty();
    }    
    
    public List<Candidat>listeCandidat(){
        List<Candidat> lc=candidatEJB.rechercheCandidat(domaineEtudes, nbExperiences, niveauEtude, ville, pays, nom, prenom );
        return lc;
    }
    
    public List<String> getAllVille() {
        List<String> lv=candidatEJB.getAllVille();
        return lv;
    }
    
     public List<String> getAllPays() {
        List<String> lv=candidatEJB.getAllPays();
        return lv;
    }


    public List<String> getAllNomCandidat() {
        List<String> lv=candidatEJB.getAllNomCandidat();
        return lv;
    }

    public List<String> getAllPrenomCandidat() {
        List<String> lv=candidatEJB.getAllPrenomCandidat();
        return lv;
    }
    
    public String reponseRecherche(){    
        return "resultatRechercheCandidat";
    }
    

    /**
     * @return the domaineEtudes
     */
    public String getDomaineEtudes() {
        return domaineEtudes;
    }

    /**
     * @param domaineEtudes the domaineEtudes to set
     */
    public void setDomaineEtudes(String domaineEtudes) {
        this.domaineEtudes = domaineEtudes;
    }

    /**
     * @return the nbExperiences
     */
    public Integer getNbExperiences() {
        return nbExperiences;
    }

    /**
     * @param nbExperiences the nbExperiences to set
     */
    public void setNbExperiences(Integer nbExperiences) {
        this.nbExperiences = nbExperiences;
    }

    /**
     * @return the niveauEtude
     */
    public String getNiveauEtude() {
        return niveauEtude;
    }

    /**
     * @param niveauEtude the niveauEtude to set
     */
    public void setNiveauEtude(String niveauEtude) {
        this.niveauEtude = niveauEtude;
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
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }




}
