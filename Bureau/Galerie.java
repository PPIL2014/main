import java.util.ArrayList;

public class Galerie {

	private String nom;

	private Integer visibilite;

	private Utilisateur proprietaire;
	/**
	 * 
	 * @element-type Image
	 */
	private ArrayList<Image>  images;

	public void ajouterImage(Image image) {
	}

	public void retirerImage(Image image) {
	}

	public Boolean rendreVisible() {
		return null;
	}

	public Boolean rendreInivisible() {
		return null;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Integer getVisibilite() {
		return visibilite;
	}

	public void setVisibilite(Integer visibilite) {
		this.visibilite = visibilite;
	}

	public Utilisateur getProprietaire() {
		return proprietaire;
	}

	public void setProprietaire(Utilisateur proprietaire) {
		this.proprietaire = proprietaire;
	}

	public ArrayList<Image> getImages() {
		return images;
	}

	public void setImages(ArrayList<Image> images) {
		this.images = images;
	}

}