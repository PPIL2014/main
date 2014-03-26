import java.util.ArrayList;

public class Questionnaire {

	private String nom;

	/**
	 * 
	 * @element-type Question
	 */
	private ArrayList<Question>  questions;

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}

	public void ajouterQuestion(Question question) {
	}

	public void retirerQuestion(Question question) {
	}

}