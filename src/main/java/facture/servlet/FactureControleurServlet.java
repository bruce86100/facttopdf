package facture.servlet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


import facture.exception.DAOException;
import facture.exception.FormatCSVException;
import facture.modele.TVA;
import facture.service.TraitementCSV;
import facture.service.VerifCSV;
import facture.DAO.*;


@WebServlet(name = "UploadServlet", urlPatterns = { "/upload" })
@MultipartConfig( maxFileSize = 1 * 1024 * 1024)
public class FactureControleurServlet extends HttpServlet{

	@EJB
	private TraitementCSV traitementCSV ;

	@EJB
	private VerifCSV verifCSV;

	@EJB
	private TVADAO tvaDAO;

	private static final long serialVersionUID = 1L;

	//Instancie deux constantes, le chemin du répertoire où le CSV et le PDF seront enregistré et le type du séparateur pour le fichier CSV
	public static final String PATH = System.getProperty("user.home")+"/Downloads/";
	public static final String SEPARATEUR = ";";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/upload.jsp");
		
		//On récupère la liste des TVA en BDD et on la passe en paramètre de la requête 7
		//afin de compléter la liste déroulante de taux de TVA proposé à l'utilisateur dans upload.jsp
		try {
			List<TVA> listeTVA = tvaDAO.chercherInfosTVA();

			req.setAttribute("liste", listeTVA);
			rd.forward(req, resp);
			
		} catch (DAOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		final String PATH_Menu = "/Menu.jsp";
		final Part lFilePart = req.getPart("file");    // Récupèrer le fichier sélectionné par Utilisateur sous forme de Part

		try {
			try {
				try {
					//Lit le flux du fichier pour vérifier le format du CSV.
					InputStream flux=lFilePart.getInputStream(); 

					InputStreamReader lecture=new InputStreamReader(flux);
					BufferedReader br=new BufferedReader(lecture);

					//méthode de vérification du format du CSV qui prend le séparateur du CSV + un bufferreader
					List<String> csv = verifCSV.verificationCSV(br, SEPARATEUR);  

					//Ferme toutes les ressources
					flux.close();
					lecture.close();
					br.close();	

					// Récupération de l'id Tva selectionné par l'utilisateur dans la liste déroulante d'upload.jsp
					int idTva = Integer.parseInt(req.getParameter("listetva"));

					//Fait appel à la méthode qui parcourt le CSV pour enregistrer les informations en BDD et récupère le nouvel id de facture crée en BDD
					int numFacture = traitementCSV.readCSV(csv, SEPARATEUR, idTva);

					String succes = "Le fichier CSV a été correctement sauvegardé sous le N° de Facture : " + numFacture;

					//Redirige vers la page Menu avec le Message de Succès
					req.setAttribute("message", succes);
					RequestDispatcher rd = getServletContext().getRequestDispatcher(PATH_Menu);
					rd.forward(req, resp);

				} catch (DAOException e) {
					String failed = "Un problème avec la base de donnée est survenu.\n" + e.getMessage();

					//Redirige vers la page Menu avec le message d'échec et l'exception personnalisée
					req.setAttribute("message2", failed);
					RequestDispatcher rd = getServletContext().getRequestDispatcher(PATH_Menu);
					rd.forward(req, resp);
				}

			} catch (FileNotFoundException|NumberFormatException e) {
				String failed = "Une erreur est survenue pendant l'enregistrement du fichier.\n" + e.getMessage();

				//Redirige vers la page Menu avec le message d'échec et l'exception personnalisée
				req.setAttribute("message2", failed);
				RequestDispatcher rd = getServletContext().getRequestDispatcher(PATH_Menu);
				rd.forward(req, resp);
			}

		} catch (FormatCSVException e) {
			String failed = "Le fichier CSV uploadé n'est pas au bon format.\n" + e.getMessage();

			//Redirige vers la page Menu avec le message d'échec et l'exception personnalisée
			req.setAttribute("message2", failed);
			RequestDispatcher rd = getServletContext().getRequestDispatcher(PATH_Menu);
			rd.forward(req, resp);
		}
	}
}
