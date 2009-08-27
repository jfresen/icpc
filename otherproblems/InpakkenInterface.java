package otherproblems;

import java.util.ArrayList;
import java.util.Scanner;

public interface InpakkenInterface
{
	
	/**
	 * Solve the problem with the given input and return an array that indicates
	 * for each test if it is possible (<code>true</code>) or not
	 * (<code>false</code>).
	 * 
	 * @param in The input for the problem.
	 * @return An array containing the result of each single test. The array
	 *         must be ordered intuitively, i.e. the first test must be on index
	 *         0, the second on index 1, etc.
	 */
	public ArrayList<Boolean> solve(Scanner in);
	
}
