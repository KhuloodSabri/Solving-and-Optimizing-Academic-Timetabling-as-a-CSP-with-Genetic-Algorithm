
# Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm

![Media1](https://user-images.githubusercontent.com/25118892/132069435-ee0e39b9-b102-42e6-b9f0-a97fe95f2454.gif)

## Table of Content
1. [Introduction](#intro)
2. [Problem specification](#prblmspec)
3. [Problem Formalization](#prblmform)
   1. [Chromosome Representation](#ChromosomeRepresentation)
   2. [Population](#Population)
   3. [Parents Selection](#ParentsSelection)
   4. [Crossover Operators](#CrossoverOperators)
   5. [Mutation Operators](#MutationOperators)
   6. [Removing Criteria](#RemovingCriteria)
   7. [Fitness Function](#FitnessFunction)
5. [Performance and Evaluation](#eval)
6. [How to Run](#howtorun)
7. [References](#References)

<a name="intro"></a>
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

<a name="prblmspec"></a>
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

<a name="prblmform"></a>
## Problem Formalization
<a name="ChromosomeRepresentation"></a>
### Chromosome Representation
To solve the timetabling problem using genetic 
algorithm, the schedule elements need to be 
represented as a chromosome. In our chromosome 
representation, every gene header represents a 
course’s section. The value of gene is a sequence of 
three values: Instructor, Room and Time Slot.

<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/ChromosomeRepresentation.PNG" width="300">

According to this representation, the length of the 
chromosome is given by the following formula:

<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/ChromosomeLengthFormula.PNG" width="250">

Where n is the number of courses and S is the 
number of sections for course ci.

<a name="Population"></a>
### Population
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

<a name="ParentsSelection"></a>
### Parents Selection
Two chromosomes are selected every iteration to 
generate two new children (off-springs). The 
tournament algorithm is used to determine the 
parents. In this algorithm, a random sub-group is 
selected from the population and the best two 
chromosomes of it are chosen. The size of the sub-group here is 60% of the population size. By 
experiment, smaller sizes give worse results since 
the randomness in selection increases. 

<a name="CrossoverOperators"></a>
### Crossover Operators
Crossover is applied with a high rate equal to 0.6 in 
order to continuously generate new off-springs. 
Two types of crossover were used with equal 
probabilities (0.5 for both):
* Multi Point Crossover: where two points are 
chosen randomly, and the segment between 
these two points is swapped between the two 
parents. 

<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/MultipointCrossover.PNG" width="400">

* Uniform Crossover: where randomly some 
genes from different locations are swapped
between the two parents.

<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/UniformCrossOver.PNG" width="400">

These two types of crossover are used rather than 
the classical one-point cross over, in order to 
explore more solutions. Experimentally, they give 
better results in our problem.

<a name="MutationOperators"></a>
### Mutation Operators
Mutation is used with a lower rate equal to 0.3, in 
order to avoid convergence to local optima and get 
new children. As in crossover, two types of mutation 
are used with equal probabilities:
* Replace one randomly selected gene in the 
parent, by another randomly generated gene.
* Replace one randomly selected value of a gene 
in the parent by another randomly generated 
value. (i.e. change either instructor, time slot or 
room value of a gene rather than changing the 
whole gene).

<a name="RemovingCriteria"></a>
### Removing Criteria
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

<a name="FitnessFunction"></a>
### Fitness Function
Fitness function evaluates how close a given 
solution is to the optimum solution of the desired 
problem. Each chromosome is given a score out of 
maximum fitness 100%. 60% of this fitness is given 
to the hard constraints (equally weighted -15% for 
each) and 40% to the soft constraints (distributed 
subjectively between them). Soft fitness is not 
computed till all hard constraints are satisfied. 
Otherwise, their fitness value has no meaning.

#### Hard Constraints
1. There are no conflicts in assigning courses for 
the same instructor. The mathematical 
measurement of this constraint is defined by 
the equation:

&emsp;&emsp;<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/FirstHardConstraintFormula.PNG" width="400">

2. The assigned courses and labs for each 
instructor are from his list of favorites. 

&emsp;&emsp;<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/SecondHardConstraintFormula.PNG" width="400">

&emsp;&emsp;<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/Second2HardConstraintFormula.PNG" width="400">

where
* n: total number of sections for all courses.
* C: Course.
* S: Section.
* Di: Instructor assigned to section Si.

3. Each instructor teaches at least 6 courses
 hours.
 
&emsp;&emsp;let n to be number of instructors, and Xi be the 
number of hours assigned to instructor i.

&emsp;&emsp;<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/ThirdHardConstraintFormula.PNG" width="300">

4. Each instructor can teach at least 12 
hours/weak and at max 18 hours/weak. Each 
course has three hours while the lab has 2 hours.

&emsp;&emsp;<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/FourthHardConstraintFormula.PNG" width="250">

5. No four or more consecutive lectures (without 
breaks) are allowed for the same instructor.

&emsp;&emsp;let n to be number of instructors, and Xi be the 
maximum number of consecutive hours for 

&emsp;&emsp;<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/FifthHardConstraintFormula.PNG" width="250">

#### Soft Constraints
1. Minimizing the number of courses of the 
same level that have the same time slot.
&emsp;&emsp;Let L be the number of levels, Xi the number of 
conflicted sections in level i, and Si number of 
sections in level i.

&emsp;&emsp;<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/FirstSoftConstraintFormula.PNG" width="350">

2. Reducing the number of lectures at 8:00.
&emsp;&emsp;Let n be the number of instructors and Xi the 
number of days start at 8:00 for instructor i.

&emsp;&emsp;<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/SecondSoftConstraintFormula.PNG" width="300">

3. Minimize waiting time between lectures.
&emsp;&emsp;Let n be the number of instructors, Bi be the 
total break hours for instructor i, and Li be the 
total lecture hours per week for instructor i.

&emsp;&emsp;<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/ThirdSoftConstraintFormula.PNG" width="350">

4. Making one day off for each instructor.
&emsp;&emsp;Let N be the number of instructors, and Xi be 
the number of off-days per week for instructor i.

&emsp;&emsp;<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/FourthSoftConstraintFormula.PNG" width="250">

5. Minimizing the number of rooms reserved from outside the building of Computer Engineering department in our university called Al-Masri building.

&emsp;&emsp;<img src="https://github.com/KhuloodSabri/Solving-and-Optimizing-Academic-Timetabling-as-a-CSP-with-Genetic-Algorithm/blob/main/imgs/FifthSoftConstraintFormula.PNG" width="400">

<a name="eval"></a>
## Performance and Evaluation
Our program usually satisfies all the hard 
constraints in around 12000 iterations (fitness = 60). 
It usually satisfies 85% of the hard and soft 
constraints in less than 15000 iterations, and 90% of 
them in about 35000 iterations. After that, the 
progress becomes very slow, it needs another 35000 
iterations to increase the fitness by only 2% and 
reach 92%. 90% however is really a good and very 
satisfying result in our case since a 100% 
satisfaction is not even a possible solution for many 
reasons. One of them is that some constraints limits 
the possibility of satisfying others. For example, in 
order not to give an instructor more than three 
consecutive busy hours, the program has to give him 
a break in between, which affects the efficiency of 
minimizing break hours. So, our program tries to 
find a tradeoff between these constraints. Another 
reason is that some constraints cannot be satisfied 
completely. For example, since the number of 
courses in the same level is large it has to be some 
conflicts between them. Actually, it is not even 
desired not to have any conflicts at all. One problem 
that faced our program, is that it sometimes 
converged to a local optimum while the fitness was 
still very small. However, this problem is solved 
easily by random restart. Our program counts the 
number of iterations which did not come with any 
progress. If the count is over some limit (30000 
iterations), and the fitness is not good enough yet 
(less than 60), the current population is replaced by 
a new initial population.

<a name="howtorun"></a>
## How to Run
* Make sure you have javafx installed
* Clone the project
* Open this path (Codes\projectAI_lastVersion\) in Eclipe 
* You need three input txt files (you can find examples in Codes\projectAI_lastVersion\)
  * Courses -> Each line represents {COURSE NAME},{COURSE CODE},{NUMBER OF SECTIONS}
  * Instructors -> Each line represents {INSTRUCTOR NAME}-{CODES OF HIS FAVOURITE COURSES SEPERATED BY COMMAS}
  * Rooms -> Each line represents:
    * If it is a normal room: {ROOM NAME},lecture
    * If it is a laboratory: {ROOM NAME},lab,{CODE OF LAB COURSE THAT CAN BE TAKEN IN THIS LABORATORY} 
 * Run the code :) 

<a name="References"></a>
## References
1. http://www.secretgeek.net/content/bambrilg.pdf , 
accessed on Nov 23,2018.
2. https://www.academia.edu/2876050/Solving_Unive
rsity_Timetabling_As_a_Constraint_Satisfaction_P
roblem_with_Genetic_Algorithm, accessed on 23
Nov, 2018.
3. https://towardsdatascience.com/introduction-togenetic-algorithms-including-example-codee396e98d8bf3, Nov 25,2018.
4. https://www.tutorialspoint.com/genetic_algorithms/
accessed on Nov 25, 2018.
5. https://towardsdatascience.com/how-to-define-afitness-function-in-a-genetic-algorithmbe572b9ea3b4, Nov 28,2018

