package Services;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Basics.*;
public class ReadFiles {

	public static ArrayList<Course> courses = new ArrayList<>();
	public static ArrayList<Doctor> doctors = new ArrayList<>();
	public static ArrayList<Room> rooms = new ArrayList<>();
	public static HashMap<String, Course> coursesSymbol = new HashMap();
	public static int numOfSections=0;
	
	public static void readFiles () throws FileNotFoundException {
		readCourses("Courses.txt");
		readDoctors("Doctors.txt");
		readRooms("Rooms.txt");
		
	}
	
	public static void readCourses(String fileName) throws FileNotFoundException {
		Scanner in = new Scanner (new File(fileName));
		String line;
		while (in.hasNextLine()) {
			line =in.nextLine();
			String tokens [] = line.split("[,]");
			Course course = new Course(tokens[0], tokens[1], Integer.parseInt(tokens[2]));
			courses.add(course);
			coursesSymbol.put(course.getSymbol(), course);
			numOfSections += Integer.parseInt(tokens[2]);
		}	
		
	}
	
	public static void readDoctors(String fileName) throws FileNotFoundException {
		Scanner in = new Scanner (new File(fileName));
		String line;
		while (in.hasNextLine()) {
			line =in.nextLine();
			String tokens [] = line.split("[-]");
			
			Doctor doctor = new Doctor(tokens[0]);
			String courses1 [] = tokens[1].split("[,]");
			Course coursesList [] = new Course [5];
			for (int i=0; i<5; i++) {
				coursesList[i]= coursesSymbol.get(courses1[i]);
				coursesList[i].getDoctors().add(doctor);	
			}
			doctor.setFavCourses(coursesList);
			
			String labs [] = tokens[2].split("[,]");
			Course labsList [] = new Course [3];
			for (int i=0; i<3; i++) {
				labsList[i]= coursesSymbol.get(labs[i]);
				labsList[i].getDoctors().add(doctor);
			}
			doctor.setFavLabs(labsList);
			doctors.add(doctor);
		}	
	}
	
	public static void readRooms(String fileName) throws FileNotFoundException {
		Scanner in = new Scanner (new File(fileName));
		String line;
		while (in.hasNextLine()) {
			line =in.nextLine();
			String tokens [] = line.split("[,]");
			Room room = new Room(tokens[0], ("lecture".equals(tokens[1]))?'c':'l') ;
			if (room.getType() == 'l') {
				coursesSymbol.get(tokens[2]).setLabRoom(room);

			}
			
			rooms.add(room);
		}	
	}
	
	
}
