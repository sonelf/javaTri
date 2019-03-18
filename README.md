Sonya Fucci README for Triangulation finder/counter

How to compile:

javac*.java

How to run:

java Main

Design Approach:

My approach was to save all the previously generated triangulations in a large array. When finding a triangulation, my algorithm splits the n-gon with a diagonal that is guaranteed to be in the triangulation. This creates two smaller polygons, the triangulations of which we have already found. We can now access the triangulations from the first half, the diagonal we are splitting the n-gon by, and then accessing the triangulations from the second half.

This process generates all valid triangulations, but then it needs to be checked for a duplicate, which is perhaps the most time consuming element.

Implementation Approach:

I store my triangulations in one massive array. In order to attempt to speed things up,
I made sure that the triangulations(arrays of edges) were sorted, and that in addition, the triangulations in the main array are sorted themselves. This is very time consuming, but if the triangulations are not sorted, then it is even slower to check whether a triangulation is a duplicate. The advantage of sorting is that I can use binary search, making detecting an already existing triangulation that much faster.

I keep the meta data from the array in a separate, shorter array. It refers to where in the master array (with all the triangulations) the triangulations for a n-gon start and end.

Results:

Things started getting slow around n=12. It took around two minutes to run it for n= 12= 16796 vertices.
So far for n = 13, it has been around 40 minutes. 

Areas for improvement:

An improvement to save storage I was considering was to make the triangulations master array hold an object that inside of it has an array. This way, once the size of the triangulation is actually known, then we will create this object instead of just filling a pre-set array.

I was originally writing my code in C, but I discovered some issues with my approach in my C program. I was focusing too much on the simpler cases (for n = 4 and n = 5, for example, where there is only one unique triangulation rotating around), and lead me into having several issues to the point that the benefit of the pointers/accessing memory directly didn't really have its benefits anymore. So I returned to java where I could use some of the library data structures to get something up and running.
