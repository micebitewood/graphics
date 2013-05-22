/*
	Assignment 9
	
	Geometry for cone, sphere & cylinder
*/
public class Geometry
{
	//the quadratic shape matrix
	private Matrix geometry;
	//the transformation matrix
	private Matrix transform;
	//for saving transformed matrix
	private Matrix afterTransformation;
	//for saving the roots if a ray hits this geometry
	double[] root;
	//for boolean combination
	boolean flag;
	
	//initialization
	public Geometry()
	{
		geometry = new Matrix();
		transform = new Matrix();
		afterTransformation = new Matrix();
		transform.identity();
		root = new double[2];
	}

	//set flag for boolean combination
	public Geometry setFlag(boolean flag)
	{
		this.flag = flag;
		return this;
	}

	//getter
	public boolean getFlag()
	{
		return flag;
	}

	//it's a plane x = 0
	public Geometry plane()
	{
		geometry.clear();
		geometry.set(0, 3, 1);
		return this;
	}

	//this is a sphere x^2+y^2+z^2-1=0
	public Geometry sphere()
	{
		geometry.identity();
		geometry.set(3, 3, -1);
		return this;
	}

	//this is a cylinder x^2+y^2-1=0
	public Geometry cylinder()
	{
		geometry.clear();
		geometry.set(0, 0, 1);
		geometry.set(1, 1, 1);
		geometry.set(3, 3, -1);
		return this;
	}

	//this is a cone x^2+y^2-z^2=0
	public Geometry cone()
	{
		geometry.identity();
		geometry.set(2, 2, -1);
		geometry.set(3, 3, 0);
		return this;
	}

	//get the matrix of transformation 
	public Matrix getMatrix()
	{
		return transform;
	}

	/*
		@param v
			the position of starting point
		@param w
			the normalized vector of direction
	*/
	public boolean hasIntersection(double[] v, double[] w)
	{
		//apply the transformation
		afterTransformation.transform(geometry, transform);
		
		double A = 0, B = 0, C = 0;
		for(int i = 0; i < 3; i++)
		{
			int j = (i + 1) % 3;
			A += afterTransformation.get(i, i) * w[i] * w[i];
			A += afterTransformation.get(i, j) * w[i] * w[j];

			B += 2 * afterTransformation.get(i, i) * v[i] * w[i];
			B += afterTransformation.get(i, j) * (v[i] * w[j] + v[j] * w[i]);
			B += afterTransformation.get(i, 3) * w[i];

			C += afterTransformation.get(i, i) * v[i] * v[i];
			C += afterTransformation.get(i, j) * v[i] * v[j];
			C += afterTransformation.get(i, 3) * v[i];
		}
		C += afterTransformation.get(3, 3);
		
		return solveQuadraticEquation(A, B, C);
	}

	//solve the quadratic equation A*x^2+B*x+C=0
	public boolean solveQuadraticEquation(double A, double B, double C)
	{
		double discriminant = B * B - 4 * A * C;
		if(discriminant < 0)
			return false;
		double d = Math.sqrt(discriminant);
		double r1 = (-B - d) / (2 * A);
		double r2 = (-B + d) / (2 * A);
		//if flag == true, root[0] <= root[1]
		if(flag)
		{
			root[0] = r1 > r2 ? r2 : r1;
			root[1] = r1 > r2 ? r1 : r2;
		}
		//if flag == false, root[0] >= root[1]
		else
		{
			root[0] = r1 > r2 ? r1 : r2;
			root[1] = r1 > r2 ? r2 : r1;
		}
		
		return true;
	}

	//get root
	public double[] getRoot()
	{
		return root;
	}
	
	//get normal in P, save it in N
	public void getNormal(double[] P, double[] N)
	{
		N[0] = 2 * afterTransformation.get(0, 0) * P[0] + afterTransformation.get(0, 2) * P[2] + afterTransformation.get(0, 1) * P[1] + afterTransformation.get(0, 3);
		N[1] = 2 * afterTransformation.get(1, 1) * P[1] + afterTransformation.get(1, 2) * P[2] + afterTransformation.get(0, 1) * P[0] + afterTransformation.get(1, 3);
		N[2] = 2 * afterTransformation.get(2, 2) * P[2] + afterTransformation.get(1, 2) * P[1] + afterTransformation.get(0, 2) * P[0] + afterTransformation.get(2, 3);
	}
}
