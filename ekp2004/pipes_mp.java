package ekp2004;

import java.io.*;
import java.util.*;

class Layer
{
	int	cost;
	int	frontier[];
	int	frontierCode;

	Layer(int f[], int c)
	{
		cost = c;
		frontier = new int[f.length];
		frontierCode = 0;

		for (int i = 0; i < frontier.length; i++)
			frontier[i] = f[i];

		for (int i = 0; i < frontier.length; i++)
		{
			if ((frontier[i] != pipes_mp.NO_PIPE) && (frontier[i] != pipes_mp.NEW_PIPE))
			{
				if (i < frontier[i])
					frontierCode += (1 << (2 * i));
				else
					frontierCode += (2 << (2 * i));
			}
		}
	}

	@Override
	public int hashCode()
	{
		return frontierCode;
	}

	@Override
	public String toString()
	{
		return "Layer " + frontierCode + " with cost " + cost;
	}

	@Override
	public boolean equals(Object o)
	{
		Layer l = (Layer)o;
		return l.frontierCode == frontierCode;
	}
}

public class pipes_mp
{
	static int		MAX_SIZE		= 10;
	static int		MAX_TABLE_SIZE	= 1024 * 1024;

	static int		NO_PIPE			= -2;
	static int		NEW_PIPE		= -1;

	static int		INFINITE_COST	= 1000000;

	static int		lr_costs[][];
	static int		ud_costs[][];

	static boolean	connected;
	static int		lower_row;

	static int		nbr_cols;
	static int		nbr_rows;

	static HashMap	old_costs;
	static HashMap	new_costs;

	public static void main(String argv[]) throws Exception
	{
		long start = System.currentTimeMillis();
		int n;
		char ch;
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader br = new BufferedReader(new FileReader("ekp2004\\sampledata\\g.in"));

		lr_costs = new int[MAX_SIZE][MAX_SIZE];
		ud_costs = new int[MAX_SIZE][MAX_SIZE];

		n = Integer.parseInt(br.readLine());

		for (int i = 0; i < n; i++)
		{
			StringTokenizer tok = new StringTokenizer(br.readLine(), " ");

			nbr_rows = Integer.parseInt(tok.nextToken());
			nbr_cols = Integer.parseInt(tok.nextToken());

			old_costs = new HashMap();
			new_costs = new HashMap();

			int fr[] = new int[nbr_cols];
			for (int j = 0; j < nbr_cols; j++)
				fr[j] = NO_PIPE;
			Layer null_layer = new Layer(fr, 0);

			br.readLine();

			for (int row = 0; row < nbr_rows; row++)
			{
				String str = br.readLine();

				for (int col = 0; col < nbr_cols - 1; col++)
				{
					ch = str.charAt(col * 2 + 2);

					if ('0' <= ch && ch <= '9')
						lr_costs[row][col] = (int)(ch - '0');
					else
						throw (new Exception());
				}

				str = br.readLine();

				if (row < nbr_rows - 1)
				{
					for (int col = 0; col < nbr_cols; col++)
					{
						ch = str.charAt(2 * col + 1);

						if ('0' <= ch && ch <= '9')
							ud_costs[row][col] = (int)(ch - '0');
						else
							throw (new Exception());
					}
				}
			}

			// Init first lower layer
			int frontier[] = new int[nbr_cols];

			for (int j = 0; j < nbr_cols; j++)
				frontier[j] = NO_PIPE;

			lower_row = -1;
			connected = false;
			extend_layer(frontier, 0, 0, NO_PIPE);

			for (lower_row = 0; lower_row < nbr_rows - 1; lower_row++)
			{
				old_costs = new_costs;
				new_costs = new HashMap();

				Iterator it = old_costs.values().iterator();
				while (it.hasNext())
				{
					Layer layer = (Layer)it.next();
					extend_layer(layer.frontier, 0, layer.cost, NO_PIPE);
				}
			}

			Layer layer = (Layer)new_costs.get(null_layer);
			System.out.println(layer.cost);
		}
		System.out.println("Time: "+(System.currentTimeMillis()-start));
	}

	static void extend_layer(int f[], int col, int cost, int from_left)
	{
		int frontier[] = new int[nbr_cols];

		for (int i = 0; i < nbr_cols; i++)
			frontier[i] = f[i];

		if (col == nbr_cols)
		{
			if (from_left == NO_PIPE)
			{
				Layer layer = new Layer(frontier, cost);
				Layer layer2 = (Layer)new_costs.get(layer);
				if (layer2 != null)
				{
					if (cost < layer2.cost)
						layer2.cost = cost;
				}
				else
					new_costs.put(layer, layer);
			}
			else
				return;
		}
		else if (from_left == NO_PIPE)
		{
			if (frontier[col] == NO_PIPE)
			{
				// UR pipe
				frontier[col] = NEW_PIPE; // REMOVE sätts senare
				extend_layer(frontier, col + 1, cost, col);
			}
			else
			{
				cost += ud_costs[lower_row][col];

				// DU pipe
				frontier[col] = frontier[col]; // REMOVE
				extend_layer(frontier, col + 1, cost, NO_PIPE);

				// DR pipe
				int tmp = frontier[col];
				frontier[col] = NO_PIPE;
				extend_layer(frontier, col + 1, cost, tmp);
			}
		}
		else
		{
			cost += lr_costs[lower_row + 1][col - 1];

			if (frontier[col] == NO_PIPE)
			{
				// LR pipe
				frontier[col] = NO_PIPE; // REMOVE
				extend_layer(frontier, col + 1, cost, from_left);

				// LU pipe
				frontier[col] = from_left;
				frontier[from_left] = col;
				extend_layer(frontier, col + 1, cost, NO_PIPE);
			}
			else
			{
				cost += ud_costs[lower_row][col];

				// DL pipe
				if (from_left != col)
				{
					frontier[from_left] = frontier[col];
					frontier[frontier[col]] = from_left;
					frontier[col] = NO_PIPE;
					extend_layer(frontier, col + 1, cost, NO_PIPE);
				}
				else if ((lower_row + 2 == nbr_rows) && !connected)
				{
					connected = true;
					frontier[col] = NO_PIPE;
					extend_layer(frontier, col + 1, cost, NO_PIPE);
					connected = false;
				}
			}
		}
	}
}
