package code;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;

import java.lang.Math;
public class SeamCarver {
	   private Color[][] ColorArray;
	   private int width;
	   private int height;
	   private double[][] energy;
	   
	   public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
	   {
		   if (picture == null)
			   throw new java.lang.IllegalArgumentException();
		   width = picture.width();
		   height = picture.height();
		   ColorArray = new Color[width][height];
		   for (int i = 0; i < width; i++)
			   for (int j = 0; j < height; j++)
				   ColorArray[i][j] = picture.get(i, j);
		   energy = new double[width][height];
		   for (int i = 0; i < width; i++)
			   for (int j = 0; j < height; j++)
				   energy[i][j] = energy(i, j);
	   }
	   public Picture picture()                          // current picture
	   {
		   Picture picture = new Picture(width, height);
		   for (int i = 0; i < width; i++)
			   for (int j = 0; j < height; j++)
				   picture.set(i, j, ColorArray[i][j]);
		   return picture;
	   }
	   public     int width()                            // width of current picture
	   {
		   return width;
	   }
	   public     int height()                           // height of current picture
	   {
		   return height;
	   }
	   public  double energy(int x, int y)               // energy of pixel at column x and row y
	   {
		   if (x < 0 || x >= width || y < 0 || y >= height)
			   throw new java.lang.IllegalArgumentException();
		   if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
			   return 1000;
		   else
		   {
			   return Math.sqrt(Delta_x_square(x, y) + Delta_y_square(x, y));
		   }
	   }
	   
	   private int Delta_x_square(int x, int y)
	   {
		   return (int)(Math.pow(ColorArray[x+1][y].getRed()-ColorArray[x-1][y].getRed(), 2) +
				   Math.pow(ColorArray[x+1][y].getBlue()-ColorArray[x-1][y].getBlue(), 2) +
				   Math.pow(ColorArray[x+1][y].getGreen()-ColorArray[x-1][y].getGreen(), 2));
	   }
	   
	   private int Delta_y_square(int x, int y)
	   {
		   return (int)(Math.pow(ColorArray[x][y+1].getRed()-ColorArray[x][y-1].getRed(), 2) +
				   Math.pow(ColorArray[x][y+1].getBlue()-ColorArray[x][y-1].getBlue(), 2) +
				   Math.pow(ColorArray[x][y+1].getGreen()-ColorArray[x][y-1].getGreen(), 2)); 
	   }
	   
	   
	   public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
	   {
		   return horizontalSeam();
	   }
	   
	   public   int[] findVerticalSeam()                 // sequence of indices for vertical seam
	   {
		   return verticalSeam();
	   }
	   
	   private int[] horizontalSeam()
	   {
		   int[][] edgeTo = new int[width][height];
		   double[][] distanceTo = new double[width][height];
		   //initialize the graph
		   for (int j = 0; j < height; j++)
		   {
			   distanceTo[0][j] = 0;
			   for (int i = 1; i < width; i++)
				   distanceTo[i][j] = Double.POSITIVE_INFINITY;
		   }
		   //relax every edge according to topological order
		   //relax every edge pointing from the given vertex
		   for (int i = 0; i < width-1; i++)
			   for (int j = 0; j < height; j++)
			   {
				 //relaex edge to (i+1,j)
				   if (energy[i+1][j] + distanceTo[i][j] < distanceTo[i+1][j])
				   {
					   edgeTo[i+1][j] = 0;
					   distanceTo[i+1][j] = energy[i+1][j] + distanceTo[i][j];
				   }
				   
				 //relax edge to (i+1,j+1)
				   if (j + 1 < height) 
				   {
					   if (energy[i+1][j+1] + distanceTo[i][j] < distanceTo[i+1][j+1])
					   {
						   edgeTo[i+1][j+1] = -1;
						   distanceTo[i+1][j+1] = energy[i+1][j+1] + distanceTo[i][j];
					   }
				   }
				 //relax edge to (i+1,j-1)
				   if (j - 1 >= 0 ) 
				   {
					   if (energy[i+1][j-1] + distanceTo[i][j] < distanceTo[i+1][j-1])
					   {
						   edgeTo[i+1][j-1] = 1;
						   distanceTo[i+1][j-1] = energy[i+1][j-1] + distanceTo[i][j];
					   }
				   }
				   
				   
			   }
		   //find the target of shortest distance in the last row
		   int target = 0;
		   for (int j = 1; j < height; j++)
		   {
			   if (distanceTo[width-1][j] < distanceTo[width-1][target])
				   target = j;
		   }
		   
		   //form the sequence of indices every line
		   int[] sequence = new int[width];
		   sequence[width-1] = target;
		   for (int i = width - 2; i >= 0; i--)
			   sequence[i] = sequence[i+1] + edgeTo[i+1][sequence[i+1]];
		   return sequence;
	   }
	   
	   
	   private int[] verticalSeam()
	   {
		   int[][] edgeTo = new int[width][height];
		   double[][] distanceTo = new double[width][height];
		   //initialize the graph
		   for (int i = 0; i < width; i++)
		   {
			   distanceTo[i][0] = 0;
			   for (int j = 1; j < height; j++)
				   distanceTo[i][j] = Double.POSITIVE_INFINITY;
		   }
		   //relax every edge according to topological order
		   //relax every edge pointing from the given vertex
		   for (int j = 0; j < height-1; j++)
			   for (int i = 0; i < width; i++)
			   {
				 //relaex edge to (i,j+1)
				   if (energy[i][j+1] + distanceTo[i][j] < distanceTo[i][j+1])
				   {
					   edgeTo[i][j+1] = 0;
					   distanceTo[i][j+1] = energy[i][j+1] + distanceTo[i][j];
				   }
				   
				 //relax edge to (i-1,j+1)
				   if (i - 1 >= 0) 
				   {
					   if (energy[i-1][j+1] + distanceTo[i][j] < distanceTo[i-1][j+1])
					   {
						   edgeTo[i-1][j+1] = 1;
						   distanceTo[i-1][j+1] = energy[i-1][j+1] + distanceTo[i][j];
					   }
				   }
				   
				   if (i + 1 < width) //relax edge to (i+1,j+1)
				   {
					   if (energy[i+1][j+1] + distanceTo[i][j] < distanceTo[i+1][j+1])
					   {
						   edgeTo[i+1][j+1] = -1;
						   distanceTo[i+1][j+1] = energy[i+1][j+1] + distanceTo[i][j];
					   }
				   }
				   
				   
			   }
		   //find the target of shortest distance in the last row
		   int target = 0;
		   for (int i = 1; i < width; i++)
		   {
			   if (distanceTo[i][height-1] < distanceTo[target][height-1])
				   target = i;
		   }
		   
		   //form the sequence of indices every line
		   int[] sequence = new int[height];
		   sequence[height-1] = target;
		   for (int j = height - 2; j >= 0; j--)
			   sequence[j] = sequence[j+1] + edgeTo[sequence[j+1]][j+1];
		   return sequence;
	   }

	   public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
	   {
		   //dealing with exception
		   if (seam == null || seam.length != width || height <= 1)
			   throw new java.lang.IllegalArgumentException();
		   for (int i = 0; i < width-1; i++)
			   if ((seam[i] - seam[i+1]) < -1 || (seam[i] - seam[i+1]) > 1 || seam[i] < 0 || seam[i] >= height)
				   throw new java.lang.IllegalArgumentException();
		   if (seam[width-1] < 0 || seam[width-1] >= height)
			   throw new java.lang.IllegalArgumentException();
		   
		   for (int i = 0; i < width; i++)
		   {
			   for (int j = seam[i]; j < height - 1; j++)
				   ColorArray[i][j] = ColorArray[i][j+1];
		   }
		   height--;
		   for (int i = 0; i < width; i++)
		   {
			   if (seam[i] > 0)
				   energy[i][seam[i]-1] = energy(i, seam[i]-1);
			   if (seam[i] <= height - 1)
				   energy[i][seam[i]] = energy(i, seam[i]);
		   }
	   }
	   
	   public    void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
	   {
		   //dealing with exception
		   if (seam == null || seam.length != height || width <= 1)
			   throw new java.lang.IllegalArgumentException();
		   for (int i = 0; i < height-1; i++)
			   if ((seam[i] - seam[i+1]) < -1 || (seam[i] - seam[i+1]) > 1 || seam[i] < 0 || seam[i] >= width)
				   throw new java.lang.IllegalArgumentException();
		   if (seam[height-1] < 0 || seam[height-1] >= width)
			   throw new java.lang.IllegalArgumentException();
		   
		   
		   for (int j = 0; j < height; j++)
		   {
			   for (int i = seam[j]; i < width - 1; i++)
				   ColorArray[i][j] = ColorArray[i+1][j];
		   }
		   width--;
		   for (int j = 0; j < height; j++)
		   {
			   if (seam[j]-1 >= 0)
				   energy[seam[j]-1][j] = energy(seam[j]-1, j);
			   if (seam[j] <= width - 1)
				   energy[seam[j]][j] = energy(seam[j], j);
		   }
	   }
}