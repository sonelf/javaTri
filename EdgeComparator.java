/*
  Name: Sonya Fucci
  File: EdgeComparator.java
  Desc: File that determines whether an edge is "greater" than another
  edge where a smaller first coordinate comes before a larger first coordinate,
  and if the first vertices are the same, then examine the second vertex.

*/

import java.util.Comparator;
public class EdgeComparator implements Comparator<Edge>{
  public static final int E1_BEFORE_E2 = -1;
  public static final int E2_BEFORE_E1 = 1;
  public static final int EQUAL = 0;
  
  @Override
  public int compare(Edge e1, Edge e2){
    if(e1 == null && e2 == null){
      return EQUAL;
    }
    else if(e1 == null && !(e2 == null)){
      return E2_BEFORE_E1;
    }
    else if(e1 != null && e2 == null){
      return E1_BEFORE_E2;
    }
    //dealing with potentially null cases
    if(e1.isNull() && e2.isNull()){
      return EQUAL;
    }
    if(e1.isNull() && !e2.isNull()){
      return E2_BEFORE_E1;
    }
    if(!e1.isNull() && e2.isNull()){
      return E1_BEFORE_E2;
    }
    //when all are integer values

    //if a is the same,
    if(e1.getAInt() > e2.getAInt()){
      return E2_BEFORE_E1;
    }
    else if(e2.getAInt() > e1.getAInt()){
      return E1_BEFORE_E2;
    }
    else{
      //if 'a's are same, then check 'b's
      if (e1.getBInt() > e2.getBInt()) {
        return E2_BEFORE_E1;
      }
      else if(e2.getBInt() > e1.getBInt()){
        return E1_BEFORE_E2;
      }
      return EQUAL;
    }


  }

}
