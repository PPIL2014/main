package persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import persistence.Candidat;
import persistence.Employeur;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-04-12T22:50:40")
@StaticMetamodel(CandidatureSpontanee.class)
public class CandidatureSpontanee_ { 

    public static volatile SingularAttribute<CandidatureSpontanee, Long> id;
    public static volatile SingularAttribute<CandidatureSpontanee, Candidat> candidat;
    public static volatile ListAttribute<CandidatureSpontanee, Employeur> employeurs;

}