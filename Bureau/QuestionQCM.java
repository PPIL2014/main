import java.util.ArrayList;

public class QuestionQCM extends Question {

	private Integer nombreChoixMax;

	private String type;

	/**
	 * 
	 * @element-type Choix
	 */
	private ArrayList<Choix> choix;

	public Integer getNombreChoixMax() {
		return nombreChoixMax;
	}

	public void setNombreChoixMax(Integer nombreChoixMax) {
		this.nombreChoixMax = nombreChoixMax;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<Choix> getChoix() {
		return choix;
	}

	public void setChoix(ArrayList<Choix> choix) {
		this.choix = choix;
	}

}