package model;

public class Vuokraamo {
	private final Pyorat pyorat = new Pyorat();
	private final Vuokraukset vuokraukset = new Vuokraukset();
	
	/**
	 * 
	 * @return
	 */
	public int getPyoria() {
		return pyorat.getLkm();
	}
	
	
	/**
	 * 
	 * @param vuokraus
	 * @throws SailoException
	 */
	public void lisaaVuokraus (Vuokraus vuokraus) throws SailoException {
		vuokraukset.lisaa(vuokraus);
	}
	
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public Pyora annaPyora(int i) {
		return pyorat.anna(i);
	}
	
	
	/**
	 * 
	 * @param pyora
	 * @throws SailoException
	 */
	public void lisaaPyora(Pyora pyora)throws SailoException {
		pyorat.lisaa(pyora);
	}
}
