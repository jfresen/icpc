package otherproblems;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class AllPairsShortestPath
{
	
	public static final int MAX = 1000;
	public static int V;
	public static int E;
	public static int[][] W;
	
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner in = new Scanner(new FileReader("otherproblems/floyd-warshall.in"));
		V = in.nextInt();
		E = in.nextInt();
		W = new int[V][V];
		for (int i = 0; i < V; i++)
			Arrays.fill(W[i], MAX);
		for (int i = 0; i < E; i++)
			W[in.nextInt()][in.nextInt()] = in.nextInt();
		print();
		int c = 0;
		for (int k = 0, w; k < V; k++)
			for (int i = 0; i < V; i++)
				for (int j = 0; j < V; j++)
					if (W[i][j] > (w = W[i][k]+W[k][j]))
					{
						W[i][j] = w;
						print();
						c++;
					}
		print();
		System.out.println(c);
	}
	
	private static void print()
	{
		for (int i = 0; i < V; i++)
		{
			for (int j = 0; j < V; j++)
//				if (W[i][j] != MAX)
					System.out.printf("%4d ", W[i][j]);
//				else
//					System.out.printf("   ° ");
			System.out.println();
		}
//		System.out.println();
	}
	
}
