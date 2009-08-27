package dkp2006;

import java.io.File;
import java.util.Scanner;

public class ProblemE
{
	
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(new File("dkp2006/testdata/e.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int m = in.nextInt(), n = in.nextInt(), salary = in.nextInt();
			int[] reward = new int[m+1];
			int[] punishment = new int[m+1];
			double[][] p = new double[m+1][n+1];
			double[][] e = new double[m+1][n+1];
			for (int i = 1; i <= m; i++)
			{
				for (int j = 1; j <= n; j++)
					p[i][j] = in.nextInt()/100.0;
				reward[i] = in.nextInt();
				punishment[i] = in.nextInt();
			}
			for (int i = 0; i <= n; i++)
				e[0][i] = -i*salary;
			double max, ev;
			for (int i = 1; i <= m; i++)
				for (int j = 0; j <= n; j++)
				{
					max = Double.NEGATIVE_INFINITY;
					for (int k = 0; k <= j; k++)
						if (max < (ev = e[i-1][j-k] + p[i][k]*(reward[i]-k*salary) - (1-p[i][k])*punishment[i]))
							max =  ev;
					e[i][j] = max;
				}
			max = Double.NEGATIVE_INFINITY;
			for (int i = 0; i <= n; i++)
				if (max < e[m][i])
					max = e[m][i];
			System.out.println(Math.round(max*100));
			for (int i = 0; i <= n; i++)
				if (e[m][i] == max)
					System.out.print(i + " ");
			System.out.println();
		}
	}
	
}
