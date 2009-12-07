package cerc2009;

import java.io.File;
import java.util.Scanner;

public class numbers
{
	static int[][] pow = new int[11][100];
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("cerc2009/testdata/numbers.in"));
		String s = in.next();
		while (!s.equals("end"))
		{
			if (s.startsWith("to"))
			{
				int n10 = Integer.valueOf(in.next());
				int r = Integer.valueOf(s.substring(2)), r1 = r-1, rr1 = r*r-1;
				int[] nr = new int[len(n10, r)];
				for (int i = 0, e = nr.length-1; i < nr.length; i++, e--)
				{
					int p = pow(r, e);
					int f = n10 / p;
					if (n10-f*p != 0)
						f++;
//					if ((e&1)==0) // even exp
//					{
//						if (n10 < 0) // even exp, -n
//							f = 0;
//						else // even exp, +n
//						{
//							if ((p-1)/rr1 >= n10) // even exp, +n, but possible with smaller exp
//								f = 0;
//							else // even exp, +n, must do something
//							{
//								
//							}
//						}
//					}
//					else
//					{
//						
//					}
//					if (e > 2 && z != 0 && (n10<0 && z>0 || n10>0 && z<0))
//						f = 0;
					nr[i] = f;
					n10 -= f*p;
					System.out.print(nr[i]);
				}
				System.out.println();
			}
			else// if (s.startsWith("from"))
			{
				String nr = in.next();
				int r = Integer.valueOf(s.substring(4));
				int n10 = 0;
				for (int i = 0; i < nr.length(); i++)
					n10 += (nr.charAt(i)-'0') * pow(r, nr.length()-i-1);
				System.out.println(n10);
			}
			s = in.next();
		}
	}
	
	private static int pow(int r, int n)
	{
		if (n == 0)
			return 1;
		if (pow[r+10][n] != 0)
			return pow[r+10][n];
		pow[r+10][n] = r*pow(r, n-1);
		return pow[r+10][n];
	}
	
	private static int len(int n, int r)
	{
		if (n == 0)
			return 1;
		int a = 0, b = 0, l = 0;
		r = -r;
		while (a > n || b < n)
		{
			l++;
			a = (l&1) == 0 ? a - (r-1)*(b-a+1) : a;
			b = (l&1) == 0 ? b : b + (r-1)*(b-a+1);
		}
		return l;
	}
}
