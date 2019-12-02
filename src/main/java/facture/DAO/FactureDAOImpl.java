package facture.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import facture.exception.DAOException;
import facture.modele.Commentaire;
import facture.modele.Facture;
import facture.modele.Produit;

@Stateless
public class FactureDAOImpl implements FactureDAO {

	@EJB
	private ConnexionBDD connexionBDD;
	
	@Override
	public int insererFacture(Facture facture) throws DAOException {
		ResultSet resultat = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("INSERT INTO facture values (0, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setDate(1, facture.getDate());
			preparedStatement.setInt(2, facture.getTVA());
			preparedStatement.executeUpdate();
			resultat = preparedStatement.getGeneratedKeys();
			resultat.next();
			int idFacture = resultat.getInt(1);
			return idFacture;
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : insererFacture");
		}
		finally {
			DAOUtilitaire.arretRessource(resultat, preparedStatement, connexion);
		}
	}

	public Facture chercherInfosFactureParIDFacture(int idFacture) throws DAOException {
		ResultSet resultat = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("SELECT * from facture where id_facture = ?;");
			preparedStatement.setInt(1, idFacture);
			resultat = preparedStatement.executeQuery();
			resultat.next();
			Date date = resultat.getDate("datecreation");
			int tva = resultat.getInt("id_facture_tva");
			Facture facture = new Facture();
			facture.setId(idFacture);
			facture.setDate(date);
			facture.setTVA(tva);

			return facture;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException("Problème de base de donnée lors de la méthode : chercherInfosFactureParIDFacture");
		}
		finally {
			DAOUtilitaire.arretRessource( resultat, preparedStatement, connexion);
		}
	}

	@Override
	public List<Facture> lister() throws DAOException{
		ResultSet resultat = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		List<Facture> liste_factures = new ArrayList<>();
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("Select * from facture;");           
			resultat = preparedStatement.executeQuery();
			while (resultat.next()) {
				int id = resultat.getInt("id_facture");
				Date date = resultat.getDate("datecreation");
				int tva = resultat.getInt("id_facture_tva");
				Facture facture = new Facture();
				facture.setId(id);
				facture.setDate(date);
				facture.setTVA(tva);

				liste_factures.add(facture);
			}
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : lister");
		}
		finally {
			DAOUtilitaire.arretRessource(resultat, preparedStatement, connexion);
		}
		return liste_factures;
	}

	//Méthode qui récupère toutes les informations sur les produits (+ cquantité) et commentaires en fonction du numéro de la facture dans la table detail_facture
	@Override
	public List<Object> listerInfosParIDFacture(int idfacture) throws DAOException{
		ResultSet resultat = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		List<Object> liste = new ArrayList<>();
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("select * from detail_facture left join commentaire on commentaire.id_commentaire = detail_facture.id_detail_facture_commentaire "
					+ "left join produit on produit.id_produit = detail_facture.id_detail_facture_produit where id_detail_facture_facture = ?;");
			preparedStatement.setInt(1, idfacture);
			resultat = preparedStatement.executeQuery();
			while (resultat.next()) {
				String reference =resultat.getString("reference");
				if (reference != null) {
					String designation =resultat.getString("designation");
					Double prix =resultat.getDouble("prix");
					Produit produit = new Produit();
					produit.setReference(reference);
					produit.setDesignation(designation);
					produit.setPrix(prix);
					liste.add(produit);

					int quantite = resultat.getInt("quantite");
					liste.add(quantite);
				}
				String libelle = resultat.getString("libelle");
				if (libelle != null) {
					Commentaire commentaire = new Commentaire();
					commentaire.setLibelle(libelle);

					liste.add(commentaire);
				}
			}
			return liste;
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : listerInfosParIDFacture");
		}
		finally {
			DAOUtilitaire.arretRessource(resultat, preparedStatement, connexion);
		}

	}

	@Override
	public void insererDetailFacture(int facture, int client, int produit, int quantite, int commentaire) throws DAOException {
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("INSERT INTO detail_facture values (0, ?, ?, ?, ?, ?);");
			preparedStatement.setInt(1, facture);
			preparedStatement.setInt(2, client);
			preparedStatement.setInt(3, produit);
			preparedStatement.setInt(4, quantite);
			preparedStatement.setInt(5, commentaire);           
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : insererDetailFacture");
		}
		finally {
			DAOUtilitaire.arretRessource(preparedStatement);
		}
	}

	@Override
	public void insererDetailFacture(int facture, int client, int produit, int quantite, String commentaire) throws DAOException {
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("INSERT INTO detail_facture values (0, ?, ?, ?, ?, ?);");
			preparedStatement.setInt(1, facture);
			preparedStatement.setInt(2, client);
			preparedStatement.setInt(3, produit);
			preparedStatement.setInt(4, quantite);
			preparedStatement.setString(5, commentaire);           
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : insererDetailFacture");
		}
		finally {
			DAOUtilitaire.arretRessource(preparedStatement, connexion);
		}
	}

	@Override
	public void insererDetailFacture(int facture, int client, String produit, String quantite, int commentaire) throws DAOException {
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("INSERT INTO detail_facture values (0, ?, ?, ?, ?, ?);");
			preparedStatement.setInt(1, facture);
			preparedStatement.setInt(2, client);
			preparedStatement.setString(3, produit);
			preparedStatement.setString(4, quantite);
			preparedStatement.setInt(5, commentaire);           
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : insererDetailFacture");
		}
		finally {
			DAOUtilitaire.arretRessource(preparedStatement, connexion);
		}
	}
}

