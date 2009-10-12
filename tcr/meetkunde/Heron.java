package tcr.meetkunde;

import java.util.Random;

public class Heron
{
	public static final int N = 10000000;
	
	public static void main(String[] args)
	{
		// It takes 1.5 seconds to proces 10 million random triangles.
		Random r = new Random();
		for (int j = 0; j < N; j++)
			area(r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt());
	}
	
	private static double area(long x1, long x2, long x3, long y1, long y2, long y3)
	{
		double a = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
		double b = Math.sqrt((x2-x3)*(x2-x3) + (y2-y3)*(y2-y3));
		double c = Math.sqrt((x3-x1)*(x3-x1) + (y3-y1)*(y3-y1));
		double t;
		if (b > a) {t = a; a = b; b = t;}
		if (c > a) {t = a; a = c; c = t;}
		if (c > b) {t = b; b = c; c = t;}
		
		return Math.sqrt((a+(b+c))*(c-(a-b))*(c+(a-b))*(a+(b-c)))/4;
	}
}
