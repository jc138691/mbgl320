package atom.energy.pw.acr;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 14/08/12, 11:55 AM
 */
// H(r) = (-1/2) {1/r d^2(r*)/dr^2 - L*(L+1)/r^2} + U(r)
// INTL r^2dr R(r)R(r)=1
//
// After R(r) = p(r) * P(r)
// -2(H(r)-U)=p2*P"+p1*P'+p0*P
//    p2=p                          *P"
//    p1=2p'+2/r*p                  *P'
//    p0=p"+2/r*p'-L*(L+1)/r^2*p    *P
//
// After P(r)=F(x); x=ln(y), y=c+r; dx=dy/y=dr/y
//    P'=F'/y;    P"=F"/y^2-F'/y^2
//
// -2(H(r)-U)=f2*F"+f1*F'+f0*F
//    p2=p                          *(F"/y^2-F'/y^2)
//    p1=2p'+2/r*p                  *(F'/y)
//    p0=p"+2/r*p'-L*(L+1)/r^2*p    *F
//
//    f2=p/y^2
//    f1=-p/y^2 + 2p'/y + 2/r*p/y
//    f0=p0
//
// Select p(r) such that f1==0;   2p'=p/y-2p/r
//    Check y=r;   2p'=p/r-2p/r=-p/r;  solution  p(r)=1/sqrt(r)
// p(r)=sqrt(y)*g  gives g'=-g/r; g=1/r; p=sqrt(y)/r
//
// f2=1/r*1/y^3/2
// f0=-1/4*1/r*1/y^3/2 - L(L+1)*sqrt(y)/r^3
//   Check y=r; f0=-1/4*1/r5/2 - L(L+1)/r^5/2=-(L+1/2)^2 * 1/r^5/2
//
// INTL r^2dr R*HR = INTL r^2 ydx sqrt(y)/r F*HF = INTL y^3/2 dx r F*HF
// (-1/2)*INTL dx [F" -{1/4 + L(L+1)*(y/r)^2}*F - 2*U(r)*F(x)]
//    check r->0; R=P(r)/r
//    check r->oo; R=P(r)/sqrt(r); x=ln(r)
//
// w are for 'x' integral
//
// d/dr R(r) = d/dr [sqrt(y) F] = 1/2 1/sqrt(y) F + sqrt(y) dx/dr F'(x) =***
// dx/dr = 1/y
// ***=  1/sqrt(y) [F/2 + F']
public class PotHAcr {
}
