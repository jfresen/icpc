/*
 * Created on 29-sep-2005
 */
package nkp2001;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

public class ProblemH
{

	private static double min[];
	private static double max[];
	private static Point punt[][];
	
	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader("nkp2001\\sampledata\\h.in"));
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer st = new StreamTokenizer(reader);
		st.eolIsSignificant(false);
		st.nextToken();
		int numCases = (int)st.nval;
		for (int caseN = 0; caseN < numCases; caseN++)
		{
			st.nextToken();
			int n = (int)st.nval;
			min = new double[n];
			max = new double[n];
			punt = new Point[n][];
			for (int i = 0; i < n; i++)
			{
				st.nextToken();
				int p = (int)st.nval;
				punt[i] = new Point[p];
				for (int j = 0; j < p; j++)
				{
					punt[i][j] = new Point();
					st.nextToken();
					punt[i][j].x = (int)st.nval;
					st.nextToken();
					punt[i][j].y = (int)st.nval;
				}
			}
			Point visser = new Point();
			st.nextToken();
			visser.x = (int)st.nval;
			st.nextToken();
			visser.y = (int)st.nval;
			for (int i = 0; i < n; i++)
				for (int j = 0; j < punt[i].length; j++)
					punt[i][j].min(visser);
			for (int i = 0; i < n; i++)
				getMinMax(i);
			solve();
		}
	}

	private static void getMinMax(int a)
	{
		double current, previous;
		current = Math.atan2(punt[a][0].y, punt[a][0].x);
		min[a] = current;
		max[a] = current;
		int k = 0;
		for (int i = 1; i < punt[a].length; i++)
		{
			previous = current;
			current = Math.atan2(punt[a][i].y, punt[a][i].x);
			if (previous < current - Math.PI)
			{
				min[a] += 2*Math.PI;
				max[a] += 2*Math.PI;
				k--;
			}
			else if (previous > current + Math.PI)
			{
				min[a] -= 2*Math.PI;
				max[a] -= 2*Math.PI;
				k++;
			}
			if (current > max[a])
				max[a] = current;
			else if (current < min[a])
				min[a] = current;
		}
		min[a] += k*2*Math.PI;
		max[a] += k*2*Math.PI;
	}
	
	private static void solve()
	{
		double bereik[][] = new double[min.length*2][2];
		int size = 0;
		// zet alles om naar het bereik (-PI, PI)
		for (int i = 0; i < min.length; i++)
		{
			if (max[i] - min[i] >= 2*Math.PI)
			{
				System.out.println("1.0000");
				return;
			}
			else if (min[i] >= -Math.PI && max[i] <= Math.PI)
			{
				bereik[size][0] = min[i];
				bereik[size][1] = max[i];
				size++;
			}
			else if (min[i] < -Math.PI)
			{
				bereik[size][0] = -Math.PI;
				bereik[size][1] = max[i];
				size++;
				min[i] += 2*Math.PI;
				bereik[size][0] = min[i];
				bereik[size][1] = Math.PI;
				size++;
			}
			else if (max[i] > Math.PI)
			{
				bereik[size][0] = min[i];
				bereik[size][1] = Math.PI;
				size++;
				max[i] -= 2*Math.PI;
				bereik[size][0] = -Math.PI;
				bereik[size][1] = max[i];
				size++;
			}
		}
		for (int i = size; i < bereik.length; i++)
			bereik[i][0] = Double.NaN;
		double horizon = 0;
		for (int i = 0; i < size; i++)
		{
			if (bereik[i][0] != Double.NaN)
			{
				for (int j = i+1; j < size; j++)
				{
					if (bereik[j][0] < bereik[i][0] && bereik[j][1] > bereik[i][1])
						bereik[j][0] = Double.NaN;
					else if (bereik[j][0] >= bereik[i][0] && bereik[j][1] <= bereik[i][1])
						bereik[i][0] = Double.NaN;
					else if (bereik[j][0] <= bereik[i][1] && bereik[j][1] > bereik[i][1])
						bereik[j][0] = bereik[i][1];
					else if (bereik[j][0] < bereik[i][0] && bereik[j][1] >= bereik[i][0])
						bereik[j][1] = bereik[i][0];
				}
				horizon += bereik[i][1] - bereik[i][0];
			}
		}
		horizon = ((int)((5000 * horizon / Math.PI) + 0.5)) / 10000.0;
		String result = "" + horizon;
		while (result.length() != 6)
			result += "0";
		System.out.println(result);
	}
	
}

class Point
{
	public int x, y;
	
	public void min(Point that)
	{
		x -= that.x;
		y -= that.y;
	}
}
