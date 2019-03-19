/*
  Name: Sonya Fucci
  File: TriangulationComparator.java
  Desc: File that determines whether a triangulation is "greater" than another
  edge in an ordering, where a smaller first edge comes before a larger first edge,
  and so on and so forth with the remaining edges. This runs under the assumption
  that the triangulations being compared are the same size.

*/

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
public class TreeSetEdgeComparator implements Comparator<TreeSet<Edge>>{
  public static final int TS1_BEFORE_TS2 = -1;
  public static final int TS2_BEFORE_TS1 = 1;
  public static final int EQUAL = 0;

  @Override
  public int compare(TreeSet<Edge> ts1, TreeSet<Edge> ts2){
    if(ts1.equals(ts2)) return EQUAL; //TODO: check if i still need this
    Iterator<Edge> ts1Iterator = ts1.iterator();
    Iterator<Edge> ts2Iterator = ts2.iterator();
    EdgeComparator ec = new EdgeComparator();
    while(ts1Iterator.hasNext() && ts2Iterator.hasNext()){
      Edge e1 = ts1Iterator.next();
      Edge e2 = ts2Iterator.next();
      int comp = ec.compare(e1, e2);
      if( comp < 0){
        return TS1_BEFORE_TS2;
      }
      else if(comp > 0){
        return TS2_BEFORE_TS1;
      }
    }
    return EQUAL;
  }
}
