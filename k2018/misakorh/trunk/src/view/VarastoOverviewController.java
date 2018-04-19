package view;

import pyoravarasto.MainApp;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import fi.jyu.mit.fxgui.ComboBoxChooser;
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

	// Reference to the main application.
	private MainApp mainApp;

	/**
	 * Konstruktori Tätä vissiin kutsutaan kun tapahtuu automaattinen initialize() metodi fxml-tiedoston ladattua. Dunno
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
	 * Tätä kutsutaan MainApp:sta. Määrittää, että tämä on perusnäkymä-
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}


	/**
	 * Muokkaa pyörää napin handläys
	 */
	@FXML
	void handleMuokkaaPyoraa() {
		muokkaaPyora();
	}


	/**
	 * Hakuehdon händläys
	 */
	@FXML
	void handleHakuehto() {
		if (pyoraKohdalla != null) hae(pyoraKohdalla.getPyoranID());
	}


	/**
	 * Händlää Apua painikkeen
	 */
	@FXML
	void handleAvaaApua() {
		avaaApua();
	}


	/**
	 * Händlää Tietoja painikkeen
	 */
	@FXML
	void handleAvaaTietoja() {
		Dialogs.showMessageDialog("PyöräVarasto\nVer. 0.6\nTekijät: Jouko Sirkka & Miro Korhonen");
	}


	/**
	 * Händlää lopeta painikkeen
	 */
	@FXML
	void handleMenuLopeta() {
		Dialogs.showMessageDialog("Ei ole vielä lisätty");
	}


	/**
	 * Händlää tulosta-painikkeen
	 */
	@FXML
	void handleMenuTulosta() {
		//Dialogs.showMessageDialog("Ei ole vielä lisätty");
		//mainApp.showUusiTulostusDialog();
		UusiTulostusDialogController tulostusDialog  = UusiTulostusDialogController.tulosta(null);
		tulostaValitut(tulostusDialog.getTextArea());
	}


	/**
	 * Händlää poistapyörä painikkeen
	 */
	@FXML
	void handlePoistaPyora() {
		poistaPyora();
	}


	/**
	 * Händlää tallennus painikkeen
	 */
	@FXML
	void handleTallenna() {
		tallenna();
	}


	/**
	 * Händlää uusipyörä painikkeen
	 */
	@FXML
	void handleUusiPyora() {
		uusiPyora();
	}


	/**
	 * Avataan vuokrausikkuna.
	 * @throws SailoException
	 */
	@FXML
	void handleUusiVuokraus() throws SailoException {
		if (pyoraKohdalla == null) {
			Dialogs.showMessageDialog("Valitse vuokrattava pyörä");
			return;
		}

		apuVuokraus = new Vuokraus();
		mainApp.showUusiVuokrausDialog(pyoraKohdalla, apuVuokraus);
		vuokraamo.lisaaVuokraus(apuVuokraus);
		hae(pyoraKohdalla.getPyoranID());

	}


	/**
	 * Händlää tilanteen jossa checkboxia "Vain vapaat" painetaan
	 */
	@FXML
	void handleVainVapaatCB() {
		hae(pyoraKohdalla.getPyoranID());
	}


	@FXML
	void handleHinnanMukaan() {
		hae(pyoraKohdalla.getPyoranID());
	}

	//===============================================================================
	// FXML:llään kuulumaton koodi tästä eteenpäin

	private String vuokraamonNimi = "MJVuokraamo";
	private Vuokraamo vuokraamo;
	private Pyora pyoraKohdalla;
	private Pyora apuPyora;
	private Vuokraus apuVuokraus;
	private boolean muokattavana = false;


	/**
	 * Alustetaan
	 */
	protected void alusta() {
		//panelPyora.setContent(areaPyora);
		panelPyora.setFitToHeight(true);
		fxChooserPyorat.clear();
		fxChooserPyorat.addSelectionListener(e -> naytaPyora());

		setVuokraamo(new Vuokraamo());
		avaa();
	}

	
	/**
	 * Muokkaa tällä hetkellä valittuna olevaa pyörää
	 */
	private void muokkaaPyora() {
		if (muokattavana) {
			return;
		}
		muokattavana = true;
		vaihdaMuokattavuus(true);
	}
	
	/**
	 * Avaa työn dokumentaation netistä
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
	 * Näyttää listasta valitun pyörän tiedot, tilapäisesti yhteen isoon edit-kenttään
	 */
	protected void naytaPyora() {
		if(muokattavana) muokattavana = false;
		
		
		pyoraKohdalla = fxChooserPyorat.getSelectedObject();

		if (pyoraKohdalla == null) {
			//areaPyora.clear();
			return;
		}

		apuVuokraus = null;
		if (pyoraKohdalla.getOnkoVarattu() == true) {
			apuVuokraus = vuokraamo.annaVuokraus(pyoraKohdalla);
			fxVuokraaButton.setText("Näytä vuokraus");
		}
		else fxVuokraaButton.setText("Vuokraa");

		//areaPyora.setText("");
		taytaKentat();
	}


	/**
	 * Täyttää pyörän kentät pyörän tiedoilla
	 */
	private void taytaKentat() {
		textFieldNimi.setText(pyoraKohdalla.anna(1));
		textFieldMalli.setText(pyoraKohdalla.anna(2));
		textFieldKunto.setText(pyoraKohdalla.anna(3));
		textFieldVuokra.setText(pyoraKohdalla.anna(6));
		textFieldTila.setText(pyoraKohdalla.anna(5));
		textFieldLisatietoja.setText(pyoraKohdalla.anna(4));
		vaihdaMuokattavuus(false);
	}


	/**
	 * Muuttaa pyörän tekstikenttien muokattavuuden truesta falseksi tai toisinpäin
	 */
	private void vaihdaMuokattavuus(boolean muokataanko) {
		boolean muokattavaksi = muokataanko;
		textFieldNimi.setEditable(muokattavaksi);
		;
		textFieldMalli.setEditable(muokattavaksi);
		textFieldKunto.setEditable(muokattavaksi);
		textFieldVuokra.setEditable(muokattavaksi);
		textFieldTila.setEditable(muokattavaksi);
		textFieldLisatietoja.setEditable(muokattavaksi);
	}


	/**
	 * Alustaa vuokraamon lukemalla sen valitun nimisestä tiedostosta
	 * @param nimi tiedosto josta vuokraamon tiedot luetaan
	 * @return null jos onnistuu, muuten virhe tekstinä
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
	 * Kysytään tiedoston nimi ja luetaan se
	 * @return true jos onnistui, false jos ei
	 */
	public boolean avaa() {
		//String uusiNimi = ""; // TODO dialog joka kysyy nimeä
		//if(uusiNimi == null) return false;
		lueTiedosto(vuokraamonNimi);
		return true;
	}


	/**
	 * Tietojen tallennus
	 * @return null jos onnistuu, muuten virhe tekstinä
	 */
	private String tallenna() {
		
		if(muokattavana == true) {
			try {
				Dialogs.showMessageDialog(pyoraKohdalla.toString());
				pyoraKohdalla.aseta(1, textFieldNimi.getText());
				pyoraKohdalla.aseta(2, textFieldMalli.getText());
				pyoraKohdalla.aseta(3, textFieldKunto.getText());
				pyoraKohdalla.aseta(4, textFieldVuokra.getText());
				pyoraKohdalla.aseta(5, textFieldTila.getText());
				pyoraKohdalla.aseta(6, textFieldLisatietoja.getText());
				vuokraamo.poistaPyora(pyoraKohdalla);
				vuokraamo.lisaaPyora(pyoraKohdalla);
				Dialogs.showMessageDialog(pyoraKohdalla.toString());
				
			} catch (Exception e) {
				Dialogs.showMessageDialog("Kenttien luvussa onglemia! " + e.getMessage());
				return e.getMessage();
			}
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
	 * Tulostaa listassa olevat pyörät tekstialueeseen
	 * @param text alue johon tulostetaan
	 */
	public void tulostaValitut(TextArea text) {
		try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
			os.println("Valitut pyörät");
			for(Pyora pyora : fxChooserPyorat.getObjects()) {
				tulosta(os, pyora);
				os.println("\n\n");
			}
		}
	} 


	/**
	 * Luo uuden jäsenen jota aletaan editoimaan
	 */
	protected void uusiPyora() {
		Pyora uusi = new Pyora();
		uusi.rekisteroi();
		uusi.aseta(1, "tyhjä");
		
		
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
	 * Poistaa halutun pyörän
	 */
	private void poistaPyora() {
		if (pyoraKohdalla == null) return;
		if (!Dialogs.showQuestionDialog("Poisto", "Poistetaanko pyörä: " + pyoraKohdalla.getNimi(), "Kyllä", "Ei"))
			return;
		vuokraamo.poistaPyora(pyoraKohdalla);
		int index = fxChooserPyorat.getSelectedIndex();
		hae(0);
		fxChooserPyorat.setSelectedIndex(index);
	}


	/**
	 * Tekee vuokrauksen valitulle pyörälle.
	 */
	public void vuokraaPyora(int kesto) throws SailoException {
		// JOptionPane.showMessageDialog(null, "Vielä ei osata lisätä vuokrausta!" );
		if (pyoraKohdalla == null) return;
		//Vuokraus vuokraus = new Vuokraus(kesto, pyoraKohdalla.getVuokraPerTunti(), pyoraKohdalla.getPyoranID(), 1); // TODO: Asiakkaat lol
		Vuokraus vuokraus = new Vuokraus();
		vuokraus.rekisteroi();
		vuokraus.testiVuokraus(pyoraKohdalla.getPyoranID(), kesto);
		vuokraamo.lisaaVuokraus(vuokraus);
		pyoraKohdalla.setOnkoVarattu(true);
		hae(pyoraKohdalla.getPyoranID());
	}


	/**
	 * Hakee jäsenten tiedot listaan
	 * @param jnro jäsenen numero, joka aktivoidaan haun jälkeen
	 */
	protected void hae(int pyoraID) {
		//int k = cbKentat.getSelectionModel().getSelectedIndex();
		String ehto = hakuehto.getText();
		//if (k > 0 || ehto.length() > 0)
		//    naytaVirhe(String.format("Ei osata hakea (ehto: %s)", ehto));
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
			Dialogs.showMessageDialog("Pyörän hakemisessa ongelmia! " + ex.getMessage());
		}
		fxChooserPyorat.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää jäsenen
	}


	/**
	 * Näyttää virheen
	 * @param virhe virheen teksti
	 */
	private void naytaVirhe(String virhe) {

		if (virhe == null || virhe.isEmpty())
			Dialogs.showMessageDialog("Tapahtui virhe");
		else
			Dialogs.showMessageDialog("Tapahtui virhe: " + virhe);
		/*
		 * if ( virhe == null || virhe.isEmpty() ) { labelVirhe.setText("");
		 * labelVirhe.getStyleClass().removeAll("virhe"); return; } labelVirhe.setText(virhe);
		 * labelVirhe.getStyleClass().add("virhe");
		 */
	}


	/**
	 * @param vuokraamo Vuokraamo jota käytetään tässä käyttöliittymässä
	 */
	public void setVuokraamo(Vuokraamo vuokraamo) {
		this.vuokraamo = vuokraamo;
		naytaPyora();
	}


	/**
	 * Tulostaa pyörän tiedot.
	 * @param os Tietovirta, mihin tulostetaan
	 * @param pyora Tulostettava pyörä
	 */
	private void tulosta(PrintStream os, Pyora pyora) {
		os.println("----------------------------------------------");
		pyora.tulosta(os);
		if (apuVuokraus != null) {
			apuVuokraus.tulosta(os);
		}

		os.println("----------------------------------------------");
	}


	/**
	 * Testihommia
	 * @param args ei käytösä
	 */
	public static void main(String[] args) {

	}

}
