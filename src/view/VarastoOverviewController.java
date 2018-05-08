package view;

import pyoravarasto.MainApp;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Asiakas;
import model.Pyora;
import model.SailoException;
import model.Vuokraus;
import model.Vuokraamo;

public class VarastoOverviewController {
	@FXML
	private MenuItem fxMenuTallenna;
	@FXML
	private Label labelVirhe;
	@FXML
	private TextField hakuehto;
	@FXML
	private MenuItem fxMenuTulosta;
	@FXML
	private MenuItem fxMenuLopeta;
	@FXML
	private MenuItem fxMenuUusiPyora;
	@FXML
	private MenuItem fxMenuPoistaPyora;
	@FXML
	private MenuItem fxMenuMuokkaaPyoraa;
	@FXML
	private MenuItem fxMenuApua;
	@FXML
	private MenuItem fxMenuTietoja;
	@FXML
	private Button fxUusiPyoraButton;
	@FXML
	private ScrollPane panelPyora;
	@FXML
	private Button fxTallennaButton;
	@FXML
	private Button fxVuokraaButton;
	@FXML
	private CheckBox fxVainVapaatCB;
	@FXML
	private CheckBox fxHinnanMukaanCB;
	@FXML
	private Button fxPoistaButton;
	@FXML
	private ListChooser<Pyora> fxChooserPyorat;
	@FXML
	private TextField textFieldNimi;
	@FXML
	private TextField textFieldMalli;
	@FXML
	private TextField textFieldKunto;
	@FXML
	private TextField textFieldVuokra;
	@FXML
	private TextField textFieldTila;
	@FXML
	private TextField textFieldLisatietoja;
    @FXML
    private Label textKunto;

	// Reference to the main application.
	private MainApp mainApp;

	/**
	 * Konstruktori T�t� vissiin kutsutaan kun tapahtuu automaattinen initialize() metodi fxml-tiedoston ladattua. Dunno
	 * tbh
	 */
	public VarastoOverviewController() {

	}


	/**
	 * Alustaa
	 */
	@FXML
	private void initialize() {
		alusta();
	}


	/**
	 * T�t� kutsutaan MainApp:sta. M��ritt��, ett� t�m� on perusn�kym�-
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}


	/**
	 * Muokkaa py�r�� napin handl�ys
	 */
	@FXML
	void handleMuokkaaPyoraa() {
		muokkaaPyora();
	}


	/**
	 * Hakuehdon h�ndl�ys
	 */
	@FXML
	void handleHakuehto() {
		if (pyoraKohdalla != null) hae(pyoraKohdalla.getPyoranID());
	}


	/**
	 * H�ndl�� Apua painikkeen
	 */
	@FXML
	void handleAvaaApua() {
		avaaApua();
	}


	/**
	 * H�ndl�� Tietoja painikkeen
	 */
	@FXML
	void handleAvaaTietoja() {
		Dialogs.showMessageDialog("Py�r�Varasto\nVer. 0.6\nTekij�t: Jouko Sirkka & Miro Korhonen");
	}


	/**
	 * H�ndl�� lopeta painikkeen
	 */
	@FXML
	void handleMenuLopeta() {
		Dialogs.showMessageDialog("Ei ole viel� lis�tty");
	}


	/**
	 * H�ndl�� tulosta-painikkeen
	 */
	@FXML
	void handleMenuTulosta() {
		UusiTulostusDialogController tulostusDialog  = UusiTulostusDialogController.tulosta(null);
		tulostaValitut(tulostusDialog.getTextArea());
	}


	/**
	 * H�ndl�� poistapy�r� painikkeen
	 */
	@FXML
	void handlePoistaPyora() {
		poistaPyora();
		tyhjennaKentat();
	}


	/**
	 * H�ndl�� tallennus painikkeen
	 */
	@FXML
	void handleTallenna() {
		tallenna();
	}


	/**
	 * H�ndl�� uusipy�r� painikkeen
	 * @throws SailoException 
	 */
	@FXML
	void handleUusiPyora() throws SailoException {
		uusiPyora();
	}


	/**
	 * Avataan vuokrausikkuna.
	 * @throws SailoException
	 */
	@FXML
	void handleUusiVuokraus() throws SailoException {
		
		if (pyoraKohdalla == null) {
			Dialogs.showMessageDialog("Valitse vuokrattava py�r�");
			return;
		}
		if (pyoraKohdalla.getOnkoVarattu()) {
			apuVuokraus = vuokraamo.annaVuokraus(pyoraKohdalla);
			apuAsiakas = vuokraamo.annaAsiakas(apuVuokraus);
			mainApp.showUusiVuokrausDialog(pyoraKohdalla, apuVuokraus, apuAsiakas, vuokraamo);
			hae(pyoraKohdalla.getPyoranID());
			
		} else {
			apuAsiakas = new Asiakas();
			apuVuokraus = new Vuokraus();
			mainApp.showUusiVuokrausDialog(pyoraKohdalla, apuVuokraus, apuAsiakas, vuokraamo);
			hae(pyoraKohdalla.getPyoranID());
		}

		
	}


	/**
	 * H�ndl�� tilanteen jossa checkboxia "Vain vapaat" painetaan
	 */
	@FXML
	void handleVainVapaatCB() {
		hae(pyoraKohdalla.getPyoranID());
	}


	/**
	 * H�ndl�� tilanteen jossa checkboxia "hinnan mukaan" painetaan
	 */
	@FXML
	void handleHinnanMukaan() {
		hae(pyoraKohdalla.getPyoranID());
	}

	//===============================================================================
	// FXML:ll��n kuulumaton koodi t�st� eteenp�in

	private String vuokraamonNimi = "MJVuokraamo";
	private Vuokraamo vuokraamo;
	private Pyora pyoraKohdalla;
	private Asiakas apuAsiakas;
	private Vuokraus apuVuokraus;
	private boolean muokattavana = false;


	/**
	 * Alustetaan
	 */
	protected void alusta() {
		panelPyora.setFitToHeight(true);
		fxChooserPyorat.clear();
		fxChooserPyorat.addSelectionListener(e -> naytaPyora());

		setVuokraamo(new Vuokraamo());
		avaa();
	}

	
	/**
	 * Muokkaa t�ll� hetkell� valittuna olevaa py�r��
	 */
	private void muokkaaPyora() {
		if (muokattavana) 
			return;
		
		// asetetaan py�r�n kunto field tekstist� numeroksi ja muutetaan labelia
		textFieldKunto.setText(Integer.toString(pyoraKohdalla.getKunto()));
		textKunto.setText("Kunto v�lil� 0-3");
		muokattavana = true;
		vaihdaMuokattavuus(true);
	}
	
	/**
	 * Avaa ty�n dokumentaation netist�
	 */
	private void avaaApua() {
		Desktop desktop = Desktop.getDesktop();
		try {
			URI uri = new URI("https://www.mit.jyu.fi/demowww/ohj2/ht18/misakorh/trunk/");
			desktop.browse(uri);
		} catch (URISyntaxException e) {
			return;
		} catch (IOException e) {
			return;
		}
	}

	
	/**
	 * N�ytt�� listasta valitun py�r�n tiedot, tilap�isesti yhteen isoon edit-kentt��n
	 */
	protected void naytaPyora() {
		if (muokattavana) muokattavana = false;

		
		pyoraKohdalla = fxChooserPyorat.getSelectedObject();
		if (pyoraKohdalla == null) return;

		apuVuokraus = null;
		if (pyoraKohdalla.getOnkoVarattu() == true) {
			apuVuokraus = vuokraamo.annaVuokraus(pyoraKohdalla);
			apuAsiakas = vuokraamo.annaAsiakas(apuVuokraus);
			fxVuokraaButton.setText("N�yt� vuokraus");
		} else
			fxVuokraaButton.setText("Vuokraa");

		taytaKentat();
	}


	/**
	 * T�ytt�� py�r�n kent�t py�r�n tiedoilla
	 */
	private void taytaKentat() {
		textFieldNimi.setText(pyoraKohdalla.anna(1));
		textFieldMalli.setText(pyoraKohdalla.anna(2));
		textFieldKunto.setText(pyoraKohdalla.anna(3));
		textFieldVuokra.setText(pyoraKohdalla.anna(6));
		textFieldTila.setText(vuokrattunaString(Boolean.parseBoolean(pyoraKohdalla.anna(5))));
		textFieldLisatietoja.setText(pyoraKohdalla.anna(4));
		vaihdaMuokattavuus(false);
	}
	
	
	/**
	 * Muuttaa py�r�n tilan merkkijonoksi vapaa tai varattu riippuen booleanista tila
	 * @param bool py�r�n tila
	 * @return vapaa jos false, true jos vuokrattuna
	 */
	private String vuokrattunaString(boolean bool) {
		if (bool) return "Vuokrattuna";
		return "Vapaa";
	}
	
	/**
	 * Muuttaa k�yttiksest� "varattuna" -> true ja vapaan -> falseksi
	 * @param str tutkittava merkkijono
	 * @return true jos vuokrattuna false jos vapaa
	 */
	private boolean vuokrattunaBool(String str) {
		if (str.equals("Vuokrattuna")) return true;
		return false;
	}
	
	/**
	 * Tyjent�� py�r�n kent�t
	 */
	private void tyhjennaKentat() {
		textFieldNimi.clear();
		textFieldMalli.clear();
		textFieldKunto.clear();
		textFieldVuokra.clear();
		textFieldTila.clear();
		textFieldLisatietoja.clear();
	}


	/**
	 * Muuttaa py�r�n tekstikenttien muokattavuuden truesta falseksi tai toisinp�in
	 */
	private void vaihdaMuokattavuus(boolean muokataanko) {
		boolean muokattavaksi = muokataanko;
		textFieldNimi.setEditable(muokattavaksi);
		textFieldMalli.setEditable(muokattavaksi);
		textFieldKunto.setEditable(muokattavaksi);
		textFieldVuokra.setEditable(muokattavaksi);
		//textFieldTila.setEditable(muokattavaksi);
		textFieldLisatietoja.setEditable(muokattavaksi);
	}


	/**
	 * Alustaa vuokraamon lukemalla sen valitun nimisest� tiedostosta
	 * @param nimi tiedosto josta vuokraamon tiedot luetaan
	 * @return null jos onnistuu, muuten virhe tekstin�
	 */
	protected String lueTiedosto(String nimi) {
		vuokraamonNimi = nimi;
		try {
			vuokraamo.lueTiedostosta(nimi);
			hae(0);
			return null;
		} catch (SailoException e) {
			hae(0);
			String virhe = e.getMessage();
			if (virhe != null) Dialogs.showMessageDialog(virhe);
			return virhe;
		}
	}


	/**
	 * Kysyt��n tiedoston nimi ja luetaan se
	 * @return true jos onnistui, false jos ei
	 */
	public boolean avaa() {
		lueTiedosto(vuokraamonNimi);
		return true;
	}


	/**
	 * Tietojen tallennus
	 * @return null jos onnistuu, muuten virhe tekstin�
	 */
	private String tallenna() {	
		if(muokattavana == true) {
			try {
				pyoraKohdalla.aseta(1, textFieldNimi.getText());
				pyoraKohdalla.aseta(2, textFieldMalli.getText());
				pyoraKohdalla.aseta(3, textFieldKunto.getText());
				pyoraKohdalla.aseta(4, textFieldVuokra.getText());
				pyoraKohdalla.aseta(5, Boolean.toString(vuokrattunaBool(textFieldTila.getText())));
				pyoraKohdalla.aseta(6, textFieldLisatietoja.getText());
				vuokraamo.poistaPyora(pyoraKohdalla);
				vuokraamo.lisaaPyora(pyoraKohdalla);
			} catch (SailoException e) {
				Dialogs.showMessageDialog(e.getMessage());
				hae(pyoraKohdalla.getPyoranID());
				return e.getMessage();
			}
			// asetetaan py�r�n kunto taas tekstiksi ja label takaisin
			textFieldKunto.setText(pyoraKohdalla.anna(3));
			textKunto.setText("Kunto");
			hae(pyoraKohdalla.getPyoranID());
			muokattavana = false;
			vaihdaMuokattavuus(false);
		}
		try {
			vuokraamo.tallenna();
			return null;
		} catch (SailoException ex) {
			Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
			return ex.getMessage();
		}
	}


	/**
	 * Tarkistetaan onko tallennus tehty
	 * @return true jos saa sulkea sovelluksen, false jos ei
	 */
	public boolean voikoSulkea() {
		tallenna();
		return true;
	}
	
	/**
	 * Tulostaa listassa olevat py�r�t tekstialueeseen
	 * @param text alue johon tulostetaan
	 */
	public void tulostaValitut(TextArea text) {
		try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
			os.println("Valitut py�r�t");
			for(Pyora pyora : fxChooserPyorat.getObjects()) {
				tulosta(os, pyora);
				os.println("\n\n");
			}
		}
	} 


	/**
	 * Luo uuden j�senen jota aletaan editoimaan
	 * @throws SailoException 
	 */
	protected void uusiPyora() throws SailoException {
		Pyora uusi = new Pyora();
		uusi.aseta(1, "tyhj�");
		
		try {
			vuokraamo.lisaaPyora(uusi);
		} catch (SailoException e) {
			Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
			return;
		}
		hae(uusi.getPyoranID());
		muokattavana = true;
		vaihdaMuokattavuus(true);
	}


	/**
	 * Poistaa halutun py�r�n
	 */
	private void poistaPyora() {
		if (pyoraKohdalla == null) return;
		if (!Dialogs.showQuestionDialog("Poisto", "Poistetaanko py�r�: " + pyoraKohdalla.getNimi(), "Kyll�", "Ei"))
			return;
		vuokraamo.poistaPyora(pyoraKohdalla);
		int index = fxChooserPyorat.getSelectedIndex();
		hae(0);
		fxChooserPyorat.setSelectedIndex(index);
	}


	/**
	 * Tekee vuokrauksen valitulle py�r�lle.
	 */
	public void vuokraaPyora(int kesto) throws SailoException {
		if (pyoraKohdalla == null) return;
		Vuokraus vuokraus = new Vuokraus();
		//vuokraus.rekisteroi();
		vuokraus.testiVuokraus(pyoraKohdalla.getPyoranID(), kesto);
		vuokraamo.lisaaVuokraus(vuokraus);
		pyoraKohdalla.setOnkoVarattu(true);
		hae(pyoraKohdalla.getPyoranID());
	}


	/**
	 * Hakee j�senten tiedot listaan
	 * @param jnro j�senen numero, joka aktivoidaan haun j�lkeen
	 */
	protected void hae(int pyoraID) {
		String ehto = hakuehto.getText();
		fxChooserPyorat.clear();

		int index = 0;
		Collection<Pyora> pyorat;
		try {
			pyorat = vuokraamo.etsi(ehto, fxVainVapaatCB.isSelected());
			if (fxHinnanMukaanCB.isSelected()) pyorat = vuokraamo.hinnanMukaan(pyorat);
			int i = 0;
			for (Pyora pyora : pyorat) {
				if (pyora.getPyoranID() == pyoraID) index = i;
				fxChooserPyorat.add(pyora.getNimi(), pyora);
				i++;
			}
		} catch (SailoException ex) {
			Dialogs.showMessageDialog("Py�r�n hakemisessa ongelmia! " + ex.getMessage());
		}
		fxChooserPyorat.setSelectedIndex(index);
	}



	/**
	 * @param vuokraamo Vuokraamo jota k�ytet��n t�ss� k�ytt�liittym�ss�
	 */
	public void setVuokraamo(Vuokraamo vuokraamo) {
		this.vuokraamo = vuokraamo;
		naytaPyora();
	}


	/**
	 * Tulostaa py�r�n tiedot.
	 * @param os Tietovirta, mihin tulostetaan
	 * @param pyora Tulostettava py�r�
	 */
	private void tulosta(PrintStream os, Pyora pyora) {
		os.println("----------------------------------------------");
		pyora.tulosta(os);
		if (pyora.getOnkoVarattu()) {
			os.println("----------------------------------------------");
			vuokraamo.annaVuokraus(pyora).tulosta(os);
			vuokraamo.annaAsiakas(vuokraamo.annaVuokraus(pyora)).tulosta(os);
		}

		os.println("----------------------------------------------");
	}


	/**
	 * Testihommia
	 * @param args ei k�yt�s�
	 */
	public static void main(String[] args) {

	}

}