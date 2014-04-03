/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author mac
 */
@Entity

public class Utilisateur implements Serializable {
    @Id
    private String pseudo;
    private String nom;
    private String prenom;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date ddn;
    private String sexe;
    private String adresse;
    private String mail;
    private String telephone;
    private String mdp;
    private Boolean estDesinscrit;
    private Boolean estBloque;
    private Boolean estSupprime;

    /**
     * 
     * @element-type Galerie
     */
    @OneToMany
    private Collection<Galerie>  galeries;
    /**
     * 
     * @element-type Utilisateur
     */
    @OneToMany
    private Collection<Contact>  contacts;
    /**
     * 
     * @element-type Conversation
     */
    @OneToMany
    private Collection<Conversation>  conversations;
    /**
     * 
     * @element-type ReponseQCM
     */
    @OneToMany
    private Collection<ReponseQCM>  reponsesQCM;
    /**
     * 
     * @element-type ReponseOuverte
     */
    @OneToMany
    private Collection<ReponseOuverte>  reponsesOuvertes;
    /**
     * 
     * @element-type SessionChat
     */
    @OneToMany
    private Collection<SessionChat>  sessionsChat;
    /**
     * 
     * @element-type Contact
     */
    @OneToOne
    private Image avatar;
    @OneToOne
    private Session session;
    
    public Utilisateur() {
        
    }
    
    public String getPseudo() {
        return pseudo;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    public String getNom() {
            return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public Date getDdn() {
        return ddn;
    }
    public void setDdn(Date ddn) {
        this.ddn = ddn;
    }
    public String getSexe() {
        return sexe;
    }
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }
    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public Boolean getEstDesinscrit() {
        return estDesinscrit;
    }
    public void setEstDesinscrit(Boolean estDesinscrit) {
        this.estDesinscrit = estDesinscrit;
    }
    public Boolean getEstBloque() {
        return estBloque;
    }
    public void setEstBloque(Boolean estBloque) {
        this.estBloque = estBloque;
    }
    public Boolean getEstSupprime() {
        return estSupprime;
    }
    public void setEstSupprime(Boolean estSupprime) {
        this.estSupprime = estSupprime;
    }
    public Collection<Galerie> getGaleries() {
        return galeries;
    }
    public void setGaleries(Collection<Galerie> galeries) {
        this.galeries = galeries;
    }
    public Collection<Contact> getContacts() {
        return contacts;
    }
    public void setContacts(Collection<Contact> contacts) {
        this.contacts = contacts;
    }
    public Collection<Conversation> getConversations() {
        return conversations;
    }
    public void setConversations(Collection<Conversation> conversations) {
        this.conversations = conversations;
    }
    public Collection<ReponseQCM> getReponsesQCM() {
        return reponsesQCM;
    }
    public void setReponsesQCM(Collection<ReponseQCM> reponsesQCM) {
        this.reponsesQCM = reponsesQCM;
    }
    public Collection<ReponseOuverte> getReponsesOuvertes() {
        return reponsesOuvertes;
    }
    public void setReponsesOuvertes(Collection<ReponseOuverte> reponsesOuvertes) {
        this.reponsesOuvertes = reponsesOuvertes;
    }
    public Collection<SessionChat> getSessionsChat() {
        return sessionsChat;
    }
    public void setSessionsChat(Collection<SessionChat> sessionsChat) {
        this.sessionsChat = sessionsChat;
    }
    public Image getAvatar() {
        return avatar;
    }
    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }
    public Session getSession() {
        return session;
    }
    public void setSession(Session session) {
        this.session = session;
    }
    public void setMdp(String mdp) {
        //this.mdp = sha1(mdp);
        this.mdp = mdp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pseudo != null ? pseudo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur) object;
        if ((this.pseudo == null && other.pseudo != null) || (this.pseudo != null && !this.pseudo.equals(other.pseudo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.entity.Utilisateur[ id=" + pseudo + " ]";
    }

    public Chat getChat() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setChat(Chat c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
    
}
