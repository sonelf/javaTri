/*
  Name: Sonya Fucci
  File: Main.java

  Desc: Main program that triangulates convex polygons with n vertices.

*/

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Main{
  public static final int TRI_SIZE = 4000000; //how many triangulations we are going to store
  public static final int ARRAY_DATA_SIZE = 20; //how many diagonals we are going to store per triangulation (max)
  public static final int DIAG_NUM = 17; //
  public static final int[] catalan = {1, 1, 2, 5, 14, 42, 132,
429, 1430, 4862, 16796, 58786, 208012, 742900, 2674440,
 9694845, 35357670, 129644790, 477638700, 1767263190};

  public static void main(String[] args){

    /*Stores the triangulations.
      Default size as defined above.
      Assuming that there will be no more than DIAG_NUM diagonals. So only sufficient for n = DIAG_NUM-3;
    */
    Edge[][] triangulations = new Edge[TRI_SIZE][DIAG_NUM];

    /*
      triangulationsData is an array that stores data about the previously calculated triangulations.
      For example, if I am calculating triangulations for n = 10,
      then I will refer to triangulations ranging from n = 3 to n = 9.
      triangulationsData[n] will tell me where I can find the triangulations
      for an n-gon in the 2D array triangulations.
      ARRAY_DATA_SIZE = 20, because this code does not currently work for any n > 20.

    */
    ArrayData[] triangulationsData = new ArrayData[ARRAY_DATA_SIZE];

    // 3 is a base case, but it has no trianuglation.
    //we just need one diagonal to get the algorithm started.

    Edge[] threeOne = new Edge[DIAG_NUM];
    threeOne[0] = new Edge(0, 2);

    triangulations[0] = threeOne;
    triangulationsData[3] = new ArrayData(0, 1, 1);


    // vertices that make up the n-gon
    Vertex[] vertices = new Vertex[ARRAY_DATA_SIZE];
    vertices[0] = new Vertex(0);
    vertices[1] = new Vertex(1);
    vertices[2] = new Vertex(2);

    //will start by triangulating n=4
    int n = 3;

    //Triangulating process:
    while(n < 20){ //attempts to go to n=20. doesn't quite work out.
      n++;
      //adding the most recent last vertex to the list of vertices we are working with
      vertices[n-1] = new Vertex(n-1);

      int numTriangulations = 0;

      //determining where to put the newest triangulations
      int arrayDataStartIdx = triangulationsData[n-1].getEndIdx();

      //that helps us find new diagonals to split the n-gon with
      //for the diagonal we will split it on
      int startIndex1 = 0;
      int startIndex2 = 2;

      //We use this to determine when we are done generating diagonals
      IndexIterator it = new IndexIterator(startIndex1, startIndex2, n);

      while(it.hasNextIndex()){ //while we haven't run out of diagonals to split by
          //getting current information for chosen diagonal
          startIndex1 = it.getCurrentIndex1();
          startIndex2 = it.getCurrentIndex2();

          /*
            The maps left, right are very important in this code. We divide our original n-gon
            into two sub-polygons of n. In order to get the previously calculated triangulations
            for these n-gons, we need to make sure we have consistent vertices.

            For example, consider a 6-gon being split into the triangle (0,1,2) and
            pentagon (2,3,4,5,0) (in this case, startIndex1 = 0, startIndex2 = 2).
            The 5-gons are stored in triangulations only in terms of their
            vertices: (0,1,2,3,4). So when we access the triangulations for the 5-gon,
            we need to to convert them from the diagonals of the (0,1,2,3,4)-gon into the diagonals
            of the (2,3,4,5,0)-gon. The HashMap is just the right tool for this.

          */
          HashMap<Integer, Integer> left = getLeftMap(startIndex1, startIndex2);
          HashMap<Integer, Integer> right = getRightMap(startIndex2, startIndex1, n);

          //sneaky way of determining the size of the left polygon
          int leftSize = left.size();
          //finding where in the grid triangulations the leftSize-gons start
          int leftStartIdx = triangulationsData[leftSize].getStartIdx();
          //finding where in the grid triangulations the leftSize-gons end
          int leftEndIdx = triangulationsData[leftSize].getEndIdx();

          //same as above, but for the polygon right of the diagonal
          int rightSize = right.size();
          int rightStartIdx = triangulationsData[rightSize].getStartIdx();
          int rightEndIdx = triangulationsData[rightSize].getEndIdx();

          /*
            We have effectively split the polygon into two with a diagonal.
            The next task is to look up the triangulations for the smaller sub-polygons
            and create all the new possible triangulations for them.

            The nested loop is accessing the triangulations from the triangulations array,
            because each triangulation needs one triangulation of the left,
            and one triangulation from the right

          */
          for(int leftIdx = leftStartIdx; leftIdx < leftEndIdx; leftIdx++){
            for(int rightIdx = rightStartIdx; rightIdx < rightEndIdx; rightIdx++){

              //create a polygon with triangulations from the triangulation at triangulations[leftIdx]
              // and triangulations[rightIdx]
              Edge[] triangulation = makeTriangulation(triangulations, leftIdx, rightIdx, left, right, startIndex1, startIndex2, n);


              //This is unfortunately very duplicate prone (as is the big challenge of this assignment)
              if(!containsTriangulation(triangulation, triangulations, arrayDataStartIdx, numTriangulations)){
                //adding the triangulation to our array
                triangulations[arrayDataStartIdx+numTriangulations] = triangulation;
                int ithTri =  numTriangulations +1;
                if(n < 7){

                  System.out.println("Triangulation # "+ ithTri + " for n = "+n+": ");
                  System.out.println(printTriangulation(triangulation));

                }
                //making sure it is correctly sorted
                Arrays.sort(triangulations, arrayDataStartIdx, arrayDataStartIdx+numTriangulations+1, new TriangulationComparator());
                numTriangulations ++;
              }

            }
          }
          //finding a new diagonal, rinse and repeat
          it.updateIndices();
      }

      //printing out the final results
      System.out.println("\nNumber of Triangulations for n = "+ n+ ": " + numTriangulations);

      //storing the metadata for the n we just computed, so n+1 and beyond can access
      int arrayDataEndIdx = arrayDataStartIdx + numTriangulations;
      triangulationsData[n] = new ArrayData(arrayDataStartIdx, numTriangulations, arrayDataEndIdx);
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
     and computes a sample triangulation of n.

     @param triangulations: master grid for all triangulations.
     @param leftIdx: where in triangulations the current triangulation for the left sub-polygon is
     @param rightIdx: where in triangulations the current triangulation for the right sub-polygon is
     @param leftMap: the hashmap that will assist with converting triangulations[leftIdx]'s vertices to our vertices
     @param rightMap: the hashmap that will assist with converting triangulations[rightIdx]'s vertices to our vertices
     @param newEdgeA: position of first vertex in diagonal we are splitting
     @param newEdgeB: position of second vertex in diagonal we are splitting
     @param n: size of polygon we are triangulating

     @return: list of edges that make up a valid triangulation
  */
  static Edge[] makeTriangulation(Edge[][]triangulations, int leftIdx, int rightIdx,
   HashMap<Integer, Integer> leftMap, HashMap<Integer, Integer> rightMap, int newEdgeA, int newEdgeB, int n){
    Edge[] tmpTri = new Edge[DIAG_NUM]; //what we will ultimately return

    Edge[] leftTri = triangulations[leftIdx]; //triangulation of polygon left of splitting diagonal
    Edge[] rightTri = triangulations[rightIdx]; //triangulation of polygon right of splitting diagonal


    int idxCounter = 0; //keeping track of where in tmpTri new vertices should go

    tmpTri[idxCounter] = new Edge(newEdgeA, newEdgeB); //adding in the diagonal
    idxCounter++;

    for(int i = 0; i < leftMap.size()-3; i++){ //has n-3 diagonals
      Edge tmpDiagonal = leftTri[i];
        tmpTri[idxCounter] = new Edge(leftMap.get(tmpDiagonal.getAInt()), leftMap.get(tmpDiagonal.getBInt()));
        idxCounter++;
    }
    //constructing the diagonal on the right
    for(int i = 0; i < rightMap.size()-3; i++){ //has n-3 diagonals
      Edge tmpDiagonal = rightTri[i];
        tmpTri[idxCounter] =new Edge(rightMap.get(tmpDiagonal.getAInt()), rightMap.get(tmpDiagonal.getBInt()));
        idxCounter++;
    }

    //ensuring the diagonals are sorted
     Arrays.sort(tmpTri, new EdgeComparator());
     return tmpTri;

  }
  /*
    Triangulation arrays have lots of null. Simple method to not include those.
    @param triangulation we want to print
    @return string with triangulation printed, with no 'null' elements appearing
  */
  public static String printTriangulation(Edge[] triangulation){
    String s = "";
    for(int i = 0; i < triangulation.length; i++){
      if(triangulation[i] == null){
        return s;
      }
      s = s + triangulation[i].toString();
    }
    return s;
  }

  /*
    Helper method to determine whether we can include a triangulation or it has been duplicated.
    @param triangulation: the triangulation we are trying to determine whether it exists.
    @param triangulations: master array with all triangulations
    @param arrayDataStartIdx: where to start in triangulations
    @param numTriangulations: helps determine how far we can go in triangulations master array.
    Each iteration this changes.
    @return bool. returns false if triangulation will not be a duplicate.
  */
  public static boolean containsTriangulation(Edge[] triangulation,
  Edge[][] triangulations, int arrayDataStartIdx, int numTriangulations){
    int triIdx = arrayDataStartIdx;
    int ending = arrayDataStartIdx + numTriangulations;

    while(triIdx < arrayDataStartIdx + numTriangulations){

      //searching for the triangulations out of the ones we have already generated
      if(Arrays.binarySearch(triangulations, arrayDataStartIdx, triIdx+1,
      triangulation, new TriangulationComparator()) >= 0){
        return true;
      }
      triIdx++;
    }
    return false;
  }

}
