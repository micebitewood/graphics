/*
	Assignment 9
	
	fog effect
*/
public class Fog
{
	public static double k = .1;
	
	/*
		@param eye   
			the field of view
		@param position 
			the point position
		@param rgb
			the original rgb
		@param fogColor
			the rgb of fog
	*/
	public static void addFog(double[] eye, double[] position, double[] rgb, double[] fogColor)
	{
		double dist = 0;
		for(int i = 0; i < 3; i++)
			dist += (position[i] - eye[i]) * (position[i] - eye[i]);
		dist = Math.sqrt(dist);
		double a = Math.pow(2, -k * dist);
		for(int i = 0; i < 3; i++)
			rgb[i] = a * rgb[i] + (1 - a) * fogColor[i];
	}
}
