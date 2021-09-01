import java.io.FileNotFoundException;
import java.util.ArrayList;

import Basics.*;
import Services.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class MainClass extends Application{
	private Stage primaryStage;
	private Parent root;
	public void start(Stage primaryStage) {
		try {
			this.primaryStage= primaryStage;
			this.primaryStage.setTitle("TimeTabling");
			root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
			Scene scene = new Scene(root);
			
			primaryStage.setScene(scene);
			primaryStage.getScene().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public static void main(String[] args) throws FileNotFoundException {
		
		ReadFiles.readFiles();
		Chromosome.initializeHeader();
		GeneticAlgorithm.initializePopulation();
		GeneticAlgorithm.solveGenetics(GeneticAlgorithm.population);
		launch(args);

	
	}

}
