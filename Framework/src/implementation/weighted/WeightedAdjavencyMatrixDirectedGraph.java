package implementation.weighted;

import implementation.adjacency.matrix.AdjacencyMatrixDirectedGraph;
import implementation.parcours.ParcoursProfondeurTools;
import implementation.tools.GraphTools;
import interfaces.IDirectedGraph;
import interfaces.IUndirectedGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe de representation d'un graphe pondere
 * 
 * @author Clément
 * 
 */
public class WeightedAdjavencyMatrixDirectedGraph implements IDirectedGraph {

	/** Matrice d'adjacence. */
	int[][] adjacencyMatrix;

	/** Nombre d'arcs. */
	int nbArcs;

	/** Nombre de noeuds. */
	int nbNodes;

	public WeightedAdjavencyMatrixDirectedGraph(int[][] matrix) {
		this.adjacencyMatrix = matrix;
		this.nbNodes = matrix.length;

		int nbArcs = 0;
		for (int[] aMatrix : matrix) {
			for (int linked : aMatrix) {
				if (linked != Integer.MAX_VALUE) {
					nbArcs++;
				}
			}
		}
		this.nbArcs = nbArcs;
	}

	public WeightedAdjavencyMatrixDirectedGraph(IUndirectedGraph graph) {
		this.nbArcs = graph.getNbEdges();
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
	public boolean isArc(int from, int to) {
		return this.adjacencyMatrix[from][to] != Integer.MAX_VALUE;
	}

	@Override
	public void removeArc(int from, int to) {
		if (this.isArc(from, to)) {
			this.adjacencyMatrix[from][to] = Integer.MAX_VALUE;
			this.nbArcs--;
		}
	}

	@Override
	public void addArc(int from, int to) {
		this.addArc(from, to, 1);
	}
	
	public void addArc(int from, int to, int weight) {
		if (!this.isArc(from, to) && from != to) {
			this.adjacencyMatrix[from][to] = weight;
			this.nbArcs++;
		}
	}
		
	@Override
	public int getNbArcs() {
		return this.nbArcs;
	}
	
	@Override
	public int[] getSuccessors(int x) {
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
	public int[] getPredecessors(int x) {
		List<Integer> predecessorsList = new ArrayList<Integer>();
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            if (this.adjacencyMatrix[i][x] != Integer.MAX_VALUE) {
                predecessorsList.add(i);
            }
        }

        int[] predecessors = new int[predecessorsList.size()];
        for (int i = 0; i < predecessorsList.size(); i++) {
            predecessors[i] = predecessorsList.get(i);
        }

        return predecessors;
	}

	@Override
	public IDirectedGraph computeInverse() {
		int[][] matrix = new int[this.nbNodes][this.nbNodes];
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                if (x == y) {
                    matrix[x][y] = Integer.MAX_VALUE;
                } else if (this.adjacencyMatrix[x][y] != Integer.MAX_VALUE) {
                    matrix[y][x] = this.adjacencyMatrix[x][y];
                }
            }
        }
        return new AdjacencyMatrixDirectedGraph(matrix);
	}

	@Override
	public List<List<Integer>> getComposanteFortementConnexe() {
		return ParcoursProfondeurTools.parcoursProfondeur(this);
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
			vActuel[y] = (this.isArc(s, y)) ? this.getPoids(s, y) : Integer.MAX_VALUE;
		}
		vActuel[s] = 0;
		for (int i = 0; i <= getNbNodes() - 2; i++) {
			vPred = Arrays.copyOf(vActuel, vActuel.length);
			for (int y = 0; y < getNbNodes(); y++) {
				for (int n : getSuccessors(y)) {
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
	public int prim(int s) {
		List<Integer> noeudsTraverses = new ArrayList<Integer>();
		noeudsTraverses.add(s);
		int nombreNoeudsTraverse = 0;
		int poids = 0;
		while (nombreNoeudsTraverse < this.getNbNodes() - 1) {
			int[] tabSommetPoidsMinimal = this.getSommetPoidsMinimal(noeudsTraverses);
			int sommetDepartPoidsMinimal = tabSommetPoidsMinimal[0];
			int sommetArriveePoidsMinimal = tabSommetPoidsMinimal[1];
			noeudsTraverses.add(sommetArriveePoidsMinimal);
			poids += this.getPoids(sommetDepartPoidsMinimal, sommetArriveePoidsMinimal);
			nombreNoeudsTraverse++;

		}
		return poids;
	}

	/**
	 * Recupere le sommet d'arrive a partir d'une liste de sommets de depart.
	 * 
	 * @param sommets
	 *            les sommets deja parcourus (interdit d'y repasser)
	 * @return
	 */
	private int[] getSommetPoidsMinimal(List<Integer> sommets) {
		int poidsMinimal = Integer.MAX_VALUE;
		int sommetArriveePoidsMinimal = -1;
		int compteurSommet = 0;
		int sommetDepartPoidsMinimal = 0;
		while (compteurSommet < sommets.size() || sommetArriveePoidsMinimal == -1) {
			int sommetPoidsMinimalCourant = this.getSommetPoidsMinimal(sommets.get(compteurSommet), sommets);
			int poidsMinimalCourant = this.getPoids(sommets.get(compteurSommet), sommetPoidsMinimalCourant);
			if (poidsMinimalCourant < poidsMinimal) {
				poidsMinimal = poidsMinimalCourant;
				sommetArriveePoidsMinimal = sommetPoidsMinimalCourant;
				sommetDepartPoidsMinimal = compteurSommet;
			}
			compteurSommet++;
		}
		return new int[] { sommetDepartPoidsMinimal, sommetArriveePoidsMinimal };
	}

	/**
	 * Recupere le sommet d'arrive de poids minimal pour un sommet de depart.
	 * 
	 * @param s
	 * @param sommetsExclure
	 * @return
	 */
	private int getSommetPoidsMinimal(int s, List<Integer> sommetsExclure) {
		int[] neighbors = this.getSuccessors(s);
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

	public static void main(String[] args) {
		int[][] matrixPrim = new int[][] { { Integer.MAX_VALUE, 5, 10 }, { 5, Integer.MAX_VALUE, 2 }, { 10, 2, Integer.MAX_VALUE } };
		int[][] matrixPrim2 = new int[][] { { Integer.MAX_VALUE, 1, 6, 3 }, { 1, Integer.MAX_VALUE, 2, 7 }, { 6, 2, Integer.MAX_VALUE, 5 },
				{ 3, 7, 5, Integer.MAX_VALUE } };
		WeightedAdjavencyMatrixDirectedGraph wgPrim = new WeightedAdjavencyMatrixDirectedGraph(matrixPrim);
		WeightedAdjavencyMatrixDirectedGraph wgPrimBis = new WeightedAdjavencyMatrixDirectedGraph(matrixPrim2);
		System.out.println(GraphTools.printMatrix(matrixPrim) + "Prim 1 : " + wgPrim.prim(0));
		System.out.println("------------------");
		System.out.println(GraphTools.printMatrix(matrixPrim2) + "Prim 2 (tricky) : " + wgPrimBis.prim(0));

		// int[][] matrix = new int[][] { { 0, 1, 6, 3 }, { 1, 0, 2, 7 }, { 6, 2, 0, 5 }, { 3, 7, 5, 0 } };
		int[][] matrixBellman = new int[][] { { Integer.MAX_VALUE, 2, 8, Integer.MAX_VALUE }, { 2, Integer.MAX_VALUE, 4, Integer.MAX_VALUE },
				{ 8, 4, Integer.MAX_VALUE, 1 }, { Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE } };
		WeightedAdjavencyMatrixDirectedGraph wgBellman = new WeightedAdjavencyMatrixDirectedGraph(matrixBellman);

		System.out.println("------------------");
		int[] res = wgBellman.bellman(0);
		System.out.println(GraphTools.printMatrix(matrixBellman) + "Bellman : " );
		for (int i = 0; i < res.length; i++) {
			System.out.print(res[i] + ";");
		}

	}
}
