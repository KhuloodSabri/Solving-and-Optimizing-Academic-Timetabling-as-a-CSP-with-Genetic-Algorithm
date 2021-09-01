package Basics;

public class Doctor {
	private static int count;
	private int id;
	private String name;
	private Course [] favCourses = new Course[5];
	private Course [] favLabs = new Course[3];
	private int assignedCourseHours = 0;
	private int assignedLabHours = 0;
	public int correct;
	
	public Doctor() {
		super();
		this.id = count++;
	}
	public Doctor(String name) {
		super();
		this.id = count++;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Course[] getFavCourses() {
		return favCourses;
	}
	public void setFavCourses(Course[] favCourses) {
		this.favCourses = favCourses;
	}
	public Course[] getFavLabs() {
		return favLabs;
	}
	public void setFavLabs(Course[] favLabs) {
		this.favLabs = favLabs;
	}
	public static int getCount() {
		return count;
	}
	public int getAssignedCourseHours() {
		return assignedCourseHours;
	}
	public void setAssignedCourseHours(int assignedCourseHours) {
		this.assignedCourseHours = assignedCourseHours;
	}
	public int getAssignedLabHours() {
		return assignedLabHours;
	}
	public void setAssignedLabHours(int assignedLabHours) {
		this.assignedLabHours = assignedLabHours;
	}
	
	

}
