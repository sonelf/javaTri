/*
  Name: Sonya Fucci
  File: TriangulationComparator.java
  Desc: File that determines whether a triangulation is "greater" than another
  edge in an ordering, where a smaller first edge comes before a larger first edge,
  and so on and so forth with the remaining edges. This runs under the assumption
  that the triangulations being compared are the same size.

*/

import java.util.Comparator;
public class TriangulationComparator implements Comparator<Edge[]>{
  public static final int T1_BEFORE_T2 = -1;
  public static final int T2_BEFORE_T1 = 1;
  public static final int EQUAL = 0;

  
  @Override
  public int compare(Edge[] t1, Edge [] t2){
    int t1Idx = 0;
    EdgeComparator ec = new EdgeComparator();
    while(t1[t1Idx]!= null && t2[t1Idx] != null){
      int comparedVal = ec.compare(t1[t1Idx], t2[t1Idx]);
      if(comparedVal > 0){
        return T2_BEFORE_T1;
      }else if (comparedVal < 0){
        return T1_BEFORE_T2;
      }
      t1Idx++;
    }
    return EQUAL;
  }

}
