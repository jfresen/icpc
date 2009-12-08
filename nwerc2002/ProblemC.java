package ekp2002;

import java.io.File;
import java.util.Locale;
import java.util.Scanner;

public class ProblemC
{
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable
	{
		Scanner in = new Scanner(new File("ekp2002/testdata/c.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{// given: X             X                                 X
			//     P1     P2     P3     P4     L1        L2        angles
			double x1,y1, x2,y2, x3,y3, x4,y4, a1,b1,c1, a2,b2,c2, d1, d2;
			x1 = in.nextInt();
			y1 = in.nextInt();
			d1 = in.nextInt();
			x3 = in.nextInt();
			y3 = in.nextInt();
			d2 = in.nextInt();
			x2 = x1 + Math.sin((d1+180)*Math.PI*2/360);
			y2 = y1 + Math.cos((d1+180)*Math.PI*2/360);
			x4 = x3 + Math.sin((d2+180)*Math.PI*2/360);
			y4 = y3 + Math.cos((d2+180)*Math.PI*2/360);
			if (x1 == x2) {a1 = 1;b1 = 0;c1 = -x1;}
			else {a1 = -(y1-y2)/(x1-x2);b1 = 1;c1 = -(a1*x1) - (b1*y1);}
			if (x3 == x4) {a2 = 1;b2 = 0;c2 = -x3;}
			else {a2 = -(y3-y4)/(x3-x4);b2 = 1;c2 = -(a2*x3) - (b2*y3);}
			
			double x,y;
			x = (b2*c1 - b1*c2) / (a2*b1 - a1*b2);
			if (b1 == 0) y = -(a2*x + c2) / b2;
			else         y = -(a1*x + c1) / b1;
			
			System.out.printf(Locale.ENGLISH, "%.4f %.4f%n", x, y);
		}
	}
	
}
