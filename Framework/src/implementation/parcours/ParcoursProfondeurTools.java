package implementation.parcours;

import interfaces.IDirectedGraph;
import interfaces.IGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParcoursProfondeurTools {
	/** Moment du parcours. */
	private static int moment = 1;

	/**
	 * Parcours profondeur récursif.
	 * 
	 * @param graph
	 * @return
	 */
	public static List<List<Integer>> parcoursProfondeur(IGraph graph) {
		moment = 1;
		int debut[] = new int[graph.getNbNodes()];
		int fin[] = new int[graph.getNbNodes()];
		List<List<Integer>> composantes = new ArrayList<List<Integer>>();

		for (int s = 0; s < graph.getNbNodes(); s++) {
			if (!contains(composantes, s)) {
				composantes.add(new ArrayList<Integer>());
				explorerSommet(graph, s, composantes, debut, fin);
			}
		}

		if (graph instanceof IDirectedGraph) {
			IDirectedGraph directedGraph = ((IDirectedGraph) graph);
			composantes.clear();
			directedGraph = directedGraph.computeInverse();
			int nodeFinReverse[] = getReverseFinNodes(fin);
			for (int i = 0; i < directedGraph.getNbNodes(); i++) {
				int s = nodeFinReverse[i];
				if (!contains(composantes, s)) {
					composantes.add(new ArrayList<Integer>());
					explorerSommet(directedGraph, s, composantes, debut, fin);
				}
			}
		}

		return composantes;
	}

	private static void explorerSommet(IGraph graph, Integer s, List<List<Integer>> composantes, int debut[], int fin[]) {
		debut[s] = moment;
		composantes.get(composantes.size() - 1).add(s);
		for (Integer t : ParcoursTools.getVoisins(graph, s)) {
			if (!contains(composantes, t)) {
				moment = moment + 1;
				explorerSommet(graph, t, composantes, debut, fin);
			}
		}
		moment = moment + 1;
		fin[s] = moment;
	}

	/**
	 * Contains of a list of list.
	 * 
	 * @param listOfList
	 * @param value
	 * @return
	 */
	private static boolean contains(List<List<Integer>> listOfList, int value) {
		boolean isContains = false;
		Iterator<List<Integer>> iteList = listOfList.iterator();
		while (iteList.hasNext() && !isContains) {
			isContains = iteList.next().contains(value);
		}
		return isContains;
	}

	private static int[] getReverseFinNodes(int[] fin) {
		List<Integer> listIndexNodeDone = new ArrayList<Integer>();
		int[] nodes = new int[fin.length];
		for (int i = 0; i < nodes.length; i++) {
			int indiceMaximum = getIndiceMaximum(fin, listIndexNodeDone);
			listIndexNodeDone.add(indiceMaximum);
			nodes[i] = indiceMaximum;
		}
		return nodes;
	}

	private static int getIndiceMaximum(int[] fin, List<Integer> listIndexNodeDone) {
		int indiceMax = 0;
		int maximum = 0;
		for (int j = 0; j < fin.length; j++) {
			if (!listIndexNodeDone.contains(j)) {
				if (fin[j] > maximum) {
					maximum = fin[j];
					indiceMax = j;
				}
			}
		}
		return indiceMax;
	}
}
