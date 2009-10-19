package ekp2003;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class ProblemD
{

	public static void main(String[] args) throws Throwable
	{
		long start = System.currentTimeMillis();
		Scanner in = new Scanner(new File("ekp2003\\testdata\\d.in"));
		System.setOut(new PrintStream(new File("ekp2003\\testdata\\d")));
		int cases = in.nextInt();
		Employee[] employee = new Employee[1000000];
		while (cases-- > 0)
		{
			int m = in.nextInt();
			int q = in.nextInt();
			Employee[] s = new Employee[m];
			for (int i = 0; i < m; i++)
				s[i] = new Employee(in.nextInt(), in.nextInt(), in.nextInt());
			// sort on increasing salary
			Arrays.sort(s);
			for (int i = 0; i < m; i++)
				employee[s[i].i] = s[i];
			// construct the tree
			for (int i = m-2, j; i >= 0; s[i--].b = j)
				for (j = i+1; s[j].l < s[i].l; j = s[j].b);
			for (int i = 0; i < m-1; i++)
				s[s[i].b].e += 1 + s[i].e;
			// execute the queries
			for (int query = 0; query < q; query++)
			{
				Employee e = employee[in.nextInt()];
				if (e.b == 0)
					System.out.println("0 " + e.e);
				else
					System.out.println(s[e.b].i + " " + e.e);
			}
		}
		long end = System.currentTimeMillis();
		System.err.println("Took " + (end-start) + " ms");
	}

}

class Employee implements Comparable<Employee>
{
	public int i,s,l,b,e;
	public Employee(int id, int sal, int len)
	{i=id;s=sal;l=len;}
	public int compareTo(Employee o)
	{return s - o.s;}
}
