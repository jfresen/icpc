package dkp2010;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemE
{
	private static int n, m;
	private static Tuple[] f;
	
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(new File("dkp2010/testdata/e.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			n = in.nextInt();
			String[] remove = new String[n];
			for (int i = 0; i < n; i++)
				remove[i] = in.next();
			m = in.nextInt();
			String[] keep = new String[m];
			for (int i = 0; i < m; i++)
				keep[i] = in.next();
			f = new Tuple[n+m];
			for (int i = 0; i < n; i++)
				f[i] = new Tuple(remove[i], true);
			for (int i = n; i < n+m; i++)
				f[i] = new Tuple(keep[i-n], false);
			Arrays.sort(f);
			
			int prev = -1, next = next(-1), cmds = 0;
			for (int curr = 0; curr < n+m; curr++)
			{
				while (curr < next)
				{
					String query = query(prev, curr, next);
					while (matches(query, curr))
						curr++;
					cmds++;
				}
				prev = next;
				next = next(next);
			}
			System.out.println(cmds);
		}
	}
	
	private static int next(int i)
	{
		for (i++; i < n+m; i++)
			if (!f[i].rm)
				return i;
		return i;
	}
	
	private static String query(int prv, int cur, int nxt)
	{
		assert prv < cur && cur < nxt;
		if (prv == -1 && nxt == n+m)
			return "*";
		String ps = prv >= 0 ? f[prv].s : "";
		String cs = f[cur].s;
		String ns = nxt < n+m ? f[nxt].s : "";
		int pp = 0, np = 0;
		while (pp<ps.length() && pp<cs.length() && ps.charAt(pp)==cs.charAt(pp)) pp++;
		while (np<ns.length() && np<cs.length() && ns.charAt(np)==cs.charAt(np)) np++;
		if (pp == cs.length() || np == cs.length())
			return cs;
		return cs.substring(0, 1 + (pp>np?pp:np)) + "*";
	}
	
	private static boolean matches(String query, int curr)
	{
		if (curr < 0 || curr >= n+m)
			return false;
		if (query.endsWith("*"))
			return f[curr].s.startsWith(query.substring(0, query.length()-1));
		return f[curr].s.equals(query);
	}
	
	private static class Tuple implements Comparable<Tuple>
	{
		String s; boolean rm;
		public Tuple(String s, boolean rm) {this.s=s; this.rm=rm;}
		@Override public int compareTo(Tuple a) {return s.compareTo(a.s);}
		@Override public String toString() {return "\n"+(rm?"-":"+")+s;}
	}
}
