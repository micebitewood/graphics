/*
	Assignment 9
	
	point light source, quadratic surfaces, transformation, boolean combination, fog, speed up
*/
import java.util.*;

public class MyMISApplet extends MISApplet
{
	private static final long serialVersionUID = 1L;

	//for saving time
	double t = 0;

	//field of view
	double FOV = 10;

	//for saving geometries
	Geometry[] geometry = new Geometry[2];

	//for computing shading
	double[] v = new double[3];
	double[] w = new double[3];
	double[] P = new double[3];
	double[] N = new double[3];
	double T;
	double[] rgb = new double[3];
	double[] fogColor = {50, 50, 50};
	double[][] newLight;
	
	//for boolean combination
	GeoComparator geoComp;
	PriorityQueue<Geometry> in, out;
	Geometry[] ins, outs;

	Material material;

	//the first light is infinite, the second one is a point source
	double[][][] lights = {
		{{-1, -1, -1}, {1, 1, 1}},
		{{0, 2, 1}, {1, 1, 1}}
	};

	Point[] points;

	//initialization for the very first frame
	public void initialize()
	{
		material = new Material();
		material.setAmbient(.2, 0, 0);
		material.setDiffuse(.8, 0, 0);
		material.setSpecular(1, 1, 1, 20);

		newLight = new double[2][3];
		geometry[0] = new Geometry();
		geometry[1] = new Geometry();

		geoComp = new GeoComparator(0);
		in = new PriorityQueue<Geometry>(2, geoComp);
		ins = new Geometry[2];

		geoComp = new GeoComparator(1);
		out = new PriorityQueue<Geometry>(2, geoComp);
		outs = new Geometry[2];

		points = new Point[W * H];
		for(int i = 0; i < W * H; i++)
			points[i] = new Point();
	}

	//initialization for every frame
	public void initFrame(double time)
	{
		t = 3 * time;
	}

	//dot multiplication between vector a and b
	public double dot(double[] a, double[] b)
	{
		double c = 0;

		for(int i = 0; i < a.length; i++)
			c += a[i] * b[i];
		return c;
	}

	//set a 3D vector
	public void set(double[] dst, double a, double b, double c)
	{
		dst[0] = a;
		dst[1] = b;
		dst[2] = c;
	}

	//save the roots in two queues if the ray hits some geometry
	public boolean raytrace(double[] v, double[] w)
	{
		boolean solved = false;

		in.clear();
		out.clear();
		for(Geometry g: geometry)
			if(g.hasIntersection(v, w))
			{
				solved = true;
				in.add(g);
				out.add(g);
			}
		

		return solved;
	}

	//phong reflection model
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
			if(light == lights[0])
				Lights.infinite(light, newLight);
			else
				Lights.pointSrc(light, v, newLight);
			set(w, newLight[0][0], newLight[0][1], newLight[0][2]);
			
			//if(raytrace(v, w))
			//	continue;
			
			double ldirN = 0;
			for(int k = 0; k < 3; k++)
				ldirN += newLight[0][k] * N[k];
			double RE = 0;
			for(int k = 0; k < 3; k++)
			{
				R[k] = 2 * ldirN * N[k] - newLight[0][k];
				RE += R[k] * E[k];
			}
			for(int k = 0; k < 3; k++)
				rgb[k] += newLight[1][k] * (D[k] * (ldirN > 0 ? ldirN : 0) + S[k] * Math.pow((RE > 0 ? RE : 0), P));
		}
		for(int k = 0; k < 3; k++)
			rgb[k] = 255 * Math.pow(rgb[k], 0.45);
	}

	//boolean combination
	public void shootRay(int x, int y)
	{
		set(v, 0, 0, FOV);
		double j = (H / 2 - y) * 1.0 / 50;
		double i = (x - W / 2) * 1.0 / 50;
		set(w, i, j, -10);
		Normalize.normalize(w);
		boolean hasIntersection = false;

		if(raytrace(v, w))
		{
			hasIntersection = true;
			in.toArray(ins);
			out.toArray(outs);

			int ind = 0, rind = 0;
			if(ins[1] == null)
			{
				if(geometry[0].getFlag() && geometry[1].getFlag())
					hasIntersection = false;
				else if(!geometry[0].getFlag() && !geometry[1].getFlag())
				{
					rind = 1;
				}
				else if(!ins[0].getFlag())
					hasIntersection = false;
			}
			else
			{
				double in0 = ins[0].getRoot()[0];
				double in1 = ins[1].getRoot()[0];
				double out0 = outs[0].getRoot()[1];
				double out1 = outs[1].getRoot()[1];
				if(in0 < out0 && out0 < out1 && out1 < in1){}
				else if(in0 < out0 && out0 < in1 && in1 < out1)
				{
					if(ins[0] == outs[0])
						hasIntersection = false;
				}
				else if(out0 < in0 && in0 < out1 && out1 < in1)	
				{
					rind = 1;
					if(ins[0] == outs[1])
						hasIntersection = false;
				}
				else if(out0 <= in0 && in0 < in1 && in1 < out1)
					ind = 1;
				else if(in0 < in1 && in1 < out0 && out0 < out1)
					ind = 1;
				else if(out0 < out1 && out1 <= in0 && in0 < in1)
					rind = 1;
			}
			if(hasIntersection)
			{
				T = ins[ind].getRoot()[rind];
				for(int k = 0; k < 3; k++)
					P[k] = v[k] + T * w[k];
				ins[ind].getNormal(P, N);
				if(!ins[ind].getFlag())
					for(int k = 0; k < 3; k++)
						N[k] = -N[k];
				Normalize.normalize(N);
				set(v, P[0], P[1], P[2]);
				computeShading(N, rgb);
				set(v, 0, 0, FOV);
			}
		}
		if(!hasIntersection)
		{
			set(P, -Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
			for(int k = 0; k < 3; k++)
				rgb[k] = unpack(pix[y * W + x], k);
		}
		Fog.addFog(v, P, rgb, fogColor);
		points[y * W + x].setZ(P[2]);
		points[y * W + x].setRgb(rgb);
		pix[y * W + x] = pack((int)rgb[0], (int)rgb[1], (int)rgb[2]);
	}

	//if difference is big, return true; else return false.
	public boolean diff(double z1, int[] rgb1, double z2, int[] rgb2)
	{
		if(Math.abs(z1 - z2) > .1)
			return true;
		int rgbDiff = 0;
		for(int i = 0; i < 3; i++)
			rgbDiff += rgb1[i] - rgb2[i];
		if(rgbDiff > 20)
			return true;
		return false;
	}

	//compare each z and rgb with adjacent pixels, which are 3 pixels left, up, right, and down
	public void speedUp(int x, int y)
	{
		shootRay(x, y);
		int ind = y * W + x;
		double zBuffer = points[ind].getZ();
		int[] rgbBuffer = points[ind].getRgb();
		boolean eachPix = false;
		//compared to left
		if(x >= 4)
			if(diff(zBuffer, rgbBuffer, points[ind - 3].getZ(), points[ind - 3].getRgb()))
				eachPix = true;
		//compared to right
		if(x <= W - 5 && !eachPix)
			if(diff(zBuffer, rgbBuffer, points[ind + 3].getZ(), points[ind + 3].getRgb()))
				eachPix = true;
		//compared to up
		if(y >= 4 && !eachPix)
			if(diff(zBuffer, rgbBuffer, points[ind - 3 * W].getZ(), points[ind - 3 * W].getRgb()))
				eachPix = true;
		//compared to down
		if(y <= H - 5 && !eachPix)
			if(diff(zBuffer, rgbBuffer, points[ind + 3 * W].getZ(), points[ind + 3 * W].getRgb()))
				eachPix = true;
		if(eachPix)
		{
			shootRay(x - 1, y - 1);
			shootRay(x, y - 1);
			shootRay(x + 1, y - 1);
			shootRay(x - 1, y);
			shootRay(x + 1, y);
			shootRay(x - 1, y + 1);
			shootRay(x, y + 1);
			shootRay(x + 1, y + 1);
		}
		else
		{
			pix[ind - W - 1] = pix[ind];
			pix[ind - W] = pix[ind];
			pix[ind - W + 1] = pix[ind];
			pix[ind - 1] = pix[ind];
			pix[ind + 1] = pix[ind];
			pix[ind + W - 1] = pix[ind];
			pix[ind + W] = pix[ind];
			pix[ind + W + 1] = pix[ind];
		}
	}

	//main function
	public void computeImage(double time)
	{
		initFrame(time);

		geometry[0].sphere().setFlag(true);
		geometry[1].cone().setFlag(false);
		
		geometry[0].getMatrix().identity().translate(Math.sin(time), 0, Math.cos(time) + 5 * Math.sin(.5 * time) - 5);
		
		geometry[1].getMatrix().identity().translate(0, 0, 5 * Math.sin(.5 * time) - 5).rotateX(Math.PI / 2);

		//speed up by a 3x3 window
		for(int y = 1; y < H - 1; y += 3)
			for(int x = 1; x < W - 1; x += 3)
				speedUp(x, y);

		//Antialiasing.antialiasing(this);
	}
}
