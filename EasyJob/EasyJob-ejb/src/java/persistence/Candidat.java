/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author celie
 */
@Entity
public class Candidat implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique=true, name = "mail")
    private String mail;
    @Column(name="mdp")
    private String mdp;
    
    private String nom;
    private String prenom;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date_naissance;
    private String telephone;
    private String numSecu;
    
    private boolean confidentialite;
    private String sitWeb;
    private String niveauEtudes;
    private String domaineEtudes;
    private List<String> langues;
    private List<String> experiences;
    private int nbExperiences;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Adresse adresse;
    
    @OneToMany
    private List<Entreprise> entreprises;
    
    @OneToMany(mappedBy = "destinataire", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<SuggestionAnnonce> suggestions;
    
    @OneToMany(mappedBy = "destinataire", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<NotificationCandidat> notifications;
    

    
    public Candidat() {
        this.entreprises = new ArrayList<Entreprise>();
        this.experiences = new ArrayList<String>();
        this.notifications = new ArrayList<NotificationCandidat>();
        this.suggestions = new ArrayList<SuggestionAnnonce>();
        this.langues = new ArrayList<String>();
    }
    
    public Candidat(String nom, String prenom, String tel, String secu, Date naissance, boolean confidentialite) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = tel;
        this.numSecu = secu;
        this.date_naissance = naissance;
        this.confidentialite = confidentialite;
        this.entreprises = new ArrayList<Entreprise>();
        this.experiences = new ArrayList<String>();
        this.notifications = new ArrayList<NotificationCandidat>();
        this.suggestions = new ArrayList<SuggestionAnnonce>();
        this.langues = new ArrayList<String>();
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Candidat)) {
            return false;
        }
        Candidat other = (Candidat) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistance.Candidat[ id=" + getId() + " ]";
    }

    /**
     * @return the mdp
     */
    public String getMdp() {
        return mdp;
    }

    /**
     * @param mdp the mdp to set
     */
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return the date_naissance
     */
    public Date getDate_naissance() {
        return date_naissance;
    }

    /**
     * @param date_naissance the date_naissance to set
     */
    public void setDate_naissance(Date date_naissance) {
        this.date_naissance = date_naissance;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return the numSecu
     */
    public String getNumSecu() {
        return numSecu;
    }

    /**
     * @param numSecu the numSecu to set
     */
    public void setNumSecu(String numSecu) {
        this.numSecu = numSecu;
    }

    /**
     * @return the confidentialite
     */
    public boolean isConfidentialite() {
        return confidentialite;
    }

    /**
     * @param confidentialite the confidentialite to set
     */
    public void setConfidentialite(boolean confidentialite) {
        this.confidentialite = confidentialite;
    }

    /**
     * @return the sitWeb
     */
    public String getSitWeb() {
        return sitWeb;
    }

    /**
     * @param sitWeb the sitWeb to set
     */
    public void setSitWeb(String sitWeb) {
        this.sitWeb = sitWeb;
    }

    /**
     * @return the niveauEtudes
     */
    public String getNiveauEtudes() {
        return niveauEtudes;
    }

    /**
     * @param niveauEtudes the niveauEtudes to set
     */
    public void setNiveauEtudes(String niveauEtudes) {
        this.niveauEtudes = niveauEtudes;
    }

    /**
     * @return the domaineEtudes
     */
    public String getDomaineEtudes() {
        return domaineEtudes;
    }

    /**
     * @param domaineEtudes the domaineEtudes to set
     */
    public void setDomaineEtudes(String domaineEtudes) {
        this.domaineEtudes = domaineEtudes;
    }

    /**
     * @return the langues
     */
    public List<String> getLangues() {
        return langues;
    }

    /**
     * @param langues the langues to set
     */
    public void setLangues(List<String> langues) {
        this.langues = langues;
    }

    /**
     * @return the experiences
     */
    public List<String> getExperiences() {
        return experiences;
    }

    /**
     * @param experiences the experiences to set
     */
    public void setExperiences(List<String> experiences) {
        this.experiences = experiences;
    }

    /**
     * @return the nbExperiences
     */
    public int getNbExperiences() {
        return nbExperiences;
    }

    /**
     * @param nbExperiences the nbExperiences to set
     */
    public void setNbExperiences(int nbExperiences) {
        this.nbExperiences = nbExperiences;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the adresse
     */
    public Adresse getAdresse() {
        return adresse;
    }

    /**
     * @param adresse the adresse to set
     */
    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    /**
     * @return the entreprises
     */
    public List<Entreprise> getEntreprises() {
        return entreprises;
    }

    /**
     * @param entreprises the entreprises to set
     */
    public void setEntreprises(List<Entreprise> entreprises) {
        this.entreprises = entreprises;
    }

    /**
     * @return the suggestions
     */
    public List<SuggestionAnnonce> getSuggestions() {
        return suggestions;
    }

    /**
     * @param suggestions the suggestions to set
     */
    public void setSuggestions(List<SuggestionAnnonce> suggestions) {
        this.suggestions = suggestions;
    }

    /**
     * @return the notifications
     */
    public List<NotificationCandidat> getNotifications() {
        return notifications;
    }

    /**
     * @param notifications the notifications to set
     */
    public void setNotifications(List<NotificationCandidat> notifications) {
        this.notifications = notifications;
    }

    /**
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }


    
}
