
public class toms708 {

	/**
	 * Computes the incomplete beta function
	 * <p>
	 * This translation was made with the aid of the Fortran-to-Java (f2j) system,
	 * developed at the University of Tennessee.
	 *<p>
	 * @author Armido Didonato, Alfred Morris (F77 version)
	 * @author John V. Burkhardt
	 * @author James Curran (j.curran@auckland.ac.nz) (Java version)
	 *
	 */
	
	public static double alnrel (double a)  {

		// 
		// c*********************************************************************72
		// c
		// cc ALNREL evaluates the function LN(1 + A).
		// c
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 
		final double p3  = -.178874546012214e-01;
		final double p2  = .405303492862024e+00;
		final double p1  = -.129418923021993e+01;
		final double q3  = -.845104217945565e-01;
		final double q2  = .747811014037616e+00;
		final double q1  = -.162752256355323e+01;
		// 


		double t = 0.0;
		double w = 0.0;
		double x = 0.0;
		double t2 = 0.0;
		if (Math.abs(a) > 0.375) {
			x  = ((1.e0+(a)));
			return (Math.log( x));
		}
		t  = (a/((a+2.0)));
		t2  = (t*t);
		w  = ((((((((((p3*t2)+p2))*t2)+p1))*t2)+1.0))/(((((((((q3*t2)+q2))*t2)+q1))*t2)+1.0)));
		return ((2.0*t)*w);
		// 		
	}

	public static double algdiv (double a, double b){

		// 
		// c*********************************************************************72
		// C
		// cc ALGDIV computes LN(GAMMA(B)/GAMMA(A+B)) when 8 <= B.
		// C
		// C
		// C     IN THIS ALGORITHM, DEL(X) IS THE FUNCTION DEFINED BY
		// C     LN(GAMMA(X))  = (X - 0.5)*LN(X) - X + 0.5*LN(2*PI) + DEL(X).
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 
		final double c5  = -.165322962780713E-02;
		final double c4  = .837308034031215E-03;
		final double c3  = -.595202931351870E-03;
		final double c2  = .793650666825390E-03;
		final double c1  = -.277777777760991E-02;
		final double c0  = .833333333333333E-01;
		// 


		double s11 = 0.0;
		double c = 0.0;
		double d = 0.0;
		double h = 0.0;
		double t = 0.0;
		double u = 0.0;
		double v = 0.0;
		double w = 0.0;
		double x = 0.0;
		double s3 = 0.0;
		double s5 = 0.0;
		double s7 = 0.0;
		double x2 = 0.0;
		double s9 = 0.0;

		if (a <= b){
			h  = a/b;
			c  = h/(1.0+h);
			x  = 1.0/(1.0+h);
			d  = b+(a-0.5);
		}else{
			h  = b/a;
			c  = 1.0/(1.0+h);
			x  = h/(1.0+h);
			d  = a+(b-0.5);
		}
		
		// C
		// C  SET SN  = (1 - X**N)/(1 - X)
		// C
		x2  = x*x;
		s3  = 1.0 + x + x2;
		s5  = 1.0 + x + x2*s3;
		s7  = 1.0 + x + x2*s5;
		s9  = 1.0 + x + x2*s7;
		s11  = 1.0 + x + x2*s9;
		// C
		// C  SET W  = DEL(B) - DEL(A + B)
		// C
		t  = (Math.pow(((1.0/b)), 2));
		w  = ((((c5*s11*t+ c4*s9)*t + c3*s7)*t + c2*s5)*t + c1*s3)*t + c0;
		w  *= (c/b);
		// C
		// C  Combine the results
		// c
		u  = d*alnrel(a/b);
		v  = a*(Math.log(b) - 1.0);
		
		if (u <= v)
			return (w-u)-v;
		
		return (w-v)-u;
	}
	
	public static double apser (double a, double b,	double x, double eps){
		// 
		// c*********************************************************************72
		// c
		// cc APSER evaluates I(1-X)(B,A) for A very small.
		// c
		// C     APSER YIELDS THE INCOMPLETE BETA RATIO I(SUB(1-X))(B,A) FOR
		// C     A <= MIN(EPS,EPS*B), B*X <= 1, AND X <= 0.5. USED WHEN
		// C     A IS VERY SMALL. USE ONLY IF ABOVE INEQUALITIES ARE SATISFIED.
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 
		final double g  = .577215664901533;
		// 

		double j = 0.0;
		double bx = 0.0;
		double c = 0.0;
		double s = 0.0;
		double t = 0.0;
		double tol = 0.0;
		double aj = 0.0;
		bx  = (b*x);
		t  = (x-bx);
		
		if (((b*eps) > 2.e-2)) {
			c  = Math.log(bx) + g + t;
		}else{
			c  = Math.log(x) + psi(b) + g + t;
		}
		
		tol  = ((5.0*eps)*Math.abs(c));
		j  = 1.0;
		s  = 0.0;
		
		do{
			j++;
			t  *= x - bx/j;
			aj = t/j;
			s  += aj;
		}while(Math.abs(aj) > tol);
		// 
		return -(a*(c+s));

	}
	
	public static double basym(double a, double b, double lambda, double eps){

		/*c*********************************************************************72
		c
		cc BASYM performs an asymptotic expansion for IX(A,B) for large A and B.
		c
		C     LAMBDA  = (A + B)*Y - B  AND EPS IS THE TOLERANCE USED.
		C     IT IS ASSUMED THAT LAMBDA IS NONNEGATIVE AND THAT
		C     A AND B ARE GREATER THAN OR EQUAL TO 15.
		C
		c  Reference:
		c
		c    Armido Didonato, Alfred Morris,
		c    Algorithm 708: 
		c    Significant Digit Computation of the Incomplete Beta Function Ratios,
		c    ACM Transactions on Mathematical Software,
		c    Volume 18, Number 3, 1992, pages 360-373.
		c*/
		//double J0, J1, LAMBDA;
		double a0[]  = new double[21]; 
		double b0[]  = new double[21];
		double c[]  = new double[21];
		double d[]  = new double[21];

		// C
		// C  NUM IS THE MAXIMUM VALUE THAT N CAN TAKE IN THE DO LOOP
		// C  ENDING AT STATEMENT 50. IT IS REQUIRED THAT NUM BE EVEN. 
		// C  THE ARRAYS A0, B0, C, D HAVE DIMENSION NUM + 1.
		// C
		final int num  = 20;
		// C
		// C  E0  = 2/SQRT(PI)
		// C  E1  = 2**(-3/2)
		// C
		final double e0  = 1.12837916709551;
		final double e1  = 0.353553390593274;
		
		double h,r0,r1,w0;

		if (a <= b){
			h  = a/b;
			r0  = 1.0/(1.0 + h);
			r1  = (b - a)/b;
			w0  = 1.0/Math.sqrt(a*(1.0 + h));
		}else{
			h  = b/a;
			r0  = 1.0/(1.0 + h);
			r1  = (b - a)/a;
			w0  = 1.0/Math.sqrt(b*(1.0 + h));
		}

		double f  = a*rlog1(-lambda/a) + b*rlog1(lambda/b);
		double t  = Math.exp(-f);
		if (t  == 0.0)
			return 0;
		
		double z0  = Math.sqrt(f);
		double z  = 0.5*(z0/e1);
		double z2  = f + f;

		a0[0]  = (2.0/3.0)*r1;
		c[0]  = - 0.5*a0[0];
		d[0]  = - c[0]; 
		double j0  = (0.5/e0)*erfc1(1,z0);
		double j1  = e1;
		double sum  = j0 + d[0]*w0*j1;

		double s  = 1.0;
		double h2  = h*h;
		double hn  = 1.0;
		double w  = w0;
		double znm1  = z;
		double zn  = z2;
	
		
		for(int n  = 2; n<= num; n+=2){
			hn  = h2*hn; 
			a0[n]  = 2.0*r0*(1.0 + h*hn)/(n + 2.0);
			int np1  = n + 1;
			s  = s + hn;
			a0[np1]  = 2.0*r1*s/(n + 3.0);
	
			for(int i  = n; i<= np1; i++){
				double r  = -0.5*(i + 1.0);
				b0[1]  = r*a0[1];
				
				for(int m  = 2; m <= i; m++){
					double bsum  = 0.0;
					int mm1  = m - 1;
					
					for(int j  = 1; j<=mm1; j++){
						int mmj  = m - j;
						bsum  = bsum + (j*r - mmj)*a0[j]*b0[mmj];
					}
					
					b0[m]  = r*a0[m] + bsum/m;
				}
				
				c[i]  = b0[i]/(i + 1.0);
		
				double dsum  = 0.0; 
				int im1  = i - 1;
				
				for(int j  = 1; j <= im1; j++){
					int imj  = i - j;
					dsum  = dsum + d[imj]*c[j];
				}
		
				d[i]  = -(dsum + c[i]);
			}

			j0  = e1*znm1 + (n - 1.0)*j0;
			j1  = e1*zn + n*j1;
			znm1  = z2*znm1;
			zn  = z2*zn; 
			w  = w0*w;
			double t0  = d[n]*w*j0;
			w  = w0*w;
			double t1  = d[np1]*w*j1;
			sum  = sum + (t0 + t1);
			if ((Math.abs(t0) + Math.abs(t1)) <= eps*sum)
				break;
		}

		double u  = Math.exp(-bcorr(a,b));
		return e0*t*u*sum;
	}
	
	public static double bcorr(double a0, double b0){
		// 
		// c*********************************************************************72
		// C
		// cc BCORR evaluates a correction term for LN(GAMMA(A)).
		// c
		// C     EVALUATION OF  DEL(A0) + DEL(B0) - DEL(A0 + B0)  WHERE
		// C     LN(GAMMA(A)) = (A - 0.5)*LN(A) - A + 0.5*LN(2*PI) + DEL(A).
		// C     IT IS ASSUMED THAT A0 .GE. 8 AND B0 .GE. 8. 
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 
		final double c5 = -.165322962780713E-02;
		final double c4 = .837308034031215E-03;
		final double c3 = -.595202931351870E-03;
		final double c2 = .793650666825390E-03;
		final double c1 = -.277777777760991E-02;
		final double c0 = .833333333333333E-01;
		// 

		double s11 = 0.0;
		double a = 0.0;
		double b = 0.0;
		double c = 0.0;
		double h = 0.0;
		double t = 0.0;
		double w = 0.0;
		double x = 0.0;
		double s3 = 0.0;
		double s5 = 0.0;
		double s7 = 0.0;
		double x2 = 0.0;
		double s9 = 0.0;
		
		a = Math.min(a0, b0);
		b = Math.max(a0, b0);

		// 
		h = a/b;
		c = h/(1.0+h);
		x = 1.0/(1.0+h);
		x2 = x*x;
		
		// C
		// C  SET SN = (1 - X**N)/(1 - X)
		// C
		s3 = 1.0+(x+x2);
		s5 = 1.0+(x+(x2*s3));
		s7 = 1.0+(x+(x2*s5));
		s9 = 1.0+(x+(x2*s7));
		s11 = 1.0+(x+(x2*s9));
		
		// C
		// C  SET W = DEL(B) - DEL(A + B)
		// C
		t = Math.pow(1.0/b, 2);
		w = ((((c5*s11*t + c4*s9)*t + c3*s7)*t + c2*s5)*t + c1*s3)*t+c0;
		w = w*(c/b);
		
		// C
		// C  COMPUTE  DEL(A) + W 
		// C
		t = Math.pow(1.0/a, 2);
		return (((((c5*t + c4)*t + c3)*t + c2)*t + c1)*t +c0)/a + w;
		// 
	}

	public static void beta_cdf_values(intW n_data, doubleW a, doubleW b, doubleW x, doubleW fx){
		// C*********************************************************************72
		// C
		// Cc BETA_CDF_VALUES returns some values of the Beta  // CDF.
		// C
		// C  Discussion:
		// C
		// C    The incomplete Beta function may be written
		// C
		// C      BETA_INC(A,B,X) = Integral (0 to X) T**(A-1) * (1-T)**(B-1) dT
		// C                      / Integral (0 to 1) T**(A-1) * (1-T)**(B-1) dT
		// C
		// C    Thus,
		// C
		// C      BETA_INC(A,B,0.0) = 0.0
		// C      BETA_INC(A,B,1.0) = 1.0
		// C
		// C    The incomplete Beta function is also sometimes  // Called the
		// C    "modified" Beta function, or the "normalized" Beta function
		// C    or the Beta  // CDF (cumulative density function.
		// C
		// C    In Mathematica, the function  // Can be evaluated by:
		// C
		// C      BETA[X,A,B] / BETA[A,B]
		// C
		// C    The function  // Can also be evaluated by using the Statistics package:
		// C
		// C      Needs["Statistics`ContinuousDistributions`"]
		// C      dist = BetaDistribution [ a, b ]
		// C       // CDF [ dist, x ]
		// C
		// C  Modified:
		// C
		// C    04 January 2006
		// C
		// C  Author:
		// C
		// C    John Burkardt
		// C
		// C  Reference:
		// C
		// C    Milton Abramowitz and Irene Stegun,
		// C    Handbook of Mathematical Functions,
		// C    US Department of  // Commerce, 1964.
		// C
		// C    Karl Pearson,
		// C    Tables of the Incomplete Beta Function,
		// C     // Cambridge University Press, 1968.
		// C
		// C    Stephen Wolfram,
		// C    The Mathematica Book,
		// C    Fourth Edition,
		// C    Wolfram Media /  // Cambridge University Press, 1999.
		// C
		// C  Parameters:
		// C
		// C    Input/output, integer N_DATA.  The user sets N_DATA to 0 before the
		// C    first  // Call.  On each  // Call, the routine increments N_DATA by 1, and
		// C    returns the  // Corresponding data; when there is no more data, the
		// C    output value of N_DATA will be 0 again.
		// C
		// C    Output, real A, B, the parameters of the function.
		// C
		// C    Output, real X, the argument of the function.
		// C
		// C    Output, real FX, the value of the function.
		// C
		// C      implicit none

		final int n_max = 42;
		
		final double a_vec[] = {
		    0.5E+00, 
		    0.5E+00, 
		    0.5E+00, 
		    1.0E+00, 
		    1.0E+00, 
		    1.0E+00, 
		    1.0E+00,
		    1.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    5.5E+00, 
		   10.0E+00, 
		   10.0E+00, 
		   10.0E+00, 
		   10.0E+00, 
		   20.0E+00, 
		   20.0E+00, 
		   20.0E+00, 
		   20.0E+00, 
		   20.0E+00, 
		   30.0E+00, 
		   30.0E+00, 
		   40.0E+00, 
		   0.1E+01, 
		    0.1E+01, 
		    0.1E+01, 
		    0.1E+01, 
		    0.1E+01, 
		    0.1E+01, 
		    0.1E+01, 
		    0.1E+01, 
		    0.2E+01, 
		    0.3E+01, 
		    0.4E+01, 
		    0.5E+01};
		
		final double b_vec[] = {
		    0.5E+00, 
		    0.5E+00, 
		    0.5E+00, 
		    0.5E+00, 
		    0.5E+00, 
		    0.5E+00, 
		    0.5E+00, 
		    1.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    2.0E+00, 
		    5.0E+00, 
		    0.5E+00, 
		    5.0E+00, 
		    5.0E+00, 
		   10.0E+00, 
		    5.0E+00, 
		   10.0E+00, 
		   10.0E+00, 
		   20.0E+00, 
		   20.0E+00, 
		   10.0E+00, 
		   10.0E+00, 
		   20.0E+00, 
		    0.5E+00, 
		    0.5E+00, 
		    0.5E+00, 
		    0.5E+00, 
		    0.2E+01, 
		    0.3E+01, 
		    0.4E+01, 
		    0.5E+01, 
		    0.2E+01, 
		    0.2E+01, 
		    0.2E+01, 
		    0.2E+01 };
		
		final double fx_vec[] = {
		   0.6376856085851985E-01, 
		   0.2048327646991335E+00, 
		   0.1000000000000000E+01, 
		   0.0000000000000000E+00, 
		   0.5012562893380045E-02, 
		   0.5131670194948620E-01, 
		   0.2928932188134525E+00, 
		   0.5000000000000000E+00, 
		   0.2800000000000000E-01, 
		   0.1040000000000000E+00, 
		   0.2160000000000000E+00, 
		   0.3520000000000000E+00, 
		   0.5000000000000000E+00, 
		   0.6480000000000000E+00, 
		   0.7840000000000000E+00, 
		   0.8960000000000000E+00, 
		   0.9720000000000000E+00, 
		   0.4361908850559777E+00, 
		   0.1516409096347099E+00, 
		   0.8978271484375000E-01, 
		   0.1000000000000000E+01, 
		   0.5000000000000000E+00, 
		   0.4598773297575791E+00, 
		   0.2146816102371739E+00, 
		   0.9507364826957875E+00, 
		   0.5000000000000000E+00, 
		   0.8979413687105918E+00, 
		   0.2241297491808366E+00, 
		   0.7586405487192086E+00, 
		   0.7001783247477069E+00, 
		   0.5131670194948620E-01, 
		   0.1055728090000841E+00, 
		   0.1633399734659245E+00, 
		   0.2254033307585166E+00, 
		   0.3600000000000000E+00, 
		   0.4880000000000000E+00, 
		   0.5904000000000000E+00, 
		   0.6723200000000000E+00, 
		   0.2160000000000000E+00, 
		   0.8370000000000000E-01, 
		   0.3078000000000000E-01, 
		   0.1093500000000000E-01};
		
		final double x_vec[] = {
		   0.01E+00, 
		   0.10E+00, 
		   1.00E+00, 
		   0.00E+00, 
		   0.01E+00, 
		   0.10E+00, 
		   0.50E+00, 
		   0.50E+00, 
		   0.10E+00, 
		   0.20E+00, 
		   0.30E+00, 
		   0.40E+00, 
		   0.50E+00, 
		   0.60E+00, 
		   0.70E+00, 
		   0.80E+00, 
		   0.90E+00, 
		   0.50E+00, 
		   0.90E+00, 
		   0.50E+00, 
		   1.00E+00, 
		   0.50E+00, 
		   0.80E+00, 
		   0.60E+00, 
		   0.80E+00, 
		   0.50E+00, 
		   0.60E+00, 
		   0.70E+00, 
		   0.80E+00, 
		   0.70E+00, 
		   0.10E+00, 
		   0.20E+00, 
		   0.30E+00, 
		   0.40E+00, 
		   0.20E+00, 
		   0.20E+00, 
		   0.20E+00, 
		   0.20E+00, 
		   0.30E+00, 
		   0.30E+00, 
		   0.30E+00, 
		   0.30E+00};
		
		if (n_data.val < 0)
			n_data.val = 0;
		
		n_data.val++;

		if (n_data.val > n_max){
			n_data.val = 0;
		
			a.val = b.val = x.val = fx.val = 0.0;
		}else{
			a.val = a_vec[n_data.val-1];
			b.val = b_vec[n_data.val-1];
			x.val = x_vec[n_data.val-1];
			fx.val = fx_vec[n_data.val-1];
		}
	}
	
	public static double betaln(double a0, double b0)  {

		// 
		// c*********************************************************************72
		// c
		// cc BETALN evaluates the logarithm of the Beta function.
		// C
		// C     E = 0.5*LN(2*PI)
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 
		final double e = .918938533204673;

		double a = 0.0;
		double b = 0.0;
		double c = 0.0;
		double h = 0.0;
		int i = 0;
		int n = 0;
		double u = 0.0;
		double v = 0.0;
		double w = 0.0;
		double z = 0.0;

		a = Math.min(a0, b0);
		b = Math.max(a0, b0);
	
		if(a < 1){
			// C
			// C  PROCEDURE WHEN A .LT. 1
			// C
			if (b < 8.0)
				return gamln(a) + algdiv(a,b);
			else
				return gamln(a)+ gamln(b) - gamln(a + b);
			
		}else if(a >= 1 && a < 8){
			// C
			// C  PROCEDURE WHEN 1 <= A .LT. 8
			// C
			if(a < 2 && b < 2){
				
				return gamln(a) + gamln(b) - gsumln(a,b);
				
			}else if(b > 2){
				w = 0.0;
				if (b < 8.0) {
					// C
					// C  REDUCTION OF B WHEN B .LT. 8
					// C
					n = (int)(b-1.0);
					z = 1.0;
	
					for (i = 1; i <= n; i++) {
						b--;
						z *= b/(a+b);
					}      
					
					return w + Math.log(z) + (gamln(a) + gamln(b) - gsumln(a,b));
				}else
					return gamln(a) + algdiv(a,b);
			}else if(a > 2){
				if ((b > 1000.0)) {
					// C
					// C  REDUCTION OF A WHEN B .GT. 1000
					// C
					n = (int)((a-1.0));
					w = 1.0;
	
					for (i = 1; i <= n; i++) {
						a--;
						w = w*(a/(1.0+a/b));
					}    
	
					return (Math.log(w) - n*Math.log(b)) + (gamln(a) + algdiv(a,b));
				}
				
				// C
				// C  REDUCTION OF A WHEN B <= 1000
				// C

				n = (int)(a-1.0);
				w = 1.0;
	
				for (i = 1; i <= n; i++) {
					a--;
					h = a/b;
					w *= h/(1.0+h);
				} 
	
				w = Math.log(w);
				if (b < 8.0){
					// C
					// C  REDUCTION OF B WHEN B .LT. 8
					// C
					n = (int)(b-1.0);
					z = 1.0;
	
					for (i = 1; i <= n; i++) {
						b--;
						z *= b/(a+b);
					}
	
					return w + Math.log(z) + (gamln(a) + gamln(b) - gsumln(a,b));
	
				}else
					return w + gamln(a) + algdiv(a,b);
			}
		}//else
		// C
		// C  PROCEDURE WHEN A .GE. 8
		// C
		w = bcorr(a,b);
		h = (a/b);
		c = (h/((1.0+h)));
		u = ((-((((a-0.5))*Math.log( c)))));
		v = b*alnrel(h);

		if (u > v)
			return (((-0.5*Math.log(b) + e) + w) - u) - v;
		else
			return (((-.5*Math.log(b) + e) + w) - v) - u;
	}
	
	public static double bfrac (double a,
			double b,
			double x,
			double y,
			double lambda,
			double eps)  {

		// 
		// c*********************************************************************72
		// c
		// cc BFRAC: continued fraction expansion for IX(A,B) when A and B are grea
		// c
		// C     IT IS ASSUMED THAT  LAMBDA = (A + B)*Y - B. 
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 

		double n = 0.0;
		double yp1 = 0.0;
		double alpha = 0.0;
		double c = 0.0;
		double e = 0.0;
		double p = 0.0;
		double r = 0.0;
		double s = 0.0;
		double t = 0.0;
		double w = 0.0;
		double c0 = 0.0;
		double c1 = 0.0;
		double anp1 = 0.0;
		double bnp1 = 0.0;
		double r0 = 0.0;
		double beta = 0.0;
		double an = 0.0;
		double bn = 0.0;

		double dResult = brcomp(a,b,x,y);
		
		if(dResult == 0.0)
			return 0;
		// 
		c = 1.0 + lambda;
		c0 = b/a;
		c1 = 1.0 + 1.0/a;
		yp1 = y + 1.0;
		// 
		n = 0.0;
		p = 1.0;
		s = (a+1.0);
		an = 0.0;
		bn = 1.0;
		anp1 = 1.0;
		bnp1 = c/c1;
		r = c1/c;
		
		// C
		// C  CONTINUED FRACTION CALCULATION 
		// C
		
		while(true){
			n++;
			t = n/a;
			w = n*(b - n)*x;
			e = a/s;
			alpha = (p*(p + c0)*e*e)*(w*x);
			e = (1.0+t)/(c1 + t + t);
			beta = n + w/s + e*(c + n*yp1);
			p = 1.0 + t;
			s = s + 2.0;
			// C
			// C  UPDATE AN, BN, ANP1, AND BNP1
			// C
			t = (alpha*an)+(beta*anp1);
			an = anp1;
			anp1 = t;
			t = (alpha*bn)+(beta*bnp1);
			bn = bnp1;
			bnp1 = t;
			// 
			r0 = r;
			r = (anp1/bnp1);
			if (Math.abs(r - r0) <= eps*r) 
				break;

			// C
			// C  RESCALE AN, BN, ANP1, AND BNP1 
			// C
			an = an/bnp1;
			bn = bn/bnp1;
			anp1 = r;
			bnp1 = 1.0;
		}
		// C
		// C  Termination 
		// c
		dResult *= r;

		return dResult;
	}
	
	public static void bgrat (double a,
			double b,
			double x,
			double y,
			doubleW w,
			double eps,
			intW ierr)  {

		// 
		// c*********************************************************************72
		// c
		// cc BGRAT uses asymptotic expansion for IX(A,B) when B < A.
		// c
		// C     ASYMPTOTIC EXPANSION FOR IX(A,B) WHEN A IS LARGER THAN B.
		// C     THE RESULT OF THE EXPANSION IS ADDED TO W. IT IS ASSUMED
		// C     THAT A .GE. 15 AND B <= 1.  EPS IS THE TOLERANCE USED.
		// C     IERR IS A VARIABLE THAT REPORTS THE STATUS OF THE RESULTS.
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 


		double j = 0.0;
		double l = 0.0;
		double lnx = 0.0;
		double nu = 0.0;
		double n2 = 0.0;
		double [] c = new double[30];
		double [] d = new double[30];
		double bm1 = 0.0;
		int nm1 = 0;
		int i = 0;
		int n = 0;
		//double p = 0.0;
		double q = 0.0;
		double r = 0.0;
		double s = 0.0;
		double t = 0.0;
		double u = 0.0;
		double v = 0.0;
		double z = 0.0;
		double sum = 0.0;
		double bp2n = 0.0;
		double t2 = 0.0;
		double coef = 0.0;
		double dj = 0.0;
		double cn = 0.0;

		bm1 = (b-0.5) - 0.5;
		nu = a + 0.5*bm1;

		if (y <= 0.375) {
			lnx = alnrel(-y);
		}else{
			lnx = Math.log(x);
		}

		z = -(nu*lnx);

		if(b*z != 0.0){
			// C
			// C  COMPUTATION OF THE EXPANSION
			// C  SET R = EXP(-Z)*Z**B/GAMMA(B)
			// C
			r = b*(1.0 + gam1(b))*Math.exp(b*Math.log(z));
			r *= Math.exp(a*lnx)*Math.exp(0.5*bm1*lnx);
			u = algdiv(b,a) + b*Math.log(nu);
			u = r*Math.exp(-u);

			if (u == 0.0){
				ierr.val = 1;
				return;
			}

			doubleW p1 = new doubleW(0);
			doubleW q1 = new doubleW(0);
			grat1(b,z,r,p1,q1,eps);
			
			// p = p1.val; // not necessary since p is never used
			q = q1.val;
			// 
			v = 0.25*Math.pow(1.0/nu, 2);
			t2 = 0.25*lnx*lnx;
			l = w.val/u;
			j = q/r;
			sum = j;
			t = 1.0;
			cn = 1.0;
			n2 = 0.0;

			for (n = 1; n <= 30; n++) {
				bp2n = b + n2;
				j = (bp2n*(bp2n + 1.0)*j + (z + bp2n + 1.0)*t)*v;
				n2 = n2 + 2.0;
				t *= t2;
				cn /= n2*(n2 + 1.0);
				c[n-1] = cn;
				s = 0.0;

				if (n != 1) {
					nm1 = n - 1;
					coef = b - n;
					for (i = 1; i <= nm1; i++) {
						s += coef*c[i - 1]*d[n - i - 1];
						coef += b;
					} 
				}

				d[n - 1] = bm1*cn + s/n;
				dj = d[n - 1]*j;
				sum += dj;

				if(sum <= 0.0){
					ierr.val = 1;
					return;
				}

				if (Math.abs(dj) <= eps*(sum + l)) {
					break;
				}
			}     
			// C
			// C  ADD THE RESULTS TO W
			// C

			ierr.val = 0;
			w.val += u*sum;
			return;
		}else{
			// C
			// C  The expansion cannot be computed
			// c
			ierr.val = 1;
			return;
		}
	}

	public static double brcomp(double a, double b, double x, double y){
		// C*********************************************************************72
		// C
		// Cc BRCOMP evaluates X**A*Y**B/BETA(A,B).
		// C
		// C  Reference:
		// C
		// C    Armido Didonato, Alfred Morris,
		// C    Algorithm 708: 
		// C    Significant Digit // Computation of the Incomplete Beta Function Ratios,
		// C    ACM Transactions on Mathematical Software,
		// C    Volume 18, Number 3, 1992, pages 360-373.
		// C
		// C
		// C  // CONST = 1/SQRT(2*PI)
		// C
		final double Const = 0.398942280401433;

		if (x == 0.0 || y == 0.0) 
			return 0;
		
		double b0;
		double a0 = Math.min(a,b);
		double lnx, lny;
		double c;
		
		if (a0 < 8.0){ 
			if (x <= 0.375){
				lnx = Math.log(x);
				lny = alnrel(-x);
			}else{
				if (y <= 0.375){
					lnx = alnrel(-y);
					lny = Math.log(y);
				}else{
					lnx = Math.log(x) ;
					lny = Math.log(y) ;
				} 

				double z = a*lnx + b*lny;

				if (a0 >= 1.0){
					z -= betaln(a,b);
					return Math.exp(z);
				}
				// C;
				// C  procedure for a < 1 or b < 1 
				// C
				b0 = Math.max(a,b);

				if (b0 < 8.0){ 
					if (b0 <= 1.0){
						// C
						// C  algorithm for b0 <= 1
						// C
						double dResult = Math.exp(z);
						if (dResult == 0.0) 
							return 0;

						double apb = a + b;
						if (apb <= 1.0){
							z = 1.0 + gam1(apb);
						}else{
							double u = a + b - 1.0;
							z = (1.0 + gam1(u))/apb ;
						}
						c = (1.0 + gam1(a))*(1.0 + gam1(b))/z;
						dResult *= (a0*c)/(1.0 + a0/b0);
						return dResult;
					}
				}else{
					double u = gamln1(a0);
					double n = b0 - 1.0;
					if (n >= 1){
						c = 1.0;
						for(int i=1; i<=n; i++){ 
							b0--;
							c *= b0/(a0 + b0); 
						}
						u = Math.log(c) + u;
					}
					z -= u;
					b0--;
					double apb = a0 + b0 ;
					double t;

					if (apb <= 1.0){
						t = 1.0 + gam1(apb);
					}else{
						u = a0 + b0 - 1.0;
						t = (1.0 + gam1(u))/apb ;
					}
					return a0*Math.exp(z)*(1.0 + gam1(b0))/t;
				}
				// C
				// C  algorithm for b0 >= 8
				// C
				double u = gamln1(a0) + algdiv(a0,b0);
				return a0*Math.exp(z - u);
			}
		}
		// C
		// C  procedure for a >= 8 and b >= 8
		// C
		double x0, y0, lambda;
		double e, h, u, v;
		if (a <= b){ 
			h = a/b;
			x0 = h/(1.0 + h);
			y0 = 1.0/(1.0 + h);
			lambda = a - (a + b)*x;
		}else{
			h = b/a;
			x0 = 1.0/(1.0 + h);
			y0 = h/(1.0 + h);
			lambda = (a + b)*y - b;
		}
		e = -lambda/a ;
		if (Math.abs(e) <= 0.6){
			u = rlog1(e);
		}else{
			u = e - Math.log(x/x0);
		}
		
		e = lambda/b;
		
		if (Math.abs(e) > 0.6){
			v = rlog1(e);
		}else{
			v = e - Math.log(y/y0);
		}

		double z = Math.exp(-(a*u + b*v));;
		return Const*Math.sqrt(b*x0)*z*Math.exp(-bcorr(a,b));

	}
	
	public static double brcmp1 (int mu, double a, double b, double x, double y ){

		// C*********************************************************************
		// C
		// C BRCMP1 evaluates EXP(MU) * (X**A*Y**B/BETA(A,B)).
		// C
		// C  Reference:
		// C
		// C    Armido Didonato, Alfred Morris,
		// C    Algorithm 708: 
		// C    Significant Digit Computation of the Incomplete Beta Function Ratios,
		// C    ACM Transactions on Mathematical Software,
		// C    Volume 18, Number 3, 1992, pages 360-373.
		// C

		// C  const = 1/sqrt(2*pi)
		// C
		final double invSqrtPi = .398942280401433;
		double c,u;
		double apb;

		double a0 = Math.min(a, b);
		
		if (a0 < 8.0){

			double lnx, lny;

			if (x <= 0.375){
				lnx = Math.log(x);
				lny = alnrel(-x);
			}else{
				if (y <= 0.375){
					lnx = alnrel(-y);
					lny = Math.log(y);
				}else{
					lnx = Math.log(x);
					lny = Math.log(y);
				}
			}

			double z = a*lnx + b*lny;
			if (a0 <= 1.0){
				z -= betaln(a,b);
				return esum(mu, z);
			}
			// C
			// C  procedure for a < 1 or b < 1 
			// C
			double b0 = Math.max(a,b);
			if (b0 >= 8.0){
				u = gamln1(a0) + algdiv(a0,b0);
				return a0*esum(mu,z - u);
			}

			if (b0 <= 1.0){
				// C
				// C  algorithm for b0 <= 1
				// C
				double dResult = esum(mu,z);

				if (dResult == 0.0) 
					return 0;

				apb = a + b;

				if (apb <= 1.0){ 
					z = 1.0 + gam1(apb);
				}else{
					u = a + b - 1.0;
					z = (1.0 + gam1(u))/apb;
				}

				c = (1.0 + gam1(a))*(1.0 + gam1(b))/z;
				dResult *= (a0*c)/(1.0 + a0/b0);
				return dResult;
			}
			// C
			// C  algorithm for 1 < b0 < 8
			// C

			u = gamln1(a0);
			int n = (int)(b0 - 1.0);

			if (n >= 1){
				c = 1.0;
				for(int i = 1; i <= n; i++){ 
					b0 -= 1.0;
					c = c*(b0/(a0 + b0)); 
				}
				u = Math.log(c) + u;
			}

			z -= u;
			b0 -= 1.0;	
			apb = a0 + b0;
			double t;

			if (apb <= 1.0){
				t = 1.0 + gam1(apb);
			}else{
				u = a0 + b0 - 1.0;
				t = (1.0 + gam1(u))/apb;
			}
			return a0*esum(mu,z)*(1.0 + gam1(b0))/t;
		}
		// C
		// C  procedure for a= 8 and b >= 8
		// C
		
		double h,x0,y0,lambda;
		
		if (a <= b){ 
			h = a/b;
			x0 = h/(1.0 + h);
			y0 = 1.0/(1.0 + h);
			lambda = a - (a + b)*x;
		}else{
			h = b/a;
			x0 = 1.0/(1.0 + h);
			y0 = h/(1.0 + h);
			lambda = (a + b)*y - b;
		}
		
		double e = -lambda/a; 
		
		if (Math.abs(e) <= 0.6){
			u = rlog1(e);
		}else{
			u = e - Math.log(x/x0);
		}

		e = lambda/b;
		double v;
		
		if (Math.abs(e) <= 0.6){
			v = rlog1(e);
		}else{
			v = e - Math.log(y/y0);
		}

		double z = esum(mu,-(a*u + b*v));
		return invSqrtPi*Math.sqrt(b*x0)*z*Math.exp(-bcorr(a,b));
	}

	public static double bpser (double a,
			double b,
			double x,
			double eps)  {

		// 
		// c*********************************************************************72
		// c
		// cc BPSER evaluates IX(A,B) when B <= 1 or B*X <= 0.7.
		// c
		// C     POWER SERIES EXPANSION FOR EVALUATING IX(A,B) WHEN B <= 1
		// C     OR B*X <= 0.7.  EPS IS THE TOLERANCE USED.
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 


		double n = 0.0;
		double apb = 0.0;
		double c = 0.0;
		int i = 0;
		int m = 0;
		double t = 0.0;
		double u = 0.0;
		double w = 0.0;
		double z = 0.0;
		double tol = 0.0;
		double sum = 0.0;
		double a0 = 0.0;
		double b0 = 0.0;
		double dResult = 0;

		if (x == 0.0){
			return 0;
		}
		// C
		// C  COMPUTE THE FACTOR X**A/(A*BETA(A,B))
		// C
		a0 = Math.min(a, b);
		if (a0 >= 1.0){
			z = a*Math.log(x) - betaln(a,b);
			dResult = Math.exp(z)/a;
		}else{
			b0 = Math.max(a, b);
			if (b0 < 8.0) {
				if (b0 <= 1.0){
					// C
					// C  PROCEDURE FOR A0 .LT. 1 AND B0 <= 1
					// C
					dResult = Math.pow(x, a);
					if (dResult == 0.0){
						return 0;
					}
					// 
					apb = (a + b);
					if (apb <= 1.0) {
						z = 1.0 + gam1(apb);
					}else{
						u = (a + b) - 1.0;
						z = (1.0 + gam1(u))/apb;
					}// 
					c = (1.0 + gam1(a))*(1.0 + gam1(b))/z;
					dResult *= c*(b/apb);
				}else{
					// C
					// C  PROCEDURE FOR A0 .LT. 1 AND 1 .LT. B0 .LT. 8
					// C
					//label40:
					//	Dummy.label("Bpser",40);
					u = gamln1(a0);
					m = (int)(b0 - 1.0);

					if (m >= 1){
						c = 1.0;
						for (i = 1; i <= m; i++){
							b0--;
							c *= b0/(a0 + b0); 
						}
						u = Math.log(c) + u;
					}

					z = a*Math.log(x) - u;
					b0--;
					apb = a0 + b0;

					if (apb <= 1.0){
						t = 1.0 + gam1(apb);
					}else{
						u = a0 + b0 - 1.0;
						t = (1.0 + gam1(u))/apb;
					}
					dResult = Math.exp(z)*(a0/a)*(1.0 + gam1(b0))/t;
				}
			}else{// b0>8
				// C
				// C  PROCEDURE FOR A0 .LT. 1 AND B0 .GE. 8
				// C
				u = gamln1(a0) + algdiv(a0,b0);
				z = a*Math.log(x) - u;
				dResult = (a0/a)*Math.exp(z);
			}
		}
		if (dResult == 0.0 || a <= 0.1*eps){
			return dResult;
		}
		// C
		// C  Compute the series
		// c
		sum = 0.0;
		n = 0.0;
		c = 1.0;
		tol = (eps/a);

		do{
			n++;
			c *= (0.5 + (0.5 - b/n))*x;
			w = c/(a + n);
			sum += w;
		}while(Math.abs(w) > tol);

		dResult *= (1.0 + (a*sum));
		return dResult;
	}

	public static void bratio (double a,
			double b,
			double x,
			double y,
			doubleW w,
			doubleW w1,
			intW ierr)  {

		// 
		// c*********************************************************************72
		// C
		// cc BRATIO evaluates the incomplete Beta function IX(A,B).
		// c
		// c  Discussion:
		// c
		// C     IT IS ASSUMED THAT A AND B ARE NONNEGATIVE, AND THAT X <= 1
		// C     AND Y = 1 - X.  BRATIO ASSIGNS W AND W1 THE VALUES
		// C
		// C                      W  = IX(A,B)
		// C                      W1 = 1 - IX(A,B) 
		// C
		// C     IERR IS A VARIABLE THAT REPORTS THE STATUS OF THE RESULTS.
		// C     IF NO INPUT ERRORS ARE DETECTED THEN IERR IS SET TO 0 AND
		// C     W AND W1 ARE COMPUTED. OTHERWISE, IF AN ERROR IS DETECTED,
		// C     THEN W AND W1 ARE ASSIGNED THE VALUE 0 AND IERR IS SET TO
		// C     ONE OF THE FOLLOWING VALUES ...
		// C
		// C        IERR = 1  IF A OR B IS NEGATIVE
		// C        IERR = 2  IF A = B = 0
		// C        IERR = 3  IF X .LT. 0 OR X .GT. 1
		// C        IERR = 4  IF Y .LT. 0 OR Y .GT. 1
		// C        IERR = 5  IF X + Y .NE. 1
		// C        IERR = 6  IF X = A = 0
		// C        IERR = 7  IF Y = B = 0
		// C
		// C     WRITTEN BY ALFRED H. MORRIS, JR.
		// C        NAVAL SURFACE WARFARE CENTER
		// C        DAHLGREN, VIRGINIA
		// C     REVISED ... NOV 1991
		// c
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// c
		// C  EPS IS A MACHINE DEPENDENT CONSTANT. EPS IS THE SMALLEST 
		// C  FLOATING POINT NUMBER FOR WHICH 1.0 + EPS .GT. 1.0
		// C



		double lambda = 0.0;
		int ind = 0;
		int n = 0;
		double t = 0.0;
		double eps = 0.0;
		double z = 0.0;
		double a0 = 0.0;
		double b0 = 0.0;
		double x0 = 0.0;
		double y0 = 0.0;
		eps = spmpar(1);
		
		
		w.val = 0.0;
		w1.val = 0.0;
		
		if (a < 0.0 || b < 0.0){
			ierr.val = 1;
			return;
		}
		
		if (a == 0.0 && b == 0.0) {
			ierr.val = 2;
			return;
		}
		
		if (x < 0.0 || x > 1.0) {
			ierr.val = 3;
			return;
		}
		
		if (y < 0.0 || y > 1.0) {
			ierr.val = 4;
			return;
		}
		
		z = ((x + y) - 0.5) - 0.5;
		if (Math.abs(z) > 3.0*eps) {
			ierr.val = 5;
			return;
		}
		
		// 
		ierr.val = 0;
		if (x == 0.0){
			if(a == 0.0) {
				ierr.val = 6;
				return;
			}else{
				w.val = 0.0;
				w1.val = 1.0;
				return;
			}
		}
		
		if (y == 0.0) {
			if (b == 0.0) {
				ierr.val = 7;
				return;
			}else{
				w.val = 1.0;
				w1.val = 0.0;
				return;
			}
		}
		
		if (a == 0.0) {
			w.val = 1.0;
			w1.val = 0.0;
			return;
		}
		
		if (b == 0.0) {
			w.val = 0.0;
			w1.val = 1.0;
			return;
		}
		
		// 
		eps = Math.max(eps, 1.E-15);
		if (Math.max(a, b) < 1.0e-3*eps) {
			w.val = b/(a + b);
			w1.val = a/(a + b);
			return;
		}
		// 
		ind = 0;
		a0 = a;
		b0 = b;
		x0 = x;
		y0 = y;
		
		if (Math.min(a0, b0) <= 1.0) {
			// C
			// C  PROCEDURE FOR A0 <= 1 OR B0 <= 1
			// C
			if (x <= 0.5) {

				ind = 1;
				a0 = b;
				b0 = a;
				x0 = y;
				y0 = x;
			}
			// 

			if(b0 < Math.min(eps, eps*a0)){
				w.val = fpser(a0,b0,x0,eps);
				w1.val = 0.5 + (0.5 - w.val);

				if (ind == 0){
					return;
				}else{
					t = w.val;
					w.val = w1.val;
					w1.val = t;
					return;
				}
			}
			if (a0 < Math.min(eps, eps*b0) && b0*x0 <= 1.0) {
				w1.val = apser(a0,b0,x0,eps);
				w.val = 0.5 + (0.5 - w1.val);

				if (ind == 0){
					return;
				}else{
					t = w.val;
					w.val = w1.val;
					w1.val = t;
					return;
				}
			}

			if (Math.max(a0, b0) <= 1.0) {
				if (a0 >= Math.min(0.2, b0) || Math.pow(x0, a0) <= 0.9) {
					w.val = bpser(a0,b0,x0,eps);
					w1.val = 0.5 + (0.5 - w.val);

					if (ind == 0){
						return;
					}else{
						t = w.val;
						w.val = w1.val;
						w1.val = t;
						return;
					}
				}

				if(x0 >= 0.3) {
					w1.val = bpser(b0,a0,y0,eps);
					w.val = 0.5 + (0.5 - w1.val);

					if (ind == 0){
						return;
					}else{
						t = w.val;
						w.val = w1.val;
						w1.val = t;
						return;
					}
				}

				n = 20;
				w1.val = bup(b0,a0,y0,x0,n,eps);
				b0 += n;

				intW ierr1 = new intW(0);
				
				bgrat(b0,a0,y0,x0,w1,(15.0*eps),ierr1);
				w.val = 0.5 + (0.5 - w1.val);

				if (ind == 0){
					return;
				}else{
					t = w.val;
					w.val = w1.val;
					w1.val = t;
					return;
				}

				// 
			}

			if (b0 <= 1.0) {
				w.val = bpser(a0,b0,x0,eps);
				w1.val = 0.5 + (0.5 - w.val);

				if (ind == 0){
					return;
				}else{
					t = w.val;
					w.val = w1.val;
					w1.val = t;
					return;
				}
			}

			if (x0 >= 0.3){
				w1.val = bpser(b0,a0,y0,eps);
				w.val = 0.5 + (0.5 - w1.val);

				if (ind == 0){
					return;
				}else{
					t = w.val;
					w.val = w1.val;
					w1.val = t;
					return;
				}
			}

			if (x0 < 0.1) {
				if (Math.pow(x0*b0, a0) <= 0.7) {
					w.val = bpser(a0,b0,x0,eps);
					w1.val = 0.5 + (0.5 - w.val);

					if (ind == 0){
						return;
					}else{
						t = w.val;
						w.val = w1.val;
						w1.val = t;
						return;
					}
				}
			}

			if (b0 > 15.0) {
				intW ierr1 = new intW(0);

				bgrat(b0,a0,y0,x0,w1,(15.0*eps),ierr1);
				w.val = 0.5 + (0.5 - w1.val);

				if (ind == 0){
					return;
				}else{
					t = w.val;
					w.val = w1.val;
					w1.val = t;
					return;
				}
			}

			n = 20;
			w1.val = bup(b0,a0,y0,x0,n,eps);
			b0 += n;

			intW ierr1 = new intW(0);

			bgrat(b0,a0,y0,x0,w1,(15.0*eps),ierr1);
			w.val = 0.5 + (0.5 - w1.val);

			if (ind == 0){
				return;
			}else{
				t = w.val;
				w.val = w1.val;
				w1.val = t;
				return;
			}

		}

		// C
		// C  PROCEDURE FOR A0 .GT. 1 AND B0 .GT. 1
		// C

		if (a <= b) {
			lambda = a - (a + b)*x;
		}else{
			lambda = (a + b)*y - b;
		}
		
		if (lambda < 0.0){
			ind = 1;
			a0 = b;
			b0 = a;
			x0 = y;
			y0 = x;
			lambda = Math.abs(lambda);
		} 
		
		if (b0 < 40.0 && b0*x0 <= 0.7){
			w.val = bpser(a0,b0,x0,eps);
			w1.val = 0.5 + (0.5 - w.val);
			
			if (ind == 0){
				return;
			}else{
				t = w.val;
				w.val = w1.val;
				w1.val = t;
				return;
			}
		}
		
		if (b0 < 40.0) {
			n = (int)b0;
			b0 -= n;
			
			if (b0 == 0.0) {
				n--;
				b0 = 1.0;
			}
			
			w.val = bup(b0,a0,y0,x0,n,eps);
			
			if (x0 <= 0.7) {
				w.val += bpser(a0,b0,x0,eps);
				w1.val = 0.5 + (0.5 - w.val);
				
				if (ind == 0){
					return;
				}else{
					t = w.val;
					w.val = w1.val;
					w1.val = t;
					return;
				}
				
			}
			if(a0 <= 15.0) {
				n = 20;
				w.val += bup(a0,b0,x0,y0,n,eps);
				a0 += n;
			}
			
			intW ierr1 = new intW(0);

			bgrat(a0,b0,x0,y0,w,(15.0*eps),ierr1);
			w1.val = 0.5 + (0.5 - w.val);
			
			if (ind == 0){
				return;
			}else{
				t = w.val;
				w.val = w1.val;
				w1.val = t;
				return;
			}
		}
		
		if(a0 <= b0){
			if (a0 <= 100.0){
				w.val = bfrac(a0,b0,x0,y0,lambda,(15.0*eps));
				w1.val = 0.5 + (0.5 - w.val);

				if (ind == 0){
					return;
				}else{
					t = w.val;
					w.val = w1.val;
					w1.val = t;
					return;
				}
			}

			if (lambda > 0.03*a0){
				w.val = bfrac(a0,b0,x0,y0,lambda,(15.0*eps));
				w1.val = 0.5 + (0.5 - w.val);

				if (ind == 0){
					return;
				}else{
					t = w.val;
					w.val = w1.val;
					w1.val = t;
					return;
				}
			}

			w.val = basym(a0,b0,lambda,(100.0*eps));
			w1.val = 0.5 + (0.5 - w.val);

			if (ind == 0){
				return;
			}else{
				t = w.val;
				w.val = w1.val;
				w1.val = t;
				return;
			}
		}else{
			if (b0 <= 100.0 || lambda > 0.03*b0) {
				w.val = bfrac(a0,b0,x0,y0,lambda,15.0*eps);
				w1.val = 0.5 + (0.5 - w.val);

				if (ind == 0){
					return;
				}else{
					t = w.val;
					w.val = w1.val;
					w1.val = t;
					return;
				}
			}


			w.val = basym(a0,b0,lambda,(100.0*eps));
			w1.val = 0.5 + (0.5 - w.val);

			if (ind == 0){
				return;
			}else{
				t = w.val;
				w.val = w1.val;
				w1.val = t;
				return;
			}
		}
	}
	
	public static double bup (double a,
			double b,
			double x,
			double y,
			int n,
			double eps)  {

		// 
		// c*********************************************************************72
		// c
		// cc BUP evaluates IX(A,B) - IX(A + N,B), where N is a positive integer.
		// c
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360 - 373.
		// c
		// C     EPS IS THE TOLERANCE USED.
		// C
		// C
		// C  OBTAIN THE SCALING FACTOR EXP(-MU) AND EXP(MU)*(X**A*Y**B/BETA(A,B))/
		// C



		double dResult;

		int mu = 0;
		double d = 1.0;
		double t = 0;
		double r = 0;
		double w = 0;

		int k;
		double l;
		double apb = a + b;
		double ap1 = a + 1.0;

		if (n != 1 && a > 1.0){
			if (apb >= 1.1*ap1){
				mu = (int)Math.abs(exparg(1));
				k = (int)exparg(0);

				if (k < mu){
					mu = k;
				}

				t = mu;
				d = Math.exp(-t);

			}
		}

		dResult = brcmp1(mu,a,b,x,y)/a;

		if (n == 1 || dResult == 0.0){
			return dResult;
		}

		int nm1 = n - 1;
		w = d;
		// C
		// C  LET K BE THE INDEX OF THE MAXIMUM TERM 
		// C
		k = 0;
		if (b <= 1.0){
			int kp1 = k + 1;

			for (int i = kp1; i <= nm1; i++) {
				l = i - 1;
				d = ((apb + l)/(ap1 + l))*x*d;
				w += d;

				if (d <= eps*w)
					break;
			}

			return dResult*w;
		}

		if (y > 1.0E-4) {
			r = (((((b - 1.0))*x)/y) - a);

			if (r < 1.0) {
				int kp1 = k + 1;

				for (int i = kp1; i <= nm1; i++) {
					l = i - 1;
					d = ((apb + l)/(ap1 + l))*x*d;
					w += d;

					if (d <= eps*w)
						break;
				}

				return dResult*w;
			}
			k = nm1;
			t = nm1;

			if (r < t) {
				k = (int)r;
			}
		}else{
			k = nm1;
		}

		// C
		// C  ADD THE INCREASING TERMS OF THE SERIES 
		// C
		for (int i = 1; i <= k; i++) {
			l = i - 1;
			d = ((apb + l)/(ap1 + l))*x*d;
			w += d;
		} 

		if (k == nm1) {
			return dResult*w;
		}

		// C
		// C  ADD THE REMAINING TERMS OF THE SERIES
		// C

		int kp1 = k + 1;

		for (int i = kp1; i <= nm1; i++) {
			l = i - 1;
			d = ((apb + l)/(ap1 + l))*x*d;
			w += d;

			if (d <= eps*w)
				break;
		}

		return dResult*w;
	}


	public static double erf(double x)  {

		// 
		// c*********************************************************************72
		// c
		// cc ERF evaluates the error function.
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 
		// 
		final double c = .564189583547756;
		// 
		final double a[] ={.771058495001320e-04,
							-.133733772997339e-02,
							.323076579225834e-01,
							.479137145607681e-01,
							.128379167095513e+00};
		
		final double b[] ={.301048631703895e-02,
							.538971687740286e-01,
							.375795757275549e+00};		// 
		
		final double p[] ={-1.36864857382717e-07,
							5.64195517478974e-01,
							7.21175825088309e+00,
							4.31622272220567e+01,
							1.52989285046940e+02,
							3.39320816734344e+02,
							4.51918953711873e+02,
							3.00459261020162e+02};
		
		final double q[] ={1.00000000000000e+00,
							1.27827273196294e+01,
							7.70001529352295e+01,
							2.77585444743988e+02,
							6.38980264465631e+02,
							9.31354094850610e+02,
							7.90950925327898e+02,
							3.00459260956983e+02};		//
		
		final double r[] ={2.10144126479064e+00,
							2.62370141675169e+01,
							2.13688200555087e+01,
							4.65807828718470e+00,
							2.82094791773523e-01};	
		
		final double s[] ={9.41537750555460e+01,
							1.87114811799590e+02,
							9.90191814623914e+01,
							1.80124575948747e+01};		// 


		double ax = 0.0;
		double bot = 0.0;
		double t = 0.0;
		double top = 0.0;
		double x2 = 0.0;
		
		ax = Math.abs(x);
		if (ax <= 0.5) {
			t = x*x;
			top = (((a[0]*t + a[1])*t + a[2])*t + a[3])*t + a[4] + 1.0;
			bot = ((b[0]*t + b[1])*t + b[2])*t + 1.0;
			return x*(top/bot);
			// 
		}else{
			if (ax <= 4.0){
				top = ((((((p[0]*ax + p[1])*ax + p[2])*ax + p[3])*ax + p[4])*ax + p[5])*ax + p[6])*ax + p[7];
				bot = ((((((q[0]*ax + q[1])*ax + q[2])*ax + q[3])*ax + q[4])*ax + q[5])*ax + q[6])*ax + q[7];

				return ((x<0) ? -(0.5 + (0.5 - Math.exp(-x*x)*top/bot)) : (0.5 + (0.5 - Math.exp(-x*x)*top/bot))); 
			}else{
				if (ax < 5.8){
					x2 = x*x;
					t = 1.0/x2;
					top = ((((r[0]*t) + r[1])*t + r[2])*t + r[3])*t + r[4];
					bot = ((((s[0]*t) + s[2])*t + s[3])*t + s[3])*t + 1.0;
					double dResult = (c - top/(x2*bot))/ax;				
					dResult = 0.5 + (0.5 - Math.exp(-x2)*dResult);
					
					return (x < 0 ? -dResult : dResult);
				}else{
					return sign(1.0,x);
				}
			}
		}
	}


	public static double erfc1(int ind,	double x){

		// 
		// c*********************************************************************72
		// c
		// cc ERFC1 evaluates the complementary error function.
		// C
		// C          ERFC1(IND,X) = ERFC(X)            IF IND = 0
		// C          ERFC1(IND,X) = EXP(X*X)*ERFC(X)   OTHERWISE
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 
		// 
		final double c = .564189583547756;
		// 
		final double a[] = {.771058495001320e-04,
					-.133733772997339e-02,
					.323076579225834e-01,
					.479137145607681e-01,
					.128379167095513e+00};
					
		final double b[] = {.301048631703895e-02,
				.538971687740286e-01,
				.375795757275549e+00};
					
		final double p[] = {-1.36864857382717e-07,
				5.64195517478974e-01,
				7.21175825088309e+00,
				4.31622272220567e+01,
				1.52989285046940e+02,
				3.39320816734344e+02,
				4.51918953711873e+02,
				3.00459261020162e+02};

		final double q[] = {1.00000000000000e+00,
				1.27827273196294e+01,
				7.70001529352295e+01,
				2.77585444743988e+02,
				6.38980264465631e+02,
				9.31354094850610e+02,
				7.90950925327898e+02,
				3.00459260956983e+02};
		// 
		final double r[] = {2.10144126479064e+00,
				2.62370141675169e+01,
				2.13688200555087e+01,
				4.65807828718470e+00,
				2.82094791773523e-01};
					
		final double s[] =  {9.41537750555460e+01,
				1.87114811799590e+02,
				9.90191814623914e+01,
				1.80124575948747e+01};
		
		// C
		// C  ABS(X) <= 0.5
		// C



		double dResult = 0.0;
		double w = 0.0;
		double ax = 0.0;
		double e = 0.0;
		double bot = 0.0;
		double t = 0.0;
		double top = 0.0;
		ax = Math.abs(x);
		
		if(ax < 0.5){
			t = x*x;
			top = (((a[0]*t+a[1])*t+a[2])*t+a[3])*t+a[4]+1.0;
			bot = (((b[0]*t+b[1])*t)+b[2])*t+1.0;
			dResult = 0.5+(0.5-x*(top/bot));

			if (ind != 0) 
				dResult *= Math.exp(t);
			
			return dResult;
		}else if(ax <= 4 && ax >= 0.5){ // I don't think the and is necessary - being safe JMC
		// C
		// C  0.5 .LT. ABS(X) <= 4
		// C
			top = ((((((p[0]*ax + p[1])*ax + p[2])*ax + p[3])*ax + p[4])*ax + p[5])*ax + p[6])*ax + p[7];
			bot = ((((((q[0]*ax + q[2])*ax + q[2])*ax + q[3])*ax + q[5])*ax + q[5])*ax + q[6])*ax + q[7];
			dResult = top/bot;
		}else{
			// C
			// C  ABS(X) .GT. 4
			// C
			if ((x <= -5.6)) {
				// C
				// C  LIMIT VALUE FOR LARGE NEGATIVE X
				// C
				if (ind != 0)
					return 2.0*Math.exp(x*x);
				else
					return 2.0;
			}
		
			if (ind != 0){
				t = Math.pow(1.0/x, 2);
				top = (((r[0]*t+r[1])*t+r[2])*t+r[3])*t+r[4];
				bot = (((s[0]*t+s[2])*t+s[3])*t+s[3])*t+1.0;
				dResult = ((c-((t*top)/bot)))/ax;
			}else{
				if (x > 100.0 || x*x > -exparg(1)){
					// C
					// C  LIMIT VALUE FOR LARGE POSITIVE X WHEN IND = 0
					// C
					return 0;
				}
			}
		}
		 
		
		// C
		// C  FINAL ASSEMBLY
		// C
		if (ind != 0){
			if (x < 0.0)
				dResult = 2.0*Math.exp(x*x) - dResult;
			return dResult;
		}

		w = x*x;
		t = w;
		e = w-t;
		dResult *= (0.5+(0.5-e))*Math.exp(-t);
		
		if (x < 0.0)
			dResult = 2.0 - dResult;
		
		return dResult;
	}
	
	public static double esum (int mu,
			double x)  {

		// 
		// c*********************************************************************72
		// c
		// cc ESUM evaluates EXP(MU + X).
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360 - 373.
		// c
		// 
		double w = 0.0;

		if (x <= 0.0){
			if (mu < 0) {
				w = mu;
				return Math.exp(w)*Math.exp(x);
			}

			w = mu + x;

			if (w > 0.0) {
				w = mu;
				return Math.exp(w)*Math.exp(x);
			}
			return Math.exp(w);

		}
		
		if (mu > 0) {
			w = mu;
			return Math.exp(w)*Math.exp(x);
		}

		w = mu + x;
		if (w < 0.0) {
			w = mu;
			return Math.exp(w)*Math.exp(x);
		}

		return Math.exp(w);
	}

	public void erf_values(intW n_data, doubleW x, doubleW fx){

		// C*********************************************************************72
		// C
		// C ERF_VALUES returns some values of the ERF or "error" function for testing.
		// C
		// CModified:
		// C
		// C17 April 2001
		// C
		// CAuthor:
		// C
		// CJohn Burkardt
		// C
		// CParameters:
		// C
		// CInput/output, integer N_DATA.
		// COn input, if N_DATA is 0, the first test data is returned, and
		// CN_DATA is set to the index of the test data.  On each subsequent
		// Call, N_DATA is incremented and that test data is returned.  When
		// Cthere is no more test data, N_DATA is set to 0.
		// C
		// COutput, real X, the argument of the function.
		// C
		// COutput, real FX, the value of the function.
		// C
		// 

		final int nmax = 21;


		final	double	bvec[]	={
				0.0000000000E+00,	
				0.1124629160E+00,	
				0.2227025892E+00,	
				0.3286267595E+00,	
				0.4283923550E+00,	
				0.5204998778E+00,	
				0.6038560908E+00,	
				0.6778011938E+00,	
				0.7421009647E+00,	
				0.7969082124E+00,	
				0.8427007929E+00,	
				0.8802050696E+00,	
				0.9103139782E+00,	
				0.9340079449E+00,	
				0.9522851198E+00,	
				0.9661051465E+00,	
				0.9763483833E+00,	
				0.9837904586E+00,	
				0.9890905016E+00,	
				0.9927904292E+00,	
				0.9953222650E+00};

		final	double	xvec[] =	{
				0.0E+00,	
				0.1E+00,	
				0.2E+00,	
				0.3E+00,	
				0.4E+00,	
				0.5E+00,	
				0.6E+00,	
				0.7E+00,	
				0.8E+00,	
				0.9E+00,	
				1.0E+00,	
				1.1E+00,	
				1.2E+00,	
				1.3E+00,	
				1.4E+00,	
				1.5E+00,	
				1.6E+00,	
				1.7E+00,	
				1.8E+00,	
				1.9E+00,	
				2.0E+00	};

		if( n_data.val < 0)
			n_data.val = 0;

		n_data.val++;

		if (nmax < n_data.val ){
			n_data.val = 0;
			x.val = 0.0;
			fx.val = 0.0;
		}else{
			x.val = xvec[n_data.val-1];
			fx.val = bvec[n_data.val-1];
		}
	}
	
	public static double exparg (int l){

		// 
		// c*********************************************************************72
		// c
		// cc EXPARG returns the largest value for which EXP can be computed.
		// c
		// C     IF L = 0 THEN  EXPARG(L) = THE LARGEST POSITIVE W FOR WHICH
		// C     EXP(W) CAN BE COMPUTED. 
		// C
		// C     IF L IS NONZERO THEN  EXPARG(L) = THE LARGEST NEGATIVE W FOR
		// C     WHICH THE COMPUTED VALUE OF EXP(W) IS NONZERO.
		// C
		// C     NOTE... ONLY AN APPROXIMATE VALUE FOR EXPARG(L) IS NEEDED.
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 



		double lnb = 0.0;
		int m = 0;
		int b = ipmpar(4);

		switch(b){
			case 2:
				lnb = .69314718055995;
				break;
			case 8:
				lnb = 2.0794415416798;
				break;
			case 16:
				lnb = 2.7725887222398;
				break;
			default:
				lnb = Math.log(b);
				break;
		}
		// 
		
		if (l != 0){
			m = ipmpar(6);
			return 0.99999*m*lnb;
		}else{
			m = ipmpar(7);
			return 0.99999*m*lnb;
		}
	}
	
	public static double fpser (double a,
			double b,
			double x,
			double eps)  {

		// 
		// c*********************************************************************72
		// c
		// cc FPSER uses a series for IX(A,B) with B < min(eps,eps*A) and X <= 0.5.
		// c
		// c  Discussion:
		// c
		// C                 EVALUATION OF IX(A,B) 
		// C
		// C          FOR B .LT. MIN(EPS,EPS*A) AND X .LE. 0.5.
		// C
		// C
		// C                  SET  FPSER = X**A
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360 - 373.
		// c
		// 



		double c = 0.0;
		double s = 0.0;
		double t = 0.0;
		double tol = 0.0;
		double an = 0.0;
		
		double dResult = 1.0;
		
		if (a > 1.0e-3*eps) {
			dResult = 0.0;
			t = a*Math.log(x);
			if (t < exparg(1)){
				return dResult;
			}
			dResult = Math.exp(t);
		}
		
		// C
		// C  NOTE THAT 1/B(A,B) = B 
		// C
		dResult = (b/a)*dResult;
		tol = eps/a;
		an = a + 1.0;
		t = x;
		s = t/an;

		do{
			an = an + 1.0;
			t = x*t;
			c = t/an;
			s = s + c;
		}while(Math.abs(c) > tol);
		// 
		return dResult*(1.0 + a*s);
	}
	
	public static double gam1 (double a)  {

		// 
		// c*********************************************************************72
		// c
		// cc GAM1 evaluates 1/GAMMA(A+1) - 1  for -0.5 <= A <= 1.5
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		
		final double p[] = {.577215664901533e+00,
		-.409078193005776e+00,
		-.230975380857675e+00,
		.597275330452234e-01,
		.766968181649490e-02,
		-.514889771323592e-02,
		.589597428611429e-03};
		
		final double q[] = {.100000000000000e+01,
				.427569613095214e+00,
				.158451672430138e+00,
				.261132021441447e-01,
				.423244297896961e-02};		
		
		final double r[] ={-.422784335098468e+00,
				-.771330383816272e+00,
				-.244757765222226e+00,
				.118378989872749e+00,
				.930357293360349e-03,
				-.118290993445146e-01,
				.223047661158249e-02,
				.266505979058923e-03,
				-.132674909766242e-03		 
		};
		
		final double s2 = .559398236957378e-01;
		final double s1 = .273076135303957e+00;
		// 

		double d = 0.0;
		double bot = 0.0;
		double t = 0.0;
		double w = 0.0;
		double top = 0.0;

		t = a;
		d = a - 0.5;
		if (d > 0.0) {
			t = d - 0.5;
		}
		
		/*{
			double _arif_tmp = t;
			if (_arif_tmp < 0)  
				Dummy.go_to("Gam1",30);
			else if (_arif_tmp == 0)  
				Dummy.go_to("Gam1",10);
			else   Dummy.go_to("Gam1",20);
		}*/
		// 

		if(t==0){
			return 0;
		}else if(t > 0){
			top = (((((p[6]*t + p[5])*t + p[4])*t + p[3])*t + p[2])*t + p[1])*t+ p[0];
			bot = ((((q[4]*t) + q[3])*t + q[2])*t + q[1])*t + 1.0;
			w = top/bot;
			
			if(d <= 0.0) {
				return a*w;
			}else{
				return (t/a)*((w - 0.5) - 0.5);
			} 
		}else{ // t < 0
			top = ((((((((r[8]*t) + r[7])*t + r[6])*t + r[5])*t + r[4])*t + r[3])*t + r[2])*t + r[1])*t + r[0];
			bot = (s2*t + s1)*t + 1.0;
			w = top/bot;
			
			if (d <= 0.0) {
				return a*((w + 0.5) + 0.5);
			}else{
				return t*w/a;
			}
		}
	}
	
	public static void grat1 (double a,
			double x,
			double r,
			doubleW p,
			doubleW q,
			double eps)  {

		// 
		// c*********************************************************************72
		// c
		// cc GRAT1 evaluates P(A,X) and Q(A,X) when A <= 1.
		// c
		// C        EVALUATION OF THE INCOMPLETE GAMMA RATIO FUNCTIONS 
		// C                      P(A,X) AND Q(A,X)
		// C
		// C     IT IS ASSUMED THAT A <= 1.  EPS IS THE TOLERANCE TO BE USED.
		// C     THE INPUT ARGUMENT R HAS THE VALUE E**(-X)*X**A/GAMMA(A).
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 



		double j = 0.0;
		double l = 0.0;
		double am0 = 0.0;
		double an0 = 0.0;
		double a2n = 0.0;
		double b2n = 0.0;
		double cma = 0.0;
		double c = 0.0;
		double g = 0.0;
		double h = 0.0;
		double t = 0.0;
		double w = 0.0;
		double z = 0.0;
		double tol = 0.0;
		double sum = 0.0;
		double a2nm1 = 0.0;
		double b2nm1 = 0.0;
		double an = 0.0;

		if (a*x == 0.0){
			if (x <= a) {
				p.val = 0.0;
				q.val = 1.0;
				return;
			}else{
				p.val = 1.0;
				q.val = 0.0;
				return;
			}
		}

		if (a == 0.5){
			if (x < 0.25){
				p.val = erf(Math.sqrt(x));
				q.val = 0.5 + (0.5 - p.val);
				return;
			}else{
				q.val = erfc1(0, Math.sqrt(x));
				p.val = 0.5 + (0.5 - q.val);
				return;
			}
		}

		if (x < 1.1){
			// C
			// C  TAYLOR SERIES FOR P(A,X)/X**A
			// C
			an = 3.0;
			c = x;
			sum = x/(a + 3.0);
			tol = (0.1*eps)/(a + 1.0);

			do{
				an++;
				c = -c*(x/an);
				t = c/(a + an);
				sum = (sum+t);
			}while(Math.abs(t) > tol);

			j = a*x*((sum/6.0 - 0.5/(a + 2.0))*x + 1.0/(a + 1.0));
			// 
			z = a*Math.log(x);
			h = gam1(a);
			g = 1.0 + h;

			if (x < 0.25){
				if (z > -.13394){
					l = rexp(z);
					w = 0.5 + (0.5 + l);
					q.val = (w*j - l)*g -h;

					if (q.val < 0.0) {
						p.val = 1.0;
						q.val = 0.0;
						return;
					}

					p.val = 0.5+ (0.5 - q.val);
					return;	
				}else{
					w = Math.exp(z);
					p.val = w*g*(0.5 + (0.5 - j));
					q.val = 0.5 + (0.5 - p.val);
					return;
				}
			}else{
				w = Math.exp(z);
				p.val = w*g*(0.5 + (0.5 - j));
				q.val = 0.5 + (0.5 - p.val);
				return;
			}// 
		}else{
			// C
			// C  CONTINUED FRACTION EXPANSION
			// C

			a2nm1 = 1.0;
			a2n = 1.0;
			b2nm1 = x;
			b2n = x + (1.0 - a);
			c = 1.0;

			do{
				a2nm1 = x*a2n + c*a2nm1;
				b2nm1 = x*b2n + c*b2nm1;
				am0 = a2nm1/b2nm1;
				c++;
				cma = c - a;
				a2n = a2nm1 + cma*a2n;
				b2n = b2nm1 + cma*b2n;
				an0 = a2n/b2n;
			}while(Math.abs(an0 - am0) >= eps*an0);

			q.val = r*an0;
			p.val = 0.5 + (0.5 - q.val);
			return;
		}
	}

	
	public static int ipmpar(int i){
		// c*********************************************************************72
		// C
		// cc IPMPAR sets integer machine constants.
		// c
		// C     IPMPAR PROVIDES THE INTEGER MACHINE CONSTANTS FOR THE COMPUTER
		// C     THAT IS USED. IT IS ASSUMED THAT THE ARGUMENT I IS AN INTEGER
		// C     HAVING ONE OF THE VALUES 1-10. IPMPAR(I) HAS THE VALUE ...
		// C
		// C  INTEGERS.
		// C
		// C     ASSUME INTEGERS ARE REPRESENTED IN THE N-DIGIT, BASE-A FORM
		// C
		// C               SIGN ( X(N-1)*A**(N-1) + ... + X(1)*A + X(0) )
		// C
		// C               WHERE 0 <= X(I) .LT. A FOR I =0,...,N-1.
		// C
		// C     IPMPAR(1)  = A, THE BASE.
		// C
		// C     IPMPAR(2)  = N, THE NUMBER OF BASE-A DIGITS. 
		// C
		// C     IPMPAR(3)  = A**N - 1, THE LARGEST MAGNITUDE.
		// C
		// C  FLOATING-POINT NUMBERS.
		// C
		// C     IT IS ASSUMED THAT THE SINGLE AND DOUBLE PRECISION doubleING
		// C     POINT ARITHMETICS HAVE THE SAME BASE, SAY B, AND THAT THE
		// C     NONZERO NUMBERS ARE REPRESENTED IN THE FORM 
		// C
		// C               SIGN (B**E) * (X(1)/B + ... + X(M)/B**M)
		// C
		// C               WHERE X(I)  = 0,1,...,B-1 FOR I =1,...,M,
		// C               X(1) .GE. 1, AND EMIN <= E <= EMAX.
		// C
		// C     IPMPAR(4)  = B, THE BASE.
		// C
		// C  SINGLE-PRECISION 
		// C
		// C     IPMPAR(5)  = M, THE NUMBER OF BASE-B DIGITS. 
		// C
		// C     IPMPAR(6)  = EMIN, THE SMALLEST EXPONENT E.
		// C
		// C     IPMPAR(7)  = EMAX, THE LARGEST EXPONENT E.
		// C
		// C  DOUBLE-PRECISION 
		// C
		// C     IPMPAR(8)  = M, THE NUMBER OF BASE-B DIGITS. 
		// C
		// C     IPMPAR(9)  = EMIN, THE SMALLEST EXPONENT E.
		// C
		// C     IPMPAR(10)  = EMAX, THE LARGEST EXPONENT E.
		// C
		// C
		// C
		// C     TO DEFINE THIS FUNCTION FOR THE COMPUTER BEING USED, ACTIVATE
		// C     THE DATA STATMENTS FOR THE COMPUTER BY REMOVING THE C FROM
		// C     COLUMN 1. (ALL THE OTHER DATA STATEMENTS SHOULD HAVE C IN
		// C     COLUMN 1.)
		// C
		// C
		// C
		// C     IPMPAR IS AN ADAPTATION OF THE FUNCTION I1MACH, WRITTEN BY
		// C     P.A. FOX, A.D. HALL, AND N.L. SCHRYER (BELL LABORATORIES).
		// C     IPMPAR WAS FORMED BY A.H. MORRIS (NSWC). THE CONSTANTS ARE
		// C     FROM BELL LABORATORIES, NSWC, AND OTHER SOURCES.
		// C
		final int imach[]  = {
				2,
				31,
				2147483647,
				2,
				24,
				-125,
				128,
				53,
				-1021,
				1024
		};

		return imach[i-1];
	}
	
	public void gamma_inc_values(intW n_data, doubleW a, doubleW x, doubleW fx){

		// C *********************************************************************72
		// C 
		// C  GAMMA_INC_VALUES returns some values of the incomplete Gamma function.
		// C 
		// C   Discussion:
		// C 
		// C     The (normalized) incomplete Gamma function P(A,X) is defined as:
		// C 
		// C       PN(A,X) = 1/Gamma(A) * Integral ( 0 <= T <= X ) T**(A-1) * exp(-T) dT.
		// C 
		// C     With this definition, for all A and X,
		// C 
		// C       0 <= PN(A,X) <= 1
		// C 
		// C     and
		// C 
		// C       PN(A,INFINITY) = 1.0
		// C 
		// C     In Mathematica, the function can be evaluated by:
		// C 
		// C       1 - GammaRegularized[A,X]
		// C 
		// C   Modified:
		// C 
		// C     28 August 2004
		// C 
		// C   Author:
		// C 
		// C     John Burkardt
		// C 
		// C   Reference:
		// C 
		// C     Milton Abramowitz and Irene Stegun,
		// C     Handbook of Mathematical Functions,
		// C     US Department of Commerce, 1964.
		// C 
		// C     Stephen Wolfram,
		// C     The Mathematica Book,
		// C     Fourth Edition,
		// C     Wolfram Media / Cambridge University Press, 1999.
		// C 
		// C   Parameters:
		// C 
		// C     Input/output, integer N_DATA.  The user sets N_DATA to 0 before the
		// C     first call.  On each call, the routine increments N_DATA by 1, and
		// C     returns the corresponding data; when there is no more data, the
		// C     output value of N_DATA will be 0 again.
		// C 
		// C     Output, real ( kind = 4 ) A, the parameter of the function.
		// C 
		// C     Output, real ( kind = 4 ) X, the argument of the function.
		// C 
		// C     Output, real ( kind = 4 ) FX, the value of the function.
		// C 
		final int n_max = 20;

		final double a_vec[] = {
			0.10E+00,	
			0.10E+00,	
			0.10E+00,	
			0.50E+00,	
			0.50E+00,	
			0.50E+00,	
			0.10E+01,	
			0.10E+01,	
			0.10E+01,	
			0.11E+01,	
			0.11E+01,	
			0.11E+01,	
			0.20E+01,	
			0.20E+01,	
			0.20E+01,	
			0.60E+01,	
			0.60E+01,	
			0.11E+02,	
			0.26E+02,	
			0.41E+02};
		
		final double fx_vec[] ={
			0.7382350532339351E+00,	
			0.9083579897300343E+00,	
			0.9886559833621947E+00,	
			0.3014646416966613E+00,	
			0.7793286380801532E+00,	
			0.9918490284064973E+00,	
			0.9516258196404043E-01,	
			0.6321205588285577E+00,	
			0.9932620530009145E+00,	
			0.7205974576054322E-01,	
			0.5891809618706485E+00,	
			0.9915368159845525E+00,	
			0.01018582711118352E+00,	
			0.4421745996289254E+00,	
			0.9927049442755639E+00,	
			0.4202103819530612E-01,	
			0.9796589705830716E+00,	
			0.9226039842296429E+00,	
			0.4470785799755852E+00,	
			0.7444549220718699E+00};

		final	double	x_vec[]	=	{
			0.30E-01,	
			0.30E+00,	
			0.15E+01,	
			0.75E-01,	
			0.75E+00,	
			0.35E+01,	
			0.10E+00,	
			0.10E+01,	
			0.50E+01,	
			0.10E+00,	
			0.10E+01,	
			0.50E+01,	
			0.15E+00,	
			0.15E+01,	
			0.70E+01,	
			0.25E+01,	
			0.12E+02,	
			0.16E+02,	
			0.25E+02,	
			0.45E+02};

		if (n_data.val < 0)
			n_data.val = 0;
		
		n_data.val++;

		if (n_max < n_data.val){
			n_data.val = 0;
			a.val = 0.0E+00;
			x.val = 0.0E+00;
			fx.val = 0.0E+00;
		}else{
			a.val = a_vec[n_data.val-1];
			x.val = x_vec[n_data.val-1];
			fx.val = fx_vec[n_data.val-1];
		}
	}
	
	public void gamma_log_values (intW n_data, doubleW x, doubleW fx){

		// C *********************************************************************72
		// C 
		// C  GAMMA_LOG_VALUES returns some values of the Log Gamma function for testing.
		// C 
		// C   Modified:
		// C 
		// C     17 April 2001
		// C 
		// C   Author:
		// C 
		// C     John Burkardt
		// C 
		// C   Reference:
		// C 
		// C     Milton Abramowitz and Irene Stegun,
		// C     Handbook of Mathematical Functions,
		// C     US Department of Commerce, 1964.
		// C 
		// C   Parameters:
		// C 
		// C     Input/output, integer N_DATA.
		// C     On input, if N_DATA is 0, the first test data is returned, and
		// C     N_DATA is set to the index of the test data.  On each subsequent
		// C     call, N_DATA is incremented and that test data is returned.  When
		// C     there is no more test data, N_DATA is set to 0.
		// C 
		// C     Output, real X, the argument of the function.
		// C 
		// C     Output, real FX, the value of the function.
		// C 
		final int nmax = 18;

		final	double	bvec[]	=	{
				1.524064183E+00,	0.7966780066E+00,	0.3982337117E+00,	
				0.1520599127E+00,	0.000000000E+00,	-0.04987246543E+00,	
				-0.08537410945E+00,	-0.1081747934E+00,	-0.1196128950E+00,	
				-0.1207822040E+00,	-0.1125917658E+00,	-0.09580771625E+00,	
				-0.07108385116E+00,	-0.03898428380E+00,	0.000000000E+00,	
				12.80182743E+00,	39.33988571E+00,	71.25704193E+00	};

		final	double	xvec[]	=	{
				0.2E+00,	0.4E+00,	0.6E+00,	0.8E+00,	
				1.0E+00,	1.1E+00,	1.2E+00,	1.3E+00,	
				1.4E+00,	1.5E+00,	1.6E+00,	1.7E+00,	
				1.8E+00,	1.9E+00,	2.0E+00,	10.0E+00,	
				20.0E+00,	30.0E+00};

		if (n_data.val < 0)
			n_data.val = 0;

		n_data.val++;

		if (nmax < n_data.val){
			n_data.val = 0;
			x.val = 0.0E+00;
			fx.val = 0.0E+00;
		}else{
			x.val = xvec[n_data.val-1];
			fx.val = bvec[n_data.val-1];
		}
	}
	
	public static double gamln (double a)  {

		// 
		// c*********************************************************************72
		// c
		// cc GAMLN evaluates LN(GAMMA(A)) for positive A.
		// C
		// C     WRITTEN BY ALFRED H. MORRIS
		// C          NAVAL SURFACE WARFARE CENTER 
		// C          DAHLGREN, VIRGINIA 
		// C
		// C     D = 0.5*(LN(2*PI) - 1)
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 
		final double d = .418938533204673;
		// 
		final double c5 = -.165322962780713e-02;
		final double c4 = .837308034031215e-03;
		final double c3 = -.595202931351870e-03;
		final double c2 = .793650666825390e-03;
		final double c1 = -.277777777760991e-02;
		final double c0 = .833333333333333e-01;
		// 
		int i = 0;
		int n = 0;
		double t = 0.0;
		double w = 0.0;

		if (a <= 0.8)
			return gamln1(a) - Math.log(a);
	
		if (a <= 2.25){
			t = (a-0.5)-0.5;
			return gamln1(t);
		}
		
		if (a < 10.0){
			n = (int)(a - 1.25);
			t = a;
			w = 1.0;
			
			for (i = 1; i <= n; i++) {
					t--;
					w *= t;
			} 
		
			return gamln1(t - 1.0) + Math.log(w);
		}
		
		t = Math.pow(1.0/a, 2);
		w = ((((((c5*t + c4)*t + c3)*t + c2)*t +c1)*t +c0)/a);
		return (d + w) + (a - 0.5)*(Math.log(a) - 1.0);
	}
	
	public static double gamln1 (double a)  {

		// 
		// c*********************************************************************72
		// c
		// cc GAMLN1 evaluates LN(GAMMA(1 + A)) for -0.2 <= A <= 1.25
		// c
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 
		final double p6 = -.271935708322958e-02;
		final double p5 = -.673562214325671e-01;
		final double p4 = -.402055799310489e+00;
		final double p3 = -.780427615533591e+00;
		final double p2 = -.168860593646662e+00;
		final double p1 = .844203922187225e+00;
		final double p0 = .577215664901533e+00;
		final double q6 = .667465618796164e-03;
		final double q5 = .325038868253937e-01;
		final double q4 = .361951990101499e+00;
		final double q3 = .156875193295039e+01;
		final double q2 = .312755088914843e+01;
		final double q1 = .288743195473681e+01;
		// c
		final double r5 = .497958207639485e-03;
		final double r4 = .170502484022650e-01;
		final double r3 = .156513060486551e+00;
		final double r2 = .565221050691933e+00;
		final double r1 = .848044614534529e+00;
		final double r0 = .422784335098467e+00;
		final double s5 = .116165475989616e-03;
		final double s4 = .713309612391000e-02;
		final double s3 = .101552187439830e+00;
		final double s2 = .548042109832463e+00;
		final double s1 = .124313399877507e+01;
		// 

		double w = 0.0;
		double x = 0.0;

		if (a < 0.6){
			w = ((((((p6*a + p5)*a + p4)*a + p3)*a + p2)*a + p1)*a + p0)/
			    ((((((q6*a + q5)*a + q4)*a + q3)*a + q2)*a + q1)*a + 1.0);
			
			return -a*w;
		}
		
		x = (a - 0.5) - 0.5;
		w = (((((r5*x + r4)*x + r3)*x + r2)*x + r1)*x + r0)/
		    (((((s5*x + s4)*x + s3)*x + s2)*x + s1)*x + 1.0);
		
		return x*w;
	}

	public static double gsumln (double a, double b)  {

		// 
		// c*********************************************************************72
		// c
		// cc GSUMLN evaluates LN(GAMMA(A + B)) for 1 <= A <= 2 and 1 <= B <= 2.
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 
		double x = a + b - 2;
		
		if (x <=0.25)
			return gamln1(1.0 + x);
		
		if (x <= 1.25)
			return gamln1(x) + alnrel(x);
		
		return gamln1(x - 1.0) + Math.log(x*(1.0+x));
	}

	public static double psi(double xx){
		// 
		// c*********************************************************************72
		// C
		// cc PSI evaluates the Digamma function.
		// c
		// C
		// C     PSI(XX) IS ASSIGNED THE VALUE 0 WHEN THE DIGAMMA FUNCTION CANNOT
		// C     BE COMPUTED.
		// C
		// C     THE MAIN COMPUTATION INVOLVES EVALUATION OF RATIONAL CHEBYSHEV
		// C     APPROXIMATIONS PUBLISHED IN MATH. COMP. 27, 123-127(1973) BY
		// C     CODY, STRECOK AND THACHER.
		// C
		// C
		// C     PSI WAS WRITTEN AT ARGONNE NATIONAL LABORATORY FOR THE FUNPACK
		// C     PACKAGE OF SPECIAL FUNCTION SUBROUTINES. PSI WAS MODIFIED BY
		// C     A.H. MORRIS (NSWC).
		// C
		// C
		// C  PIOV4  = PI/4
		// C  DX0  = ZERO OF PSI TO EXTENDED PRECISION
		// C
		final double piov4  = .785398163397448;
		final double dx0  = 1.461632144968362341262659542325721325e0;
		// C
		// C  COEFFICIENTS FOR RATIONAL APPROXIMATION OF
		// C  PSI(X) / (X - X0),  0.5 <= X <= 3.0
		// C
		
		final double p1[]  = {.895385022981970E-02,
				.477762828042627E+01,
				.142441585084029E+03,
				.118645200713425E+04,
				.363351846806499E+04,
				.413810161269013E+04,
				.130560269827897E+04};
				
		final double q1[]  = {.448452573429826E+02,
				.520752771467162E+03,
				.221000799247830E+04,
				.364127349079381E+04,
				.190831076596300E+04,
				.691091682714533E-05
		};
		
	
		// C
		// C  COEFFICIENTS FOR RATIONAL APPROXIMATION OF
		// C  PSI(X) - LN(X) + 1 / (2*X),  X .GT. 3.0
		// C
		
		final double p2[]  = {-.212940445131011E+01,
				-.701677227766759E+01,
				-.448616543918019E+01,
				-.648157123766197E+00
		};
		
		final double q2[]  = {.322703493791143E+02,
				.892920700481861E+02,
				.546117738103215E+02,
				.777788548522962E+01
				
		};
		
		// C
		// C  MACHINE DEPENDENT CONSTANTS ...
		// C
		// C        XMAX1   = THE SMALLEST POSITIVE doubleING POINT CONSTANT
		// C                 WITH ENTIRELY INTEGER REPRESENTATION.  ALSO USED
		// C                 AS NEGATIVE OF LOWER BOUND ON ACCEPTABLE NEGATIVE
		// C                 ARGUMENTS AND AS THE POSITIVE ARGUMENT BEYOND WHICH 
		// C                 PSI MAY BE REPRESENTED AS ALOG(X).
		// C
		// C        XSMALL  = ABSOLUTE ARGUMENT BELOW WHICH PI*COTAN(PI*X)
		// C                 MAY BE REPRESENTED BY 1/X.
		// C
		// C

		int nq  = 0;
		double xsmall  = 0.0;
		double xmax1  = 0.0;
		double den  = 0.0;
		int i  = 0;
		double aug  = 0.0;
		int m  = 0;
		int n  = 0;
		double sgn  = 0.0;
		double w  = 0.0;
		double x  = 0.0;
		double z  = 0.0;
		double upper = 0.0;
		double xmx0 = 0.0;
		
		xmax1  = ipmpar(3);
		xmax1  = Math.min(xmax1, (1.0/spmpar(1)));
		xsmall  = 1.E-9;
		// 
		x  = xx;
		aug  = 0.0;
		if (x < 0.5) {
			// C
			// C  X .LT. 0.5,  USE REFLECTION FORMULA
			// C  PSI(1-X)  = PSI(X) + PI * COTAN(PI*X)
			// C
			if (Math.abs(x) <= xsmall){

				// error exit
				if (x  == 0.0) 
					return 0;

				// C
				// C  0 .LT. ABS(X) <= XSMALL.  USE 1/X AS A SUBSTITUTE
				// C  FOR  PI*COTAN(PI*X)
				// C
				aug  = -1/x;
			}else{
				// C
				// C  REDUCTION OF ARGUMENT FOR COTAN
				// C
				w  = (-(x));
				sgn  = piov4;
				if (w <= 0.0) {
					w  = -w;
					sgn  = -sgn;
				}
				// C
				// C  MAKE AN ERROR EXIT IF X <= -XMAX1
				// C

				// error exit
				if (w >= xmax1) 
					return 0;

				nq  = (int)w;
				w  = w-nq;
				nq  = (int)(w*4.0);
				w  = 4.0*(w-(nq*0.25));
				// C
				// C  W IS NOW RELATED TO THE FRACTIONAL PART OF  4.0 * X.
				// C  ADJUST ARGUMENT TO CORRESPOND TO VALUES IN FIRST
				// C  QUADRANT AND DETERMINE SIGN
				// C
				n  = (nq/2);
				if ((n+n) != nq) {
					w  = (1.0-w);
				}
				z  = piov4*w;
				m  = n/2;
				if ((m+m) != n) {
					sgn  = -sgn;
				}
				// C
				// C  DETERMINE FINAL VALUE FOR  -PI*COTAN(PI*X)
				// C
				n  = (nq+1)/2;
				m  = (n/2);
				m  = (m+m);
				if ((m  == n)) {
					// C
					// C  CHECK FOR SINGULARITY
					// C
					// error exit
					if (z  == 0.0)
						return 0;

					// C
					// C  USE COS/SIN AS A SUBSTITUTE FOR COTAN, AND
					// C  SIN/COS AS A SUBSTITUTE FOR TAN
					// C
					aug  = sgn*(Math.cos(z)/Math.sin(z)*4.0);
				}else{
					aug  = sgn*(Math.sin(z)/Math.cos(z)*4.0);
				}
			}
			x  = 1-x;
		}
		if (x <= 3.0){
			// C
			// C  0.5 <= X <= 3.0
			// C
			den  = x;
			upper  = (p1[1]*x);
			// 
			
			for (i  = 0; i < 5; i++) {
					den  = (den+q1[i])*x;
					upper  = (upper+p1[i+1])*x;
			}
			
			// 
			den  = (upper+p1[6])/(den+q1[5]);
			xmx0  = (((x)-dx0));
			return (den*xmx0)+aug;
			
			// C
			// C  IF X .GE. XMAX1, PSI  = LN(X)
			// C
		}
		if (x < xmax1) {
			// C
			// C  3.0 .LT. X .LT. XMAX1
			// C
			w  = 1.0/(x*x);
			den  = w;
			upper  = p2[0]*w;
			// 
			for (i  = 0; i < 3; i++) {
				den  = (den+q2[i])*w;
				upper  = (upper+p2[i+1])*w;
			}              //  Close for() loop. 
			// 
			aug  = (((upper/((den+q2[3])))-(0.5/x))+aug);
		}
		
		return aug+Math.log(x);
	}
	
	public void psi_values(intW n, doubleW x, doubleW fx){

		// C *********************************************************************72
		// C 
		// C  PSI_VALUES returns some values of the Psi or Digamma function for testing.
		// C 
		// C   Discussion:
		// C 
		// C     PSI(X) = d LN ( GAMMA ( X ) ) / d X = GAMMA'(X) / GAMMA(X)
		// C 
		// C     PSI(1) = - Euler's constant.
		// C 
		// C     PSI(X+1) = PSI(X) + 1 / X.
		// C 
		// C   Modified:
		// C 
		// C     17 May 2001
		// C 
		// C   Author:
		// C 
		// C     John Burkardt
		// C 
		// C   Reference:
		// C 
		// C     Milton Abramowitz and Irene Stegun,
		// C     Handbook of Mathematical Functions,
		// C     US Department of Commerce, 1964.
		// C 
		// C   Parameters:
		// C 
		// C     Input/output, integer N.
		// C     On input, if N is 0, the first test data is returned, and N is set
		// C     to the index of the test data.  On each subsequent call, N is
		// C     incremented and that test data is returned.  When there is no more
		// C     test data, N is set to 0.
		// C 
		// C     Output, real X, the argument of the function.
		// C 
		// C     Output, real FX, the value of the function.
		// C 
		final int nmax = 11;
		final	double	fxvec[]	=	{
				-0.5772156649E+00,
				-0.4237549404E+00,
				-0.2890398966E+00,	
				-0.1691908889E+00,	
				-0.0613845446E+00,	
				-0.0364899740E+00,	
				0.1260474528E+00,	
				0.2085478749E+00,	
				0.2849914333E+00,	
				0.3561841612E+00,	
				0.4227843351E+00};

		final	double	xvec[]	={
				1.0E+00,	
				1.1E+00,	
				1.2E+00,	
				1.3E+00,	
				1.4E+00,	
				1.5E+00,	
				1.6E+00,	
				1.7E+00,	
				1.8E+00,	
				1.9E+00,	
				2.0E+00};

		if ( n.val < 0 )
			n.val = 0;
		
		n.val++;

		if (nmax < n.val){
			n.val = 0;
			x.val = 0.0E+00;
			fx.val = 0.0E+00;
		}else{
			x.val = xvec[n.val-1];
			fx.val = fxvec[n.val-1];
		}
	}
	
	double r4_epsilon(){

		// C *********************************************************************72
		// C 
		// C R4_EPSILON returns the round off unit for floating arithmetic.
		// C 
		// C Discussion:
		// C 
		// C R4_EPSILON is a number R which is a power of 2 with the property that,
		// C to the precision of the computer's arithmetic,
		// C 1 < 1 + R
		// C but
		// C 1 = ( 1 + R / 2 )
		// C 
		// C FORTRAN90 provides the superior library routine
		// C 
		// C EPSILON ( X )
		// C 
		// C Modified:
		// C 
		// C 28 November 2005
		// C 
		// C Author:
		// C 
		// C John Burkardt
		// C 
		// C Parameters:
		// C 
		// C Output, real R4_EPSILON, the floating point round-off unit.
		// C 

		double r = 1.0E+00;
		double r_test = 1.0E+00 + r/2.0E+00;

		while(1.0E+00 < r_test){
			r = r / 2.0E+00;
			r_test = 1.0E+00 + r / 2.0E+00;
		}

		return r;
	}
	
	public static double rexp (double x)  {

		// 
		// c*********************************************************************72
		// c
		// cc REXP evaluates the function EXP(X) - 1.
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360 - 373.
		// c
		// 
		final double q4 = .595130811860248e-03;
		final double q3 = -.119041179760821e-01;
		final double q2 = .107141568980644e+00;
		final double q1 = -.499999999085958e+00;
		final double p2 = .238082361044469e-01;
		final double p1 = .914041914819518e-09;


		double w = 0.0;
		
		if(Math.abs(x) <= 0.15){
			return  x*(((p2*x + p1)*x + 1.0)/((((q4*x + q3)*x + q2)*x + q1))*x + 1.0);
		}else{
			w = Math.exp( x);

			if (x <= 0.0) {
				return (w - 0.5) - 0.5;
			}else{
				return w*(0.5 + (0.5 - (1.0/w)));
			}
		}
	}
	
	public static double rlog1 (double x){

		// 
		// c*********************************************************************72
		// c
		// cc RLOG1 evaluates the function X - LN(1 + X).
		// C
		// c  Reference:
		// c
		// c    Armido Didonato, Alfred Morris,
		// c    Algorithm 708: 
		// c    Significant Digit Computation of the Incomplete Beta Function Ratio
		// c    ACM Transactions on Mathematical Software,
		// c    Volume 18, Number 3, 1992, pages 360-373.
		// c
		// 
		final double a  = 0.566749439387324E-01;
		final double b  = 0.456512608815524E-01;
		// 
		final double p2  = .620886815375787E-02;
		final double p1  = -0.224696413112536E+00;
		final double p0  = 0.333333333333333E+00;
		final double q2  = 0.354508718369557E+00;
		final double q1  = -0.127408923933623E+01;
		// 

		double h  = 0.0;
		double r  = 0.0;
		double t  = 0.0;
		double w  = 0.0;
		double w1 = 0.0;
		
		if (x < -0.39 || x > 0.57) {
			w  = (x+0.5)+0.5;
			
			return x-Math.log(w);
		}
		
		if (x < -0.18){
			h  = (((x)+0.3e0));
			h  = (h/0.7);
			w1  = (a-(h*0.3));
		}else if(x > 0.18){
			h  = (((0.75e0*(x))-0.25e0));
			w1  = (b+(h/3.0));
		}else{
			// C
			// C  ARGUMENT REDUCTION
			// C
			h  = x;
			w1  = 0.0;
		}
		
		// C
		// C  Series expansion
		// c
		r  = (h/((h+2.0)));
		t  = (r*r);
		w  = ((p2*t+p1)*t+p0)/((q2*t+q1)*t+1.0);
		
		return 2.0*t*(1.0/(1.0-r)-r*w)+w1;
		// 
	}

	/**
	 * Fortran floating point transfer of sign (SIGN) intrinsic function.
	 * <p>
	 * Returns:<br>
	 * <ul>
	 *   <li> abs(a1), if a2 &gt;= 0
	 *   <li>-abs(a1), if a2 &lt; 0
	 * </ul>
	 *
	 * @param a1 floating point value
	 * @param a2 sign transfer indicator
	 *
	 * @return equivalent of Fortran SIGN(a1,a2) as described above.
	 *  <p>
	 *  @author Keith Seymour (seymour@cs.utk.edu)
	 */
	public static double sign(double a1, double a2) {
		return (a2 >= 0) ? Math.abs(a1) : -Math.abs(a1);
	}
	
	public static double spmpar(int i){
		// 
		// c*********************************************************************72
		// c
		// cc SPMPAR returns single precision real machine constants.
		// c
		// C     SPMPAR PROVIDES THE SINGLE PRECISION MACHINE CONSTANTS FOR
		// C     THE COMPUTER BEING USED. IT IS ASSUMED THAT THE ARGUMENT
		// C     I IS AN INTEGER HAVING ONE OF THE VALUES 1, 2, OR 3. IF THE
		// C     SINGLE PRECISION ARITHMETIC BEING USED HAS M BASE B DIGITS AND
		// C     ITS SMALLEST AND LARGEST EXPONENTS ARE EMIN AND EMAX, THEN
		// C
		// C        SPMPAR(1)  = B**(1 - M), THE MACHINE PRECISION,
		// C
		// C        SPMPAR(2)  = B**(EMIN - 1), THE SMALLEST MAGNITUDE, 
		// C
		// C        SPMPAR(3)  = B**EMAX*(1 - B**(-M)), THE LARGEST MAGNITUDE.
		// C
		// C
		// C     WRITTEN BY
		// C        ALFRED H. MORRIS, JR.
		// C        NAVAL SURFACE WARFARE CENTER
		// C        DAHLGREN VIRGINIA
		// C
		// 

		int emin = 0;
		int emax = 0;
		double binv = 0.0;
		double bm1 = 0.0;
		int ibeta = 0;
		double b = 0.0;
		int m = 0;
		double one = 0.0;
		double w = 0.0;
		double z = 0.0;
		
		if (i > 1) {
			if(i > 2){		
				ibeta  = ipmpar(4);
				m  = ipmpar(5);
				emax  = ipmpar(7);
				// 
				b  = ibeta;
				bm1  = ibeta - 1;
				one  = 1.0;
				z  = Math.pow(b, m - 1);
				w  = ((((((z-one))*b)+bm1))/((b*z)));
				// 
				z  = (Math.pow(b, ((emax-2))));
				return (((((w*z))*b))*b);
			}else{
				b  = ipmpar(4);
				emin  = ipmpar(6);
				one  = 1.0;
				binv  = one/b;
				w  = Math.pow(b, emin + 2);
				return w*binv*binv*binv;
			}
		}
		//else
		b  = ipmpar(4);
		m  = ipmpar(5);
		return Math.pow(b, 1 - m);
		
	}

	public static void main(String[] args) {
		// 
		// c*********************************************************************72
		// c
		// cc MAIN is the main program for TOMS708_PRB1.
		// c
		// c      ALGORITHM 708, COLLECTED ALGORITHMS FROM ACM.
		// c      THIS WORK PUBLISHED IN TRANSACTIONS ON MATHEMATICAL SOFTWARE,
		// c      VOL. 18, NO. 3, SEPTEMBER, 1992, PP. 360-373z.
		// c
		// c     SAMPLE PROGRAM USING BRATIO. GIVEN THE NONNEGATIVE VALUES
		// c     A, B, X, Y WHERE A AND B ARE NOT BOTH 0 AND X + Y = 1. THEN
		// c
		// c              CALL BRATIO (A, B, X, Y, W, W1, IERR)
		// c
		// c     COMPUTES THE VALUES
		// c
		// c                W = I (A,B)  AND W1 = 1 - I (A,B).
		// c                     X                     X
		// c
		// c     IERR IS A VARIABLE THAT REPORTS THE STATUS OF THE RESULTS.
		// c     IF NO INPUT ERRORS ARE DETECTED THEN IERR IS SET TO 0 AND
		// c     W AND W1 ARE COMPUTED. FOR MORE DETAILS SEE THE IN-LINE
		// c     DOCUMENTATION OF BRATIO.
		// c
		// 
		// 


		double a = 0.0;
		double b = 0.0;
		int i = 0;
		intW ierr = new intW(0);
		doubleW w = new doubleW(0.0);
		doubleW w1 = new doubleW(0.0);
		double x = 0.0;
		double y = 0.0;
		
		System.out.println(" ");
		System.out.println("TOMS708_PRB1");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("   Java version");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("    X     Y           W              W1");
		System.out.println(" ");
		System.out.println(" ");
		
		a = 5.3;
		b = 10.1;
		x = 0.01;
		// 
		for (i = 1; i <= 50; i++) {
			
			y = 0.5 + (0.5 - x);
			
			bratio(a,b,x,y,w,w1,ierr);		 
			if (ierr.val != 0)  {
				System.out.printf("%f %f %s\n", x, y, "Error occurred");
			}
			else  {
				//System.out.printf("%f %f %e% %e\n", x, y, w, w1);
				System.out.printf("%6.2f", x);
				System.out.printf("%6.2f", y);
				System.out.printf("%16.6e", w.val);
				System.out.printf("%16.6e", w1.val);
				System.out.println();
			}          
			// 
			x += 0.01;
		}
		// 
		System.out.println(" ");
		System.out.println("TOMS708_PRB1");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("   Normal end of execution.");
		// 
	}

}
