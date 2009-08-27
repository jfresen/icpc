/*
 * Created on 24-jan-2006
 */
package otherproblems;

import java.io.BufferedReader;
import java.io.FileReader;

public class Abeltje
{
	
	public int n, i;
	public int[] pos;
	
	public static void main(String[] args) throws Throwable
	{
		Abeltje a = new Abeltje();
	}
	
	public Abeltje() throws Throwable
	{
		BufferedReader in = new BufferedReader(new FileReader("otherproblems\\sampledata\\abeltje.in"));
		int g = Integer.parseInt(in.readLine());
		for (; g > 0; g--)
		{
			n = Integer.parseInt(in.readLine()) >> 1; // only need n/2
			pos = new int[n];
			pos[0] = pos[1] = 1;
			i = 2;
		}
	}
	
}
