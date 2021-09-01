package Basics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import Services.ReadFiles;

public class Chromosome {
	public static int headers[] = new int[ReadFiles.numOfSections];
	public int[] chromosome = new int[ReadFiles.numOfSections * 3];
	static ArrayList<TimeSlot> doctorsSlots[] = new ArrayList[ReadFiles.doctors.size()];
	static ArrayList<TimeSlot> roomsSlots[] = new ArrayList[ReadFiles.rooms.size()];
	static HashMap<Integer, ArrayList<TimeSlot>> levelSlots = new HashMap();
	public double hardFittness;
	public double totalFittness;
	public double softFittness;
	private long id = 0;
	private static long count = 0;

	public static void initializeHeader() {
		int variableIndex = 0;
		for (int i = 0; i < ReadFiles.courses.size(); i++) {
			Course course = ReadFiles.courses.get(i);
			for (int j = 0; j < course.getNumOfSections(); j++) {
				headers[variableIndex++] = course.getId();
			}
		}
		headers = Helpers.RandomizeArray(headers);

	}

	public Chromosome(boolean random) {
		int j = 0;
		if (random) {
			for (int i = 0; i < headers.length; i++) {
				Course course = ReadFiles.courses.get(headers[i]);
				// doctor timeSlot room
				ArrayList<Doctor> doctors = course.getDoctors();
				int index = 0;
				if (doctors.size() == 1) {
					index = 0;
				} else {
					index = Helpers.getRandomNumberInRange(0, doctors.size() - 1);
				}
				chromosome[j++] = doctors.get(index).getId();

				chromosome[j++] = getTimeSlotIdOfCourseId(headers[i]);

				// should check lab or course
				chromosome[j++] = getRandomRoomId (headers[i]);
				

			}
		}
		this.id = count++;
	}
	

	public Chromosome(Chromosome c) {
		for (int i = 0; i < c.chromosome.length; i++) {
			this.chromosome[i] = c.chromosome[i];
		}
		this.id = count++;
	}
	
	public static int getRandomRoomId(int courseId) {
		Course course= ReadFiles.courses.get(courseId);
		if (course.getType() == 'l') {
			 return course.getLabRoom().getId();
		} else {
			int roomId = Helpers.getRandomNumberInRange(0, ReadFiles.rooms.size() - 1);
			while (ReadFiles.rooms.get(roomId).getType()=='l') {
				roomId = Helpers.getRandomNumberInRange(0, ReadFiles.rooms.size() - 1);
			}
			return roomId;
		}
	}

	public static int getTimeSlotIdOfCourseId(int courseId) {
		int endOfTimeSlots = 0;
		int startOfTimeSlots = 0;
		if (ReadFiles.courses.get(courseId).getType() == 'c') {
			startOfTimeSlots = 0;
			endOfTimeSlots = TimeSlot.getCoursesTimeSlots().size();
		} else {
			startOfTimeSlots = TimeSlot.getCoursesTimeSlots().size();
			endOfTimeSlots = TimeSlot.getLabsTimeSlots().size() + TimeSlot.getCoursesTimeSlots().size();
		}
		int timeSlotId = Helpers.getRandomNumberInRange(startOfTimeSlots, endOfTimeSlots - 1);
		return timeSlotId;
	}

	public static double calcConflictsFittness(int[] c) {

		int totalConflicts = 0;
		for (int i = 0; i < doctorsSlots.length; i++) {
			totalConflicts += TimeSlot.checkTimeConflictsforDoctors(doctorsSlots[i]);
		}
		for (int i = 0; i < roomsSlots.length; i++) {
			totalConflicts += TimeSlot.checkTimeConflictsforRoom(roomsSlots[i]);
		}
		if (ReadFiles.numOfSections - totalConflicts < 0) {
			return 0;
		}
		return Math.abs(ReadFiles.numOfSections - totalConflicts) * 1.0 / ReadFiles.numOfSections;

	}

	public static double calcAssignedCourcesFittness(int[] c) {
		int correctNum = 0;
		for (int i = 0; i < ReadFiles.doctors.size(); i++) {
			ReadFiles.doctors.get(i).setAssignedLabHours(0);
			ReadFiles.doctors.get(i).setAssignedCourseHours(0);
			ReadFiles.doctors.get(i).correct = 0;
		}
		for (int i = 0; i < c.length; i += 3) {
			Doctor doctor = ReadFiles.doctors.get(c[i]);
			Course course = ReadFiles.courses.get(headers[i / 3]);
			if (course.getType() == 'c') {
				doctor.setAssignedCourseHours(doctor.getAssignedCourseHours() + 3);
			} else {
				doctor.setAssignedLabHours(doctor.getAssignedLabHours() + 2);
			}
			boolean found = false;

			for (int j = 0; j < doctor.getFavCourses().length; j++) {
				if (doctor.getFavCourses()[j].getId() == course.getId()) {
					found = true;
					(doctor.correct)++;
					break;
				}
			}

			for (int j = 0; j < doctor.getFavLabs().length; j++) {
				if (doctor.getFavLabs()[j].getId() == course.getId()) {
					found = true;
					(doctor.correct)++;
					break;
				}
			}

			if (found) {
				correctNum++;
			}

		}
		return correctNum * 1.0 / ReadFiles.numOfSections;

	}

	public static double clacNumOfTotalHoursFittness(int[] c) {
		double numerator = 0;
		for (int i = 0; i < ReadFiles.doctors.size(); i++) {
			int hours = ReadFiles.doctors.get(i).getAssignedCourseHours()
					+ ReadFiles.doctors.get(i).getAssignedLabHours();
			if (hours >= 12 && hours <= 18) {
				numerator = numerator + 1.0;
			} else if (hours < 12) {
				numerator += (hours * 1.0 / 12);
			} else {
				numerator += (18.0 / hours);
			}
		}

		return (numerator / ReadFiles.doctors.size());
	}

	public static double clacNumOfCourseHoursFittness(int[] c) {
		float numerator = 0;
		for (int i = 0; i < ReadFiles.doctors.size(); i++) {
			int hours = ReadFiles.doctors.get(i).getAssignedCourseHours();
			if (hours < 6) {
				numerator += (hours * 1.0 / 6);
			} else {
				numerator += 1.0;
			}
		}
		return (numerator / ReadFiles.doctors.size());
	}

	public static double calcConsecutiveFittness(int c[]) {
		double numerator = 0;
		for (int i = 0; i < doctorsSlots.length; i++) {
			double consecutive = TimeSlot.getConsecutiveHours(doctorsSlots[i], false);
			if (consecutive <= 3) {
				numerator += 1.0;
			} else {
				numerator += (3.0 / consecutive);
			}
		}
		return numerator / ReadFiles.doctors.size();
	}

	public static void initializeArraysForHardFittness(int[] c) {
		for (int i = 0; i < doctorsSlots.length; i++) {
			doctorsSlots[i] = new ArrayList<TimeSlot>();
		}
		for (int i = 0; i < roomsSlots.length; i++) {
			roomsSlots[i] = new ArrayList<TimeSlot>();
		}
		for (int i = 0; i < c.length; i += 3) {
			TimeSlot t;
			// System.out.println(ReadFiles.courses.get(headers[i/3]).getSymbol());
			if ((ReadFiles.courses.get(headers[i / 3])).getType() == 'c') {
				// System.out.println("hhhhhhh i+" +i+" timeslot id"+ c[i+1]);
				t = TimeSlot.getCoursesTimeSlots().get(c[i + 1]);
			} else {
				// System.out.println("in else i+" +i +" timeslot id"+ c[i+1]);
				t = TimeSlot.getLabsTimeSlots().get(c[i + 1] - TimeSlot.getCoursesTimeSlots().size());
			}
			doctorsSlots[c[i]].add(t);
			roomsSlots[c[i + 2]].add(t);
		}
	}

	public void getHardFittness() {
		int[] c = this.chromosome;
		double fittness = 0;

		initializeArraysForHardFittness(c);

		fittness += calcConflictsFittness(c) * 1.0 * 15;

		fittness += calcAssignedCourcesFittness(c) * 15;

		fittness += clacNumOfTotalHoursFittness(c) * 7.5;

		fittness += clacNumOfCourseHoursFittness(c) * 7.5;

		fittness += calcConsecutiveFittness(c) * 15;

		this.hardFittness = fittness;
	}

	public static double calcLevelConflictFittness(int c[]) {
		for (Iterator<Integer> iterator = levelSlots.keySet().iterator(); iterator.hasNext();) {
			Integer key = iterator.next();
			ArrayList<TimeSlot> timeSlots = levelSlots.get(key);
			timeSlots.clear();
		}
		for (int i = 0; i < c.length; i += 3) {
			int level = ReadFiles.courses.get(headers[i / 3]).getYear();
			TimeSlot slot;
			if (ReadFiles.courses.get(headers[i / 3]).getType() == 'c') {
				slot = TimeSlot.getCoursesTimeSlots().get(c[i + 1]);
			} else {
				slot = TimeSlot.getLabsTimeSlots().get(c[i + 1] - TimeSlot.getCoursesTimeSlots().size());
			}

			if (levelSlots.get(level) == null) {
				levelSlots.put(level, new ArrayList<TimeSlot>());
			}
			levelSlots.get(level).add(slot);
		}
		double numerator = 0;
		for (Iterator<Integer> iterator = levelSlots.keySet().iterator(); iterator.hasNext();) {
			Integer key = iterator.next();
			ArrayList<TimeSlot> timeSlots = levelSlots.get(key);
			int conflicts = TimeSlot.getTimeConflicts(timeSlots);
			numerator += conflicts * 1.0 / timeSlots.size();
		}

		return 1 - (numerator / levelSlots.size());
	}

	public static double calc8AMFittness(int[] c) {
		double numerator = 0;
		for (int i = 0; i < doctorsSlots.length; i++) {
			int at8Count = 0;
			for (int j = 0; j < doctorsSlots[i].size(); j++) {
				if (doctorsSlots[i].get(j).getStartTime() == 8.0) {
					at8Count++;
				}
			}
			numerator += at8Count * 1.0 / 5.0;
		}

		return (1 - numerator / doctorsSlots.length);
	}

	public static double calcWaitTimeFittness(int[] c) {
		double numerator = 0;
		for (int i = 0; i < doctorsSlots.length; i++) {
			numerator += TimeSlot.getWaitPercentPerDoctor(doctorsSlots[i], false);
		}
		return 1 - (numerator / ReadFiles.doctors.size());
	}

	public static double calcDayOffFittnes(int[] c) {
		int numerator = 0;
		for (int i = 0; i < doctorsSlots.length; i++) {
			if (TimeSlot.isThereDayOff(doctorsSlots[i])) {
				numerator++;
			}
		}
		return numerator * 1.0 / ReadFiles.doctors.size();
	}

	public static double calcBuildingFittness(int[] c) {
		int numerator = 0;
		for (int i = 2; i < c.length; i += 3) {
			// System.out.println(ReadFiles.rooms.get(c[i]).getName());
			if (ReadFiles.rooms.get(c[i]).getName().substring(0, 5).equals("Masri")) {
				numerator++;
			}
		}
		return numerator * 1.0 / headers.length;
	}

	public void getSoftFittness() {
		double fittness = 0;
		fittness += calcLevelConflictFittness(this.chromosome) * 12;
		fittness += calc8AMFittness(this.chromosome) * 10;
		fittness += calcWaitTimeFittness(this.chromosome) * 5;
		fittness += calcDayOffFittnes(this.chromosome) * 5;
		fittness += calcBuildingFittness(this.chromosome) * 8;
		this.softFittness = fittness;
	}

	public void getTotalFittness() {
		this.getHardFittness();
		if (Math.floor(this.hardFittness) == 60.0) {
			this.getSoftFittness();
			this.totalFittness = 60.0 + this.softFittness;
		} else {
			this.totalFittness = this.hardFittness;
		}

	}

	public static void printByDoctors(Chromosome c) {
		initializeArraysForHardFittness(c.chromosome);
		for (int i = 0; i < ReadFiles.doctors.size(); i++) {
			System.out.println(ReadFiles.doctors.get(i).getName());
			for (int j = 0; j < c.chromosome.length; j += 3) {
				if (ReadFiles.doctors.get(i).getId() == c.chromosome[j]) {
					Course course = ReadFiles.courses.get(headers[j / 3]);
					System.out.print("	 course : " + course.getSymbol());
					if (course.getType() == 'c') {
						TimeSlot t = TimeSlot.getCoursesTimeSlots().get(c.chromosome[j + 1]);
						System.out.print(
								"  TimeSlot: " + t.getDayString() + "  " + t.getStartTime() + "-" + t.getEndTime());
					} else {
						TimeSlot t = TimeSlot.getLabsTimeSlots()
								.get(c.chromosome[j + 1] - TimeSlot.getCoursesTimeSlots().size());
						System.out.print(
								"  TimeSlot: " + t.getDayString() + "  " + t.getStartTime() + "-" + t.getEndTime());

					}

					System.out.println("	Room : " + ReadFiles.rooms.get(c.chromosome[j + 2]).getName());
				}
			}
			System.out.println("**********  \nconflict " + TimeSlot.checkTimeConflictsforDoctors(doctorsSlots[i]));
			System.out.println("consecutive hours" + TimeSlot.getConsecutiveHours(doctorsSlots[i], false) + "  "
					+ calcConsecutiveFittness(c.chromosome));
			System.out.println("total hours = "
					+ (ReadFiles.doctors.get(i).getAssignedCourseHours()
							+ ReadFiles.doctors.get(i).getAssignedLabHours())
					+ "    " + clacNumOfTotalHoursFittness(c.chromosome));
			System.out.println("course hours =" + ReadFiles.doctors.get(i).getAssignedCourseHours() + "      "
					+ clacNumOfCourseHoursFittness(c.chromosome));
			System.out.println("assigned courses fittness =" + calcAssignedCourcesFittness(c.chromosome));
			System.out.println("correct" + ReadFiles.doctors.get(i).correct);
			System.out.println("wait" + TimeSlot.getWaitPercentPerDoctor(doctorsSlots[i], false));
		}
	}

	public static void printByRooms(Chromosome c) {
		initializeArraysForHardFittness(c.chromosome);
		for (int i = 0; i < ReadFiles.rooms.size(); i++) {
			System.out.println(ReadFiles.rooms.get(i).getName());
			for (int j = 2; j < c.chromosome.length; j += 3) {
				if (ReadFiles.rooms.get(i).getId() == c.chromosome[j]) {
					Course course = ReadFiles.courses.get(headers[j / 3]);
					System.out.print("  course : " + course.getSymbol());
					System.out.print("	Instructor : " + ReadFiles.doctors.get(c.chromosome[j - 2]).getName());
					if (course.getType() == 'c') {
						TimeSlot t = TimeSlot.getCoursesTimeSlots().get(c.chromosome[j - 1]);
						System.out.println(
								"  TimeSlot: " + t.getDayString() + "  " + t.getStartTime() + "-" + t.getEndTime());
					} else {
						TimeSlot t = TimeSlot.getLabsTimeSlots()
								.get(c.chromosome[j - 1] - TimeSlot.getCoursesTimeSlots().size());
						System.out.println(
								"  TimeSlot: " + t.getDayString() + "  " + t.getStartTime() + "-" + t.getEndTime());

					}

				}
			}
			System.out.println("**********     conflict " + TimeSlot.checkTimeConflictsforRoom(roomsSlots[i]));
		}
	}

	public static void printBylevels(Chromosome c) {
		double f = calcLevelConflictFittness(c.chromosome);
		for (Iterator<Integer> iterator = levelSlots.keySet().iterator(); iterator.hasNext();) {
			Integer key = iterator.next();
			ArrayList<TimeSlot> timeSlots = levelSlots.get(key);
			System.out.println("level" + key);
			for (int i = 0; i < timeSlots.size(); i++) {
				TimeSlot t = timeSlots.get(i);
				System.out.println("     " + t.getDayString() + " " + t.getStartTime() + "-" + t.getEndTime());
			}
			System.out.println("--------------------" + f + "   " + TimeSlot.getTimeConflicts(timeSlots));

		}
	}

}
