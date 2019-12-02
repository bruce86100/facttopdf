package facture.modele;

import java.sql.Date;

public class Facture {

	private int id;
	private Date date;
	private int tva;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getTVA() {
		return tva;
	}

	public void setTVA(int idTva) {
		tva = idTva;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
