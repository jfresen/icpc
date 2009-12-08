/*
 * Created on 25-sep-2005
 */
package nwerc2004;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ProblemG
{

	public static Module[][] floor;
	public static int x, y;
	public static int highcost;
	
	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException
	{
		long start = System.currentTimeMillis();
		BufferedReader reader = new BufferedReader(new FileReader("ekp2004\\sampledata\\g.in"));
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int numCases = Integer.parseInt(reader.readLine());
		highcost = 10*10*9+1; // maxX*maxY*maxPrijs+1 = maximumprijs + 1
		for (int caseN = 0; caseN < numCases; caseN++)
		{
			StringTokenizer dim = new StringTokenizer(reader.readLine());
			y = Integer.parseInt(dim.nextToken());
			x = Integer.parseInt(dim.nextToken());
			floor = new Module[x][y];
			
			// maak lege modules
			for (int i = 0; i < x; i++)
				for (int j = 0; j < y; j++)
					floor[i][j] = new Module();
			
			// lees de tekening in
			for (int i = 0; i < y; i++)
			{
				String line = reader.readLine();
				if (line.charAt(1) == '#')
				{
					for (int j = 0; j < x; j++)
						floor[j][i].northcost = -1;
				}
				else
				{
					for (int j = 0; j < x; j++)
					{
						int cost = line.charAt(2*j+1) - '0';
						floor[j][i].northcost = cost;
						floor[j][i-1].southcost = cost;
					}
				}
				line = reader.readLine();
				for (int j = 0; j < x; j++)
				{
					if (line.charAt(j) == '#')
					{
						floor[j][i].eastcost = -1;
					}
					else
					{
						int cost = line.charAt(2*j) - '0';
						floor[j-1][i].westcost = cost;
						floor[j][i].eastcost = cost;
					}
				}
				floor[x-1][i].westcost = -1;
			}
			reader.readLine();
			for (int j = 0; j < x; j++)
				floor[j][y-1].southcost = -1;
			
			solve();
		}
		System.out.println("Time: "+(System.currentTimeMillis()-start));
	}

	public static void solve()
	{
		boolean[][] attended = new boolean[x][y];
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
				attended[i][j] = false;
		attended[0][0] = true;
		int cost = Math.min(cheapestPath(attended, 1, 0)+floor[0][0].westcost,
		                    cheapestPath(attended, 0, 1)+floor[0][0].southcost);
		System.out.println(cost);
	}
	
	public static int cheapestPath(boolean[][] attended, int cx, int cy)
	{
//		System.out.println("x: "+cx+" y: "+cy);
		if (attended[cx][cy])
		{
			if (cx == 0 && cy == 0)
			{
				// check of we alles hebben gehad
				boolean allAttended = true;
				for (int i = 0; i < x && allAttended; i++)
					for (int j = 0; j < y && allAttended; j++)
						allAttended = allAttended && attended[i][j];
				if (allAttended)
					return 0;
				else
					return highcost;
			}
			else
			{
				return highcost;
			}
		}
		
		attended[cx][cy] = true;
		int north = highcost;
		int west = highcost;
		int south = highcost;
		int east = highcost;
		if (cx > 0)
			east =  cheapestPath(attended, cx-1, cy)+floor[cx][cy].eastcost;
		if (cx < x-1)
			west =  cheapestPath(attended, cx+1, cy)+floor[cx][cy].westcost;
		if (cy > 0)
			north = cheapestPath(attended, cx, cy-1)+floor[cx][cy].northcost;
		if (cy < y-1)
			south = cheapestPath(attended, cx, cy+1)+floor[cx][cy].southcost;
		attended[cx][cy] = false;
		return min(east, west, north, south);
	}
	
	public static int min(int a, int b, int c, int d)
	{
		return Math.min(a, Math.min(b, Math.min(c, d)));
	}
	
}

class Module
{
	public int x, y, northcost, westcost, southcost, eastcost;
}