package facture.DAO;

import javax.ejb.Stateless;

import facture.exception.DAOException;
import facture.modele.Commentaire;

@Stateless
public interface CommentaireDAO {
	int insererCommentaire(Commentaire commentaire) throws DAOException;
}
