public class CodeBase
{
	
	// Computes (b^e) mod m, where m <= 3.037.000.500
	// If long is replaced by int, then m <= 46.341
	public static long modexp(long b, long e, long m)
	{
		long x = 1;
		b = b % m;
		while (e > 0)
		{
			if ((e&1)==1)
				x = (x*b)%m;
			e >>= 1;
			b = (b*b)%m;
		}
		return x;
	}
	
	// Computes gcd(u, v), where u and v are both positive.
	public static int gcd(int u, int v)
	{
		int k, t, d;
		if (u == 0) return v;
		if (v == 0) return u;
		// Find common factors of 2.
		for (k = 0, t = (u|v); (t&1)==0; t >>= 1, k++);
		// Remove common factors of 2.
		u >>= k; v >>= k;
		// Calculate the remaining gcd.
		// First, initiate the value of d.
		if      ((u&1)==0) d =  u >> 1;
		else if ((v&1)==0) d = -v >> 1;
		else               d = (u >> 1) - (v >> 1);
		// Then calculate.
		for (; d != 0; d = (u-v) >> 1)
		{
			while ((d&1)==0) d >>= 1;
			if (d > 0) u =  d;
			else       v = -d;
		}
		return u << k;
	}
	
}

