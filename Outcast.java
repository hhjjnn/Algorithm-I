package code;
import edu.princeton.cs.algs4.*;
public class Outcast {
	   private WordNet wordnet;
		public Outcast(WordNet wordnet)         // constructor takes a WordNet object
	   {
		   this.wordnet = wordnet;
	   }
	   public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
	   {
		   String outcast = nouns[0];
		   int distance = 0;
		   int[] distances = new int[nouns.length];
		   for (int i = 0; i < nouns.length; i++)
		   {
			   distances[i] = 0;
			   for (int j = 0; j < nouns.length; j++)
				   distances[i] += wordnet.distance(nouns[i], nouns[j]);
			   if (distance < distances[i])
			   {
				   outcast = nouns[i];
				   distance = distances[i];
			   }
			   
		   }
		   return outcast;
	   }
	   public static void main(String[] args)  // see test client below
	   {
		   WordNet wordnet = new WordNet(args[0], args[1]);
		    Outcast outcast = new Outcast(wordnet);
		    for (int t = 2; t < args.length; t++) {
		        In in = new In(args[t]);
		        String[] nouns = in.readAllStrings();
		        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		    }
	   }
	}