package facture.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.DocumentException;

import facture.exception.DAOException;
import facture.service.CreationPDF;

@WebServlet(name = "facturepdf", urlPatterns = { "/facturepdf" })
public class AfficherPDF extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@EJB
	private CreationPDF creationPDF;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int idFacture = Integer.parseInt(req.getParameter("id"));
		
		try {
			String nomFacture = "facture" + idFacture + ".pdf";
			
			creationPDF.createPdf(FactureControleurServlet.PATH, nomFacture, idFacture);
			
			File pdfFile = new File(FactureControleurServlet.PATH+nomFacture);
			
			resp.setContentType("application/pdf");
			resp.setHeader("Content-Disposition", "inline; filename="+nomFacture);
			resp.setContentLength((int) pdfFile.length());
			req.setAttribute("fichierpdf", pdfFile);
			
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(pdfFile));
			BufferedOutputStream bos = new BufferedOutputStream(resp.getOutputStream());
			
			byte[] buffer = new byte[2048];
			boolean end = false;
			while(!end) {
				int length = bis.read(buffer);
				if(length == -1) {
					end = true;
				} else {
					bos.write(buffer, 0, length);
				}
			}
			
			try {
				bis.close();
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
			
			try {
				bos.flush();
			} catch(IOException ex) {
				ex.printStackTrace();
			}
			
			bos.close();
			
		} catch (DocumentException | DAOException de) {
			de.printStackTrace();
		}
	}
}
