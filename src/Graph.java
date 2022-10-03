import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class Graph
{
    //Represents all of the nodes in the graph
    private ArrayList<String> names = new ArrayList<>();
    //Represents the the neighbors of each node in the graph
    private ArrayList<ArrayList<Boolean>> neighborMap = new ArrayList<>();

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
        ArrayList<Boolean> column = new ArrayList<>();
        for (int j = 0; j < neighborMap.size(); j++)
        {
            //Inserts the new row in the matrix
            neighborMap.get(j).add(i, false);
            //Builds the new column
            column.add(false);
        }
        column.add(false);
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
     * Adds a directed edge from node 'from' to node 'to'
     */
    public boolean addEdge(String from, String to)
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
            neighborMap.get(fromPos).set(toPos, true);
            neighborMap.get(toPos).set(fromPos, true);
            return true;
        }
        //Returns false if the edge already existed, directed to the same node, or if they nodes did not exist
            return false;
    }

    /**
     * Adds an undirected edge from node 'from' to each node in 'tolist'
     */
    public boolean addEdges(String from, String[] tolist)
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
            if (fromPos != -1 && toPos != -1 && fromPos != toPos)
            {
                neighborMap.get(fromPos).set(toPos, true);
                neighborMap.get(toPos).set(fromPos, true);
            } else
                successful = false;
        }

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
     * Constructs a graph from the given text file
     */
    public static Graph read(String filename) throws FileNotFoundException {
        //Represents the graph to be returned
        Graph graph = new Graph();
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
            graph.addNodes(nodes);
            graph.addEdges(nodes[0], nodes);
        }

        return graph;
    }

    /**
     *  Prints the object's graph
     */
    public void printGraph()
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
                if(neighborMap.get(i).get(j))
                    System.out.print(" " + names.get(j));
            }
            //Makes a new line for the nex node
            System.out.println();
        }
    }

    /**
     *
     */
    public String[] DFS(String from, String to, String neighborOrder)
    {
        //Represents the nodes that have already been encountered
        boolean[] encountered = new boolean[names.size()];
        //Represents the current path from 'from' to 'to'
        LinkedList<String> path = new LinkedList<>();
        //These represent 'from' and 'to' positions in the 'nodes' array
        int fromPos = -1;
        int toPos = -1;

        //Looks for the positions of 'from' and 'to' in the array of names
        for (int i = 0; i < names.size(); i++)
        {
            //Only assigns fromPos once
            if (fromPos == -1 && from.equals(names.get(i)))
                fromPos = i;
            if (toPos == -1 && to.equals(names.get(i)))
                toPos = i;
        }
        //Only recurses if the indices were found for both 'from' and 'to'
        if(fromPos != -1 || toPos != -1)
        {
            //Initializes recursive call for dfs
            if (neighborOrder.equals("alphabetical"))
                dfsAlpha(fromPos, toPos, encountered, path);
            else if (neighborOrder.equals("reverse"))
                dfsReverse(fromPos, toPos, encountered, path);
        }

        //Returns the path to the destination
        return path.toArray(new String[]{});
    }

    /**
     * Recursive call to DFS - Alphabetical
     */
    public boolean dfsAlpha(int curPos, int dest, boolean[] encountered, LinkedList<String> path)
    {
        //If the current node is at the destination, return true and add it to the path
        if(curPos == dest)
        {
            path.addFirst(names.get(curPos));
            return true;
        }
        //Goes through the matrix looking for nodes neighboring the current node
        for (int i = 0; i < names.size(); i++)
        {
            //If the node at 'i' is a neighbor to 'curPos', visit the node unless it was encountered before
            if(neighborMap.get(curPos).get(i) && !encountered[i])
            {
                encountered[curPos] = true;
                //Runs dfs on the neighbor
                //If the destination has been encountered, add this node to the path
                if(dfsAlpha(i, dest, encountered, path))
                {
                    //Adds node to path and indicates the previous node is part of the path
                    path.addFirst(names.get(curPos));
                    return true;
                }
            }
        }
        //There are no more neighbors for the current node
        return false;
    }

    /**
     * Recursive call to DFS - Reverse
     */
    public boolean dfsReverse(int curPos, int dest, boolean[] encountered, LinkedList<String> path)
    {
        //If the current node is at the destination, return true and add it to the path
        if(curPos == dest)
        {
            path.addFirst(names.get(curPos));
            return true;
        }
        //Goes through the matrix looking for nodes neighboring the current node
        for (int i = names.size() - 1; i >= 0; i--)
        {
            //If the node at 'i' is a neighbor to 'curPos', visit the node unless it was encountered before
            if(neighborMap.get(curPos).get(i) && !encountered[i])
            {
                encountered[curPos] = true;
                //Runs dfs on the neighbor
                //If the destination has been encountered, add this node to the path
                if(dfsReverse(i, dest, encountered, path))
                {
                    //Adds node to path and indicates the previous node is part of the path
                    path.addFirst(names.get(curPos));
                    return true;
                }
            }
        }
        //There are no more neighbors for the current node
        return false;
    }

    /**
     *
     */
    public String[] BFS(String from, String to, String neighborOrder)
    {
        //Represents the queue that enables breadth first traversal
        //Nodes are grouped based on how many edges they are away from the starting point
        LinkedList<Integer> queue = new LinkedList<>();

        //Represents the parents of nodes in the traversal - nodes will only have up to one parent
        //Positions in the array correspond to the array of 'names'
        Integer[] parents = new Integer[names.size()];

        //These represent 'from', and 'to' positions in the 'nodes' array
        int fromPos = -1;
        int dest = -1;

        //Looks for the positions of 'from' and 'to' in the array of names
        for (int i = 0; i < names.size(); i++)
        {
            //Finds positions of 'from' and 'to' in the array of names if it exists
            if (fromPos == -1 && from.equals(names.get(i)))
                fromPos = i;
            if (dest == -1 && to.equals(names.get(i)))
                dest = i;
        }
        //Returns an empty array if either 'from' or 'to' do not exist
        if(fromPos == -1 || dest == -1)
            return new String[]{};

        //Traverses the graph in a breadth first manner until the destination has been reached or the graph has been traversed
        //Starting from 'from'
        int currPos = fromPos;
        queue.add(currPos);
        while(!queue.isEmpty() && currPos != dest)
        {
            //Visits the next node in the breadth traversal
            currPos = queue.removeFirst();
            if(currPos != dest)
            {
                //Determines what order the traversal is in
                if(neighborOrder.equals("alphabetical"))
                {
                    //Adds all the node's neighbors to the queue for the next breadth 1 extra edge away
                    for (int neighbors = 0; neighbors < names.size(); neighbors++)
                    {
                        if (neighborMap.get(currPos).get(neighbors) && parents[neighbors] == null && neighbors != fromPos)
                        {
                            queue.add(neighbors);
                            //Labels this node, 'currPos' as the parent if its neighbors, 'i'
                            parents[neighbors] = currPos;
                        }
                    }
                }
                //Does BFS is reverse alphabetical order
                else if(neighborOrder.equals("reverse"))
                {
                    //Adds all the node's neighbors to the queue for the next breadth 1 extra edge away if they have not been already visited
                    for (int neighbors = names.size() - 1; neighbors >= 0; neighbors--)
                    {
                        if (neighborMap.get(currPos).get(neighbors) && parents[neighbors] == null && neighbors != fromPos)
                        {
                            queue.add(neighbors);
                            //Labels this node, 'currPos' as the parent if its neighbors, 'i'
                            parents[neighbors] = currPos;
                        }
                    }
                }
            }
        }

        //Assembles the path created from the traversal - only if a path was found
        LinkedList<String> path = new LinkedList<>();
        if(currPos == dest)
        {
            path.addFirst(names.get(currPos));
            while (parents[currPos] != null)
            {
                currPos = parents[currPos];
                path.addFirst(names.get(currPos));
            }
        }
        return path.toArray(new String[]{});
    }

    /**
     * Removes an undirected edge from the graph
     */
    public void removeEdge(String from, String to)
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
        {
            neighborMap.get(fromPos).set(toPos, false);
            neighborMap.get(toPos).set(fromPos, false);
        }
    }

    /**
     * Findest the secondShortestPath in a graph
     */
    public String[] secondShortestPath(String from, String to)
    {
        String[] shortestPath = BFS(from, to, "alphabetical");

        //If there is no path, there is no second shortest path
        if(shortestPath.length <= 1)
            return new String[]{};

        //Represents the current second shortest path
        String[] secondShortest = null;
        //Represents the next possible shortest path
        String[] nextShortest;
        //For each edge in the shortest path, finds the shortest path that does not include that edge
        for (int i = 0; i < shortestPath.length - 1; i++)
        {
            removeEdge(shortestPath[i], shortestPath[i+1]);
            nextShortest = BFS(from, to, "alphabetical");
            //Assigns initial value to second shortest
            //Compares one short path to another short path, both longer than the shortest path, assigning the shorter path to secondShortest
            if(secondShortest == null || nextShortest.length < secondShortest.length && nextShortest.length > shortestPath.length)
                secondShortest = nextShortest;
            addEdge(shortestPath[i], shortestPath[i+1]);
        }

        //If there is a path with just one more edge than the shortest path, return it
        if(secondShortest != null && (secondShortest.length == shortestPath.length + 1 || shortestPath.length == 2))
            return secondShortest;
        //Otherwise, the shortest path is simply doubling back once in a path
        else if(shortestPath.length >= 3)
        {
            //The path to be returned
            secondShortest = new String[shortestPath.length + 2];
            //Creates the new path with the double back
            secondShortest[0] = shortestPath[0];
            secondShortest[1] = shortestPath[1];
            for (int i = 2; i < secondShortest.length; i++)
            {
                secondShortest[i] = shortestPath[i-2];
            }
            return secondShortest;
        }
        return new String[]{};

    }

    public static void main(String[] args)
    {
        Graph graph = new Graph();
        //A pentagon, with a triangle inside
        System.out.println("Node added: " + graph.addNode("a"));
        System.out.println("Nodes added: " + graph.addNodes(new String[]{"b", "c", "d", "e"}));
        System.out.println("Edges added: " + graph.addEdges("b", new String[]{"d", "e"}));

        System.out.println("Edge added: " + graph.addEdge("a", "b"));
        System.out.println("Edge added: " + graph.addEdge("b", "c"));
        System.out.println("Edge added: " + graph.addEdge("c", "d"));
        System.out.println("Edge added: " + graph.addEdge("d", "e"));
        System.out.println("Nodes added: " + graph.addNodes(new String[]{"x", "y"}));
        System.out.println("Node removed: " + graph.removeNodes(new String[]{"x", "y"}));
        //The graph
        graph.printGraph();

        System.out.println("DFS traversal from 'a' to 'e', alphabetical: " + Arrays.toString(graph.DFS("a", "e", "alphabetical")));
        System.out.println("DFS traversal from 'a' to 'e', reverse: "+ Arrays.toString(graph.DFS("a", "e", "reverse")));
        System.out.println("DFS traversal from 'e' to 'c', alphabetical: " + Arrays.toString(graph.DFS("e", "c", "alphabetical")));
        System.out.println("DFS traversal from 'e' to 'c', reverse: "+ Arrays.toString(graph.DFS("e", "c", "reverse")));

        System.out.println("BFS traversal from 'a' to 'e', alphabetical: " + Arrays.toString(graph.BFS("a", "e", "alphabetical")));
        System.out.println("BFS traversal from 'a' to 'e', alphabetical: " + Arrays.toString(graph.BFS("a", "e", "reverse")));

        //Hexagon with a line inside
        graph = new Graph();
        graph.addNodes(new String[]{"a", "b", "c", "d", "e", "f", "x", "y"});
        graph.addEdge("a", "b");
        graph.addEdge("b", "c");
        graph.addEdge("c", "d");
        graph.addEdge("d", "e");
        graph.addEdge("e", "f");
        graph.addEdge("f", "a");

        graph.addEdge("x", "y");
        graph.addEdges("x", new String[]{"a","b","c"});
        graph.addEdges("y", new String[]{"d","e","f"});

        System.out.println("BFS traversal from 'b' to 'f', alphabetical: " + Arrays.toString(graph.BFS("b", "f", "alphabetical")));
        System.out.println("BFS traversal from 'b' to 'f', reverse: " + Arrays.toString(graph.BFS("b", "f", "reverse")));

        System.out.println("DFS traversal from 'b' to 'f', alphabetical: " + Arrays.toString(graph.DFS("b", "f", "alphabetical")));
        System.out.println("DFS traversal from 'b' to 'f', reverse: " + Arrays.toString(graph.DFS("b", "f", "reverse")));

        System.out.println("Second shortest path from 'b' to 'f', reverse: " + Arrays.toString(graph.secondShortestPath("a", "f")));
        System.out.println("Second shortest path from 'b' to 'f', reverse: " + Arrays.toString(graph.secondShortestPath("a", "c")));
        System.out.println("Second shortest path from 'b' to 'f', reverse: " + Arrays.toString(graph.secondShortestPath("a", "d")));
    }
}
