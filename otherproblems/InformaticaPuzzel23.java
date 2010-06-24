package otherproblems;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class InformaticaPuzzel23
{
	private static int i = 0;
	
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("otherproblems/sampledata/23-quadtree.in"));
		for (int cases = in.nextInt(); cases-- > 0; i = 0)
			System.out.println(compact(in.next()));
//		Generator g = new Generator();
//		String s = g.generateInput(32, Generator.SEMI_RANDOM);
//		System.out.println(s);
//		System.out.println(compact(s));
	}
	
	private static String compact(String s)
	{
		if (s.charAt(i++) == '1')
			return s.substring(i-1, ++i);
		String t = "0"+compact(s)+compact(s)+compact(s)+compact(s);
		if (t.equals("010101010"))
			return "10";
		else if (t.equals("011111111"))
			return "11";
		return t;
	}
	
	static class Generator
	{
		private static final int BLACK = 0;
		private static final int WHITE = 1;
		private static final int RANDOM = 2;
		private static final int SEMI_RANDOM = 3;
		private Random r;
		
		public Generator() {r = new Random();}
		
		public String generateInput(int x, int mode)
		{
			if (Integer.bitCount(x) != 1)
			{
				System.out.println("Given dimensions are not allowed. Make sure it is a power of two.");
				return "";
			}
			if (x == 1)
				switch (mode)
				{
					case BLACK: return "11";
					case WHITE: return "10";
					case RANDOM:
					case SEMI_RANDOM:
					default:    return "1" + r.nextInt(2);
				}
			String s = "0";
			for (int i = 0; i < 4; i++)
			{
				int nextmode = mode;
				if (mode == SEMI_RANDOM)
					nextmode = (r.nextInt(x>>1) > 0 ? SEMI_RANDOM : r.nextInt(2));
				s += generateInput(x>>1, nextmode);
			}
			return s;
		}
	}
	
}
