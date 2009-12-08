package ekp2007;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ProblemA
{
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ekp2007\\testdata\\a.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int n = in.nextInt();
			int b = in.nextInt();
			Component[] c = new Component[n];
			HashSet<String> uniqueTypes = new HashSet<String>();
			for (int i = 0; i < n; i++)
			{
				c[i] = new Component(in);
				uniqueTypes.add(c[i].t);
			}
			Arrays.sort(c);
			@SuppressWarnings("unchecked")
			LinkedList<Component>[] comps = new LinkedList[uniqueTypes.size()];
			PriorityQueue<Tuple> queue = new PriorityQueue<Tuple>();
			for (int i = 0, j = 0; i < comps.length; i++)
			{
				comps[i] = new LinkedList<Component>();
				queue.add(new Tuple(i, c[j].q));
				b -= c[j].p;
				for (String t = c[j].t; j < n && c[j].t.equals(t); j++)
					comps[i].addLast(c[j]);
			}
			while (true)
			{
				Tuple t = queue.poll();
				if (comps[t.i].size() == 1 || comps[t.i].get(1).p - comps[t.i].get(0).p > b)
				{
					System.out.println(t.q);
					break;
				}
				Component bad = comps[t.i].removeFirst();
				Component best = bad;
				while (best == bad && !comps[t.i].isEmpty())
				{
					Component cmp = comps[t.i].removeFirst();
					if (cmp.q > bad.q && cmp.p - bad.p <= b)
					{
						best = cmp;
						b -= cmp.p - bad.p;
					}
				}
				if (best == bad)
				{
					System.out.println(best.q);
					break;
				}
				comps[t.i].addFirst(best);
				queue.add(new Tuple(t.i, best.q));
			}
		}
	}
	
}

class Tuple implements Comparable<Tuple>
{
	public int i, q;
	public Tuple(int i, int q)
	{this.i = i; this.q = q;}
	public int compareTo(Tuple t)
	{return q - t.q;}
}

class Component implements Comparable<Component>
{
	public String t, n;
	public int p, q;
	public Component(Scanner in)
	{
		t = in.next();
		n = in.next();
		p = in.nextInt();
		q = in.nextInt();
	}
	public int compareTo(Component c)
	{
		int ct = t.compareTo(c.t);
		int cp = p - c.p;
		int cq = c.q - q;
		if (ct == 0 && cp == 0)
			return cq;
		else if (ct == 0)
			return cp;
		else
			return ct;
	}
}