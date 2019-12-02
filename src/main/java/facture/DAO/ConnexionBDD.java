package facture.DAO;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

@Stateless
public class ConnexionBDD {

	@Resource(name = "upload")
	private DataSource dataSource;
	
	public Connection getConnection() {
	Connection connexion = null;
	try {
		connexion = dataSource.getConnection();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return connexion;
	}
}
