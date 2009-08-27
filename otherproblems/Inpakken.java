package otherproblems;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Inpakken
{
	
	public static void main(String[] args) throws Throwable
	{
		// Create a handle to the inputdata:
//		File random = generateRandomTests(100000);
		File fixed1 = new File("otherproblems//sampledata//inpakken.in");
		File fixed2 = new File("C:\\DOCUME~1\\JELLEF~1\\LOCALS~1\\Temp\\inpakken15056.in");
		Scanner in = new Scanner(fixed2);
		
		// Solve the problem:
		InpakkenInterface solver = new InpakkenSolver1();
		long start = System.currentTimeMillis(), end = 0;
		solver.solve(in);
		end = System.currentTimeMillis();
		System.out.printf("Testing took %.2f seconds%n", (end - start) / 1000d);
	}
	
	/**
	 * Generates a temporary file with n random tests. The file will be deleted
	 * when the JVM exists. The numbers are randomly generated between 1 and an
	 * upperbound. The upperbound is different for all three boxes. Usually, the
	 * largest box will have a higher upperbound and the two small boxes will
	 * have the same upperbound, lower than the upperbound of the largest box.
	 * 
	 * @param n The number of tests that must be generated in the file.
	 * @return A temporary file that contains n random tests.
	 * @throws Throwable Handles no errors.
	 */
	private static File generateRandomTests(int n) throws Throwable
	{
		// Number generation variables:
		Random r = new Random();
		final int BIG_BOX = 10000;
		final int SMALL_BOX_1 = 3000;
		final int SMALL_BOX_2 = 8000;
		
		// File writing variables:
		File temp = File.createTempFile("inpakken", ".in", null);
		temp.deleteOnExit();
		PrintWriter out = new PrintWriter(new FileWriter(temp));
		
		// Number generation:
		for (int i = 0; i < n; i++)
		{
			out.printf("%d %d %d%n", 1+r.nextInt(BIG_BOX), 1+r.nextInt(BIG_BOX), 1+r.nextInt(BIG_BOX));
			out.printf("%d %d %d%n", 1+r.nextInt(SMALL_BOX_1), 1+r.nextInt(SMALL_BOX_1), 1+r.nextInt(SMALL_BOX_1));
			out.printf("%d %d %d%n", 1+r.nextInt(SMALL_BOX_2), 1+r.nextInt(SMALL_BOX_2), 1+r.nextInt(SMALL_BOX_2));
		}
		
		// The finishing touch:
		out.printf("0 0 0%n");
		out.close();
		System.out.println(temp);
		return temp;
	}
	
}
