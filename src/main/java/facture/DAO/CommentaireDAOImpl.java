package facture.DAO;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import facture.exception.DAOException;
import facture.modele.Commentaire;

@Stateless
public class CommentaireDAOImpl implements CommentaireDAO {

	@EJB
	private ConnexionBDD connexionBDD;
	
	@Override
	public int insererCommentaire(Commentaire commentaire) throws DAOException {
		ResultSet result = null;
		PreparedStatement preparedStatement = null;
		Connection connexion = null;
		int idCommentaire = 0;
		try {
			connexion = connexionBDD.getConnection();
			preparedStatement = connexion.prepareStatement("INSERT INTO commentaire values (0, ?);", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, commentaire.getLibelle());
			preparedStatement.executeUpdate();
			result = preparedStatement.getGeneratedKeys();
			result.next();
			idCommentaire = result.getInt(1);
		} catch (SQLException e) {
			throw new DAOException("Problème de base de donnée lors de la méthode : insererCommentaire");
		}
		finally {
			DAOUtilitaire.arretRessource(result, preparedStatement, connexion);
		}
		return idCommentaire;
	}
}