package view;

import pyoravarasto.MainApp;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
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

/**
 * Ohjelman p��ikkunan controller. Sis�lt�� ohjaimet ohjelman k�ytt�miseen.
 * @author Jouko Sirkka, Miro Korhonen
 * @version 1.2.1, 23.5.2018
 */
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
	 * @param mainApp mainApp
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
		Dialogs.showMessageDialog("Py�r�Varasto\nVer. 1.2\nTekij�t: Jouko Sirkka & Miro Korhonen");
	}


	/**
	 * H�ndl�� lopeta painikkeen
	 */
	@FXML
	void handleMenuLopeta() {
		voikoSulkea();
	}


	/**
	 * H�ndl�� tulosta-painikkeen
	 */
	@FXML
	void handleMenuTulosta() {
		UusiTulostusDialogController tulostusDialog = UusiTulostusDialogController.tulosta(null);
		tulostaValitut(tulostusDialog.getTextArea());
	}


	/**
	 * H�ndl�� poistapy�r� painikkeen
	 */
	@FXML
	void handlePoistaPyora() {
		poistaPyora();
		//tyhjennaKentat();
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

		uusiVuokraus();

	}


	/**
	 * H�ndl�� tilanteen jossa checkboxia "Vain vapaat" painetaan
	 */
	@FXML
	void handleVainVapaatCB() {
		hae(0);
	}


	/**
	 * H�ndl�� tilanteen jossa checkboxia "hinnan mukaan" painetaan
	 */
	@FXML
	void handleHinnanMukaan() {
		hae(0);
	}

	//==================================================================================
	// FXML:ll��n kuulumaton koodi t�st� eteenp�in

	private String vuokraamonNimi = "MJVuokraamo";
	private Vuokraamo vuokraamo;
	private Pyora pyoraKohdalla;
	private Asiakas apuAsiakas;
	private Vuokraus apuVuokraus;
	private boolean muokattavana = false;
	private List<TextField> tekstikentat;


	/**
	 * Alustetaan
	 */
	protected void alusta() {
		panelPyora.setFitToHeight(true);
		fxChooserPyorat.clear();
		fxChooserPyorat.addSelectionListener(e -> naytaPyora());

		tekstikentat = List.<TextField>of(textFieldNimi, textFieldMalli, textFieldKunto, textFieldVuokra, textFieldTila,
				textFieldLisatietoja);

		setVuokraamo(new Vuokraamo());
		avaa();
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
	 * N�ytt�� listasta valitun py�r�n tiedot
	 */
	protected void naytaPyora() {
		if (muokattavana) {
			boolean vastaus = Dialogs.showQuestionDialog("Tallennus", "Tallenetaanko nykyinen py�r�?", "Kyll�", "Ei");
			if (vastaus) {
				tallenna();
				muokattavana = false;
				vaihdaMuokattavuus(false);
			} else {
				vuokraamo.poistaPyora(pyoraKohdalla);
				muokattavana = false;
				vaihdaMuokattavuus(false);
				hae(0);
			}
		}
		

		pyoraKohdalla = fxChooserPyorat.getSelectedObject();
		if (pyoraKohdalla == null) {
			return;
		}

		if (pyoraKohdalla.getOnkoVarattu() == true) {
			apuVuokraus = vuokraamo.annaVuokraus(pyoraKohdalla);
			apuAsiakas = vuokraamo.annaAsiakas(apuVuokraus);
			// jos py�r� on merkattu varatuksi mutta vuokrausta tai asiakasta ei l�ydy
			// niin asetetaan varattu falseksi
			if (apuVuokraus == null || apuAsiakas == null) {
				pyoraKohdalla.setOnkoVarattu(false);
				return;
			}

			fxVuokraaButton.setText("N�yt� vuokraus");
		} else
			fxVuokraaButton.setText("Vuokraa");

		taytaKentat(pyoraKohdalla);
	}


	/**
	 * T�ytt�� py�r�n kent�t py�r�n tiedoilla
	 */
	private void taytaKentat(Pyora pyora) {
		textFieldNimi.setText(pyora.anna(1));
		textFieldMalli.setText(pyora.anna(2));
		textFieldKunto.setText(pyora.anna(3));
		textFieldVuokra.setText(pyora.anna(6));
		textFieldTila.setText(vuokrattunaString(Boolean.parseBoolean(pyora.anna(5))));
		textFieldLisatietoja.setText(pyora.anna(4));
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
			return virhe;
		}
	}


	/**
	 * Kysyt��n tiedoston nimi ja luetaan se
	 * @return true jos onnistui, false jos ei
	 */
	public boolean avaa() {
		String virhe = lueTiedosto(vuokraamonNimi);
		if (virhe == null)
			return true;
		return false;
	}


	/**
	 * Tarkistetaan onko tallennus tehty
	 */
	public void voikoSulkea() {
		if (muokattavana == true) {
			boolean vastaus = Dialogs.showQuestionDialog("Tallennus", "Tallenetaanko ennen sulkemista?", "Kyll�", "Ei");
			if (vastaus) {
				tallenna();
			}
		}
		Platform.exit();
	}


	/**
	 * Tulostaa listassa olevat py�r�t tekstialueeseen
	 * @param text alue johon tulostetaan
	 */
	public void tulostaValitut(TextArea text) {
		try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
			os.println("Valitut py�r�t");
			for (Pyora pyora : fxChooserPyorat.getObjects()) {
				tulosta(os, pyora);
				os.println("\n\n");
			}
		}
	}


	/**
	 * Muuttaa py�r�n tekstikenttien muokattavuuden truesta falseksi tai toisinp�in
	 * Asettaa my�s kenttien opacityn tummemmaksi tai kirkkaammaksi riippuen tilasta.
	 */
	private void vaihdaMuokattavuus(boolean muokataanko) {
	    
	    double opacityFull = 1.0;
	    double opacityHalf = 0.7;
	    
		boolean muokattavaksi = muokataanko;
		textFieldNimi.setEditable(muokattavaksi);
		textFieldMalli.setEditable(muokattavaksi);
		textFieldKunto.setEditable(muokattavaksi);
		textFieldVuokra.setEditable(muokattavaksi);
		textFieldLisatietoja.setEditable(muokattavaksi);
		
		if(muokattavaksi == true) {
		    textFieldNimi.setOpacity(opacityFull);
		    textFieldMalli.setOpacity(opacityFull);
		    textFieldKunto.setOpacity(opacityFull);
		    textFieldVuokra.setOpacity(opacityFull);
		    textFieldLisatietoja.setOpacity(opacityFull);
		}
		else {
		    textFieldNimi.setOpacity(opacityHalf);
            textFieldMalli.setOpacity(opacityHalf);
            textFieldKunto.setOpacity(opacityHalf);
            textFieldVuokra.setOpacity(opacityHalf);
            textFieldLisatietoja.setOpacity(opacityHalf);
		}
	}


	/**
	 * Luo ikkunan uuden vuokrauksen luontia varten.
	 * @throws SailoException jos virhe tapahtuu vuokrauksen luonnissa.
	 */
	public void uusiVuokraus() throws SailoException {
		if (pyoraKohdalla == null) {
			Dialogs.showMessageDialog("Valitse vuokrattava py�r�");
			return;
		}
		
		if(muokattavana == true) {
			Dialogs.showMessageDialog("Tallenna py�r� ensin.");
			return;
		}
		
		if (pyoraKohdalla.getOnkoVarattu()) {
			int palautus = mainApp.showUusiVuokrausDialog(pyoraKohdalla, apuVuokraus, apuAsiakas);
			// jos vuokraus poistettiin (eli dialogi palautti), poistetaan vuokraus ja asiakas.
			if (palautus == -1) {
				vuokraamo.poistaVuokraus(apuVuokraus);
				pyoraKohdalla.setOnkoVarattu(false);
			}
			hae(pyoraKohdalla.getPyoranID());
		} else {
			apuAsiakas = new Asiakas();
			apuVuokraus = new Vuokraus();
			int palautus = mainApp.showUusiVuokrausDialog(pyoraKohdalla, apuVuokraus, apuAsiakas);
			if (palautus == 1) {
				vuokraamo.lisaaAsiakas(apuAsiakas);
				apuVuokraus.setAsiakasId(apuAsiakas.getAsiakasId());
				apuVuokraus.setPyoraId(pyoraKohdalla.getPyoranID());
				vuokraamo.lisaaVuokraus(apuVuokraus);
			}

			hae(pyoraKohdalla.getPyoranID());
		}
	}


	/**
	 * Muokkaa t�ll� hetkell� valittuna olevaa py�r��
	 */
	private void muokkaaPyora() {
		if (pyoraKohdalla.getOnkoVarattu() == true) {
			Dialogs.showMessageDialog("Vuokrattua py�r�� ei voida muokata.");
			return;
		}

		// asetetaan py�r�n kunto field tekstist� numeroksi ja muutetaan labelia
		textFieldKunto.setText(Integer.toString(pyoraKohdalla.getKunto()));
		textKunto.setText("Kunto v�lil� 0-3");
		muokattavana = true;
		vaihdaMuokattavuus(true);
		textFieldNimi.requestFocus();
		textFieldNimi.selectEnd();
	}


	/**
	 * Luo uuden py�r�n jota aletaan editoimaan
	 * @throws SailoException jos virhe tapahtuu uuden luomisessa.
	 */
	protected void uusiPyora() throws SailoException {
		if (muokattavana) {
			boolean vastaus = Dialogs.showQuestionDialog("Tallennus", "Tallenetaanko nykyinen py�r�?", "Kyll�", "Ei");
			if (vastaus) {
				tallenna();
				muokattavana = false;
				vaihdaMuokattavuus(false);
			} else {
				vuokraamo.poistaPyora(pyoraKohdalla);
				int index = fxChooserPyorat.getSelectedIndex();
				hae(0);
				fxChooserPyorat.setSelectedIndex(index);
				muokattavana = false;
				vaihdaMuokattavuus(false);
			}
		}

		Pyora uusi = new Pyora();
		uusi.aseta(1, "tyhj�");

		try {
			vuokraamo.lisaaPyora(uusi);
		} catch (SailoException e) {
			Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
			return;
		}

		pyoraKohdalla = uusi;
		hae(uusi.getPyoranID());
		muokkaaPyora();
	}


	/**
	 * Poistaa kohdalla olevan py�r�n
	 */
	private void poistaPyora() {
		if (pyoraKohdalla == null) return;
		if(pyoraKohdalla.getOnkoVarattu()) {
			Dialogs.showMessageDialog("Vuokrattua py�r�� ei voi poistaa. Kuittaa ensin vuokraus");
			return;
		}
		if (!Dialogs.showQuestionDialog("Poisto", "Poistetaanko py�r�: " + pyoraKohdalla.getNimi(), "Kyll�", "Ei")) {
			return;
		}
		vuokraamo.poistaPyora(pyoraKohdalla);
		muokattavana = false;
		vaihdaMuokattavuus(false);

		hae(0);
	}


	/**
	 * Tietojen tallennus
	 * @return null jos onnistuu, muuten virhe tekstin�
	 */
	private String tallenna() {
		if (muokattavana == true) {
			for (TextField textField : tekstikentat) {
				if (textField.getText().contains("|")) {
					Dialogs.showMessageDialog("Tekstikentt��n ei voi sy�tt�� | merkki�");
					return "Tekstikentt��n ei voi sy�tt�� | merkki�";
				}
			}
			try {
				pyoraKohdalla.aseta(1, textFieldNimi.getText());
				pyoraKohdalla.aseta(2, textFieldMalli.getText());
				pyoraKohdalla.aseta(3, textFieldKunto.getText());
				pyoraKohdalla.aseta(4, textFieldVuokra.getText());
				pyoraKohdalla.aseta(5, Boolean.toString(vuokrattunaBool(textFieldTila.getText())));
				pyoraKohdalla.aseta(6, textFieldLisatietoja.getText());
			} catch (SailoException e) {
				Dialogs.showMessageDialog(e.getMessage());
				hae(pyoraKohdalla.getPyoranID());
				return e.getMessage();
			}
			// asetetaan py�r�n kunto taas tekstiksi ja label takaisin
			textFieldKunto.setText(pyoraKohdalla.anna(3));
			textKunto.setText("Kunto");

			muokattavana = false;
			vaihdaMuokattavuus(false);
		}
		try {
			vuokraamo.tallenna();
			hae(pyoraKohdalla.getPyoranID());
			return null;
		} catch (SailoException ex) {
			Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
			return ex.getMessage();
		}
	}


	/**
	 * Hakee py�rien tiedot listaan
	 * @param pyoraID py�r�n ID, joka aktivoidaan haun j�lkeen
	 */
	protected void hae(int pyoraID) {
		String ehto = hakuehto.getText();
		fxChooserPyorat.clear();

		int index = 0;
		Collection<Pyora> pyorat;
		int i = 0;
		try {
			pyorat = vuokraamo.etsi(ehto, fxVainVapaatCB.isSelected());
			if (fxHinnanMukaanCB.isSelected()) pyorat = vuokraamo.hinnanMukaan(pyorat);
			//int i = 0;
			for (Pyora pyora : pyorat) {
				if (pyora.getPyoranID() == pyoraID) index = i;
				fxChooserPyorat.add(pyora.getNimi(), pyora);
				i++;
			}
		} catch (SailoException ex) {
			Dialogs.showMessageDialog("Py�r�n hakemisessa ongelmia! " + ex.getMessage());
		}
		
		// jos ei ole py�ri�, niin tyhjennet��n kent�t. Parempi paikka t�lle olisi naytaPyora() aliohjelmassa, mutta vaivalloinen fxguin takia.
		if(i == 0) {
			//pyoraKohdalla = null;
			tyhjennaKentat();
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
	    // Ei k�yt�ss�.
	}

}
