package facture.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ejb.Stateless;

@Stateless
public class DAOUtilitaire {
	
	/* Arrête la ressource : resultset */
	public static void arretRessource( ResultSet resultSet ) {
	    if ( resultSet != null ) {
	        try {
	            resultSet.close();
	        } catch ( SQLException e ) {
	            System.out.println( "Impossible de fermer le ResultSet : " + e.getMessage() );
	        }
	    }
	}


	/* Arrête la ressource : preparedstatement */
	public static void arretRessource( PreparedStatement preparedstatement ) {
	    if ( preparedstatement != null ) {
	        try {
	        	preparedstatement.close();
	        } catch ( SQLException e ) {
	            System.out.println( "Impossible de fermer le Statement : " + e.getMessage() );
	        }
	    }
	}


	/* Arrête la ressource : Connexion */
	public static void arretRessource( Connection connexion ) {
	    if ( connexion != null ) {
	        try {
	            connexion.close();
	        } catch ( SQLException e ) {
	            System.out.println( "Impossible de fermer la connexion : " + e.getMessage() );
	        }
	    }
	}

	/* Arrêt de 2 types de ressources  */
	public static void arretRessource( ResultSet resultSet, PreparedStatement preparedstatement) {
		arretRessource( resultSet );
		arretRessource( preparedstatement );
	}
	
	/* Arrêt de 2 types de ressources  */
	public static void arretRessource( ResultSet resultSet, Connection connexion) {
		arretRessource( resultSet );
		arretRessource( connexion );
	}
	
	/* Arrêt de 2 types de ressources  */
	public static void arretRessource( PreparedStatement preparedstatement, Connection connexion ) {
		arretRessource( preparedstatement );
		arretRessource( connexion );
	}


	/* Arrêt de 3 types de  ressources  */
	public static void arretRessource( ResultSet resultSet, PreparedStatement preparedstatement, Connection connexion ) {
		arretRessource( resultSet );
		arretRessource( preparedstatement );
		arretRessource( connexion );
	}
}
