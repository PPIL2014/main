/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.List;
import javax.ejb.Local;
import persistence.Adresse;
import persistence.Candidat;
import persistence.CandidatureSpontanee;
import persistence.Entreprise;

/**
 *
 * @author celie
 */
@Local
public interface EntrepriseLocal {

    public Entreprise getEntrepriseByNom(String nom);
                
    public void saveEntreprise(Entreprise ent);
    
    public void removeEntreprise(Entreprise ent);
    
    public boolean existByName(String ent);
    
    public void change(Entreprise ent);
    
    public boolean aDeposerCand(Entreprise ent, Candidat cand) ;
    
    public void creerCandidatureSpontanee(CandidatureSpontanee spont, Entreprise ent) ; 

    public List<Entreprise> rechercheEntreprise(String nomEntreprise, String domaine, String ville, String pays, String statut);

    public List<String> getAllVille();

    public List<String> getAllPays();

    public List<String> getAllNomEntreprise();

    public Entreprise getEntrepriseById(int parseInt);
    
    public void change(Adresse addr);
    
    public int getNbEmployeurs(Entreprise e);
    
}
