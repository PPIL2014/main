
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

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
    private Boolean administrateur = false;
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
     * @element-type SignalementUtilisateur
     */
    @OneToMany
    private Collection<SignalementUtilisateur>  signalementsUtilisateur = new ArrayList<SignalementUtilisateur>();
    
    /**
     * 
     * @element-type Contact
     */
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Contact>  contacts  = new ArrayList<Contact>();
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
    private Collection<ReponseQCM>  reponsesQCM = new ArrayList<ReponseQCM>();
    /**
     * 
     * @element-type ReponseOuverte
     */
    @OneToMany
    private Collection<ReponseOuverte>  reponsesOuvertes = new ArrayList<ReponseOuverte>();;
    /**
     * 
     * @element-type SessionChat
     */
    
    @OneToMany
    private List<SessionChat>  sessionsChat;

    
    /**
     * 
     * @element-type Contact
     */
    @OneToOne(cascade=CascadeType.ALL)
    private Image avatar;
   
    /**
     * 
     * @element-type MyBadge
     */
    @OneToMany(cascade=CascadeType.ALL)
    private Collection<MyBadge>  badges;
    
    public Utilisateur() {
        
    }
    
    public void addGalerie(Galerie g){
        this.galeries.add(g);
    }
    
    public List<Utilisateur> getContactsByName(String pieceOfName) {
        ArrayList<Utilisateur> contactsByName = new ArrayList<Utilisateur>();
        for(Conversation c : conversations) {  
            if(c.getDestinataire().getPseudo().toLowerCase().startsWith(pieceOfName.toLowerCase()))  
                contactsByName.add(c.getDestinataire());
        }
        return contactsByName;
    }
    
    public Conversation getConv(Utilisateur dest) {
        if(dest == null)
            return null ;
        for (Conversation conv : dest.getConversations())
            if(conv.getExpediteur().getPseudo().equals(pseudo) || conv.getDestinataire().getPseudo().equals(pseudo))
                return conv ;
        return null ;
    }
	
	/*
	pour le moment les 4 fonction suivante ne servent pas mais il faut les garder !
	*/
	public Contact.Type getTypeContact(String pseudo){
        for(Contact c : this.contacts){
            if(c.getEstEnContactAvec().getPseudo() == pseudo){
                return c.getType();
            }
        }
        return null;
    }
    
    public void confirmerAmitie(String pseudo){
        for(Contact c : this.contacts){
            if(c.getEstEnContactAvec().getPseudo() == pseudo){
                c.setType(Contact.Type.AMI);
            }
        }
    }
    
    public void refuserAmitie(String pseudo){
        for(Contact c : this.contacts){
            if(c.getEstEnContactAvec().getPseudo() == pseudo){
                c.setType(Contact.Type.REFUSE);
            }
        }
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
	public Boolean getAdministrateur() {
        return administrateur;
    }
    public void setAdministrateur(Boolean administrateur) {
        this.administrateur = administrateur;
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
	public Collection<SignalementUtilisateur> getSignalementsUtilisateur() {
        return signalementsUtilisateur;
    }
    public void setSignalementsUtilisateur(Collection<SignalementUtilisateur> signalementsUtilisateur) {
        this.signalementsUtilisateur = signalementsUtilisateur;
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

    public List<SessionChat> getSessionsChat() {
        return sessionsChat;
    }
    public void setSessionsChat(List<SessionChat> sessionsChat) {
        this.sessionsChat = sessionsChat;
    }
    public Image getAvatar() {
        return avatar;
    }
    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public String getMdp() {
        return this.mdp;
    }
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public Collection<MyBadge> getBadges() {
        return badges;
    }

    public void setBadges(Collection<MyBadge> badges) {
        this.badges = badges;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pseudo != null ? pseudo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "model.entity.Utilisateur[ id=" + pseudo + " ]";
    }

    public void closeAllChat() {
        for (SessionChat c : sessionsChat) {
            c.setEstDemarree(false);
        }
    }

    public SessionChat recupererChat(Utilisateur u2) {
        for (SessionChat c : sessionsChat) {
            if (c.getUtilisateur1().equals(u2) || c.getUtilisateur2().equals(u2))
                return c ;
        }
        return null ;
    }
    
    public void ajouterChat(SessionChat c) {
        sessionsChat.add(c);
    }

    public SessionChat getSessionChatDemarree() {        
        for (SessionChat c : sessionsChat) {
            if (c.getEstDemarree())
                return c ;
        }
        return null ;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Utilisateur other = (Utilisateur) obj;
        if (!Objects.equals(this.pseudo, other.pseudo)) {
            return false;
        }
        return true;
    }
    
    public void ajouterReponseQCM(ReponseQCM rep){
	this.reponsesQCM.size();
        if(rep.getId() == null){
            this.reponsesQCM.add(rep);
        }else{
            for(ReponseQCM repq : this.reponsesQCM){
                if(repq.getId() == rep.getId()){
                    repq = rep;
                }
            }
        }
    }
        
    public void ajouterReponseOuverte(ReponseOuverte rep){
	this.reponsesOuvertes.size();
        if(rep.getId() == null){
		this.reponsesOuvertes.add(rep);
        }for(ReponseOuverte repo : this.reponsesOuvertes){
            if(repo.getId() == rep.getId()){
                repo = rep;
            }
        }
    }
            
    public boolean estAmisAvec (Utilisateur u) {
        for (Contact c : contacts){
            if ((c.getEstEnContactAvec().equals(u)) && (c.getType().equals(Contact.Type.AMI) || c.getType().equals(Contact.Type.FAVORI))) {
                return true ;
            }
        }
        return false ;
    }    

    public void retirerReponse(ReponseQCM r) {
        Iterator<ReponseQCM> iter = this.getReponsesQCM().iterator();
        while (iter.hasNext()) {
            if (iter.next().getId() == r.getId()) {
                iter.remove();
            }
        }
    }

    public void retirerReponse(ReponseOuverte r) {
        Iterator<ReponseOuverte> iter = this.getReponsesOuvertes().iterator();
        while (iter.hasNext()) {
            if (iter.next().getId() == r.getId()) {
                iter.remove();
            }
        }

    }

}
