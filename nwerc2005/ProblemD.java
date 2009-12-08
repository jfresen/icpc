package nwerc2005;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProblemD {

	static int[] hs, rs, Rs;
	static double[][] heights;
	static int bowls;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int n = Integer.parseInt(in.readLine());
		
		for (int i = 0; i < n; i++) {
			bowls = Integer.parseInt(in.readLine());
			hs = new int[bowls];
			rs = new int[bowls];
			Rs = new int[bowls];
			
			for (int j = 0; j < bowls; j++) {
				String[] token = in.readLine().split(" ");
				int h = Integer.parseInt(token[0]);
				int r = Integer.parseInt(token[1]);
				int R = Integer.parseInt(token[2]);
				
				hs[j] = h;
				rs[j] = r;
				Rs[j] = R;
			}
		}
		
		for (int x = 0; x < bowls; x++) {
			for (int y = 0; y < bowls; y++) {
				
			}
		}
	}

}
