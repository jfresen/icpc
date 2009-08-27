package dkp2006;

import java.io.*;
import java.util.Arrays;

class ProblemH {

	static int sx, sy;
	
	static double[] starts, ends;
	static Segment[] segments;
	static HPoint[] points;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		BufferedReader r = new BufferedReader(new FileReader("h.in"));
		int cases = Integer.parseInt(r.readLine());
		
		for (int n = 0; n < cases; n++) {
			int lines = Integer.parseInt(r.readLine());
			
			String[] source = r.readLine().split(" ");
			sx = Integer.parseInt(source[0]);
			sy = Integer.parseInt(source[1]);
			
			if (lines == 0) {
				System.out.println("1");
				continue;
			}
			
//			starts = new double[lines];
//			ends = new double[lines];
			// segments = new Segment[lines];
			points = new HPoint[lines*2];
			
			String[] lt;

			for (int l = 0; l < lines; l++) {
				lt = r.readLine().split(" ");
				int x0 = Integer.parseInt(lt[0]);
				int y0 = Integer.parseInt(lt[1]);
				int x1 = Integer.parseInt(lt[2]);
				int y1 = Integer.parseInt(lt[3]);
				
				double d0 = calcy0(x0, y0);
				double d1 = calcy0(x1, y1);
				
				// System.out.println(d0);
				// System.out.println(d1);
				if (d0 > d1) {
					double dt = d0;
					d0 = d1;
					d1 = dt;
				}
				
				// segments[l] = new Segment(d0, d1);
				points[l*2] = new HPoint(true, d0);
				points[l*2+1] = new HPoint(false, d1);
			}

			int segs = 1;
			Arrays.sort(points);
			
			/*System.out.println("Segments: ");
			for (int l = 0; l < lines; l++) {
				System.out.println(segments[l]);
			}
			
			System.out.println("Compares:");*/
/*			double end = segments[0].end;
			for (int l = 1; l < lines; l++) {
				// System.out.println("" + segments[l].start + " " + segments[l-1].end);
				if (segments[l].start > end)
					 segs++;
				else if (segments[l].end > end) {
					end = segments[l].end;
				}
			}*/
			
			int inSeg = 0;
			for (int p = 0; p < lines*2; p++) {
				HPoint pt = points[p];
				
				if (pt.isStart) {
					if (inSeg == 0) {
						segs++;
					}
					inSeg++;
				}
				else {
					inSeg--;
				}
				
			}
			
			System.out.println(segs);
		}
	}
	
	public static double calcy0(int x, int y) {
		if (sx == x)
			return x;
		
		double dx = sx - x;
		double dy = sy - y;
		
		double diff = dy/dx;
		
		return ((double) x) - ((double) y) / diff;	
	}

}

/**
 * @author szp
 *
 */
class Segment implements Comparable {
	public double start, end;

	public Segment(double d0, double d1) {
		start = d0;
		end = d1;
	}

	public int compareTo(Object arg0) {
		Segment other = (Segment) arg0;
		if (other.start == start)
			return 0;
		if (other.start > start)
			return -1;
		return 1;
	}
	
	@Override
	public String toString() {
		return "" + start + ", " + end;
	}
}

class HPoint implements Comparable {
	public boolean isStart;
	public double x;
	
	public HPoint(boolean isStart, double x) {
		super();
		// TODO Auto-generated constructor stub
		this.isStart = isStart;
		this.x = x;
	}

	public int compareTo(Object arg0) {
		HPoint other = (HPoint) arg0;
		if (other.x > x) 
			return -1;
		return 1;
	}
	
	@Override
	public String toString() {
		return (isStart ? "S " : "E ") + x;
	}
}