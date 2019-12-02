package facture.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import facture.exception.DAOException;
import facture.modele.TVA;

@Stateless
public class TVADAOImpl implements TVADAO {

	@EJB
	private ConnexionBDD connexionBDD;
	
	@Override
	public TVA recupererTVA(int idTVA) throws DAOException {
		ResultSet resultat = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		TVA tva = new TVA();
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("Select * from tva where id_tva =  ? ;");
			preparedStatement.setInt(1, idTVA);           
			resultat = preparedStatement.executeQuery();
			resultat.next();
			tva.setLibelle(resultat.getString("nom"));
			tva.setTaux(resultat.getFloat("taux"));
			return tva;
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : chercherInfosTVA");
		}
		finally {
			DAOUtilitaire.arretRessource(resultat, preparedStatement, connexion);
		}
	}
	
	@Override
	public List<TVA> chercherInfosTVA() throws DAOException {
		ResultSet resultat = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		List<TVA> listeTva = new ArrayList<>();
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("Select * from tva order by taux;");      
			resultat = preparedStatement.executeQuery();
			while (resultat.next()) {
			TVA tva = new TVA();
			tva.setId(resultat.getInt("id_tva"));
			tva.setLibelle(resultat.getString("nom"));
			tva.setTaux(resultat.getFloat("taux"));
			listeTva.add(tva);
			}
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : chercherInfosTVA");
		}
		finally {
			DAOUtilitaire.arretRessource(resultat, preparedStatement, connexion);
		}
		return listeTva;
	}

}
