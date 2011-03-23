package nwerc2005;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProblemCLive {

	public static boolean[][] rs; 
	public static boolean[][][] loop;
	public static int ansx, ansy, direction, x, y, size;
	
	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int n = Integer.parseInt(in.readLine());
		
		for (int i = 0; i < n; i++) {
			String[] token = in.readLine().split(" ");
			size = Integer.parseInt(token[0]);
			int r = Integer.parseInt(token[1]);
			
			ansx = 0;
			ansy = 0;
			
			rs = new boolean[size+2][size+2];
			loop = new boolean[size+2][size+2][4];
			
			for (int j = 0; j < r; j++) {
				token = in.readLine().split(" ");
				int x = Integer.parseInt(token[0]);
				int y = Integer.parseInt(token[1]);
				rs[x][y] = true;
			}
			
			token = in.readLine().split(" ");
			x = Integer.parseInt(token[0]);
			y = Integer.parseInt(token[1]);
			
			
			if (x == 0) {
				direction = 1;
			}
			else if (x == size + 1) {
				direction = 3;
			}
			else if (y == 0) {
				direction = 0;
			}
			else {
				direction = 2;
			}
			
			while (step()) {
			}
			
			System.out.println("" + ansx + " " + ansy);
		}
	}
	
	public static boolean step() {
		// System.err.println("\t" + x + " " + y + " " + direction);
		if (loop[x][y][direction])
			return false;
		
		loop[x][y][direction] = true;
		switch (direction) {
		case 0:
			y += 1;
			break;
		case 1:
			x += 1;
			break;
		case 2:
			y -= 1;
			break;
		case 3:
			x -= 1;
			break;		
		}
		
		if (rs[x][y])
			direction = (direction + 1) % 4;
		
		if (x > size || y > size || x == 0 || y == 0) {
			ansx = x; ansy = y;
			return false;
		}
		
		return true;
	}

}
