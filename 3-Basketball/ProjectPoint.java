/*
	Assinment 3
	John Mu
	jm4911@nyu.edu


	This is a class of static fields and a static method, used to project points in the 3D world into the screen.

*/
public class ProjectPoint
{
	private static int w = 640;
	private static int h = 480;
	private static double FL = 4.0;
	public static void project(double xyz[], int pxy[])
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
