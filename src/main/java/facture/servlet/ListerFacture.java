package facture.servlet;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javax.ejb.EJB;

import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facture.DAO.FactureDAO;
import facture.exception.DAOException;
import facture.modele.Facture;
import facture.service.CreationPDF;

@WebServlet(name = "listerFacture", urlPatterns = { "/listeFacture" })
public class ListerFacture extends HttpServlet {

	@EJB
	private CreationPDF creationPDF;
	
	@EJB
	FactureDAO factureDAO;

	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Facture> lst = listeFacture();
		List<String> factureName = new ArrayList<>();
		for (Facture f : lst) {
			String facture = String.valueOf(f. getId());
			factureName.add(facture);
		}

		req.setAttribute("liste", factureName);
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/listefacture.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}

	List<Facture> listeFacture() {
		List<Facture> lstNomFacture = new ArrayList<>();
		try {
			List<Facture> lstFacture = factureDAO.lister();
			for (Facture f : lstFacture) {
				lstNomFacture.add(f);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return lstNomFacture;
	}
}

