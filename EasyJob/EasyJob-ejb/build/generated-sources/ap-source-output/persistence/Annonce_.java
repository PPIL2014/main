package persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import persistence.Adresse;
import persistence.CandidatureAnnonce;
import persistence.Employeur;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-04-10T10:26:15")
@StaticMetamodel(Annonce.class)
public class Annonce_ { 

    public static volatile SingularAttribute<Annonce, Adresse> lieu;
    public static volatile SingularAttribute<Annonce, Date> dateLimite;
    public static volatile SingularAttribute<Annonce, String> contrat;
    public static volatile SingularAttribute<Annonce, String> etudes;
    public static volatile SingularAttribute<Annonce, String> titre;
    public static volatile SingularAttribute<Annonce, String> secteur;
    public static volatile SingularAttribute<Annonce, Long> id;
    public static volatile SingularAttribute<Annonce, String> description;
    public static volatile SingularAttribute<Annonce, Integer> salaire;
    public static volatile SingularAttribute<Annonce, Integer> experience;
    public static volatile SingularAttribute<Annonce, Employeur> employeur;
    public static volatile ListAttribute<Annonce, CandidatureAnnonce> candidatures;
    public static volatile SingularAttribute<Annonce, Date> dateEmission;

}