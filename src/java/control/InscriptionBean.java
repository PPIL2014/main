/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Utilisateur;

/**
 *
 * @author laurent84u
 */
@Named(value = "inscriptionBean")
@ManagedBean
@RequestScoped
public class InscriptionBean {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;


    @ManagedProperty(value="#{nom}")
    private String nom;
    @ManagedProperty(value="#{prenom}")
    private String prenom;
    @ManagedProperty(value="#{pseudo}")
    private String pseudo;
    @ManagedProperty(value="#{sexe}")
    private String sexe;
    @ManagedProperty(value="#{naissance}")
    private String naissance;
    @ManagedProperty(value="#{telephone}")
    private String telephone;
    @ManagedProperty(value="#{mdp}")
    private String mdp;
    @ManagedProperty(value="#{mdp2}")
    private String mdp2;
    @ManagedProperty(value="#{mail}")
    private String mail;
    @ManagedProperty(value="#{adresse}")
    private String adresse;
    
    /**
     * Creates a new instance of InscriptionBean
     */
    public InscriptionBean() {
    }
    
    
    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getSexe() {
        return sexe;
    }

    public String getNaissance() {
        return naissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getMdp() {
        return mdp;
    }
    
    public String getMdp2() {
        return mdp2;
    }

    public String getMail() {
        return mail;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setNaissance(String naissance) {
        this.naissance = naissance;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
    
    public void setMdp2(String mdp) {
        this.mdp2 = mdp;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    
    public boolean testPseudo() {
        boolean b = true;
        Query query = em.createQuery("SELECT u FROM Utilisateur u where u.pseudo=\"" + pseudo + "\"");
        try {
            //S'il existe déja un utilisateur ayant le pseudo indiqué, on le lui indique
           query.getSingleResult();
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur Pseudo : Le pseudo existe déjà, veuillez en choisir un autre !", "Erreur Pseudo : Le pseudo existe déjà, veuillez en choisir un autre !"));
            b = false;
        } catch (NoResultException e) {
            b = true;
        }
        return b;
    }
    
    
    public String inscrire() {
        if (mdp.compareTo(mdp2) != 0) return "inscription.xhtml";
        if (testPseudo())
        {
            Utilisateur u = new Utilisateur();
            u.setAdresse(adresse);
            u.setMail(mail);
            u.setNom(nom);
            u.setPrenom(prenom);
            u.setPseudo(pseudo);
            u.setSexe(sexe);
            u.setTelephone(telephone);
            u.setMdp(mdp);
            try {
                ut.begin();
                em.persist(u);
                ut.commit();
            } catch (NotSupportedException nse) {
                Logger.getLogger(InscriptionBean.class.getName()).log(Level.SEVERE, null, nse);
            } catch (SystemException ex) {
                Logger.getLogger(InscriptionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackException ex) {
                Logger.getLogger(InscriptionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(InscriptionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(InscriptionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(InscriptionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(InscriptionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Inscription terminée", "Inscription terminée"));
            return "index.xhtml";
        }
        else return "inscription.xhtml";
    }
}
