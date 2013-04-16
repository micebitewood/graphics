/*
	Assinment 5
	John Mu
	jm4911@nyu.edu

	This is a scene of a cube rotating at the center, with background moving.
*/
public class MyMISApplet extends MISApplet {

	//for saving the time
    double t = 0;
	
	//this is a cube
	Geometry world = new Geometry();
	Geometry cube = world.add();
	
	//for saving the data in cube, using vertices = getVertices() and faces = getFaces()
	double[][] vertices;
	int[][] faces;
	
	//for saving the points after projection
	class Point
	{
		public int[] xy = new int[2];
		public int[] rgb = new int[3];
		public double zBuffer;
	}
	Point[] point = new Point[4];

	//for saving the points after transformation
	double[][] trans = new double[4][3];
	
	//for projecting the points in vertices to point
	Projection projection = new Projection();

    public void initFrame(double time) {
	
		//save the time in t
		t = 3 * time;
		for(int i = 0; i < 4; i++)		
			point[i] = new Point();
	   
    }
    public void setPixel(int x, int y, int rgb[]) {
	
		//play around, I'm sorry that might make you dizzy
		double r = Math.sqrt((double)( (x - W / 2) * (x - W / 2) + (y - H / 2) * (y - H / 2)));
		for(int i = 0; i < 3; i++)
		{
			rgb[i] = (int) (50 * (Math.sin(t) + 2) * (Math.sin(t + r) + 1 + i));
		}
		
    }
	
	public void drawFace()
	{
		//for saving a single face
		int face[];
		
		for(int i = 0; i < faces.length; i++)
		{
			face = faces[i];
			
			//Projection
			for(int j = 0; j < face.length; j++)
			{
				cube.getMatrix().transform(vertices[face[j]], trans[j]);
				point[j].xy[0] = W / 2 + (int)(H * trans[0]);
				point[j].xy[1] = H / 2 + (int)(H * trans[1]);
			}
			
			double area = 0;
			
			//calculate the area
			for(int j = 0; j < face.length; j++)
				area += (point[j][0] - point[(j + 1) % face.length][0]) * (point[j][1] + point[(j + 1) % face.length][1]) / 2.0;
			//if it's larger than zero, it means the face is in the back
			if(area > 0)
				continue;
			
			//First, let's deal with the triangle point[0], point[1], point[2]
			//If there is a D existing
			split(point[0], point[1], point[2]);

			//Second, let's deal with the triangle point[2], point[3], point[0]
			//If there is a D existing
			split(point[2], point[3], point[0]);
		}
	}
	public void split(int[] pointA, int[] pointB, int[] pointC)
	{
		//A is the top point
		//B is the middle point
		//C is the bottom point
		int[] A;
		int[] B;
		int[] C;
		int[] D = {0, 0};
//--------------------------------------Part 1----------------------------------------------
		int min = pointA[1];
		A = pointA;
		int mid;
		if(min > pointB[1])
		{
			mid = min;
			min = pointB[1];
			A = pointB;
			B = pointA;
		}
		else
		{
			mid = pointB[1];
			B = pointB;
		}
		if(min > pointC[1])
		{
			C = B;
			B = A;
			A = pointC;
		}
		else if(mid > pointC[1])
		{
			C = B;
			B = pointC;
		}
		else
			C = pointC;
		
//----------------------------Part 2------------------------------------

		//if D exists
		if(A[1] != B[1] && B[1] != C[1])
		{
			//xD = xA + (yB - yA) * (xC - xA) / (yC - yA)
			D[0] = A[0] + (int)((B[1] - A[1]) * (C[0] - A[0]) / (C[1] - A[1]));
			//yD = yB
			D[1] = B[1];

			draw(A, B, D, true);
			draw(B, D, C, false);
		}
		//if not
		else
			draw(A, B, C, B[1] == C[1]);
	}
	
	/*
		if(b): two cases
			A
		   / \
		  /   \
		 /	   \
		B-------C
	-------or-------
			A
		   / \
		  /   \
		 /	   \
		C-------B
--------------------------------------------------------------
		if(!b): two cases
		A-------B
		 \	   /
		  \   /
		   \ /
			C
	--------or--------
		B-------A
		 \     /
		  \   /
		   \ /
			C
	*/
	public void draw(int[] A, int[] B, int[] C, boolean b)
	{
		if(b)
		{
			if(B[0] > C[0])
				drawTrapezoid(A, A, C, B);
			else
				drawTrapezoid(A, A, B, C);
		}
		else
		{
			if(A[0] > B[0])
				drawTrapezoid(B, A, C, C);
			else
				drawTrapezoid(A, B, C, C);
		}
	}

	public void drawTrapezoid(int[] LT, int[] RT, int[] LB, int[] RB)
	{
		for(int y = LT[1]; y <= LB[1]; y++)
		{
			double t = (y - LT[1]) * 1.0 / (LB[1] - LT[1]);
			int xL = (int) (LT[0] + t * (LB[0] - LT[0]));
			int xR = (int) (RT[0] + t * (RB[0] - RT[0]));
			for(int x = xL; x <= xR; x++)
			{
				pix[y * W + x] = pack(255, 0, 0);
			}
		}
	}
	
	private int rgb[] = new int[3];
	public void computeImage(double time)
	{
		initFrame(time);
		
		cube.cube();
		cube.getMatrix().identity().rotateY(time).rotateX(time);
		
		projection.setWidth(W);
		projection.setHeight(H);
		projection.setFL(10);
		
		vertices = cube.getVertices();
		faces = cube.getFaces();

		//paint the background
		for(int y = 0; y < H; y++)
			for(int x = 0; x < W; x++)
			{
				setPixel(x, y, rgb);
				pix[x + y * W] = pack(rgb[0], rgb[1], rgb[2]);
			}
		//paint the cube
		drawFace();
	}
}
