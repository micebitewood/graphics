public class MyMISApplet extends MISApplet
{
	double t = 0;

	double FOV = 10;

	double[][] sphere = {
		{0, 0, 0, 1}, {0, 0, 0, 1}, {0, 0, 0, 1}
	};

	double[] v = new double[3];
	double[] w = new double[3];
	double[] v_c = new double[3];
	double[] P = new double[3];
	double[] N = new double[3];
	double[] T = new double[2];
	double[] T_s;
	double[] root = new double[2];
	double[] rgb = new double[3];

	Material material;
	
	double[][][] lights = {
		{{1, 1, 1}, {1, 1, 1}},
		{{-1,-1,-1}, {.1, .1, 1}}
	};
	
	int x;
	int y;

	public void initialize()
	{
		material = new Material();
		for(double[][] light: lights)
			normalize(light[0]);
	}

	public void initFrame(double time)
	{
		t = 3 * time;

		for(int y = 0; y < H; y++)
			for(int x = 0; x < W; x++)
				pix[x * W + y] = pack(255, 255, 255);
		material.setAmbient(.2, 0, 0);
		material.setDiffuse(.8, 0, 0);
		material.setSpecular(1, 1, 1, 20);
	}

	public double dot(double[] a, double[] b)
	{
		double c = 0;

		for(int i = 0; i < a.length; i++)
			c += a[i] * b[i];
		return c;
	}

	public void set(double[] dst, double a, double b, double c)
	{
		dst[0] = a;
		dst[1] = b;
		dst[2] = c;
	}

	public void normalize(double[] vector)
	{
		double sqrSum = 0;
		for(int i = 0; i < vector.length; i++)
			sqrSum += vector[i] * vector[i];
		double sqrtSum = Math.sqrt(sqrSum);
		for(int i = 0; i < vector.length; i++)
			vector[i] /= sqrtSum;
	}

	public boolean solveQuadraticEquation(double A, double B, double C, double[] root)
	{
		double discriminant = B * B - 4 * A * C;
		if(discriminant < 0)
			return false;
		double d = Math.sqrt(discriminant);
		root[0] = (-B - d) / (2 * A);
		root[1] = (-B + d) / (2 * A);
		return true;
	}

	public boolean raytrace(double[] v, double[] w, double[] T)
	{
		boolean solved = false;
		T[0] = Double.MAX_VALUE;

		for(double[] s: sphere)
		{
			for(int i = 0; i < v.length; i++)
				v_c[i] = v[i] - s[i];
			double A = 1.0;
			double B = 2 * dot(w, v_c);
			double C = dot(v_c, v_c) - sphere[0][3] * sphere[0][3];
			if(solveQuadraticEquation(A, B, C, root))
			{
				solved = true;
				if(root[0] < T[0])
				{
					T_s = s;
					T[0] = root[0];
					T[1] = root[1];
				}
			}
		}

		return solved;
	}

	public void computeShading(double[] N, double[] rgb)
	{
		double[] A = material.getAmbient();
		double[] D = material.getDiffuse();
		double[] S = material.getSpecular();
		double P = material.getPower();
		double[] E = {0, 0, 1};
		double[] R = {0, 0, 0};

		for(int k = 0; k < 3; k++)
			rgb[k] = A[k];

		for(double[][] light: lights)
		{
			double ldirN = 0;
			for(int k = 0; k < 3; k++)
				ldirN += light[0][k] * N[k];
			double RE = 0;
			for(int k = 0; k < 3; k++)
			{
				R[k] = 2 * ldirN * N[k] - light[0][k];
				RE += R[k] * E[k];
			}
			for(int k = 0; k < 3; k++)
				rgb[k] += light[1][k] * (D[k] * (ldirN > 0 ? ldirN : 0) + S[k] * Math.pow((RE > 0 ? RE : 0), P));
		}
		for(int k = 0; k < 3; k++)
			rgb[k] = 255 * Math.pow(rgb[k], 0.45);
	}

	public void computeImage(double time)
	{
		initFrame(time);

		sphere[0][0] = Math.sin(time);
		sphere[0][1] = Math.cos(time);
		sphere[0][2] = -10;
		sphere[1][0] = Math.sin(time + 2); 
		sphere[1][1] = Math.cos(time + 2);
		sphere[1][2] = -9;
		sphere[2][0] = Math.sin(time + 4); 
		sphere[2][1] = Math.cos(time + 4);
		sphere[2][2] = -8;
		for(y = 0; y < H; y++)
		{
			for(x = 0; x < W; x++)
			{
				set(v, 0, 0, FOV);
				double j = (H / 2 - y) * 1.0 / 50;
				double i = (x - W / 2) * 1.0 / 50;
				set(w, i, j, -10);
				normalize(w);

				if(raytrace(v, w, T))
				{
					for(int k = 0; k < 3; k++)
					{
						P[k] = v[k] + T[0] * w[k];
						N[k] = P[k] - T_s[k];
					}
					normalize(N);
					computeShading(N, rgb);
					pix[y * W + x] = pack((int)rgb[0], (int)rgb[1], (int)rgb[2]);
				}
				
			}
		}
	}
}
