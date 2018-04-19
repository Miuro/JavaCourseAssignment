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
import java.util.NoSuchElementException;


public class Asiakkaat implements Iterable<Asiakas> {

	private static final int MAX_ASIAKKAITA = 5;
	private int lkm = 0;
	private String tiedostonPerusNimi = "asiakkaat";
	private Asiakas alkiot[] = new Asiakas[MAX_ASIAKKAITA];
	private boolean muutettu = false;


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
	 * Palauttaa tiedoston nimen jota k�ytet��n tallennukseen
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
	 * Palauttaa tiedoston nimen jota k�ytet��n tallennukseen ilman p��tett�
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonPerusNimi() {
		return this.tiedostonPerusNimi;
	}


	/**
	 * Asettaa tiedoston nimen ilman p��tett�
	 * @param tied haluttu tiedoston nimi
	 */
	public void setTiedostonPerusNimi(String tied) {
		this.tiedostonPerusNimi = tied;
	}


	/**
	 * Lis�� uuden asiakkaan tietorakenteeseensa. Ottaa asiakkaan omistukseensa
	 * @param asiakas lis�tt�v�n asiakkaan viite. Huom. tietorakenne muuttuu omistajaksi
	 * @throws SailoException jos tietorakenne on jo t�ynn�
	 */
	public void lisaa(Asiakas asiakas) /* throws SailoException */ {
		if (lkm >= alkiot.length) {
			Asiakas[] isompi = new Asiakas[alkiot.length + 5];
			for (int i = 0; i < alkiot.length; i++) {
				isompi[i] = alkiot[i];
			}
			alkiot = isompi;
		}
		//throw new SailoException("Liikaa alkioita");
		asiakas.rekisteroi();
		muutettu = true;
		alkiot[lkm] = asiakas;
		lkm++;
	}


	/**
	 * Poistaa asiakkaan jonka id on sama kuin parametrina tuodun asiakkaan id
	 * @param asiakas Asiakas
	 * @return true jos poisto onnistui
	 */
	public boolean poista(Asiakas asiakas) {
		for (int i = 0; i < alkiot.length; i++) {
			if(alkiot[i] == null) 
				continue;
			if (alkiot[i].getAsiakasId() == asiakas.getAsiakasId()) {
				alkiot[i] = null;
				lkm--;
				
				return true;
			}
		}
		return false;
	}


	/**
	 * Palauttaa viitteen i:teen asiakkaaseen
	 * @param i monennenko asiakkaan viite halutaan
	 * @return viite asiakkaaseen jonka indeksi on i
	 * @throws IndexOutOfBoundsException jos indeksi i ei ole sallitulla alueella
	 */
	public Asiakas anna(int i) throws IndexOutOfBoundsException {
		if (i < 0 || lkm <= i) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		return alkiot[i];
	}


	/**
	 * Lukee asiakkaat tiedostosta
	 * @param tied tiedoston nimi
	 * @throws FileNotFoundException jos ei aukea
	 * @throws IOException jos ongelmia tiedoston kanssa
	 */
	public void lueTiedostosta(String tied) throws SailoException {
		setTiedostonPerusNimi(tied);

		try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {
			String rivi;
			while ((rivi = fi.readLine()) != null) {
				rivi = rivi.trim();
				if ("".equals(rivi) || rivi.charAt(0) == ';') continue;
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
	 * Palauttaa asiakkaiden lukum��r�n
	 * @return asiakkaiden lukum��r�
	 */
	public int getLkm() {
		return lkm;
	}


	/**
	 * Testiohjelma asiakkaille
	 * @param args ei k�yt�s�
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
			
		testi.poista(a1);
		
		for (Asiakas asiakas : testi) 
			System.out.println(asiakas);


	}


	@Override
	public Iterator<Asiakas> iterator() {
		return Arrays.asList(alkiot).iterator();
	}
}