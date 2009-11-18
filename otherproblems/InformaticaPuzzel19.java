package otherproblems;
import java.io.File;
import java.util.Scanner;

public class InformaticaPuzzel19
{
	
	private static int n;
	private static Interval[] input = new Interval[8];
	private static boolean[] used = new boolean[8];
	private static Interval[] schedule = new Interval[8];
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("otherproblems/sampledata/19-approach.in"));
		int casenr = 0;
		while (true)
		{
			casenr++;
			if ((n=in.nextInt()) == 0)
				return;
			for (int i = 0; i < n; i++)
				input[i] = new Interval(in.nextInt(), in.nextInt());
			double result = permute(0);
			System.out.printf("Case %d: %d:%02d%n", casenr, (int)result, (int)((result - (int)result)*60));
		}
	}
	
	/**
	 * Oh, gee, I wonder what the 'permute' function would do?? Maybe it...
	 * permutes?? No, to obvious. Well, ok, you might be interested to know that
	 * it permutes all planes, schedules the planes optimal for each permutation
	 * and returns the maximum length of the smallest time between two planes
	 * found so far. Ok, there you have it, now you know it. I've confessed, so
	 * please go and leave me with my misery.
	 * @param i Indicates the level at which we are backtracking. It starts at 1
	 *        and ends at n.
	 * @return The maximum length of the smallest time between to planes.
	 */
	private static double permute(int i)
	{
		double max = -1, curr;
		if (i == n)
		{
			double min = Double.MAX_VALUE;
			for (int j = 0; j < n; j++)
				for (int k = j+1; k < n; k++)
					min = Math.min(min, (double)(schedule[k].b - schedule[j].a) / (k-j));
			return min;
		}
		// Some classic backtracking. Might serve well as educational example.
		for (int j = 0; j < n; j++)
			if (!used[j])
			{
				used[j] = true;
				schedule[i] = input[j];
				if (max < (curr = permute(i+1)))
					max = curr;
				used[j] = false;
			}
		return max;
	}
	
	/**
	 * Interval. Interval. That reminds me of something. Wait, let me think. Oh,
	 * it's *this* close to me, I just know I've heard that word before. Oooooh,
	 * now I remember! It's a range of time, bounded by two timestamps.
	 * Now, any morron that still doesn't know what this class is for, should go
	 * and stick his head in a timemachine, and experience the true meaning of
	 * the word 'interval'.
	 * @author jellefresen
	 */
	private static class Interval
	{
		public int a, b;
		public Interval(int a, int b)
		{
			this.a = a;
			this.b = b;
		}
	}
	
}
