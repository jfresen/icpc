package bapc2005;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemF
{
	
	static int[] primes = new int[80000]; // tight bound: 79500
	static int psize;
	static int possiblePrime;
	
	static int[] jmp;
	
	public static void main(String[] args) throws Throwable
	{
		long start = System.currentTimeMillis();
		makePrimeTable();
		makeJumpTable();
		Scanner in = new Scanner(new File("bapc2005/testdata/f.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int s = in.nextInt();
			for (int b = in.nextInt(); b > 0; b--)
				for (int t = jmp[s]; s < t; s++)
					if (t > jmp[s])
						t = jmp[s];
			System.out.println(s);
		}
		long end = System.currentTimeMillis();
		System.out.printf("Took %.2f seconds%n", (end - start)/1000.0);
	}
	
	private static void makeJumpTable()
	{
		jmp = new int[primes[primes.length-1]+1];
		Arrays.fill(jmp, jmp.length);
		for (int i = 0; i < 3; i++) jmp[i] = i;
		jmp[3] = 4;
		for (int i = 5; i < jmp.length; i++)
		{
			int j = Arrays.binarySearch(primes, i);
			if (j<0) for (j=1; i%primes[j] != 0; j++);
			int to = i - i/primes[j];
			if (jmp[to] == jmp.length) jmp[to] = i;
		}
	}
	
	private static void makePrimeTable()
	{
		primes[psize++] = 2;
		primes[psize++] = 3;
		primes[psize++] = 4; // useful for the creation of the jumptable
		for (possiblePrime = 5; psize < primes.length; possiblePrime += 2)
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
