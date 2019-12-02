package facture.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import facture.DAO.ClientDAO;
import facture.DAO.FactureDAO;
import facture.DAO.TVADAO;
import facture.exception.DAOException;
import facture.modele.Client;
import facture.modele.Commentaire;
import facture.modele.Facture;
import facture.modele.Produit;
import facture.modele.TVA;

@Stateless
public class CreationPDF {

	@EJB
	private FactureDAO factureDAO;
	
	@EJB
	private TVADAO tvaDAO;
	
	@EJB
	private ClientDAO clientDAO;

	//Méthode générale qui créer le PDF en faisantr appel aux autres méthodes qui va insérer chaque partie du pdf 
	public void createPdf(String path, String filename, int numeroFacture) throws IOException, DocumentException, DAOException {
		Document document = new Document();

		@SuppressWarnings("unused")
		PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(path+filename));

		document.open();

		double total = 0;
		
			//Récupère les infos sur le Client et la Facture dont l'idFacture est passé en paramètre. Ces infos serviront dans l'en-tête de la Facture en PDF
			Client client = clientDAO.chercherClientParIDFacture(numeroFacture);   
			Facture facture = factureDAO.chercherInfosFactureParIDFacture(numeroFacture);
			
			//Récupère l'id de la TVA concerné par cette Facture pour récupèrer le taux de TVA à appliquer pour cette facture
			int idTVA = facture.getTVA();
			TVA tva = tvaDAO.recupererTVA(idTVA);
			Float TauxTVA = tva.getTaux();
			
			//Fait appel à la méthode qui crée l'en-tête de la facture avec les infos sur le Client et sur la Facture
			insererEnTete(document, client, facture);
			
			//Fait appel à la méthode qui crée l'en-tête du tableau de produits/commentaires
			insererEnTeteTableau(document);	
			
			//Récupère en BDD toutes les informations sur les Produits et Commentaires concernés par cette facture et les stocke dans une ArrayList d'Objets
			//composé de Produits, d'entiers pour les quantités et de Commentaires
			List<Object> liste = factureDAO.listerInfosParIDFacture(numeroFacture);

			//Boucle qui parcourt l'ArrayList pour créer soit des lignes Produits soit des lignes Commentaires
			for (int i = 0; i < liste.size(); i++) {
				if (liste.get(i) instanceof Produit) {
					Produit produit = (Produit) liste.get(i);
					int quantite = (int) liste.get(i+1);
					
					insererLigneProduitTableau(document, produit, quantite);
					
					//On calcule le total à chaque ligne produit
					total += produit.getPrix() * quantite;
				}
				if (liste.get(i) instanceof Commentaire) {
					Commentaire commentaire = (Commentaire) liste.get(i);
					
					insererLigneCommentaireTableau(document, commentaire);
				}
			}
			
			//Méthode qui crée le pied de page de la Facture avec le calcul du montant HT puis TTC (variable total)
			insererPiedPage(document, total, TauxTVA);
			
			document.close();
		}

	//Méthode qui créer l'en-tête de la facture avec les informations du client, le numéro de la facture et la date de la facture
	public Document insererEnTete(Document document, Client client, Facture facture) throws DocumentException {
		Font f=new Font(FontFamily.TIMES_ROMAN,18f,Font.UNDERLINE,BaseColor.BLACK);
		String idFacture = String.valueOf(facture.getId());
		Paragraph paragraph = new Paragraph("Facture N° : " + idFacture, f);
		paragraph.setIndentationLeft(200);
		paragraph.setSpacingAfter(20);			
		paragraph.setSpacingAfter(30);
		document.add(paragraph);

		document.add(new Paragraph("Nom : " + client.getNom()));
		document.add(new Paragraph("Prénom : " + client.getPrenom()));
		document.add(new Paragraph("Adresse : " + client.getAdresse1()));
		if (client.getAdresse2() == null) {
			document.add(new Paragraph("Complément d'adresse : "));
		} else {
			document.add(new Paragraph("Complément d'adresse : " + client.getAdresse2()));
		}
		document.add(new Paragraph(String.valueOf(client.getCp()) + "  " + client.getVille()));
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Paragraph paragraph2 = new Paragraph("Le " + dateFormat.format(facture.getDate()));
		paragraph2.setIndentationLeft(350);
		document.add(paragraph2);
		return document;
	}


	//Méthode qui créer l'en-tête du tableau
	public Document insererEnTeteTableau(Document document) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		table.setSpacingBefore(50);
		PdfPCell defaultcell = table.getDefaultCell();
		defaultcell.setFixedHeight(35f);
		defaultcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		defaultcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		Font f2=new Font(FontFamily.TIMES_ROMAN,14f,Font.BOLD,BaseColor.BLACK);
		table.addCell(new Paragraph("Référence", f2));
		table.addCell(new Paragraph("Désignation", f2));
		table.addCell(new Paragraph("Quantité", f2));
		table.addCell(new Paragraph("Prix unitaire HT", f2));
		table.setWidthPercentage(100);
		float[] columnWidths = {2.5f, 3f, 1f, 1.5f};
		table.setWidths(columnWidths);
		document.add(table);
		return document;
	}


	//Méthode qui crée une ligne pour un produit
	public Document insererLigneProduitTableau(Document document, Produit produit, int quantite) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		PdfPCell defaultcell = table.getDefaultCell();
		NumberFormat numberFormat = NumberFormat.getInstance(java.util.Locale.FRENCH);
		float[] columnWidths = {2.5f, 3f, 1f, 1.5f};
		defaultcell.setFixedHeight(18f);
		defaultcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		defaultcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100);
		table.setWidths(columnWidths);
		table.addCell(produit.getReference());
		table.addCell(produit.getDesignation());
		table.addCell(String.valueOf(quantite));
		table.addCell(new Paragraph(String.valueOf(numberFormat.format(produit.getPrix()))));
		document.add(table);

		return document;
	}


	//Méthode qui crée une ligne pour un commentaire
	public Document insererLigneCommentaireTableau(Document document, Commentaire commentaire) throws DocumentException {
		PdfPTable table = new PdfPTable(4);
		PdfPCell defaultcell = table.getDefaultCell();
		float[] columnWidths = {2.5f, 3f, 1f, 1.5f};
		defaultcell.setFixedHeight(18f);
		defaultcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		defaultcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100);
		table.setWidths(columnWidths);
		table.addCell("Commentaire");
		table.addCell(commentaire.getLibelle());
		table.addCell("");
		table.addCell("");
		document.add(table);

		return document;
	}

	//Méthode qui créer le pied de page de la facture avec le Total calculé HT et TTC
	public Document insererPiedPage (Document document, Double total, Float TVA) throws DocumentException {
		Paragraph paragraph = new Paragraph("TOTAL HT en €");
		paragraph.setSpacingBefore(50);
		paragraph.setIndentationLeft(340);
		document.add(paragraph);

		PdfPTable table = new PdfPTable(1);
		NumberFormat numberFormat = NumberFormat.getInstance(java.util.Locale.FRENCH);
		PdfPCell cel1 = new PdfPCell(new Paragraph(String.valueOf(numberFormat.format(total))));
		table.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cel1);
		table.setWidthPercentage(15);
		document.add(table);

		Paragraph paragraph2 = new Paragraph("TVA en %");
		paragraph2.setIndentationLeft(340);
		document.add(paragraph2);
		PdfPTable table2 = new PdfPTable(1);
		PdfPCell cel2 = new PdfPCell(new Paragraph(String.valueOf(numberFormat.format(TVA))));
		table2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cel2);
		table2.setWidthPercentage(15);
		document.add(table2);

		Paragraph paragraph3 = new Paragraph("TOTAL TTC en €");
		paragraph3.setIndentationLeft(340);
		document.add(paragraph3);
		PdfPTable table3 = new PdfPTable(1);
		PdfPCell cel3 = new PdfPCell(new Paragraph(String.valueOf(numberFormat.format((total * (1.0f+(TVA/100.0f)))))));
		table3.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cel3.setHorizontalAlignment(Element.ALIGN_CENTER);
		table3.addCell(cel3);
		table3.setWidthPercentage(15);
		document.add(table3);
		
		return document;
	}
}

