package persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import persistence.Candidat;
import persistence.CandidatureAnnonce;
import persistence.Employeur;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-04-09T18:50:38")
@StaticMetamodel(NotificationEmployeur.class)
public class NotificationEmployeur_ { 

    public static volatile SingularAttribute<NotificationEmployeur, Long> id;
    public static volatile SingularAttribute<NotificationEmployeur, String> message;
    public static volatile SingularAttribute<NotificationEmployeur, Employeur> destinataire;
    public static volatile SingularAttribute<NotificationEmployeur, Candidat> candidat;
    public static volatile SingularAttribute<NotificationEmployeur, CandidatureAnnonce> candidature;

}