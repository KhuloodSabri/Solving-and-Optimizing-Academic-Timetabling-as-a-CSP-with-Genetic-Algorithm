package Services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Basics.*;

public class GeneticAlgorithm {
	public static int initialPopulationSize = 1000;
	public static int maximumlPopulationSize = 3000;
	public static int maxIterations = 40000;
	public static double crossOverRate = 0.6;
	public static double mutationRate = 0.3;
	public static double maxFittness = 100;
	public static double bestFittness = 0;
	public static Chromosome best = null;
	public static double totalPopulationFittness = 0;
	public static ArrayList<Chromosome> population = new ArrayList<Chromosome>();

	public static Chromosome[] crossOver(Chromosome parent1, Chromosome parent2) {
		Chromosome child1 = new Chromosome(false);
		Chromosome child2 = new Chromosome(false);
		if(Math.random() < 0.5) {
			int start = Helpers.getRandomNumberInRange(0, Chromosome.headers.length - 1) * 3;
			int end = Helpers.getRandomNumberInRange(0, Chromosome.headers.length - 1) * 3;
			if (start > end) {
				int temp = start;
				start = end;
				end = temp;
			}

			
			for (int i = 0; i < start; i++) {
				child1.chromosome[i] = parent1.chromosome[i];
				child2.chromosome[i] = parent2.chromosome[i];
			}
			for (int i = start; i < end; i++) {
				child1.chromosome[i] = parent2.chromosome[i];
				child2.chromosome[i] = parent1.chromosome[i];
			}
			for (int i = end; i < parent1.chromosome.length; i++) {
				child1.chromosome[i] = parent1.chromosome[i];
				child2.chromosome[i] = parent2.chromosome[i];
			}
		}else {
			for(int i = 0; i < Chromosome.headers.length *3;i++) {
				if(Math.random() < 0.5) {
					child1.chromosome[i] = parent1.chromosome[i];
					child2.chromosome[i] = parent2.chromosome[i];
				}else {
					child1.chromosome[i] = parent2.chromosome[i];
					child2.chromosome[i] = parent1.chromosome[i];
				}
			}
		}
		
		child1.getTotalFittness();
		child2.getTotalFittness();
		return new Chromosome[] { child1, child2 };
	}

	public static Chromosome mutation(Chromosome parent) {
		Chromosome child = new Chromosome(parent);
			if (Math.random() < 0.5) {
				mutationByGene(child);
			} else {
				mutationByflip(child);
			}
			
		child.getTotalFittness();
		return child;
	}

	public static void mutationByGene(Chromosome child) {
		int mutationIndex = Helpers.getRandomNumberInRange(0, Chromosome.headers.length - 1) * 3;

		int doctorId = Helpers.getRandomNumberInRange(0, ReadFiles.doctors.size() - 1);
		child.chromosome[mutationIndex] = doctorId;

		int timeSlotId = Chromosome.getTimeSlotIdOfCourseId(Chromosome.headers[mutationIndex / 3]);
		child.chromosome[mutationIndex + 1] = timeSlotId;

		int roomId = Chromosome.getRandomRoomId(Chromosome.headers[mutationIndex / 3]);
		child.chromosome[mutationIndex + 2] = roomId;

	}

	public static void mutationByflip(Chromosome child) {
		// System.out.println("bit");
		int mutationIndex = Helpers.getRandomNumberInRange(0, Chromosome.headers.length * 3 - 1);
		if (mutationIndex == 0 || mutationIndex % 3 == 0) {
			int doctorId = Helpers.getRandomNumberInRange(0, ReadFiles.doctors.size() - 1);
			child.chromosome[mutationIndex] = doctorId;
		} else if (mutationIndex % 3 == 1) {
			int timeSlotId = Chromosome.getTimeSlotIdOfCourseId(Chromosome.headers[mutationIndex / 3]);
			child.chromosome[mutationIndex] = timeSlotId;
		} else {
			int roomId = Chromosome.getRandomRoomId(Chromosome.headers[mutationIndex / 3]);
			child.chromosome[mutationIndex] = roomId;
		}
	}

	public static int[] selectTwoParents() {
		int parentIndex[] = new int[2];
		double parentFittness[] = new double[2];
		for(int i = 0; i <2;i++) {
			parentIndex[i] = 0;
			parentFittness[i] = -1;
		}
		for (int i = 0; i < population.size() * 6 / 10; i++) {
			int index = Helpers.getRandomNumberInRange(0, population.size() - 1);
			for(int j = 0; j < 2; j++) {
				double f =population.get(index).totalFittness;
				if (f > parentFittness[j]) {
					boolean isExist = false;
					double min = Integer.MAX_VALUE;
					int minIndex = 0;
					for(int y= 0; y < 2; y++) {
						if(parentIndex[y] == index) {
							isExist = true;
							break;
						}
						if(parentFittness[y] < min) {
							min = parentFittness[y];
							minIndex = y;
						}
					}
					if(!isExist) {
						parentIndex[minIndex] =index;
						parentFittness[minIndex]= f;
						break;
					}
				}				
			}	
		}
		
		return parentIndex;
	}

	public static void removeChromosome(int number) {	
		ArrayList<Integer> kickedIndex = new ArrayList();
		ArrayList<Double> kickedFittness = new ArrayList();
		for(int i = 0; i <number;i++) {
			kickedFittness.add(i, new Double(maxFittness));
			kickedIndex.add(i, new Integer(-1));
		}
		for (int i = 0; i < population.size() * 2 / 10; i++) {
			int index = Helpers.getRandomNumberInRange(0, population.size() - 1);
			for(int j = 0; j < number; j++) {
				double f =population.get(index).totalFittness;
				if (f < kickedFittness.get(j)) {
					boolean isExist = false;
					double max = 0;
					int maxIndex = 0;
					for(int y= 0; y < number; y++) {
						if(kickedIndex.get(y) == index) {
							isExist = true;
							break;
						}
						if(kickedFittness.get(y) > max) {
							max = kickedFittness.get(y);
							maxIndex = y;
						}
					}
					if(!isExist) {
						kickedIndex.remove(maxIndex);
						kickedFittness.remove(maxIndex);
						kickedIndex.add(new Integer(index));
						kickedFittness.add(new Double(f));
						break;
					}
				}				
			}	
		}
		Collections.sort(kickedIndex, Collections.reverseOrder());
		
		for (int j = 0; j < number; j++) {
			population.remove(kickedIndex.get(j).intValue());
		}
}

	public static void initializePopulation() {
		bestFittness = 0;
		best = null;
		totalPopulationFittness = 0;
		population.clear();
		for(int i =0; i < initialPopulationSize; i++) {
			Chromosome c = new Chromosome(true);
			population.add(c);
			c.getTotalFittness();
			totalPopulationFittness+= c.totalFittness;
			if (c.totalFittness > bestFittness) {
				bestFittness = c.totalFittness;
				best = c;
			}	
		}

	}

	public static void solveGenetics(ArrayList<Chromosome> p) {
		int steadyCount = 0;
		double prevBestFittness = -1;
		population = p;
		int count =0;
		for (int i = 0; i < maxIterations && bestFittness < maxFittness; i++) {
			if (i % 5000 == 0) {
				System.out.println(bestFittness + "   " + i + "   size" + population.size());
				if(prevBestFittness == bestFittness) {
					steadyCount++;
					if(steadyCount == 6 && bestFittness < 60) {
						initializePopulation();
						solveGenetics(population);
						break;	
					}
				}
			}

			int parentsIndex[] = selectTwoParents();
			if (Math.random() < crossOverRate) {
				Chromosome childs[] = crossOver(population.get(parentsIndex[0]), population.get(parentsIndex[1]));
				if (childs[0].totalFittness > bestFittness) {
					bestFittness = childs[0].totalFittness;
					best = childs[0];
				}
				if (childs[1].totalFittness > bestFittness) {
					bestFittness = childs[1].totalFittness;
					best = childs[1];
					
				}
				population.add(childs[0]);
				population.add(childs[1]);
			}
			if (Math.random() < mutationRate) {
				Chromosome child = mutation(population.get(parentsIndex[0]));
				if (child.totalFittness > bestFittness) {
					bestFittness = child.totalFittness;
					best = child;
				}
				population.add(child);
			}
			if (population.size() > maximumlPopulationSize) {
				removeChromosome(population.size() - maximumlPopulationSize);
			}
			prevBestFittness = bestFittness;
			
		}
		System.out.println("best" + bestFittness);
		totalPopulationFittness =0;
		for(int y= 0; y < population.size();y++) {
			totalPopulationFittness+= population.get(y).totalFittness;
			if (Math.floor(population.get(y).hardFittness) == 60.0) {
				count++;
			}
		}
		
		System.out.println("average fittness "+ totalPopulationFittness/population.size());
		System.out.println("count "+ count);
		Chromosome.printByDoctors(best);
		Chromosome.printByRooms(best);

	}

}
