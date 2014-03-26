import java.util.ArrayList;

public abstract class Question {

	private String question;

	/**
	 * 
	 * @element-type Questionnaire
	 */
	private ArrayList<Questionnaire>  questionnaires;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public ArrayList<Questionnaire> getQuestionnaires() {
		return questionnaires;
	}

	public void setQuestionnaires(ArrayList<Questionnaire> questionnaires) {
		this.questionnaires = questionnaires;
	}
	
}