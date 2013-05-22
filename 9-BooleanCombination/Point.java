/*
	Assignment 9
	
	for speed up, save the z and rgb for every pixel
*/
public class Point
{
	private double z;
	private int[] rgb;
	public Point()
	{
		rgb = new int[3];
	}
	public void setZ(double z)
	{
		this.z = z;
	}
	public void setRgb(double[] rgb)
	{
		for(int i = 0; i < 3; i++)
			this.rgb[i] = (int)rgb[i];
	}
	public double getZ()
	{
		return z;
	}
	public int[] getRgb()
	{
		return rgb;
	}
}
