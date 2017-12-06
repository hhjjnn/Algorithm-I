package code;
import edu.princeton.cs.algs4.*;
public class SAP {
	private Digraph graph_copy;
	private int length;
	private int ancestor;
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G)
	{
		if (G == null)
			throw new java.lang.IllegalArgumentException();
		//copy the graph G
		graph_copy = new Digraph(G.V());
		for (int i = 0; i < graph_copy.V(); i++)
			for (int j : G.adj(i))
				graph_copy.addEdge(i, j);
	}
	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w)
	{
		validateVertex(v);
		validateVertex(w);
		initialize(v, w);
		return length;
	}
	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w)
	{
		validateVertex(v);
		validateVertex(w);
		initialize(v, w);
		return ancestor;
	}
	
	private void validateVertex(int v) {
        int V = graph_copy.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
	
	private void initialize(int v, int w)
	{
		BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(graph_copy, v);
		BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(graph_copy, w);
		ancestor = -1;
		length = -1;
		int distance;
		for (int i = 0; i < graph_copy.V(); i++)
		{
			distance = bfs_v.distTo(i) + bfs_w.distTo(i);
			if (bfs_v.hasPathTo(i) && bfs_w.hasPathTo(i) && (distance < length || length == -1))
			{
				length = distance;
				ancestor = i;
			}
		}
	}
	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w)
	{
		validateVertices(v);
		validateVertices(w);
		initialize(v, w);
		return length;
	}
	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
	{
		validateVertices(v);
		validateVertices(w);
		initialize(v, w);
		return ancestor;
	}
	
	private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = graph_copy.V();
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
            }
        }
    }
	
	private void initialize(Iterable<Integer> v, Iterable<Integer> w)
	{
		BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(graph_copy, v);
		BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(graph_copy, w);
		ancestor = -1;
		length = -1;
		int distance;
		for (int i = 0; i < graph_copy.V(); i++)
		{
			distance = bfs_v.distTo(i) + bfs_w.distTo(i);
			if (bfs_v.hasPathTo(i) && bfs_w.hasPathTo(i) && (distance < length || length == -1))
			{
				length = distance;
				ancestor = i;
			}
		}
	}
	// do unit testing of this class
	public static void main(String[] args)
	{
		In in = new In(args[0]);
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    while (!StdIn.isEmpty()) {
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }
	}
}
