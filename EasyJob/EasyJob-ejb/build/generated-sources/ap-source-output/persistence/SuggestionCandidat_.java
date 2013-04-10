package persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import persistence.Candidat;
import persistence.Employeur;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-04-10T13:41:58")
@StaticMetamodel(SuggestionCandidat.class)
public class SuggestionCandidat_ { 

    public static volatile SingularAttribute<SuggestionCandidat, Long> id;
    public static volatile SingularAttribute<SuggestionCandidat, Employeur> destinataire;
    public static volatile SingularAttribute<SuggestionCandidat, Candidat> candidat;
    public static volatile SingularAttribute<SuggestionCandidat, Employeur> emetteur;

}