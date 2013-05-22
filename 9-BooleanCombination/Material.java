/*
	Assignment 9
	John Mu
	jm4911
	
	same with former version
*/
class Material
{
	double[] ambientColor;
	double[] diffuseColor;
	double[] specularColor;
	double specularPower;

	public Material()
	{
		ambientColor = new double[3];
		diffuseColor = new double[3];
		specularColor = new double[3];
	}

	public void setAmbient(double r, double g, double b)
	{
		ambientColor[0] = r;
		ambientColor[1] = g;
		ambientColor[2] = b;
	}

	public void setDiffuse(double r, double g, double b)
	{
		diffuseColor[0] = r;
		diffuseColor[1] = g;
		diffuseColor[2] = b;
	}

	public void setSpecular(double r, double g, double b, double pow)
	{
		specularColor[0] = r;
		specularColor[1] = g;
		specularColor[2] = b;
		specularPower = pow;
	}

	public double[] getAmbient()
	{
		return ambientColor;
	}

	public double[] getDiffuse()
	{
		return diffuseColor;
	}

	public double[] getSpecular()
	{
		return specularColor;
	}

	public double getPower()
	{
		return specularPower;
	}

}
