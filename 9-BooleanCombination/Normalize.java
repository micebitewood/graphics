/*
	Assignment 9
	
	for normalization
*/
public class Normalize
{
	public static void normalize(double[] vector)
	{
		double sum = 0;
		for(int i = 0; i < vector.length; i++)
			sum += vector[i] * vector[i];

		sum = Math.sqrt(sum);

		for(int i = 0; i < vector.length; i++)
			vector[i] /= sum;
	}
}
