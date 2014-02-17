package implementation.parcours;

import interfaces.IGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParcoursLargeurTools {

	/**
	 * Parcours largeur itératif.
	 * 
	 * @param graph
	 * @return
	 */
	public static List<Integer> parcoursLargeur(IGraph graph, int s) {
		List<Integer> atteint = new ArrayList<Integer>();
		atteint.add(s);
		boolean mark[] = new boolean[graph.getNbNodes()];
		for (int i = 0; i < mark.length; i++) {
			mark[i] = false;
		}
		mark[s] = true;
		int[] toVisit = new int[1];
		toVisit[0] = s;
		while (toVisit.length > 0) {
			int v = toVisit[toVisit.length - 1];
			toVisit = Arrays.copyOf(toVisit, toVisit.length - 1);
			for (Integer w : ParcoursTools.getVoisins(graph, v)) {
				if (!mark[w]) {
					atteint.add(w);
					mark[w] = true;
					int[] newToVisit = Arrays.copyOf(toVisit, toVisit.length + 1);
					newToVisit[newToVisit.length - 1] = w;
					toVisit = newToVisit;
				}
			}
		}
		return atteint;
	}
}
