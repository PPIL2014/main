/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.EmployeurLocal;
import interfaces.EntrepriseLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import persistence.Employeur;
import persistence.Entreprise;

/**
 *
 * @author Marc
 */
@ManagedBean(name = "rechercheEntrepriseBean")
@SessionScoped
public class RechercheEntrepriseBean implements Serializable{

    private int id;
    private String nomEntreprise;
    private String domaine;
    private String statut;
    private String ville;
    private String pays;
    
    private Entreprise entreprise;
    
    @Inject
    private EntrepriseLocal entrepriseEJB; 
    private EmployeurLocal employeurEJB; 
    
    
    @PostConstruct
    public void initialisation() {
        
    }
    
    public boolean entrepriseTrouve(){
        return !entrepriseEJB.rechercheEntreprise(nomEntreprise, domaine, ville, pays, statut).isEmpty();
    }    
    
    public List<Entreprise>listeEntreprise(){
        List<Entreprise> lc = entrepriseEJB.rechercheEntreprise(nomEntreprise, domaine, ville, pays, statut );
        return lc;
    }
    
    public List<Employeur>listeEmployeur(){
        List<Employeur> lc = employeurEJB.rechercheEmployeur(id );
        return lc;
    }
    
    public List<String> getAllVille() {
        List<String> lv=entrepriseEJB.getAllVille();
        return lv;
    }
    
     public List<String> getAllPays() {
        List<String> lv=entrepriseEJB.getAllPays();
        return lv;
    }


    public List<String> getAllNomEntreprise() {
        List<String> lv=entrepriseEJB.getAllNomEntreprise();
        return lv;
    }
    
    public String reponseRecherche(){       
        return "resultatRechercheEntreprise";
    }
    
    /**
     * @return the domaine
     */
    public String getDomaine() {
        return domaine;
    }

    /**
     * @param domaine the domaine to set
     */
    public void setDomaine(String domaine) {
        this.domaine = domaine;
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
     * @return the nomEntreprise
     */
    public String getNomEntreprise() {
        return nomEntreprise;
    }

    /**
     * @param nomEntreprise  the nom to set
     */
    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    /**
     * @return the statut
     */
    public String getStatut() {
        return statut;
    }

    /**
     * @param statut the prenom to set
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }




}
