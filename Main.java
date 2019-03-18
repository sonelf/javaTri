import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;


public class Main{
  public static final int TRI_SIZE = 100000;
  public static final int DIAG_SIZE = 20;
  public static final int DIAG_NUM = 10;
  public static final int[] catalan = {1, 1, 2, 5, 14, 42, 132,
429, 1430, 4862, 16796, 58786, 208012, 742900, 2674440,
 9694845, 35357670, 129644790, 477638700, 1767263190};

  public static void main(String[] args){

    //providing n = 4 and n = 5 as base cases because they both have one set of unique diagonals
    // (as in, only one pair of rotateable vertices)
    Edge[][] triangulations = new Edge[TRI_SIZE][10];
    ArrayData[] triangulationsData = new ArrayData[DIAG_SIZE];

    Edge[] threeOne = new Edge[DIAG_NUM];
    threeOne[0] = new Edge(new Vertex(0), new Vertex(2));

    Edge[] fourOne = new Edge[DIAG_NUM];
    fourOne[0] = new Edge(new Vertex(0), new Vertex(2));
    Edge[] fourTwo = new Edge[DIAG_NUM];
    fourTwo[0] = new Edge(new Vertex(1), new Vertex(3));

    Edge[] fiveOne = new Edge[DIAG_NUM];
    fiveOne[0] = new Edge(new Vertex(0), new Vertex(2));
    fiveOne[1] = new Edge(new Vertex(2), new Vertex(4));

    Edge[] fiveTwo = new Edge[DIAG_NUM];
    fiveTwo[0] = new Edge(new Vertex(1), new Vertex(3));
    fiveTwo[1] = new Edge(new Vertex(3), new Vertex(0));

    Edge[] fiveThree = new Edge[DIAG_NUM];
    fiveThree[0] = new Edge(new Vertex(2), new Vertex(4));
    fiveThree[1] = new Edge(new Vertex(4), new Vertex(1));

    Edge[] fiveFour = new Edge[DIAG_NUM];
    fiveFour[0] = new Edge(new Vertex(3), new Vertex(0));
    fiveFour[1] = new Edge(new Vertex(0), new Vertex(2));

    Edge[] fiveFive = new Edge[DIAG_NUM];
    fiveFive[0] = new Edge(new Vertex(4), new Vertex(1));
    fiveFive[1] = new Edge(new Vertex(1), new Vertex(3));

    triangulations[0] = threeOne;
    triangulations[1] = fourOne;
    triangulations[2] = fourTwo;
    triangulations[3] = fiveOne;
    triangulations[4] = fiveTwo;
    triangulations[5] = fiveThree;
    triangulations[6] = fiveFour;
    triangulations[7] = fiveFive;

    triangulationsData[3] = new ArrayData(0, 1, 1);
    triangulationsData[4] = new ArrayData(1, 2, 3);
    triangulationsData[5] = new ArrayData(3, 5, 8);

    //n = 6:
    int n = 6;

    Vertex[] vertices = new Vertex[DIAG_SIZE];
    vertices[0] = new Vertex(0);
    vertices[1] = new Vertex(1);
    vertices[2] = new Vertex(2);
    vertices[3] = new Vertex(3);
    vertices[4] = new Vertex(4);
    vertices[5] = new Vertex(5);

    int startIndex1 = 0;
    int startIndex2 = 2;

    //get the polygon to the left of the diagonal
    // if it's a triangle, then leave startIndex1, startIndex2 in there

    //int offset = 0;
    boolean start = true;
    int numTriangulations = 0;
    int arrayDataStartIdx = triangulationsData[n-1].getEndIdx();
    while(startIndex1%n != n-2 || start == true){
      start = false;
      boolean innerStart = true;
      startIndex2 = (startIndex1+2)%n;
      while((startIndex2%n < n || innerStart == true) && startIndex2 > startIndex1 && startIndex2 - startIndex1 < n -1){
        innerStart = false;

        HashMap<Integer, Integer> left = getLeftMap(startIndex1, startIndex2);
        //System.out.println(left.toString());

        HashMap<Integer, Integer> right = getRightMap(startIndex2, startIndex1, n);
        //System.out.println(right.toString());
        int leftSize = left.size();
        //if(leftSize < 3) continue;
        System.out.println(startIndex1 + " , "+ startIndex2+ "  left size: "+ leftSize);
        int leftStartIdx = triangulationsData[leftSize].getStartIdx();
        int leftEndIdx = triangulationsData[leftSize].getEndIdx();

        int rightSize = right.size();
        //if(rightSize < 3) continue;
        int rightStartIdx = triangulationsData[rightSize].getStartIdx();
        int rightEndIdx = triangulationsData[rightSize].getEndIdx();



        for(int leftIdx = leftStartIdx; leftIdx < leftEndIdx; leftIdx++){
          for(int rightIdx = rightStartIdx; rightIdx < rightEndIdx; rightIdx++){
            numTriangulations ++;

            Edge[] triangulation = makeTriangulation(triangulations, leftIdx, rightIdx, left, right, startIndex1, startIndex2, n);
            //create a polygon with triangulations from triangulations[leftIdx] and triangulations[rightIdx]
          //  while(!containsTriangulation(triangulation, triangulations, arrayDataStartIdx, numTriangulations)){
            //  triangulation = shiftTriangulation(triangulation, n);
            //}
            triangulations[arrayDataStartIdx+numTriangulations] = triangulation;
            numTriangulations ++;
            System.out.println(printTriangulation(triangulation));
          }
        }

        //generate list of triangulations for right

        startIndex2 = (startIndex2+1)%n;
      }
      startIndex1 = (startIndex1 +1)%n;
    }
    System.out.println("number of triangulations: " + numTriangulations);
    int arrayDataEndIdx = arrayDataStartIdx + numTriangulations;
    triangulationsData[n] = new ArrayData(arrayDataStartIdx, numTriangulations, arrayDataEndIdx);
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


  */
  static Edge[] makeTriangulation(Edge[][]triangulations, int leftIdx, int rightIdx,
   HashMap<Integer, Integer> leftMap, HashMap<Integer, Integer> rightMap, int newEdgeA, int newEdgeB, int n){
    Edge[] tmpTri = new Edge[DIAG_NUM]; //what we will ultimately return

    Edge[] leftTri = triangulations[leftIdx];
    Edge[] rightTri = triangulations[rightIdx];
    //System.out.println("right index: "+ rightIdx + triangulations[rightIdx][0]);

    //System.out.println("leftTri: " +Arrays.toString(leftTri));
    //System.out.println("rightTri: " +Arrays.toString(rightTri));
    int idxCounter = 0;
    tmpTri[idxCounter] = new Edge(new Vertex(newEdgeA), new Vertex(newEdgeB));
    //System.out.println("tmpTri: " +Arrays.toString(tmpTri));
    idxCounter++;

    for(int i = 0; i < leftMap.size()-3; i++){ //has n-3 diagonals
      Edge tmpDiagonal = leftTri[i];
      Edge tmpDiagonalConverted = new Edge(leftMap.get(tmpDiagonal.getAInt()),leftMap.get(tmpDiagonal.getBInt()));
      boolean add = true;
      for(int j = 0; j < idxCounter; j++){
        if(tmpDiagonalConverted.equals(leftTri[i])){
          add = false;
        }
      }
      if(add){
        tmpTri[idxCounter] = new Edge(leftMap.get(tmpDiagonal.getAInt()), leftMap.get(tmpDiagonal.getBInt()));
        idxCounter++;
      }

    }

    //System.out.println("rightMap: "+ rightMap.toString());
    for(int i = 0; i < rightMap.size()-3; i++){ //has n-3 diagonals
      Edge tmpDiagonal = rightTri[i];
      //System.out.println(Arrays.toString(rightTri));
      Edge tmpDiagonalConverted = new Edge(rightMap.get(tmpDiagonal.getAInt()),rightMap.get(tmpDiagonal.getBInt()));
      boolean add = true;
      for(int j = 0; j < idxCounter; j++){
        if(tmpDiagonalConverted.equals(rightTri[i])){
          add = false;
        }
      }
      if(add){
        tmpTri[idxCounter] =new Edge(rightMap.get(tmpDiagonal.getAInt()), rightMap.get(tmpDiagonal.getBInt()));
        idxCounter++;
      }

    }

    return tmpTri;
  }

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

}
