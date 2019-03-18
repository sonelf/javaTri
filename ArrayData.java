/*
  Name: Sonya Fucci
  File: ArrayData.java

  Desc: Metadata about the triangulations of n.
  This datatype is stored in an array, where array[n] shows the ArrayData for the triangulations
  with n vertices.


*/
public class ArrayData{
   //where in triangulations array we
   //can find triangulations for a particular n start
  private int startIndex;
  //number of triangulations, equal to C_(n-2)
  private int size;
  //where in triangulations array we
  //can find triangulations for a particular n+1 start
  private int endIndex;

  public ArrayData(int startIndex, int size, int endIndex){
    this.startIndex = startIndex;
    this.size = size;
    this.endIndex = endIndex;
  }

  /*
  @return where in the triangulations array
   do the triangulations start(inclusive)
  */
  public int getStartIdx(){
    return startIndex;
  }
  /*
  @return where in the triangulations
  array do the triangulations start(exclusive)
  */
  public int getEndIdx(){
    return endIndex;
  }
}
