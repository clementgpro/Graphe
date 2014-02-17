package test;

import implementation.adjacency.list.AdjacencyListUndirectedGraph;
import implementation.adjacency.matrix.AdjacencyMatrixUndirectedGraph;
import implementation.incident.IncidentMatrixUndirectedGraph;
import implementation.tools.GraphTools;
import interfaces.IUndirectedGraph;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(value=Parameterized.class)
public class IUndirectedGraphTest {
    private Class<IUndirectedGraph> implClass;

    public IUndirectedGraphTest(Class<IUndirectedGraph> cl) {
        this.implClass = cl;
    }

    @Test
    public void testComposantConnexe() throws Exception {
    	IUndirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[]{int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 3, true));
    	
//    	Assert.assertEquals(3, graph.getComposanteConnexe().);
    }
    @Test
    public void testGetNbEdges() throws Exception {
        IUndirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[]{int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 3, true));
        Assert.assertEquals(3, graph.getNbEdges());
    }

    @Test
    public void testIsEdge() throws Exception {
        IUndirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[]{int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 3, true));
        Assert.assertTrue(graph.isEdge(0, 1));
        Assert.assertFalse(graph.isEdge(0, 0));
    }

    @Test
    public void testRemoveEdge() throws Exception {
        IUndirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[]{int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 3, true));
        graph.removeEdge(0, 1);
        Assert.assertFalse(graph.isEdge(0, 1));
        Assert.assertFalse(graph.isEdge(1, 0));
        Assert.assertEquals(2, graph.getNbEdges());

        graph.removeEdge(0, 1);
        Assert.assertEquals(2, graph.getNbEdges());
    }

    @Test
    public void testAddEdge() throws Exception {
        IUndirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[]{int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 0, true));
        graph.addEdge(0, 1);
        Assert.assertTrue(graph.isEdge(0, 1));
        Assert.assertTrue(graph.isEdge(1, 0));
        Assert.assertEquals(1, graph.getNbEdges());

        graph.addEdge(0, 1);
        Assert.assertEquals(1, graph.getNbEdges());

        graph.addEdge(0, 0);
        Assert.assertEquals(1, graph.getNbEdges());
        Assert.assertFalse(graph.isEdge(0, 0));
    }

    @Test
    public void testGetNeighbors() throws Exception {
        IUndirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[]{int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 0, true));
        Assert.assertEquals(0, graph.getNeighbors(0).length);
        graph = this.implClass.getDeclaredConstructor(new Class[]{int[][].class}).newInstance((Object)new int[][]{{0, 0, 1},{0, 0, 0},{1, 0, 0}});
        Assert.assertEquals(1, graph.getNeighbors(0).length);
        Assert.assertEquals(2, graph.getNeighbors(0)[0]);
    }

    @Test
    public void testGetNbNodes() throws Exception {
        IUndirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[]{int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 0, true));
        Assert.assertEquals(3, graph.getNbNodes());
    }

    @Test
    public void testToAdjacencyMatrix() throws Exception {
        IUndirectedGraph graph = this.implClass.getDeclaredConstructor(new Class[]{int[][].class}).newInstance((Object)GraphTools.generateGraphData(3, 3, true));
        int[][] testMatric = new int[][]{{0, 1, 1},{1, 0, 1},{1, 1, 0}};
        Assert.assertArrayEquals(testMatric, graph.toAdjacencyMatrix());
    }

    @Parameterized.Parameters
    public static Collection<Object[]> implList() {
        return Arrays.asList(
                new Object[]{AdjacencyListUndirectedGraph.class},
                new Object[]{AdjacencyMatrixUndirectedGraph.class},
                new Object[]{IncidentMatrixUndirectedGraph.class}
        );
    }
}
