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

    //Edge[] tri1 = {new Edge(0,4), new Edge(0,2), new Edge(2,4), null};
    //Edge[] tri2 = {new Edge(0,2), new Edge(4,2), new Edge(4,0), null};

    //System.out.println(equalsTriangulation(tri1, tri2));
    //System.out.println(new Edge(0,4).equals(new Edge(4,0)));
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

    Vertex[] vertices = new Vertex[DIAG_SIZE];
    vertices[0] = new Vertex(0);
    vertices[1] = new Vertex(1);
    vertices[2] = new Vertex(2);
    vertices[3] = new Vertex(3);
    vertices[4] = new Vertex(4);
    //n = 6:
    int n = 5;

    while(n < 9){
      n++;

      vertices[n-1] = new Vertex(n-1);

      int startIndex1 = 0;
      int startIndex2 = 2;

      //get the polygon to the left of the diagonal
      // if it's a triangle, then leave startIndex1, startIndex2 in there

      //int offset = 0;
      boolean start = true;
      int numTriangulations = 0;
      int arrayDataStartIdx = triangulationsData[n-1].getEndIdx();

      IndexIterator it = new IndexIterator(startIndex1, startIndex2, n);
      while(it.hasNextIndex()){
          startIndex1 = it.getCurrentIndex1();
          startIndex2 = it.getCurrentIndex2();
          HashMap<Integer, Integer> left = getLeftMap(startIndex1, startIndex2);
          //System.out.println(startIndex1 + "& " + startIndex2);

          HashMap<Integer, Integer> right = getRightMap(startIndex2, startIndex1, n);
          //System.out.println(right.toString());
          int leftSize = left.size();
          //if(leftSize < 3) continue;
          //System.out.println(startIndex1 + " , "+ startIndex2+ "  left size: "+ leftSize);
          int leftStartIdx = triangulationsData[leftSize].getStartIdx();
          int leftEndIdx = triangulationsData[leftSize].getEndIdx();

          int rightSize = right.size();
          //if(rightSize < 3) continue;
          int rightStartIdx = triangulationsData[rightSize].getStartIdx();
          int rightEndIdx = triangulationsData[rightSize].getEndIdx();



          for(int leftIdx = leftStartIdx; leftIdx < leftEndIdx; leftIdx++){
            for(int rightIdx = rightStartIdx; rightIdx < rightEndIdx; rightIdx++){
              //numTriangulations ++;
              Edge[] triangulation = makeTriangulation(triangulations, leftIdx, rightIdx, left, right, startIndex1, startIndex2, n);
              //create a polygon with triangulations from triangulations[leftIdx] and triangulations[rightIdx]

              if(!containsTriangulation(triangulation, triangulations, arrayDataStartIdx, numTriangulations)){
                triangulations[arrayDataStartIdx+numTriangulations] = triangulation;
                numTriangulations ++;
                if(n < 8){
                System.out.println("Triangulation #" + numTriangulations+ ":\n");
                System.out.println(printTriangulation(triangulation));
              }
              }

              //System.out.println(printTriangulation(triangulation));
            }
          }

          it.updateIndices();
      }

      System.out.println("number of triangulations for n = "+ n+ ": " + numTriangulations);
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
      //System.out.println("tmpDiagonal" + tmpDiagonal);
      /*Edge tmpDiagonalConverted = new Edge(leftMap.get(tmpDiagonal.getAInt()),leftMap.get(tmpDiagonal.getBInt()));
      System.out.println("tmpDiagonalConverted: " + tmpDiagonalConverted.toString());
      boolean add = true;
      for(int j = 0; j < idxCounter; j++){
        if(tmpDiagonalConverted.equals(leftTri[i])){
          add = false;
        }
      }
      if(add){*/
        tmpTri[idxCounter] = new Edge(leftMap.get(tmpDiagonal.getAInt()), leftMap.get(tmpDiagonal.getBInt()));
        idxCounter++;
      //}

    }

    //System.out.println("rightMap: "+ rightMap.toString());
    for(int i = 0; i < rightMap.size()-3; i++){ //has n-3 diagonals
      Edge tmpDiagonal = rightTri[i];
      //System.out.println(Arrays.toString(rightTri));
    /*  Edge tmpDiagonalConverted = new Edge(rightMap.get(tmpDiagonal.getAInt()),rightMap.get(tmpDiagonal.getBInt()));
      boolean add = true;
      for(int j = 0; j < idxCounter; j++){
        if(tmpDiagonalConverted.equals(rightTri[i])){
          add = false;
        }
      }
      if(add){*/
        tmpTri[idxCounter] =new Edge(rightMap.get(tmpDiagonal.getAInt()), rightMap.get(tmpDiagonal.getBInt()));
        idxCounter++;
      //}

    }
    //System.out.println(Arrays.toString(tmpTri));
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

  public static boolean containsTriangulation(Edge[] triangulation, Edge[][] triangulations, int arrayDataStartIdx, int numTriangulations){
    int triIdx = arrayDataStartIdx;
    int ending = arrayDataStartIdx + numTriangulations;
    //System.out.println("here hello "+ arrayDataStartIdx + "ending: "+ ending);
    printTriangulation(triangulations[triIdx]);
    while(triIdx < arrayDataStartIdx + numTriangulations){
      //System.out.println("hi???");
      printTriangulation(triangulations[triIdx]);
      if(equalsTriangulation(triangulation, triangulations[triIdx])){
        return true;
      }
      triIdx++;
    }
    return false;
  }

  public static boolean equalsTriangulation(Edge[] tri1, Edge[] tri2){
    int tri1Idx = 0;
    int tri2Idx = 0;

    //System.out.println("here");
    while(tri1[tri1Idx] != null){
      boolean containsEdge = false;
      tri2Idx = 0;
      while(tri2[tri2Idx] != null){
        if(tri1[tri1Idx].equals(tri2[tri2Idx])){
          containsEdge = true;
        }
        tri2Idx++;
      }
      if(!containsEdge){
        return false;
      }
      tri1Idx++;
    }
    return true;
  }


}
