package interfaces;

import java.util.List;

public interface IDirectedGraph extends IGraph {
	// Returns the number of arcs in the graph
	public int getNbArcs();

	// Returns true iff arc(from,to) figures in the graph
	public boolean isArc(int from, int to);

	// Removes the arc (from,to) if exists
	public void removeArc(int from, int to);

	// Adds the arc (from,to) if it is not already present in the graph, requires from /= to
	public void addArc(int from, int to);

	// Returns a new int array representing successors of node x
	public int[] getSuccessors(int x);
	
	// Returns a new int array representing predecessors of node x
	public int[] getPredecessors(int x);
	
	// Computes the inverse graph
	public IDirectedGraph computeInverse();
	
	// Returns les composantes fortements connexes du graphe.
	public List<List<Integer>> getComposanteFortementConnexe();
}
