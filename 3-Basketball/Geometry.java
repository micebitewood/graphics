/*
	Assinment 3
	John Mu
	jm4911@nyu.edu

	Geometry
		public void setMesh(int)
		public Matrix getMatrix()
		private void fillFaces(int, int)
		public void sphere(double, double)
		public void cube()
		public void cylinder(double)
		public void torus(double, double, double, double)
		public void draw(Graphics

*/
import java.awt.*;

public class Geometry
{
	private int mesh = 30;//the number of mesh
	private double vertices[][];
	private int faces[][];
	private Matrix matrix = new Matrix();

	public void setMesh(int mesh)
	{
		this.mesh = mesh;
	}
	//set the precision

	public Matrix getMatrix()
	{
		return matrix;
	}
	//get matrix for transformation

/*
	public void setFaces(int faces[][])
	{
		this.faces = faces;
	}
	//

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
//			System.out.println(faces[j][0] + " " + faces[j][1] + " " + faces[j][2] + " " + faces[j][3]);
			i++;
		}
	}
	//generate faces as a y * x grid

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

	public void draw(Graphics g)
	{
		int face[];
		//for saving a single face

		int point[][] = new int[4][3];
		//for saving the points after projection

		double trans[][] = new double[4][3];
		//for saving the points after transformation

		for(int i = 0; i < faces.length; i++)
		{
			face = faces[i];
			if(face.length == 3)//if rendered with triangles
			{
				matrix.transform(vertices[face[0]], trans[0]);
				matrix.transform(vertices[face[1]], trans[1]);
				matrix.transform(vertices[face[2]], trans[2]);
				//transformation of points by matrix

				ProjectPoint.project(vertices[face[0]], point[0]);
				ProjectPoint.project(vertices[face[1]], point[1]);
				ProjectPoint.project(vertices[face[2]], point[2]);
				//ProjectPoint has a static method project, which project a vertice onto a plane

				g.drawLine(point[0][0], point[0][1], point[1][0], point[1][1]);
				g.drawLine(point[1][0], point[1][1], point[2][0], point[2][1]);
				g.drawLine(point[2][0], point[2][1], point[0][0], point[0][1]);
				//draw the lines
			}
			else//if rendered with rectangles
			{
				matrix.transform(vertices[face[0]], trans[0]);
				matrix.transform(vertices[face[1]], trans[1]);
				matrix.transform(vertices[face[2]], trans[2]);
				matrix.transform(vertices[face[3]], trans[3]);
				//transformation

				ProjectPoint.project(trans[0], point[0]);
				ProjectPoint.project(trans[1], point[1]);
				ProjectPoint.project(trans[2], point[2]);
				ProjectPoint.project(trans[3], point[3]);
				//Projection
				g.drawLine(point[0][0], point[0][1], point[1][0], point[1][1]);
				g.drawLine(point[1][0], point[1][1], point[2][0], point[2][1]);
				g.drawLine(point[2][0], point[2][1], point[3][0], point[3][1]);
				g.drawLine(point[3][0], point[3][1], point[0][0], point[0][1]);
				//draw the lines
			}
		}
	}
}
