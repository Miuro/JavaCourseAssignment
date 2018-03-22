package model;

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
}
