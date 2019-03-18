public class Edge {
  private Vertex a;
  private Vertex b;

  public Edge(Vertex a, Vertex b){
    this.a = a;
    this.b = b;
  }

  @Override
  public String toString(){
    return "( " +a+ ", "+b+" )";
  }
}
