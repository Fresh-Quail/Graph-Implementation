import org.junit.*;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class WeightedGraphTests {

    @Test
    public void testAddNode() throws NoSuchFieldException, IllegalAccessException {
        WeightedGraph graph = new WeightedGraph();
        Field arrayField = graph.getClass().getDeclaredField("names");
        arrayField.setAccessible(true);

        //Tests zero
        assertEquals(0, ((ArrayList<String>) arrayField.get(graph)).size());
        //Tests one
        graph.addNode("c");
        assertEquals(1, ((ArrayList<String>) arrayField.get(graph)).size());
        //Tests many
        graph.addNode("p");
        assertEquals(2, ((ArrayList<String>) arrayField.get(graph)).size());

        graph.addNode("a");
        graph.addNode("b");
        assertEquals(4, ((ArrayList<String>) arrayField.get(graph)).size());

        //Tests duplicates
        graph.addNode("a");
        graph.addNode("b");
        assertEquals(4, ((ArrayList<String>) arrayField.get(graph)).size());

        //Tests alphabetical
        assertArrayEquals(new String[]{"a", "b", "c", "p"}, ((ArrayList<String>) arrayField.get(graph)).toArray(new String[]{}));

    }

    @Test
    public void testAddNodes() throws NoSuchFieldException, IllegalAccessException {
        WeightedGraph graph = new WeightedGraph();
        Field arrayField = graph.getClass().getDeclaredField("names");
        arrayField.setAccessible(true);

        //Tests adding an empty array
        assertTrue(graph.addNodes(new String[]{}));
        assertEquals(0, ((ArrayList<String>) arrayField.get(graph)).size());

        //Tests adding an array of one value
        assertTrue(graph.addNodes(new String[]{"p"}));
        assertEquals(1, ((ArrayList<String>) arrayField.get(graph)).size());

        //Tests many
        assertTrue(graph.addNodes(new String[]{"a", "c", "e", "g"}));
        assertEquals(5, ((ArrayList<String>) arrayField.get(graph)).size());

        //Tests duplicates
        assertFalse(graph.addNodes(new String[]{"a", "c", "e", "g"}));
        assertFalse(graph.addNodes(new String[]{"c", "c", "c", "a"}));
        assertEquals(5, ((ArrayList<String>) arrayField.get(graph)).size());

        //Tests alphabetical
        assertArrayEquals(new String[]{"a", "c", "e", "g", "p"}, ((ArrayList<String>) arrayField.get(graph)).toArray(new String[]{}));
        graph.addNodes(new String[]{"b", "d", "f"});
        assertArrayEquals(new String[]{"a", "b", "c", "d", "e", "f", "g", "p"}, ((ArrayList<String>) arrayField.get(graph)).toArray(new String[]{}));
    }

    @Test
    public void testaddWeightedEdge() throws NoSuchFieldException, IllegalAccessException {
        WeightedGraph graph = new WeightedGraph();
        Field arrayField = graph.getClass().getDeclaredField("neighborMap");
        arrayField.setAccessible(true);

        //Tests adding edges to an empty graph
        assertFalse(graph.addWeightedEdge("a", "b", 10));
        assertFalse(graph.addWeightedEdge("a", "a", 10));
        //Tests adding edges to a graph with one node
        graph.addNode("a");
        assertFalse(graph.addWeightedEdge("a", "b", 10));
        assertFalse(graph.addWeightedEdge("a", "a", 10));

        //Tests edges in a graph with multiple nodes
        graph.addNodes(new String[]{"b", "c", "d", "e", "f"});
        //Tests no edges
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                assertEquals(null, ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(i).get(j));
            }
        }

        //Tests one edge
        graph.addWeightedEdge("b", "c", 10);
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                if (i == 1 && j == 2)
                    assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(i).get(j));
                else
                    assertNull(((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(i).get(j));
            }
        }
        //Tests many edges
        graph.addWeightedEdge("a", "c", 10);
        graph.addWeightedEdge("c", "d", 10);
        graph.addWeightedEdge("d", "e", 0);
        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(0).get(2));
        assertNull(((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(2).get(0));

        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(2).get(3));
        assertNull(((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(3).get(2));

        assertEquals(Integer.valueOf(0), ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(3).get(4));
        assertNull(((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(4).get(3));

        assertNull(((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(0).get(1));
        assertNull(((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(1).get(0));
    }

    @Test
    public void testaddWeightedEdges() throws NoSuchFieldException, IllegalAccessException {
        WeightedGraph graph = new WeightedGraph();
        Field arrayField = graph.getClass().getDeclaredField("neighborMap");
        arrayField.setAccessible(true);

        //Tests adding edges to a WeightedGraph with no nodes - And if edges match up with nodes
        assertTrue(graph.addWeightedEdges("a", new String[]{}, new int[]{}));
        assertTrue(graph.addWeightedEdges("a", new String[]{}, new int[]{10}));
        assertTrue(graph.addWeightedEdges("a", new String[]{}, new int[]{10, 10, 10}));

        assertFalse(graph.addWeightedEdges("a", new String[]{"a"}, new int[]{}));
        assertFalse(graph.addWeightedEdges("a", new String[]{"a"}, new int[]{10}));
        assertFalse(graph.addWeightedEdges("a", new String[]{"a"}, new int[]{10, 10, 10}));

        assertFalse(graph.addWeightedEdges("a", new String[]{"a", "b", "c"}, new int[]{}));
        assertFalse(graph.addWeightedEdges("a", new String[]{"a", "b", "c"}, new int[]{10}));
        assertFalse(graph.addWeightedEdges("a", new String[]{"a", "b", "c"}, new int[]{10, 10, 10}));
        //Tests adding edges to a WeightedGraph with one node - And if edges match up with nodes
        graph.addNode("a");
        assertTrue(graph.addWeightedEdges("a", new String[]{}, new int[]{}));
        assertTrue(graph.addWeightedEdges("a", new String[]{}, new int[]{10}));
        assertTrue(graph.addWeightedEdges("a", new String[]{}, new int[]{10, 10, 10}));

        assertFalse(graph.addWeightedEdges("a", new String[]{"a"}, new int[]{}));
        assertFalse(graph.addWeightedEdges("a", new String[]{"a"}, new int[]{10}));
        assertFalse(graph.addWeightedEdges("a", new String[]{"a"}, new int[]{10, 10, 10}));

        assertFalse(graph.addWeightedEdges("a", new String[]{"a", "b", "c"}, new int[]{}));
        assertFalse(graph.addWeightedEdges("a", new String[]{"a", "b", "c"}, new int[]{10}));
        assertFalse(graph.addWeightedEdges("a", new String[]{"a", "b", "c"}, new int[]{10, 10, 10}));

        assertNull(((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(0).get(0));
        //Tests matrix representation
        assertEquals(1, ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).size());
        assertEquals(1, ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(0).size());

        //Tests adding edges to a WeightedGraph with many nodes
        //Tests adding an empty array
        graph.addNodes(new String[]{"b", "c", "d", "e", "f"});

        assertTrue(graph.addWeightedEdges("a", new String[]{}, new int[]{}));
        assertTrue(graph.addWeightedEdges("a", new String[]{}, new int[]{10}));
        assertTrue(graph.addWeightedEdges("a", new String[]{}, new int[]{10, 10, 10}));
        //Tests adding an array of one value - And if edges match up with nodes
        assertFalse(graph.addWeightedEdges("a", new String[]{"c"}, new int[]{}));
        assertTrue(graph.addWeightedEdges("a", new String[]{"c"}, new int[]{10, 10, 10}));;
        assertTrue(graph.addWeightedEdges("a", new String[]{"c"}, new int[]{10}));

        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>)arrayField.get(graph)).get(0).get(2));
        assertNull(((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(2).get(0));

        //Tests adding an array of many values - And if edges match up with nodes
        assertFalse(graph.addWeightedEdges("a", new String[]{"b", "d", "e", "f"}, new int[]{}));
        assertFalse(graph.addWeightedEdges("a", new String[]{"b", "d", "e", "f"}, new int[]{10}));
        assertTrue(graph.addWeightedEdges("a", new String[]{"d", "e", "f"}, new int[]{10, 10, 10}));;

        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(0).get(1));
        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(0).get(2));
        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(0).get(3));
        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(0).get(4));
        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(0).get(5));

        //Tests duplicates
        assertFalse(graph.addWeightedEdges("a", new String[]{"a", "c", "e", "g"}, new int[]{10, 10, 10, 10}));
        assertFalse(graph.addWeightedEdges("a", new String[]{"c", "c", "c", "a"}, new int[]{10, 10, 10, 10}));
        assertFalse(graph.addWeightedEdges("c", new String[]{"c", "c", "c", "f"}, new int[]{10, 10, 10, 10}));
        assertNull(((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(2).get(2));
        assertNull(((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(0).get(0));

        //Tests matrix representation
        assertEquals(6, ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).size());
        assertEquals(6, ((ArrayList<ArrayList<Integer>>) arrayField.get(graph)).get(0).size());
    }

    @Test
    public void testRemoveNode() throws NoSuchFieldException, IllegalAccessException {
        WeightedGraph graph = new WeightedGraph();
        Field names = graph.getClass().getDeclaredField("names");
        Field map = graph.getClass().getDeclaredField("neighborMap");
        names.setAccessible(true);
        map.setAccessible(true);

        graph.addNodes(new String[]{"a", "b", "c", "d", "e"});
        graph.addWeightedEdge("b", "c", 10);
        graph.addWeightedEdge("c", "b", 10);
        graph.addWeightedEdge("a", "d", 10);
        graph.addWeightedEdge("d", "a", 10);
        //Tests removing a node from a WeightedGraph with many
        //Removes last node
        assertFalse(graph.removeNode("l"));
        assertTrue(graph.removeNode("e"));
        assertEquals(4, ((ArrayList<String>) names.get(graph)).size());
        assertEquals(4, ((ArrayList<ArrayList<Integer>>) map.get(graph)).size());
        assertEquals(4, ((ArrayList<ArrayList<Integer>>) map.get(graph)).get(0).size());

        //Tests the rearranging matrix
        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) map.get(graph)).get(0).get(3));
        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) map.get(graph)).get(3).get(0));
        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) map.get(graph)).get(1).get(2));
        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) map.get(graph)).get(2).get(1));

        //Removes first node
        assertFalse(graph.removeNode("e"));
        assertTrue(graph.removeNode("a"));
        assertEquals(3, ((ArrayList<String>) names.get(graph)).size());
        assertEquals(3, ((ArrayList<ArrayList<Integer>>) map.get(graph)).size());
        assertEquals(3, ((ArrayList<ArrayList<Integer>>) map.get(graph)).get(0).size());

        //Tests the rearranging matrix
        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) map.get(graph)).get(0).get(1));
        assertEquals(Integer.valueOf(10), ((ArrayList<ArrayList<Integer>>) map.get(graph)).get(1).get(0));
        assertNull(((ArrayList<ArrayList<Integer>>) map.get(graph)).get(2).get(0));

        //Removes middle node
        assertFalse(graph.removeNode("a"));
        assertTrue(graph.removeNode("c"));
        assertEquals(2, ((ArrayList<String>) names.get(graph)).size());
        assertEquals(2, ((ArrayList<ArrayList<Integer>>) map.get(graph)).size());
        assertEquals(2, ((ArrayList<ArrayList<Integer>>) map.get(graph)).get(0).size());

        //Tests the rearranging matrix
        assertNull(((ArrayList<ArrayList<Integer>>) map.get(graph)).get(0).get(1));
        assertNull(((ArrayList<ArrayList<Integer>>) map.get(graph)).get(1).get(0));

        //Tests removing the last node of a WeightedGraph
        assertTrue(graph.removeNode("b"));
        assertNull(((ArrayList<ArrayList<Integer>>) map.get(graph)).get(0).get(0));

        assertTrue(graph.removeNode("d"));
        assertEquals(0, ((ArrayList<String>) names.get(graph)).size());
        assertEquals(0, ((ArrayList<ArrayList<Integer>>) map.get(graph)).size());

        //Tests removing from an empty WeightedGraph
        assertFalse(graph.removeNode("b"));
        assertFalse(graph.removeNode("a"));
    }

    @Test
    public void testRemoveNodes() {
        WeightedGraph graph = new WeightedGraph();
        graph.addNodes(new String[]{"a", "b", "c", "d", "e", "f", "g", "h"});
        //Tests removing zero nodes
        assertTrue(graph.removeNodes(new String[]{}));

        //Tests removing one node
        assertTrue(graph.removeNodes(new String[]{"a"}));
        assertFalse(graph.removeNodes(new String[]{"k"}));

        //Test removing many nodes
        assertTrue(graph.removeNodes(new String[]{"b", "c", "d"}));
        assertFalse(graph.removeNodes(new String[]{"e", "f", "a"}));
        //Test removing duplicate nodes
        assertFalse(graph.removeNodes(new String[]{"g", "h", "g", "h"}));

        //Tests removing from an empty list
        assertTrue(graph.removeNodes(new String[]{}));
        assertFalse(graph.removeNodes(new String[]{"a"}));
        assertFalse(graph.removeNodes(new String[]{"e", "f", "a"}));
    }

    @Test
    public void testRead_PrintWeightedGraph() throws FileNotFoundException {
        WeightedGraph graph = new WeightedGraph();

        //Tests reading an empty WeightedGraph
        graph = WeightedGraph.readWeighted("C:\\Users\\Aibra\\Alcove\\Mound\\P6\\test resources\\EmptyW.txt");
        graph.printWeightedGraph();
        //Tests reading a WeightedGraph with one node
        graph = WeightedGraph.readWeighted("C:\\Users\\Aibra\\Alcove\\Mound\\P6\\test resources\\OneNodeW.txt");
        graph.printWeightedGraph();
        //Tests reading a WeightedGraph with many nodes
        graph = WeightedGraph.readWeighted("C:\\Users\\Aibra\\Alcove\\Mound\\P6\\test resources\\ManyNodesW.txt");
        graph.printWeightedGraph();
        //Tests reading a WeightedGraph with nodes and neighbors
        graph = WeightedGraph.readWeighted("C:\\Users\\Aibra\\Alcove\\Mound\\P6\\test resources\\NodesNeighborsW.txt");
        graph.printWeightedGraph();
    }

    @Test
    public void testShortestPath()
    {
        WeightedGraph graph = new WeightedGraph();

        //Tests shortestPath on a graph with no nodes
        assertArrayEquals(new String[]{}, graph.shortestPath("a", "b"));

        //Tests shortestPath on a graph with one node
        graph.addNode("a");
        assertArrayEquals(new String[]{}, graph.shortestPath("a", "b"));
        assertArrayEquals(new String[]{"a"}, graph.shortestPath("a", "a"));

        //Tests shortestPath on a graph with many nodes
        graph.addNodes(new String[]{"b", "c"});
        graph.addWeightedEdge("a", "b", 1);
        graph.addWeightedEdge("b", "c", 1);

        assertArrayEquals(new String[]{}, graph.shortestPath("a", "e"));

        assertArrayEquals(new String[]{"a", "b"}, graph.shortestPath("a", "b"));
        assertArrayEquals(new String[]{}, graph.shortestPath("b", "a"));

        assertArrayEquals(new String[]{"a", "b", "c"}, graph.shortestPath("a", "c"));
        assertArrayEquals(new String[]{}, graph.shortestPath("c", "a"));

        //Tests shortestPath on a hexagon with a line inside
        graph.removeEdge("b", "c");
        graph.addWeightedEdge("c", "b", 1);
        graph.addNodes(new String[]{"d", "e", "f", "x", "y"});
        graph.addWeightedEdge("c", "d", 5);
        graph.addWeightedEdge("a", "f", 3);

        graph.addWeightedEdge("d", "e", 1);
        graph.addWeightedEdge("f", "e", 1);

        graph.addWeightedEdge("x", "y", 1);
        graph.addWeightedEdge("y", "x", 1);
        graph.addWeightedEdge("b", "x", 3);
        graph.addWeightedEdge("e", "y", 5);

        graph.addWeightedEdges("x", new String[]{"a", "c"}, new int[]{1, 1});
        graph.addWeightedEdges("y", new String[]{"d", "f"}, new int[]{1, 1});

        //Hexagon with a line inside
        assertArrayEquals(new String[]{"b", "x", "y", "f"}, graph.shortestPath("b", "f"));

        assertArrayEquals(new String[]{"b", "x", "y", "d"}, graph.shortestPath("b", "d"));

        assertArrayEquals(new String[]{"b", "x", "y", "f", "e"}, graph.shortestPath("b", "e"));

        assertArrayEquals(new String[]{"c", "d", "e"}, graph.shortestPath("c", "e"));

        graph.addWeightedEdge("d", "e", 10);
        assertArrayEquals(new String[]{"c", "b", "x", "y", "f", "e"}, graph.shortestPath("c", "e"));
        graph.addWeightedEdge("d", "e", 1);

        assertArrayEquals(new String[]{"b", "x", "y", "f"}, graph.shortestPath("b", "f"));

        graph.addWeightedEdge("y","f", 10);
        assertArrayEquals(new String[]{"b", "x", "a", "f"}, graph.shortestPath("b", "f"));

    }

    @Test
    public void testSecondShortestPath()
    {
        WeightedGraph graph = new WeightedGraph();

        //Tests secondShortestPath on a graph with no nodes
        assertArrayEquals(new String[]{}, graph.secondShortestPath("a", "b"));

        //Tests secondShortestPath on a graph with one node
        graph.addNode("a");
        assertArrayEquals(new String[]{}, graph.secondShortestPath("a", "b"));
        assertArrayEquals(new String[]{}, graph.secondShortestPath("a", "a"));

        //Tests secondShortestPath on a graph with many nodes
        graph.addNodes(new String[]{"b", "c"});
        graph.addWeightedEdge("a", "b", 1);
        graph.addWeightedEdge("b", "c", 1);

        assertArrayEquals(new String[]{}, graph.secondShortestPath("a", "e"));

        assertArrayEquals(new String[]{}, graph.secondShortestPath("a", "b"));
        assertArrayEquals(new String[]{}, graph.secondShortestPath("b", "a"));

        assertArrayEquals(new String[]{}, graph.secondShortestPath("a", "c"));
        assertArrayEquals(new String[]{}, graph.secondShortestPath("c", "a"));

        //Tests secondShortestPath on a hexagon with a line inside
        graph.removeEdge("b", "c");
        graph.addWeightedEdge("c", "b", 1);
        graph.addNodes(new String[]{"d", "e", "f", "x", "y"});
        graph.addWeightedEdge("c", "d", 5);
        graph.addWeightedEdge("a", "f", 3);

        graph.addWeightedEdge("d", "e", 1);
        graph.addWeightedEdge("f", "e", 1);

        graph.addWeightedEdge("x", "y", 1);
        graph.addWeightedEdge("y", "x", 1);
        graph.addWeightedEdge("b", "x", 3);
        graph.addWeightedEdge("e", "y", 5);

        graph.addWeightedEdges("x", new String[]{"a", "c"}, new int[]{1, 1});
        graph.addWeightedEdges("y", new String[]{"d", "f"}, new int[]{1, 1});

        //Hexagon with a line inside
        assertArrayEquals(new String[]{"b", "x", "a", "f"}, graph.secondShortestPath("b", "f"));

        assertArrayEquals(new String[]{"b", "x", "c", "d"}, graph.secondShortestPath("b", "d"));

        assertArrayEquals(new String[]{}, graph.secondShortestPath("b", "e"));

        assertArrayEquals(new String[]{"c", "b", "x", "y", "f", "e"}, graph.secondShortestPath("c", "e"));

        assertArrayEquals(new String[]{"b", "x", "a", "f"}, graph.secondShortestPath("b", "f"));
    }
}
