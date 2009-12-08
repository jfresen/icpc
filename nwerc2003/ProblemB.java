package ekp2003;

import java.io.File;
import java.util.Scanner;

/*

 In Python even wat dingetjes uitrekenen:

def fac(n):
	if (n <= 1):
		return 1
	return n*fac(n-1)

def binom(n,k):
	return fac(n) / (fac(k)*fac(n-k))

for i in range(1,11):
	print str(i) + " " + str(binom(10,i)**2)

1 100
2 2025
3 14400
4 44100
5 63504
6 44100
7 14400
8 2025
9 100
10 1

 */

public class ProblemB
{

	//                   [s][d]
	private static boolean[][] vase;
	//                          [maxk][(10 over 5)**2]
	private static K[][] k = new K[11][63504];
	private static int[] ks;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ekp2003\\testdata\\b.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int m = in.nextInt();
			vase = new boolean[37][37];
			ks = new int[11];
			for (int i = 0; i < m; i++)
			{
				int s = in.nextInt(), d = in.nextInt();
				vase[s][d] = true;
				k[1][ks[1]++] = new K(s, d);
			}
			int maxk = 1;
			K curr = null;
			// For each k, create all possible subcollections that Cheng has.
			// (relax, there're less than 200k possible subsets)
			while (maxk <= 10 && ks[maxk] > 0)
			{
				for (int i = 0; i < ks[maxk]; i++)
				{
					curr = k[maxk][i];
					// try to expand K_{maxk,maxk} to K_{maxk+1,maxk+1}
					for (int s = curr.S+1; s <= 36; s++)
						for (int d = curr.D+1; d <= 36; d++)
							if (isFull(curr, s, d))
								k[maxk+1][ks[maxk+1]++] = new K(curr, s, d);
				}
				maxk++;
			}
			System.out.println(maxk-1);
		}
	}
	
	private static boolean isFull(K k, int s, int d)
	{
		if (!vase[s][d])
			return false;
		// Does Cheng contain all decorations with shape s?
		for (int i = 0; i < k.s.length; i++)
			if (!vase[k.s[i]][d])
				return false;
		// And does he have all shapes with decoration d?
		for (int i = 0; i < k.d.length; i++)
			if (!vase[s][k.d[i]])
				return false;
		return true;
	}
	
}

class K
{
	public int[] s, d;
	public int S, D; // max
	public K(int s, int d)
	{
		this.s = new int[]{s}; S = s;
		this.d = new int[]{d}; D = d;
	}
	public K(K prev, int s, int d)
	{
		int n = prev.s.length;
		this.s = new int[n+1];
		this.d = new int[n+1];
		System.arraycopy(prev.s, 0, this.s, 0, n);
		System.arraycopy(prev.d, 0, this.d, 0, n);
		this.s[n] = s;
		this.d[n] = d;
		S = Math.max(prev.S, s);
		D = Math.max(prev.D, d);
	}
}