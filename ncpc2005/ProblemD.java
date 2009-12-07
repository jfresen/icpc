package ncpc2005;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemD
{
	static int[] primes = new int[79000];
	static BigInteger[] PRIMES = new BigInteger[79000];
	static int psize = 0, prime;
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ncpc2005/sampledata/d.in"));
//		Scanner in = new Scanner(System.in);
		generatePrimes();
		for (int i = 0; i < psize; i++)
			PRIMES[i] = BigInteger.valueOf(primes[i]);
		nextcase: while (true)
		{
			BigInteger k = new BigInteger(in.next());
			int l = in.nextInt();
			if (k.equals(BigInteger.ZERO) && l == 0)
				return;
			int a = Arrays.binarySearch(primes, 0, psize, l);
			if (a < 0) a = ~a;
			for (int i = 0; i < a; i++)
				if (k.remainder(PRIMES[i]).equals(BigInteger.ZERO))
				{
					System.out.println("BAD " + primes[i]);
					continue nextcase;
				}
			System.out.println("GOOD");
		}
	}
	
	private static void generatePrimes()
	{
		psize = 0;
		primes[psize++] = 2;
		for (prime = 3; prime < 1000000; prime += 2)
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
