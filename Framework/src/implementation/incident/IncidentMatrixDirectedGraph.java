package implementation.incident; 

import implementation.parcours.ParcoursProfondeurTools;
import interfaces.IDirectedGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation de la matrice d'incidence, les sommets en abscisse, les arcs en ordonnee.
 * 
 * @author Clément
 * 
 */
public class IncidentMatrixDirectedGraph implements IDirectedGraph {
    int[][] incidentMatrix;
    int nbArcs;
    int nbNodes;

    public IncidentMatrixDirectedGraph(int[][] matrix) {
        this.nbNodes = matrix.length;

        this.nbArcs = 0;
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0;  y < matrix.length; y++) {
                if (matrix[x][y] == 1) {
                    this.nbArcs++;
                }
            }
        }

        this.incidentMatrix = new int[matrix.length][this.nbArcs];
        int arc = 0;
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0;  y < matrix.length; y++) {
                if (matrix[x][y] == 1) {
                    this.incidentMatrix[x][arc] = -1;
                    this.incidentMatrix[y][arc] = 1;
                    arc++;
                }
            }
        }
    }

    public IncidentMatrixDirectedGraph(int[][] matrix, int nbNodes, int nbArcs) {
        this.incidentMatrix = matrix;
        this.nbNodes = nbNodes;
        this.nbArcs = nbArcs;
    }

    @Override
    public int getNbArcs() {
        return this.nbArcs;
    }

    @Override
    public boolean isArc(int from, int to) {
        if (from != to) {
            for (int col = 0; col < this.nbArcs; col++) {
                if (this.incidentMatrix[from][col] == -1 && this.incidentMatrix[to][col] == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void removeArc(int from, int to) {
        if (this.isArc(from, to)) {
            this.nbArcs--;
            int[][] incidentMatrixUpdated = new int[this.nbNodes][this.nbArcs];
            int edge = 0;
            for (int col = 0; col < this.incidentMatrix[0].length; col++) {
                if (this.incidentMatrix[from][col] != -1 || this.incidentMatrix[to][col] != 1) {
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
    public void addArc(int from, int to) {
        if (!this.isArc(from, to) && from != to) {
            this.nbArcs++;
            int[][] incidentMatrixUpdated = new int[this.nbNodes][this.nbArcs];
            for (int row = 0; row < this.incidentMatrix.length; row++) {
                System.arraycopy(this.incidentMatrix[row], 0, incidentMatrixUpdated[row], 0, this.incidentMatrix[row].length);
            }
            incidentMatrixUpdated[from][this.nbArcs-1] = -1;
            incidentMatrixUpdated[to][this.nbArcs-1] = 1;
            this.incidentMatrix = incidentMatrixUpdated;
        }
    }

    @Override
    public int[] getSuccessors(int x) {
        List<Integer> successorsList = new ArrayList<Integer>();
        for (int arc = 0; arc < this.nbArcs; arc++) {
            if (this.incidentMatrix[x][arc] == -1) {
                for (int i = 0; i < this.incidentMatrix.length; i++) {
                    if (this.incidentMatrix[i][arc] == 1) {
                        successorsList.add(i);
                        break;
                    }
                }
            }
        }

        int[] successors = new int[successorsList.size()];
        for (int i = 0; i < successorsList.size(); i++) {
            successors[i] = successorsList.get(i);
        }

        return successors;
    }

    @Override
    public int[] getPredecessors(int x) {
        List<Integer> predecessorsList = new ArrayList<Integer>();
        for (int arc = 0; arc < this.nbArcs; arc++) {
            if (this.incidentMatrix[x][arc] == 1) {
                for (int i = 0; i < this.incidentMatrix.length; i++) {
                    if (this.incidentMatrix[i][arc] == -1) {
                        predecessorsList.add(i);
                        break;
                    }
                }
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
        int[][] incidentMatrixUpdated = new int[this.nbNodes][this.nbArcs];
        for (int row = 0; row < this.nbNodes; row++) {
            for (int col = 0; col < this.nbArcs; col++) {
                if (this.incidentMatrix[row][col] == -1) {
                    incidentMatrixUpdated[row][col] = 1;
                } else if (this.incidentMatrix[row][col] == 1) {
                    incidentMatrixUpdated[row][col] = -1;
                } else {
                    incidentMatrixUpdated[row][col] = 0;
                }
            }
        }
        return new IncidentMatrixDirectedGraph(incidentMatrixUpdated, nbNodes, nbArcs);
    }

    @Override
    public int getNbNodes() {
        return this.nbNodes;
    }

    @Override
	public List<List<Integer>> getComposanteFortementConnexe() {
		return ParcoursProfondeurTools.parcoursProfondeur(this);
	}
    
    @Override
    public int[][] toAdjacencyMatrix() {
        int[][] adjacencyMatrix = new int[this.nbNodes][this.nbNodes];
        for (int arc = 0; arc < this.nbArcs; arc++) {
            int from = -1;
            int to = -1;
            for (int row = 0; row < this.nbNodes; row++) {
                if (this.incidentMatrix[row][arc] == -1) {
                    from = row;
                } else if (this.incidentMatrix[row][arc] == 1) {
                    to = row;
                }
                if (from != -1 && to != -1) {
                    break;
                }
            }
            adjacencyMatrix[from][to] = 1;
        }
        return adjacencyMatrix;
    }
}
