import org.junit.*;
import java.io.FileNotFoundException;

public class EmilsTests {
    WeightedGraph g1 = new WeightedGraph();
    WeightedGraph g2 = WeightedGraph.readWeighted("C:\\Users\\Aibra\\Desktop\\test3.txt");
    WeightedGraph g3 = WeightedGraph.readWeighted("C:\\Users\\Aibra\\Desktop\\test4.txt");

    public EmilsTests() throws FileNotFoundException {
    }

    @Test
    public void testShortestPath() {
        g1.addNode("Node");
        Assert.assertArrayEquals(new String[]{"Node"}, g1.shortestPath("Node", "Node"));
        Assert.assertArrayEquals(new String[]{"A"}, g2.shortestPath("A", "A"));
        Assert.assertArrayEquals(new String[]{}, g2.shortestPath("F", "G"));
        Assert.assertArrayEquals(new String[]{"A", "D"}, g2.shortestPath("A", "D"));
        Assert.assertArrayEquals(new String[]{"D", "C", "A", "B"}, g2.shortestPath("D", "B"));
        Assert.assertArrayEquals(new String[]{"D", "G", "F"}, g2.shortestPath("D", "F"));
        Assert.assertArrayEquals(new String[]{"C", "A", "D", "E"}, g2.shortestPath("C", "E"));
        Assert.assertArrayEquals(new String[]{"Warsaw", "Łódź"}, g3.shortestPath("Warsaw", "Łódź"));
        Assert.assertArrayEquals(new String[]{"Łódź", "Wrocław", "Poznań", "Warsaw"}, g3.shortestPath("Łódź", "Warsaw"));
        Assert.assertArrayEquals(new String[]{"Łódź", "Wrocław", "Poznań", "Warsaw"}, g3.shortestPath("Łódź", "Warsaw"));
        Assert.assertArrayEquals(new String[]{"Wrocław", "Poznań", "Warsaw", "Gdańsk"}, g3.shortestPath("Wrocław", "Gdańsk"));
    }


    @Test
    public void testDFS() throws FileNotFoundException {
        Graph g1 = new Graph();
        Graph g2 = Graph.read("C:\\Users\\Aibra\\Desktop\\test1.txt");
        Graph g3 = Graph.read("C:\\Users\\Aibra\\Desktop\\test2.txt");
        g1.addNode("Node");
        Assert.assertArrayEquals(new String[]{"Node"}, g1.DFS("Node", "Node", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"A"}, g2.DFS("A", "A", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"A"}, g2.DFS("A", "A", "reverse"));
        Assert.assertArrayEquals(new String[]{"D", "A", "B"}, g2.DFS("D", "B", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"D", "A", "C", "B"}, g2.DFS("D", "B", "reverse"));
        Assert.assertArrayEquals(new String[]{"A", "C"}, g2.DFS("A", "C", "reverse"));
        Assert.assertArrayEquals(new String[]{"A", "B", "C"}, g2.DFS("A", "C", "alphabetical"));
        Assert.assertArrayEquals(new String[]{}, g3.DFS("XXX", "Wyoming", "alphabetical"));
        Assert.assertArrayEquals(new String[]{}, g3.DFS("XXX", "Wyoming", "reverse"));
        Assert.assertArrayEquals(new String[]{}, g3.DFS("Wyoming", "XXX", "alphabetical"));
        Assert.assertArrayEquals(new String[]{}, g3.DFS("Wyoming", "XXX", "reverse"));
        Assert.assertArrayEquals(new String[]{"Idaho", "Montana", "Wyoming"}, g3.DFS("Idaho", "Wyoming", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"Idaho", "Wyoming"}, g3.DFS("Idaho", "Wyoming", "reverse"));
        Assert.assertArrayEquals(new String[]{"Washington", "Idaho", "Montana", "Wyoming", "Colorado", "New-Mexico"}, g3.DFS("Washington", "New-Mexico", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"Washington", "Oregon", "Nevada", "Utah", "Wyoming", "Colorado", "New-Mexico"}, g3.DFS("Washington", "New-Mexico", "reverse"));
        Assert.assertArrayEquals(new String[]{"California", "Arizona", "Nevada", "Idaho", "Montana", "Wyoming", "Colorado"}, g3.DFS("California", "Colorado", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"California", "Oregon", "Washington", "Idaho", "Wyoming", "Utah", "Nevada", "Arizona", "New-Mexico", "Colorado"}, g3.DFS("California", "Colorado", "reverse"));
        Assert.assertArrayEquals(new String[]{"Arizona", "California"}, g3.DFS("Arizona", "California", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"Arizona", "Utah", "Wyoming", "Montana", "Idaho", "Washington", "Oregon", "Nevada", "California"}, g3.DFS("Arizona", "California", "reverse"));
        Assert.assertArrayEquals(new String[]{"Colorado", "New-Mexico", "Arizona"}, g3.DFS("Colorado", "Arizona", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"Colorado", "Wyoming", "Utah", "Nevada", "Oregon", "California", "Arizona"}, g3.DFS("Colorado", "Arizona", "reverse"));
        Assert.assertArrayEquals(new String[]{}, g3.DFS("Arizona", "Hawaii", "reverse"));
    }

    @Test
    public void testBFS() throws FileNotFoundException {
        Graph g1 = new Graph();
        Graph g2 = Graph.read("C:\\Users\\Aibra\\Desktop\\test1.txt");
        Graph g3 = Graph.read("C:\\Users\\Aibra\\Desktop\\test2.txt");
        g1.addNode("Node");
        Assert.assertArrayEquals(new String[]{"Node"}, g1.BFS("Node", "Node", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"A"}, g2.BFS("A", "A", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"A"}, g2.BFS("A", "A", "reverse"));
        Assert.assertArrayEquals(new String[]{"D", "A", "B"}, g2.BFS("D", "B", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"D", "A", "B"}, g2.BFS("D", "B", "reverse"));
        Assert.assertArrayEquals(new String[]{"A", "C"}, g2.BFS("A", "C", "reverse"));
        Assert.assertArrayEquals(new String[]{"A", "C"}, g2.BFS("A", "C", "alphabetical"));
        Assert.assertArrayEquals(new String[]{}, g3.BFS("XXX", "Wyoming", "alphabetical"));
        Assert.assertArrayEquals(new String[]{}, g3.BFS("XXX", "Wyoming", "reverse"));
        Assert.assertArrayEquals(new String[]{}, g3.BFS("Wyoming", "XXX", "alphabetical"));
        Assert.assertArrayEquals(new String[]{}, g3.BFS("Wyoming", "XXX", "reverse"));
        Assert.assertArrayEquals(new String[]{"Idaho", "Wyoming"}, g3.BFS("Idaho", "Wyoming", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"Idaho", "Wyoming"}, g3.BFS("Idaho", "Wyoming", "reverse"));
        Assert.assertArrayEquals(new String[]{"Washington", "Idaho", "Nevada", "Arizona", "New-Mexico"}, g3.BFS("Washington", "New-Mexico", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"Washington", "Oregon", "Nevada", "Arizona", "New-Mexico"}, g3.BFS("Washington", "New-Mexico", "reverse"));
        Assert.assertArrayEquals(new String[]{"California", "Arizona", "New-Mexico", "Colorado"}, g3.BFS("California", "Colorado", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"California", "Nevada", "Utah", "Colorado"}, g3.BFS("California", "Colorado", "reverse"));
        Assert.assertArrayEquals(new String[]{"Nevada", "Idaho", "Washington"}, g3.BFS("Nevada", "Washington", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"Nevada", "Oregon", "Washington"}, g3.BFS("Nevada", "Washington", "reverse"));
        Assert.assertArrayEquals(new String[]{"Colorado", "New-Mexico", "Arizona", "California"}, g3.BFS("Colorado", "California", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"Colorado", "Utah", "Nevada", "California"}, g3.BFS("Colorado", "California", "reverse"));
        Assert.assertArrayEquals(new String[]{}, g3.BFS("Arizona", "Hawaii", "reverse"));
    }
}
