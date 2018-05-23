package model;

import java.io.File;
import java.util.Collection;

/**
 * Ohjelman pääluokka. Tämän kautta voidaan käsitellä helposti kaikkia muita tietorakenteita.
 * @author Jouko Sirkka, Miro Korhonen
 * @version 1.0, 15.5.2018
 *
 */
public class Vuokraamo {
	private Pyorat pyorat = new Pyorat();
	private Vuokraukset vuokraukset = new Vuokraukset();
	private Asiakkaat asiakkaat = new Asiakkaat();



	/**
	 * Lisää uuden pyörän pyöriin
	 * @param pyora lisättävä pyörä
	 * @throws SailoException jos tietorakenne jo täynnä
	 */
	public void lisaaPyora(Pyora pyora) throws SailoException {
		pyora.rekisteroi();
		pyorat.lisaa(pyora);
	}


	/**
	 * Lisää vuokrauksen vuokrauksiin
	 * @param vuokraus lisättävä vuokraus
	 */
	public void lisaaVuokraus(Vuokraus vuokraus) {
		Pyora temp = pyorat.anna(vuokraus.getPyoraId());
		if (temp == null || temp.getOnkoVarattu() == true) return;
		temp.setOnkoVarattu(true);
		vuokraus.rekisteroi();
		vuokraukset.lisaa(vuokraus);
	}


	/**
	 * Lisää asiakkaan asiakkaisiin
	 * @param asiakas lisättävä asiakas
	 * @throws SailoException jos tietorakenne jo täynnä
	 */
	public void lisaaAsiakas(Asiakas asiakas) throws SailoException {
		asiakas.rekisteroi();
		asiakkaat.lisaa(asiakas);
	}


	/**
	 * Antaa halutun pyörän vuokrauksen
	 * @param pyora pyörä jonka vuokrausta halutaan
	 * @return pyörän vuokraus, null jos ei ole vuokrausta
	 */
	public Vuokraus annaVuokraus(Pyora pyora) {
		Vuokraus v = vuokraukset.etsi(pyora.getPyoranID());
		return v;
		
	}
	
	
	/**
	 * Palauttaa asiakkaan vuokrauksen avulla
	 * @param vuokraus Vuokraus, jonka asiakas etsitään
	 * @return Asiakas, jos sellainen löydettiin, muuten null
	 */
	public Asiakas annaAsiakas(Vuokraus vuokraus) {
		if(vuokraus != null) {
			Asiakas a = asiakkaat.etsi(vuokraus.getAsiakasId());
			return a;
		}
		return null;
	}


	/**
	 * Antaa pyörien lukumäärän
	 * @return pyörien lukumäärä
	 */
	public int getPyoria() {
		return pyorat.getLkm();
	}
	
	public Collection<Pyora> hinnanMukaan(Collection<Pyora> lista) throws SailoException{
		return pyorat.jarjestaHalvin(lista);
	}


	/**
	 * Asettaa tiedostojen nimet ja luo tiedostorakenteen
	 * @param nimi hakemiston nimi
	 */
	public void setTiedosto(String nimi) {
		File dir = new File(nimi);
		dir.mkdirs();
		String hakemistonNimi = "";
		if (!nimi.isEmpty()) hakemistonNimi = nimi + "/";
		pyorat.setTiedostonPerusNimi(hakemistonNimi + "pyorat");
		vuokraukset.setTiedostonPerusNimi(hakemistonNimi + "vuokraukset");
		asiakkaat.setTiedostonPerusNimi(hakemistonNimi + "asiakkaat");
	}


	/**
	 * Luetuttaa kaikki tiedostot
	 * @param nimi tiedoston nimi
	 * @throws SailoException jos tulee poikkeus
	 */
	public void lueTiedostosta(String nimi) throws SailoException {
		pyorat = new Pyorat();
		vuokraukset = new Vuokraukset();
		asiakkaat = new Asiakkaat();

		setTiedosto(nimi);
		pyorat.lueTiedostosta();
		vuokraukset.lueTiedostosta();
		asiakkaat.lueTiedostosta();
	}


	/**
	 * Palauttaa "taulukossa" hakuehtoon vastaavien pyörien viitteet
	 * @param hakuehto hakuehto
	 * @return tietorakenteen löytyneistä pyöristä
	 * @throws SailoException Jos jotakin menee väärin
	 */
	public Collection<Pyora> etsi(String hakuehto, boolean vapaanaValittu) throws SailoException {
		return pyorat.etsi(hakuehto, vapaanaValittu);
	}


	/**
	 * Tallentaa tiedostot
	 * @throws SailoException
	 */
	public void tallenna() throws SailoException {
		pyorat.setMuutettu(true);
		String virhe = "";
		try {
			pyorat.tallenna();
		} catch (SailoException e) {
			virhe = e.getMessage();
		}
		try {
			vuokraukset.tallenna();
		} catch (SailoException e) {
			virhe += e.getMessage();
		}
		try {
			asiakkaat.tallenna();
		} catch (SailoException e) {
			virhe += e.getMessage();
		}
		if (!"".equals(virhe)) throw new SailoException(virhe);
	}
	
	
	 /**
	 * Muuttaa pyörän toiseksi
	 * @param alku muutettava pyörä
	 * @param uusi pyörä jolla alku korvataan
	  */
	public void muutaPyora(Pyora alku, Pyora uusi) {
		pyorat.muutaPyora(alku, uusi);
		
	}


	/**
	 * Poistaa valitun pyörän
	 * @param pyoraKohdalla valittu pyörä
	 * @return true jos onnistui, muuten false
	 */
	public boolean poistaPyora(Pyora pyoraKohdalla) {

		if (pyoraKohdalla == null) return false;
		boolean ret = pyorat.poista(pyoraKohdalla.getPyoranID());
		return ret;
	}

	
	/**
	 * Poistaa vuokraamosta parametrina annetun asiakkaan
	 * @param asiakas Poistettava asiakas
	 * @return True, jos poisto onnistui
	 */
	public boolean poistaAsiakas(Asiakas asiakas) {

		if (asiakas == null) return false;
		boolean ret = asiakkaat.poista(asiakas.getAsiakasId());
		return ret;
	}
	
	/**
	 * Poistaa vuokraamosta parametrina annetun asiakkaan
	 * @param asiakas Poistettava asiakas
	 * @return True, jos poisto onnistui
	 */
	public boolean poistaVuokraus(Vuokraus vuokraus) {

		if (vuokraus == null) return false;
		boolean ret = vuokraukset.poista(vuokraus.getPyoraId());
		return ret;
	}

	/**
	 * Testi ohjelmaa vuokramolle
	 * @param args ei käytösä
	 * @throws SailoException
	 */
	public static void main(String[] args) throws SailoException {
		Vuokraamo testi = new Vuokraamo();
		
		
		try {
			testi.lueTiedostosta("Testi");
		} catch (SailoException e) {
			System.out.println(e.getMessage());
		}

		Pyora p1 = new Pyora(), p2 = new Pyora(), p3 = new Pyora();
		p1.vastaaJopo();
		p2.vastaaJopo();
		p3.vastaaJopo();

		testi.lisaaPyora(p1);
		testi.lisaaPyora(p2);
		testi.lisaaPyora(p3);
		
		Asiakas juuu = new Asiakas();
		Vuokraus asd = new Vuokraus();
		juuu.vastaaHessuHopo();
		testi.lisaaAsiakas(juuu);
		asd.testiVuokraus(p1.getPyoranID(), 4);
		asd.setAsiakasId(juuu.getAsiakasId());
		
		testi.lisaaVuokraus(asd);
		
		Vuokraus v2 = new Vuokraus();
		v2.testiVuokraus(p2.getPyoranID(), 10);
		v2.setAsiakasId(juuu.getAsiakasId());
		
		testi.lisaaVuokraus(v2);

		System.out.println(testi.annaVuokraus(p1).toString());
		System.out.println(testi.annaVuokraus(p2).toString());
		
		testi.tallenna();
		
		
		for (Pyora p : testi.pyorat) {
			System.out.println(p);
		}
		
		testi = new Vuokraamo();
		testi.lueTiedostosta("Testi");
		
		Vuokraus v3 = new Vuokraus();
		v3.testiVuokraus(p3.getPyoranID(), 15);
		v3.setAsiakasId(juuu.getAsiakasId());
		testi.lisaaVuokraus(v3);
		testi.tallenna();

		/*
		Iterator<Pyora> iter1 = testi.pyorat.iterator();
		Iterator<Vuokraus> iter2 = testi.vuokraukset.iterator();

		System.out.println("<----- Pyörät ----->");
		while (iter1.hasNext()) {
			System.out.println(iter1.next().toString());
		}

		System.out.println("<----- Vuokraukset ----->");

		while (iter2.hasNext()) {
			System.out.println(iter2.next().toString());
		}
		*/
	}

}
