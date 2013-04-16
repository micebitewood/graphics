/*
	Assinment 5
	John Mu
	jm4911@nyu.edu

*/
public class MyMISApplet extends MISApplet {

	//for saving the time
    double t = 0;
	double[][] zBuffer;
	double FL = 5;
	
	//this is a cube
	Geometry world = new Geometry();
	Geometry sphere = world.add();
	
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

	Point D = new Point();

	//for saving the points after transformation
	double[] trans = new double[6];
	
	public void initialize()
	{
		for(int i = 0; i < 4; i++)
			point[i] = new Point();
		zBuffer = new double[H][W];
	}

    public void initFrame(double time) {
	
		//save the time in t
		t = 3 * time;
	   
    }
    public void setPixel(int x, int y, int rgb[]) {
	
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
				sphere.getMatrix().transform(vertices[face[j]], trans);
				point[j].xy[0] = W / 2 + (int)(H * trans[0] / FL);
				point[j].xy[1] = H / 2 + (int)(H * trans[1] / FL);
				//rgbBuffer is calculated with norm
				point[j].zBuffer= trans[2];
				point[j].rgb[0] = (int)((trans[3] + 1) * 255 / 2);
				point[j].rgb[1] = (int)((trans[4] + 1) * 255 / 2);
				point[j].rgb[2] = (int)((trans[5] + 1) * 255 / 2);
				if(point[j].rgb[0] + point[j].rgb[1] + point[j].rgb[2] == 0)
					System.out.println("black");
			}
			
			double area = 0;
			
			//calculate the area
			for(int j = 0; j < face.length; j++)
				area += (point[j].xy[0] - point[(j + 1) % face.length].xy[0]) * (point[j].xy[1] + point[(j + 1) % face.length].xy[1]) / 2.0;
			//if it's larger than zero, it means the face is in the back
			if(area > 0)
				continue;
			
			//First, let's deal with the triangle point[0], point[1], point[2]
			//If there is a D existing
			split(point[0], point[1], point[2]);

			//Second, let's deal with the triangle point[2], point[3], point[0]
			//If there is a D existing
			split(point[0], point[2], point[3]);
		}
	}

	/*
		D stores the vertice of D, and BD divides the triangle into to pieces
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

			draw(A, B, D, true);
			draw(B, D, C, false);
		}
		//if not
		else
			draw(A, B, C, B.xy[1] == C.xy[1]);
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

	public void drawTrapezoid(Point LT, Point RT, Point LB, Point RB)
	{
		for(int y = LT.xy[1]; y <= LB.xy[1]; y++)
		{
			double t;
			if(LB.xy[1] == LT.xy[1])
				t = 0;
			else
				t = (y - LT.xy[1]) * 1.0 / (LB.xy[1] - LT.xy[1]);
			int xL = (int) (LT.xy[0] + t * (LB.xy[0] - LT.xy[0]));
			int[] LRGB = {0, 0, 0};
			double LzBuffer = LT.zBuffer+ t * (LB.zBuffer- LT.zBuffer);
			int xR = (int) (RT.xy[0] + t * (RB.xy[0] - RT.xy[0]));
			int[] RRGB = {0, 0, 0};
			double RzBuffer = RT.zBuffer+ t * (RB.zBuffer- RT.zBuffer);
			for(int i = 0; i < 3; i++)
			{
				LRGB[i] = (int)(LT.rgb[i] + t * (LB.rgb[i] - LT.rgb[i]));
				RRGB[i] = (int)(RT.rgb[i] + t * (RB.rgb[i] - RT.rgb[i]));
			}
			if(LRGB[0] + LRGB[1] + LRGB[2] == 0)
				System.out.println("L BLack");
			if(RRGB[0] + RRGB[1] + RRGB[2] == 0)
				System.out.println("R BLack");
			for(int x = xL; x <= xR; x++)
			{
				double T;
				if(xR == xL)
					T = 0;
				else
					T = (x - xL) * 1.0 / (xR - xL);
				double zBufferBuffer = LzBuffer + T * (RzBuffer - LzBuffer);
				if(zBufferBuffer > zBuffer[y][x])
				{
					int[] RGB = {0, 0, 0};
					for(int i = 0; i < 3; i++)
						RGB[i] = (int)(LRGB[i] + T * (RRGB[i] - LRGB[i]));
					pix[y * W + x] = pack(RGB[0], RGB[1], RGB[2]);
					zBuffer[y][x] = zBufferBuffer;
				}
			}
		}
	}

//------------------------------------------Triangle filled---------------------------------------------------------------------------------------------
	
	public void computeImage(double time)
	{
		initFrame(time);
		
		sphere.sphere();
		sphere.getMatrix().identity().rotateX(time).rotateY(time).setFL(FL);

		for(int i = 0; i < H; i++)
			for(int j = 0; j < W; j++)
				zBuffer[i][j] = -FL;
		
		vertices = sphere.getVertices();
		faces = sphere.getFaces();

		//paint the sphere
		drawFace();
	}

}
