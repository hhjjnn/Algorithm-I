

import edu.princeton.cs.algs4.*;
public class PointSET {
	   private SET<Point2D> points = new SET<Point2D>();
	   public         PointSET()                               // construct an empty set of points
	   {
		   points = new SET<Point2D>();
	   }
	   public           boolean isEmpty()                      // is the set empty?
	   {
		   return (points.size() == 0);
	   }
	   public               int size()                         // number of points in the set
	   {
		   return points.size();
	   }
	   public              void insert(Point2D p)              // add the point to the set (if it is not already in the set)
	   {
		   if (p == null)
			   throw new java.lang.IllegalArgumentException();
		   points.add(p);
	   }
	   public           boolean contains(Point2D p)            // does the set contain point p?
	   {
		   if (p == null)
			   throw new java.lang.IllegalArgumentException();
		   return points.contains(p);
	   }
	   public              void draw()                         // draw all points to standard draw
	   {
		   for (Point2D p : points)
			   StdDraw.point(p.x(), p.y());
		   StdDraw.show();
	   }
	   public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
	   {
		   LinkedQueue<Point2D> queue = new LinkedQueue<Point2D>();
		   if (rect == null)
			   throw new java.lang.IllegalArgumentException();
		   for (Point2D p : points)
		   {
			   if (rect.contains(p))
				   queue.enqueue(p);
		   }
		   return queue;
			   
	   }
	   public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
	   {
		   if (points.size() == 0)
			   throw new java.lang.IllegalArgumentException();
		   Point2D neighbor = points.min();
		   for (Point2D point : points)
		   {
			   if (point.distanceSquaredTo(p) < neighbor.distanceSquaredTo(p))
				   neighbor = point;
		   }
		   return neighbor;
	   }
	   public static void main(String[] args)                  // unit testing of the methods (optional)
	   {
		   In in = new In("circle4.txt");

	        StdDraw.enableDoubleBuffering();

	        // initialize the data structures with N points from standard input
	        PointSET brute = new PointSET();
	        while (!in.isEmpty()) {
	            double x = in.readDouble();
	            double y = in.readDouble();
	            Point2D p = new Point2D(x, y);
	            brute.insert(p);
	        }

	        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
	        double x1 = 0.0, y1 = 0.0;      // current location of mouse
	        boolean isDragging = false;     // is the user dragging a rectangle

	        // draw the points
	        StdDraw.clear();
	        StdDraw.setPenColor(StdDraw.BLACK);
	        StdDraw.setPenRadius(0.01);
	        brute.draw();
	        StdDraw.show();

	        while (true) {

	            // user starts to drag a rectangle
	            if (StdDraw.mousePressed() && !isDragging) {
	                x0 = StdDraw.mouseX();
	                y0 = StdDraw.mouseY();
	                isDragging = true;
	                continue;
	            }

	            // user is dragging a rectangle
	            else if (StdDraw.mousePressed() && isDragging) {
	                x1 = StdDraw.mouseX();
	                y1 = StdDraw.mouseY();
	                continue;
	            }

	            // mouse no longer pressed
	            else if (!StdDraw.mousePressed() && isDragging) {
	                isDragging = false;
	            }


	            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
	                                     Math.max(x0, x1), Math.max(y0, y1));
	            // draw the points
	            StdDraw.clear();
	            StdDraw.setPenColor(StdDraw.BLACK);
	            StdDraw.setPenRadius(0.01);
	            brute.draw();

	            // draw the rectangle
	            StdDraw.setPenColor(StdDraw.BLACK);
	            StdDraw.setPenRadius();
	            rect.draw();

	            // draw the range search results for brute-force data structure in red
	            StdDraw.setPenRadius(0.03);
	            StdDraw.setPenColor(StdDraw.RED);
	            for (Point2D p : brute.range(rect))
	                p.draw();

	            StdDraw.show();
	            StdDraw.pause(40);
	        }
	   }
	}