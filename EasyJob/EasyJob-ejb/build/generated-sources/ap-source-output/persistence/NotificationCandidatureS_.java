package persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import persistence.CandidatureSpontanee;
import persistence.Employeur;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-04-09T18:50:38")
@StaticMetamodel(NotificationCandidatureS.class)
public class NotificationCandidatureS_ { 

    public static volatile SingularAttribute<NotificationCandidatureS, Long> id;
    public static volatile SingularAttribute<NotificationCandidatureS, CandidatureSpontanee> candidature;
    public static volatile ListAttribute<NotificationCandidatureS, Employeur> destinataires;

}