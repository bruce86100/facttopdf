package facture.DAO;

import java.util.List;

import javax.ejb.Stateless;

import facture.exception.DAOException;
import facture.modele.Facture;

@Stateless
public interface FactureDAO {
	int insererFacture(Facture facture) throws DAOException;
	void insererDetailFacture(int facture, int client, int produit, int quantite, int commentaire) throws DAOException;	
	void insererDetailFacture(int facture, int client, int produit, int quantite, String commentaire) throws DAOException;
	void insererDetailFacture(int facture, int client, String produit, String quantite, int commentaire) throws DAOException;
	List<Facture> lister() throws DAOException;
	List<Object> listerInfosParIDFacture(int idfacture) throws DAOException;
	Facture chercherInfosFactureParIDFacture(int idfacture) throws DAOException;
}
