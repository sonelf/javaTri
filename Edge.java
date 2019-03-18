public class Edge {
  private Vertex a;
  private Vertex b;

  public Edge(Vertex a, Vertex b){
    this.a = a;
    this.b = b;
  }

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

  public boolean equals(Edge e){
    return (e.a == a && e.b == b) || (e.a == b && e.b == a);
  }


}
