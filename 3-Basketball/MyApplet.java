/*
	Assinment 3
	John Mu
	jm4911@nyu.edu


	This is scene of shooting a basketball. We can drag the mouse to rotate the whole scene according to the Y axis.

*/
import java.awt.*;

public class MyApplet extends BufferedApplet
{
	int w = 0, h = 0;
	//width and height

	Geometry basket, backboard, ball;
	//Three objects: basket is a torus, backboard is a cube, ball is a sphere

	double theta = Math.PI / 4;
	//The shooting angle

	double v = 8.5;
	//the shooting speed

	double g = 9.8;
	//the gravity

	double startTime;
	double time;
	//this is the time

	double[] startPos = {6.71 , -0.55, 0};
	double[] currentPos = {6.71 , -0.55, 0};
	//position of the ball, it is in fact the three point line

	Color ballColor = new Color(200, 100, 50);
	//the color of the ball

	int x = 0;
	//the previous position of the mouse
	double alpha = 0;
	//alpha is the angle of the scene rotating.

	private void getCurrentPos(double[] startPos, double[] currentPos, double time)
	{
		double x = time * v * Math.cos(theta);
		double y = v * Math.sin(theta) * time - g * time * time / 2;
		currentPos[0] = startPos[0] - x;
		currentPos[1] = startPos[1] + y;
		currentPos[2] = 0;
	}
	//calculation of the current position of the ball, considering the gravity and initial velocity and shooting angle

	public boolean mouseDown(Event e, int x, int y)
	{
		this.x = x;
		return true;
	}
	//if mouse is down, set this.x = x

	public boolean mouseDrag(Event e, int x, int y)
	{
		alpha = Math.PI * (x - this.x) / 200 + alpha;
		this.x = x;
		return true;
	}
	//while mouse is dragging, calculate the angle and save x

	public void render(Graphics g)
	{
		if(w == 0)
		{
			w = getWidth();
			h = getHeight();

			g.setColor(Color.white);
			g.fillRect(0, 0, w, h);

			basket = new Geometry();
			backboard = new Geometry();
			ball = new Geometry();
			//initialization

			g.setColor(Color.black);

			basket.torus(2 * Math.PI, 2 * Math.PI, 0.225, 0.02);//Geometry.torus(u, v, R, r), 0 <= {u, v} <= 2 * pi, R > 0, 0 < r <= R 
			basket.getMatrix().identity().rotateX(Math.PI / 2);
			basket.draw(g);

			backboard.cube();//Geometry.cube() create a standard cube.
			backboard.getMatrix().identity().translate(-0.24 * Math.sin(alpha), 0.4,  -0.24 * Math.cos(alpha)).rotateY(alpha).scale(0.9, 0.525, 0.015);
			backboard.draw(g);

			ball.sphere(2 * Math.PI, Math.PI / 2); //Geometry.sphere(u, v), 0 <= u <= 2 * pi, -pi / 2 <= v <= pi / 2
			ball.getMatrix().identity().translate(startPos[0], startPos[1], startPos[2]).scale(0.125, 0.125, 0.125);
			ball.draw(g);

			startTime = System.currentTimeMillis() / 1000.0;
		}

		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		//refreshing

		if(currentPos[0] < -7)//set startTime and currentPos to initial states if currentPos.x < -7
		{
			startTime = System.currentTimeMillis() / 1000.0;
			for(int i = 0; i < 3; i++)
				currentPos[i] = startPos[i];
		}

		time = System.currentTimeMillis() / 1000.0 - startTime;

		getCurrentPos(startPos, currentPos, time);
		//gets the current position of the ball

		ball.getMatrix().identity().translate(currentPos[0] * Math.cos(alpha), currentPos[1], -currentPos[0] * Math.sin(alpha)).scale(0.125, 0.125, 0.125);
		//sets the matrix so that we can translate the ball to the current position, also considers the scene angle

		backboard.getMatrix().identity().translate(-0.24 * Math.sin(alpha), 0.4,  -0.24 * Math.cos(alpha)).rotateY(alpha).scale(0.9, 0.525, 0.015);
		//sets the matrix so that the backboard will move according to alpha

		g.setColor(ballColor);
		ball.draw(g);

		g.setColor(Color.black);
		g.drawString("Please drag the mouse to change the view", 40, 10);
		basket.draw(g);
		backboard.draw(g);
	}
}
