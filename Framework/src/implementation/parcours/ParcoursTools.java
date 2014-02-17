package implementation.parcours;

import interfaces.IDirectedGraph;
import interfaces.IGraph;
import interfaces.IUndirectedGraph;

public class ParcoursTools {

	/**
	 * Recupere les voisins du sommet s du graph.
	 * 
	 * @param graph
	 *            le graphe
	 * @param s
	 *            le sommet sur lequel on recherche les voisins
	 * @return le tableau des voisins
	 */
	public static int[] getVoisins(IGraph graph, Integer s) {
		int[] voisins = null;
		if (graph instanceof IUndirectedGraph) {
			voisins = ((IUndirectedGraph) graph).getNeighbors(s);
		} else if (graph instanceof IDirectedGraph) {
			voisins = ((IDirectedGraph) graph).getSuccessors(s);
		}
		return voisins;
	}
}
