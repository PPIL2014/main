/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.List;
import javax.ejb.Local;
import persistence.Annonce;
import persistence.Candidat;
import persistence.Employeur;
import persistence.Entreprise;
import persistence.NotificationCandidatureS;
import persistence.NotificationEmployeur;
import persistence.SuggestionCandidat;

/**
 *
 * @author celie
 */
@Local
public interface EmployeurLocal {

    public Employeur getEmployeurByMail(String mail); 
    
    public List<Employeur> getEmployeurByNom(String nom, String prenom);
    
    public boolean loginEmployeur(String login, String mdp);
    
    public boolean existeMail(String mail);
    
    public void majEmployeur(Employeur emp);
    
    public void saveEmployeur(Employeur emp);

    public void removeEmployeur(Employeur employeur);
    
    public void change(Employeur employeur);
    
    public void removeEmployeur(Entreprise ent, Employeur emp);
    
    public void removeAnnonce(Annonce annonce, Employeur employeur);
    
    public void addNotificationEmp(Employeur emp, NotificationEmployeur notification);
   
    public void removeNotificationEmp(Employeur emp, NotificationEmployeur notification);
    
    public void addSuggestionCandidat(SuggestionCandidat sc, Employeur emp);
    
    public void removeSuggestionCandidat(SuggestionCandidat sc, Employeur emmp);
    
    public List<SuggestionCandidat> getListeSuggestionC(Employeur e);

    public List<Employeur> rechercheEmployeur(int id);
    
    public int getNbEmployeursInscrits();
    
    public void addNotificationCandidatureSpontanee(NotificationCandidatureS notif, Employeur emp);
    
    public void removeNotificationCandidatureSpontanee(NotificationCandidatureS notif, Employeur emp);
    
    public List<Annonce> listeAnnonceCree(Employeur e);

    public void remove(Entreprise e);
    
    public boolean aSuggere(Employeur e, Candidat c, Employeur d);
        
}
