package view;

import pyoravarasto.MainApp;

import java.io.PrintStream;
import java.util.List;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;

import model.Asiakas;
import model.Pyora;
import model.SailoException;
import model.Vuokraus;
import model.Vuokraamo;

public class VarastoOverviewController {

	@FXML
	private MenuItem fxMenuTallenna;

	@FXML
	private MenuItem fxMenuAvaa;

	@FXML
	private MenuItem fxMenuTulosta;

	@FXML
	private MenuItem fxMenuLopeta;

	@FXML
	private MenuItem fxMenuUusiPyora;

	@FXML
	private MenuItem fxMenuPoistaPyora;

	@FXML
	private MenuItem fxMenuApua;

	@FXML
	private MenuItem fxMenuTietoja;

	@FXML
	private Button fxUusiPyoraButton;

	@FXML
	private Button fxTallennaButton;

	@FXML
	private Button fxVuokraaButton;
	
    @FXML
    private CheckBox fxVainVapaatCB;

	@FXML
	private Button fxPoistaButton;
	
    @FXML
    private ListChooser<Pyora> fxChooserPyorat;

	// Reference to the main application. En ole ihan varma mit‰ t‰m‰ tekee.
	private MainApp mainApp;

	/**
	 * Konstruktori
	 * T‰t‰ vissiin kutsutaan kun tapahtuu automaattinen initialize() metodi
	 * fxml-tiedoston ladattua. Dunno tbh
	 */
	public VarastoOverviewController() {

	}

	
	/**
	 * T‰t‰ kutsutaan MainApp:sta. M‰‰ritt‰‰, ett‰ t‰m‰ on perusn‰kym‰-
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
    void handleAvaaApua() {
		Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
    }

    @FXML
    void handleAvaaTietoja() {
    	Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
    }

    @FXML
    void handleMenuAvaa() {
    	Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
    }

    @FXML
    void handleMenuLopeta() {
    	Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
    }

    @FXML
    void handleMenuTulosta() {
    	//Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
		mainApp.showUusiTulostusDialog();
    }

	@FXML
	void handlePoistaPyora() {
		Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
	}

	@FXML
	void handleTallenna() {
		Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
	}

	@FXML
	void handleUusiPyora() {
		Dialogs.showMessageDialog("Ei ole viel‰ lis‰tty");
	}

	/**
	 * Avataan vuokrausikkuna.
	 */
	@FXML
	void handleUusiVuokraus() {
		mainApp.showUusiVuokrausDialog();
	}
	
	//====================================================
	// FXML:ll‰‰n kuulumaton koodi t‰st‰ eteenp‰in
	
	//private Vuokraamo vuokraamo;
    private Pyora pyoraKohdalla;
    private Vuokraamo vuokraamo;
    private TextArea areaPyora = new TextArea();
	
    /**
     * N‰ytt‰‰ listasta valitun j‰senen tiedot, tilap‰isesti yhteen isoon edit-kentt‰‰n
     */
    protected void naytaPyora() {
        pyoraKohdalla = fxChooserPyorat.getSelectedObject();

        if (pyoraKohdalla == null) return;

        areaPyora.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaPyora)) {
            tulosta(os,pyoraKohdalla); 
        }
    }
    
    /**
     * Luo uuden j‰senen jota aletaan editoimaan 
     */
    protected void uusiPyora() {
        Pyora uusi = new Pyora();
        uusi.rekisteroi();
        uusi.vastaaJopo();
        
        try {
            vuokraamo.lisaaPyora(uusi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }
        
        hae(uusi.getPyoranID());
    }
    
    
    /**
     * Tekee vuokrauksen valitulle pyˆr‰lle.
     */
    public void vuokraaPyora(int kesto) {
        // JOptionPane.showMessageDialog(null, "Viel‰ ei osata lis‰t‰ harrastusta!" );
        if ( pyoraKohdalla == null ) return; 
        Vuokraus vuokraus = new Vuokraus(); 
        vuokraus.rekisteroi(); 
        vuokraus.testiVuokraus(kesto);
        vuokraamo.lisaaVuokraus(vuokraus); 
        hae(pyoraKohdalla.getPyoranID());         
    }
    
    
    /**
     * Hakee j‰senten tiedot listaan
     * @param jnro j‰senen numero, joka aktivoidaan haun j‰lkeen
     */
    protected void hae(int pyoraID) {
        fxChooserPyorat.clear();

        int index = 0;
        for (int i = 0; i < vuokraamo.getPyoria(); i++) {
            Pyora pyora = vuokraamo.annaPyora(i);
            if (pyora.getPyoranID() == pyoraID) index = i;
            fxChooserPyorat.add(pyora.getNimi(), pyora);
        }
        fxChooserPyorat.setSelectedIndex(index); // t‰st‰ tulee muutosviesti joka n‰ytt‰‰ pyˆr‰n
    }
    


    /**
     * Tulostaa pyˆr‰n tiedot.
     * @param os Tietovirta, mihin tulostetaan
     * @param pyora Tulostettava pyˆr‰
     */
	private void tulosta(PrintStream os, Pyora pyora) {
		os.println("----------------------------------------------");
        pyora.tulosta(os);
        os.println("----------------------------------------------");
	}
	
	

}
