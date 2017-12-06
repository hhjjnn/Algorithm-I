package code;
import edu.princeton.cs.algs4.*;
import java.util.ArrayList;
public class WordNet {
	private Digraph G;
	private ST<String, Bag<Integer>> synset_table;
	private ArrayList<String> list;
	private SAP sap;
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms)
	{
		if (synsets == null || hypernyms == null)
			throw new java.lang.IllegalArgumentException();
		
		//construct all the vertices
		synset_table = new ST<String, Bag<Integer>>();
		list = new ArrayList<String>();
		readSynsets(synsets);
		
		
		//construct all the edges
		
		readHypernyms(hypernyms);
		StdOut.println(G.toString());
		//check is a rooted DAG
		if (!isRootedDAG())
			throw new java.lang.IllegalArgumentException();
		
		//construct the SAP
		sap = new SAP(G);
	}
	
	private void readSynsets(String synsets)
	{
		In file_input = new In(synsets);
		String[] line, nouns;
		int V = 0;
		while(file_input.hasNextLine())
		{
			line = file_input.readLine().split(",");
			nouns = line[1].split(" ");
			//add the second field in synset to the list
			list.add(line[1]);
			
			//add the synset id to its corresponding noun
			for (String noun : nouns)
			{
				if (synset_table.get(noun) == null)
				{
					Bag<Integer> q = new Bag<Integer>();
					q.add(Integer.parseInt(line[0]));
					synset_table.put(noun, q);
				}
				else
					synset_table.get(noun).add(Integer.parseInt(line[0]));
				
			}
			V++;
		}
		
		//construct the digraph
		G = new Digraph(V);
		
	}
	
	private void readHypernyms(String hypernyms)
	{
		In file_input = new In(hypernyms);
		String[] line;
		while(file_input.hasNextLine())
		{
			line = file_input.readLine().split(",");
			for (int i = 1; i < line.length; i++)
				G.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
		}
	}
	
	private boolean isRootedDAG()
	{
		
		//check whether the graph is a rooted DAG
		Iterable<Integer> order = (new Topological(G)).order();
		if (order == null)
			return false;
		else 
		{
		int root = order.iterator().next();
		StdOut.println(root);
		BreadthFirstDirectedPaths path = new BreadthFirstDirectedPaths(G, root);
		for (int i = 0; i < G.V(); i++)
		{
			if (!path.hasPathTo(i))
				return false;
		}
		}
		return true;
	}
	
	// returns all WordNet nouns
	public Iterable<String> nouns()
	{
		return synset_table.keys();
	}
	// is the word a WordNet noun?
	public boolean isNoun(String word)
	{
		return synset_table.contains(word);
	}
	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB)
	{
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException("one of the noun is not in the net");
		return sap.length(synset_table.get(nounA), synset_table.get(nounB));
	}
	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB)
	{
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException("one of the noun is not in the net");
		return list.get(sap.ancestor(synset_table.get(nounA), synset_table.get(nounB)));
	}
	// do unit testing of this class
	public static void main(String[] args)
	{
		WordNet w = new WordNet(args[0], args[1]);
		StdOut.println(w.sap("worm", "bird"));
		StdOut.println(w.distance("worm", "bird"));
		for (int i : w.synset_table.get("worm"))
			StdOut.println(w.list.get(i));
		StdOut.println(w.synset_table.size());
		
	}
}
