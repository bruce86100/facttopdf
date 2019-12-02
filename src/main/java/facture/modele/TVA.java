package facture.modele;

public class TVA {
	private int id;
	private String libelle;
	private Float taux;
	
	public Float getTaux() {
		return taux;
	}
	public void setTaux(Float taux) {
		this.taux = taux;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
