/*
  Name: Sonya Fucci
  File: Edge.java
  Desc: Simple class that describes an edge.
   Is just represented as two vertices, which of course
   are each created with integers. Similar functionality to a pair of vertices.

*/

public class Edge {
  private Vertex a;
  private Vertex b;

  public Edge(Vertex a, Vertex b){
    this.a = a;
    this.b = b;
  }

  /*Duplicate constructor that
  creates a vertex from the given ints*/
  public Edge(int a, int b){
    this.a = new Vertex(a);
    this.b = new Vertex(b);
  }

  public Vertex getA(){
    return a;
  }

  public Vertex getB(){
    return b;
  }

  public Integer getAInt(){
    return a.getPos();
  }

  public Integer getBInt(){
    return b.getPos();
  }

  @Override
  public String toString(){
    return "( " +a+ ", "+b+" )";
  }

  /*
    @return whether the edges are equivalent regardless of the
    direction of the diagonal.
  */
  public boolean equals(Edge e){
    return (e.a.getPos() == a.getPos() && e.b.getPos() == b.getPos())
    || (e.a.getPos() == b.getPos() && e.b.getPos() == a.getPos());
  }


}
