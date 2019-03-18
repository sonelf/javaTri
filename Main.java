import java.util.LinkedList;
import java.util.HashMap;
public class Main{
  public static final int TRI_SIZE = 100000;
  public static final int DIAG_SIZE = 20;

  public static void main(String[] args){

    //providing n = 4 and n = 5 as base cases because they both have one set of unique diagonals
    // (as in, only one pair of rotateable vertices)
    Edge[][] triangulations = new Edge[TRI_SIZE][10];
    ArrayData[] triangulationsData = new ArrayData[20];

    Edge[] fourOne = new Edge[10];
    fourOne[0] = new Edge(new Vertex(0), new Vertex(2));

    Edge[] fourTwo = new Edge[10];
    fourOne[0] = new Edge(new Vertex(1), new Vertex(3));

    Edge[] fiveOne = new Edge[10];
    fiveOne[0] = new Edge(new Vertex(0), new Vertex(2));
    fiveOne[1] = new Edge(new Vertex(2), new Vertex(4));

    Edge[] fiveTwo = new Edge[10];
    fiveTwo[0] = new Edge(new Vertex(1), new Vertex(3));
    fiveTwo[1] = new Edge(new Vertex(3), new Vertex(0));

    Edge[] fiveThree = new Edge[10];
    fiveThree[0] = new Edge(new Vertex(2), new Vertex(4));
    fiveThree[1] = new Edge(new Vertex(4), new Vertex(1));

    Edge[] fiveFour = new Edge[10];
    fiveFour[0] = new Edge(new Vertex(3), new Vertex(0));
    fiveFour[1] = new Edge(new Vertex(0), new Vertex(2));

    Edge[] fiveFive = new Edge[10];
    fiveFive[0] = new Edge(new Vertex(4), new Vertex(1));
    fiveFive[1] = new Edge(new Vertex(1), new Vertex(3));

    triangulations[0] = fourOne;
    triangulations[1] = fourTwo;
    triangulations[2] = fiveOne;
    triangulations[3] = fiveTwo;
    triangulations[4] = fiveThree;
    triangulations[5] = fiveFour;
    triangulations[5] = fiveFive;

    triangulationsData[4] = new ArrayData(0, 2, 2);
    triangulationsData[5] = new ArrayData(2, 5, 7);

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

    int offset = 0;
    boolean start = true;

    while((startIndex2+offset)%n != n-1 || start == true){
      start = false;

      HashMap<Integer, Integer> left = getLeftMap(startIndex1, startIndex2);
      System.out.println(left.toString());

      HashMap<Integer, Integer> right = getRightMap(startIndex2, startIndex1, n);
      System.out.println(right.toString());
      //startIndex1++;
      startIndex2++;
    }

    //get polygon to the right of the diagonal and its size
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

}
