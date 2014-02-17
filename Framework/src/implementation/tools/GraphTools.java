package implementation.tools;

import implementation.adjacency.matrix.AdjacencyMatrixDirectedGraph;
import implementation.adjacency.matrix.AdjacencyMatrixUndirectedGraph;
import interfaces.IDirectedGraph;
import interfaces.IUndirectedGraph;

import java.util.Random;

public class GraphTools {
	public static int[][] generateGraphData(int n, int m, boolean s) {
		assert m > n * n : "requires m <= n²";

		Random r = new Random(1);
		int[][] matrix = new int[n][n];
		// mise en place
		int i = 0;
		while (i < m) {
			int l = r.nextInt(n);
			int c = r.nextInt(n);
			if (l != c && matrix[l][c] != 1) {
				matrix[l][c] = 1;
				// si matrice symetrique
				if (s)
					matrix[c][l] = 1;
				i++;
			}

		}
		return matrix;
	}

	public static String printMatrix(int[][] matrix) {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				int tmp = (matrix[i][j] == Integer.MAX_VALUE) ? 0 : matrix[i][j];
				res.append(tmp + ";");
			}
			res.append("\n");
		}
		return res.toString();
	}

	public static void main(String[] args) {
		int[][] matrixUndirected = GraphTools.generateGraphData(3, 3, true);
//		int[][] matrix = new int[][] { { 0, 1, 1, 0 }, { 0, 0, 0, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }};
		int[][] matrixDirected = new int[][] {
				{0,0,1,0,1,0},
				{1,0,0,0,0,0},
				{0,1,0,1,0,0},
				{0,0,0,0,1,0},
				{0,0,0,1,0,0},
				{0,0,1,0,0,0}
		};
		IUndirectedGraph uGraph = new AdjacencyMatrixUndirectedGraph(matrixUndirected);
		IDirectedGraph dGraph = new AdjacencyMatrixDirectedGraph(matrixDirected);
		// System.out.println("Parcours en profondeur : " + ParcoursProfondeurTools.parcoursProfondeur(uGraph));
		System.out.println("Graphe non orienté : " + uGraph.getComposanteConnexe());
		System.out.println("Graphe orienté : " + dGraph.getComposanteFortementConnexe());
//		System.out.println("Parcours en largeur : " + ParcoursLargeurTools.parcoursLargeur(uGraph,0));
//		System.out.println("Parcours en largeur : " + ParcoursLargeurTools.parcoursLargeur(dGraph,0));
		System.out.println(GraphTools.printMatrix(matrixUndirected));
	}
}
