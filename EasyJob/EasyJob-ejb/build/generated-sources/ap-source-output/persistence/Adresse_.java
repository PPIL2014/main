package persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-06-03T20:49:00")
@StaticMetamodel(Adresse.class)
public class Adresse_ { 

    public static volatile SingularAttribute<Adresse, Long> id;
    public static volatile SingularAttribute<Adresse, String> rue;
    public static volatile SingularAttribute<Adresse, String> codePostal;
    public static volatile SingularAttribute<Adresse, String> ville;
    public static volatile SingularAttribute<Adresse, String> pays;

}