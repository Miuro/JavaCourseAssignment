package model;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

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
		pyorat.lisaa(pyora);
	}


	/**
	 * Lisää vuokrauksen vuokrauksiin
	 * @param vuokraus lisättävä vuokraus
	 */
	public void lisaaVuokraus(Vuokraus vuokraus) {
		Pyora temp = pyorat.anna(vuokraus.getPyoraId());
		if (temp.getOnkoVarattu() == true) return;
		//temp.rekisteroi();
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
		asiakkaat.lisaa(asiakas);
	}


	/**
	 * Antaa halutun pyörän vuokrauksen
	 * @param pyora pyörä jonka vuokrausta halutaan
	 * @return pyörän vuokraus, null jos ei ole vuokrausta
	 */
	public Vuokraus annaVuokraus(Pyora pyora) {
		if (pyora.getOnkoVarattu() == true) {
			Vuokraus v = vuokraukset.etsi(pyora.getPyoranID());
			return v;
		} else
			return null;
	}


	/**
	 * Antaa pyörien lukumäärän
	 * @return pyörien lukumäärä
	 */
	public int getPyoria() {
		return pyorat.getLkm();
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
	public Collection<Pyora> etsi(String hakuehto) throws SailoException {
		return pyorat.etsi(hakuehto);
	}


	/**
	 * Tallentaa tiedostot
	 * @throws SailoException
	 */
	public void tallenna() throws SailoException {
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

		Pyora p1 = new Pyora(), p2 = new Pyora();
		p1.rekisteroi();
		p1.vastaaJopo();
		p2.rekisteroi();
		p2.vastaaJopo();

		testi.lisaaPyora(p1);
		testi.lisaaPyora(p2);

		Asiakas a1 = new Asiakas();
		a1.rekisteroi();
		a1.vastaaHessuHopo();
		testi.lisaaAsiakas(a1);

		// int id1 = p1.getPyoranID();
		// int i2 = p2.getPyoranID();

		Vuokraus v1 = new Vuokraus();
		v1.rekisteroi();
		v1.testiVuokraus(p1.getPyoranID(), 5);
		testi.lisaaVuokraus(v1);

		testi.tallenna();

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

	}

}
