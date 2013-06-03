package persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import persistence.Annonce;
import persistence.Candidat;
import persistence.CandidatureSpontanee;
import persistence.Entreprise;
import persistence.NotificationCandidatureS;
import persistence.NotificationEmployeur;
import persistence.SuggestionCandidat;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-06-03T09:44:17")
@StaticMetamodel(Employeur.class)
public class Employeur_ { 

    public static volatile ListAttribute<Employeur, NotificationCandidatureS> notificationsCS;
    public static volatile SingularAttribute<Employeur, String> mail;
    public static volatile SingularAttribute<Employeur, Boolean> confidentialite;
    public static volatile ListAttribute<Employeur, SuggestionCandidat> suggestions;
    public static volatile SingularAttribute<Employeur, String> nom;
    public static volatile SingularAttribute<Employeur, Long> id;
    public static volatile SingularAttribute<Employeur, String> prenom;
    public static volatile SingularAttribute<Employeur, String> mdp;
    public static volatile ListAttribute<Employeur, NotificationEmployeur> notifications;
    public static volatile SingularAttribute<Employeur, Entreprise> entreprise;
    public static volatile ListAttribute<Employeur, Candidat> candidats;
    public static volatile ListAttribute<Employeur, Annonce> annonces;
    public static volatile SingularAttribute<Employeur, String> telephone;
    public static volatile ListAttribute<Employeur, CandidatureSpontanee> candidatures;

}