/*
	Assinment 6
	John Mu
	jm4911@nyu.edu

*/
public class MyMISApplet extends MISApplet {

	//for saving the time
    double t = 0;

	double[][] zBuffer;
	double FL = 10;
	
	//Actually, I cannot apply a tree structure currently, because the drawing function is out of Geometry class.
	//So it's a fake tree structure, still need figure it out.
	Geometry world = new Geometry();
	Geometry sphere = world.add();
	Geometry cube = world.add();
	Geometry cylinder = world.add();
	Geometry torus = world.add();
	
	//for saving the data in cube, using vertices = getVertices() and faces = getFaces()
	double[][] vertices;
	int[][] faces;
	
	//for saving the points, better encapsulate them
	class Point
	{
		public int[] xy = new int[2];
		public int[] rgb = new int[3];
		public double zBuffer;
	}

	Point[] point = new Point[4];

	Point D = new Point();

	//for saving the points after transformation
	double[] trans = new double[6];
	
	//run at the beginning before anything happens
	public void initialize()
	{
		for(int i = 0; i < 4; i++)
			point[i] = new Point();
		zBuffer = new double[H][W];
	}

	//run at the beginning of every frame
    public void initFrame(double time) {
	
		//save the time in t
		t = 3 * time;

		//refresh the frame
		for(int x = 0; x < H; x++)
			for(int y = 0; y < W; y++)
				pix[y * W + x] = pack(255, 255, 255);

		//initialization of zBuffer
		for(int i = 0; i < H; i++)
			for(int j = 0; j < W; j++)
				zBuffer[i][j] = -FL;
    }

    public void setPixel(int x, int y, int rgb[]) {
	
    }
	
	public void drawFace(Geometry geo)
	{
		//for saving a single face
		int face[];
		
		for(int i = 0; i < faces.length; i++)
		{
			face = faces[i];
			
			//Projection
			for(int j = 0; j < face.length; j++)
			{
				geo.getMatrix().transform(vertices[face[j]], trans);

				point[j].xy[0] = W / 2 + (int)(H * trans[0] / FL);
				point[j].xy[1] = H / 2 - (int)(H * trans[1] / FL);
				//zBuffer is actually z
				//rgb is calculated with norm
				point[j].zBuffer= trans[2];
				point[j].rgb[0] = (int)((trans[3] + 1) * 255 / 2);
				point[j].rgb[1] = (int)((trans[4] + 1) * 255 / 2);
				point[j].rgb[2] = (int)((trans[5] + 1) * 255 / 2);
			}
			
			
			//First, let's deal with the triangle point[0], point[1], point[2]
			split(point[0], point[1], point[2]);

			//Second, let's deal with the triangle point[2], point[3], point[0]
			split(point[0], point[2], point[3]);
		}
	}

	/*
		D stores the vertice of D, and BD divides the triangle into two pieces
	*/
	public void split(Point pointA, Point pointB, Point pointC)
	{
		//A is the top point
		//B is the middle point
		//C is the bottom point
		Point A;
		Point B;
		Point C;
//--------------------------------------Part 1----------------------------------------------
		int min = pointA.xy[1];
		A = pointA;
		int mid;
		if(min > pointB.xy[1])
		{
			mid = min;
			min = pointB.xy[1];
			A = pointB;
			B = pointA;
		}
		else
		{
			mid = pointB.xy[1];
			B = pointB;
		}
		if(min > pointC.xy[1])
		{
			C = B;
			B = A;
			A = pointC;
		}
		else if(mid > pointC.xy[1])
		{
			C = B;
			B = pointC;
		}
		else
			C = pointC;
		
//----------------------------Part 2------------------------------------

		//if D exists
		if(A.xy[1] != B.xy[1] && B.xy[1] != C.xy[1])
		{
			//xD = xA + (yB - yA) * (xC - xA) / (yC - yA)
			D.xy[0] = A.xy[0] + (int)((B.xy[1] - A.xy[1]) * (C.xy[0] - A.xy[0]) / (C.xy[1] - A.xy[1]));
			D.zBuffer = A.zBuffer + (B.xy[1] - A.xy[1]) * (C.zBuffer - A.zBuffer) / (C.xy[1] - A.xy[1]);
			for(int i = 0; i < 3; i++)
				D.rgb[i] = A.rgb[i] + (B.xy[1] - A.xy[1]) * (C.rgb[i] - A.rgb[i]) / (C.xy[1] - A.xy[1]);
			//yD = yB
			D.xy[1] = B.xy[1];

			draw(A, B, D, true);//true means it's a normal triangle
			draw(B, D, C, false);//false means it's a triangle upside down
		}
		//if not
		else
			draw(A, B, C, B.xy[1] == C.xy[1]);//if true, a normal one; else, upside down
	}

//------------------------------------------fill triangle----------------------------------------------------------------------------------------------------------	


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
	public void draw(Point A, Point B, Point C, boolean b)
	{
		if(b)
		{
			if(B.xy[0] > C.xy[0])
				drawTrapezoid(A, A, C, B);
			else
				drawTrapezoid(A, A, B, C);
		}
		else
		{
			if(A.xy[0] > B.xy[0])
				drawTrapezoid(B, A, C, C);
			else
				drawTrapezoid(A, B, C, C);
		}
	}

	//the real drawing part
	public void drawTrapezoid(Point LT, Point RT, Point LB, Point RB)
	{
		for(int y = LT.xy[1]; y <= LB.xy[1]; y++)
		{
			double t;

			//in case of dividing by zero
			if(LB.xy[1] == LT.xy[1])
				t = 0;
			else
				t = (y - LT.xy[1]) * 1.0 / (LB.xy[1] - LT.xy[1]);

			int xL = (int) (LT.xy[0] + t * (LB.xy[0] - LT.xy[0]));
			int xR = (int) (RT.xy[0] + t * (RB.xy[0] - RT.xy[0]));

			double LzBuffer = LT.zBuffer+ t * (LB.zBuffer- LT.zBuffer);
			double RzBuffer = RT.zBuffer+ t * (RB.zBuffer- RT.zBuffer);

			int[] LRGB = {0, 0, 0};
			int[] RRGB = {0, 0, 0};

			for(int i = 0; i < 3; i++)
			{
				LRGB[i] = (int)(LT.rgb[i] + t * (LB.rgb[i] - LT.rgb[i]));
				RRGB[i] = (int)(RT.rgb[i] + t * (RB.rgb[i] - RT.rgb[i]));
			}

			for(int x = xL; x <= xR; x++)
			{
				double T;

				//in case of dividing by zero
				if(xR == xL)
					T = 0;
				else
					T = (x - xL) * 1.0 / (xR - xL);

				double zBufferBuffer = LzBuffer + T * (RzBuffer - LzBuffer);

				if(zBufferBuffer > zBuffer[x][y])
				{
					int[] RGB = {0, 0, 0};
					for(int i = 0; i < 3; i++)
						RGB[i] = (int)(LRGB[i] + T * (RRGB[i] - LRGB[i]));

					pix[y * W + x] = pack(RGB[0], RGB[1], RGB[2]);
					zBuffer[x][y] = zBufferBuffer;
				}
			}
		}
	}

//------------------------------------------Triangle filled---------------------------------------------------------------------------------------------
	
	public void computeImage(double time)
	{
		initFrame(time);

		world.setFL(FL);
		
		sphere.sphere();
		sphere.getMatrix().identity().translate(Math.sin(time) * 2, 0, 0);

		vertices = sphere.getVertices();
		faces = sphere.getFaces();

		//draw the sphere
		drawFace(sphere);

		cube.cube();
		cube.getMatrix().identity().translate(0, 2, Math.sin(time)).rotateX(time).rotateY(time);

		vertices = cube.getVertices();
		faces = cube.getFaces();

		//draw the cube
		drawFace(cube);

		torus.torus();
		torus.getMatrix().identity().translate(0, -2, 0).rotateX(.5).rotateY(time).scale(.5, .5, .5);
		
		vertices = torus.getVertices();
		faces = torus.getFaces();

		drawFace(torus);
	}

}
