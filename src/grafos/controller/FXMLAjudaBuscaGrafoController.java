/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package grafos.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Danielle
 */
public class FXMLAjudaBuscaGrafoController implements Initializable {

    private Stage dialogStage;   
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    } 
    
    @FXML
    public void handleButtonFechar() {
        getDialogStage().close();
    }
    
    public void handleLinkArquivosGrafos() throws MalformedURLException, URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URL("https://encurtador.com.br/gmDG5").toURI());
    }
}
