package facture.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import facture.exception.DAOException;

@Stateless
public class ProduitDAOImpl implements ProduitDAO {

	@EJB
	private ConnexionBDD connexionBDD;
	
	@Override
	public int chercherProduit(String reference) throws DAOException {
		ResultSet resultat = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		int idproduit = 0;
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("Select id_produit from produit where reference like ? LIMIT 1;");           
			preparedStatement.setString(1, reference);
			resultat = preparedStatement.executeQuery();
			resultat.next();
			idproduit = resultat.getInt("id_produit");  
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : chercherProduit");
		}
		finally {
			DAOUtilitaire.arretRessource(resultat, preparedStatement, connexion);
		}
		return idproduit;
	}

}
