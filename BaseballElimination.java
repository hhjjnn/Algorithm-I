package code;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.Bag;

public class BaseballElimination {
	private ST<String, Integer> teams;
	private int n;
	private int[] w;
	private int[] l;
	private int[] r;
	private int[][] g;
	public BaseballElimination(String filename)                    // create a baseball division from given filename in format specified below
	{
		if (filename == null)
			throw new java.lang.IllegalArgumentException();
		In file = new In(filename);
		n = Integer.parseInt(file.readLine());
		String line;
		teams = new ST<String, Integer>();
		w = new int[n];
		l = new int[n];
		r = new int[n];
		String[] words;
		g = new int[n][n];
		int i = 0;
		
		while (file.hasNextLine())
		{
			line = file.readLine().trim();
			words = line.split(" +");
			teams.put(words[0], i);
			w[i] = Integer.parseInt(words[1]);
			l[i] = Integer.parseInt(words[2]);
			r[i] = Integer.parseInt(words[3]);
			for (int j = 0; j < n; j++)
				g[i][j] = Integer.parseInt(words[j+4]);
			i++;
			
		}
	}
	public              int numberOfTeams()                        // number of teams
	{
		return n;
	}
	public Iterable<String> teams()                                // all teams
	{
		return teams.keys();
	}
	public              int wins(String team)                      // number of wins for given team
	{
		if (team == null || !teams.contains(team))
			throw new java.lang.IllegalArgumentException();
		return w[teams.get(team)];
	}
	public              int losses(String team)                    // number of losses for given team
	{
		if (team == null || !teams.contains(team))
			throw new java.lang.IllegalArgumentException();
		return l[teams.get(team)];
	}
	public              int remaining(String team)                 // number of remaining games for given team
	{
		if (team == null || !teams.contains(team))
			throw new java.lang.IllegalArgumentException();
		return r[teams.get(team)];
	}
	
	public              int against(String team1, String team2)    // number of remaining games between team1 and team2
	{
		if (team1 == null || team2 == null || !teams.contains(team1) || !teams.contains(team2))
			throw new java.lang.IllegalArgumentException();
		return g[teams.get(team1)][teams.get(team2)];
	}
	public  boolean isEliminated(String team)              // is given team eliminated?
	{
		if (team == null || !teams.contains(team))
			throw new java.lang.IllegalArgumentException();
		int teamNumber = teams.get(team);
		//eliminate trivial case
		for (int i = 0; i < n; i++)
		{
			if (w[teamNumber] + r[teamNumber] < w[i])
				return true;
		}
		//find a max flow
		FordFulkerson maxFlow = network(teamNumber);
		//if there is vertices from 0 to n*n+n-1 that connected to source with no full value, then eliminate
		for (int i = 0; i < n; i++)
			for (int j = i + 1; j < n; j++)
			{
				if (maxFlow.inCut(i * n + j))
					return true;
			}
		
		return false;
	}
	
	public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
	{
		if (team == null || !teams.contains(team))
			throw new java.lang.IllegalArgumentException();
		int teamNumber = teams.get(team);
		int number;
		Bag<String> R = new Bag<String>();
		//Eliminated in the trivial case
		for (String t : teams.keys())
		{
			number = teams.get(t);
			if (w[teamNumber] + r[teamNumber] < w[number])
			{
				R.add(t);
				return R;
			}
		}
		//not eliminated in the trivial case
		FordFulkerson maxFlow = network(teamNumber);
		for (String t : teams.keys())
		{
			if (maxFlow.inCut(n * n + teams.get(t)))
				R.add(t);
		}
		if (R.isEmpty())
			return null;
		else
			return R;
	}
	
	private FordFulkerson network(int teamNumber)
	{
		FlowNetwork fn = new FlowNetwork(n*n + n + 2);
		for (int i = 0; i < n; i++)
		{
			fn.addEdge(new FlowEdge(n * n + i, n * n + n + 1, w[teamNumber] + r[teamNumber] - w[i]));
			for (int j = i + 1; j < n; j++)
			{
				fn.addEdge(new FlowEdge(i * n + j, n * n + i, Integer.MAX_VALUE));
				fn.addEdge(new FlowEdge(i * n + j, n * n + j, Integer.MAX_VALUE));
				if (g[i][j] != 0)
					fn.addEdge(new FlowEdge(n * n + n, i * n + j, g[i][j]));
			}
		}
		FordFulkerson maxFlow = new FordFulkerson(fn, n*n+n, n*n+n+1);
		//StdOut.print(fn.toString());
		return maxFlow;
	}
	
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination("baseball/teams4.txt");
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
}