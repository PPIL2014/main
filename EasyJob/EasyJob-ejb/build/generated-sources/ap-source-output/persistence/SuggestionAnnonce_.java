package persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import persistence.Annonce;
import persistence.Candidat;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-06-03T09:36:42")
@StaticMetamodel(SuggestionAnnonce.class)
public class SuggestionAnnonce_ { 

    public static volatile SingularAttribute<SuggestionAnnonce, Long> id;
    public static volatile SingularAttribute<SuggestionAnnonce, Candidat> destinataire;
    public static volatile SingularAttribute<SuggestionAnnonce, Date> dateCreation;
    public static volatile SingularAttribute<SuggestionAnnonce, Annonce> annonce;
    public static volatile SingularAttribute<SuggestionAnnonce, Candidat> emetteur;

}