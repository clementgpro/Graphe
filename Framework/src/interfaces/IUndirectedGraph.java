package interfaces;

import java.util.List;

public interface IUndirectedGraph extends IGraph {
	// Returns the number of edges in the graph
	public int getNbEdges();

	// Returns true iff there is an edge between x and y
	public boolean isEdge(int x, int y);

	// Removes edge (x,y) if exists
	public void removeEdge(int x, int y);

	// Adds edge (x,y) if not already present, requires x /= y
	public void addEdge(int x, int y);

	// Returns a new int array representing neighbors of node x
	public int[] getNeighbors(int x);
	
	// Retourne les composantes connexes du graphe.
	public List<List<Integer>> getComposanteConnexe();
}
