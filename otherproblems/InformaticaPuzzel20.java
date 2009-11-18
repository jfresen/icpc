package otherproblems;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class InformaticaPuzzel20
{
	
	static int[] primes = new int[100];
	static int psize;
	
	static int max, n;
	static int[] numbers;
	static boolean[] possible;
	
	/**
	 * The idea of the solution is actually just to simulate the game and
	 * propagate the knowledge gained from the answers. I keep a boolean array
	 * which stores for each i whether or not it is possible that i is the
	 * number from the gameleader.
	 * When a logician doesn't know the number, you know that there are at least
	 * two possible numbers that are the same in modulo p, so you strike out all
	 * numbers which are uniquely possible in modulo p.
	 * Similar, when a logician does know the number, you know that it must have
	 * been a unique number in modulo p, so you can strike out all numbers that
	 * are not unique in modulo p.
	 * 
	 * @author Jelle Fresen
	 */
	public static void main(String[] args) throws FileNotFoundException
	{
		long start = System.currentTimeMillis();
		Scanner in = new Scanner(new File("otherproblems/sampledata/20-getallenraden.in"));
		for (;;)
		{
			max = in.nextInt();
			if (max == 0)
				break;
			n = in.nextInt();
			// from Wolfram Mathworld:
			// An upper bound for pi(n) is given by
			//   pi(n) < 1.25506*n / ln(n)
			//   for n > 1
			primes = new int[n < 30 ? 10 : (int)(1.25506*n/Math.log(n))];
			calculatePrimes(max);
			numbers = new int[n];
			for (int i = 0; i < n; i++)
				numbers[i] = in.nextInt();
			possible = new boolean[max+1];
			Arrays.fill(possible, true);
			// iterate through all rounds of the game
			for (int i = 0; i < n; i++)
			{
				// iterate through all logicians that don't know the number
				for (int j = 0; j < psize && primes[j] < numbers[i]; j++)
					// iterate through all numbers in modulo p
					for (int k = 1; k <= primes[j]; k++)
						// count possibilities congruent to k modulo p
						if (occurrences(k, primes[j]) == 1)
							// flag the one (unknown) occurrence as impossible
							for (int l = k; l <= max; l += primes[j])
								possible[l] = false;
				// now for the logician that did know the number
				// iterate through all numbers in modulo p
				for (int k = 1; k <= numbers[i]; k++)
					// count possibilities congruent to k modulo p
					if (occurrences(k, numbers[i]) == 2)
						// flag all occurrences as impossible
						for (int l = k; l <= max; l += numbers[i])
							possible[l] = false;
			}
			boolean seen = false;
			for (int i = 1; i <= max; i++)
				if (possible[i])
				{
					System.out.print(i+" ");
					seen = true;
				}
			System.out.println(seen ? "" : "ONMOGELIJK");
		}
		long end = System.currentTimeMillis();
		System.out.printf("Took %.2f seconds", (end - start)/1000.0);
	}
	
	private static int occurrences(int n, int p)
	{
		boolean seen = false, seenTwice = false;
		for (; n <= max; n += p)
		{
			if (possible[n])
				if (!seen) seen      = true;
				else       seenTwice = true;
			if (seenTwice)
				break;
		}
		if (!seen)
			return 0;
		if (!seenTwice)
			return 1;
		return 2;
	}
	
	private static void calculatePrimes(int max)
	{
		psize = 0;
		primes[psize++] = 2;
		for (int possiblePrime = 3; primes[psize-1] < max; possiblePrime += 2)
			if (isPrime(possiblePrime))
				primes[psize++] = possiblePrime;
	}
	
	private static boolean isPrime(int possibility)
	{
		int ubound = (int)Math.sqrt(possibility);
		for (int i = 1; i < psize && primes[i] <= ubound; i++)
			if (possibility % primes[i] == 0)
				return false;
		return true;
	}
	
}
