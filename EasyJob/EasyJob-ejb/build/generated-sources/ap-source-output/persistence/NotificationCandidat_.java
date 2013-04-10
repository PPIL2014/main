package persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import persistence.Annonce;
import persistence.Candidat;
import persistence.Entreprise;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-04-10T10:26:15")
@StaticMetamodel(NotificationCandidat.class)
public class NotificationCandidat_ { 

    public static volatile SingularAttribute<NotificationCandidat, Long> id;
    public static volatile SingularAttribute<NotificationCandidat, String> message;
    public static volatile SingularAttribute<NotificationCandidat, Candidat> destinataire;
    public static volatile SingularAttribute<NotificationCandidat, Entreprise> entreprise;
    public static volatile SingularAttribute<NotificationCandidat, Annonce> annonce;

}