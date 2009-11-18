package nwerc2009;

import java.io.File;

import java.util.Scanner;

public class ProblemA
{
	static int[] primes = new int[1000000];
	static int psize = 0, prime;
	static int[] max = new int[] {0, 10, 100, 1000, 10000, 100000, 1000000, 10000000};
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2009/testdata/a.in"));
		int cases = in.nextInt();
		generatePrimes();
		while (cases-- > 0)
		{
			int[] digits = stoi(in.next());
			int num = 0;
			for (int i = 0; primes[i] < max[digits.length] && i < psize; i++)
				if (fits(stoi(String.valueOf(primes[i])), digits))
					num++;
			System.out.println(num);
		}
	}
	
	private static int[] stoi(String n)
	{
		int[] digits = new int[n.length()];
		for (int i = 0; i < digits.length; i++)
			digits[i] = n.charAt(i) - '0';
		return digits;
	}
	
	private static boolean fits(int[] prime, int[] digits)
	{
		boolean[] used = new boolean[digits.length];
		for (int i = 0; i < prime.length; i++)
		{
			boolean found = false;
			for (int j = 0; j < digits.length && !found; j++)
				if (prime[i] == digits[j] && !used[j])
					used[j] = found = true;
			if (!found) return false;
		}
		return true;
	}
	
	private static void generatePrimes()
	{
		primes[psize++] = 2;
		for (prime = 3; prime < 10000000; prime += 2)
			if (possibilityIsPrime())
				primes[psize++] = prime;
	}
	
	private static boolean possibilityIsPrime()
	{
		int ubound = (int)Math.sqrt(prime);
		for (int i = 1; i < psize && primes[i] <= ubound; i++)
			if (prime % primes[i] == 0)
				return false;
		return true;
	}
}
