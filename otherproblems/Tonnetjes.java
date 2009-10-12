package otherproblems;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Tonnetjes
{
	
	public static void main(String[] args)
	{
		BigDecimal getal1 = new BigDecimal(71);
		BigDecimal getal2 = new BigDecimal(101);
		BigDecimal getal3 = getal1.divide(getal2, new MathContext(500));
		System.out.println(getal3);
		Node[] nodes = new Node[12*14];
		for (int i = 0; i <= 11; i++)
			for (int j = 0; j <= 13; j++)
				nodes[i*14+j] = new Node(24-i-j, j, i);
//		System.out.println(Arrays.toString(nodes));
		for (Node n : nodes)
		{
			n.edges.add(nodes[n.c*14 + Math.min(13,n.a+n.b)]);                    // 24->13  (max(0,a+b-13), min(13,a+b), c)
			n.edges.add(nodes[Math.min(11,n.a+n.c)*14 + n.b]);                    // 24->11  (max(0,a+c-11), b, min(11,a+c))
			n.edges.add(nodes[n.c*14 + 0]);                                       // 13->24  (a+b, 0, c)
			n.edges.add(nodes[Math.min(11,n.b+n.c)*14 + Math.max(0,n.b+n.c-11)]); // 13->11  (a, max(0,b+c-11), min(11,b+c))
			n.edges.add(nodes[0*14 + n.b]);                                       // 11->24  (a+c, b, 0)
			n.edges.add(nodes[Math.max(0,n.b+n.c-13)*14 + Math.min(13,n.b+n.c)]); // 11->13  (a, min(13,b+c), max(0,b+c-13))
		}
		Queue<Node> q = new LinkedList<Node>();
		boolean[] visited = new boolean[nodes.length];
		q.add(nodes[0]);
		visited[0] = true;
		Node[] prev = new Node[nodes.length];
		while (!q.isEmpty())
		{
			Node n = q.poll();
			if (n.a == 8 && n.b == 8)
				break;
			for (Node m : n.edges)
			{
				if (!visited[m.c*14+m.b])
				{
					prev[m.c*14+m.b] = n;
					q.add(m);
					visited[m.c*14+m.b] = true;
				}
			}
		}
		LinkedList<Node> result = new LinkedList<Node>();
		Node curr = nodes[8*14+8];
		while (curr != null)
		{
			result.addFirst(curr);
			curr = prev[curr.c*14+curr.b];
		}
		if (visited[8*14+8])
			System.out.println("Yes, we can! " + result);
		else
			System.out.println("KUTTOM!!!! Je hebt je laten verneuken door de ETV...");
	}
	
	public static class Node
	{
		int a, b, c;
		List<Node> edges = new ArrayList<Node>();
		public Node(int a, int b, int c)
		{this.a=a;this.b=b;this.c=c;}
		@Override public String toString()
		{return "("+a+","+b+","+c+")";}
	}
	
}
