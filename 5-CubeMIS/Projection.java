/*
	Assignment 5
	John Mu
	jm4911@nyu.edu

	Same with assignment 4
*/
public class Projection
{
	private int w = 640;
	private int h = 480;
	private double FL = 10.0;
	public void setWidth(int w)
	{
		this.w = w;
	}
	public void setHeight(int h)
	{
		this.h = h;
	}
	public void setFL(double FL)
	{
		this.FL = FL;
	}
	public void project(double xyz[], int pxy[])
	{
		if(xyz.length == 2)
		{
			double x = xyz[0];
			double y = xyz[1];

			pxy[0] = w / 2 + (int)(h * x / FL);
			pxy[1] = h / 2 - (int)(h * y / FL);
		}
		else
		{
			double x = xyz[0];
			double y = xyz[1];
			double z = xyz[2];

			pxy[0] = w / 2 + (int)(h * x / (FL - z));
			pxy[1] = h / 2 - (int)(h * y / (FL - z));
		}
	}
}
