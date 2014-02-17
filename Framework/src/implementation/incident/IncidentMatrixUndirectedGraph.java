package implementation.incident;

import implementation.parcours.ParcoursProfondeurTools;
import interfaces.IUndirectedGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation de la matrice d'incidence, les sommets en abscisse, les arcs en ordonnee.
 * 
 * @author Clément
 * 
 */
public class IncidentMatrixUndirectedGraph implements IUndirectedGraph {
	/** Matrice d'incidence. */
	int[][] incidentMatrix;
	
	/** Nombre de noeuds. */
	int nbNodes;
	
	/** Nombre d'arcs. */
	int nbEdges;

	public IncidentMatrixUndirectedGraph(int[][] matrix) {
		this.nbNodes = matrix.length;

		this.nbEdges = 0;
		for (int x = 0; x < matrix.length; x++) {
			for (int y = 0; y < matrix.length; y++) {
				if (matrix[x][y] == 1 && x < y) {
					this.nbEdges++;
				}
			}
		}

		this.incidentMatrix = new int[matrix.length][this.nbEdges];
		int edge = 0;
		for (int x = 0; x < matrix.length; x++) {
			for (int y = 0; y < matrix.length; y++) {
				if (matrix[x][y] == 1 && x < y) {
					this.incidentMatrix[x][edge] = 1;
					this.incidentMatrix[y][edge] = 1;
					edge++;
				}
			}
		}
	}

	@Override
	public int getNbEdges() {
		return this.nbEdges;
	}

	@Override
	public boolean isEdge(int x, int y) {
		if (x != y) {
			for (int i = 0; i < this.incidentMatrix[0].length; i++) {
				if (this.incidentMatrix[x][i] == 1 && this.incidentMatrix[y][i] == 1) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void removeEdge(int x, int y) {
		if (this.isEdge(x, y)) {
			this.nbEdges--;
			int[][] incidentMatrixUpdated = new int[this.nbNodes][this.nbEdges];
			int edge = 0;
			for (int col = 0; col < this.incidentMatrix[0].length; col++) {
				if (this.incidentMatrix[x][col] == 0 || this.incidentMatrix[y][col] == 0) {
					for (int row = 0; row < this.incidentMatrix.length; row++) {
						incidentMatrixUpdated[row][edge] = this.incidentMatrix[row][col];
					}
					edge++;
				}
			}
			this.incidentMatrix = incidentMatrixUpdated;
		}
	}

	@Override
	public void addEdge(int x, int y) {
		if (!this.isEdge(x, y) && x != y) {
			this.nbEdges++;
			int[][] incidentMatrixUpdated = new int[this.nbNodes][this.nbEdges];
			for (int row = 0; row < this.incidentMatrix.length; row++) {
				System.arraycopy(this.incidentMatrix[row], 0, incidentMatrixUpdated[row], 0, this.incidentMatrix[row].length);
			}
			incidentMatrixUpdated[x][this.nbEdges - 1] = 1;
			incidentMatrixUpdated[y][this.nbEdges - 1] = 1;
			this.incidentMatrix = incidentMatrixUpdated;
		}
	}

	@Override
	public int[] getNeighbors(int x) {
		List<Integer> neighborsList = new ArrayList<Integer>();
		for (int edge = 0; edge < this.nbEdges; edge++) {
			if (this.incidentMatrix[x][edge] == 1) {
				for (int i = 0; i < this.incidentMatrix.length; i++) {
					if (i != x && this.incidentMatrix[i][edge] == 1) {
						neighborsList.add(i);
						break;
					}
				}
			}
		}

		int[] neighbors = new int[neighborsList.size()];
		for (int i = 0; i < neighborsList.size(); i++) {
			neighbors[i] = neighborsList.get(i);
		}

		return neighbors;
	}

	@Override
	public int getNbNodes() {
		return this.nbNodes;
	}

	@Override
	public int[][] toAdjacencyMatrix() {
		int[][] adjacencyMatrix = new int[this.nbNodes][this.nbNodes];
		for (int edge = 0; edge < this.nbEdges; edge++) {
			int x = -1;
			int y = -1;
			for (int row = 0; row < this.nbNodes; row++) {
				if (this.incidentMatrix[row][edge] == 1) {
					if (x == -1) {
						x = row;
					} else {
						y = row;
					}
					if (x != -1 && y != -1) {
						break;
					}
				}
			}
			adjacencyMatrix[x][y] = 1;
			adjacencyMatrix[y][x] = 1;
		}
		return adjacencyMatrix;
	}

	@Override
	public List<List<Integer>> getComposanteConnexe() {
		return ParcoursProfondeurTools.parcoursProfondeur(this);
	}
}
