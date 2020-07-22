# The Game of Death
## Overview

In this project, I have created a proof of concept for an extensible simulation framework that will be used to model the movement of humans within their habitats. This will be helpful to generate statistics on how the pandemic is spread with varying human behaviors.
The project is designed using Java as it is an object-oriented language that allows us to create modular programs and reusable code.

## Solution Design

The scope of this project includes:
- Problem Analysis
- Designing an object-oriented model
- Coding the object-oriented in model in Java
- Testing the solution

### Problem Analysis
>Time spent: 1 hour

### Designing an object-oriented model
>Time spent: 2 hours

First step in developing the framework was to design an object-oriented model of the framework that will serve as a base structure in the development process. This model is developed by keeping the fact in mind that the framework will be extended in the future and more attributes will be added to it.
<UML>

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

