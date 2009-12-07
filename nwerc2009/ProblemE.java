package nwerc2009;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemE
{
	static final int MD_DEP = 0;
	static final int MD_ARR = 1;
	static final int MA_DEP = 2;
	static final int MA_ARR = 3;
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("nwerc2009/sampledata/e.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = 0;
			int a = 1, b = 1;
			Car[] A = new Car[n+1], B = new Car[n+1];
			for (int i = 0; i < n; i++)
				switch (in.next().charAt(0))
				{
					case 'A': A[a++] = new Car(in.nextInt(), in.nextInt()); break;
					case 'B': B[b++] = new Car(in.nextInt(), in.nextInt()); break;
				}
			int[][] btob = new int[b+1][b];
			for (int i = 1; i < b; i++)
				for (int j=i, dep=-10, arr=-10, s=B[i].t; j < b; j++)
				{
					dep = Math.max(dep+10, B[i].t);
					arr = Math.max(arr+10, dep+B[i].d);
					btob[i][j] = arr - s;
				}
			int[][][] dyn = new int[a][b][2];
			// [0][0][md_dep] = 0 
			// [0][1][md_dep] = 
			// [0][i][md_dep] 
			// [0][i][md_dep] 
			// [0][i][md_dep] 
			// [0][i][md_arr] 
			// [0][i][ma_dep] 
			// [0][i][ma_arr] 
			
			// store in dyn[i][j]: the DEP and ARR, optimized for minimal DEP
			// and the DEP and ARR, optimized for minimal ARR
			
			for (int i = 1; i < a; i++)
				for (int j = 0; j < b; j++)
					for (int k = 0, dep, arr; k <= j; k++)
					{
						if (k != j)
						{
							dep = Math.max(dyn[i-1][k][MA_ARR]+btob[k+1][j], A[i].t);
							arr = dep + A[i].d;
							if (dyn[i][j][MD_DEP] > dep)
							{
								dyn[i][j][MD_DEP] = dep;
								dyn[i][j][MD_ARR] = arr;
							}
							else if (dyn[i][j][MD_DEP] == dep && dyn[i][j][MD_ARR] > arr)
								dyn[i][j][MD_ARR] = arr;
							if (dyn[i][j][MA_ARR] > arr)
							{
								dyn[i][j][MA_DEP] = dep;
								dyn[i][j][MA_ARR] = arr;
							}
							else if (dyn[i][j][MA_ARR] == arr && dyn[i][j][MA_DEP] > dep)
								dyn[i][j][MA_DEP] = dep;
						}
						else
						{
							dep = Math.max(dyn[i-1][k][MD_DEP]+10, A[i].t);
							arr = Math.max(dyn[i-1][k][MD_ARR]+10, dep+A[i].d);
							if (dyn[i][j][MD_DEP] > dep)
							{
								dyn[i][j][MD_DEP] = dep;
								dyn[i][j][MD_ARR] = arr;
							}
							else if (dyn[i][j][MD_DEP] == dep && dyn[i][j][MD_ARR] > arr)
								dyn[i][j][MD_ARR] = arr;
							dep = Math.max(dyn[i-1][k][MA_DEP]+10, A[i].t);
							arr = Math.max(dyn[i-1][k][MA_ARR]+10, dep+A[i].d);
							if (dyn[i][j][MA_ARR] > arr)
							{
								dyn[i][j][MA_DEP] = dep;
								dyn[i][j][MA_ARR] = arr;
							}
							else if (dyn[i][j][MA_ARR] == arr && dyn[i][j][MA_DEP] > dep)
								dyn[i][j][MA_DEP] = dep;
						}
					}
		}
	}
	
	private static class Car
	{
		int t, d;
		public Car(int t, int d)
		{this.t=t; this.d=d;}
	}
}
