/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author celie
 */
@Entity
public class Annonce implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String titre;
    private String description;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateLimite;
    private String contrat;
    private String secteur;
    private int salaire;
    private String etudes;
    private int experience;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEmission;
    
    @OneToOne(cascade = CascadeType.MERGE)
    private Adresse lieu;
    
    @OneToMany(mappedBy="annonce", cascade = CascadeType.ALL)
    private List<CandidatureAnnonce> candidatures;
    
    @ManyToOne
    private Employeur employeur;
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Annonce)) {
            return false;
        }
        Annonce other = (Annonce) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistance.Annonce[ id=" + getId() + " ]";
    }

    /**
     * @return the titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * @param titre the titre to set
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the dateLimite
     */
    public Date getDateLimite() {
        return dateLimite;
    }

    /**
     * @param dateLimite the dateLimite to set
     */
    public void setDateLimite(Date dateLimite) {
        this.dateLimite = dateLimite;
    }

    /**
     * @return the contrat
     */
    public String getContrat() {
        return contrat;
    }

    /**
     * @param contrat the contrat to set
     */
    public void setContrat(String contrat) {
        this.contrat = contrat;
    }

    /**
     * @return the secteur
     */
    public String getSecteur() {
        return secteur;
    }

    /**
     * @param secteur the secteur to set
     */
    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    /**
     * @return the salaire
     */
    public int getSalaire() {
        return salaire;
    }

    /**
     * @param salaire the salaire to set
     */
    public void setSalaire(int salaire) {
        this.salaire = salaire;
    }

    /**
     * @return the etudes
     */
    public String getEtudes() {
        return etudes;
    }

    /**
     * @param etudes the etudes to set
     */
    public void setEtudes(String etudes) {
        this.etudes = etudes;
    }

    /**
     * @return the experience
     */
    public int getExperience() {
        return experience;
    }

    /**
     * @param experience the experience to set
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * @return the dateEmission
     */
    public Date getDateEmission() {
        return dateEmission;
    }

    /**
     * @param dateEmission the dateEmission to set
     */
    public void setDateEmission(Date dateEmission) {
        this.dateEmission = dateEmission;
    }

    /**
     * @return the lieu
     */
    public Adresse getLieu() {
        return lieu;
    }

    /**
     * @param lieu the lieu to set
     */
    public void setLieu(Adresse lieu) {
        this.lieu = lieu;
    }

    /**
     * @return the candidatures
     */
    public List<CandidatureAnnonce> getCandidatures() {
        return candidatures;
    }

    /**
     * @param candidatures the candidatures to set
     */
    public void setCandidatures(List<CandidatureAnnonce> candidatures) {
        this.candidatures = candidatures;
    }

    /**
     * @return the employeur
     */
    public Employeur getEmployeur() {
        return employeur;
    }

    /**
     * @param employeur the employeur to set
     */
    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }
    
}
