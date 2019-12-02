package facture.DAO;

import javax.ejb.Stateless;

import facture.exception.DAOException;
import facture.modele.Client;

@Stateless
public interface ClientDAO {
	int insererClient(Client client) throws DAOException;
	boolean verifierClient(String nom, String prenom)  throws DAOException;
	int chercherIDClient(String nom, String prenom) throws DAOException;
	Client chercherClientParIDClient(int idClient) throws DAOException;
	Client chercherClientParIDFacture(int idfacture) throws DAOException;
}

