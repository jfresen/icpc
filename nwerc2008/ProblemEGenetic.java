package nwerc2008;

import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class ProblemEGenetic
{
	
	public static int n, d;
	public static int[] h, min, max;
	public static Random r = new Random();
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ekp2008/sampledata/e.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// Read input
			n = in.nextInt();
			d = in.nextInt();
			h = new int[n];
			for (int i = 0; i < n; i++)
				h[i] = in.nextInt();
			
			if (Math.abs(h[0]-h[n-1]) > (n-1)*d)
			{
				System.out.println("impossible");
				continue;
			}
			min = new int[n];
			max = new int[n];
			min[0] = max[0] = h[0];
			min[n-1] = max[n-1] = h[n-1];
			for (int i = 1; i < n-1; i++)
			{
				min[i] = Math.max(0, min[i-1] - d);
				max[i] = Math.min(1000000000, max[i-1] + d);
			}
			for (int i = n-2; i > 0; i--)
			{
				min[i] = Math.max(min[i], min[i+1] - d);
				max[i] = Math.min(max[i], max[i+1] + d);
			}
			
			// Solve?
			long best = Long.MAX_VALUE, curr;
			for (int i = 0; i < 100; i++)
				if (best > (curr = geneticSolve()))
					best = curr;
			System.out.println(best);
		}
	}
	
	public static long geneticSolve()
	{
		// Generate start population
		Specimen[] population = new Specimen[220];
		Specimen[] newPopulation = new Specimen[220];
		Specimen[] tmp;
		for (int i = 0; i < 220; i++)
			population[i] = new Specimen(gen());
		Arrays.sort(population, 0, 220);
		
		// Start the algorithm
		long best = population[0].value;
		for (int i = 0; i < 100; i++)
		{
			int j;
			for (j = 0; j < 10; j++)
				newPopulation[j] = population[j];
			for (int k = 0; k < 15; k++)
				for (int l = k+1; l < 15; l++)
				{
					Specimen[] children = gen(population[k], population[l]);
					newPopulation[j++] = children[0];
					newPopulation[j++] = children[1];
				}
			Arrays.sort(newPopulation);
			if (best > newPopulation[0].value)
				best = newPopulation[0].value;
			tmp = population;
			population = newPopulation;
			newPopulation = tmp;
		}
		return best;
	}
	
	public static int[] gen()
	{
		int[] solution = new int[n];
		for (int i = 0; i < n; i++)
			solution[i] = r.nextInt(max[i]-min[i]+1) + min[i];
		return solution;
	}
	
	public static Specimen[] gen(Specimen a, Specimen b)
	{
		int[] guess1 = new int[n];
		int[] guess2 = new int[n];
		int m = r.nextInt(n-1)+1;
		System.arraycopy(a.guess, 0, guess1, 0, m);
		System.arraycopy(b.guess, m, guess1, m, n-m);
		System.arraycopy(b.guess, 0, guess2, 0, m);
		System.arraycopy(a.guess, m, guess2, m, n-m);
		for (int i = 0; i < n; i++)
		{
			if (Math.random() < 0.3/n)
				guess1[i] = r.nextInt(max[i]-min[i]+1) + min[i];
			if (Math.random() < 0.3/n)
				guess2[i] = r.nextInt(max[i]-min[i]+1) + min[i];
		}
		return new Specimen[] {new Specimen(guess1), new Specimen(guess2)};
	}
	
	public static long v(int[] a)
	{
		long diff = 0;
		for (int i = 0; i < n; i++)
			diff += Math.abs(a[i]-h[i]);
		return diff;
	}
	
	private static class Specimen implements Comparable<Specimen>
	{
		public int[] guess;
		public long value;
		public Specimen(int[] guess)
		{
			this.guess = guess;
			this.value = v(guess);
		}
		public int compareTo(Specimen o)
		{
			if (value < o.value)
				return -1;
			if (value > o.value)
				return 1;
			return 0;
		}
		
	}
	
}
