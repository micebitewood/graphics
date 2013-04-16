/*
	Assignment 2
	John Mu
	jm4911
*/

import java.awt.*;

public class MyApplet extends BufferedApplet
{
	/*
		This is a robot(I know this is ridiculous to have a robot with lines). The scaling method is not applicable, otherwise the components will be separated.
	*/
	int w = 0, h = 0;//The height and width of the frame.
	double[][] head = {
		{-1, 2, -1}, {1, 2, -1}, {-1, 4, -1}, {1, 4, -1},
		{-1, 2, 1}, {1, 2, 1}, {-1, 4, 1}, {1, 4, 1}
	};
	double[][] body = {
		{-2, -2, -2}, {2, -2, -2}, {-2, 2, -2}, {2, 2, -2},
		{-2, -2, 2}, {2, -2, 2}, {-2, 2, 2}, {2, 2, 2}
	};
	double[][] rUpperArm = {
		{2, 0, -1}, {4, 0, -1}, {2, 2, -1}, {4, 2, -1},
		{2, 0, 1}, {4, 0, 1}, {2, 2, 1}, {4, 2, 1}
	};
	double[][] rLowerArm = {
		{4, 0, -1}, {6, 0, -1}, {4, 4, -1}, {6, 4, -1},
		{4, 0, 1}, {6, 0, 1}, {4, 4, 1}, {6, 4, 1}
	};
	double[][] rHand = {
		{3, 4, -1}, {7, 4, -1}, {3, 6, -1}, {7, 6, -1},
		{3, 4, 1}, {7, 4, 1}, {3, 6, 1}, {7, 6, 1}
	};
	double[][] lUpperArm = {
		{-2, 0, -1}, {-4, 0, -1}, {-2, 2, -1}, {-4, 2, -1},
		{-2, 0, 1}, {-4, 0, 1}, {-2, 2, 1}, {-4, 2, 1}
	};
	double[][] lLowerArm = {
		{-4, 0, -1}, {-6, 0, -1}, {-4, 4, -1}, {-6, 4, -1},
		{-4, 0, 1}, {-6, 0, 1}, {-4, 4, 1}, {-6, 4, 1}
	};
	double[][] lHand = {
		{-3, 4, -1}, {-7, 4, -1}, {-3, 6, -1}, {-7, 6, -1},
		{-3, 4, 1}, {-7, 4, 1}, {-3, 6, 1}, {-7, 6, 1}
	};
	double[][] rLeg = {
		{2, -7, -2}, {0, -7, -2}, {2, -2, -2}, {0, -2, -2},
		{2, -7, 2}, {0, -7, 2}, {2, -2, 2}, {0, -2, 2}
	};
	double[][] lLeg = {
		{-2, -7, -2}, {0, -7, -2}, {-2, -2, -2}, {0, -2, -2},
		{-2, -7, 2}, {0, -7, 2}, {-2, -2, 2}, {0, -2, 2}
	};
	//The coordinates of different parts.

    int[][] edges = {
          {0,1},{2,3},{4,5},{6,7}, // EDGES IN X DIRECTION
          {0,2},{1,3},{4,6},{5,7}, // EDGES IN Y DIRECTION
          {0,4},{1,5},{2,6},{3,7}, // EDGES IN Z DIRECTION
    };

	Matrix matrix = new Matrix(); //See detail in Matrix.java
    double FL = 20.0; // "FOCAL LENGTH" OF THE CAMERA

    double[] point0 = new double[3];
    double[] point1 = new double[3];
	//Coordinates after transformation

    int[] a = new int[2];
    int[] b = new int[2];
	//Coordinates after projection

	double time = 0;
	double startTime = 0;

    public void projectPoint(double[] xyz, int[] pxy) {

      // INPUT: YOUR POINT IN 3D

      double x = xyz[0];
      double y = xyz[1];
      double z = xyz[2];

      // OUTPUT: PIXEL COORDINATES TO DISPLAY YOUR POINT

      pxy[0] = w / 2 + (int)(h * x / (FL - z));
      pxy[1] = h / 2 - (int)(h * y / (FL - z));
    }
	public void drawTheLine(double[] i, double[] j, Graphics g) {

		matrix.transform(i, point0);
        matrix.transform(j, point1);
		//transform the points

//	    projectPoint(point0, a);
//	    projectPoint(point1, b);
		//projection of the points
		a[0] = w / 2 + (int)(h * point0[0] / FL);
		a[1] = h / 2 - (int)(h * point0[1] / FL);
		b[0] = w / 2 + (int)(h * point1[0] / FL);
		b[1] = h / 2 - (int)(h * point1[1] / FL);

		g.drawLine(a[0], a[1], b[0], b[1]);
		//draw the line
	}

	public void render(Graphics g) {
		if(w == 0) {
			w = getWidth();
			h = getHeight();
			
			g.setColor(Color.white);
			g.fillRect(0, 0, w, h);
			//Clean the screen

			startTime = System.currentTimeMillis();//Let's start!
		}

		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		//Clean the screen

		g.setColor(Color.black);

		time = System.currentTimeMillis() - startTime;

		matrix.identity();//set matrix to identity
		matrix.translate(0, Math.sin(time / 1000), 0);//moving up and down
		matrix.rotateY(Math.PI * time / 4000);//rotating according to Y axis
		matrix.setFL(FL);

		for (int e = 0 ; e < edges.length ; e++) {
            int i = edges[e][0];
            int j = edges[e][1];
			//pick one edge

			drawTheLine(head[i], head[j], g);
			//this is the head

			drawTheLine(body[i], body[j], g);
			//this is the body

			drawTheLine(rUpperArm[i], rUpperArm[j], g);
			//right upper arm

			drawTheLine(rLowerArm[i], rLowerArm[j], g);
			//right lower arm

			drawTheLine(rHand[i], rHand[j], g);
			//right hand

			drawTheLine(lUpperArm[i], lUpperArm[j], g);
			//left upper arm

			drawTheLine(lLowerArm[i], lLowerArm[j], g);
			//left lower arm

			drawTheLine(lHand[i], lHand[j], g);
			//left hand

			drawTheLine(rLeg[i], rLeg[j], g);
			//right leg

			drawTheLine(lLeg[i], lLeg[j], g);
			//left leg
		}
	}
}
