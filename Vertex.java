/*
  Name: Sonya Fucci
  File: Vertex.java
  Desc: Simple class that describes a vertex.
   Is just represented as an integer. I mainly made a class
   because it would make the Edge code more readable than just an int.

*/

public class Vertex{
  private int pos;

  public Vertex(int pos){
    this.pos = pos;
  }

  public int getPos(){
    return pos;
  }

  public String toString(){
    return ""+pos;
  }

}
