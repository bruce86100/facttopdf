package facture.modele;

//Class qui contient les trigrammes qui servent d'En-tête du fichier CSV et serviront de contrôle
public enum EnumDonnee  {
	client("CLI"),
	produit("PDT"),
	commentaire("COM");

	private String code = "";

	//Constructeur
	EnumDonnee(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	@Override
	public String toString(){
	    return code;
	  }
}
