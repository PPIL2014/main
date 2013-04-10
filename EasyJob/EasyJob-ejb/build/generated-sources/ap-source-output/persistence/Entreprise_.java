package persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import persistence.Adresse;
import persistence.Employeur;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-04-10T17:30:47")
@StaticMetamodel(Entreprise.class)
public class Entreprise_ { 

    public static volatile SingularAttribute<Entreprise, Long> id;
    public static volatile SingularAttribute<Entreprise, Integer> nbEmployes;
    public static volatile SingularAttribute<Entreprise, String> statut;
    public static volatile SingularAttribute<Entreprise, Adresse> adresse;
    public static volatile SingularAttribute<Entreprise, String> description;
    public static volatile SingularAttribute<Entreprise, String> domaine;
    public static volatile SingularAttribute<Entreprise, String> siteWeb;
    public static volatile SingularAttribute<Entreprise, String> nomEntreprise;
    public static volatile SingularAttribute<Entreprise, String> telephone;
    public static volatile ListAttribute<Entreprise, Employeur> employeurs;

}