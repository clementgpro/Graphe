package test;

import implementation.adjacency.list.AdjacencyListDirectedGraph;
import implementation.adjacency.matrix.AdjacencyMatrixDirectedGraph;
import implementation.incident.IncidentMatrixDirectedGraph;
import implementation.tools.GraphTools;
import interfaces.IDirectedGraph;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(value=Parameterized.class)
public class IDirectedGraphTest {
    private Class<IDirectedGraph> implClass;

    public IDirectedGraphTest(Class<IDirectedGraph> cl) {
        this.implClass = cl;
    }
    
    @Parameterized.Parameters
    public static Collection<Object[]> implList() {
        return Arrays.asList(
                new Object[]{AdjacencyListDirectedGraph.class},
                new Object[]{AdjacencyMatrixDirectedGraph.class},
                new Object[]{IncidentMatrixDirectedGraph.class}
        );
    }
    
    @Test
    public void testGetNbArcs() throws Exception {
        IDirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[] {int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 6, false));
        Assert.assertEquals(6, graph.getNbArcs());
    }

    @Test
    public void testIsArc() throws Exception {
        IDirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[] {int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 6, false));
        Assert.assertTrue(graph.isArc(0, 1));
        Assert.assertFalse(graph.isArc(0, 0));
    }

    @Test
    public void testRemoveArc() throws Exception {
        IDirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[] {int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 6, false));
        graph.removeArc(0, 1);
        Assert.assertFalse(graph.isArc(0, 1));
        Assert.assertTrue(graph.isArc(1, 0));
        Assert.assertEquals(5, graph.getNbArcs());

        graph.removeArc(0 ,1);
        Assert.assertEquals(5, graph.getNbArcs());
    }

    @Test
    public void testAddArc() throws Exception {
        IDirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[] {int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 0, false));
        graph.addArc(0, 1);
        Assert.assertTrue(graph.isArc(0, 1));
        Assert.assertFalse(graph.isArc(1, 0));
        Assert.assertEquals(1, graph.getNbArcs());

        graph.addArc(0, 1);
        Assert.assertEquals(1, graph.getNbArcs());

        graph.addArc(0, 0);
        Assert.assertEquals(1, graph.getNbArcs());
        Assert.assertFalse(graph.isArc(0, 0));
    }

    @Test
    public void testGetSuccessors() throws Exception {
        IDirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[] {int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 0, false));
        Assert.assertEquals(0, graph.getSuccessors(0).length);

        graph.addArc(0, 2);
        Assert.assertEquals(1, graph.getSuccessors(0).length);
        Assert.assertEquals(2, graph.getSuccessors(0)[0]);
    }

    @Test
    public void testGetPredecessors() throws Exception {
        IDirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[] {int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 0, false));
        Assert.assertEquals(0, graph.getPredecessors(0).length);

        graph.addArc(0, 2);
        Assert.assertEquals(1, graph.getPredecessors(2).length);
        Assert.assertEquals(0, graph.getPredecessors(2)[0]);
    }

    @Test
    public void testComputeInverse() throws Exception {
        IDirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[] {int[][].class}).newInstance((Object)new int[][]{{0, 1, 0},{0, 0, 1},{0, 0, 0}});
        IDirectedGraph inverseGraph = graph.computeInverse();
        int[][] testMatrix = new int[][]{{0, 0, 0},{1, 0, 0},{0, 1, 0}};
        Assert.assertArrayEquals(testMatrix, inverseGraph.toAdjacencyMatrix());
    }

    @Test
    public void testGetNbNodes() throws Exception {
        IDirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[] {int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 0, false));
        Assert.assertEquals(3, graph.getNbNodes());
    }

    @Test
    public void testToAdjacencyMatrix() throws Exception {
        IDirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[] {int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 6, false));
        int[][] testMatrix = new int[][]{{0, 1, 1},{1, 0, 1},{1, 1, 0}};
        Assert.assertArrayEquals(testMatrix, graph.toAdjacencyMatrix());
    }
}
