import java.util.ArrayList;
import java.util.List;

public interface GraphADT<T> {

	public int numVertices();
	public int numEdges();
	public void addVertex(T vertex);
	public void removeVertex(T vertex);
	public void addEdge(T fromVertex, T toVertex, double weight);
	public void removeEdge(T fromVertex, T toVertex);
	public boolean existEdge(T fromVertex, T toVertex);
	public boolean isEmpty();
	public int numComponents();
	public boolean existPath(T fromVertex, T toVertex);
	public ArrayList<T> path(T fromVertex, T toVertex);
	public ArrayList<T> shortestPath(T fromVertex, T toVertex);
	public List<T> neighbors(T vertex);
	//may be others to be added later
	
}
