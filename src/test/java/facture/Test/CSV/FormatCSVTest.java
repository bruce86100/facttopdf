package facture.Test.CSV;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import facture.modele.EnumDonnee;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import facture.exception.FormatCSVException;
import facture.service.VerifCSV;


@RunWith(MockitoJUnitRunner.class)
public class FormatCSVTest {

	@InjectMocks
	private VerifCSV verifCSV = new VerifCSV();


	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void testQueLaMethodeDeValidationEnTeteNEnvoiePasFormatCSVException_CarTrigrammeCLIFaitPartieDesTrigrammesValides() throws Exception {
		String trigramme = "CLI";
		try {
			verifCSV.validationEnTETE(trigramme);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}
	@Test
	public void testQueLaMethodeDeValidationEnTeteNEnvoiePasFormatCSVException_CarTrigrammePDTFaitPartieDesTrigrammesValides() throws Exception {
		String trigramme = "PDT";
		try {
			verifCSV.validationEnTETE(trigramme);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}
	@Test
	public void testQueLaMethodeDeValidationEnTeteNEnvoiePasFormatCSVException_CarTrigrammeCOMFaitPartieDesTrigrammesValides() throws Exception {
		String trigramme = "COM";
		try {
			verifCSV.validationEnTETE(trigramme);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}

	@Test
	public void testQueLaMethodeDeValidationEnTeteEnvoieFormatCSVException_CarTrigrammeNeFaitPasPartieDesTrigrammesValides() throws Exception {
		String trigramme = "CLIE";

		expectedException.expect(FormatCSVException.class);
		verifCSV.validationEnTETE(trigramme);
	}

	@Test
	public void testQueLaMethodeDeValidationEnTeteEnvoieFormatCSVException_CarTrigrammeEstVide() throws Exception {
		String trigramme = "";

		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("L'en-tête ne doit pas être vide");
		verifCSV.validationEnTETE(trigramme);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandLePrenomClientESTOK_AlorsPasdException() throws Exception {
		String ligneClient [] = {"CLI", "DUJARDIN", "Paul", "adresse1", "", "33000", "Bordeaux"};
		try {
			verifCSV.validationLigneClient(ligneClient);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandLePrenomClientavectiretESTOK_AlorsPasdException() throws Exception {
		String ligneClient [] = {"CLI", "DUJARDIN", "pre-nom", "adresse1", "", "33000", "Bordeaux"};
		try {
			verifCSV.validationLigneClient(ligneClient);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandLePrenomClientavecChiffreESTPasOK_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "DUJARDIN", "pren1om", "adresse1", "", "33000", "Bordeaux"};

		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le format pour le nom et/ou le prénom et/ou la ville n'est pas valide");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandLePrenomClientavecAccentESTOK_AlorsPasdException() throws Exception {
		String ligneClient [] = {"CLI", "DUJARDIN", "préèàöôùnom", "adresse1", "", "33000", "Bordeaux"};
		try {
			verifCSV.validationLigneClient(ligneClient);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandLePrenomClientavecCaractereSpeciauxEstPasOK_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "DUJAR,DIN", "prenom", "adresse1", "", "33000", "Bordeaux"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le format pour le nom et/ou le prénom et/ou la ville n'est pas valide");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandLeNomClientESTOK_AlorsPasdException() throws Exception {
		String ligneClient [] = {"CLI", "DUJARDIN", "prenom", "adresse1", "", "33000", "Bordeaux"};
		try {
			verifCSV.validationLigneClient(ligneClient);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandLeNomClientavectiretESTOK_AlorsPasdException() throws Exception {
		String ligneClient [] = {"CLI", "DUJ-ARDIN", "prenom", "adresse1", "", "33000", "Bordeaux"};
		try {
			verifCSV.validationLigneClient(ligneClient);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandLeNomClientavecCaractereSpeciauxEstPasOK_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "DUJAR,DIN", "prenom", "adresse1", "", "33000", "Bordeaux"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le format pour le nom et/ou le prénom et/ou la ville n'est pas valide");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandLeNomClientavecChiffreESTPasOK_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "DUJA1RDIN", "prenom", "adresse1", "", "33000", "Bordeaux"};

		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le format pour le nom et/ou le prénom et/ou la ville n'est pas valide");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandLeNomClientavecAccentESTOK_AlorsPasdException() throws Exception {
		String ligneClient [] = {"CLI", "Déèàöôùpre", "prenom", "adresse1", "", "33000", "Bordeaux"};
		try {
			verifCSV.validationLigneClient(ligneClient);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandNomClientVide_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "", "prenom", "adresse1", "", "33000", "Bordeaux"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un des champs autre qu'adresse2 n'est pas renseigné.");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandPrenomClientVide_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "nom", "", "adresse1", "", "33000", "Bordeaux"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un des champs autre qu'adresse2 n'est pas renseigné.");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandAdresse1ClientVide_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "nom", "prénom", "", "", "33000", "Bordeaux"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un des champs autre qu'adresse2 n'est pas renseigné.");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandCPClientVide_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "nom", "prénom", "1 rue de Mireuil", "", "", "Bordeaux"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le format du code postal n'est pas bon.");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandVilleClientVide_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "nom", "prénom", "1 rue de Mireuil", "", "33000", ""};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un des champs autre qu'adresse2 n'est pas renseigné.");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandVilleClientAvecChiffre_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "nom", "prénom", "1 rue de Mireuil", "", "33000", "Bord2eaux"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le format pour le nom et/ou le prénom et/ou la ville n'est pas valide");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandCPClientAvec4chiffres_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "nom", "prénom", "1 rue de Mireuil", "", "3300", "Bordeaux"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le format du code postal n'est pas bon.");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandCPClientAvec6chiffres_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "nom", "prénom", "1 rue de Mireuil", "", "330000", "Bordeaux"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le format du code postal n'est pas bon.");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testSiCPClientAvecLettres_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "nom", "prénom", "1 rue de Mireuil", "", "codepostal", "Bordeaux"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le format du code postal n'est pas bon.");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandLigneClientAvecDonnéesEnTrop_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "nom", "prénom", "1 rue de Mireuil", "", "codepostal", "Bordeaux", ""};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un des 6 champs nécessaires pour la ligne client n'est pas renseigné. Rappel du format : Nom; Prenom; adresse1; adresse 2; CP; ville ");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneClientQuandLigneClientAvecDonnéesEnMoins_AlorsFormatCSVException() throws Exception {
		String ligneClient [] = {"CLI", "nom", "prénom", "1 rue de Mireuil", "", "codepostal"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un des 6 champs nécessaires pour la ligne client n'est pas renseigné. Rappel du format : Nom; Prenom; adresse1; adresse 2; CP; ville ");
		verifCSV.validationLigneClient(ligneClient);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneProduitQuandLigneProduitAvecDonnéesEnMoins_AlorsFormatCSVException() throws Exception {
		String ligneProduit [] = {"PDT" , "reference", "designation", "1"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un des 4 champs nécessaires pour la ligne produit n'est pas renseigné. Rappel du format : Référence; Désignation; Quantité; Prix HT");
		verifCSV.validationLigneProduit(ligneProduit);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneProduitQuandLigneProduitAvecDonnéesEnTrop_AlorsFormatCSVException() throws Exception {
		String ligneProduit [] = {"PDT" , "reference" , "designation", "1", "1", "1"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un des 4 champs nécessaires pour la ligne produit n'est pas renseigné. Rappel du format : Référence; Désignation; Quantité; Prix HT");
		verifCSV.validationLigneProduit(ligneProduit);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneProduitQuandReferenceEstVide_AlorsFormatCSVException() throws Exception {
		String ligneProduit [] = {"PDT" , "", "designation", "1", "1"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un des champs n'est pas renseigné.");
		verifCSV.validationLigneProduit(ligneProduit);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneProduitQuandDesignationEstVide_AlorsFormatCSVException() throws Exception {
		String ligneProduit [] = {"PDT" , "reference", "", "1", "1"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un des champs n'est pas renseigné.");
		verifCSV.validationLigneProduit(ligneProduit);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneProduitQuandQuantiteEstVide_AlorsFormatCSVException() throws Exception {
		String ligneProduit [] = {"PDT" , "reference" , "designation", "", "1"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un des champs n'est pas renseigné.");
		verifCSV.validationLigneProduit(ligneProduit);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneProduitQuandPrixEstVide_AlorsFormatCSVException() throws Exception {
		String ligneProduit [] = {"PDT" , "reference", "designation", "", "1"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un des champs n'est pas renseigné.");
		verifCSV.validationLigneProduit(ligneProduit);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneProduitQuandQuantitéNEstPasEntier_AlorsFormatCSVException() throws Exception {
		String ligneProduit [] = {"PDT" , "reference", "designation", "1,1", "1"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le champ quantité ne peut être composé que de chiffres entiers positifs.");
		verifCSV.validationLigneProduit(ligneProduit);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneProduitQuandQuantitéNEstPasUnChiffre_AlorsFormatCSVException() throws Exception {
		String ligneProduit [] = {"PDT" , "reference", "designation", "coucou", "1"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le champ quantité ne peut être composé que de chiffres entiers positifs.");
		verifCSV.validationLigneProduit(ligneProduit);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneProduitQuandQuantiteEstNegatif_AlorsFormatCSVException() throws Exception {
		String ligneProduit [] = {"PDT" , "reference", "designation", "-1", "1"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le champ quantité ne peut être composé que de chiffres entiers positifs.");
		verifCSV.validationLigneProduit(ligneProduit);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneProduitQuandPrixEstNegatif_AlorsFormatCSVException() throws Exception {
		String ligneProduit [] = {"PDT" , "reference", "designation", "1", "-1"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le champ prix ne peut être composé que de chiffres décimaux positifs.");
		verifCSV.validationLigneProduit(ligneProduit);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneProduitQuandPrixNEstPasUnChiffre_AlorsFormatCSVException() throws Exception {
		String ligneProduit [] = {"PDT" , "reference", "designation", "1", "coucou"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le champ prix ne peut être composé que de chiffres décimaux positifs.");
		verifCSV.validationLigneProduit(ligneProduit);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneCommentaireAveDonneeEnTrop_AlorsFormatCSVException() throws Exception {
		String ligneCommentaire [] = {"COM" , "Coucou Le Monde", "designation"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un seul champ (non vide) est nécessaire pour la ligne commentaire");
		verifCSV.validationLigneCommentaire(ligneCommentaire);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneCommentaireAveDonneeEnMoins_AlorsFormatCSVException() throws Exception {
		String ligneCommentaire [] = {"COM"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Un seul champ (non vide) est nécessaire pour la ligne commentaire");
		verifCSV.validationLigneCommentaire(ligneCommentaire);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneCommentaireAvecPlusDe250Caracteres_AlorsFormatCSVException() throws Exception {
		String ligneCommentaire [] = {"COM", "ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc"};
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("Le commentaire ne peut dépasser 250 caractères.");
		verifCSV.validationLigneCommentaire(ligneCommentaire);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneCSVEstOKAvec1LigneClient_AlorsPasDeFormatCSVException() throws Exception {
		List<String> ligneClient = new ArrayList<String>();
		ligneClient.add("CLI;coucou;RODOLPHE;1 rue des Champs Elysee;;33000;Bordeaux");
		try {
			verifCSV.verifierLigneCsv(ligneClient, EnumDonnee.client);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}

	@Test
	public void testQueLaMethodeDeValidationLigneCSVNEstPASOKAvec2LignesClient_AlorsFormatCSVException() throws Exception {
		List<String> ligneClient = new ArrayList<String>();
		ligneClient.add("CLI;coucou;RODOLPHE;1 rue des Champs Elysee;;33000;Bordeaux");
		ligneClient.add("CLI;coucou;RODOLPHE;1 rue des Champs Elysee;;33000;Bordeaux");
		
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("le fichier doit comporter exactement une et une seule ligne CLIENT");
		
		verifCSV.verifierLigneCsv(ligneClient, EnumDonnee.client);
	}
	
	@Test
	public void testQueLaMethodeDeValidationLigneCSVEstOKAvec1LigneProduit_AlorsPasDeFormatCSVException() throws Exception {
		List<String> ligneProduit = new ArrayList<String>();
		ligneProduit.add("PDT;BRENDALAPLUSBELLE;Ok Google;1;399.00");
		try {
			verifCSV.verifierLigneCsv(ligneProduit, EnumDonnee.produit);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}

	@Test
	public void testQueLaMethodeDeValidationLigneCSVNEstPASOKAvec12LignesProduits_AlorsFormatCSVException() throws Exception {
		List<String> ligneProduit = new ArrayList<String>();
		for (int i=0; i<13; i++) {
			ligneProduit.add("PDT;BRENDALAPLUSBELLE;Ok Google;1;399.00");
		}
		
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("le fichier ne doit comporter qu'entre 1 et 10 lignes PRODUIT");
		
		verifCSV.verifierLigneCsv(ligneProduit, EnumDonnee.produit);
	}
	
	@Test
	public void testQueLaMethodeDeValidationLigneCSVNEstPASOKAvec0LignesProduits_AlorsFormatCSVException() throws Exception {
		List<String> ligneProduit = new ArrayList<String>();
		
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("le fichier ne doit comporter qu'entre 1 et 10 lignes PRODUIT");
		
		verifCSV.verifierLigneCsv(ligneProduit, EnumDonnee.produit);
	}

	@Test
	public void testQueLaMethodeDeValidationLigneCSVEstOKAvec0LigneCommentaire_AlorsPasDeFormatCSVException() throws Exception {
		List<String> ligneCommentaire = new ArrayList<String>();
		try {
			verifCSV.verifierLigneCsv(ligneCommentaire, EnumDonnee.commentaire);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}
	
	@Test
	public void testQueLaMethodeDeValidationLigneCSVEstOKAvec1LigneCommentaire_AlorsPasDeFormatCSVException() throws Exception {
		List<String> ligneCommentaire = new ArrayList<String>();
		ligneCommentaire.add("COM;Hello World");
		try {
			verifCSV.verifierLigneCsv(ligneCommentaire, EnumDonnee.commentaire);
		} catch (FormatCSVException e) {
			fail("Une FormatCSVException a été levé");
		}
	}

	@Test
	public void testQueLaMethodeDeValidationLigneCSVNEstPASOKAvec6LignesCommentaires_AlorsFormatCSVException() throws Exception {
		List<String> ligneCommentaire = new ArrayList<String>();
		for (int i=0; i<7; i++) {
			ligneCommentaire.add("COM;Hello World");
		}
		
		expectedException.expect(FormatCSVException.class);
		expectedException.expectMessage("le ficher ne doit comporter qu'entre 0 et 5 lignes COMMENTAIRE");
		
		verifCSV.verifierLigneCsv(ligneCommentaire, EnumDonnee.commentaire);
	}
}
