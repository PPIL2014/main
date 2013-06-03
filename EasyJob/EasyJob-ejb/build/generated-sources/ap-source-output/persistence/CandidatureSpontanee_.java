package persistence;

import java.io.File;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import persistence.Candidat;
import persistence.Employeur;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-06-03T09:36:42")
@StaticMetamodel(CandidatureSpontanee.class)
public class CandidatureSpontanee_ { 

    public static volatile SingularAttribute<CandidatureSpontanee, Long> id;
    public static volatile SingularAttribute<CandidatureSpontanee, Date> dateCreation;
    public static volatile SingularAttribute<CandidatureSpontanee, File> lettre;
    public static volatile SingularAttribute<CandidatureSpontanee, Candidat> candidat;
    public static volatile SingularAttribute<CandidatureSpontanee, File> cv;
    public static volatile ListAttribute<CandidatureSpontanee, Employeur> employeurs;

}