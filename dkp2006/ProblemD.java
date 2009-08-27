package dkp2006;

import java.io.BufferedReader;
import java.io.FileReader;

public class ProblemD {

	static char[] target;
	static char[] s1;
	static char[] s2;
	static int targetSize;
	static int sSize;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		BufferedReader r = new BufferedReader(new FileReader("d.in"));
		int cases = Integer.parseInt(r.readLine());
		
		for (int i = 0; i < cases; i++) {
			target = r.readLine().toCharArray();
//			System.out.println(Arrays.toString(target));
			s1 = r.readLine().toCharArray();
//			System.out.println(Arrays.toString(s1));
			s2 = r.readLine().toCharArray();
//			System.out.println(Arrays.toString(s2));
			targetSize = target.length;
			sSize = s1.length;
			doMagic();
		}
	}
	
	public static void doMagic() {
		int nextChar = 0;
		for (int i = 0; i < sSize; i++) {
			if (s1[i] == '*' ||
			    s1[i] == target[nextChar] ||
			    s2[i] == target[nextChar]) {
				if (++nextChar == targetSize) {
					System.out.println("win");
					return;
				}
			}
		}
		System.out.println("lose");
	}

}
