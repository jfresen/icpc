package tcr.meetkunde;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizer
{

	public static JFrame showConvexHull(Point[] points, Point[] hull)
	{
		return showWindow("Convex Hull", new ConvexHullPanel(points, hull));
	}
	
	public static JFrame showYMonotonePolygon(Triangulation.Point[] points, List<Triangulation.Point> monotones)
	{
		return showWindow("Polygon Monotonation", new MonotonationPanel(points, monotones));
	}
	
	public static JFrame showTriangulation(Point[] triangles)
	{
		return showWindow("Polygon Triangulation", new TriangulationPanel(triangles));
	}
	
	public static void main(String[] args)
	{
		showDelaunayTryoutPanel();
	}
	
	public static JFrame showDelaunayTriangulation(Triangle[] t, Point[] p)
	{
		return showWindow("Delaunay Triangulation", new DelaunayTriangulationPanel(t, p));
	}
	
	public static JFrame showDelaunayTryoutPanel()
	{
		return showWindow("Delaunay Tryout", new DelaunayTryOutPanel());
	}
	
	private static JFrame showWindow(String title, JPanel panel)
	{
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		
		Dimension scr = frame.getToolkit().getScreenSize();
		Dimension win = frame.getSize();
		frame.setLocation(scr.width/2 - win.width/2, scr.height/2 - win.height/2);
		
		return frame;
	}
	
	private static class ConvexHullPanel extends JPanel
	{
		private static final long serialVersionUID = 976226483047884696L;
		private Point[] points;
		private Point[] hull;
		public ConvexHullPanel(Point[] p, Point[] h)
		{
			points = p;
			hull = h;
			setPreferredSize(new Dimension(400, 400));
		}
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			for (Point p : points)
				g.fillOval(p.igetX()-2, p.igetY()-2, 5, 5);
			int[] x = new int[hull.length];
			int[] y = new int[hull.length];
			for (int i = 0; i < hull.length; i++)
			{
				x[i] = hull[i].igetX();
				y[i] = hull[i].igetY();
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
		private Point[] triangles;
		public TriangulationPanel(Point[] t)
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
				x[i] = triangles[i].igetX();
				y[i] = triangles[i].igetY();
			}
			int[] z = {3, 0, 0};
			for (int i = 0, j = 1; i < n; i++, j++)
				g.drawLine(x[i], y[i], x[j-z[j%3]], y[j-z[j%3]]);
		}
	}
	
	private static class DelaunayTriangulationPanel extends JPanel
	{
		private static final long serialVersionUID = -6821370679374902488L;
		private Triangle[] triangles;
		private Point[] points;
		double scalex, scaley;
		int lx, ly, ux, uy;
		public DelaunayTriangulationPanel(Triangle[] t, Point[] p)
		{
			triangles = t;
			points = p;
			lx = 0; ly = 0;
			ux = 0; uy = 0;
			for (Point point : points)
			{
				if (lx > point.igetX()) lx = point.igetX();
				if (ly > point.igetY()) ly = point.igetY();
				if (ux < point.igetX()) ux = point.igetX();
				if (uy < point.igetY()) uy = point.igetY();
			}
			if (ux-lx < 10) {lx -= (10-ux+lx)/2; ux = lx+10;}
			if (uy-ly < 10) {ly -= (10-uy+ly)/2; uy = ly+10;}
			scalex = 320.0/(ux-lx);
			scaley = 320.0/(uy-ly);
			setPreferredSize(new Dimension(400, 400));
		}
		@Override
		protected void paintComponent(Graphics g1)
		{
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D)g1;
			g.setColor(new Color(192, 0, 0, 128));
			for (Point p : points)
			{
				int x = (int)((p.getX()-lx)*scalex + 40 - 2*scalex);
				int y = (int)((p.getY()-ly)*scaley + 40 - 2*scaley);
				g.fillOval(x, y, (int)(4*scalex), (int)(4*scaley));
			}
			g.setColor(Color.BLACK);
			for (Triangle t : triangles)
			{
				int ax = (int)((t.get(0).getX()-lx)*scalex+40);
				int ay = (int)((t.get(0).getY()-ly)*scaley+40);
				int bx = (int)((t.get(1).getX()-lx)*scalex+40);
				int by = (int)((t.get(1).getY()-ly)*scaley+40);
				int cx = (int)((t.get(2).getX()-lx)*scalex+40);
				int cy = (int)((t.get(2).getY()-ly)*scaley+40);
				g.drawLine(ax, ay, bx, by);
				g.drawLine(bx, by, cx, cy);
				g.drawLine(cx, cy, ax, ay);
			}
			g.setColor(Color.GREEN);
			int x = (int)(-lx*scalex + 40 - scalex);
			int y = (int)(-ly*scaley + 40 - scaley);
			g.fillOval(x, y, (int)(2*scalex), (int)(2*scaley));
		}
	}
	
	private static class DelaunayTryOutPanel extends JPanel
	{
		private static final long serialVersionUID = -780411250139814483L;
		Point[] p = new Point[5];
		Point selected;
		public DelaunayTryOutPanel()
		{
			p = new Point[]
			{
				new Point(100, 100),
				new Point(50, 200),
				new Point(75, 265),
				new Point(100, 300),
				new Point(115, 300),
			};
			setPreferredSize(new Dimension(400, 400));
			addMouseListener(new MouseListener()
			{
				@Override public void mouseClicked(MouseEvent e) {}
				@Override public void mouseEntered(MouseEvent e) {}
				@Override public void mouseExited(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e)
				{
					double dist = Double.MAX_VALUE, d;
					Point b = new Point(e.getPoint().x, e.getPoint().y);
					for (Point a : p)
						if (dist > (d = (a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y)))
						{
							dist = d;
							selected = a;
						}
					if (dist > 49)
						selected = null;
				}
				@Override
				public void mouseReleased(MouseEvent e)
				{
					for (Point pt : p)
						System.out.println("("+pt.x+","+pt.y+")");
					System.out.println("=========");
					selected = null;
				}
			});
			addMouseMotionListener(new MouseMotionListener()
			{
				@Override
				public void mouseDragged(MouseEvent e)
				{
					if (selected == null)
						return;
					selected.x = e.getX();
					selected.y = e.getY();
					repaint();
				}
				@Override public void mouseMoved(MouseEvent e) {}
			});
		}
		@Override
		protected void paintComponent(Graphics g1)
		{
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D)g1;
			g.drawLine(p[0].x, p[0].y, p[1].x, p[1].y);
			g.drawLine(p[0].x, p[0].y, p[3].x, p[3].y);
			g.drawLine(p[0].x, p[0].y, p[4].x, p[4].y);
			g.drawLine(p[1].x, p[1].y, p[2].x, p[2].y);
			g.drawLine(p[1].x, p[1].y, p[3].x, p[3].y);
			g.drawLine(p[2].x, p[2].y, p[3].x, p[3].y);
			g.drawLine(p[3].x, p[3].y, p[4].x, p[4].y);
			drawCircumcircle(g, p[0], p[1], p[3]);
			drawCircumcircle(g, p[1], p[2], p[4]);
//			g.drawOval(x, y, width, height)
			for (Point point : p)
				g.fillOval(point.x-2, point.y-2, 5, 5);
		}
		
		private void drawCircumcircle(Graphics2D g, Point pa, Point pb, Point pc)
		{
			long bx = pb.x-pa.x;
			long by = pb.y-pa.y;
			long cx = pc.x-pa.x;
			long cy = pc.y-pa.y;
			long d = 2*(bx*cy - by*cx);
			long bxby = bx*bx + by*by;
			long cxcy = cx*cx + cy*cy;
			double x = (cy*bxby - by*cxcy) / d + pa.x;
			double y = (bx*cxcy - cx*bxby) / d + pa.y;
			double r = Math.sqrt((pa.x-x)*(pa.x-x) + (pa.y-y)*(pa.y-y));
			g.drawOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
		}
		
		private static class Point
		{
			int x, y;
			public Point(int x, int y)
			{this.x=x; this.y=y;}
		}
	}
	
}
