/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import persistence.Adresse;
import persistence.Annonce;
import persistence.Candidat;
import persistence.CandidatureAnnonce;
import persistence.CandidatureSpontanee;
import persistence.Employeur;

/**
 *
 * @author celie
 */
@Local
public interface AnnonceLocal {
    
    public Annonce getAnnonceById(long id);
    
    public boolean aPostuler(Annonce annonce, Candidat candidat) ;
    
    public void creerCandidatureAnnonce(CandidatureAnnonce candidAnn, Annonce annonce) ;
    
    public void creerAnnonce(Annonce annonce, Employeur emp);
    
    public void supprimerAnnonce(Annonce a);
    
    public boolean estProprietaire(Annonce a,Employeur e);

    public List<Annonce> rechercheAnnonce(String contrat, String niveauEtude, String secteur, Date date, int experience, int salaire, String lieu);
    
    public List<String> getAllVille();
    
    public void supprimerAnnonce();
    
    public int getNbAnnoncesPostees();
    
    public void miseAJourAdresse(Adresse s);
    
    public void miseAJourAnnonce(Annonce a);
    
    public void enregistrerAdresse(Adresse a);
    
    public CandidatureAnnonce getCandidatureAnnonce(long id);
    
    public CandidatureSpontanee getCandidatureSpontanee(long id);
     
    public void supprimerCandidatureSpontanee(CandidatureSpontanee cs,Employeur e);
}
