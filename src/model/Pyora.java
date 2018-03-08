package model;


/*
 * Pyorä-olio
 * 
 * 
 */
public class Pyora {
	private enum PyoranKunto {
		RIKKI, TYYDYTTAVA, HYVA, ERINOMAINEN
	}
	
	private int pyoranID;
	private String malli = "";
	private PyoranKunto kunto = PyoranKunto.ERINOMAINEN;
	private double vuokraPerPaiva = 0;
	private boolean vuokrauksenTila; // FALSE = vapaana, TRUE = vuokrattuna
	private String lisatietoja = "";
	
	
	public Pyora(String malli, PyoranKunto kunto, double vuokraPerPaiva) {
		this.malli = malli;
		this.kunto = kunto;
		this.vuokraPerPaiva = vuokraPerPaiva;
	}

	public static void main(String[] args) {
		Pyora testi = new Pyora("Helkama Jopo", PyoranKunto.ERINOMAINEN, 100);
	}
	
}


