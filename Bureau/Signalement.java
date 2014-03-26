import java.util.Date;

public abstract class Signalement {

	private Date date;

	private String description;

	private Boolean estTraitee;

	private Boolean estEnCoursDeTraitement;

	private Boolean estEnAttente;

	private Utilisateur emetteur;

	public Boolean enCoursDeTraitement() {
		return null;
	}

	public Boolean traitee() {
		return null;
	}

	public Boolean enAttente() {
		return null;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEstTraitee() {
		return estTraitee;
	}

	public void setEstTraitee(Boolean estTraitee) {
		this.estTraitee = estTraitee;
	}

	public Boolean getEstEnCoursDeTraitement() {
		return estEnCoursDeTraitement;
	}

	public void setEstEnCoursDeTraitement(Boolean estEnCoursDeTraitement) {
		this.estEnCoursDeTraitement = estEnCoursDeTraitement;
	}

	public Boolean getEstEnAttente() {
		return estEnAttente;
	}

	public void setEstEnAttente(Boolean estEnAttente) {
		this.estEnAttente = estEnAttente;
	}

	public Utilisateur getEmetteur() {
		return emetteur;
	}

	public void setEmetteur(Utilisateur emetteur) {
		this.emetteur = emetteur;
	}
	
}