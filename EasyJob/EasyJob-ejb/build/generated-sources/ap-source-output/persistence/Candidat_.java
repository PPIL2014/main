package persistence;

import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import persistence.Adresse;
import persistence.Entreprise;
import persistence.NotificationCandidat;
import persistence.SuggestionAnnonce;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-04-12T22:50:40")
@StaticMetamodel(Candidat.class)
public class Candidat_ { 

    public static volatile SingularAttribute<Candidat, String> mail;
    public static volatile SingularAttribute<Candidat, Boolean> confidentialite;
    public static volatile SingularAttribute<Candidat, List> experiences;
    public static volatile SingularAttribute<Candidat, Adresse> adresse;
    public static volatile ListAttribute<Candidat, Entreprise> entreprises;
    public static volatile SingularAttribute<Candidat, List> langues;
    public static volatile SingularAttribute<Candidat, Integer> nbExperiences;
    public static volatile ListAttribute<Candidat, SuggestionAnnonce> suggestions;
    public static volatile SingularAttribute<Candidat, String> nom;
    public static volatile SingularAttribute<Candidat, String> id;
    public static volatile SingularAttribute<Candidat, String> prenom;
    public static volatile SingularAttribute<Candidat, String> sitWeb;
    public static volatile SingularAttribute<Candidat, String> mdp;
    public static volatile SingularAttribute<Candidat, String> numSecu;
    public static volatile ListAttribute<Candidat, NotificationCandidat> notifications;
    public static volatile SingularAttribute<Candidat, String> niveauEtudes;
    public static volatile SingularAttribute<Candidat, Date> date_naissance;
    public static volatile SingularAttribute<Candidat, String> domaineEtudes;
    public static volatile SingularAttribute<Candidat, String> telephone;

}