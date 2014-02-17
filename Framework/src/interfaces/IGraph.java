package interfaces;

public interface IGraph {
	// Returns the number of nodes in the graph (referred to as the order of the graph)
	public int getNbNodes();

	// Returns the adjacency matrix representation of the graph
	public int[][] toAdjacencyMatrix();
	
}
