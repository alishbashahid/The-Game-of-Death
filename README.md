# The Game of Death
## Overview

In this project, I have created a proof of concept for an extensible simulation framework that will be used to model the movement of humans within their habitats. This will be helpful to generate statistics on how the pandemic is spread with varying human behaviors.
The project is designed using Java as it is an object-oriented language that allows us to create modular programs and reusable code.

## Solution Design

The scope of this project includes:

- Designing an object-oriented model
- Coding the solution
- Testing the solution


### Designing an object-oriented model
>Time spent: 2 hours

First step in developing the framework was to design an object-oriented model of the framework that will serve as a base structure in the development process. This model is developed by keeping the fact in mind that the framework will be extended in the future and more attributes will be added to it.
![UML](https://github.com/alishbashahid/The-Game-of-Death/blob/master/UML/Diagram.png)

#### Java Classes
| Classes | Purpose |
| ------ | ------ |
| Entity |  It is an abstract class that represents an object in the habitat at the location represented by homeLocation (Coordinates). |
| Human | It is derived from the Entity class and represents a human entity in the habitat with some unique attributes such as immunity and currentLocation.|
| WorkingHuman | Represents a human entity in a habitat that may move between home and office. Unlike human, it is not static at one location. It is Derived from Human class. |
| Office | Represents office in a habitat where a working human may come and go from. An office can hold multiple humans, unlike any other cell where there can be only one human at a time. It is derived from the Entity class.|
| DiseaseSimulation |  An abstract base class that can be extended to create any disease simulation of choice. It holds a list of entities and habitat size, with two abstract functions **tick** (used to run simulation step by step) and **initialize** (used to initialize habitat.)|
| Covid19Simulation | Inherited from DiseaseSimulation this class represents an implementation of COVID-19 simulation in a habitat based on rules defined. It holds information about offices,humans, and working humans. Manages the movement of working humans and their infections and their spreads.|
|Coordinates|This class is created in order to represent the location of an entity at any point in a habitat. Every entity has coordinates.|
|Driver|It is the main class that is used to run the simulation based on passed parameters. It contains a reference to DiseaseSimulation.|

### Coding the solution
>Time spent: 6-7 hours

After designing, the next step is to code the model and program the logic to run our simulation framework.
#### Initialization of Humans and Offices:
Initialization of humans and offices is done in a similar pattern, that is according to the following pseudocode:
```sh
block_size = getBlockSize(n,o) //Calculate the block size of small blocks such that ‘o’ or ‘h’ number of small blocks may fit in the habitat of size n2 without overlap
 
// Keep going until all the humans or offices are initialized
while (o or h)>0:
        	// Starting from top right initialize start_x, start_y, end_x, end_y.
        	start_x = 0
        	start_y = 0
        	end_x = block_size
        	end_y = block_size
 
        	// Randomly pick X and Y from block represented by parameters in previous step.
        	x = Random(start_x, end_x)
        	y = Random(start_y, end_y)
 
        	// Update the block such that it moves horizontally in the habitat,
        	start_x = end_x
        	end_x += block_size.
 
        	// If block has reached its end horizontally update the block so that it moves down vertically
        	if end_x>n:
                    	//Reset start_x and end_x to begining of the habitat
                    	start_x = 0
                    	end_x = block_size
 
                    	//Update start_y and end_y to move down the block vertically
                    	start_y += block_size
                    	end_y += block_size
                    	
                    	// If end is reached vertically Reset start_x, end_x, start_y and end_y so that block is again at start of the habitat.
                    	if end_y>n
                                	start_x = 0;
                                	end_x = block_size;
                                	start_y = 0;
                                	end_y = block_size;
                                	
        	if x,y are taken:
                    	continue
 
        	Create office/human object with coordinates x and y.

```
#### Movement of Working Humans:
Working Humans either go in x-direction or y-direction based on random decisions towards their destination. If they don’t have to go in x-direction anymore they move towards y-direction and vice versa. Humans can move through offices to get to the destination. If either direction x or y is blocked by another human, the human moves in direction x or y whichever is available. If both the directions are blocked no movement occurs. Once the human reaches its destination, either home or office it switches its destination to the other place and in the next tick, it begins moving towards it.
 
#### Checking infections:
After movement is complete all of the humans are checked in their vicinity of 8 adjacent cells for infected humans, if there are any, the human is infected on the basis of it’s immunity for t ticks
 
#### Updating Infection:
After each tick, the time of every infected human is reduced by 1.

### Testing the solution
>Time spent: 2 hours

| Test # | n | h | w | o | t | p |
| ------ | ------ | ------ | ------ | ------ | ------ | ------ |
| 1 | 5 | 4 | 2 | 2 | 30 | 1% |
| 2 | 10 | 8 | 4 | 4 | 30 | 1% |
| 3 | 100 | 50 | 25 | 10 | 30 | 1% |
| 4 | 1000 | 1000 | 750 | 100 | 30 | 1% |
| 5 | 10000 | 10000 | 2000 | 300 | 30 | 1% |

**Example:**
```sh
Java -jar “The Game of Death.jar” 10000 10000 2000 300 30
```

## Running the application
Application can be run from jar.

```sh
Usage: <Integer:n> <Integer:h> <Integer:w> <Integer:o> <Integer:t> || [Float:p]
```
**Example:**
```sh
Java -jar “The Game of Death.jar” 10000 10000 2000 300 30 10.0
```

## Qualitative Attributes

The solution contains the following non-functional requirements:

#### Readability:
Code is well commented and code logic is distributed evenly throughout the project source code.
#### Performance:
The solution is deadlock-free, and there is no algorithm that has quadratic or cubic runtime complexities such as O(n^2) or O(n^3) or any other higher-order runtime complexity. So the solution runs without consuming much execution time. Since no 2D array of redundant values is used, memory consumption is also very optimal.

## Thought process behind my choices
There were many important decisions that I had to made like the choice of programming language, choice of data structures and I also had to consider the constraint of memory and time.
One of the problems that I faced was the initialization of habitat using a 2D array.
Since there is limited memory available, so for larger values of n, the 2D space is bound to consume huge amount of memory costing the solution poor memory performance. So, instead of creating a 2D array of nxn, I used hash maps where the key is the coordinates of entities in the habitat and values are the entities themselves.
There were two benefits that I considered i.e we can reference each entity by its coordinates, hence helping us checking for infected in the vicinity of humans directly and the memory is only consumed by the entities and not by empty spaces.

The other problem that I faced was with the placement of offices in the habitat. If the size of habitat is small and the number of entities is large, it will result in very high chances of a collision. So to solve this problem we divided the habitat into equal blocks of any size such that o number of office or h number of humans can be randomly fit in each small block without collision as in each block there can be only one human or office. The advantage of this solution is that we get the minimum number of collision, no matter the size of the habitat or size of entities. The second advantage is the entities are evenly distributed across the habitat.

