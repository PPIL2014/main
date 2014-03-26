import java.util.ArrayList;

public class Conversation {

	private Utilisateur destinataire;
	private Utilisateur expediteur;
	/**
	 * 
	 * @element-type MessageConversation
	 */
	private ArrayList<MessageConversation>  messages;
	public Utilisateur getDestinataire() {
		return destinataire;
	}
	public void setDestinataire(Utilisateur destinataire) {
		this.destinataire = destinataire;
	}
	public Utilisateur getExpediteur() {
		return expediteur;
	}
	public void setExpediteur(Utilisateur expediteur) {
		this.expediteur = expediteur;
	}
	public ArrayList<MessageConversation> getMessages() {
		return messages;
	}
	public void setMessages(ArrayList<MessageConversation> messages) {
		this.messages = messages;
	}

}