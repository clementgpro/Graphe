package implementation.adjacency.list;

import java.util.ArrayList;
import java.util.List;

import implementation.parcours.ParcoursProfondeurTools;
import interfaces.IDirectedGraph;

public class AdjacencyListDirectedGraph implements IDirectedGraph {

	// Liste d'adjacence
	private List<List<Integer>> adjacencyList;
	private int nbNodes;
	private int nbArcs;

	public AdjacencyListDirectedGraph(int[][] matrix) {
		this.nbNodes = 0;
		this.nbArcs = 0;

		List<List<Integer>> adjacencyList = new ArrayList<List<Integer>>();
		for (int x = 0; x < matrix.length; x++) {
			List<Integer> successorsList = new ArrayList<Integer>();
			for (int y = 0; y < matrix[x].length; y++) {
				if (matrix[x][y] == 1) {
					successorsList.add(y);
					this.nbArcs++;
				}
			}
			adjacencyList.add(successorsList);
			this.nbNodes++;
		}
		this.adjacencyList = adjacencyList;
	}
	
	@Override
	public int getNbNodes() {
		return this.nbNodes;
	}

	@Override
	public int[][] toAdjacencyMatrix() {
		int[][] matrix = new int[getNbNodes()][getNbNodes()];
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
	public int getNbArcs() {
		return this.nbArcs;
	}

	@Override
	public boolean isArc(int from, int to) {
		return from < adjacencyList.size() && adjacencyList.get(from) != null && adjacencyList.get(from).contains(to);
	}

	@Override
	public void removeArc(int from, int to) {
		if (this.isArc(from, to)) {
			adjacencyList.get(from).remove(Integer.valueOf(to));
			this.nbArcs--;
		}
	}

	@Override
	public void addArc(int from, int to) {
		if (from != to && !this.isArc(from, to)) {
			adjacencyList.get(from).add(to);
			this.nbArcs++;
		}

	}

	@Override
	public int[] getSuccessors(int x) {
		int[] successors = new int[0];
		if (adjacencyList.get(x) != null) {
			successors = new int[adjacencyList.get(x).size()];
			int cpt = 0;
			for (Integer neighbor : adjacencyList.get(x)) {
				successors[cpt++] = neighbor.intValue();
			}
		}
		return successors;
	}

	@Override
	public int[] getPredecessors(int x) {
		int predecessors[] = new int[0];
		int pred = 0;
		int cpt = 0;
		for(List<Integer> successors : adjacencyList){
			if(successors.contains(x)){
				cpt++;
				predecessors = new int[cpt];
				predecessors[cpt-1] = pred;
			}
			pred++;
		}
		return predecessors;
	}

	@Override
	public List<List<Integer>> getComposanteFortementConnexe() {
		return ParcoursProfondeurTools.parcoursProfondeur(this);
	}
	
	@Override
	public IDirectedGraph computeInverse() {
		int[][] matrix = new int[getNbNodes()][getNbNodes()];
		
		for(int node=0;node<this.getNbNodes();node++){
			int[] newSuccessors = this.getPredecessors(node);
			for(int i=0;i<newSuccessors.length;i++){
				matrix[node][newSuccessors[i]] = 1;
			}
		}
		return new AdjacencyListDirectedGraph(matrix);
	}

}
