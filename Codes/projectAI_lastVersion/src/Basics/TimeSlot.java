package Basics;

import java.util.ArrayList;
import java.util.Collections;

import Services.ReadFiles;

public class TimeSlot implements Comparable<TimeSlot> {
	private static int count = 0;
	private int id;
	private double startTime;
	private double endTime;
	private char courseType;
	private String dayString;
	private boolean[] days = new boolean[5];
	private static ArrayList<TimeSlot> coursesTimeSlots = null;
	private static ArrayList<TimeSlot> labsTimeSlots = null;

	public TimeSlot() {
		super();
		this.id = count++;
	}

	public TimeSlot(double startTime, double endTime, char courseType, boolean[] days, String daysString) {
		super();
		this.id = count++;
		this.startTime = startTime;
		this.endTime = endTime;
		this.courseType = courseType;
		this.days = days;
		this.dayString = daysString;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}

	public char getCourseType() {
		return courseType;
	}

	public void setCourseType(char courseType) {
		this.courseType = courseType;
	}

	public boolean[] getDays() {
		return days;
	}

	public void setDays(boolean[] days) {
		this.days = days;
	}

	public static int getCount() {
		return count;
	}

	public static boolean isThereDayOff(ArrayList<TimeSlot> timeSlots) {
		boolean table[][] = getWeekTable(timeSlots);
		for (int i = 0; i < table.length; i++) {
			boolean isDayOff = true;
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j]) {
					isDayOff = false;
					break;
				}
			}
			if (isDayOff) {
				return true;
			}
		}
		return false;
	}

	public static int checkTimeConflictsforRoom(ArrayList<TimeSlot> timeSlots) {
		int conflicts = 0;
		for (int i = 0; i < timeSlots.size(); i++) {
			for (int j = i + 1; j < timeSlots.size(); j++) {
				if (timeSlots.get(i).getId() == timeSlots.get(j).getId()) {
					++conflicts;
				}
			}
		}
		return conflicts;
	}

	public static int checkTimeConflictsforDoctors(ArrayList<TimeSlot> timeSlots) {
		return getTimeConflicts(timeSlots);
	}

	public static int getTimeConflicts(ArrayList<TimeSlot> timeSlots) {
		int conflicts = 0;
		for (int i = 0; i < timeSlots.size(); i++) {
			for (int j = i + 1; j < timeSlots.size(); j++) {
				if (timeSlots.get(i).getId() == timeSlots.get(j).getId()) {
					++conflicts;
				} else if ((timeSlots.get(i).getEndTime() - timeSlots.get(i).getStartTime() == 3
						&& timeSlots.get(j).getEndTime() - timeSlots.get(j).getStartTime() < 3)
						|| (timeSlots.get(i).getEndTime() - timeSlots.get(i).getStartTime() < 3
								&& timeSlots.get(j).getEndTime() - timeSlots.get(j).getStartTime() == 3)) {

					boolean sameDay = false;
					for (int h = 0; h < 5; h++) {
						if (timeSlots.get(i).days[h] && timeSlots.get(j).days[h]) {
							sameDay = true;
							break;
						}
					}
					if (sameDay) {
						TimeSlot first = timeSlots.get(i);
						TimeSlot second = timeSlots.get(j);
						if (first.getStartTime() > second.getStartTime()) {
							TimeSlot temp = first;
							first = second;
							second = temp;
						}
						if (first.getEndTime() > second.getStartTime()) {
							conflicts++;
						}

					}

				}
			}
		}
		return conflicts;
	}

	public static boolean[][] getWeekTable(ArrayList<TimeSlot> timeSlots) {
		Collections.sort(timeSlots);
		boolean table[][] = new boolean[5][18];
		for (int i = 0; i < timeSlots.size(); i++) {
			for (int j = 0; j < 5; j++) {
				if (!timeSlots.get(i).days[j]) {
					continue;
				} else {
					for (int h = (int) ((timeSlots.get(i).startTime - 8) * 2); h < (int) ((timeSlots.get(i).endTime - 8)
							* 2); h++) {
						table[j][h] = true;
					}
				}
			}
		}
		return table;
	}
	
	public static double getWaitPercentPerDoctor(ArrayList<TimeSlot> timeSlots, boolean tttt) {
		boolean table[][] = getWeekTable(timeSlots);
		if (tttt) {
			for (int i = 0; i < table.length; i++) {
				for (int j = 0; j < table[i].length; j++) {
					System.out.printf("%7b",table[i][j]);
				}
				System.out.println();
			}
		}	
		int totalWait = 0;
		int totalBusy = 0;
		for (int i = 0; i < table.length; i++) {
			int lastWait = -1;
			boolean dayStarted = false;
			boolean dayEnded = false;
			int dayStart = 0;
			int dayEnd = 0;
			for (int j = table[i].length - 1; j >= 0; j--) {
				if (table[i][j] && !dayEnded) {
					dayEnded = true;
					dayEnd = j + 1;
				}
				if (dayEnded && !table[i][j]) {
					lastWait = j;
					break;
				}
			}
			for (int j = 0; j < table[i].length && dayEnd != 0; j++) {

				if (table[i][j]) {
					if (!dayStarted) {
						dayStarted = true;
						dayStart = j;
					}
				}else if (dayStarted && j > lastWait) {
					break;
				}else if(dayStarted) {
					totalWait++;
				}
				if (tttt) {
					System.out.print(" day=" + i + " j=" + j + "w=>" + totalWait + "start=>"+dayStart +"end=>"+ dayEnd +" *** ");
				}				
			}
			totalBusy+= dayEnd-dayStart;
			if (tttt) {
				System.out.println();
			}
		}
		if(totalBusy == 0) {
			return 0;
		}else {
			return totalWait * 1.0 /totalBusy;
		}
	}


	public static double getConsecutiveHours(ArrayList<TimeSlot> timeSlots, boolean tttt) {
		boolean table[][] = getWeekTable(timeSlots);
		if (tttt) {
			for (int i = 0; i < table.length; i++) {
				for (int j = 0; j < table[i].length; j++) {
					System.out.print(table[i][j] + "  ");
				}
				System.out.println();
			}
		}

		int currentConsecutive = 0;
		int maxConsecutive = 0;
		for (int i = 0; i < table.length; i++) {

			currentConsecutive = 0;
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j]) {
					currentConsecutive++;
				} else {
					if (currentConsecutive > maxConsecutive) {
						maxConsecutive = currentConsecutive;
					}
					currentConsecutive = 0;
				}
				if (tttt) {
					System.out.print(" day=" + i + " j=" + j + "=>" + currentConsecutive + "   ");
				}
			}
			if (tttt) {
				System.out.println();
			}
			if (currentConsecutive > maxConsecutive) {
				maxConsecutive = currentConsecutive;
			}

		}
		return maxConsecutive / 2.0;
	}

	public static void generateTimeSlots() {
		boolean SMW[] = { true, true, false, true, false };
		boolean TR[] = { false, false, true, false, true };
		coursesTimeSlots = new ArrayList<>();
		labsTimeSlots = new ArrayList<>();
		for (int i = 8; i < 17; i++) {
			TimeSlot slot = new TimeSlot(i, i + 1, 'c', SMW, "SMW");
			coursesTimeSlots.add(slot);
		}
		for (double i = 8; i < 17; i += 1.5) {
			TimeSlot slot = new TimeSlot(i, i + 1.5, 'c', TR, "TR");
			coursesTimeSlots.add(slot);
		}
		TimeSlot labSlot1 = new TimeSlot(14, 17, 'l', new boolean[] { true, false, false, false, false }, "S");
		TimeSlot labSlot2 = new TimeSlot(14, 17, 'l', new boolean[] { false, true, false, false, false }, "M");
		TimeSlot labSlot3 = new TimeSlot(14, 17, 'l', new boolean[] { false, false, false, true, false }, "W");
		labsTimeSlots.add(labSlot1);
		labsTimeSlots.add(labSlot2);
		labsTimeSlots.add(labSlot3);

		for (int i = 8; i <= 14; i += 3) {
			TimeSlot labSlot4 = new TimeSlot(i, i + 3, 'l', new boolean[] { false, false, true, false, false }, "T");
			TimeSlot labSlot5 = new TimeSlot(i, i + 3, 'l', new boolean[] { false, false, false, false, true }, "R");
			labsTimeSlots.add(labSlot4);
			labsTimeSlots.add(labSlot5);
		}
		System.out.println("course Ids");
		for (int y = 0; y < coursesTimeSlots.size(); y++) {
			System.out.print(coursesTimeSlots.get(y).id + ",  ");
		}
		System.out.println("\nlabs Ids");
		for (int y = 0; y < labsTimeSlots.size(); y++) {
			System.out.print(labsTimeSlots.get(y).id + ",  ");
		}
		System.out.println();
	}

	public static ArrayList<TimeSlot> getCoursesTimeSlots() {
		if (coursesTimeSlots == null) {
			generateTimeSlots();
		}
		return coursesTimeSlots;

	}

	public static ArrayList<TimeSlot> getLabsTimeSlots() {
		if (labsTimeSlots == null) {
			generateTimeSlots();
		}
		return labsTimeSlots;
	}

	@Override
	public int compareTo(TimeSlot o) {
		if (this.id == o.id) {
			return 0;
		} else if (this.id > o.id) {
			return 1;
		} else {
			return -1;
		}
	}

	public String getDayString() {
		return dayString;
	}

	public void setDayString(String dayString) {
		this.dayString = dayString;
	}

}
