package persistence;

import java.io.File;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import persistence.Annonce;
import persistence.Candidat;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-06-03T20:49:00")
@StaticMetamodel(CandidatureAnnonce.class)
public class CandidatureAnnonce_ { 

    public static volatile SingularAttribute<CandidatureAnnonce, Long> id;
    public static volatile SingularAttribute<CandidatureAnnonce, Date> dateCreation;
    public static volatile SingularAttribute<CandidatureAnnonce, File> lettre;
    public static volatile SingularAttribute<CandidatureAnnonce, Candidat> candidat;
    public static volatile SingularAttribute<CandidatureAnnonce, Annonce> annonce;
    public static volatile SingularAttribute<CandidatureAnnonce, File> cv;

}