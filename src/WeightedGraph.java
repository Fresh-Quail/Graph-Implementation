import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WeightedGraph {

    //Represents all of the nodes in the graph
    private ArrayList<String> names = new ArrayList<>();
    //Represents the the neighbors of each node in the graph
    private ArrayList<ArrayList<Integer>> neighborMap = new ArrayList<>();

    /**
     * Adds a node to the graph, checking for duplicates
     */
    public boolean addNode(String name)
    {
        //Iterates through the graph checking if the node already exists in the graph
        //And locates the name that lexicographically proceeds the name
        int i = 0;
        while(i < names.size() && names.get(i).compareTo(name) <= 0)
        {
            //If the name is duplicated, does not add to the list
            if(names.get(i).equals(name))
                return false;
            i++;
        }

        //If not duplicated, add the node to the list of names
        names.add(i, name);
        //Resizes the matrix to reflect the new length of the list of names and creates the new column
        ArrayList<Integer> column = new ArrayList<>();
        for (int j = 0; j < neighborMap.size(); j++)
        {
            //Inserts the new row in the matrix
            neighborMap.get(j).add(i, null);
            //Builds the new column
            column.add(null);
        }
        column.add(null);
        //If it is the first name, initializes the neighborMap
        if(neighborMap.size() == 0){
            neighborMap.add(column);
        }
        else //Inserts the new column
            neighborMap.add(i, column);
        return true;
    }

    /**
     * Adds a list of nodes to the graph, checking for duplicates
     */
    public boolean addNodes(String[] names)
    {
        //Represents if all of the operations were successful
        boolean successful = true;

        //Adds each node in the list to the array of strings, refactoring the matrix for each node
        for (int i = 0; i < names.length; i++)
            successful = addNode(names[i]);

        return successful;
    }

    /**
     * Removes a node from the graph along with all connected edges.
     */
    public boolean removeNode(String name)
    {
        //Iterates through the graph checking if the node exists in the graph
        int i = 0;
        while(i < names.size() && !name.equals(names.get(i)))
        {
            i++;
        }
        //If the node does not exist, the operation was not successful
        if(i == names.size())
            return false;

        //Resizes the matrix to reflect the new length of the list of names
        //Removes a column
        neighborMap.remove(i);
        for (int j = 0; j < neighborMap.size(); j++)
        {
            //Removes a row in the matrix
            neighborMap.get(j).remove(i);
        }
        //Removes the given node from the list of names
        names.remove(i);

        return true;
    }

    /**
     * Removes each node in nodelist and their edges from the graph
     */
    public boolean removeNodes(String[] nodelist)
    {
        //Represents if all of the operations were successful
        boolean successful = true;
        for (int i = 0; i < nodelist.length; i++)
            successful = removeNode(nodelist[i]);
        return successful;
    }

    /**
     * Adds a weighted directed edge from 'from' to 'to'
     */
    public boolean addWeightedEdge(String from, String to, int weight)
    {
        //These represent 'from' and 'to' positions in the 'nodes' array
        int fromPos = -1;
        int toPos = -1;

        //Looks for the positions of 'from' and 'to' in the array of names
        for (int i = 0; i < names.size(); i++)
        {
            if(from.equals(names.get(i)))
                fromPos = i;
            if(to.equals(names.get(i)))
                toPos = i;
        }

        //Only adds the edge if both 'to' and 'from' exist in the list
        //And they are not the same node
        if(fromPos != -1 && toPos != -1 && fromPos != toPos)
        {
            neighborMap.get(fromPos).set(toPos, weight);
            return true;
        }
        //Returns false if the edge already existed, directed to the same node, or if they nodes did not exist
        return false;
    }

    /**
     * Adds an edge from 'from' to each node in the given list
     */
    public boolean addWeightedEdges(String from, String[] tolist, int[] weightlist)
    {
        //These represent 'from' and 'to' positions in the 'nodes' array
        int fromPos = -1;
        int toPos = -1;
        //Represents if all of the operations were successful
        boolean successful = true;

        //For every element in toList
        for (int j = 0; j < tolist.length; j++)
        {
            //Look for its position in the array of names
            //And add an undirected edge between 'from' and 'to' if they both exist
            for (int i = 0; i < names.size(); i++)
            {
                //Only assigns fromPos once
                if (fromPos == -1 && from.equals(names.get(i)))
                    fromPos = i;
                if (tolist[j].equals(names.get(i)))
                    toPos = i;
            }

            //Only adds the edges if both 'to' and 'from' exist in the list
            //And they are not the same node
            if (fromPos != -1 && toPos != -1 && fromPos != toPos && j < weightlist.length)
                neighborMap.get(fromPos).set(toPos, weightlist[j]);
            else
                successful = false;
        }

        return successful;
    }

    /**
     * Creates a graph from the given file
     */
    public static WeightedGraph readWeighted(String filename) throws FileNotFoundException {
        //Represents the graph to be returned
        WeightedGraph graph = new WeightedGraph();
        //Parses the given file for processing
        Scanner sc = new Scanner(new File(filename));
        //Represents each line of nodes
        String[] nodes;

        //Parses each line of the given text file into the graph
        while(sc.hasNextLine())
        {
            //Splits the line into nodes
            nodes = sc.nextLine().split(" ");
            //Adds the nodes and their edges
            for (int i = 0; i < nodes.length; i = i + 2)
            {
                //All nodes are at even indices
                graph.addNode(nodes[i]);
                //All edges are at odd indices
                if(i != 0)
                    graph.addWeightedEdge(nodes[0], nodes[i], Integer.parseInt(nodes[i - 1]));
            }
        }

        return graph;
    }

    /**
     * Prints the object's graph
     */
    public void printWeightedGraph()
    {
        //Traverses through the list of names
        for (int i = 0; i < names.size(); i++)
        {
            System.out.print(names.get(i));
            //For each name in the graph's list, print out its neighbors
            for (int j = 0; j < names.size(); j++)
            {
                //If the edge exists for the nodes 'i' and 'j'
                //Print the neighbor
                if(neighborMap.get(i).get(j) != null)
                    System.out.print(" " + neighborMap.get(i).get(j) +  " " + names.get(j));
            }
            //Makes a new line for the nex node
            System.out.println();
        }
    }

    /**
     * uses Dijkstra’s algorithm to find the shortest path from node from to node to
     */
    public String[] shortestPath(String from, String to) {
        //Represents the parent of each node in the graph in its shortest path
        //Each integer corresponds to a node in the 'names' array field
        Integer[] parents = new Integer[names.size()];
        //Represents nodes that have been finalized
        boolean[] finalized = new boolean[names.size()];
        //Represents the shortest paths from the starting node to other nodes
        Integer[] shortestPathTo = new Integer[names.size()];
        // 'neighbor' corresponds to node positions in the 'names' array field
        class Paths {
            int node;
            int cost;

            Paths(int neighbor, int cost) {
                this.node = neighbor;
                this.cost = cost;
            }
        }
        //Represents a min-at-top heap with the next closest node in Dijkstra's at the top
        PriorityQueue<Paths> nextClosest = new PriorityQueue<>((o1, o2) -> o1.cost - o2.cost);

        //These represent 'from' and 'to' positions in the 'nodes' array
        int fromPos = -1;
        int toPos = -1;
        //Looks for 'from's and 'to's position in the array of names
        for (int i = 0; i < names.size(); i++)
        {
            //Only assigns fromPos once
            if (fromPos == -1 && from.equals(names.get(i)))
                fromPos = i;
            if (to.equals(names.get(i)))
                toPos = i;
        }
        //If either the from position or to position are not found, do not perform
        if (fromPos == -1 || toPos == -1)
            return new String[]{};

        //Represents the current visited node as an index of the 'names' field
        int currPos = fromPos;
        shortestPathTo[currPos] = 0;
        //Initializes the PQ with the first node
        nextClosest.add(new Paths(currPos, 0));

        //Does Dijkstra's for/finalizes for all connected nodes in the graph
        while (!nextClosest.isEmpty())
        {
            //Visits the next closest known node to the start position
            currPos = nextClosest.remove().node;
            //Finalizes the current node
            finalized[currPos] = true;

            //Iterates through the neighborlist for the current visited node
            for (int neighbor = 0; neighbor < names.size(); neighbor++)
            {
                //Finds the visited node's unfinalized neighbors
                if (neighborMap.get(currPos).get(neighbor) != null && !finalized[neighbor])
                {
                    //If the path from this node to the neighbor is shorter than the neighbors shortest path
                    //Change the parent and cost of the neighbors path
                    if (shortestPathTo[neighbor] == null || shortestPathTo[neighbor] > shortestPathTo[currPos] + neighborMap.get(currPos).get(neighbor))
                    {
                        parents[neighbor] = currPos;
                        //New shortest path is the path to the current node plus the cost to the neighbor
                        shortestPathTo[neighbor] = shortestPathTo[currPos] + neighborMap.get(currPos).get(neighbor);
                        //The updated path to the priority queue in order to calculate the next shortest path
                        nextClosest.add(new Paths(neighbor, shortestPathTo[neighbor]));
                    }
                }
            }
        }

        //Backtracks using the 'parents' array field to find the shortest path to 'to' if there is a path
        //Adds the ending point, which has no child in the path
        LinkedList<String> path = new LinkedList<>();
        path.addFirst(names.get(toPos));
        while (parents[toPos] != null)
        {
            path.addFirst(names.get(parents[toPos]));
            toPos = parents[toPos];
        }
        if(path.get(0).equals(names.get(fromPos)))
            return path.toArray(new String[]{});
        else
            return new String[]{};
    }

    /**
     * Removes an undirected edge from the graph
     * @return Returns null if there was no edge between the two nodes
     */
    public Integer removeEdge(String from, String to)
    {
        //These represent 'from', and 'to' positions in the 'nodes' array
        int fromPos = -1;
        int toPos = -1;

        //Looks for the positions of 'from' and 'to' in the array of names
        for (int i = 0; i < names.size(); i++)
        {
            //Finds positions of 'from' and 'to' in the array of names if it exists
            if (fromPos == -1 && from.equals(names.get(i)))
                fromPos = i;
            if (toPos == -1 && to.equals(names.get(i)))
                toPos = i;
        }
        if(fromPos != -1 && toPos != -1)
            return neighborMap.get(fromPos).set(toPos, null);
        return null;
    }

    /**
     * Findest the secondShortestPath in a graph
     */
    public String[] secondShortestPath(String from, String to)
    {
        String[] shortestPath = shortestPath(from, to);

        //If there is no path, there is no second shortest path
        if(shortestPath.length <= 1)
            return new String[]{};

        //Represents the length of the shortest path
        int shortestPathLength = 0;
        for (int i = 0; i < shortestPath.length - 1; i++)
        {
            shortestPathLength = shortestPathLength + neighborMap.get(names.indexOf(shortestPath[i])).get(names.indexOf(shortestPath[i + 1]));
        }

        //Represents the current second shortest path
        String[] secondShortest = new String[]{};
        int secondShortestLength = 0;
        //Represents the next possible shortest path
        String[] possiblyShortest;
        int possiblyShortestLength = 0;
        //Represents the edge that was removed
        int edge;

        //For each edge in the shortest path, finds the shortest path that does not include that edge
        for (int i = 0; i < shortestPath.length - 1; i++)
        {
            edge = removeEdge(shortestPath[i], shortestPath[i+1]);
            //Assigns values of the possibly shortest path to the path string and path length
            possiblyShortest = shortestPath(from, to);
            possiblyShortestLength = 0;
            for (int j = 0; j < possiblyShortest.length - 1; j++)
            {
                    possiblyShortestLength = possiblyShortestLength + neighborMap.get(names.indexOf(possiblyShortest[i])).get(names.indexOf(possiblyShortest[i + 1]));
            }
            //Assigns initial value to second shortest if it doesn't yet exist
            //Compares one short path to another short path, both longer than the shortest path, assigning the shorter path to secondShortest
            if(secondShortestLength == 0 && possiblyShortestLength > shortestPathLength || (secondShortestLength != 0 && possiblyShortestLength < secondShortestLength && possiblyShortestLength > shortestPathLength))
            {
                secondShortestLength = possiblyShortestLength;
                secondShortest = possiblyShortest;
            }
            addWeightedEdge(shortestPath[i], shortestPath[i+1], edge);
        }

        //If there is a path longer than the shortest path, return it
        if(secondShortestLength != shortestPathLength)
            return secondShortest;
        return new String[]{};

    }

    public static void main(String[] args)
    {
        WeightedGraph graph = new WeightedGraph();

        //Hexagon with a line inside
        System.out.println("Node added: " + graph.addNode("a"));
        System.out.println("Nodes added: " + graph.addNodes(new String[]{"b", "c", "d", "e", "f", "x", "y"}));

        System.out.println("Edges added: " + graph.addWeightedEdges("a", new String[]{"b", "f"}, new int[]{1, 3}));
        System.out.println("Edges added: " + graph.addWeightedEdges("c", new String[]{"b", "d"}, new int[]{1, 5}));

        System.out.println("Edge added: " + graph.addWeightedEdge("d", "e", 1));
        System.out.println("Edge added: " + graph.addWeightedEdge("f", "e", 1));

        System.out.println("Edge added: " + graph.addWeightedEdge("x", "y", 1));
        System.out.println("Edge added: " + graph.addWeightedEdge("y", "x", 1));

        System.out.println("Edge added: " + graph.addWeightedEdge("b", "x", 3));
        System.out.println("Edge added: " + graph.addWeightedEdge("e", "y", 5));
        System.out.println("Edges added: " + graph.addWeightedEdges("x", new String[]{"a", "c"}, new int[]{1, 1}));
        System.out.println("Edges added: " + graph.addWeightedEdges("y", new String[]{"d", "f"}, new int[]{1, 1}));
        System.out.println("Nodes added: " + graph.addNodes(new String[]{"z", "bat", "cat"}));
        System.out.println("Node removed: " + graph.removeNode("z"));
        System.out.println("Nodes removed: " + graph.removeNodes(new String[]{"bat", "cat"}));
        //The graph
        graph.printWeightedGraph();

        System.out.println("Shortest path from 'b' to 'f': " + Arrays.toString(graph.shortestPath("b", "f")));
        System.out.println("Second Shortest path from 'b' to 'f': " + Arrays.toString(graph.secondShortestPath("b", "f")));

        System.out.println("Shortest path from 'b' to 'd': " + Arrays.toString(graph.shortestPath("b", "d")));
        System.out.println("Second Shortest path from 'b' to 'd': " + Arrays.toString(graph.secondShortestPath("b", "d")));

        System.out.println("Shortest path from 'c' to 'e': " + Arrays.toString(graph.shortestPath("c", "e")));
        System.out.println("Second Shortest path from 'c' to 'e': " + Arrays.toString(graph.secondShortestPath("c", "e")));


        //Real world example
        graph = new WeightedGraph();
        graph.addNodes(new String[]{"Gdynia", "Sopot", "Gdansk", "Bialystok", "Warsaw", "Lublin", "Krakow", "Lodz", "Czestochowa", "Katowice", "Wroclaw", "Poznan", "Gniezno"});
        graph.addWeightedEdges("Gdynia", new String[]{"Sopot", "Gdansk"}, new int[]{10, 36});
        graph.addWeightedEdges("Sopot", new String[]{"Gdynia", "Gdansk"}, new int[]{10, 13});
        graph.addWeightedEdges("Gdansk", new String[]{"Gdynia", "Sopot", "Warsaw", "Bialystok"}, new int[]{36, 13, 340, 422});
        graph.addWeightedEdges("Bialystok", new String[]{"Gdansk", "Warsaw"}, new int[]{422, 340});
        graph.addWeightedEdges("Warsaw", new String[]{"Gdansk", "Bialystok", "Lublin", "Krakow", "Lodz", "Poznan"}, new int[]{340, 198, 173, 290, 136, 311});
        graph.addWeightedEdges("Lublin", new String[]{"Warsaw"}, new int[]{173});
        graph.addWeightedEdges("Krakow", new String[]{"Warsaw"}, new int[]{390});
        graph.addWeightedEdges("Lodz", new String[]{"Warsaw", "Czestochowa", "Wroclaw"}, new int[]{136, 126, 217});
        graph.addWeightedEdges("Czestochowa", new String[]{"Lodz", "Katowice", "Wroclaw"}, new int[]{126, 73, 258});
        graph.addWeightedEdges("Katowice", new String[]{"Czestochowa"}, new int[]{73});
        graph.addWeightedEdges("Wroclaw", new String[]{"Lodz", "Czestochowa", "Poznan"}, new int[]{217, 258, 182});
        graph.addWeightedEdges("Poznan", new String[]{"Warsaw", "Wroclaw", "Gniezno"}, new int[]{311, 182, 63});
        graph.addWeightedEdges("Gniezno", new String[]{"Poznan"}, new int[]{63});

        System.out.println("The graph of Poland: ");
        graph.printWeightedGraph();

        System.out.println("Shortest path from 'Bialystok' to 'Gniezno': " + Arrays.toString(graph.shortestPath("Bialystok", "Gniezno")));
        System.out.println("Second Shortest path from 'Bialystok' to 'Gniezno': " + Arrays.toString(graph.secondShortestPath("Bialystok", "Gniezno")));

        System.out.println("Shortest path from 'Czestochowa' to 'Gdansk': " + Arrays.toString(graph.shortestPath("Czestochowa", "Gdansk")));
        System.out.println("Second Shortest path from 'Czestochowa to 'Gdansk': " + Arrays.toString(graph.secondShortestPath("Czestochowa", "Gdansk")));

        System.out.println("Shortest path from 'Czestochowa' to 'Lublin " + Arrays.toString(graph.shortestPath("Czestochowa", "Lublin")));
        System.out.println("Second Shortest path from 'Czestochowa' to 'Lublin': " + Arrays.toString(graph.secondShortestPath("Czestochowa", "Lublin")));

        System.out.println("Shortest path from 'Gdynia' to 'Gdansk " + Arrays.toString(graph.shortestPath("Czestochowa", "Lublin")));
        System.out.println("Second Shortest path from 'Gdynia' to 'Gdansk': " + Arrays.toString(graph.secondShortestPath("Czestochowa", "Lublin")));
    }
}
