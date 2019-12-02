package facture.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import facture.exception.DAOException;
import facture.modele.Client;

@Stateless
public class ClientDAOImpl implements ClientDAO {

	@EJB
	private ConnexionBDD connexionBDD;
	
	@Override
	public int insererClient(Client client) throws DAOException {
		ResultSet resultat = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		int idClient = 0;
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("INSERT INTO client values (0, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, client.getNom());
			preparedStatement.setString(2, client.getPrenom());
			preparedStatement.setString(3, client.getAdresse1());
			preparedStatement.setString(4, client.getAdresse2());
			preparedStatement.setInt(5, client.getCp());            
			preparedStatement.setString(6, client.getVille());
			preparedStatement.executeUpdate();
			resultat = preparedStatement.getGeneratedKeys();
			resultat.next();
			idClient = resultat.getInt(1);
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : insererClient");
		}
		finally {
			DAOUtilitaire.arretRessource(resultat, preparedStatement, connexion);
		}
		return idClient;
	}

	//Méthode qui cherche en BDD si le client est déjà présent (recherche sur nom + prénom) et renvoi un booléen. S'il est présent, le booléen est True sinon il est False.
	@Override
	public boolean verifierClient(String nom, String prenom) throws DAOException  {
		ResultSet resultat = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		Boolean bool = false;
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("Select * from client where nom like ? and prenom like ? LIMIT 1;");
			preparedStatement.setString(1, nom);
			preparedStatement.setString(2, prenom);	            
			resultat = preparedStatement.executeQuery();
			bool = resultat.next();
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : verifierClient");
		}
		finally {
			DAOUtilitaire.arretRessource(resultat, preparedStatement, connexion);
		}
		return bool;
	}

	@Override
	public Client chercherClientParIDClient(int idClient) throws DAOException {
		ResultSet resultat = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		Client client = new Client();
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("Select * from client where id_client =  ? ;");
			preparedStatement.setInt(1, idClient);           
			resultat = preparedStatement.executeQuery();
			resultat.next();
			client.setNom(resultat.getString("nom"));
			client.setPrenom(resultat.getString("prenom"));
			client.setAdresse1(resultat.getString("adresse1"));
			client.setAdresse2(resultat.getString("adresse2"));
			client.setCp(resultat.getInt("cp"));
			client.setVille(resultat.getString("ville"));
			return client;
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : chercherClientParID");
		}
		finally {
			DAOUtilitaire.arretRessource(resultat, preparedStatement, connexion);
		}
	}

	@Override
	public int chercherIDClient(String nom, String prenom) throws DAOException {
		ResultSet resultat = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		int idClient = 0;
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("Select id_client from client where nom like ? and prenom like ? LIMIT 1;", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, nom);
			preparedStatement.setString(2, prenom);	           
			resultat = preparedStatement.executeQuery();
			resultat.next();
			idClient = resultat.getInt("id_client");
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : chercherIDClient");
		}
		finally {
			DAOUtilitaire.arretRessource(resultat, preparedStatement, connexion);
		}
		return idClient;
	}
	
	//Méthode qui récupère toutes les informations du client en fonction du numéro de la facture dans la table detail_facture
	@Override
	public Client chercherClientParIDFacture(int idfacture) throws DAOException{
		ResultSet resultat = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("select * from client left join detail_facture on client.id_client = detail_facture.id_detail_facture_client"
					+ " where id_detail_facture_facture = ? limit 1;");
			preparedStatement.setInt(1, idfacture);
			resultat = preparedStatement.executeQuery();
			resultat.next();
			String nom =resultat.getString("nom");
			String prenom =resultat.getString("prenom");
			String adresse1 =resultat.getString("adresse1");
			String adresse2 =resultat.getString("adresse2");
			int cp = resultat.getInt("cp");
			String ville = resultat.getString("ville");
			Client client = new Client();
			client.setNom(nom);
			client.setPrenom(prenom);
			client.setAdresse1(adresse1);
			client.setAdresse1(adresse2);
			client.setCp(cp);
			client.setVille(ville);

			return client;
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : chercherClientParIDFacture");
		}
		finally {
			DAOUtilitaire.arretRessource( resultat, preparedStatement, connexion);
		}

	}

}

