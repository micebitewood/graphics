/*
	Assignment 9
	John MU
	jm4911

	minor changes, almost the same with earlier version
*/

public class Matrix{

	private double[][] matrix;
	private double[][] tempMatrix;

	public Matrix() {
		matrix = new double[4][4];
		tempMatrix = new double[4][4];
	}
	
	public void clear()
	{
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				matrix[i][j] = 0;
	}

	//transformation of src with trans, save the transformed matrix in local
	public void transform(Matrix src, Matrix trans)
	{
		trans.invertMatrix();
		this.identity().rightMultiply(src).leftMultiply(this.transpose(trans.getInvertedMatrix())).rightMultiply(trans.getInvertedMatrix());
		for(int i = 0; i < 4; i++)
			for(int j = i + 1; j < 4; j++)
			{
				matrix[i][j] += matrix[j][i];
				matrix[j][i] = 0;
			}
	}

	//invert matrix and save it in tempMatrix
	public void invertMatrix()
	{
		MatrixInverter.invert(matrix, tempMatrix);
	}

	public double[][] getInvertedMatrix()
	{
		return tempMatrix;
	}

	public Matrix rightMultiply(Matrix other) {
		this.rightMultiply(other.getMatrix());
		return this;
	}

	//matrix * other
	public Matrix rightMultiply(double[][] other) {
		double[] temp = {0, 0, 0, 0};
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				temp[j] = 0;
				for(int k = 0; k < 4; k++) {
					temp[j] += matrix[i][k] * other[k][j];
				}
			}
			for(int j = 0; j < 4; j++) {
				matrix[i][j] = temp[j];
			}
		}
		return this;
	}

	//other * matrix
	public Matrix leftMultiply(double[][] other) {
		double[] temp = {0, 0, 0, 0};
		for(int j = 0; j < 4; j++) {
			for(int i = 0; i < 4; i++) {
				temp[i] = 0;
				for(int k = 0; k < 4; k++) {
					temp[i] += other[i][k] * matrix[k][j];
				}
			}
			for(int i = 0; i < 4; i++)
				matrix[i][j] = temp[i];
		}
		return this;
	}
	
	//transpose other and save it in tempMatrix
	public double[][] transpose(double[][] other)
	{
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				tempMatrix[i][j] = other[j][i];
		return tempMatrix;
	}

	public double[][] getMatrix()
	{
		return this.matrix;
	}

	//make the matrix identity
	public Matrix identity() {
		for(int i = 0; i < 4; i++) {
			matrix[i][i] = 1;
			for(int j = 0; j < 4; j++) {
				if(i == j)
					continue;
				matrix[i][j] = 0;
			}
		}
		return this;
	}

	public void set(int row, int col, double value) {
		matrix[row][col] = value;
	}

	public double get(int row, int col) {
		return matrix[row][col];
	}

	//moving towards X, Y, Z with x, y, z steps relatively
	public Matrix translate(double x, double y, double z) {
		for(int i = 0; i < 4; i++) {
			matrix[i][3] += matrix[i][0] * x + matrix[i][1] * y + matrix[i][2] * z;
		}
		return this;
	}

	//rotating by X axis
	public Matrix rotateX(double radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		for(int i = 0; i < 4; i++) {
			double a = matrix[i][1] * cos + matrix[i][2] * sin;
			double b = -matrix[i][1] * sin + matrix[i][2] * cos;
			matrix[i][1] = a;
			matrix[i][2] = b;
		}
		return this;
	}

	//rotate by Y axis
	public Matrix rotateY(double radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		for(int i = 0; i < 4; i++) {
			double a = matrix[i][0] * cos - matrix[i][2] * sin;
			double b = matrix[i][0] * sin + matrix[i][2] * cos;
			matrix[i][0] = a;
			matrix[i][2] = b;
		}
		return this;
	}

	//rotate by Z axis
	public Matrix rotateZ(double radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);
		for(int i = 0; i < 4; i++) {
			double a = matrix[i][0] * cos + matrix[i][1] * sin;
			double b = -matrix[i][0] * sin + matrix[i][1] * cos;
			matrix[i][0] = a;
			matrix[i][1] = b;
		}
		return this;
	}

	//scaling
	public Matrix scale(double x, double y, double z) {
		for(int i = 0; i < 4; i++) {
			matrix[i][0] *= x;
			matrix[i][1] *= y;
			matrix[i][2] *= z;
		}
		return this;
	}



	public void print()
	{
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
				System.out.print(matrix[i][j] + " ");
			System.out.println();
		}
		System.out.println();
	}
}
