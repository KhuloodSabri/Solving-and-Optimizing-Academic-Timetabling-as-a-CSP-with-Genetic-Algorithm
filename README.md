# Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm

## Introduction
Developing timetables for scheduling courses of 
educational institutions is considered to be one of 
the most exhausting and well-known search 
problems. Due to its huge state space, it cannot be 
solved using classical search methods. There are 
various approaches to solve it. Experiments have 
shown that local search methods give reasonable 
solutions to this problem. This paper will discuss an 
approach of representing timetabling as a constraint 
satisfaction problem that is then solved using the 
Genetic Algorithm (GA).

## Problem specification
University timetabling problem could be defined as 
a set of constraints that are usually divided into two 
categories: “hard” and “soft”. Hard constraints are 
rigidly enforced while Soft constraints are those that 
are desirable but not absolutely essential. These 
constraints need to be satisfied with limited, 
available resources. Basically, there are four 
elements that compromise university timetable: 
courses, lecturers, classrooms, and time slots. The input provided to 
this problem is: 
* A list of all courses and labs
* A list of specific faculty members with a list of five 
favorite courses and five labs for each 
instructor.
* A list of time slots, where classes in 
Sundays, Mondays and Wednesday are one hour, classes in Tuesdays nad Thursdays are 1.5 
hours, and labs are three hours. The labs can 
be assigned on Sundays, Mondayorand Wednesday at 2:00 or on Tuesdays or Thursdays at 8, 11, 
or 2.
* A list of available rooms and labs available.

## Problem Formalization
### Chromosome Representation
To solve the timetabling problem using genetic 
algorithm, the schedule elements need to be 
represented as a chromosome. In our chromosome 
representation, every gene header represents a 
course’s section. The value of gene is a sequence of 
three values: Instructor, Room and Time Slot. Figure 
1 illustrates this structure.

![alt text](https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/image1.jpg?raw=true)
Figure 1: Chromosome Representation



According to this representation, the length of the 
chromosome is given by the following formula:
𝑪𝒉𝒓𝒐𝒎𝒐𝒔𝒐𝒎𝒆 𝑳𝒆𝒏𝒈𝒕𝒉 = ∑𝑺𝒄𝒊 ∗ 𝟑
𝒏
𝒊=𝟎
Where n is the number of courses and S is the 
number of sections for course ci.
3.2. Population
An initial population of size 1000 is generated. Each 
section is assigned randomly to one instructor who
prefers to teach this course. This constraint helps in 
increasing the fitness of the chromosome and so 
decreases the number of iterations needed to 
converge to an acceptable solution. The other two 
values of gene (room, time slot) are assigned 
completely to random values. Through genetic 
algorithm, Population grows to a maximum size of 
3000. When the population size exceeds 3000, a 
number of chromosomes are removed according to 
a criterion discussed later, in order to maintain the 
size limit to 3000 chromosomes.
3.3. Parents Selection
Two chromosomes are selected every iteration to 
generate two new children (off-springs). The 
tournament algorithm is used to determine the 
parents. In this algorithm, a random sub-group is 
selected from the population and the best two 
chromosomes of it are chosen. The size of the subgroup here is 60% of the population size. By 
experiment, smaller sizes give worse results since 
the randomness in selection increases. 
3.3. Crossover Operators
Crossover is applied with a high rate equal to 0.6 in 
order to continuously generate new off-springs. 
Two types of crossover were used with equal 
probabilities (0.5 for both):
• Multi Point Crossover: where two points are 
chosen randomly, and the segment between 
these two points is swapped between the two 
parents. 
Figure 2: Multipoint Crossover
• Uniform Crossover: where randomly some 
genes from different locations are swapped
between the two parents.
Figure 3: Uniform Crossover
These two types of crossover are used rather than 
the classical one-point cross over, in order to 
explore more solutions. Experimentally, they give 
better results in our problem.
3.4 Mutation Operators
Mutation is used with a lower rate equal to 0.3, in 
order to avoid convergence to local optima and get 
new children. As in crossover, two types of mutation 
are used with equal probabilities:
• Replace one randomly selected gene in the 
parent, by another randomly generated gene.
• Replace one randomly selected value of a gene 
in the parent by another randomly generated 
value. (i.e. change either instructor, time slot or 
room value of a gene rather than changing the 
whole gene).
3.5. Removing Criteria
As said earlier, there is a need to get rid of some 
chromosomes in order to avoid infinitely growing 
population. Here, the tournament algorithm is used 
again but this time to decide which chromosomes 
will be removed. Appling this algorithm leads to 
removing the worst chromosomes of a randomly
selected sub-group. The purpose is to get some 
randomness but never remove chromosomes with 
really good fitness. The determined size of the 
selected subgroup is 20% of the population size. 
Larger sizes experimentally do not lead to any better 
results.
