import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Basics.*;
import Services.GeneticAlgorithm;
import Services.ReadFiles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Controller implements Initializable {

    @FXML
    private GridPane doctorTable;
    
    @FXML
    private TableView<CourseTableEntry> doctorBrowser;
    
    @FXML
    private TableColumn<CourseTableEntry, String> courseLabel;

    @FXML
    private TableColumn<CourseTableEntry, String> section;

    @FXML
    private TableColumn<CourseTableEntry, String> courseTitle;

    @FXML
    private TableColumn<CourseTableEntry, String> days;

    @FXML
    private TableColumn<CourseTableEntry, String> time;

    @FXML
    private TableColumn<CourseTableEntry, String> roomNumber;

    
    @FXML
    private ComboBox<String> doctorsCombo;
    
    int sections [] = new int [Chromosome.headers.length];
    
    @FXML
    void showTable(ActionEvent event) {
    	fillTable();
    }
    
    @FXML
    private AnchorPane container;

    @FXML
    private VBox vboxContainer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeDoctorsCombo();
		bindDoctorBrowserTable();
		fillCourseBrowser();
	}
	
	public void bindDoctorBrowserTable() {
		courseLabel.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("courseSymbol"));
		courseLabel.setStyle("-fx-alignment: CENTER;");
		section.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("section"));
		section.setStyle("-fx-alignment: CENTER;");
		courseTitle.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("courseName"));
		days.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("day"));
		days.setStyle("-fx-alignment: CENTER;");
		time.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("time"));
		time.setStyle("-fx-alignment: CENTER;");
		roomNumber.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("room"));	
		roomNumber.setStyle("-fx-alignment: CENTER;");
		
	}
	
	public void fillCourseBrowser() {
		int height = 0;
		for(int i = 0; i < ReadFiles.courses.size(); i++) {
			
			height++;
			Course course = ReadFiles.courses.get(i);
			Label label = new Label(course.getSymbol() +"/   "+course.getName());
			label.setStyle("-fx-font-size: 16;");
			vboxContainer.getChildren().add(label);
			
			TableView<CourseTableEntry> table = new TableView<CourseTableEntry>();
			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			table.getItems().clear();
			
			
		    table.setEditable(true);
		    ArrayList<CourseTableEntry> sectionsList = new ArrayList<>(); 
		    int c[] = GeneticAlgorithm.best.chromosome;
		    int count = 0;
		    for(int j =0; j < c.length;j+=3) {				
				if(Chromosome.headers[j/3] == course.getId() ) {
					height++;
					TimeSlot t;
					if (course.getType() == 'c') {
						t = TimeSlot.getCoursesTimeSlots().get(c[j + 1]);
					} else {
						t = TimeSlot.getLabsTimeSlots().get(c[j + 1] - TimeSlot.getCoursesTimeSlots().size());
					}
					
					sectionsList.add( new CourseTableEntry(++count,ReadFiles.doctors.get(c[j]).getName(),
							t.getDayString(), t.getStartTime()+"-"+ t.getEndTime(),ReadFiles.rooms.get(c[j+2]).getName()));
					sections[j/3] = count; 

				}
			}
		    
		    TableColumn<CourseTableEntry, Integer> section = new TableColumn<CourseTableEntry, Integer>("Section");
		    section.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, Integer>("section"));
		    
		    TableColumn<CourseTableEntry, String> instructor = new TableColumn<CourseTableEntry, String>("Instructor");
		    instructor.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("instructor"));
		    instructor.setPrefWidth(130);
		    
		    TableColumn<CourseTableEntry, String> day = new TableColumn<CourseTableEntry, String>("Day");
		    day.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("day"));
		    
		    TableColumn<CourseTableEntry, String> time = new TableColumn<CourseTableEntry, String>("Time");
		    time.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("time"));
		    
		    TableColumn<CourseTableEntry, String> room = new TableColumn<CourseTableEntry, String>("Room");
		    room.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("room"));
		    
		    table.getColumns().addAll(section, instructor, day, time, room);
		    table.setMaxHeight((sectionsList.size()+1)* 25);
		    

		    table.setId("my-table");
		    table.getItems().setAll(sectionsList);
		    
			vboxContainer.getChildren().add(table);
		}
		vboxContainer.setPrefHeight((height+20) *25);
		container.setPrefHeight((height+20) *40);
		vboxContainer.setPadding(new Insets(40));
		vboxContainer.setSpacing(30);
		
	}
	
	public void initializeDoctorsCombo() {
		for (int i = 0 ; i< ReadFiles.doctors.size(); i++) {
			doctorsCombo.getItems().add(ReadFiles.doctors.get(i).getName());
		}
	}
	
	public void fillTable() {
		doctorTable.getChildren().clear();
		doctorBrowser.getItems().clear();
		
		int selectedIndex = doctorsCombo.getSelectionModel().getSelectedIndex();
		Color sectionsColors[] = new Color[Chromosome.headers.length]; 
		for(int i = 0; i< sectionsColors.length;i++) {
			sectionsColors[i] = Color.rgb(Helpers.getRandomNumberInRange(0, 255), Helpers.getRandomNumberInRange(0, 255), Helpers.getRandomNumberInRange(0, 255),0.5);
		}
		 
		int c[]= GeneticAlgorithm.best.chromosome;
		for(int i =0; i < GeneticAlgorithm.best.chromosome.length; i+=3) {
			
			if (ReadFiles.doctors.get(selectedIndex).getId() == c[i]) {
				Course course = ReadFiles.courses.get(Chromosome.headers[i / 3]);
				TimeSlot t;
				if (course.getType() == 'c') {
					t = TimeSlot.getCoursesTimeSlots().get(c[i + 1]);
				} else {
					t = TimeSlot.getLabsTimeSlots().get(c[i + 1] - TimeSlot.getCoursesTimeSlots().size());
				}
				
				doctorBrowser.getItems().add(new CourseTableEntry(sections[i/3], t.getDayString(), t.getStartTime() +"-"+t.getEndTime(),
						ReadFiles.rooms.get(c[i+2]).getName(), course.getSymbol(), course.getName()));
				int column = (int)((t.getStartTime()-8)*2);
				int width = (int)((t.getEndTime() - t.getStartTime()) * 2);
				ArrayList<Integer> rows = new ArrayList<Integer>();
				for(int j =0; j < t.getDays().length;j++) {
					if(t.getDays()[j]) {
						if(j>= 1) {
							rows.add(j+1);
						}else {
							rows.add(j);
						}
						
					}
				}
				 for(int j =0; j < rows.size();j++) {
					 Rectangle label = new Rectangle(38.51 *width, 30);
					 label.setFill(sectionsColors[i/3]);
					 Text text = new Text(course.getSymbol());
					 StackPane pane = new StackPane();
					 pane.getChildren().addAll(label, text);
					 doctorTable.add(pane, column, rows.get(j));
				 }
				 
			}
		}
		 
	     
	    
	}
    
    

}
