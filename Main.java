/*
  Name: Sonya Fucci
  File: Main.java

  Desc: Main program that triangulates convex polygons with n vertices.

*/

import java.util.Arrays;
import java.util.Iterator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main{

  public static final int ARRAY_DATA_SIZE = 20; //how many vertices we are going to store
  //(just for calculating the vertices for the map)

  public static void main(String[] args){

    /*Stores the triangulations.
      A triangulation is a TreeSet of Edges.
    */
    TreeMap<Integer, TreeSet<TreeSet<Edge>>> triangulations = new TreeMap<Integer, TreeSet<TreeSet<Edge>>>();

    /* Setting up base case for n=3
    Even though n=3 does not have a triangulation,
    we need an edge in the TreeMap{3} slot to get n=4 up and running.

    */
    TreeSet<Edge> tri = new TreeSet<Edge>(new EdgeComparator());
    tri.add(new Edge(0,2));
    TreeSet<TreeSet<Edge>> three = new TreeSet<TreeSet<Edge>>(new TreeSetEdgeComparator());
    three.add(tri);
    triangulations.put(3, three);

    // vertices that make up the n-gon
    Vertex[] vertices = new Vertex[ARRAY_DATA_SIZE];
    vertices[0] = new Vertex(0);
    vertices[1] = new Vertex(1);
    vertices[2] = new Vertex(2);

    //will start by triangulating n=4
    int n = 3;

    //where the triangulation actually starts
    while(n < 20){ //attempts to triangulate a lot. doesn't quite make it that far.
      n++;
      //adding the most recent last vertex to the list of vertices we are working with
      vertices[n-1] = new Vertex(n-1);

      int numTriangulations = 0;

      //starting vertices of diagonals we are splitting the n-gon with
      int startIndex1 = 0;
      int startIndex2 = 2;

      //We use this to determine when we are done generating diagonals
      IndexIterator it = new IndexIterator(startIndex1, startIndex2, n);

      TreeSet<TreeSet<Edge>> nTri = new TreeSet<TreeSet<Edge>>(new TreeSetEdgeComparator());
      triangulations.put(n, nTri); //has to start non-null or else we will get duplicates. This gets replaced
      while(it.hasNextIndex()){ //while we haven't run out of diagonals to split the polygon by
          //getting current information for chosen diagonal
          startIndex1 = it.getCurrentIndex1();
          startIndex2 = it.getCurrentIndex2();

          /*
            The maps left, right are very important in this code. We divide our original n-gon
            into two sub-polygons of n. In order to get the previously calculated triangulations
            for these n-gons, we need to make sure we have consistent vertices.

          */
          HashMap<Integer, Integer> left = getLeftMap(startIndex1, startIndex2);
          HashMap<Integer, Integer> right = getRightMap(startIndex2, startIndex1, n);

          //sneaky way of determining the size of the polygons
          int leftSize = left.size();
          int rightSize = right.size();


          /*
            We have effectively split the polygon into two with a diagonal.
            The next task is to look up the triangulations for the smaller sub-polygons
            and create all the new possible triangulations for them.

            The nested loop is accessing the triangulations from the triangulations array,
            because each triangulation needs one triangulation of the left,
            and one triangulation from the right

          */

          //how we access each of the prior triangulations
          Iterator<TreeSet<Edge>> leftSideIt = triangulations.get(leftSize).iterator();
          Iterator<TreeSet<Edge>> rightSideIt = triangulations.get(rightSize).iterator();

          while(leftSideIt.hasNext()){
            TreeSet<Edge> leftSide = leftSideIt.next();
            while(rightSideIt.hasNext()){

              //create a polygon with triangulations from a triangulation on the left,
              //and a triangulation on the right

              TreeSet<Edge> triangulation = makeTriangulation(triangulations, left, right, startIndex1, startIndex2, n,
              leftSide, rightSideIt.next());

              //This is unfortunately very duplicate prone (as is the big challenge of this assignment)
              if(!containsTriangulation(triangulation, triangulations, n)){
                //adding the triangulation to our working list of triangulations
                nTri.add(triangulation);
                int ithTri =  numTriangulations +1;
                if(n < 7){

                  System.out.println("Triangulation #"+ ithTri + " for n = "+n+": ");
                  System.out.println(printTriangulation(triangulation));

                }
                //making sure it is correctly sorted
                //Arrays.sort(triangulations, arrayDataStartIdx, arrayDataStartIdx+numTriangulations+1, new TriangulationComparator());
                numTriangulations ++;
              }

            }
          }
          //finding a new diagonal, rinse and repeat
          it.updateIndices();
      }

      //printing out the final results
      System.out.println("\nNumber of Triangulations for n = "+ n+ ": " + numTriangulations);
      triangulations.put(n, nTri);
    }
  }

  /*
    Non-wrapping method to get the vertices of the polygon to the left of
    the diagonal
    @param start = the starting index of the diagonal we are using to split the polygon
    @param end = the ending index of the diagonal we are using to split the polygon

    @return hash map where the key is the"ith" index of the polygon,
     and the value is the original index.
  */
  static HashMap<Integer, Integer> getLeftMap(int start, int end){
    HashMap<Integer, Integer> tmpMap = new HashMap<Integer, Integer>();
    int i = start;
    int counter = 0;
    while(i <= end){
      tmpMap.put(counter, i);
      counter++;
      i++;
    }
    return tmpMap;
  }

  /*
    Wrapping method to get the vertices of the polygon to the left of
    the diagonal
    @param start = the starting index of the diagonal we are using to split the polygon
    @param end = the ending index of the diagonal we are using to split the polygon

    @return hash map where the key is the "ith" index of the polygon,
     and the value is the original index.
  */
  static HashMap<Integer, Integer> getRightMap(int start, int end, int n){
    HashMap<Integer, Integer> tmpMap = new HashMap<Integer, Integer>();
    int i = start;
    int counter = 0;
    while(i != end+1){
      tmpMap.put(counter, i);
      counter++;
      i = (i+1)%n;
    }
    return tmpMap;
  }

  /*
    Takes two partial triangulations, vertices of a diagonal,
      converts the vertices from the sub-triangulations,
      and returns the final triangulation.

     @param triangulations: master hashmap for all triangulations.
     @param leftMap: the hashmap that will assist with converting triangulations[leftIdx]'s vertices to our vertices
     @param rightMap: the hashmap that will assist with converting triangulations[rightIdx]'s vertices to our vertices
     @param newEdgeA: position of first vertex in diagonal we are splitting
     @param newEdgeB: position of second vertex in diagonal we are splitting
     @param n: size of polygon we are triangulating
     @param leftTri: triangulation of the left half
     @param rightTri: triangulation of the right half

     @return: TreeSet<Edge> that make up a valid triangulation
  */
  static TreeSet<Edge> makeTriangulation(TreeMap<Integer, TreeSet<TreeSet<Edge>>> triangulations,
   HashMap<Integer, Integer> leftMap, HashMap<Integer, Integer> rightMap, int newEdgeA,
   int newEdgeB, int n, TreeSet<Edge> leftTri, TreeSet<Edge> rightTri){

    TreeSet<Edge> tmpTri = new TreeSet<Edge>(new EdgeComparator());

    tmpTri.add(new Edge(newEdgeA, newEdgeB)); //adding in the diagonal

    Iterator<Edge> itLeft = leftTri.iterator();
    while(itLeft.hasNext()){ //has n-3 diagonals
      Edge tmpDiagonal = itLeft.next();
        tmpTri.add(new Edge(leftMap.get(tmpDiagonal.getAInt()), leftMap.get(tmpDiagonal.getBInt())));
    }
    //constructing the diagonal on the right
    Iterator<Edge> itRight = rightTri.iterator();
    while(itRight.hasNext()){ //has n-3 diagonals
      Edge tmpDiagonal = itRight.next();
        tmpTri.add(new Edge(rightMap.get(tmpDiagonal.getAInt()), rightMap.get(tmpDiagonal.getBInt())));
    }
     return tmpTri;
  }
  /*
    Quasi-unnecessary, but convenient/prettier.
    @param triangulation we want to print
    @return string with triangulation diagonals printed
  */
  public static String printTriangulation(TreeSet<Edge> triangulation){
    return triangulation.toString();
  }

  /*
    Helper method to determine whether we can include a triangulation or it has been duplicated.
    @param triangulation: the triangulation we are trying to determine whether it exists.
    @param tri: master TreeMap with all triangulations
    @param n: size of n-gon we are triangulating

    @return bool. returns false if triangulation will not be a duplicate.
  */
  public static boolean containsTriangulation(TreeSet<Edge> triangulation,
  TreeMap<Integer, TreeSet<TreeSet<Edge>>> tri, int n){
    return tri.get(n).contains(triangulation); //needs to access the TreeSet<TreeSet<Edge>> for the correct n
  }

}
