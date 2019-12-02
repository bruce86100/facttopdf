package facture.DAO;

import java.util.List;
import javax.ejb.Stateless;

import facture.exception.DAOException;
import facture.modele.TVA;

@Stateless
public interface TVADAO {
	List<TVA>chercherInfosTVA() throws DAOException;
	TVA recupererTVA(int idTVA) throws DAOException;
}
