package bapc2005;

public class ProblemF
{
	
	static int[] primes = new int[80000];
	static int psize;
	static int possiblePrime;
	
	static int[] tolls;
	
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		calculatePrimes();
		calculateTolls();
		long end = System.currentTimeMillis();
		System.out.printf("Took %.2f seconds", (end - start)/1000.0);
	}
	
	private static void calculateTolls()
	{
		tolls = new int[primes[primes.length-1]];
	}
	
	private static void calculatePrimes()
	{
		primes[psize++] = 2;
		for (possiblePrime = 3; psize < primes.length; possiblePrime += 2)
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
