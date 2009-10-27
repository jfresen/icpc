package otherproblems;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class LogicaSpel
{
	
	static int[] primes = new int[100];
	static int psize;
	static int possiblePrime;
	
	static int max, n;
	static int[] numbers;
	static boolean[] possible;
	
	public static void main(String[] args) throws FileNotFoundException
	{
		long start = System.currentTimeMillis();
		Scanner in = new Scanner(new FileReader("otherproblems/spel.in"));
		for (;;)
		{
			max = in.nextInt();
			if (max == 0)
				break;
			n = in.nextInt();
			primes = new int[Math.max(1000, max/7)];
			psize = 0;
			calculatePrimes(max);
			numbers = new int[n];
			for (int i = 0; i < n; i++)
				numbers[i] = in.nextInt();
			possible = new boolean[max+1];
			Arrays.fill(possible, true);
			for (int i = 0; i < n; i++)
			{
				for (int j = 0; j < psize && primes[j] < numbers[i]; j++)
					for (int k = 1; k <= primes[j]; k++)
						if (occurences(k, primes[j]) == 1)
							for (int l = k; l <= max; l += primes[j])
								possible[l] = false;
				for (int k = 1; k <= numbers[i]; k++)
					if (occurences(k, numbers[i]) == 2)
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
	
	private static int occurences(int n, int p)
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
		primes[psize++] = 2;
		for (possiblePrime = 3; primes[psize-1] < max; possiblePrime += 2)
			if (possibilityIsPrime())
				primes[psize++] = possiblePrime;
	}
	
	private static boolean possibilityIsPrime()
	{
		int ubound = (int)Math.sqrt(possiblePrime);
		for (int i = 1; i < psize && primes[i] <= ubound; i++)
			if (possiblePrime % primes[i] == 0)
				return false;
		return true;
	}
	
}
