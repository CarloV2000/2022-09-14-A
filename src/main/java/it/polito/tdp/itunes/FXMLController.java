/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnComponente"
    private Button btnComponente; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSet"
    private Button btnSet; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="txtDurata"
    private TextField txtDurata; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doComponente(ActionEvent event) {
    	Album a = this.cmbA1.getValue();
    	if(a==null) {
    		this.txtResult.setText("Selezionare un album nella box a1!");
    		return;
    	}
    	int n = model.numeroComponenteConnessa(a);
    	double d = model.durataComponenteConnessa(a);
    	this.txtResult.setText("Numero componenti connesse di "+a.getTitle()+": "+n);
    	this.txtResult.appendText("\nDurata componenti connesse di "+a.getTitle()+": "+d+" minuti.");

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String input = this.txtDurata.getText();
    	Double inputDurata;
    	if(input == "") {
    		this.txtResult.setText("Inserire un valore di durata!");
    		return;
    	}
    	try {
    		inputDurata = Double.parseDouble(input);
    		String s = model.creaGrafo(inputDurata);
    		this.txtResult.setText(s);
    		
    		this.cmbA1.getItems().clear();
    		for(Album a : model.getGrafo().vertexSet())
    			this.cmbA1.getItems().add(a);
    		
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire numero nel campo durata !");
    		return;
    	}
    }

    @FXML
    void doEstraiSet(ActionEvent event) {
    	Album a = this.cmbA1.getValue();
    	if(a==null) {
    		this.txtResult.setText("Selezionare un album nella box a1!");
    		return;
    	}
    	String s = this.txtX.getText();
    	if(s==null) {
    		this.txtResult.setText("Inserire una soglia dTot!");
    		return;
    	}
    	String result = "";
    	Double dTot;
    	try {
    		dTot = Double.parseDouble(s);
    		Set<Album>res = new HashSet<>(model.ricercaSetMassimo(a, dTot));
    		for(Album x : res) {
    			result += x.getTitle()+"\n";
    		}
    		this.txtResult.setText(result);
    		
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire numero nella soglia durata !");
    		return;
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnComponente != null : "fx:id=\"btnComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSet != null : "fx:id=\"btnSet\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDurata != null : "fx:id=\"txtDurata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }

}
