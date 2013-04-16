/*
	Assignment 4
	John MU
	jm4911

	same with assignment 3, nothing's changed
*/
public class Matrix{

	private double[][] matrix;
	private double[] temp = {0, 0, 0, 0};

	public Matrix() {
		matrix = new double[4][4];
	}//construction

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
	}//set to identity

	public void set(int col, int row, double value) {
		matrix[row][col] = value;
	}//setter

	public double get(int col, int row) {
		return matrix[row][col];
	}//getter

	public Matrix translate(double x, double y, double z) {
		for(int i = 0; i < 4; i++) {
			matrix[i][3] += matrix[i][0] * x + matrix[i][1] * y + matrix[i][2] * z;
		}
		return this;
	}//moving towards X, Y, Z with x, y, z steps relatively

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
	}//rotating by X axis

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
	}//rotate by Y axis

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
	}//rotate by Z axis

	public Matrix scale(double x, double y, double z) {
		for(int i = 0; i < 4; i++) {
			matrix[i][0] *= x;
			matrix[i][1] *= y;
			matrix[i][2] *= z;
		}
		return this;
	}//scaling

	public void leftMultiply(Matrix other) {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				double temp = 0;
				for(int k = 0; k < 4; k++) {
					temp += other.get(k, i) * matrix[k][j];
				}
				matrix[i][j] = temp;
			}
		}
	}//other.matrix * this.matrix

	public Matrix rightMultiply(Matrix other) {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				temp[j] = 0;
				for(int k = 0; k < 4; k++) {
					temp[j] += other.get(j, k) * matrix[i][k];
				}
			}
			for(int j = 0; j < 4; j++) 
				matrix[i][j] = temp[j];
		}
		return this;
	}//this.matrix * other.matrix
	public void transform(double[] src, double[] dst) {
		for(int i = 0; i < 3; i++) {
			dst[i] = 0;
			for(int j = 0; j < 3; j++) {
				dst[i] += matrix[i][j] * src[j];
			}
			dst[i] += matrix[i][3] * 1;
		}
		//x' y' z' 

		double w = 0;
		for(int j = 0; j < 3; j++) {
			w += matrix[3][j] * src[j];
		}
		w += matrix[3][3] * 1;
		//w' 

		if(w == 0) {
			for(int i = 0; i < 3; i++) {
				dst[i] = Double.MAX_VALUE;
			}
		}
		//if w' == 0, make the point infinite

		else {
			for(int i = 0; i < 3; i++) {
				dst[i] /= w;
			}
		}
	}
	//[dst w'] = this.matrix * [src 1]
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
