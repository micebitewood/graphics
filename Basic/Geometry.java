/*
	Assinment 4
	John Mu
	jm4911@nyu.edu

	Geometry
		public void setMesh(int)
		private void fillFaces(int, int)
		public void sphere(double, double)
		public void cube()
		public void cylinder(double)
		public void torus(double, double, double, double)
		public void draw(Graphics)
----------------new methods----------------------------------------------------
		public Geometry add()
		public Geometry getChild(int)
		public Matrix getMatrix()
		public int getNumChildren()
		public void remove(Geometry)

*/
import java.awt.*;
import java.util.*;

public class Geometry
{
	private int mesh = 30;//the number of mesh
	private double vertices[][];
	private int faces[][];
	private Matrix globalMatrix;//save the parent matrix
	private Matrix localMatrix;//save the local matrix
	private ArrayList<Geometry> children;//an arraylist of children

	public Geometry()
	{
		globalMatrix = new Matrix();
		globalMatrix.identity();

		localMatrix = new Matrix();
		localMatrix.identity();
		children = new ArrayList<Geometry>();
	}
	//constructor

	public Geometry add()
	{
		Geometry child = new Geometry();
		children.add(child);
		return child;
	}
	//add a child and return it

	public Geometry getChild(int i)
	{
		if(children.size() >= i)
		{
			return children.get(i);
		}
		else
		{
			System.out.println("The child you get doesn't exist, replaced by getting the last child");
			return children.get(children.size() - 1);
		}
	}
	//get the ith child, if i is larger than the size of the arraylist, print an error message

	public int getNumChildren()
	{
		return children.size();
	}
	//get the size of children

	public void remove(Geometry geometry)
	{
		if(children.indexOf(geometry) != -1)
			children.remove(geometry);
		else
			System.out.println("the child you wanna remove doesn't exist");
	}
	//remove a child, if it doesn't exist, print an error message

	public Matrix getGlobalMatrix()
	{
		return globalMatrix;
	}
	//return the globalMatrix

	public void draw(Graphics g, Projection projection)
	{
		int face[];
		//for saving a single face

		int point[][] = new int[4][3];
		//for saving the points after projection

		double trans[][] = new double[4][3];
		//for saving the points after transformation
		
		if(vertices == null)
		{
			for(Geometry child: children)
			{
				child.getGlobalMatrix().identity().rightMultiply(this.globalMatrix).rightMultiply(child.getMatrix());
				child.draw(g, projection);
			}
			return;
		}
		//a joint has no "real" geometry, and a "real" geometry has no children. We should identify first whether this is a "real" one or not.
		//if vertices has not been newed, this is not a "real" geometry.
		for(int i = 0; i < faces.length; i++)
		{
			face = faces[i];
			for(int j = 0; j < face.length; j++)
			{
				globalMatrix.transform(vertices[face[j]], trans[j]);
				projection.project(trans[j], point[j]);
			}
			for(int j = 0; j < face.length; j++)
			{
				int k = j + 1;
				k %= face.length;
				g.drawLine(point[j][0], point[j][1], point[k][0], point[k][1]);
			}
		}
	}
	//this is a modified version of draw(g), here we have another param Prejection, with which we can easily modify the height, width, and FL with an instance of Projection.
	//instead of using the localMatrix for projection, we now use the globalMatrix.
	//Instead of the old duplicated version, now we have a precise one which utilizes iterations.

//------------------------old methods--------------------------------------------

	public void setMesh(int mesh)
	{
		this.mesh = mesh;
	}
	//set the precision

	public Matrix getMatrix()
	{
		return localMatrix;
	}
	//get matrix for transformation

	public void setMatrix(Matrix matrix)
	{
		this.localMatrix = matrix;
	}

/*
	public void setFaces(int faces[][])
	{
		this.faces = faces;
	}

	public void setVertices(double vertices[][])
	{
		this.vertices = vertices;
	}

	public double[][] getFaces()
	{
		return faces;
	}

	public int[][] getVertices()
	{
		return vertices;
	}
*/

	private void fillFaces(int x, int y)
	{
		int i = 0;
		for(int j = 0; j < x * y; j++)
		{
			if(i % (y + 1) == y)
				i++;
			faces[j][0] = i;
			faces[j][1] = i + 1;
			faces[j][2] = i + y + 2;
			faces[j][3] = i + y + 1;
			i++;
		}
	}
	//generate faces as a y * x grid

	public void sphere()
	{
		this.sphere(2 * Math.PI, Math.PI / 2);
	}

	public void sphere(double u, double v)//create (a part of) a sphere, 0 <= u <= 2 * pi, -pi / 2 <= v <= pi / 2
	{
		vertices = new double[(mesh + 1) * (mesh + 1)][3];
		faces = new int[mesh * mesh][4];

		int x = mesh;
		int y = mesh;
		//initialization of parameters of fillFaces(x, y)

		int loc = 0;
		//the location of vertice

		for(int k = 0; k <= mesh; k++)
		{
			double theta = 2 * k * Math.PI / mesh;

			if(theta > u)
			{
				x = k - 1;
				break;
			}
			//save x if current theta > u, then stop here

			for(int j = 0; j <= mesh; j++)
			{
				double phi = j * Math.PI / mesh - Math.PI / 2;

				if(phi > v)
				{
					y = j - 1;
					break;
				}
				//save y if current phi > v, then stop here

				vertices[loc][0] = Math.cos(theta) * Math.cos(phi);
				vertices[loc][1] = Math.sin(theta) * Math.cos(phi);
				vertices[loc][2] = Math.sin(phi);
				//calculation of the coordinate of the point

//				System.out.println(vertices[loc][0] + " " + vertices[loc][1] + " " + vertices[loc][2]);
				loc++;
			}
		}
		fillFaces(x, y);
	}

	public void cube()
	{
		double[][] v = {
		{-1, -1, 1}, {1, -1, 1}, {1, -1, -1}, {-1, -1, -1},
		{-1, -1, -1}, {1, -1, -1}, {1, 1, -1}, {-1, 1, -1},
		{-1, -1, -1}, {-1, 1, -1}, {-1, 1, 1}, {-1, -1, 1},
		{-1, -1, 1}, {1, -1, 1}, {1, 1, 1}, {-1, 1, 1},
		{1, -1, 1}, {1, -1, -1}, {1, 1, -1}, {1, 1, 1},
		{-1, 1, 1}, {1, 1, 1}, {1, 1, -1}, {-1, 1, -1}};
		int[][] f = {
		{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, 
		{12, 13, 14, 15}, {16, 17, 18, 19}, {20, 21, 22, 23}};
		//this is an enumeration

		vertices = v;
		faces = f;
	}

	public void cylinder()
	{
		this.cylinder(Math.PI * 2);
	}
	public void cylinder(double u)//0 <= u <= 2 * pi
	{
		int x = mesh;
		int y = mesh;

		vertices = new double[(mesh + 1) * (mesh + 1)][3];
		faces = new int[mesh * mesh][4];

		int loc = 0;
		//same with sphere

		for(int k = 0; k <= mesh; k++)
		{
			double theta = 2 * Math.PI * k / mesh;

			if(theta > u)
			{
				x = k -1;
				break;
			}
			//same with sphere

			for(int j = 0; j <= mesh; j++)
			{
				double z = 2.0 * j /mesh - 1;

				vertices[loc][0] = Math.cos(theta);
				vertices[loc][1] = Math.sin(theta);
				vertices[loc][2] = z;
				//same with sphere

				loc++;
			}
		}
		fillFaces(x, y);
	}

	public void torus(double R, double r)
	{
		this.torus(2 * Math.PI, 2 * Math.PI, R, r);
	}
	public void torus(double u, double v, double R, double r)//0 <= u <= 2 * pi, 0 <= v <= 2 * pi
	{
		int x = mesh;
		int y = mesh;

		vertices = new double[(mesh + 1) * (mesh + 1)][3];
		faces = new int[mesh * mesh][4];

		int loc = 0;
		//same with sphere

		for(int k = 0; k <= mesh; k++)
		{
			double theta = 2 * Math.PI * k / mesh;

			if(theta > u)
			{
				x = k -1;
				break;
			}
			//same with sphere

			for(int j = 0; j <= mesh; j++)
			{
				double phi = 2 * j * Math.PI /mesh;
				
				if(phi > v)
				{
					y = j - 1;
					break;
				}

				vertices[loc][0] = Math.cos(theta) * (R + r * Math.cos(phi));
				vertices[loc][1] = Math.sin(theta) * (R + r * Math.cos(phi));;
				vertices[loc][2] = r * Math.sin(phi);
				//same with sphere

				loc++;
			}
		}
		fillFaces(x, y);
	}

}