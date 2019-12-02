package facture.DAO;

import javax.ejb.Stateless;

import facture.exception.DAOException;

@Stateless
public interface ProduitDAO {
	int chercherProduit(String reference) throws DAOException;
}
