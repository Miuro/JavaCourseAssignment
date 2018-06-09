package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;

// HUOM. Asiakkaan poistoa ei ole toteutettu ohjelmassa. Koodi ei v‰ltt‰m‰tt‰ toimi.

/**
 * Tietorakenne asiakkaille
 * @author Jouko Sirkka, Miro Korhonen
 * @version 1.0, 15.5.2018
 */
public class Asiakkaat implements Iterable<Asiakas> {

	private static final int MAX_ASIAKKAITA = 5;
	private int lkm = 0;
	private String tiedostonPerusNimi = "asiakkaat";
	private Asiakas alkiot[] = new Asiakas[MAX_ASIAKKAITA];
	private boolean muutettu = false;


	/**
	 * Etsii halutun asiakkaan
	 * @param vuokraajaID asiakkaanId
	 * @return haluttu asiakas tai null jos ei ole
	 * <pre name="test">
	 * Asiakkaat testi = new Asiakkaat();
	 * Asiakas a1 = new Asiakas();
	 * a1.vastaaHessuHopo();
	 * a1.setAsiakasId(4);
	 * Asiakas a2 = new Asiakas();
	 * a2.vastaaHessuHopo();
	 * a2.setAsiakasId(5);
	 * a2.aseta(1, "Aku Ankka");
	 * testi.lisaa(a1);
	 * testi.lisaa(a2);
	 * testi.etsi(4) === a1;
	 * testi.etsi(5) === a2;
	 * testi.etsi(6) === null;
	 * </pre>
	 */
	public Asiakas etsi(int vuokraajaID) {
		for (Asiakas asiakas : alkiot) {
			if (asiakas != null) 
				if (asiakas.getAsiakasId() == vuokraajaID) 
					return asiakas;
		}
		return null;
	}


	/**
	 * Oletusmuodostaja
	 */
	public Asiakkaat() {
		
	}


	/**
	 * Tallentaa asiakkaan tiedostoon.
	 * @throws SailoException jos tallennuse ep‰onnistuu.
	 */
	public void tallenna() throws SailoException {
		if (!muutettu) return;

		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete();
		ftied.renameTo(fbak);

		try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
			for (Asiakas asiakas : alkiot) {
				if(asiakas != null) {
					fo.println(asiakas);
				}
			}
			
		} catch (FileNotFoundException ex) {
			throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
		} catch (IOException ex) {
			throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
		}
		muutettu = false;
	}


	/**
	 * Palauttaa tiedoston nimen jota k‰ytet‰‰n tallennukseen
	 * @return Tiedoston nimi.
	 */
	public String getTiedostonNimi() {
		return tiedostonPerusNimi + ".dat";
	}


	/**
	 * Palauttaa varakopiotiedoston nimen
	 * @return varakopiotiedoston nimi
	 */
	public String getBakNimi() {
		return this.tiedostonPerusNimi + ".bak";
	}


	/**
	 * Palauttaa tiedoston nimen jota k‰ytet‰‰n tallennukseen ilman p‰‰tett‰
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonPerusNimi() {
		return this.tiedostonPerusNimi;
	}


	/**
	 * Asettaa tiedoston nimen ilman p‰‰tett‰
	 * @param tied haluttu tiedoston nimi
	 */
	public void setTiedostonPerusNimi(String tied) {
		this.tiedostonPerusNimi = tied;
	}


	/**
	 * Lis‰‰ uuden asiakkaan tietorakenteeseensa. Ottaa asiakkaan omistukseensa
	 * @param asiakas lis‰tt‰v‰n asiakkaan viite. Huom. tietorakenne muuttuu omistajaksi
	 * @example
	 * <pre name="test">
	 * Asiakkaat asiakkaat = new Asiakkaat();
	 * Asiakas a1 = new Asiakas();
	 * a1.vastaaHessuHopo();
	 * asiakkaat.getLkm() === 0;
	 * asiakkaat.lisaa(a1);
	 * asiakkaat.getLkm() === 1;
	 * </pre>
	 */
	public void lisaa(Asiakas asiakas) {
		if (lkm >= alkiot.length) {
			Asiakas[] isompi = new Asiakas[alkiot.length + 5];
			for (int i = 0; i < alkiot.length; i++) {
				isompi[i] = alkiot[i];
			}
			alkiot = isompi;
		}
		//asiakas.rekisteroi();
		alkiot[lkm] = asiakas;
		lkm++;
		muutettu = true;
	}


	/**
	 * Poistaa asiakkaan jonka id on sama kuin parametrina tuodun asiakkaan id
	 * @param asiakasID Poistettavan asiakkaan id
	 * @return true jos poisto onnistui
	 * <pre name="test">
	 * Asiakkaat testi = new Asiakkaat();
	 * Asiakas a1 = new Asiakas();
	 * a1.vastaaHessuHopo();
	 * a1.setAsiakasId(1);
	 * Asiakas a2 = new Asiakas();
	 * a2.vastaaHessuHopo();
	 * a2.setAsiakasId(2);
	 * a2.aseta(1, "Aku Ankka");
	 * testi.lisaa(a1);
	 * testi.lisaa(a2);
	 * testi.etsi(1) === a1;
	 * testi.etsi(2) === a2;
	 * testi.etsi(3) === null;
	 * testi.poista(a1.getAsiakasId()) === true;
	 * </pre>
	 */
	/*public boolean poista(int asiakasID) {
		for (int i = 0; i < alkiot.length; i++) {
			if(alkiot[i] == null) 
				continue;
			if (alkiot[i].getAsiakasId() == asiakasID) {
				alkiot[i] = null;
				lkm--;
				
				return true;
			}
		}
		return false;
	}*/


	/**
	 * Palauttaa viitteen i:teen asiakkaaseen
	 * @param i monennenko asiakkaan viite halutaan
	 * @return viite asiakkaaseen jonka indeksi on i
	 * @throws IndexOutOfBoundsException jos indeksi i ei ole sallitulla alueella
	 * @example
	 * <pre name="test">
	 * 
	 * </pre>
	 */
	/*public Asiakas anna(int i) throws IndexOutOfBoundsException {
		if (i < 0 || lkm <= i) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		return alkiot[i];
	}*/


	/**
	 * Lukee asiakkaat tiedostosta
	 * @param tied tiedoston nimi
	 * @throws SailoException jos luku ep‰onnistuu.
	 */
	public void lueTiedostosta(String tied) throws SailoException {
		setTiedostonPerusNimi(tied);

		try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {
			String rivi;
			while ((rivi = fi.readLine()) != null) {
				rivi = rivi.trim();
				if ( "".equals(rivi) || rivi.charAt(0) == ';') continue;
				Asiakas asiakas = new Asiakas();
				asiakas.parse(rivi);
				lisaa(asiakas);
			}
			muutettu = false;

		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
		} catch (IOException e) {
			throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
		}
	}


	/**
	 * Luetaan aikaisemmin annetusta tiedostosta
	 * @throws SailoException jos tulee poikkeus
	 */
	public void lueTiedostosta() throws SailoException {
		lueTiedostosta(getTiedostonPerusNimi());
	}


	/**
	 * Palauttaa asiakkaiden lukum‰‰r‰n
	 * @return asiakkaiden lukum‰‰r‰
	 * <pre name="test">
	 * Asiakkaat testi2 = new Asiakkaat();
	 * Asiakas a6 = new Asiakas();
	 * a6.vastaaHessuHopo();
	 * testi2.getLkm() === 0;
	 * testi2.lisaa(a6);
	 * testi2.getLkm() === 1;
	 * </pre>
	 */
	public int getLkm() {
		return lkm;
	}


	/**
	 * Testiohjelma asiakkaille
	 * @param args ei k‰ytˆs‰
	 */
	public static void main(String[] args) {
		Asiakkaat testi = new Asiakkaat();
		Asiakas a1 = new Asiakas();
		a1.vastaaHessuHopo();
		Asiakas a2 = new Asiakas();
		a2.vastaaHessuHopo();
		a2.aseta(1, "Aku Ankka");
		
		
		testi.lisaa(a1);
		testi.lisaa(a2);

		for (Asiakas asiakas : testi) 
			System.out.println(asiakas);
			
		//testi.poista(a1);
		
		for (Asiakas asiakas : testi) 
			System.out.println(asiakas);


	}

	/**
	 * Luo iteraattorin asiakkaille
	 */
	@Override
	public Iterator<Asiakas> iterator() {
		return Arrays.asList(alkiot).iterator();
	}
}