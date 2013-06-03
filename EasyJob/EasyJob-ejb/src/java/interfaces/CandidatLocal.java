/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.List;
import javax.ejb.Local;
import persistence.Annonce;
import persistence.Candidat;
import persistence.CandidatureAnnonce;
import persistence.CandidatureSpontanee;
import persistence.NotificationCandidat;
import persistence.SuggestionAnnonce;

/**
 *
 * @author celie
 */
@Local
public interface CandidatLocal {
    
    public Candidat getCandidatByMail(String mail); 
    
    public Candidat getCandidatById(int id);
    
    public boolean loginCandidat(String login, String mdp);
    
    public boolean existeMail(String mail);
    
    public void saveCandidat(Candidat candidat);
    
    public void removeCandidat(Candidat candidat);
    
    public void majCandidat(Candidat c);

    public List<Candidat> rechercheCandidat(String domaineEtudes, Integer nbExperiences, String niveauEtude, String ville, String pays, String nom, String prenom );

    public List<Candidat> rechercheCandidatByDomaine(String domaine);
    
    public List<String> getAllVille();
    
    public List<String> getAllPays();
    
    public List<String> getAllNomCandidat();
    
    public List<String> getAllPrenomCandidat();
    
    public void addNotification(Candidat candidat, NotificationCandidat notification);
    
    public void removeNtification(Candidat candidat, NotificationCandidat notification);
    
    public void addSuggestion(SuggestionAnnonce suggestion, Candidat candidat);
    
    public void removeSuggestion(SuggestionAnnonce suggestion, Candidat candidat);
    
    public List<Candidat> getCandidatByNom(String nom, String prenom);
    
    public int getNbCandidatsInscrits();
    
    public List<CandidatureAnnonce> getAnnonceParticipe(Candidat c);
    
    public List<CandidatureSpontanee> getCandidatureSpontanee(Candidat c);
    
    public boolean aSuggere(Candidat e, Annonce a, Candidat d);

}
