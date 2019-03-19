import java.util.TreeSet;
public class Triangulation{
  private int n;
  private TreeSet<TreeSet<Edge>> tri;

  public Triangulation(int n, TreeSet<TreeSet<Edge>> tri){
    this.n = n;
    this.tri = tri;
  }

  public int getN(){
    return n;
  }

}
