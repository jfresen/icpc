package dkp2006;

// OULIPO, by Jan Kuipers
import java.io.*;

class MainG2
{

	public static void main(String[] args) throws Exception
	{

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int runs = Integer.parseInt(in.readLine());

		while (runs-- > 0)
		{
			String t = in.readLine();
			String s = in.readLine();

			int i, j, T = t.length(), S = s.length(), c = 0;
			int[] x = new int[T + 1];

			i = 1;
			j = 0;
			while (i < T)
			{
				if (t.charAt(i) == t.charAt(j))
				{
					i++;
					j++;
					x[i] = j;
				}
				else if (j > 0)
				{
					j = x[j];
				}
				else
				{
					i++;
				}
			}

			i = 0;
			j = 0;
			while (i < S)
			{
				if (s.charAt(i) == t.charAt(j))
				{
					i++;
					j++;
					if (j == T)
					{
						c++;
						j = x[j];
					}
				}
				else if (j > 0)
				{
					j = x[j];
				}
				else
				{
					i++;
				}
			}

			System.out.println(c);
		}
	}
}
