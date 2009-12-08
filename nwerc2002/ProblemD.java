package ekp2002;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class ProblemD
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ekp2002/testdata/d.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			// The whole floor:
			Point ll = new Point(0, 0);
			Point ur = new Point(in.nextInt(), in.nextInt());
			int n = in.nextInt();
			HashMap<Integer, Line> horiz = new HashMap<Integer, Line>();
			HashMap<Integer, Line> verti = new HashMap<Integer, Line>();
			Line line;
			for (int i = 0; i < n; i++)
			{
				int xl = in.nextInt();
				int yl = in.nextInt();
				int xh = in.nextInt();
				int yh = in.nextInt();
				if ((line = horiz.get(yl)) == null)
				{
					line = new Line(yl);
					horiz.put(yl, line);
				}
				line.add(xl, xh);
				if ((line = horiz.get(yh)) == null)
				{
					line = new Line(yh);
					horiz.put(yh, line);
				}
				line.add(xl, xh);
				if ((line = verti.get(xl)) == null)
				{
					line = new Line(xl);
					verti.put(xl, line);
				}
				line.add(yl, yh);
				if ((line = verti.get(xh)) == null)
				{
					line = new Line(xh);
					verti.put(xh, line);
				}
				line.add(yl, yh);
			}
			Line[] horizontal = new Line[horiz.size()];
			int i = 0;
			for (Line l : horiz.values())
				horizontal[i++] = l;
			Arrays.sort(horizontal);
			Line[] vertical = new Line[verti.size()];
			i = 0;
			for (Line l : verti.values())
				vertical[i++] = l;
			Arrays.sort(vertical);
			System.out.println(solve(horizontal, vertical, ll, ur));
		}
	}

	private static int solve(Line[] h, Line[] v, Point ll, Point ur)
	{
		int i = find(h, ll.x, ur.x);
		if (i != -1)
		{
			Line[] h1 = new Line[i+1];
			Line[] h2 = new Line[h.length-i];
			System.arraycopy(h, 0, h1, 0, i+1);
			System.arraycopy(h, i, h2, 0, h.length-i);
			ArrayList<Line> v1 = new ArrayList<Line>(v.length);
			ArrayList<Line> v2 = new ArrayList<Line>(v.length);
			for (int j = 0; j < v.length; j++)
			{
				Line[] split = v[j].split(h[i].v);
				if (split[0] != null)
					v1.add(split[0]);
				if (split[1] != null)
					v2.add(split[1]);
			}
			return Math.max(solve(h1, v1.toArray(new Line[0]), ll, new Point(ur.x, h[i].v)),
			                solve(h2, v2.toArray(new Line[0]), new Point(ll.x, h[i].v), ur));
		}
		i = find(v, ll.y, ur.y);
		if (i != -1)
		{
			Line[] v1 = new Line[i+1];
			Line[] v2 = new Line[v.length-i];
			System.arraycopy(v, 0, v1, 0, i+1);
			System.arraycopy(v, i, v2, 0, v.length-i);
			ArrayList<Line> h1 = new ArrayList<Line>(v.length);
			ArrayList<Line> h2 = new ArrayList<Line>(v.length);
			for (int j = 0; j < h.length; j++)
			{
				Line[] split = h[j].split(v[i].v);
				if (split[0] != null)
					h1.add(split[0]);
				if (split[1] != null)
					h2.add(split[1]);
			}
			return Math.max(solve(h1.toArray(new Line[0]), v1, ll, new Point(v[i].v, ur.y)),
			                solve(h2.toArray(new Line[0]), v2, new Point(v[i].v, ll.y), ur));
		}
		int area = (ur.x-ll.x)*(ur.y-ll.y);
		return area;
	}

	private static int find(Line[] lines, int l, int h)
	{
		// skip first and last, which is the canvas
		for (int i = 1; i < lines.length-1; i++)
			if (lines[i].equals(l, h))
				return i;
		return -1;
	}
	
}

class Line implements Comparable<Line>
{
	LinkedList<Segment> segments;
	int v;
	
	public Line(int v)
	{
		segments = new LinkedList<Segment>();
		this.v = v;
	}
	
	public void add(Segment s)
	{
		segments.add(s);
	}
	
	public void add(int l, int h)
	{
		Segment s = new Segment(l, h);
		Iterator<Segment> i = segments.iterator();
		LinkedList<Segment> toJoin = new LinkedList<Segment>();
		while (i.hasNext())
		{
			Segment curr = i.next();
			if (curr.overlaps(s))
			{
				i.remove();
				toJoin.add(curr);
			}
		}
		i = toJoin.iterator();
		while (i.hasNext())
			s = s.join(i.next());
		segments.add(s);
	}
	
	public Line[] split(int v)
	{
		Line before = new Line(this.v);
		Line after = new Line(this.v);
		Iterator<Segment> i = segments.iterator();
		while (i.hasNext())
		{
			Segment curr = i.next();
			if (curr.contains(v))
			{
				before.add(new Segment(curr.l, v));
				after.add(new Segment(v, curr.h));
			}
			else if (curr.l < v)
				before.add(curr);
			else
				after.add(curr);
		}
		return new Line[] {before.segments.size() > 0 ? before : null,
		                   after.segments.size() > 0 ? after : null};
	}
	
	public boolean equals(int l, int h)
	{
		return segments.size() == 1 && segments.getFirst().equals(l, h);
	}
	
	@Override
	public String toString()
	{
		String s = v+":";
		Iterator<Segment> i = segments.iterator();
		while (i.hasNext())
		{
			Segment a = i.next();
			s += a + (i.hasNext() ? ", " : "");
		}
		return s;
	}

	public int compareTo(Line that)
	{
		return this.v - that.v;
	}
}

class Segment
{
	int l, h;
	
	public Segment(int l, int h)
	{
		this.l = l;
		this.h = h;
	}
	
	public boolean contains(int v)
	{
		return l < v && v < h;
	}
	
	public boolean overlaps(Segment s)
	{
		return !(h < s.l || l > s.h);
	}
	
	public Segment join(Segment s)
	{
		return new Segment(Math.min(l, s.l), Math.max(h, s.h));
	}
	
	public boolean equals(int l, int h)
	{
		return this.l == l && this.h == h;
	}

	@Override
	public String toString()
	{
		return "["+l+","+h+"]";
	}
}
