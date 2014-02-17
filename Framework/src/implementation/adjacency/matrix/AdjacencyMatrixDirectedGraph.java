package implementation.adjacency.matrix;


import implementation.parcours.ParcoursProfondeurTools;
import interfaces.IDirectedGraph;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyMatrixDirectedGraph implements IDirectedGraph{
    int[][] adjacencyMatrix;
    int nbArcs;
    int nbNodes;

    public AdjacencyMatrixDirectedGraph(int[][] matrix) {
        this.adjacencyMatrix = matrix;
        this.nbNodes = matrix.length;

        int nbArcs = 0;
        for (int[] aMatrix : matrix) {
            for (int linked : aMatrix) {
                if (linked == 1) {
                    nbArcs++;
                }
            }
        }
        this.nbArcs = nbArcs;
    }

    public AdjacencyMatrixDirectedGraph(IDirectedGraph graph) {
        this.nbArcs = graph.getNbArcs();
        this.nbNodes = graph.getNbNodes();
        this.adjacencyMatrix = graph.toAdjacencyMatrix();
    }

    @Override
    public int getNbArcs() {
        return this.nbArcs;
    }

    @Override
    public boolean isArc(int from, int to) {
        return this.adjacencyMatrix[from][to] == 1;
    }

    @Override
    public void removeArc(int from, int to) {
        if (this.isArc(from, to)) {
            this.adjacencyMatrix[from][to] = 0;
            this.nbArcs--;
        }
    }

    @Override
    public void addArc(int from, int to) {
        if (!this.isArc(from, to) && from != to) {
            this.adjacencyMatrix[from][to] = 1;
            this.nbArcs++;
        }
    }

    @Override
    public int[] getSuccessors(int x) {
        List<Integer> successorsList = new ArrayList<Integer>();
        for (int i = 0; i < this.adjacencyMatrix[x].length; i++) {
            if (this.adjacencyMatrix[x][i] == 1) {
                successorsList.add(i);
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
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            if (this.adjacencyMatrix[i][x] == 1) {
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
                    matrix[x][y] = 0;
                } else if (this.adjacencyMatrix[x][y] == 1) {
                    matrix[y][x] = 1;
                }
            }
        }
        return new AdjacencyMatrixDirectedGraph(matrix);
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
        return this.adjacencyMatrix;
    }
}
