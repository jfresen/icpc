package nwerc2004;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemE2008
{
	public static void main(String[] args) throws Throwable
    {
		Scanner in = new Scanner(new File("ekp2004//sampledata//e.in"));
		for (int tests = in.nextInt(); tests-- > 0;)
		{
			int k = in.nextInt();
			Card2008[] a = new Card2008[k];
			Card2008[] e = new Card2008[k];
			for (int i = 0; i < k; i++)
				a[i] = new Card2008(in.next());
			for (int i = 0; i < k; i++)
				e[i] = new Card2008(in.next());
			Arrays.sort(a);
			Arrays.sort(e);
			int points = 0;
			for (int pe = 0, pa = 0; pe < k; pe++, pa++)
			{
				while (pa < k && e[pe].compareTo(a[pa]) >= 0)
					pa++;
				if (pa < k)
					points++;
			}
			System.out.println(points);
		}
    }
}

class Card2008 implements Comparable<Card2008>
{
	
	public int v, s;
	
	public Card2008(String t)
	{
		switch (t.charAt(0))
		{
			case 'T': v = 10; break;
			case 'J': v = 11; break;
			case 'Q': v = 12; break;
			case 'K': v = 13; break;
			case 'A': v = 14; break;
			default:  v = Integer.valueOf(t.substring(0, 1));
		}
		switch (t.charAt(1))
		{
			case 'C': s = 1; break;
			case 'D': s = 2; break;
			case 'S': s = 3; break;
			case 'H': s = 4; break;
		}
	}
	
	public int compareTo(Card2008 c)
    {
		if (v == c.v)
			return c.s - s;
	    return c.v - v;
    }
	
}