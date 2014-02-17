package implementation.adjacency.matrix;


import implementation.parcours.ParcoursProfondeurTools;
import interfaces.IUndirectedGraph;

import java.util.Arrays;
import java.util.List;

public class AdjacencyMatrixUndirectedGraph implements IUndirectedGraph{
    int[][] adjacencyMatrix;
    int nbEdges;
    int nbNodes;

    public AdjacencyMatrixUndirectedGraph(int[][] matrix) {
        this.adjacencyMatrix = matrix;
        this.nbNodes = matrix.length;

        int nbEdges = 0;
        for (int[] aMatrix : matrix) {
            for (int linked : aMatrix) {
                if (linked == 1) {
                    nbEdges++;
                }
            }
        }
        this.nbEdges = nbEdges/2;
    }

    public AdjacencyMatrixUndirectedGraph(IUndirectedGraph graph) {
        this.nbEdges = graph.getNbEdges();
        this.nbNodes = graph.getNbNodes();
        this.adjacencyMatrix = graph.toAdjacencyMatrix();
    }

    @Override
    public int getNbEdges() {
        return this.nbEdges;
    }

    @Override
    public boolean isEdge(int x, int y) {
        return this.adjacencyMatrix[x][y] == 1 && this.adjacencyMatrix[y][x] == 1;
    }

    @Override
    public void removeEdge(int x, int y) {
        if (this.isEdge(x, y)) {
            this.adjacencyMatrix[x][y] = 0;
            this.adjacencyMatrix[y][x] = 0;
            this.nbEdges--;
        }
    }

    @Override
    public void addEdge(int x, int y) {
        if (!this.isEdge(x, y) && x != y) {
            this.adjacencyMatrix[x][y] = 1;
            this.adjacencyMatrix[y][x] = 1;
            this.nbEdges++;
        }
    }

    @Override
    public int[] getNeighbors(int x) {
    	int cpt = 0;
    	int[] neighbors = new int[0];
        for (int s = 0; s < this.adjacencyMatrix[x].length; s++) {
            if (this.adjacencyMatrix[x][s] == 1) {
            	neighbors = Arrays.copyOf(neighbors, neighbors.length+1);
            	neighbors[cpt++] = s;
            }
        }

        return neighbors;
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
	public List<List<Integer>> getComposanteConnexe() {
		return ParcoursProfondeurTools.parcoursProfondeur(this);
	}
}
