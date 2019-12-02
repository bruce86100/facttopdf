package facture.service;

import java.io.BufferedReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import facture.modele.EnumDonnee;
import facture.exception.FormatCSVException;

@Stateless
public class VerifCSV {

	// Méthode globale qui vérifie le format du CSV (données non nulles, trigrammes d'en-tête, nombre de lignes)
	// Jette un erreur FormatCSVException personnalisée si pas OK 
	public List<String> verificationCSV (BufferedReader br, String separateur) throws FormatCSVException, IOException {

		List<String> result = new ArrayList<>();
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			result.add(line);
		}

		// Vérification du nombre de lignes
		verifierLigneCsv(result, EnumDonnee.client, EnumDonnee.produit, EnumDonnee.commentaire);

		// Fait appel à la méthode de vérification adaptée selon le trigramme d'en-tête
		for(int i=0; i<result.size(); i++) {
			String cellule[] = result.get(i).split(separateur);
			validationEnTETE(cellule[0]);
			if (cellule[0].equals(EnumDonnee.client.toString())) {
				validationLigneClient(cellule);
			} else if (cellule[0].equals(EnumDonnee.produit.toString())) {
				validationLigneProduit(cellule);
			} else if (cellule[0].equals(EnumDonnee.commentaire.toString())) {
				validationLigneCommentaire(cellule);
			}
		}

		return result;
	}


	//Méthode de vérification des trigrammes d'en-tête "règlementaire" personalisé dans Enumération Donnée
	public void validationEnTETE(String en_tete) throws FormatCSVException {
		if ( en_tete.equals("")) {
			throw new FormatCSVException("L'en-tête ne doit pas être vide");
		}
		if ( en_tete.length() != 3) {
			throw new FormatCSVException( "L'en-tête doit être un composé de 3 caractères." );
		}
		if (!((en_tete.equals(EnumDonnee.client.toString())) || (en_tete.equals(EnumDonnee.produit.toString())) || (en_tete.equals(EnumDonnee.commentaire.toString())))) {
			throw new FormatCSVException( "L'en_tête ne correspond pas à un trigramme toléré :  'CLI', 'PDT' ou 'COM' " );
		}

	}

	//Méthode de vérification spécifique à la ligne CLient
	public void validationLigneClient(String donneeClient[]) throws FormatCSVException {
		String regex = "\\p{L}*(-\\p{L}*)*";
		
		if ( donneeClient.length != 7) {
			throw new FormatCSVException("Un des 6 champs nécessaires pour la ligne client n'est pas renseigné. Rappel du format : Nom; Prenom; adresse1; adresse 2; CP; ville ");
		}
		if (!donneeClient[5].matches("\\d{5}")) {
			throw new FormatCSVException( "Le format du code postal n'est pas bon." );
		} 
		if ( donneeClient[1].equals("") || donneeClient[2].equals("")  || donneeClient[3].equals("")  || donneeClient[5].equals("")  || donneeClient[6].equals("") ) {
			throw new FormatCSVException( "Un des champs autre qu'adresse2 n'est pas renseigné." );
		}
		if (!donneeClient[1].matches(regex) || !donneeClient[2].matches(regex) || !donneeClient[6].matches(regex)) {
			throw new FormatCSVException( "Le format pour le nom et/ou le prénom et/ou la ville n'est pas valide" );
		}
	}

	//Méthode de vérification spécifique à la ligne Produit
	public void validationLigneProduit (String donneeProduit[]) throws FormatCSVException {
		if ( donneeProduit.length != 5 ) {
			throw new FormatCSVException("Un des 4 champs nécessaires pour la ligne produit n'est pas renseigné. Rappel du format : Référence; Désignation; Quantité; Prix HT");
		}
		if ( donneeProduit[1].equals("") || donneeProduit[2].equals("") || donneeProduit[3].equals("") || donneeProduit[4].equals("")) {
			throw new FormatCSVException( "Un des champs n'est pas renseigné." );
		}
		if (!(donneeProduit[3].replaceAll(" ", "").matches("\\d{1,}"))) {
			throw new FormatCSVException( "Le champ quantité ne peut être composé que de chiffres entiers positifs." );
		}
		if (!(donneeProduit[4].replaceAll(" ", "").matches("^(\\d*)([,.]?(\\d*))*$"))) {
			throw new FormatCSVException( "Le champ prix ne peut être composé que de chiffres décimaux positifs." );
		} 
	}

	//Méthode de vérification spécifique à la ligne Commentaire
	public void validationLigneCommentaire (String donneeCommentaire[]) throws FormatCSVException {
		if ( donneeCommentaire.length != 2) {
			throw new FormatCSVException("Un seul champ (non vide) est nécessaire pour la ligne commentaire");
		}
		if ( donneeCommentaire[1].length() > 250) {
			throw new FormatCSVException( "Le commentaire ne peut dépasser 250 caractères." );
		}
	}

	// Méthode générique qui vérifie le nombre de ligne selon le trigramme en paramètre
	public void verifierLigneCsv(List<String> result, EnumDonnee ...e) throws FormatCSVException	{

		//Compteur du nombre de lignes coorespondant au trigramme en paramètre
		for(int i=0; i < e.length; i++) {

			int nb = 0;

			String type = e[i].toString();

			//Compteur du nombre de lignes coorespondant au trigramme en paramètre
			for(String line : result ) {
				if(line.contains(type))
					nb++; 
			}

			// Jette une exception avec message personnalisé si nombre de lignes pas OK selon le trigramme en paramètre
			if(e[i] == EnumDonnee.client && nb != 1) {
				throw new FormatCSVException("le fichier doit comporter exactement une et une seule ligne CLIENT");
			} else if (e[i] == EnumDonnee.produit && (nb < 1 || nb > 10)) {
				throw new FormatCSVException("le fichier ne doit comporter qu'entre 1 et 10 lignes PRODUIT");
			} else if(e[i] == EnumDonnee.commentaire && (nb < 0 || nb > 5)) {
				throw new FormatCSVException("le ficher ne doit comporter qu'entre 0 et 5 lignes COMMENTAIRE");
			}

		}
	}
}
