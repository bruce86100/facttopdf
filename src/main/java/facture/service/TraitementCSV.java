package facture.service;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import facture.DAO.ClientDAO;
import facture.DAO.CommentaireDAO;
import facture.DAO.FactureDAO;
import facture.DAO.ProduitDAO;
import facture.DAO.TVADAO;
import facture.exception.DAOException;
import facture.modele.Client;
import facture.modele.Commentaire;
import facture.modele.Facture;

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
public class TraitementCSV {

	@EJB
	private ClientDAO clientDAO ;
	@EJB
	private FactureDAO factureDAO ;
	@EJB
	private ProduitDAO produitDAO ;
	@EJB
	private CommentaireDAO commentaireDAO ;
	@EJB
	private TVADAO tvaDAO;
	//Ressource qui gère la gestion transactionnelle en BDD
	@Resource
	private UserTransaction transaction;

	
	//Méthode qui insère en BDD toutes les informations du CSV et retourne le nouvel ID de la facture finalement créée
	public int readCSV (List<String> CSV, String separateur, int idTVA) throws DAOException {

		Facture facture = new Facture();

		ArrayList<Integer> produits = new ArrayList<>();
		ArrayList<Integer> quantites = new ArrayList<>();
		ArrayList<Integer> commentaires = new ArrayList<>();

		int idclient  = 0;
		int idfacture = 0;

		Date dateSQL = new java.sql.Date(new java.util.Date().getTime()); //Récupère la date du jour au format SQL

		//  Enregistre toutes les lignes du CSV dans un tableau de String "Result" qui sera parcouru ligne après ligne
		//  et selon le trigramme d'en-tête, un objet Client ou Produit ou Commentaire sera crée et enfin une nouvelle Facture
		//  Finalement, chaque objet sera inséré en BDD 

		try {
			transaction.begin();
			for(int i=0; i<CSV.size(); i++) {

				String cellule[] = CSV.get(i).split(separateur);

				if (cellule[0].equals("CLI")) {
					String nomclient = cellule[1];
					String prenomclient = cellule[2];

					//si le client n'est pas déjà en BDD, la méthode verifierClient renvoie un booléen à False
					//puis cela crée un nouveau client en BDD et l'insère en BDD
					if (!clientDAO.verifierClient(nomclient, prenomclient)) { 

						Client client = new Client();
						client.setNom(cellule[1]);		
						client.setPrenom(cellule[2]);
						client.setAdresse1(cellule[3]);
						client.setAdresse2(cellule[4]);
						client.setCp(Integer.parseInt(cellule[5]));	
						client.setVille(cellule[6]);

						//Insère le nouveau client en BDD
						idclient = clientDAO.insererClient(client);
					} 

					//Sinon affecte à client, le client correspondant en BDD
					idclient = clientDAO.chercherIDClient(nomclient, prenomclient); 

				} else if (cellule[0].equals("PDT")) {

					String referenceproduit = cellule[1];

					//Affecte au nouveau produit, le produit correspondant en BDD
					int idproduit = produitDAO.chercherProduit(referenceproduit);

					// ajoute l'id du produit correspondant en BDD à l'arraylist "produits"
					produits.add(idproduit);
					// ajoute la quantité de ce produit à l'arraylist "produits"
					quantites.add(Integer.parseInt(cellule[3]));

				} else if (cellule[0].equals("COM")) {

					Commentaire commentaire = new Commentaire();
					commentaire.setLibelle(cellule[1]);

					// insère le commentaire en BDD
					int idcommentaire = commentaireDAO.insererCommentaire(commentaire);
					commentaires.add(idcommentaire);

				}
			}
            
			facture.setDate(dateSQL); 
			facture.setTVA(idTVA);            

			idfacture = factureDAO.insererFacture(facture);      // insère la nouvelle facture en BDD et recupère son ID

			//  Deux cas de figure possibles : 
			//  1/ Le nombre de produits dans le CSV est > au nombre de commentaires
			//  2/ Le nombre de commentaires dans le CSV est > au nombre de produits 
			int p = produits.size();  // le nombre de produits dans le CSV
			int c = commentaires.size();  // le nombre de commentaires dans le CSV

			// Insertion de toutes les LigneFacture une fois toutes les clés étrangères connues
			// Selon le cas de figure, il faut prévoir que la ligneFacture insère un produit + une quantité nulle ou un commentaire nul
			// lorsque le compteur de la boucle k qui insère toutes les lignes factures a épuisé la donnée limitante (produits ou commentaires)
			if ( p > c ) {
				for (int k = 0; k < p; k++) {	
					if (k < c) {
						factureDAO.insererDetailFacture(idfacture, idclient , produits.get(k), quantites.get(k), commentaires.get(k)); //produits.get(k) correspond au produit       produits.get(k+1) correspond à la quantité
					} else {
						factureDAO.insererDetailFacture(idfacture, idclient, produits.get(k), quantites.get(k), null);
					}
				}
			} else {
				for (int k = 0; k < c; k++) {				
					if (k < p) {
						factureDAO.insererDetailFacture(idfacture, idclient, produits.get(k), quantites.get(k), commentaires.get(k)); //produits.get(k) correspond au produit       produits.get(k+1) correspond à la quantité
					} else {
						factureDAO.insererDetailFacture(idfacture, idclient, null, null, commentaires.get(k));
					}
				}
			}

			// On finit par un commit si tout est OK et on retourne l'id de la facture crée en BDD
			try {
				transaction.commit(); 
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				System.out.println("Problème avec le commit lors de la transaction avec la BDD");
				e.printStackTrace();
			}

			//Si Erreur, on fait un rollback pour conserver l'intégrité de la BDD
		} catch (DAOException | NotSupportedException | SystemException e) {
			try {
				transaction.rollback();
				throw new DAOException("Une erreur est survenu pendant l'enregistrement en BDD. Un retour arrière a été effectué pour conserver l'intégrité de la BDD");	
			} catch (IllegalStateException | SecurityException | SystemException e1) {
				System.out.println("Problème avec le roolback lors de la transaction avec la BDD");
				e1.printStackTrace();
			}
		}
		return idfacture;
	}
}




