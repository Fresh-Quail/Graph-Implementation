import org.junit.*;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class GraphTests {

    @Test
    public void testAddNode() throws NoSuchFieldException, IllegalAccessException {
        Graph graph = new Graph();
        Field arrayField = graph.getClass().getDeclaredField("names");
        arrayField.setAccessible(true);

        //Tests zero
        assertEquals(0, ((ArrayList<String>)arrayField.get(graph)).size());
        //Tests one
        graph.addNode("c");
        assertEquals(1, ((ArrayList<String>)arrayField.get(graph)).size());
        //Tests many
        graph.addNode("p");
        assertEquals(2, ((ArrayList<String>)arrayField.get(graph)).size());

        graph.addNode("a");
        graph.addNode("b");
        assertEquals(4, ((ArrayList<String>)arrayField.get(graph)).size());

        //Tests duplicates
        graph.addNode("a");
        graph.addNode("b");
        assertEquals(4, ((ArrayList<String>)arrayField.get(graph)).size());

        //Tests alphabetical
        assertArrayEquals(new String[]{"a", "b", "c", "p"}, ((ArrayList<String>)arrayField.get(graph)).toArray(new String[]{}));

    }

    @Test
    public void testAddNodes() throws NoSuchFieldException, IllegalAccessException {
        Graph graph = new Graph();
        Field arrayField = graph.getClass().getDeclaredField("names");
        arrayField.setAccessible(true);

        //Tests adding an empty array
        assertTrue(graph.addNodes(new String[]{}));
        assertEquals(0, ((ArrayList<String>)arrayField.get(graph)).size());

        //Tests adding an array of one value
        assertTrue(graph.addNodes(new String[]{"p"}));
        assertEquals(1, ((ArrayList<String>)arrayField.get(graph)).size());

        //Tests many
        assertTrue(graph.addNodes(new String[]{"a", "c", "e", "g"}));
        assertEquals(5, ((ArrayList<String>)arrayField.get(graph)).size());

        //Tests duplicates
        assertFalse(graph.addNodes(new String[]{"a", "c", "e", "g"}));
        assertFalse(graph.addNodes(new String[]{"c", "c", "c", "a"}));
        assertEquals(5, ((ArrayList<String>)arrayField.get(graph)).size());

        //Tests alphabetical
        assertArrayEquals(new String[]{"a", "c", "e", "g", "p"}, ((ArrayList<String>)arrayField.get(graph)).toArray(new String[]{}));
        graph.addNodes(new String[]{"b","d","f"});
        assertArrayEquals(new String[]{"a", "b", "c", "d", "e", "f", "g", "p"}, ((ArrayList<String>)arrayField.get(graph)).toArray(new String[]{}));
    }

    @Test
    public void testAddEdge() throws NoSuchFieldException, IllegalAccessException {
        Graph graph = new Graph();
        Field arrayField = graph.getClass().getDeclaredField("neighborMap");
        arrayField.setAccessible(true);

        //Tests adding edges to an empty graph
        assertFalse(graph.addEdge("a","b"));
        assertFalse(graph.addEdge("a","a"));
        //Tests adding edges to a graph with one node
        graph.addNode("a");
        assertFalse(graph.addEdge("a", "b"));
        assertFalse(graph.addEdge("a","a"));

        //Tests edges in a graph with multiple nodes
        graph.addNodes(new String[]{"b", "c", "d", "e", "f"});
        //Tests no edges
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                assertFalse(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(i).get(j));
            }
        }

        //Tests one edge
        graph.addEdge("b", "c");
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                if(i == 1 && j == 2 || (i == 2 && j == 1))
                    assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(i).get(j));
                else
                    assertFalse(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(i).get(j));
            }
        }
        //Tests many edges
        graph.addEdge("a", "c");
        graph.addEdge("c", "d");
        graph.addEdge("d", "e");
        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(0).get(2));
        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(2).get(0));

        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(2).get(3));
        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(3).get(2));

        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(3).get(4));
        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(4).get(3));

        assertFalse(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(0).get(1));
        assertFalse(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(1).get(0));
    }

    @Test
    public void testAddEdges() throws NoSuchFieldException, IllegalAccessException {
        Graph graph = new Graph();
        Field arrayField = graph.getClass().getDeclaredField("neighborMap");
        arrayField.setAccessible(true);

        //Tests adding edges to a graph with no nodes
        assertTrue(graph.addEdges("a", new String[]{}));
        assertFalse(graph.addEdges("a", new String[]{"a"}));
        assertFalse(graph.addEdges("a", new String[]{"a", "b", "c"}));
        //Tests adding edges to a graph with one node
        graph.addNode("a");
        assertTrue(graph.addEdges("a", new String[]{}));
        assertFalse(graph.addEdges("a", new String[]{"a"}));
        assertFalse(graph.addEdges("a", new String[]{"a", "b", "c"}));
        assertFalse(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(0).get(0));
        //Tests matrix representation
        assertEquals(1, ((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).size());
        assertEquals(1, ((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(0).size());

        //Tests adding edges to a graph with many nodes
        //Tests adding an empty array
        graph.addNodes(new String[]{"b", "c", "d", "e", "f"});

        assertTrue(graph.addEdges("a", new String[]{}));

        //Tests adding an array of one value
        assertTrue(graph.addEdges("a", new String[]{"c"}));
        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(0).get(2));
        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(2).get(0));

        //Tests adding an array of many values
        assertTrue(graph.addEdges("a", new String[]{"b", "d", "e", "f"}));
        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(0).get(1));
        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(0).get(2));
        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(0).get(3));
        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(0).get(4));
        assertTrue(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(0).get(5));

        //Tests duplicates
        assertFalse(graph.addEdges("a", new String[]{"a", "c", "e", "g"}));
        assertFalse(graph.addEdges("a", new String[]{"c", "c", "c", "a"}));
        assertFalse(graph.addEdges("c", new String[]{"c", "c", "c", "f"}));
        assertFalse(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(2).get(2));
        assertFalse(((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(0).get(0));

        //Tests matrix representation
        assertEquals(6, ((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).size());
        assertEquals(6, ((ArrayList<ArrayList<Boolean>>)arrayField.get(graph)).get(0).size());
    }

    @Test
    public void testRemoveNode() throws NoSuchFieldException, IllegalAccessException {
        Graph graph = new Graph();
        Field names = graph.getClass().getDeclaredField("names");
        Field map = graph.getClass().getDeclaredField("neighborMap");
        names.setAccessible(true);
        map.setAccessible(true);

        graph.addNodes(new String[]{"a","b", "c", "d", "e"});
        graph.addEdge("b","c");
        graph.addEdge("a","d");
        //Tests removing a node from a graph with many
        //Removes last node
        assertFalse(graph.removeNode("l"));
        assertTrue(graph.removeNode("e"));
        assertEquals(4, ((ArrayList<String>)names.get(graph)).size());
        assertEquals(4, ((ArrayList<ArrayList<Boolean>>)map.get(graph)).size());
        assertEquals(4, ((ArrayList<ArrayList<Boolean>>)map.get(graph)).get(0).size());

        //Tests the rearranging matrix
        assertTrue(((ArrayList<ArrayList<Boolean>>)map.get(graph)).get(0).get(3));
        assertTrue(((ArrayList<ArrayList<Boolean>>)map.get(graph)).get(3).get(0));
        assertTrue(((ArrayList<ArrayList<Boolean>>)map.get(graph)).get(1).get(2));
        assertTrue(((ArrayList<ArrayList<Boolean>>)map.get(graph)).get(2).get(1));

        //Removes first node
        assertFalse(graph.removeNode("e"));
        assertTrue(graph.removeNode("a"));
        assertEquals(3, ((ArrayList<String>)names.get(graph)).size());
        assertEquals(3, ((ArrayList<ArrayList<Boolean>>)map.get(graph)).size());
        assertEquals(3, ((ArrayList<ArrayList<Boolean>>)map.get(graph)).get(0).size());

        //Tests the rearranging matrix
        assertTrue(((ArrayList<ArrayList<Boolean>>)map.get(graph)).get(0).get(1));
        assertTrue(((ArrayList<ArrayList<Boolean>>)map.get(graph)).get(1).get(0));
        assertFalse(((ArrayList<ArrayList<Boolean>>)map.get(graph)).get(2).get(0));

        //Removes middle node
        assertFalse(graph.removeNode("a"));
        assertTrue(graph.removeNode("c"));
        assertEquals(2, ((ArrayList<String>)names.get(graph)).size());
        assertEquals(2, ((ArrayList<ArrayList<Boolean>>)map.get(graph)).size());
        assertEquals(2, ((ArrayList<ArrayList<Boolean>>)map.get(graph)).get(0).size());

        //Tests the rearranging matrix
        assertFalse(((ArrayList<ArrayList<Boolean>>)map.get(graph)).get(0).get(1));
        assertFalse(((ArrayList<ArrayList<Boolean>>)map.get(graph)).get(1).get(0));

        //Tests removing the last node of a graph
        assertTrue(graph.removeNode("b"));
        assertTrue(graph.removeNode("d"));
        assertEquals(0, ((ArrayList<String>)names.get(graph)).size());
        assertEquals(0, ((ArrayList<ArrayList<Boolean>>)map.get(graph)).size());

        //Tests removing from an empty graph
        assertFalse(graph.removeNode("b"));
        assertFalse(graph.removeNode("a"));
    }

    @Test
    public void testRemoveNodes()
    {
        Graph graph = new Graph();
        graph.addNodes(new String[]{"a","b", "c", "d", "e", "f","g","h"});
        //Tests removing zero nodes
        assertTrue(graph.removeNodes(new String[]{}));

        //Tests removing one node
        assertTrue(graph.removeNodes(new String[]{"a"}));
        assertFalse(graph.removeNodes(new String[]{"k"}));

        //Test removing many nodes
        assertTrue(graph.removeNodes(new String[]{"b","c","d"}));
        assertFalse(graph.removeNodes(new String[]{"e","f","a"}));
        //Test removing duplicate nodes
        assertFalse(graph.removeNodes(new String[]{"g","h","g","h"}));

        //Tests removing from an empty list
        assertTrue(graph.removeNodes(new String[]{}));
        assertFalse(graph.removeNodes(new String[]{"a"}));
        assertFalse(graph.removeNodes(new String[]{"e","f","a"}));
    }

    @Test
    public void testRead_PrintGraph() throws FileNotFoundException {
        Graph graph = new Graph();

        //Tests reading an empty graph
        graph = Graph.read("C:\\Users\\Aibra\\Alcove\\Mound\\P6\\test resources\\Empty.txt");
        graph.printGraph();
        //Tests reading a graph with one node
        graph = Graph.read("C:\\Users\\Aibra\\Alcove\\Mound\\P6\\test resources\\OneNode.txt");
        graph.printGraph();
        //Tests reading a graph with many nodes
        graph = Graph.read("C:\\Users\\Aibra\\Alcove\\Mound\\P6\\test resources\\ManyNodes.txt");
        graph.printGraph();
        //Tests reading a graph with nodes and neighbors
        graph = Graph.read("C:\\Users\\Aibra\\Alcove\\Mound\\P6\\test resources\\NodesNeighbors.txt");
        graph.printGraph();
    }

    @Test
    public void testDFS()
    {
        Graph graph = new Graph();

        //Tests DFS on a graph with no nodes
        assertArrayEquals(new String[]{}, graph.DFS("a", "b", "alphabetical"));
        //Tests DFS on a graph with one node
        graph.addNode("a");
        assertArrayEquals(new String[]{}, graph.DFS("a", "b", "alphabetical"));
        assertArrayEquals(new String[]{"a"}, graph.DFS("a", "a", "alphabetical"));
        assertArrayEquals(new String[]{"a"}, graph.DFS("a", "a", "reverse"));

        //Tests DFS on a graph with many nodes
        graph.addNodes(new String[]{"b", "c"});
        graph.addEdge("a", "b");
        graph.addEdge("b", "c");

        assertArrayEquals(new String[]{}, graph.DFS("a", "e", "alphabetical"));

        assertArrayEquals(new String[]{"a", "b"}, graph.DFS("a", "b", "alphabetical"));
        assertArrayEquals(new String[]{"b", "a"}, graph.DFS("b", "a", "alphabetical"));
        assertArrayEquals(new String[]{"a", "b"}, graph.DFS("a", "b", "reverse"));
        assertArrayEquals(new String[]{"b", "a"}, graph.DFS("b", "a", "reverse"));

        assertArrayEquals(new String[]{"a", "b", "c"}, graph.DFS("a", "c", "alphabetical"));
        assertArrayEquals(new String[]{"a", "b", "c"}, graph.DFS("a", "c", "reverse"));
        assertArrayEquals(new String[]{"c", "b", "a"}, graph.DFS("c", "a", "alphabetical"));
        assertArrayEquals(new String[]{"c", "b", "a"}, graph.DFS("c", "a", "reverse"));

        graph.addNodes(new String[]{"d", "e"});
        assertArrayEquals(new String[]{}, graph.DFS("a", "e", "alphabetical"));
        graph.addEdge("c", "d");
        graph.addEdge("d", "e");
        graph.addEdge("e", "a");
        graph.addEdge("b", "e");
        graph.addEdge("b", "d");
        //Pentagon with a tent inside
        assertArrayEquals(new String[]{"a", "b", "c", "d", "e"}, graph.DFS("a", "e", "alphabetical"));
        assertArrayEquals(new String[]{"a", "e"}, graph.DFS("a", "e", "reverse"));
        assertArrayEquals(new String[]{"e", "a", "b", "c"}, graph.DFS("e", "c", "alphabetical"));
        assertArrayEquals(new String[]{"e", "d", "c"}, graph.DFS("e", "c", "reverse"));
    }

    @Test
    public void testBFS()
    {
        Graph graph = new Graph();

        //Tests BFS on a graph with no nodes
        assertArrayEquals(new String[]{}, graph.BFS("a", "b", "alphabetical"));
        //Tests BFS on a graph with one node
        graph.addNode("a");
        assertArrayEquals(new String[]{}, graph.BFS("a", "b", "alphabetical"));
        assertArrayEquals(new String[]{"a"}, graph.BFS("a", "a", "alphabetical"));
        assertArrayEquals(new String[]{"a"}, graph.BFS("a", "a", "reverse"));

        //Tests BFS on a graph with many nodes
        graph.addNodes(new String[]{"b", "c"});
        graph.addEdge("a", "b");
        graph.addEdge("b", "c");

        assertArrayEquals(new String[]{}, graph.BFS("a", "e", "alphabetical"));

        assertArrayEquals(new String[]{"a", "b"}, graph.BFS("a", "b", "alphabetical"));
        assertArrayEquals(new String[]{"b", "a"}, graph.BFS("b", "a", "alphabetical"));
        assertArrayEquals(new String[]{"a", "b"}, graph.BFS("a", "b", "reverse"));
        assertArrayEquals(new String[]{"b", "a"}, graph.BFS("b", "a", "reverse"));

        assertArrayEquals(new String[]{"a", "b", "c"}, graph.BFS("a", "c", "alphabetical"));
        assertArrayEquals(new String[]{"a", "b", "c"}, graph.BFS("a", "c", "reverse"));
        assertArrayEquals(new String[]{"c", "b", "a"}, graph.BFS("c", "a", "alphabetical"));
        assertArrayEquals(new String[]{"c", "b", "a"}, graph.BFS("c", "a", "reverse"));

        graph.addNodes(new String[]{"d", "e", "f", "x", "y"});
        assertArrayEquals(new String[]{}, graph.BFS("a", "e", "alphabetical"));
        graph.addEdge("c", "d");
        graph.addEdge("d", "e");
        graph.addEdge("e", "f");
        graph.addEdge("f", "a");

        graph.addEdge("x", "y");
        graph.addEdges("x", new String[]{"a","b","c"});
        graph.addEdges("y", new String[]{"d","e","f"});

        //Hexagon with a line inside
        assertArrayEquals(new String[]{"b", "a", "f"}, graph.BFS("b", "f", "alphabetical"));
        assertArrayEquals(new String[]{"b", "a", "f"}, graph.BFS("b", "f", "reverse"));

        assertArrayEquals(new String[]{"b", "c", "d"}, graph.BFS("b", "d", "alphabetical"));
        assertArrayEquals(new String[]{"b", "c", "d"}, graph.BFS("b", "d", "reverse"));

        assertArrayEquals(new String[]{"b", "a", "f", "e"}, graph.BFS("b", "e", "alphabetical"));
        assertArrayEquals(new String[]{"b", "x", "y", "e"}, graph.BFS("b", "e", "reverse"));

        assertArrayEquals(new String[]{"b", "a", "f", "e"}, graph.BFS("b", "e", "alphabetical"));
        assertArrayEquals(new String[]{"b", "x", "y", "e"}, graph.BFS("b", "e", "reverse"));

        assertArrayEquals(new String[]{"b", "a", "f", "e"}, graph.BFS("b", "e", "alphabetical"));
        assertArrayEquals(new String[]{"b", "x", "y", "e"}, graph.BFS("b", "e", "reverse"));
    }

    @Test
    public void secondShortestPath()
    {
        Graph graph = new Graph();

        //Tests a graph with no nodes
        assertArrayEquals(new String[]{}, graph.secondShortestPath("a", "b"));

        //Tests a graph with one node
        graph.addNode("a");
        assertArrayEquals(new String[]{}, graph.secondShortestPath("a", "b"));
        assertArrayEquals(new String[]{}, graph.secondShortestPath("a", "a"));

        //Tests a graph with two nodes
        graph.addNode("b");
        graph.addEdge("a", "b");
        assertArrayEquals(new String[]{}, graph.secondShortestPath("a", "b"));
        assertArrayEquals(new String[]{}, graph.secondShortestPath("a", "a"));

        //Tests a graph with many nodes
        graph.addNode("c");
        graph.addEdge("b", "c");
        assertArrayEquals(new String[]{}, graph.secondShortestPath("a", "b"));
        assertArrayEquals(new String[]{}, graph.secondShortestPath("b", "a"));

        assertArrayEquals(new String[]{"a", "b", "a", "b", "c"}, graph.secondShortestPath("a", "c"));
        assertArrayEquals(new String[]{"c", "b", "c", "b", "a"}, graph.secondShortestPath("c", "a"));

        //Tests a pentagon
        graph.addNodes(new String[]{"d", "e"});
        graph.addEdge("c", "d");
        graph.addEdge("d", "e");
        graph.addEdge("e", "a");

        assertArrayEquals(new String[]{"a", "b", "c", "d", "e"}, graph.secondShortestPath("a", "e"));
        assertArrayEquals(new String[]{"a", "e", "d", "c"}, graph.secondShortestPath("a", "c"));

        //Tests a hexagon
        graph.addNode("f");
        graph.removeEdge("e", "a");
        graph.addEdge("e", "f");
        graph.addEdge("f", "a");

        assertArrayEquals(new String[]{"a", "b", "c", "d", "e", "f"}, graph.secondShortestPath("a", "f"));
        assertArrayEquals(new String[]{"a", "b", "a", "b", "c"}, graph.secondShortestPath("a", "c"));
        assertArrayEquals(new String[]{"a", "b", "a", "b", "c", "d"}, graph.secondShortestPath("a", "d"));

        //Tests a hexagon with a node in the center
        graph.addNode("x");
        graph.addEdge("x", "b");
        graph.addEdge("x", "f");
        assertArrayEquals(new String[]{"a", "b", "x", "f"}, graph.secondShortestPath("a", "f"));
        assertArrayEquals(new String[]{"a", "b", "a", "b", "c"}, graph.secondShortestPath("a", "c"));
        assertArrayEquals(new String[]{"a", "b", "a", "b", "c", "d"}, graph.secondShortestPath("a", "d"));

    }
}

