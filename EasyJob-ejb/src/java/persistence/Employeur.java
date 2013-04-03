/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author celie
 */
@Entity
public class Employeur implements Serializable {
    
    private static long serialVersionUID = 1L;
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique=true)
    private String mail;
    private String mdp;
    
    private String nom;
    private String prenom;
    private String telephone;
    private boolean confidentialite;
    
    @OneToMany(mappedBy="employeur", cascade = CascadeType.ALL)
    private List<Annonce> annonces;
    
    @ManyToMany(mappedBy="employeurs", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CandidatureSpontanee> candidatures;
    
    @ManyToOne
    private Entreprise entreprise;
    
    @OneToMany(mappedBy="employeur")
    private List<SuiviCandidat> candidats;
    
    @OneToMany(mappedBy = "destinataire", cascade = CascadeType.ALL)
    private List<SuggestionCandidat> suggestions;
    
    @OneToMany(mappedBy = "destinataire", cascade = CascadeType.ALL)
    private List<NotificationEmployeur> notifications;
    
    @ManyToMany(mappedBy = "destinataires", cascade = CascadeType.ALL)
    private List<NotificationCandidatureS> notificationsCS;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employeur)) {
            return false;
        }
        Employeur other = (Employeur) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistance.Employeur[ id=" + getId() + " ]";
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
     * @return the annonces
     */
    public List<Annonce> getAnnonces() {
        return annonces;
    }

    /**
     * @param annonces the annonces to set
     */
    public void setAnnonces(List<Annonce> annonces) {
        this.annonces = annonces;
    }

    /**
     * @return the candidatures
     */
    public List<CandidatureSpontanee> getCandidatures() {
        return candidatures;
    }

    /**
     * @param candidatures the candidatures to set
     */
    public void setCandidatures(List<CandidatureSpontanee> candidatures) {
        this.candidatures = candidatures;
    }

    /**
     * @return the entreprise
     */
    public Entreprise getEntreprise() {
        return entreprise;
    }

    /**
     * @param entreprise the entreprise to set
     */
    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    /**
     * @return the candidats
     */
    public List<SuiviCandidat> getCandidats() {
        return candidats;
    }

    /**
     * @param candidats the candidats to set
     */
    public void setCandidats(List<SuiviCandidat> candidats) {
        this.candidats = candidats;
    }

    /**
     * @return the suggestions
     */
    public List<SuggestionCandidat> getSuggestions() {
        return suggestions;
    }

    /**
     * @param suggestions the suggestions to set
     */
    public void setSuggestions(List<SuggestionCandidat> suggestions) {
        this.suggestions = suggestions;
    }

    /**
     * @return the notifications
     */
    public List<NotificationEmployeur> getNotifications() {
        return notifications;
    }

    /**
     * @param notifications the notifications to set
     */
    public void setNotifications(List<NotificationEmployeur> notifications) {
        this.notifications = notifications;
    }

    /**
     * @return the notificationsCS
     */
    public List<NotificationCandidatureS> getNotificationsCS() {
        return notificationsCS;
    }

    /**
     * @param notificationsCS the notificationsCS to set
     */
    public void setNotificationsCS(List<NotificationCandidatureS> notificationsCS) {
        this.notificationsCS = notificationsCS;
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
