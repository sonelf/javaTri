public class IndexIterator{
  private int prevStartingIdx1;
  private int prevStartingIdx2;
  private int currentIdx1;
  private int currentIdx2;
  private int n;

  public IndexIterator(int firstIdx, int secondIdx, int n){
    this.currentIdx1 = firstIdx;
    this.currentIdx2 = secondIdx;
    this.prevStartingIdx1 = currentIdx1;
    this.prevStartingIdx2 = currentIdx2;
    this.n = n;
  }

  public int getCurrentIndex1(){
    return currentIdx1;
  }

  public int getCurrentIndex2(){
    return currentIdx2;
  }

  public void updateIndices(){
    //System.out.println("currentIdx1: " + currentIdx1 + " currentIdx2: " + currentIdx2);
    if((currentIdx2 + 2)%n == currentIdx1 || (currentIdx2 == n-1)){
      //System.out.println("here switching");
      currentIdx1 = (prevStartingIdx1 +1);
      currentIdx2 = (prevStartingIdx2 +1);

      prevStartingIdx1 = currentIdx1;
      prevStartingIdx2 = currentIdx2;

      if(currentIdx2 >=n){
        currentIdx2 = -1;
      }
      if(currentIdx1 >= n){
        currentIdx1 = -1;
      }
    }
    else{
      currentIdx2 = currentIdx2+1;
    }
  }

  public boolean hasNextIndex(){
    return currentIdx1 != -1 && currentIdx2 != -1;
  }
}
