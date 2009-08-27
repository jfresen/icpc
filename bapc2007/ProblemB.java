package bapc2007;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;


public class ProblemB
{
	
	public static HashMap<Integer, Zone> zones;
	public static HashSet<Zone> start;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable
	{
//		Scanner in = new Scanner(System.in);
		Scanner in = new Scanner(new File("bapc2007/testdata/b.in"));
		int cases = in.nextInt();
		while (cases-- > 0)
		{
			int nz = in.nextInt();
			int nr = in.nextInt();
			zones = new HashMap<Integer, Zone>();
			// Lees zones in.
			for (int i = 0; i < nz; i++)
			{
				Zone z = new Zone(in.nextInt(), in.nextInt(), in);
				zones.put(z.id, z);
			}
			// Lees zones in waar je moet komen.
			start = new HashSet<Zone>();
			for (int i = 0; i < nr; i++)
				for (int j = 0, mri = in.nextInt(); j < mri; j++)
					start.add(zones.get(in.nextInt()));
			for (Zone z : zones.values())
				z.init(start.size());
			
			// Ga nu (maximaal 200 keer) BF doen.
			int i = 0;
			for (Zone currentStart : start)
			{
				LinkedList<Zone> queue = new LinkedList<Zone>();
				currentStart.intree[i] = true;
				queue.add(currentStart);
				while (!queue.isEmpty())
				{
					Zone z = queue.removeFirst();
					// Save maxDistance.
					if (z.maxDistance < z.distance[i])
						z.maxDistance = z.distance[i];
					// Add all subsequent zones to the queue.
					for (int neighbor : z.neighbors)
					{
						Zone next = zones.get(neighbor);
						if (!next.intree[i])
						{
							next.intree[i] = true;
							next.distance[i] = z.distance[i] + 1;
							queue.addLast(next);
						}
					}
				}
				i++;
			}
			
			// Get the min-max from all potential centers.
			int lowestMax = Integer.MAX_VALUE;
			for (Zone z : zones.values())
				if (lowestMax > z.maxDistance)
					lowestMax = z.maxDistance;
			// Isolate all best centers.
			ArrayList<Zone> list = new ArrayList<Zone>();
			for (Zone z : zones.values())
				if (z.maxDistance == lowestMax)
					list.add(z);
			// Get the lowest zonenumber from the best centers.
			Zone[] array = list.toArray(new Zone[0]);
			Arrays.sort(array);
			System.out.println(lowestMax+1 + " " + array[0].id);
		}
	}

}

class Zone implements Comparable<Zone>
{
	public int id;
	public int[] neighbors;
	public boolean[] intree;
	public int[] distance;
	public int maxDistance = Integer.MIN_VALUE;
	
	public Zone(int id, int neighbors, Scanner in)
	{
		this.id = id;
		this.neighbors = new int[neighbors];
		for (int i = 0; i < this.neighbors.length; i++)
			this.neighbors[i] = in.nextInt();
	}
	
	public void init(int n)
	{
		intree = new boolean[n];
		distance = new int[n];
	}
	
	public int compareTo(Zone that)
	{
		return this.id - that.id;
	}
	
	public String toString()
	{
		return id + " -- " + Arrays.toString(distance);
	}
}
