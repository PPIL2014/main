import java.util.Date;

public class Image {

	private String nom;

	private Date date;

	private String description;

	private Boolean signalee;

	private Boolean estRetiree;

	private Galerie galerie;

	public Boolean signaler() {
		return null;
	}

	public Boolean retirer() {
		return null;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
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

	public Boolean getSignalee() {
		return signalee;
	}

	public void setSignalee(Boolean signalee) {
		this.signalee = signalee;
	}

	public Boolean getEstRetiree() {
		return estRetiree;
	}

	public void setEstRetiree(Boolean estRetiree) {
		this.estRetiree = estRetiree;
	}

	public Galerie getGalerie() {
		return galerie;
	}

	public void setGalerie(Galerie galerie) {
		this.galerie = galerie;
	}

}