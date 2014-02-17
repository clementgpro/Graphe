package implementation.weighted;

import implementation.parcours.ParcoursProfondeurTools;
import interfaces.IGraph;
import interfaces.IUndirectedGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe de representation d'un graphe pondere
 * 
 * @author Cl�ment
 * 
 */
public class WeightedAdjavencyMatrixDirectedGraph implements IUndirectedGraph {

	/** Matrice d'adjacence. */
	int[][] adjacencyMatrix;

	/** Nombre d'arcs. */
	int nbEdges;

	/** Nombre de noeuds. */
	int nbNodes;

	public WeightedAdjavencyMatrixDirectedGraph(int[][] matrix) {
		this.adjacencyMatrix = matrix;
		this.nbNodes = matrix.length;

		int nbEdges = 0;
		for (int[] aMatrix : matrix) {
			for (int linked : aMatrix) {
				if (linked != Integer.MAX_VALUE) {
					nbEdges++;
				}
			}
		}
		this.nbEdges = nbEdges;
	}

	public WeightedAdjavencyMatrixDirectedGraph(IUndirectedGraph graph) {
		this.nbEdges = graph.getNbEdges();
		this.nbNodes = graph.getNbNodes();
		this.adjacencyMatrix = graph.toAdjacencyMatrix();
	}

	@Override
	public int getNbNodes() {
		return this.nbNodes;
	}

	@Override
	public int[][] toAdjacencyMatrix() {
		return this.adjacencyMatrix;
	}

	@Override
	public int getNbEdges() {
		return this.nbEdges;
	}

	@Override
	public boolean isEdge(int x, int y) {
		return this.adjacencyMatrix[x][y] != Integer.MAX_VALUE;
	}

	@Override
	public void removeEdge(int x, int y) {
		if (this.isEdge(x, y)) {
			this.adjacencyMatrix[x][y] = Integer.MAX_VALUE;;
			this.nbEdges--;
		}
	}

	public void addEdge(int x, int y, int weight) {
		if (!this.isEdge(x, y) && x != y) {
			this.adjacencyMatrix[x][y] = weight;
			this.adjacencyMatrix[y][x] = weight;
			this.nbEdges++;
		}
	}

	@Override
	public int[] getNeighbors(int x) {
		int cpt = 0;
		int[] neighbors = new int[0];
		for (int s = 0; s < this.adjacencyMatrix[x].length; s++) {
			if (this.adjacencyMatrix[x][s] != Integer.MAX_VALUE) {
				neighbors = Arrays.copyOf(neighbors, neighbors.length + 1);
				neighbors[cpt++] = s;
			}
		}

		return neighbors;
	}

	@Override
	public List<List<Integer>> getComposanteConnexe() {
		return ParcoursProfondeurTools.parcoursProfondeur(this);
	}

	@Override
	public void addEdge(int x, int y) {
		this.addEdge(x, y, 1);
	}

	/**
	 * Algorithme de bellman
	 * 
	 * @param s
	 * @return
	 */
	public int[] bellman(int s) {
		int[] vPred = new int[getNbNodes()];
		int[] vActuel = new int[getNbNodes()];
		for (int y = 0; y < getNbNodes(); y++) {
			vActuel[y] = (this.isEdge(s, y)) ? this.getPoids(s, y) : Integer.MAX_VALUE;
		}
		vActuel[s] = 0;
		for (int i = 0; i <= getNbNodes() - 2; i++) {
			vPred = Arrays.copyOf(vActuel, vActuel.length);
			for (int y = 0; y <= getNbNodes() - 1; y++) {
				for (int n : getNeighbors(y)) {
					if (vPred[n] != Integer.MAX_VALUE) {
						vActuel[y] = Math.min(vActuel[y], vPred[n] + getPoids(n, y));
					}
				}
			}
		}
		return vActuel;
	}

	/**
	 * Algorithme de PRIM.
	 * 
	 * @param undirectedGraph
	 * @param s
	 * @return
	 */
	public IGraph prim(IUndirectedGraph undirectedGraph, int s) {
		// IGraph graph = new AdjacencyMatrixUndirectedGraph();
		List<Integer> noeudsTraverses = new ArrayList<Integer>();
		noeudsTraverses.add(s);
		int nombreNoeudsTraverse = 0;
		while (nombreNoeudsTraverse < undirectedGraph.getNbNodes() - 1) {
			noeudsTraverses.add(this.getSommetPoidsMinimal(noeudsTraverses));
			nombreNoeudsTraverse++;
		}
		System.out.println(noeudsTraverses);
		return null;
	}

	/**
	 * Recupere le sommet d'arrive a partir d'une liste de sommets de depart.
	 * 
	 * @param sommets
	 *            les sommets deja parcourus (interdit d'y repasser)
	 * @return
	 */
	private int getSommetPoidsMinimal(List<Integer> sommets) {
		int poidsMinimal = Integer.MAX_VALUE;
		int sommetPoidsMinimal = -1;
		int compteurSommet = 0;
		while (compteurSommet < sommets.size() || sommetPoidsMinimal == -1) {
			int sommetPoidsMinimalCourant = this.getSommetPoidsMinimal(sommets.get(compteurSommet), sommets);
			int poidsMinimalCourant = this.getPoids(sommets.get(compteurSommet), sommetPoidsMinimalCourant);
			if (poidsMinimalCourant < poidsMinimal) {
				poidsMinimal = poidsMinimalCourant;
				sommetPoidsMinimal = sommetPoidsMinimalCourant;
			}
			compteurSommet++;
		}
		return sommetPoidsMinimal;
	}

	/**
	 * Recupere le sommet d'arrive de poids minimal pour un sommet de depart.
	 * 
	 * @param s
	 * @param sommetsExclure
	 * @return
	 */
	private int getSommetPoidsMinimal(int s, List<Integer> sommetsExclure) {
		int[] neighbors = this.getNeighbors(s);
		int poidsMinimal = Integer.MAX_VALUE;
		int sommetPoidsMinimal = -1;
		int compteurVoisin = 0;
		while (compteurVoisin < neighbors.length || sommetPoidsMinimal == -1) {
			if ((!sommetsExclure.contains(neighbors[compteurVoisin])) && this.getPoids(s, neighbors[compteurVoisin]) < poidsMinimal) {
				poidsMinimal = this.getPoids(s, neighbors[compteurVoisin]);
				sommetPoidsMinimal = neighbors[compteurVoisin];
			}
			compteurVoisin++;
		}
		return sommetPoidsMinimal;
	}

	/**
	 * Recupere le poids entre deux arcs.
	 * 
	 * @param sommetDepart
	 * @param sommetArrive
	 * @return le poids
	 */
	private int getPoids(int sommetDepart, int sommetArrive) {
		return this.adjacencyMatrix[sommetDepart][sommetArrive];
	}
}