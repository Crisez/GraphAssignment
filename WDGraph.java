/*
 * Zach Crise
 * CIT360
 * 12/4/19
 * 
 * Objective: update the WDGraph to utilize dijkstra's algorithm
 * 
 * pseudo code from the word document and
 * https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 * were used to help build this code
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * T is required to have overridden the method equal
 * 
 * @author bgolshan
 *
 * @param <T>
 */
public class WDGraph<T> implements GraphADT<T> {

	private static int CAPACITY = 2;
	private double[][] adjMatrix;
	private int numEdges;
	private int numVertices;
	private T[] vertices;
	private final double INFINITY = Double.POSITIVE_INFINITY;

	public WDGraph(int capacity) {
		numVertices = 0;
		numEdges = 0;
		CAPACITY = capacity;
		adjMatrix = new double[capacity][capacity];
		vertices = (T[]) new Object[capacity];
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix[i].length; j++) {
				adjMatrix[i][j] = INFINITY;
			}

		}
	}

	public WDGraph() {
		this(CAPACITY);
	}

	@Override
	public int numVertices() {
		return numVertices;
	}

	@Override
	public int numEdges() {
		return numEdges;
	}

	@Override
	public void addVertex(T vertex) {
		if (numVertices == CAPACITY)
			expand();

		if (!existVertex(vertex)) {
			vertices[numVertices] = vertex;
			numVertices++;
		}

	}

	
	private void expand() {
		int newCapacity = 2 * CAPACITY;
		double[][] newAdjMatrix = new double[newCapacity][newCapacity];
		T[] newVertices = (T[]) new Object[newCapacity];
		
		for (int i = 0; i < numVertices; i++) {
			newVertices[i] = vertices[i];
		}
		
		for (int i = 0; i < newAdjMatrix.length; i++) {
			for (int j = 0; j < newAdjMatrix[i].length; j++) {
				newAdjMatrix[i][j] = INFINITY;
			}
		}
		
		
		for (int i = 0; i < numVertices; i++) {
			for (int j = 0; j < numVertices; j++) {
				newAdjMatrix[i][j] = adjMatrix[i][j];
			}
		}	
		
		CAPACITY = newCapacity;
		adjMatrix = newAdjMatrix;
		vertices = newVertices;
		
		
	}
	
	
	
	public String toString() {
		String result = "";
		
		int GAP = 7;
		if(isEmpty())
			return result;
		
		result += String.format("%7s", "");
		for (int i = 0; i < numVertices; i++) {
			result += String.format("%7s", vertices[i]);
		}
		
		result += "\n";
		for (int i = 0; i < numVertices; i++) {
			result += String.format("%7s", vertices[i]);
			
			for (int j = 0; j < numVertices; j++) {
				if(adjMatrix[i][j] == INFINITY)
					result += String.format("%" + GAP + "s", '\u221e');
				else
					result += String.format("%7.0f", adjMatrix[i][j]);
			}
			result += "\n";
		}
		
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * The object type T should have overridden the .equals method
	 * 
	 * @param vertex
	 * @return only if the vertex already in graph
	 */
	private boolean existVertex(T vertex) {
		for (int i = 0; i < numVertices; i++) {
			if (vertex.equals(vertices[i]))
				return true;
		}
		return false;
	}

	@Override
	public void removeVertex(T vertex) {
		int index = vertexIndex(vertex);
		if (index == -1)
			return;
		
		
		int numEdgesRemoved = 0;
		for (int i = 0; i < numVertices; i++) {
			if(adjMatrix[index][i] != INFINITY)
				numEdgesRemoved++;
			if(adjMatrix[i][index] != INFINITY)
				numEdgesRemoved++;
		}

		numEdges -= numEdgesRemoved;
		
		//shift the columns left
		for (int col = index; col < numVertices -1; col++) {
			for (int row = 0; row < numVertices; row++) {
				adjMatrix[row][col] = adjMatrix[row][col+1]; 
			}
		}
		
		//shift the rows up
		for (int row = index; row < numVertices -1; row++) {
			for (int col = 0; col < numVertices; col++) {
				adjMatrix[row][col] = adjMatrix[row+1][col]; 
			}
		}
		
		//set last row and last columns to infinity
		for (int i = 0; i < numVertices; i++) {
			adjMatrix[numVertices-1][i] = INFINITY;
			adjMatrix[i][numVertices-1] = INFINITY;
		}
		//move vertices to the left
		for (int i = index; i < numVertices-1; i++) {
			vertices[i] = vertices[i+1];
		}		
		vertices[numVertices-1] = null;  //nullify the last value
		numVertices--;
		
	}

	
	
	private int vertexIndex(T vertex) {
		for(int i = 0; i < numVertices; i++)
			if(vertices[i].equals(vertex))
				return i;
		
		return -1;
	}
	
	
	
	

	@Override
	public void addEdge(T fromVertex, T toVertex, double weight) {
		if(this.existVertex(fromVertex) && this.existVertex(toVertex) &&
				fromVertex != toVertex && weight >= 0) {
			if(!this.existEdge(fromVertex, toVertex))
				numEdges++;
			adjMatrix[vertexIndex(fromVertex)][vertexIndex(toVertex)] = weight;
		}

	}

	


	@Override
	public boolean existEdge(T fromVertex, T toVertex) {
		return ( this.existVertex(fromVertex) && 
		    this.existVertex(toVertex) &&
			adjMatrix[vertexIndex(fromVertex)][vertexIndex(toVertex)] != INFINITY
		  );
	}

	@Override
	public void removeEdge(T fromVertex, T toVertex) {
		if(this.existVertex(fromVertex) && this.existVertex(toVertex) &&
				existEdge(fromVertex, toVertex)) {
			numEdges--;
			adjMatrix[vertexIndex(fromVertex)][vertexIndex(toVertex)] = INFINITY;	
		}
	}
	
	
	
	@Override
	public boolean isEmpty() {
		return numVertices == 0;
	}

	@Override
	public int numComponents() {
		// TODO Auto-generated method stub
		return 0;
	}
    /**
     * use the BFS alg to determine if there is a path
     * 
     */
	@Override
	public boolean existPath(T fromVertex, T toVertex) {
		if(!existVertex(fromVertex) || !existVertex(toVertex))
			return false;
		
		boolean[] visited = new boolean[numVertices];
		LQueue<T> que = new LQueue<T>();
		T tempVertex;
		List<T> ngbrs;
		
		que.enqueue(fromVertex);
		boolean done = false;
		visited[vertexIndex(fromVertex)] = true;
		
		while(!que.isEmpty() && !done) {
		   if(que.peek().equals(toVertex)) {
			   done = true;
			   break;
		   }
		   tempVertex = que.dequeue();
		   ngbrs = neighbors(tempVertex);
		   for(T ver : ngbrs) {
			   if(!visited[vertexIndex(ver)]) {
				   visited[vertexIndex(ver)] = true;
				   que.enqueue(ver);
			   }
		   }
		   
		}
		
		return done;
	}


	@Override
	public ArrayList<T> path(T fromVertex, T toVertex) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Here is DIJKSTRA'S algorithm
	 */
	@Override
	public ArrayList<T> shortestPath(T fromVertex, T toVertex) { //T source, T destination
		Set<Integer> setVisit = new HashSet<Integer>();
		double[] dist = new double[numVertices];
		int[] prev = new int[numVertices];
		double temp = INFINITY;
		
		//vertex with min distance
		int v;
		//distance from root
		double rootDist;
		//list of neighbors
		List<T> l;
		//neighbor index
		int N;
		
		//index of both source vertex and destination vertex
		int source = vertexIndex(fromVertex);
		int destination = vertexIndex(toVertex);
		
		//stack for correcting the shortest path as well as the Arraylist to return and total distance variable
		Stack<Integer> stack = new Stack<Integer>();
		ArrayList directions = new ArrayList();
		int distTotal;
		
		//for all the vertices, set the previous array to undefined, 
		//the set to contain all vertices, and distance array to infinity except for the source
		for(int i = 0; i < numVertices; i++) {
			if(i == source) {
				dist[i] = 0;
			} else {
				dist[i] = INFINITY;
			}
			prev[i] = -1;
			setVisit.add(i);
		}
		
		//set the min dist vertex to source in case nothing is less than temp
		v = vertexIndex(fromVertex);
		
		//value to stop the loop once the destination has been reached.
		boolean done = false;
		
		while(!setVisit.isEmpty() && !done) {
			//reset temp to infinity
			temp = INFINITY;
			
			for(int u: setVisit) {
				//check every index u in the set to see if the distance of the associated vertex is less than temp
				if(dist[u] < temp) { 
					//set temp to that distance if the vertex distance is smaller 
					//and then set the min vertex index v to that index
					temp = dist[u];
					v = u;
				}
			}
			
			//use the name of the vertex with the index v to create a list of neighbors then remove v from the set
			l = neighbors(vertices[v]);
			setVisit.remove(v);
			
			//if v is the destination, stop the loop
			if(v == destination) {
				done = true;
			}
			
			// for every vertex within the neighbor list take the vertex, find its index and make that N
			for(T n : l) {
				N = vertexIndex(n);
				//if N is in the set then take the distance of v and add that to the distance of v to N
				if(setVisit.contains(N)) {
					rootDist = dist[v] + adjMatrix[v][N];
					
					//if the distance calculated above is smaller than the 
					//neighbor's distance then set the neighbor's distance to the new distance 
					//and set the previous array of the neighbor's index to the index of the min distance vertex
					if(rootDist < dist[N]) {
						dist[N] = rootDist;
						prev[N] = v;
						
					}
				}
			}
			
			
			
		}
		
		//get the distance from the root for the last vertex and turn the total distance into an int
		distTotal = (int) dist[v];
		
		//while the index is not that of the source vertex, push a vertex of the shortest path onto a stack
		while (v != source) {
			stack.push(v);
			//set the vertex to the next index within the previous array
			v = prev[v];
		}
		
		//unload the shortest path into an arraylist and then format for printing
		while(!stack.isEmpty()) {
			Integer indexPath = stack.pop();
			//approximate(because we are turning them to ints the distances will be off from the total distance slightly) travel distance 
			//between the path index and its previous index, path index, and name is the format for each index taken out of the stack.
			directions.add("Travel approximately " + (int) adjMatrix[prev[indexPath]][indexPath] + "ft to Point " + indexPath + ": " + vertices[indexPath] );
		}
		
		//add the total distance traveled to the end of the array
		directions.add("Total Distance Traveled: " + distTotal + "ft");
		
		
		return directions;
	}

	@Override
	public List<T> neighbors(T vertex) {
		ArrayList<T> list = new ArrayList<T>();
		
		if(!existVertex(vertex))
			return null;
		int index = vertexIndex(vertex);
		
		for (int i = 0; i < numVertices; i++) {
		   if(adjMatrix[index][i] != INFINITY)
			   list.add(vertices[i]);
		}
		return list;
	}

	

	
	
	
}
