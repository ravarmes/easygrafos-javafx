/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author john_
 */
public class Main extends Application{
    
    /**
     *Elementos gráficos que são recebidos pelo construtor e passados para a
     */
   
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/grafos/view/FXMLVBoxMain.fxml"));
        
        Scene scene = new Scene(root);  
        
        stage.setScene(scene);
        
        stage.setTitle("Sistema para aprendizagem de grafos (Front-End)");
        
        
        //stage.setResizable(false);
        stage.centerOnScreen();
        
        Image icone = new Image(getClass().getResourceAsStream("/grafos/Imagens/ifes-logo.jpg"));
        
        stage.getIcons().add(icone);
        
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    
    
}
