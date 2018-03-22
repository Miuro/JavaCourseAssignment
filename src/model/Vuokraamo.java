package model;

public class Vuokraamo {
	private final Pyorat pyorat = new Pyorat();
	private final Vuokraukset vuokraukset = new Vuokraukset();
	
	/**
	 * Antaa pyörien lukumäärän
	 * @return pyörien lukumäärä
	 */
	public int getPyoria() {
		return pyorat.getLkm();
	}
	
	
	/**
	 * Lisää uuden vuokrauksen kokoelmaan
	 * @param vuokraus lisättävä vuokraus
	 * @throws SailoException jos täysi
	 */
	public void lisaaVuokraus (Vuokraus vuokraus) throws SailoException {
		vuokraukset.lisaa(vuokraus);
	}
	
	
	/**
	 * Antaa halutun pyörän
	 * @param i halutun pyörän indeksi
	 * @return haluttu pyörä
	 */
	public Pyora annaPyora(int i) {
		return pyorat.anna(i);
	}
	
	
	/**
	 * Lisää pyörän kokoelmaan
	 * @param pyora lisättävä pyörä
	 * @throws SailoException jos menee yli
	 */
	public void lisaaPyora(Pyora pyora)throws SailoException {
		pyorat.lisaa(pyora);
	}
}
