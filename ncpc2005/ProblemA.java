package ncpc2005;

import java.io.File;
import java.util.Scanner;

public class ProblemA
{
	static final int N = 0;
	static final int E = 1;
	static final int S = 2;
	static final int W = 3;
	static final int[] DX = {0, 1, 0, -1};
	static final int[] DY = {1, 0, -1, 0};
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ncpc2005/sampledata/a.in"));
//		Scanner in = new Scanner(System.in);
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int a = in.nextInt(), b = in.nextInt();
			int n = in.nextInt(), m = in.nextInt();
			int[] x = new int[n+1];
			int[] y = new int[n+1];
			int[] d = new int[n+1];
			boolean[][] map = new boolean[a+1][b+1];
			for (int i = 1; i <= n; i++)
			{
				x[i] = in.nextInt();
				y[i] = in.nextInt();
				d[i] = in.next().charAt(0);
				d[i] = d[i]=='N' ? 0 : d[i]=='E' ? 1 : d[i]=='S' ? 2 : 3;
				map[x[i]][y[i]] = true;
			}
			boolean idle = false;
			nextaction: for (int i = 1; i <= m; i++)
			{
				int ri = in.nextInt();
				char action = in.next().charAt(0);
				int repeat = in.nextInt();
				if (idle)
					continue nextaction;
				switch (action)
				{
					case 'L': d[ri] = (d[ri] + 3*repeat) % 4; break;
					case 'R': d[ri] = (d[ri] + 1*repeat) % 4; break;
					case 'F':
						int dx = DX[d[ri]];
						int dy = DY[d[ri]];
						map[x[ri]][y[ri]] = false;
						for (int j = 0; j < repeat; j++)
						{
							x[ri] += dx;
							y[ri] += dy;
							if (x[ri] == 0 || x[ri] > a || y[ri] == 0 || y[ri] > b)
							{
								System.out.printf("Robot %d crashes into the wall%n", ri);
								idle = true;
								continue nextaction;
							}
							if (map[x[ri]][y[ri]])
							{
								int rj;
								for (rj = 1; rj <= n; rj++)
									if (ri != rj && x[rj] == x[ri] && y[rj] == y[ri])
										break;
								System.out.printf("Robot %d crashes into robot %d%n", ri, rj);
								idle = true;
								continue nextaction;
							}
						}
						map[x[ri]][y[ri]] = true;
				}
			}
			if (!idle)
				System.out.println("OK");
		}
	}
}
