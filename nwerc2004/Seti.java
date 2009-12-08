package nwerc2004;

/*
 * Seti
 * Sample solution by Stein Norheim
 * 
 * The functions for solving a linear equation system are implemented
 * closely to the algorithms in Introduction to Algorithms by Cormen et al
 */
import java.io.*;

public class Seti
{

	public int mod(int a, int n)
	{
		if (a < 0)
			return (a % n) + n;
		else
			return a % n;
	}

	public class EuclidReturn
	{
		public int	d;
		public int	x;
		public int	y;

		public EuclidReturn(int _d, int _x, int _y)
		{
			d = _d;
			x = _x;
			y = _y;
		}
	}

	public EuclidReturn Extended_Euclid(int a, int b)
	{
		if (b == 0)
			return new EuclidReturn(a, 1, 0);

		EuclidReturn prim = Extended_Euclid(b, a % b);
		int newY = prim.x - (a / b) * prim.y;
		return new EuclidReturn(prim.d, prim.y, newY);
	}

	public int inverseMod(int a, int b, int n)
	{
		EuclidReturn prim = Extended_Euclid(a, n);

		if (b % prim.d == 0)
		{
			int dd = mod(prim.x * (b / prim.d), n);
			return dd;
		}
		else
			return -1;
	}

	public int[] LU_Solve(int[][] A, int[] b, int p)
	{
		int n = b.length;

		int[] x, y;
		int i, j;

		x = new int[n];
		y = new int[n];

		for (i = 1; i <= n; i++)
		{
			int tmpSum = 0;
			for (j = 1; j <= i - 1; j++)
				tmpSum = mod(tmpSum + (mod(A[i - 1][j - 1] * y[j - 1], p)), p);
			y[i - 1] = mod(b[i - 1] - tmpSum, p);
		}

		for (i = n; i >= 1; i--)
		{
			int tmpSum = 0;
			for (j = i + 1; j <= n; j++)
			{
				tmpSum = mod(tmpSum + mod(A[i - 1][j - 1] * x[j - 1], p), p);
			}
			x[i - 1] = inverseMod((A[i - 1][i - 1]), (mod(y[i - 1] - tmpSum, p)), p);
		}

		return x;
	}

	public void LU_Decompose(int[][] A, int p)
	{
		int n = A.length;
		int i, j, k;

		for (k = 1; k <= n; k++)
		{

			for (i = k + 1; i <= n; i++)
			{
				A[i - 1][k - 1] = inverseMod(A[k - 1][k - 1], A[i - 1][k - 1], p);
			}

			for (i = k + 1; i <= n; i++)
			{
				for (j = k + 1; j <= n; j++)
				{
					A[i - 1][j - 1] = mod(A[i - 1][j - 1] - (mod(A[i - 1][k - 1] * (A[k - 1][j - 1]), p)), p);
				}
			}
		}
	}

	public int[] SolveEquations(int[][] A, int[] b, int p)
	{
		LU_Decompose(A, p);
		int[] x = LU_Solve(A, b, p);
		return x;
	}

	public int[][] CreateA(int n, int p)
	{
		int[][] A = new int[n][n];
		int t = 0;
		for (int i = 0; i < n; i++)
		{
			int x = 1;
			t = mod(t + 1, p);

			for (int j = 0; j < n; j++)
			{
				A[i][j] = x;
				x = mod(x * t, p);
			}
		}

		return A;
	}

	public int f(int[] a, int xint, int p)
	{
		int x = xint;
		int xi = 1;
		int result = 0;
		for (int i = 0; i < a.length; i++)
		{
			result = mod(result + a[i] * xi, p);
			xi = mod(xi * x, p);
		}

		return result;
	}

	public static void PrintVector(int[] x)
	{

		for (int i = 0; i < x.length; i++)
		{
			if (i > 0)
				System.out.print(" ");

			System.out.print(x[i]);
		}
		System.out.println();
	}

	public String Decode(int[] a, int p)
	{
		char c;
		String targetStr = "";

		for (int i = 0; i < a.length; i++)
		{

			int v = f(a, (i + 1), p);

			int ch = v;

			if (ch == 0)
				c = '*';
			else
				c = (char)('a' + ch - 1);
			targetStr += c;
		}
		return targetStr;
	}

	public boolean Verify(int[] a, String compareString, int p)
	{
		return (Decode(a, p).equals(compareString));
	}

	public int[] StringToVector(String s)
	{
		int[] dest = new int[s.length()];

		for (int i = 0; i < s.length(); i++)
		{
			char ch = s.charAt(i);

			if (ch == '*')
				dest[i] = 0;
			else
			{
				int temp = (int)(ch - 'a') + 1;
				dest[i] = temp;
			}
		}

		return dest;
	}

	public void realMain(String[] s) throws IOException
	{

		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		StreamTokenizer tokens = new StreamTokenizer(stdin);

		tokens.resetSyntax();
		tokens.wordChars('a', 'z');
		tokens.wordChars('*', '*');
		tokens.parseNumbers();
		tokens.whitespaceChars(1, 32);

		tokens.nextToken();
		int N = (int)Math.round(tokens.nval);

		for (int i = 0; i < N; i++)
		{
			tokens.nextToken();
			int p = (int)Math.round(tokens.nval);

			tokens.nextToken();
			String strToEncode = tokens.sval;

			int[] b = StringToVector(strToEncode);
			int[][] A = CreateA(b.length, p);
			int[] x = SolveEquations(A, b, p);

			if (Verify(x, strToEncode, p))
				PrintVector(x);
			else
				System.out.println("verification failed!");
		}
	}

	public static void main(String[] s) throws IOException
	{
		Seti o = new Seti();
		o.realMain(s);
	}
}
