import java.util.Date;
import java.util.ArrayList;

public class Utilisateur {

	private String pseudo;

	private String nom;

	private String prenom;

	private Date ddn;

	private String sexe;

	private String adresse;

	private String mail;

	private String telephone;

	private Boolean estDesinscrit;

	private Boolean estBloque;

	private Boolean estSupprime;

	/**
	 * 
	 * @element-type Galerie
	 */
	private ArrayList<Galerie>  galeries;
	/**
	 * 
	 * @element-type Utilisateur
	 */
	private ArrayList<Contact>  contacts;
	/**
	 * 
	 * @element-type Conversation
	 */
	private ArrayList<Conversation>  conversations;
	/**
	 * 
	 * @element-type ReponseQCM
	 */
	private ArrayList<ReponseQCM>  reponsesQCM;
	/**
	 * 
	 * @element-type ReponseOuverte
	 */
	private ArrayList<ReponseOuverte>  reponsesOuvertes;
	/**
	 * 
	 * @element-type SessionChat
	 */
	private ArrayList<SessionChat>  sessionsChat;
	/**
	 * 
	 * @element-type Contact
	 */
	private Image avatar;
	private Session session;
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
	public ArrayList<Galerie> getGaleries() {
		return galeries;
	}
	public void setGaleries(ArrayList<Galerie> galeries) {
		this.galeries = galeries;
	}
	public ArrayList<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(ArrayList<Contact> contacts) {
		this.contacts = contacts;
	}
	public ArrayList<Conversation> getConversations() {
		return conversations;
	}
	public void setConversations(ArrayList<Conversation> conversations) {
		this.conversations = conversations;
	}
	public ArrayList<ReponseQCM> getReponsesQCM() {
		return reponsesQCM;
	}
	public void setReponsesQCM(ArrayList<ReponseQCM> reponsesQCM) {
		this.reponsesQCM = reponsesQCM;
	}
	public ArrayList<ReponseOuverte> getReponsesOuvertes() {
		return reponsesOuvertes;
	}
	public void setReponsesOuvertes(ArrayList<ReponseOuverte> reponsesOuvertes) {
		this.reponsesOuvertes = reponsesOuvertes;
	}
	public ArrayList<SessionChat> getSessionsChat() {
		return sessionsChat;
	}
	public void setSessionsChat(ArrayList<SessionChat> sessionsChat) {
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
	
	
}