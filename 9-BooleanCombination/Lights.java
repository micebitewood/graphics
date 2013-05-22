/*
	Assignment 9
	
	for generating infinite light source, or point light source, both including normalization. save the transformed light in newLight
*/
public class Lights
{
	static double[] direction = new double[3];
	
	/*
		@param light
			the original light
		@param position
			the position of the target
		@param newLight
			for saving the transformed light
	*/
	public static void pointSrc(double[][] light, double[] position, double[][] newLight)
	{
		double distSqr = 0;
		for(int i = 0; i < 3; i++)
		{
			direction[i] = light[0][i] - position[i];
			distSqr += direction[i] * direction[i];
		}
		if(distSqr < .000001)
			distSqr = 1;
		double dist = Math.sqrt(distSqr);
		for(int i = 0; i < 3; i++)
		{
			newLight[0][i] = direction[i] / dist;
			newLight[1][i] = light[1][i] / (.1 * distSqr);
		}
	}
	
	//it's just a normalization
	public static void infinite(double[][] light, double[][] newLight)
	{
		for(int i = 0; i < 3; i++)
		{
			newLight[0][i] = light[0][i];
			newLight[1][i] = light[1][i];
		}
		Normalize.normalize(newLight[0]);
	}
}
