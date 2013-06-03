/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.AnnonceLocal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import persistence.Annonce;

/**
 *
 * @author Maxime
 */
@ManagedBean(name = "rechercheAnnonceBean")
@SessionScoped
public class RechercheAnnonceBean implements Serializable{

    
    private String contrat;
    private String niveauEtude;
    private String secteur;
    private Date date;
    private int experience;
    private int salaire;
    private String lieu;
       
    @Inject
    private AnnonceLocal annonceEJB; 
    
    
    @PostConstruct
    public void initialisation() {
        salaire = -1;
    }
    
    /**
     * retourne la liste d'annonces selon les criteres souhaites
     * @return 
     */
    public List<Annonce> listeAnnonce(){
        List<Annonce> la = annonceEJB.rechercheAnnonce(contrat, niveauEtude, secteur, date, experience, salaire, lieu);
        return la;
    }
    
    public boolean annonceTrouve(){
        return !annonceEJB.rechercheAnnonce(contrat, niveauEtude, secteur, date, experience, salaire, lieu).isEmpty();
    }    
    
    public List<String> getAllVille(){
        List<String> lv=annonceEJB.getAllVille();
        return lv;
    }
    
    /**
     * @return the contrat
     */
    public String getContrat() {
        return contrat;
    }

    /**
     * @return the niveauEtude
     */
    public String getNiveauEtude() {
        return niveauEtude;
    }

    /**
     * @return the secteur
     */
    public String getSecteur() {
        return secteur;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the experience
     */
    public int getExperience() {
        return experience;
    }

    /**
     * @return the salaire
     */
    public int getSalaire() {
        return salaire;
    }

    /**
     * @return the lieu
     */
    public String getLieu() {
        return lieu;
    }

    /**
     * @param contrat the contrat to set
     */
    public void setContrat(String contrat) {
        this.contrat = contrat;
    }

    /**
     * @param niveauEtude the niveauEtude to set
     */
    public void setNiveauEtude(String niveauEtude) {
        this.niveauEtude = niveauEtude;
    }

    /**
     * @param secteur the secteur to set
     */
    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @param experience the experience to set
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * @param salaire the salaire to set
     */
    public void setSalaire(int salaire) {
        this.salaire = salaire;
    }

    /**
     * @param lieu the lieu to set
     */
    public void setLieu(String lieu) {      
        this.lieu = lieu;
    }

}
