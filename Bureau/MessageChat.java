import java.util.Date;

public class MessageChat {

	private String contenu;

	private Date date;

	private SessionChat sessionChat;
	private Utilisateur expediteur;
	public String getContenu() {
		return contenu;
	}
	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public SessionChat getSessionChat() {
		return sessionChat;
	}
	public void setSessionChat(SessionChat sessionChat) {
		this.sessionChat = sessionChat;
	}
	public Utilisateur getExpediteur() {
		return expediteur;
	}
	public void setExpediteur(Utilisateur expediteur) {
		this.expediteur = expediteur;
	}

}