package atom.energy.pw.msr;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 14/08/12, 11:55 AM
 */
// H(r) = (-1/2) {d^2 /dr^2 - L*(L+1)/r^2} + U(r)
// INTL dr P(r)P(r)=1
//
// x = a * r + b * ln(y);  y = c + r;  z = a + b/y;  yz = ay+b
// x_r = dx/dr = z;  x'' = d(dx/dr)/dr=dz/dr=z_r;
// z_x = dz/dx = dz/dr dr/dx = z_r / z
// z_r = -b/y^2;   z_rr = 2b/y^3;
//
// After P(r) = f(x) * F(x)
//    P_r=[f_x*F+f*Fx]*x';
//    P_r=[f_x*F+f*Fx]*z;
//    P_rr=[f_xx*F + 2f_x*Fx + f*Fxx]*(x_r)^2 + [f_x*F + f*Fx] * x_rr;
//    P_rr=[f_xx*F + 2f_x*Fx + f*Fxx]*z^2     + [f_x*F + f*Fx] * z_r;
//
// -2(H(r)-U)=f2*Fxx+f1*Fx+f0*F
//    f2 = f * z^2
//    f1 = 2*f_x *z^2 +f * z_r
//    f0 = f_rr = d[df/dr]/dr
//    f0 = f_xx * z^2 + f_x * z_r = d[ f_x * z] /dr
//
//    Need f1=0;  f_x/f = -0.5* z_r/z^2
//    d[ln(f)]/dx = 1/z  d[ln z^{-1/2}]/dr;
//    d[ln(f)]  = dx/dr  1/z  d[ln z^{-1/2}]=d[ln z^{-1/2}];
//    f    = z^{-1/2};
//    f_r  = (-1/2) z^{-3/2} z_r;
//    f_rr = (-1/2) z^{-3/2} [ (-3/2) (z_r)^2 / z + z_rr];
//    f_rr = f * (-1/2) * 1/z [ (-3/2) (z_r)^2 / z + z_rr];
//
//    f2 = f * z^2
//    f1 = 0
//    f0 = f * z^2 [  (-1/2) * 1/(z^3) [ (-3/2) (z_r)^2 / z + z_rr]  ]
//
//    CHECK f1
//    f_x = d[f]/dx=d[z^{-1/2}]/dx=(-1/2) z^{-3/2} dz/dx = (-1/2) z^{-5/2} z_r;
//    f1 =2*f_x *z^2 + f * z_r = -z^{-1/2} z_r + z^{-1/2} z_r == 0
//
//    f0 = f_rr
// [ (-3/2) (z_r)^2 / z + z_rr] = [ (-3/2) b^2/y^4 / z + 2b/y^3]= 1/(2zy^4) [-3b^2 + 4byz]
// [ -3b^2 + 4byz]=[-3b^2 + 4b(ay+b)]=[b^2 + 4bay)]=b[b + 4ay)]=b[yz + 3ay)]=by[z + 3a]=
//    f0 = by[z + 3a] / [2zy^4]=b[z + 3a] / [2zy^3]=z^2 b[z + 3a]/2 /(zy)^3
//
// IDEA:  x = r^{1-2a}/[1-2a]; z = r^{-2a};
// f = z^{-1/2} = r^{a};
// f_r = ar^{a-1}; f_rr=a(a-1) r^{a-2};
// f_rr / z^2 =a(a-1) r^{5a-2}; a=2/5
//

public class PotHLlr { // Msr - mixed scale r
}
