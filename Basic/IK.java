import java.awt.*;
        
public class IK
{
//<i><b>-------- SOLVE TWO LINK INVERSE KINEMATICS -------------</b>

// Given a two link joint from [0,0,0] to end effector position <b>P</b>,
// let link lengths be a and b, and let norm |P| = c.  Clearly a+b >= c.
//
// Problem: find a "knee" position <b>Q</b> such that |Q| = a and |P-Q| = b.
//
// In the case of a point on the x axis <b>R</b> = [c,0,0], there is a
// closed form solution <b>S</b> = [d,e,0], where |S| = a and |R-S| = b:
//
//    d^2+e^2 = a^2                  -- because |S| = a
//    (c-d)^2+e^2 = b^2              -- because |R-S| = b
//
//    c^2-2cd+d^2+e^2 = b^2           -- combine the two equations
//    c^2-2cd = b^2 - a^2
//    c-2d = (b^2-a^2)/c
//    d - c/2 = (a^2-b^2)/c / 2
//
//    d = (c + (a^2-b^2/c) / 2      -- to solve for d and e.
//    e = sqrt(a^2-d^2)

   static double findD(double a, double b, double c) {
      return Math.max(0, Math.min(a, (c + (a*a-b*b)/c) / 2));
   }
   static double findE(double a, double d) { return Math.sqrt(a*a-d*d); } 

//<i> This leads to a solution to the more general problem:
//
//   (1) <b>R</b> = Mfwd(<b>P</b>)         -- rotate <b>P</b> onto the x axis
//   (2) Solve for <b>S</b>
//   (3) <b>Q</b> = Minv(<b>S</b>)         -- rotate back again</i>

   static double[][] Mfwd = new double[3][3], Minv = new double[3][3];

   static public boolean solve(double A, double B, double[] P, double[] D, double[] Q) {
      double[] R = new double[3];
      defineM(P,D);
      rot(Minv,P,R);
      double d = findD(A,B,norm(R));
      double e = findE(A,d);
      double[] S = {d,e,0};
      rot(Mfwd,S,Q);
      return d &gt; 0 &amp;&amp; d &lt; A;
   }

//<i> If "knee" position <b>Q</b> needs to be as close as possible to some point <b>D</b>,
// then choose M such that M(<b>D</b>) is in the y&gt;0 half of the z=0 plane.
//
// Given that constraint, define the forward and inverse of M as follows:</i>

   static void defineM(double[] P, double[] D) {
      double[] X = Minv[0], Y = Minv[1], Z = Minv[2];

//<i> M<sub>inv</sub> defines a coordinate system whose x axis contains <b>P</b>, so <b>X</b> = unit(<b>P</b>).</i>

      for (int i = 0 ; i &lt; 3 ; i++)
         X[i] = P[i];
      normalize(X);

//<i> The y axis of M<sub>inv</sub> is perpendicular to <b>P</b>, so <b>Y</b> = unit( <b>D</b> - <b>X</b>(D<b>¡¤</b>X) ).</i>

      double dDOTx = dot(D,X);
      for (int i = 0 ; i &lt; 3 ; i++)
         Y[i] = D[i] - dDOTx * X[i];
      normalize(Y);

//<i> The z axis of M<sub>inv</sub> is perpendicular to both <b>X</b> and <b>Y</b>, so <b>Z</b> = <b>X¡ÁY</b>.</i>

      cross(X,Y,Z);

//<i> M<sub>fwd</sub> = (M<sub>inv</sub>)<sup>T</sup>, since transposing inverts a rotation matrix.</i>

      for (int i = 0 ; i &lt; 3 ; i++) {
         Mfwd[i][0] = Minv[0][i];
         Mfwd[i][1] = Minv[1][i];
         Mfwd[i][2] = Minv[2][i];
      }
   }

//<i><b>------------ GENERAL VECTOR MATH SUPPORT -----------</b></i>

   static double norm(double[] v) { return Math.sqrt( dot(v,v) ); }

   static void normalize(double[] v) {
      double norm = norm(v);
      for (int i = 0 ; i &lt; 3 ; i++)
         v[i] /= norm;
   }

   static double dot(double[] a, double[] b) { return a[0]*b[0] + a[1]*b[1] + a[2]*b[2]; }

   static void cross(double[] a, double[] b, double[] c) {
      c[0] = a[1] * b[2] - a[2] * b[1];
      c[1] = a[2] * b[0] - a[0] * b[2];
      c[2] = a[0] * b[1] - a[1] * b[0];
   }

   static void rot(double[][] M, double[] src, double[] dst) {
      for (int i = 0 ; i &lt; 3 ; i++)
         dst[i] = dot(M[i],src);
   }
}