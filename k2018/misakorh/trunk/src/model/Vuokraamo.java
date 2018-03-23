package model;

import java.io.File;

public class Vuokraamo {
	private final Pyorat pyorat = new Pyorat();
	private final Vuokraukset vuokraukset = new Vuokraukset();
	
	
	/**
	 * Antaa py�rien lukum��r�n
	 * @return py�rien lukum��r�
	 */
	public int getPyoria() {
		return pyorat.getLkm();
	}
	
	
	/**
	 * Lis�� uuden vuokrauksen kokoelmaan
	 * @param vuokraus lis�tt�v� vuokraus
	 * @throws SailoException jos t�ysi
	 */
	public void lisaaVuokraus (Vuokraus vuokraus) throws SailoException {
		vuokraukset.lisaa(vuokraus);
	}
	
	
	
	/**
	 * Antaa halutun py�r�n
	 * @param i halutun py�r�n indeksi
	 * @return haluttu py�r�
	 */
	public Pyora annaPyora(int i) {
		return pyorat.anna(i);
	}
	
	
	/**
	 * Lis�� py�r�n kokoelmaan
	 * @param pyora lis�tt�v� py�r�
	 * @throws SailoException jos menee yli
	 */
	public void lisaaPyora(Pyora pyora)throws SailoException {
		pyorat.lisaa(pyora);
	}
	
	public static void main(String[] args) {
		Vuokraamo testi = new Vuokraamo();
		try {
			Pyora p1 = new Pyora(), p2 = new Pyora();
			p1.rekisteroi();
			p1.vastaaJopo();
			p2.rekisteroi();
			p2.vastaaJopo();
		
			testi.lisaaPyora(p1);
			testi.lisaaPyora(p2);
			
			int id1 = p1.getPyoranID();
			int i2 = p2.getPyoranID();
			
			Asiakas a1 = new Asiakas();
			a1.vastaaHessuHopo();
			a1.rekisteroi();
			
			Vuokraus v1 = new Vuokraus();
			v1.testiVuokraus(5);
			
			
			
		} catch (SailoException e) {
			System.out.println(e.getMessage());
		}
	}
}