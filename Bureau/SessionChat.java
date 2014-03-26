import java.util.ArrayList;

public class SessionChat {

	private Boolean estDemarree;

	private Utilisateur utilisateur1;
	private Utilisateur utilisateur2;
	/**
	 * 
	 * @element-type MessageChat
	 */
	private ArrayList<MessageChat>  messages;

	public Boolean getEstDemarree() {
		return estDemarree;
	}

	public void setEstDemarree(Boolean estDemarree) {
		this.estDemarree = estDemarree;
	}

	public Utilisateur getUtilisateur1() {
		return utilisateur1;
	}

	public void setUtilisateur1(Utilisateur utilisateur1) {
		this.utilisateur1 = utilisateur1;
	}

	public Utilisateur getUtilisateur2() {
		return utilisateur2;
	}

	public void setUtilisateur2(Utilisateur utilisateur2) {
		this.utilisateur2 = utilisateur2;
	}

	public ArrayList<MessageChat> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<MessageChat> messages) {
		this.messages = messages;
	}

	public Boolean demarrerSessionChat() {
		return null;
	}

	public Boolean terminerSessionChat() {
		return null;
	}

}