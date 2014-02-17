package implementation.adjacency.list;

import implementation.parcours.ParcoursProfondeurTools;
import implementation.tools.GraphTools;
import interfaces.IUndirectedGraph;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyListUndirectedGraph implements IUndirectedGraph {
	// Liste d'adjacence
	private List<List<Integer>> adjacencyList;
	private int nbNodes;
	private int nbEdges;

	/**
	 * Constructor
	 * 
	 * @param n
	 *            the number of nodes
	 * @param m
	 *            the number of edges
	 * @throws Exception
	 */
	public AdjacencyListUndirectedGraph(int[][] matrix) {
		this.nbNodes = 0;
		int nbEdgesTotal = 0;

		List<List<Integer>> adjacencyList = new ArrayList<List<Integer>>();
		for (int x = 0; x < matrix.length; x++) {
			List<Integer> successorsList = new ArrayList<Integer>();
			for (int y = 0; y < matrix[x].length; y++) {
				if (matrix[x][y] == 1) {
					successorsList.add(y);
					nbEdgesTotal++;
				}
			}
			adjacencyList.add(successorsList);
			this.nbNodes++;
		}
		this.nbEdges = nbEdgesTotal / 2;
		this.adjacencyList = adjacencyList;
	}

	public AdjacencyListUndirectedGraph(IUndirectedGraph undirectedGraph) {
		this(GraphTools.generateGraphData(undirectedGraph.getNbNodes(), undirectedGraph.getNbEdges(), true));
	}

	@Override
	public int getNbNodes() {
		return this.nbNodes;
	}

	@Override
	public int[][] toAdjacencyMatrix() {
		int[][] matrix = new int[getNbNodes()][getNbEdges()];
		int x = 0;
		for (List<Integer> successorsList : adjacencyList) {
			for (Integer successor : successorsList) {
				matrix[x][successor] = 1;
			}
			x++;
		}
		return matrix;

	}

	@Override
	public int getNbEdges() {
		return this.nbEdges;
	}

	@Override
	public boolean isEdge(int x, int y) {
		return x < adjacencyList.size() && adjacencyList.get(x) != null && adjacencyList.get(x).contains(y) && y < adjacencyList.size()
				&& adjacencyList.get(y) != null && adjacencyList.get(y).contains(x);
	}

	@Override
	public void removeEdge(int x, int y) {
		if (this.isEdge(x, y)) {
			adjacencyList.get(x).remove(Integer.valueOf(y));
			adjacencyList.get(y).remove(Integer.valueOf(x));
			this.nbEdges--;
		}
	}

	@Override
	public void addEdge(int x, int y) {
		if (x != y && !this.isEdge(x, y)) {
			adjacencyList.get(x).add(y);
			adjacencyList.get(y).add(x);
			this.nbEdges++;
		}
	}

	@Override
	public int[] getNeighbors(int x) {
		int[] neighbors = new int[0];
		if (adjacencyList.get(x) != null) {
			neighbors = new int[adjacencyList.get(x).size()];
			int cpt = 0;
			for (Integer neighbor : adjacencyList.get(x)) {
				neighbors[cpt++] = neighbor.intValue();
			}
		}
		return neighbors;

	}

	@Override
	public List<List<Integer>> getComposanteConnexe() {
		return ParcoursProfondeurTools.parcoursProfondeur(this);
	}
	
	public static void main(String[] args) {
		AdjacencyListUndirectedGraph adjacencyListUndirectedGraph = new AdjacencyListUndirectedGraph(GraphTools.generateGraphData(3, 3, true));
		List<List<Integer>> adjcencyList = adjacencyListUndirectedGraph.adjacencyList;
		int i = 0;
		for (List<Integer> successorsList : adjcencyList) {
			System.out.print(i++ + " : [");
			for (Integer successor : successorsList) {
				System.out.print(successor);
			}
			System.out.println("] ");
		}

		// System.out.println(adjacencyListUndirectedGraph.isEdge(0, 2));
		System.out.println(GraphTools.printMatrix(adjacencyListUndirectedGraph.toAdjacencyMatrix()));
	}
}
