# Documentation for IJA project submission
Authors:
 - Jakub Ramašeuski (xramas01@stud.fit.vut.cz)
 - Martin Vrablec (xvrabl06@stud.fit.vut.cz)

## Building 
In the project root directory, executing
```bash
mvn clean javafx:run
```
will lead to launching the application directly (without jar compilation). Program takes no arguments

## Theory of map generation
As it may be obvious, this submission does not hold any maps within its repository. All maps are generated with a variety of steps that will be mentioned
1. first, the n+1 positions are generated
	- these positions will serve as candidates for power and lightbulb nodes
2. copies of those positions are then converted to points and interconnected with Segments.
	- By intersecting all Segments we are able to generate even more points that are then added to the list
3. these points then serve as nodes among which are generated edges
	- these edges are then filtered by a minimum spanning tree algorithm that first sorts all edges by length, then selects the shortests edges, which have start or end in the open list, but not both at the same time.
		- edges are then converted back to segments and returned to the caller of this method
4. returned segments are finally converted to link nodes by rasterizing and
5. lightbulb and power nodes are created on either the position that was assigned to them in step 1, or on a close position that is empty.
6. upon successful generation, all excessive paths are filtered (meaning that every link, that has a side that leads nowhere, or any link that only has one side, is freed of that side or deleted completely)
7. if there are any links that have 4 sides, random ammount of them is erased
	- if not all lightbulbs are accessible after the erasure, side is put back into place.
8. these steps repeat until it generates a map that has all the lightbulbs connected to the power source
9. map/game is passed to the BoardController, which then shuffles the nodes