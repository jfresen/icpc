package tcr.meetkunde;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizer
{

	public static void showConvexHull(ConvexHull.Point[] points, ConvexHull.Point[] hull)
	{
		JPanel panel = new ConvexHullPanel(points, hull);
		showWindow("Convex Hull", panel);
	}
	
	public static void showYMonotonePolygon(Triangulation.Point[] points, List<Triangulation.Point> monotones)
	{
		showWindow("Polygon Monotonation", new MonotonationPanel(points, monotones));
	}
	
	public static void showTriangulation(Triangulation.Point[] triangles)
	{
		showWindow("Polygon Triangulation", new TriangulationPanel(triangles));
	}
	
	private static void showWindow(String title, JPanel panel)
	{
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		
		Dimension scr = frame.getToolkit().getScreenSize();
		Dimension win = frame.getSize();
		frame.setLocation(scr.width/2 - win.width/2, scr.height/2 - win.height/2);
	}
	
	private static class ConvexHullPanel extends JPanel
	{
		private static final long serialVersionUID = 976226483047884696L;
		private ConvexHull.Point[] points;
		private ConvexHull.Point[] hull;
		public ConvexHullPanel(ConvexHull.Point[] p, ConvexHull.Point[] h)
		{
			points = p;
			hull = h;
			setPreferredSize(new Dimension(400, 400));
		}
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			for (ConvexHull.Point p : points)
				g.fillOval((int)p.x-2, (int)p.y-2, 5, 5);
			int[] x = new int[hull.length];
			int[] y = new int[hull.length];
			for (int i = 0; i < hull.length; i++)
			{
				x[i] = (int)hull[i].x;
				y[i] = (int)hull[i].y;
			}
			g.drawPolygon(x, y, hull.length);
		}
	}

	private static class MonotonationPanel extends JPanel
	{
		private static final long serialVersionUID = -7401152458083787649L;
		private Triangulation.Point[] points;
		private List<Triangulation.Point> monotones;
		private int seed;
		public MonotonationPanel(Triangulation.Point[] p, List<Triangulation.Point> m)
		{
			points = p;
			monotones = m;
			seed = (int)(Math.random()*10000);
			setPreferredSize(new Dimension(400, 400));
		}
		@Override
		protected void paintComponent(Graphics g1)
		{
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D)g1;
			g.scale(1, -1);
			Random r = new Random(seed);
			int n = points.length;
			int[] x = new int[n];
			int[] y = new int[n];
			for (Triangulation.Point p : monotones)
			{
				Triangulation.Point s = p;
				int i;
				for (i = 0; i == 0 || p != s; p = p.next, i++)
				{
					x[i] = (int)p.x;
					y[i] = (int)p.y;
				}
				g.setColor(new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
				g.fillPolygon(x, y, i);
				g.setColor(Color.BLACK);
				g.drawPolygon(x, y, i);
			}
			g.setColor(Color.BLACK);
			for (Triangulation.Point p : points)
				g.fillOval((int)p.x-2, (int)p.y-2, 5, 5);
			for (int i = 0; i < n; i++)
				g.drawLine((int)points[i].x, (int)points[i].y,
				           (int)points[(i+1)%n].x, (int)points[(i+1)%n].y);
		}
		
	}
	
	private static class TriangulationPanel extends JPanel
	{
		private static final long serialVersionUID = -6821370679374902488L;
		private Triangulation.Point[] triangles;
		public TriangulationPanel(Triangulation.Point[] t)
		{
			triangles = t;
			setPreferredSize(new Dimension(400, 400));
		}
		@Override
		protected void paintComponent(Graphics g1)
		{
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D)g1;
			g.scale(1, -1);
			int n = triangles.length;
			int[] x = new int[n];
			int[] y = new int[n];
			for (int i = 0; i < n; i++)
			{
				x[i] = (int)triangles[i].x;
				y[i] = (int)triangles[i].y;
			}
			int[] z = {3, 0, 0};
			for (int i = 0, j = 1; i < n; i++, j++)
				g.drawLine(x[i], y[i], x[j-z[j%3]], y[j-z[j%3]]);
		}
	}
	
}
