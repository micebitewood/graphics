	double[] v = new double[3];
	double[] w = new double[3];
	double[] s = { -0.1, 0.2, 0.0, 0.21 };
	double[] v_s = new double[3];
	double[] nn = new double[3];
	double[] t = new double[2];
	
boolean solveQuadraticEquation(A, B, C, double[] t) {
	discriminant = B * B - 4 * A * C;
	if (discriminant < 0)
		return false;

	d = Math.sqrt(discriminant);
	t[0] = (-B - d) / (2 * A);
	t[1] = (-B + d) / (2 * A);
	return true;
}

boolean raytrace(double[] v, double[] w, double[] t) {
	diff(v, s, v_s);

	A = 1.0;
	B = 2 * dot(w, v_s);
	C = dot(v_s, v_s) - s[3] * s[3];

	return solveQuadraticEquation(A, B, C, t);
}

void computeShading(nn) {

      // I leave this up to you to implement.
}

draw() {

	s[0] = 0.2 * Math.sin(2 * time);
	s[1] = 0.2 * Math.cos(2 * time);
	s[2] = 2.2 * Math.cos(4 * time);

	draw.drawBox(0,0,0.81, 0.61);

	draw.drawText("cols=" + ncols, 0,.67);
	draw.drawText("rows=" + nrows, -1.06, 0);

	draw.drawText("f=" + f, 0,-.67);

	for (i = 0 ; i < ncols ; i++)
		for (j = 0 ; j < nrows ; j++) {
			x = 0.8 * ((i+0.5) / ncols - 0.5);
			y = 0.8 * ((j+0.5) / ncols - 0.5 * nrows / ncols);

			draw.setColor(Color.black);
			draw.drawRect(draw.x(2 * x), draw.y(2 * y), 1, 1);

			set(v, 0, 0, f);

			set(w, x, y, -f);

			normalize(w);


			if (raytrace(v, w, t)) {
/*
            for (j = 0 ; j < 3 ; j++)
               nn[j] = v[j] + t[0] * w[j] - c[j];
            normalize(nn);
            computeShading(nn);
*/
			draw.setColor(Color.red);
			draw.fillBox(2 * x, 2 * y, 1.2 / ncols);
			}
		}
}