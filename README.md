Sonya Fucci README for Triangulation finder/counter

How to compile:

javac*.java

How to run:

java Main

Design Approach:

My approach was to save all the previously generated triangulations. When finding a triangulation, my algorithm splits the n-gon with a diagonal that is guaranteed to be in the triangulation. This creates two smaller polygons, the triangulations of which we have already found. We can now access the triangulations from the first half, the diagonal we are splitting the n-gon by, and then accessing the triangulations from the second half.

This process generates all valid triangulations, but then it needs to be checked for a duplication issues, which is perhaps the most time consuming element.

Implementation Approach:

I store my triangulations in a TreeMap (from the Java libraries). The keys for the map are the integers that represent the n-gons I am triangulating. The values are of TreeSet<TreeSet<Edge>> datatype. This is, in plain English, a set of (sets of edges). In other words, this is a set of the triangulations. I used this because they can be inserted sorted, so it makes it *that* much faster to check for duplicates in some cases if both the triangulations and edges are sorted. I got the idea from Ruby to use TreeSets for this assignment, but I had to use TreeMaps in addition so that I could quickly retrieve my sub-solutions.

I realized I had a key issue I needed to resolve when accessing the triangulations from lower indices (ie, getting the triangulations from the sub-polygons after I split from a diagonal). For example, consider a 6-gon being split into the triangle (0,1,2) and pentagon (2,3,4,5,0) (in this case, startIndex1 = 0, startIndex2 = 2). The 5-gons are stored in triangulations only in terms of their vertices: (0,1,2,3,4). So when we access the triangulations for the 5-gon, we need to to convert them from the diagonals of the (0,1,2,3,4)-gon into the diagonals of the (2,3,4,5,0)-gon. In order to deal with this problem, I create a HashMap that, for a sub-polygon, translates the vertices in the subpolygon to the vertices in a n-gon.

Results:

This algorithm makes it up to n=16. In its attempt to calculate triangulations for a 17-gon, it throws an OutOfMemoryError. it's not entirely surprising that after storing all triangulations I run out of storage.

Areas for improvement:

An improvement to save storage I was considering was to modify/write some of the library data structures I used to suit my needs a little bit better.

I was originally writing my code in C, but I discovered some issues with my approach in my C program. I was focusing too much on the simpler cases (for n = 4 and n = 5, for example, where there is only one unique triangulation rotating around), and lead me into having several issues to the point that the benefit of the pointers/accessing memory directly didn't really have its benefits anymore, considering my idea. So I returned to java where I could use some of the library data structures to get something up and running.
