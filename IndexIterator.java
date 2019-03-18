/*
  Name: Sonya Fucci
  Name: IndexIterator.java
  Desc: Helps determine the diagonal we are splitting the current n-gon into.

  Ex: for a 6-gon, starting will be (0,2). Then (0,3), (0,4), then
  returns to (1,3)

*/
public class IndexIterator{
  private int prevStartingIdx1; //this is used to cycle back to the diagonals in (n, n+2) format
  private int prevStartingIdx2; //this is used to cycle back to the diagonals in (n, n+2) format
  private int currentIdx1; //current first segment of diagonal
  private int currentIdx2; //current second segment of diagonal
  private int n; //n-gon size

  public IndexIterator(int firstIdx, int secondIdx, int n){
    this.currentIdx1 = firstIdx;
    this.currentIdx2 = secondIdx;
    this.prevStartingIdx1 = currentIdx1;
    this.prevStartingIdx2 = currentIdx2;
    this.n = n;
  }

  /*
  @return current first index of diagonal
  that will be splitting the n-gon
  */
  public int getCurrentIndex1(){
    return currentIdx1;
  }

  /*
  @return current second index of diagonal
   that will be splitting the n-gon
  */
  public int getCurrentIndex2(){
    return currentIdx2;
  }

  /*
    Ensuring that the next diagonal splitting
    the n-gon, if any, is the correct one.
  */
  public void updateIndices(){

    //if we need to chose a new starting diagonal
    if((currentIdx2 + 2)%n == currentIdx1 || (currentIdx2 == n-1)){

      currentIdx1 = (prevStartingIdx1 +1);
      currentIdx2 = (prevStartingIdx2 +1);

      prevStartingIdx1 = currentIdx1;
      prevStartingIdx2 = currentIdx2;

      // if we have reached the end of the diagonals we need
      if(currentIdx2 >= n){
        currentIdx2 = -1;
      }
      if(currentIdx1 >= n){
        currentIdx1 = -1;
      }
    }
    //if the current starting vertex is sufficient
    else{
      currentIdx2 = currentIdx2+1;
    }
  }

  /*
  Indicates whether we have analyzed all the diagonals,
  and thus, whether our triangulation is complete

  @return boolean -- if there are more diagonals
  to split the n-gon, return true. else return false.
  */
  public boolean hasNextIndex(){
    return currentIdx1 != -1 && currentIdx2 != -1;
  }
}
