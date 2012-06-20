// from http://www.ee.ucl.ac.uk/~mflanaga/java/Complex.html
package flanagan.complex;
/*
*   Class   Complex
*
*   Defines a complex number as an object and includes
*   the methods needed for standard complex arithmetic
*
*   See class ComplexMatrix for complex matrix manipulations
*   See class ComplexPoly for complex polynomial manipulations
*   See class ComplexErrorProp for the error propogation in complex arithmetic
*
*   WRITTEN BY: Dr Michael Thomas Flanagan
*
*   DATE:    February 2002
*   UPDATED: 1 August 2006, 29 April 2007, 15,21,22 June 2007, 22 November 2007
*            20 May 2008, 26 August 2008
*
*   DOCUMENTATION:
*   See Michael T Flanagan's Java library on-line web pages:
*   http://www.ee.ucl.ac.uk/~mflanaga/java/Complex.html
*   http://www.ee.ucl.ac.uk/~mflanaga/java/
*
*   Copyright (c) 2002 - 2008    Michael Thomas Flanagan
*
*   PERMISSION TO COPY:
*   Permission to use, copy and modify this software and its documentation for
*   NON-COMMERCIAL purposes is granted, without fee, provided that an acknowledgement
*   to the author, Michael Thomas Flanagan at www.ee.ucl.ac.uk/~mflanaga, appears in all copies.
*
*   Dr Michael Thomas Flanagan makes no representations about the suitability
*   or fitness of the software for any or for a particular purpose.
*   Michael Thomas Flanagan shall not be liable for any damages suffered
*   as a result of using, modifying or distributing this software or its derivatives.
*
***************************************************************************************/


import flanagan.math.Fmath;

public class Cmplx {
  private static boolean toStringFloat = true;
  public static final Cmplx ZERO = new Cmplx(0);
  public static final Cmplx ONE = new Cmplx(1);
  public static final Cmplx i = new Cmplx(0, 1);

  private double real = 0.0D;         // Real part of a complex number
  private double imag = 0.0D;         // Imaginary part of a complex number
  private static char jori = 'j';     // i or j in a + j.b or a + i.b representaion
  // default value = j
  private static boolean infOption = true;  // option determining how infinity is handled
  // if true (default option):
  //  multiplication with either complex number with either part = infinity returns infinity
  //      unless the one complex number is zero in both parts
  //  division by a complex number with either part = infinity returns zero
  //      unless the dividend is also infinite in either part
  // if false:
  //      standard arithmetic performed


  public boolean isNegOrZeroInt() {
    return (imag == 0  &&  -Math.abs(real) == ((int)real)); //      IF(GIMAG(U) .EQ. 0 .AND. -ABS(X) .EQ. INT(X)) THEN
  }


  public static void setToStringFloat(boolean b) {
    toStringFloat = b;
  }
  /*********************************************************/

  // CONSTRUCTORS
  // default constructor - real and imag = zero
  public Cmplx()
  {
    this.real = 0.0D;
    this.imag = 0.0D;
  }

  // constructor - initialises both real and imag
  public Cmplx(double real, double imag)
  {
    this.real = real;
    this.imag = imag;
  }

  // constructor - initialises  real, imag = 0.0
  public Cmplx(double real)
  {
    this.real = real;
    this.imag = 0.0D;
  }

  // constructor - initialises both real and imag to the values of an existing Complex
  public Cmplx(Cmplx c)
  {
    this.real = c.real;
    this.imag = c.imag;
  }

  public String toString() {
    if (toStringFloat) {
      return "(" + (float)getReal() + ", " + (float)getImag() + ")";
    }
    else {
      return "(" + getReal() + ", " + getImag() + ")";
    }
  }

  // PUBLIC METHODS
  public void setReal(double real){    this.real = real;  }
  public void setImag(double imag){    this.imag = imag;  }
  public void setRe(double real){    this.real = real;  }
  public void setIm(double imag){    this.imag = imag;  }

  // Set the values of real and imag
  public void reset(double real, double imag){
    this.real = real;
    this.imag = imag;
  }

  public void addToSelf(Cmplx c2) {
    setReal(getReal() + c2.getReal());
    setImag(getImag() + c2.getImag());
//    re += c2.re;
//    im += c2.im;
  }

  // Set real and imag given the modulus and argument (in radians)
  public void polarRad(double mod, double arg){
    this.real = mod*Math.cos(arg);
    this.imag = mod*Math.sin(arg);
  }

  // Set real and imag given the modulus and argument (in radians)
  // retained for compatibility
  public void polar(double mod, double arg){
    this.real = mod*Math.cos(arg);
    this.imag = mod*Math.sin(arg);
  }

  // Set real and imag given the modulus and argument (in degrees)
  public void polarDeg(double mod, double arg){
    arg = Math.toRadians(arg);
    this.real = mod*Math.cos(arg);
    this.imag = mod*Math.sin(arg);
  }

  public double getReal(){    return real;}
  public double getImag(){    return imag;  }
  public double getRe(){    return real;}
  public double getIm(){    return imag;  }

  // INPUT AND OUTPUT




  // TRUNCATION
  // Rounds the mantissae of both the real and imaginary parts of Complex to prec places
  // Static method
  public static Cmplx truncate(Cmplx x, int prec){
    if(prec<0)return x;

    double xR = x.getReal();
    double xI = x.getImag();
    Cmplx y = new Cmplx();

    xR = Fmath.truncate(xR, prec);
    xI = Fmath.truncate(xI, prec);

    y.reset(xR, xI);

    return y;
  }

  // instance method
  public Cmplx truncate(int prec){
    if(prec<0)return this;

    double xR = this.getReal();
    double xI = this.getImag();
    Cmplx y = new Cmplx();

    xR = Fmath.truncate(xR, prec);
    xI = Fmath.truncate(xI, prec);

    y.reset(xR, xI);

    return y;
  }


  // CONVERSIONS
  // Format a complex number as a string, a + jb or a + ib[instance method]
  // < value of real > < + or - > < j or i> < value of imag >
  // Choice of j or i is set by Complex.seti() or Complex.setj()
  // j is the default option for j or i
  // Overides java.lang.String.toCsv()
//  public String toCsv(){
//    char ch='+';
//    if(this.imag<0.0D)ch='-';
//    return this.real+" "+ch+" "+jori+Math.abs(this.imag);
//  }

  // Format a complex number as a string, a + jb or a + ib [static method]
  // See static method above for comments
  public static String toString(Cmplx aa){
    char ch='+';
    if(aa.imag<0.0D)ch='-';
    return aa.real+" "+ch+jori+Math.abs(aa.imag);
  }

  // Sets the representation of the square root of minus one to j in Strings
  public static void setj(){
    jori = 'j';
  }

  // Sets the representation of the square root of minus one to i in Strings
  public static void seti(){
    jori = 'i';
  }

  // Returns the representation of the square root of minus one (j or i) set for Strings
  public static char getjori(){
    return jori;
  }

  // Parse a string to obtain Complex
  // accepts strings 'real''s''sign''s''x''imag'
  // where x may be i or j and s may be no spaces or any number of spaces
  // and sign may be + or -
  // e.g.  2+j3, 2 + j3, 2+i3, 2 + i3
  public static Cmplx parseComplex(String ss){
    Cmplx aa = new Cmplx();
    ss = ss.trim();
    double first = 1.0D;
    if(ss.charAt(0)=='-'){
      first = -1.0D;
      ss = ss.substring(1);
    }

    int i = ss.indexOf('j');
    if(i==-1){
      i = ss.indexOf('i');
    }
    if(i==-1)throw new NumberFormatException("no i or j found");

    int imagSign=1;
    int j = ss.indexOf('+');

    if(j==-1){
      j = ss.indexOf('-');
      if(j>-1) imagSign=-1;
    }
    if(j==-1)throw new NumberFormatException("no + or - found");

    int r0=0;
    int r1=j;
    int i0=i+1;
    int i1=ss.length();
    String sreal=ss.substring(r0,r1);
    String simag=ss.substring(i0,i1);
    aa.real=first*Double.parseDouble(sreal);
    aa.imag=imagSign*Double.parseDouble(simag);
    return aa;
  }

  // Same method as parseComplex
  // Overides java.lang.Object.valueOf()
  public static Cmplx valueOf(String ss){
    return Cmplx.parseComplex(ss);
  }

  // Return a HASH CODE for the Complex number
  // Overides java.lang.Object.hashCode()
  public int hashCode()
  {
    long lreal = Double.doubleToLongBits(this.real);
    long limag = Double.doubleToLongBits(this.imag);
    int hreal = (int)(lreal^(lreal>>>32));
    int himag = (int)(limag^(limag>>>32));
    return 7*(hreal/10)+3*(himag/10);
  }

  // ARRAYS

  // Create a one dimensional array of Complex objects of length n
  // all real = 0 and all imag = 0
  public static Cmplx[] oneDarray(int n){
    Cmplx[] a =new Cmplx[n];
    for(int i=0; i<n; i++){
      a[i]= Cmplx.zero();
    }
    return a;
  }

  // Create a one dimensional array of Complex objects of length n
  // all real = a and all imag = b
  public static Cmplx[] oneDarray(int n, double a, double b){
    Cmplx[] c =new Cmplx[n];
    for(int i=0; i<n; i++){
      c[i]= Cmplx.zero();
      c[i].reset(a, b);
    }
    return c;
  }

  // Arithmetic mean of a one dimensional array of complex numbers
  public static Cmplx mean(Cmplx[] aa){
    int n = aa.length;
    Cmplx sum = new Cmplx(0.0D, 0.0D);
    for(int i=0; i<n; i++){
      sum = sum.plus(aa[i]);
    }
    return sum.over((double)n);
  }

  // Create a one dimensional array of Complex objects of length n
  // all = the Complex constant
  public static Cmplx[] oneDarray(int n, Cmplx constant){
    Cmplx[] c =new Cmplx[n];
    for(int i=0; i<n; i++){
      c[i]= Cmplx.copy(constant);
    }
    return c;
  }

  // Create a two dimensional array of Complex objects of dimensions n and m
  // all real = zero and all imag = zero
  public static Cmplx[][] twoDarray(int n, int m){
    Cmplx[][] a =new Cmplx[n][m];
    for(int i=0; i<n; i++){
      for(int j=0; j<m; j++){
        a[i][j]= Cmplx.zero();
      }
    }
    return a;
  }

  // Create a two dimensional array of Complex objects of dimensions n and m
  // all real = a and all imag = b
  public static Cmplx[][] twoDarray(int n, int m, double a, double b){
    Cmplx[][] c =new Cmplx[n][m];
    for(int i=0; i<n; i++){
      for(int j=0; j<m; j++){
        c[i][j]= Cmplx.zero();
        c[i][j].reset(a, b);
      }
    }
    return c;
  }

  // Create a two dimensional array of Complex objects of dimensions n and m
  // all  =  the Complex constant
  public static Cmplx[][] twoDarray(int n, int m, Cmplx constant){
    Cmplx[][] c =new Cmplx[n][m];
    for(int i=0; i<n; i++){
      for(int j=0; j<m; j++){
        c[i][j]= Cmplx.copy(constant);
      }
    }
    return c;
  }

  // Create a three dimensional array of Complex objects of dimensions n,  m and l
  // all real = zero and all imag = zero
  public static Cmplx[][][] threeDarray(int n, int m, int l){
    Cmplx[][][] a =new Cmplx[n][m][l];
    for(int i=0; i<n; i++){
      for(int j=0; j<m; j++){
        for(int k=0; k<l; k++){
          a[i][j][k]= Cmplx.zero();
        }
      }
    }
    return a;
  }

  // Create a three dimensional array of Complex objects of dimensions n, m and l
  // all real = a and all imag = b
  public static Cmplx[][][] threeDarray(int n, int m, int l, double a, double b){
    Cmplx[][][] c =new Cmplx[n][m][l];
    for(int i=0; i<n; i++){
      for(int j=0; j<m; j++){
        for(int k=0; k<l; k++){
          c[i][j][k]= Cmplx.zero();
          c[i][j][k].reset(a, b);
        }
      }
    }
    return c;
  }

  // Create a three dimensional array of Complex objects of dimensions n, m and l
  // all  =  the Complex constant
  public static Cmplx[][][] threeDarray(int n, int m, int l, Cmplx constant){
    Cmplx[][][] c =new Cmplx[n][m][l];
    for(int i=0; i<n; i++){
      for(int j=0; j<m; j++){
        for(int k=0; k<l; k++){
          c[i][j][k]= Cmplx.copy(constant);
        }
      }
    }
    return c;
  }

  // COPY
  // Copy a single complex number [static method]
  public static Cmplx copy(Cmplx a){
    if(a==null){
      return null;
    }
    else{
      Cmplx b = new Cmplx();
      b.real=a.real;
      b.imag=a.imag;
      return b;
    }
  }

  // Copy a single complex number [instance method]
  public Cmplx copy(){
    if(this==null){
      return null;
    }
    else{
      Cmplx b = new Cmplx();
      b.real=this.real;
      b.imag=this.imag;
      return b;
    }
  }


  // Copy a 1D array of complex numbers (deep copy)
  // static metod
  public static Cmplx[] copy(Cmplx[] a){
    if(a==null){
      return null;
    }
    else{
      int n =a.length;
      Cmplx[] b = Cmplx.oneDarray(n);
      for(int i=0; i<n; i++){
        b[i]= Cmplx.copy(a[i]);
      }
      return b;
    }
  }

  // Copy a 2D array of complex numbers (deep copy)
  public static Cmplx[][] copy(Cmplx[][] a){
    if(a==null){
      return null;
    }
    else{
      int n =a.length;
      int m =a[0].length;
      Cmplx[][] b = Cmplx.twoDarray(n, m);
      for(int i=0; i<n; i++){
        for(int j=0; j<m; j++){
          b[i][j]= Cmplx.copy(a[i][j]);
        }
      }
      return b;
    }
  }

  // Copy a 3D array of complex numbers (deep copy)
  public static Cmplx[][][] copy(Cmplx[][][] a){
    if(a==null){
      return null;
    }
    else{
      int n = a.length;
      int m = a[0].length;
      int l = a[0][0].length;
      Cmplx[][][] b = Cmplx.threeDarray(n, m, l);
      for(int i=0; i<n; i++){
        for(int j=0; j<m; j++){
          for(int k=0; k<l; k++){
            b[i][j][k]= Cmplx.copy(a[i][j][k]);
          }
        }
      }
      return b;
    }
  }

  // CLONE
  // Overrides Java.Object method clone
  // Copy a single complex number [instance method]
  public Object clone(){
    Object ret = null;

    if(this!=null){
      Cmplx b = new Cmplx();
      b.real=this.real;
      b.imag=this.imag;
      ret = (Object)b;
    }

    return ret;
  }

  // ADDITION
  // Add two Complex numbers [static method]
  public static Cmplx plus(Cmplx a, Cmplx b){
    Cmplx c = new Cmplx();
    c.real=a.real+b.real;
    c.imag=a.imag+b.imag;
    return c;
  }

  // Add a double to a Complex number [static method]
  public static Cmplx plus(Cmplx a, double b){
    Cmplx c = new Cmplx();
    c.real=a.real+b;
    c.imag=a.imag;
    return c;
  }

  // Add a Complex number to a double [static method]
  public static Cmplx plus(double a, Cmplx b){
    Cmplx c = new Cmplx();
    c.real=a+b.real;
    c.imag=b.imag;
    return c;
  }

  // Add a double number to a double and return sum as Complex [static method]
  public static Cmplx plus(double a, double b){
    Cmplx c = new Cmplx();
    c.real=a+b;
    c.imag=0.0D;
    return c;
  }

  // Add a Complex number to this Complex number [instance method]
  // this Complex number remains unaltered
  public Cmplx add(Cmplx a){ return plus(a);  }
  public Cmplx plus(Cmplx a ){
    Cmplx b = new Cmplx();
    b.real=this.real + a.real;
    b.imag=this.imag + a.imag;
    return b;
  }

  // Add double number to this Complex number [instance method]
  // this Complex number remains unaltered
  public Cmplx add(double a){ return plus(a);  }
  public Cmplx plus(double a ){
    Cmplx b = new Cmplx();
    b.real = this.real + a;
    b.imag = this.imag;
    return b;
  }

  // Add a Complex number to this Complex number and replace this with the sum
  public void plusEquals(Cmplx a ){
    this.real+=a.real;
    this.imag+=a.imag;
  }

  // Add double number to this Complex number and replace this with the sum
  public void plusEquals(double a ){
    this.real+=a;
    this.imag=this.imag;
  }

  //  SUBTRACTION
  // Subtract two Complex numbers [static method]
  public static Cmplx minus (Cmplx a, Cmplx b){
    Cmplx c = new Cmplx();
    c.real=a.real-b.real;
    c.imag=a.imag-b.imag;
    return c;
  }

  // Subtract a double from a Complex number [static method]
  public static Cmplx minus(Cmplx a, double b){
    Cmplx c = new Cmplx();
    c.real=a.real-b;
    c.imag=a.imag;
    return c;
  }

  // Subtract a Complex number from a double [static method]
  public static Cmplx minus(double a, Cmplx b){
    Cmplx c = new Cmplx();
    c.real=a-b.real;
    c.imag=-b.imag;
    return c;
  }

  // Subtract a double number to a double and return difference as Complex [static method]
  public static Cmplx minus(double a, double b){
    Cmplx c = new Cmplx();
    c.real=a-b;
    c.imag=0.0D;
    return c;
  }

  // Subtract a Complex number from this Complex number [instance method]
  // this Complex number remains unaltered
  public Cmplx minus(Cmplx a ){
    Cmplx b = new Cmplx();
    b.real=this.real-a.real;
    b.imag=this.imag-a.imag;
    return b;
  }

  // Subtract a double number from this Complex number [instance method]
  // this Complex number remains unaltered
  public Cmplx minus(double a ){
    Cmplx b = new Cmplx();
    b.real=this.real-a;
    b.imag=this.imag;
    return b;
  }

  // Subtract this Complex number from a double number [instance method]
  // this Complex number remains unaltered
  public Cmplx transposedMinus(double a ){
    Cmplx b = new Cmplx();
    b.real=a - this.real;
    b.imag=this.imag;
    return b;
  }

  // Subtract a Complex number from this Complex number and replace this by the difference
  public void minusEquals(Cmplx a ){
    this.real-=a.real;
    this.imag-=a.imag;
  }

  // Subtract a double number from this Complex number and replace this by the difference
  public void minusEquals(double a ){
    this.real-=a;
    this.imag=this.imag;
  }

  // MULTIPLICATION
  // Sets the infinity handling option in multiplication and division
  // infOption -> true; standard arithmetic overriden - see above (instance variable definitions) for details
  // infOption -> false: standard arithmetic used
  public static void setInfOption(boolean infOpt){
    Cmplx.infOption = infOpt;
  }

  // Sets the infinity handling option in multiplication and division
  // opt = 0:   infOption -> true; standard arithmetic overriden - see above (instance variable definitions) for details
  // opt = 1:   infOption -> false: standard arithmetic used
  public static void setInfOption(int opt){
    if(opt<0 || opt>1)throw new IllegalArgumentException("opt must be 0 or 1");
    Cmplx.infOption = true;
    if(opt==1) Cmplx.infOption = false;
  }

  // Gets the infinity handling option in multiplication and division
  // infOption -> true; standard arithmetic overriden - see above (instance variable definitions) for details
  // infOption -> false: standard arithmetic used
  public static boolean getInfOption(){
    return Cmplx.infOption;
  }

  // Multiply two Complex numbers [static method]
  public static Cmplx times(Cmplx a, Cmplx b){
    Cmplx c = new Cmplx(0.0D, 0.0D);
    if(Cmplx.infOption){
      if(a.isInfinite() && !b.isZero()){
        c.reset(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        return c;
      }
      if(b.isInfinite() && !a.isZero()){
        c.reset(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        return c;
      }
    }

    c.real=a.real*b.real-a.imag*b.imag;
    c.imag=a.real*b.imag+a.imag*b.real;
    return c;
  }

  // Multiply a Complex number by a double [static method]
  public static Cmplx times(Cmplx a, double b){
    Cmplx c = new Cmplx();
    if(Cmplx.infOption){
      if(a.isInfinite() && b!=0.0D){
        c.reset(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        return c;
      }
      if(Fmath.isInfinity(b) && !a.isZero()){
        c.reset(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        return c;
      }
    }
    c.real=a.real*b;
    c.imag=a.imag*b;
    return c;
  }

  // Multiply a double by a Complex number [static method]
  public static Cmplx times(double a, Cmplx b){
    Cmplx c = new Cmplx();
    if(Cmplx.infOption){
      if(b.isInfinite() && a!=0.0D){
        c.reset(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        return c;
      }
      if(Fmath.isInfinity(a) && !b.isZero()){
        c.reset(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        return c;
      }
    }

    c.real=a*b.real;
    c.imag=a*b.imag;
    return c;
  }

  // Multiply a double number to a double and return product as Complex [static method]
  public static Cmplx times(double a, double b){
    Cmplx c = new Cmplx();
    c.real=a*b;
    c.imag=0.0D;
    return c;
  }

  // Multiply this Complex number by a Complex number [instance method]
  // this Complex number remains unaltered
  public Cmplx mult(Cmplx a){ return times(a);    }
  public Cmplx times(Cmplx a){
    Cmplx b = new Cmplx();
    if(Cmplx.infOption){
      if(this.isInfinite() && !a.isZero()){
        b.reset(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        return b;
      }
      if(a.isInfinite() && !this.isZero()){
        b.reset(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        return b;
      }
    }

    b.real=this.real*a.real-this.imag*a.imag;
    b.imag=this.real*a.imag+this.imag*a.real;
    return b;
  }

  // Multiply this Complex number by a double [instance method]
  // this Complex number remains unaltered
  public Cmplx times(double a){
    Cmplx b = new Cmplx();
    if(Cmplx.infOption){
      if(this.isInfinite() && a!=0.0D){
        b.reset(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        return b;
      }
      if(Fmath.isInfinity(a) && !this.isZero()){
        b.reset(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        return b;
      }
    }

    b.real=this.real*a;
    b.imag=this.imag*a;
    return b;
  }

  // Multiply this Complex number by a Complex number and replace this by the product
  public void timesEquals(Cmplx a){
    Cmplx b = new Cmplx();
    boolean test = true;
    if(Cmplx.infOption){
      if((this.isInfinite() && !a.isZero()) || (a.isInfinite() && !this.isZero())){
        this.real = Double.POSITIVE_INFINITY;
        this.imag = Double.POSITIVE_INFINITY;
        test = false;
      }
    }
    if(test){
      b.real=a.real*this.real-a.imag*this.imag;
      b.imag=a.real*this.imag+a.imag*this.real;
      this.real=b.real;
      this.imag=b.imag;
    }
  }

  // Multiply this Complex number by a double and replace this by the product
  public void timesEquals(double a){
    boolean test = true;
    if(Cmplx.infOption){
      if((this.isInfinite() && a!=0.0D) || (Fmath.isInfinity(a) && !this.isZero())){
        this.real = Double.POSITIVE_INFINITY;
        this.imag = Double.POSITIVE_INFINITY;
        test = false;
      }
    }
    if(test){
      this.real=this.real*a;
      this.imag=this.imag*a;
    }
  }


  // DIVISION
  // Division of two Complex numbers a/b [static method]
  public static Cmplx over(Cmplx a, Cmplx b){
    Cmplx c = new Cmplx(0.0D,0.0D);
    if(Cmplx.infOption && !a.isInfinite() && b.isInfinite())return c;

    double denom = 0.0D, ratio = 0.0D;
    if(a.isZero()){
      if(b.isZero()){
        c.real=Double.NaN;
        c.imag=Double.NaN;
      }
      else{
        c.real=0.0D;
        c.imag=0.0D;
      }
    }
    else{
      if(Math.abs(b.real)>=Math.abs(b.imag)){
        ratio=b.imag/b.real;
        denom=b.real+b.imag*ratio;
        c.real=(a.real+a.imag*ratio)/denom;
        c.imag=(a.imag-a.real*ratio)/denom;
      }
      else{
        ratio=b.real/b.imag;
        denom=b.real*ratio+b.imag;
        c.real=(a.real*ratio+a.imag)/denom;
        c.imag=(a.imag*ratio-a.real)/denom;
      }
    }
    return c;
  }

  // Division of a Complex number, a, by a double, b [static method]
  public static Cmplx over(Cmplx a, double b){
    Cmplx c = new Cmplx(0.0D, 0.0D);
    if(Cmplx.infOption && Fmath.isInfinity(b))return c;

    c.real=a.real/b;
    c.imag=a.imag/b;
    return c;
  }

  // Division of a double, a, by a Complex number, b  [static method]
  public static Cmplx over(double a, Cmplx b){
    Cmplx c = new Cmplx();
    if(Cmplx.infOption && !Fmath.isInfinity(a) && b.isInfinite())return c;

    double denom, ratio;

    if(a==0.0D){
      if(b.isZero()){
        c.real=Double.NaN;
        c.imag=Double.NaN;
      }
      else{
        c.real=0.0D;
        c.imag=0.0D;
      }
    }
    else{
      if(Math.abs(b.real)>=Math.abs(b.imag)){
        ratio=b.imag/b.real;
        denom=b.real+b.imag*ratio;
        c.real=a/denom;
        c.imag=-a*ratio/denom;
      }
      else{
        ratio=b.real/b.imag;
        denom=b.real*ratio+b.imag;
        c.real=a*ratio/denom;
        c.imag=-a/denom;
      }
    }
    return c;
  }

  // Divide a double number by a double and return quotient as Complex [static method]
  public static Cmplx over(double a, double b){
    Cmplx c = new Cmplx();
    c.real=a/b;
    c.imag=0.0;
    return c;
  }

  // Division of this Complex number by a Complex number [instance method]
  // this Complex number remains unaltered
  public Cmplx div(Cmplx a){ return over(a); }
  public Cmplx over(Cmplx a){
    Cmplx b = new Cmplx(0.0D, 0.0D);
    if(Cmplx.infOption && !this.isInfinite() && a.isInfinite())return b;

    double denom = 0.0D, ratio = 0.0D;
    if(Math.abs(a.real)>=Math.abs(a.imag)){
      ratio=a.imag/a.real;
      denom=a.real+a.imag*ratio;
      b.real=(this.real+this.imag*ratio)/denom;
      b.imag=(this.imag-this.real*ratio)/denom;
    }
    else
    {
      ratio=a.real/a.imag;
      denom=a.real*ratio+a.imag;
      b.real=(this.real*ratio+this.imag)/denom;
      b.imag=(this.imag*ratio-this.real)/denom;
    }
    return b;
  }

  // Division of this Complex number by a double [instance method]
  // this Complex number remains unaltered
  public Cmplx div(double a){ return over(a); }
  public Cmplx over(double a){
    Cmplx b = new Cmplx(0.0D, 0.0D);
    b.real=this.real/a;
    b.imag=this.imag/a;
    return b;
  }

  // Division of a double by this Complex number [instance method]
  // this Complex number remains unaltered
  public Cmplx transposedOver(double a){
    Cmplx c = new Cmplx(0.0D, 0.0D);
    if(Cmplx.infOption && !Fmath.isInfinity(a) && this.isInfinite())return c;

    double denom = 0.0D, ratio = 0.0D;
    if(Math.abs(this.real)>=Math.abs(this.imag)){
      ratio=this.imag/this.real;
      denom=this.real+this.imag*ratio;
      c.real=a/denom;
      c.imag=-a*ratio/denom;
    }
    else
    {
      ratio=this.real/this.imag;
      denom=this.real*ratio+this.imag;
      c.real=a*ratio/denom;
      c.imag=-a/denom;
    }
    return c;
  }

  // Division of this Complex number by a Complex number and replace this by the quotient
  public void overEquals(Cmplx b){
    Cmplx c = new Cmplx(0.0D, 0.0D);

    boolean test = true;
    if(Cmplx.infOption && !this.isInfinite() && b.isInfinite()){
      this.real = 0.0D;
      this.imag = 0.0D;
      test=false;
    }
    if(test){
      double denom = 0.0D, ratio = 0.0D;
      if(Math.abs(b.real)>=Math.abs(b.imag)){
        ratio=b.imag/b.real;
        denom=b.real+b.imag*ratio;
        c.real=(this.real+this.imag*ratio)/denom;
        c.imag=(this.imag-this.real*ratio)/denom;
      }
      else
      {
        ratio=b.real/b.imag;
        denom=b.real*ratio+b.imag;
        c.real=(this.real*ratio+this.imag)/denom;
        c.imag=(this.imag*ratio-this.real)/denom;
      }
      this.real = c.real;
      this.imag = c.imag;
    }
  }

  // Division of this Complex number by a double and replace this by the quotient
  public void overEquals(double a){
    this.real=this.real/a;
    this.imag=this.imag/a;
  }

  // RECIPROCAL
  // Returns the reciprocal (1/a) of a Complex number (a) [static method]
  public static Cmplx inverse(Cmplx a){
    Cmplx b = new Cmplx(0.0D, 0.0D);
    if(Cmplx.infOption && a.isInfinite())return b;

    b = Cmplx.over(1.0D, a);
    return b;
  }

  // Returns the reciprocal (1/a) of a Complex number (a) [instance method]
  public Cmplx inverse(){
    Cmplx b = new Cmplx(0.0D, 0.0D);
    b = Cmplx.over(1.0D, this);
    return b;
  }

  // FURTHER MATHEMATICAL FUNCTIONS

  // Negates a Complex number [static method]
  public static Cmplx negate(Cmplx a){
    Cmplx c = new Cmplx();
    c.real=-a.real;
    c.imag=-a.imag;
    return c;
  }

  // Negates a Complex number [instance method]
  public Cmplx negate(){
    Cmplx c = new Cmplx();
    c.real=-this.real;
    c.imag=-this.imag;
    return c;
  }

  // Absolute value (modulus) of a complex number [static method]
  public static double abs(Cmplx a){
    double rmod = Math.abs(a.real);
    double imod = Math.abs(a.imag);
    double ratio = 0.0D;
    double res = 0.0D;

    if(rmod==0.0D){
      res=imod;
    }
    else{
      if(imod==0.0D){
        res=rmod;
      }
      if(rmod>=imod){
        ratio=a.imag/a.real;
        res=rmod*Math.sqrt(1.0D + ratio*ratio);
      }
      else{
        ratio=a.real/a.imag;
        res=imod*Math.sqrt(1.0D + ratio*ratio);
      }
    }
    return res;
  }

  // Absolute value (modulus) of a complex number [instance method]
  public double abs2(){
    return real * real + imag * imag;
  }
  public double abs(){
    double rmod = Math.abs(this.real);
    double imod = Math.abs(this.imag);
    double ratio = 0.0D;
    double res = 0.0D;

    if(rmod==0.0D){
      res=imod;
    }
    else{
      if(imod==0.0D){
        res=rmod;
      }
      if(rmod>=imod){
        ratio=this.imag/this.real;
        res=rmod*Math.sqrt(1.0D + ratio*ratio);
      }
      else
      {
        ratio=this.real/this.imag;
        res=imod*Math.sqrt(1.0D + ratio*ratio);
      }
    }
    return res;
  }


  // Square of the absolute value (modulus) of a complex number [static method]
  public static double squareAbs(Cmplx a){
    return a.real*a.real + a.imag*a.imag;
  }

  // Square of the absolute value (modulus) of a complex number [instance method]
  public double squareAbs(){
    return this.real*this.real + this.imag*this.imag;
  }

  // Argument of a complex number (in radians) [static method]
  public static double arg(Cmplx a){
    return Math.atan2(a.imag, a.real);
  }

  // Argument of a complex number (in radians)[instance method]
  public double arg(){
    return Math.atan2(this.imag, this.real);
  }

  // Argument of a complex number (in radians) [static method]
  public static double argRad(Cmplx a){
    return Math.atan2(a.imag, a.real);
  }

  // Argument of a complex number (in radians)[instance method]
  public double argRad(){
    return Math.atan2(this.imag, this.real);
  }

  // Argument of a complex number (in degrees) [static method]
  public static double argDeg(Cmplx a){
    return Math.toDegrees(Math.atan2(a.imag, a.real));
  }

  // Argument of a complex number (in degrees)[instance method]
  public double argDeg(){
    return Math.toDegrees(Math.atan2(this.imag, this.real));
  }

  // Complex conjugate of a complex number [static method]
  public static Cmplx conjugate(Cmplx a){
    Cmplx c = new Cmplx();
    c.real=a.real;
    c.imag=-a.imag;
    return c;
  }

  // Complex conjugate of a complex number [instance method]
  public Cmplx conjugate(){
    Cmplx c = new Cmplx();
    c.real=this.real;
    c.imag=-this.imag;
    return c;
  }

  // Returns the length of the hypotenuse of a and b i.e. sqrt(abs(a)*abs(a)+abs(b)*abs(b))
  // where a and b are Complex [without unecessary overflow or underflow]
  public static double hypot(Cmplx aa, Cmplx bb){
    double amod= Cmplx.abs(aa);
    double bmod= Cmplx.abs(bb);
    double cc = 0.0D, ratio = 0.0D;

    if(amod==0.0D){
      cc=bmod;
    }
    else{
      if(bmod==0.0D){
        cc=amod;
      }
      else{
        if(amod>=bmod){
          ratio=bmod/amod;
          cc=amod*Math.sqrt(1.0 + ratio*ratio);
        }
        else{
          ratio=amod/bmod;
          cc=bmod*Math.sqrt(1.0 + ratio*ratio);
        }
      }
    }
    return cc;
  }

  // Exponential of a complex number (instance method)
  public Cmplx exp(){
    return Cmplx.exp(this);
  }

  // Exponential of a complex number (static method)
  public static Cmplx exp(Cmplx aa){
    Cmplx z = new Cmplx();

    double a = aa.real;
    double b = aa.imag;

    if(b==0.0D){
      z.real=Math.exp(a);
      z.imag=0.0D;
    }
    else{
      if(a==0D){
        z.real=Math.cos(b);
        z.imag=Math.sin(b);
      }
      else{
        double c=Math.exp(a);
        z.real=c*Math.cos(b);
        z.imag=c*Math.sin(b);
      }
    }
    return z;
  }

  // Exponential of a real number returned as a complex number
  public static Cmplx exp(double aa){
    Cmplx bb = new Cmplx(aa, 0.0D);
    return Cmplx.exp(bb);
  }

  // Returns exp(j*arg) where arg is real (a double)
  public static Cmplx expPlusJayArg(double arg){
    Cmplx argc = new Cmplx(0.0D, arg);
    return Cmplx.exp(argc);
  }

  // Returns exp(-j*arg) where arg is real (a double)
  public static Cmplx expMinusJayArg(double arg){
    Cmplx argc = new Cmplx(0.0D, -arg);
    return Cmplx.exp(argc);
  }

  // Principal value of the natural log of an Complex number (instance method)
  public Cmplx log(){

    double a=this.real;
    double b=this.imag;
    Cmplx c = new Cmplx();

    c.real=Math.log(Cmplx.abs(this));
    c.imag=Math.atan2(b,a);

    return c;
  }

  // Principal value of the natural log of an Complex number
  public static Cmplx log(Cmplx aa ){

    double a=aa.real;
    double b=aa.imag;
    Cmplx c = new Cmplx();

    c.real=Math.log(Cmplx.abs(aa));
    c.imag=Math.atan2(b,a);

    return c;
  }

  // Roots
  // Principal value of the square root of a complex number (instance method)
  public Cmplx sqrt(){
    return Cmplx.sqrt(this);
  }


  // Principal value of the square root of a complex number
  public static Cmplx sqrt(Cmplx aa ){
    double a=aa.real;
    double b=aa.imag;
    Cmplx c = new Cmplx();

    if(b==0.0D){
      if(a>=0.0D){
        c.real=Math.sqrt(a);
        c.imag=0.0D;
      }
      else{
        c.real=0.0D;
        c.imag= Math.sqrt(-a);
      }
    }
    else{
      double w, ratio;
      double amod=Math.abs(a);
      double bmod=Math.abs(b);
      if(amod>=bmod){
        ratio=b/a;
        w=Math.sqrt(amod)*Math.sqrt(0.5D*(1.0D + Math.sqrt(1.0D + ratio*ratio)));
      }
      else{
        ratio=a/b;
        w=Math.sqrt(bmod)*Math.sqrt(0.5D*(Math.abs(ratio) + Math.sqrt(1.0D + ratio*ratio)));
      }
      if(a>=0.0){
        c.real=w;
        c.imag=b/(2.0D*w);
      }
      else{
        if(b>=0.0){
          c.imag=w;
          c.real=b/(2.0D*c.imag);
        }
        else{
          c.imag=-w;
          c.real=b/(2.0D*c.imag);
        }
      }
    }
    return c;
  }

  // Principal value of the nth root of a complex number (n = integer > 1) [instance method]
  public Cmplx nthRoot(int n){
    return Cmplx.nthRoot(this, n);
  }


  // Principal value of the nth root of a complex number (n = integer > 1) [static method]
  public static Cmplx nthRoot(Cmplx aa, int n ){
    Cmplx c = new Cmplx();
    if(n==0){
      c = new Cmplx(Double.POSITIVE_INFINITY, 0.0);
    }
    else{
      if(n==1){
        c = aa;
      }
      else{
        c = Cmplx.exp((Cmplx.log(aa)).over((double)n));
      }
    }

    return c;
  }

  // Powers
  // Square of a complex number (static method)
  public static Cmplx square(Cmplx aa){
    Cmplx c = new Cmplx();
    c.real= aa.real*aa.real-aa.imag*aa.imag;
    c.imag= 2.0D*aa.real*aa.imag;
    return c;
  }

  // Square of a complex number (instance method)
  public Cmplx square(){
    return this.times(this);
  }

  // returns a Complex number raised to a Complex power (instance method)
  public Cmplx pow(Cmplx b ){
    Cmplx c = new Cmplx();
    if(this.isZero()){
      if(b.imag==0){
        if(b.real==0){
          c = new Cmplx(1.0, 0.0);
        }
        else{
          if(b.real>0.0){
            c = new Cmplx(0.0, 0.0);
          }
          else{
            if(b.real<0.0){
              c = new Cmplx(Double.POSITIVE_INFINITY, 0.0);
            }
          }
        }
      }
      else{
        c = Cmplx.exp(b.times(Cmplx.log(this)));
      }
    }
    else{
      c = Cmplx.exp(b.times(Cmplx.log(this)));
    }

    return c;
  }

  // returns a Complex number raised to a Complex power
  public static Cmplx pow(Cmplx a, Cmplx b ){
    Cmplx c = new Cmplx();
    if(a.isZero()){
      if(b.imag==0){
        if(b.real==0){
          c = new Cmplx(1.0, 0.0);
        }
        else{
          if(a.real>0.0){
            c = new Cmplx(0.0, 0.0);
          }
          else{
            if(a.real<0.0){
              c = new Cmplx(Double.POSITIVE_INFINITY, 0.0);
            }
          }
        }
      }
      else{
        c= Cmplx.exp(b.times(Cmplx.log(a)));
      }
    }
    else{
      c= Cmplx.exp(b.times(Cmplx.log(a)));
    }

    return c;
  }

  // returns a Complex number raised to a double power [instance method]
  public Cmplx pow(double b){
    return  powDouble(this, b);
  }

  // returns a Complex number raised to a double power
  public static Cmplx pow(Cmplx a, double b){
    return  powDouble(a, b);
  }

  // returns a Complex number raised to an integer, i.e. int, power [instance method]
  public Cmplx pow(int n ){
    double b = (double) n;
    return  powDouble(this, b);
  }

  // returns a Complex number raised to an integer, i.e. int, power
  public static Cmplx pow(Cmplx a, int n ){
    double b = (double) n;
    return  powDouble(a, b);
  }

  // returns a double raised to a Complex power
  public static Cmplx pow(double a, Cmplx b ){
    Cmplx c = new Cmplx();
    if(a==0){
      if(b.imag==0){
        if(b.real==0){
          c = new Cmplx(1.0, 0.0);
        }
        else{
          if(b.real>0.0){
            c = new Cmplx(0.0, 0.0);
          }
          else{
            if(b.real<0.0){
              c = new Cmplx(Double.POSITIVE_INFINITY, 0.0);
            }
          }
        }
      }
      else{
        double z = Math.pow(a, b.real);
        c= Cmplx.exp(Cmplx.times(Cmplx.plusJay(), b.imag*Math.log(a)));
        c= Cmplx.times(z, c);
      }
    }
    else{
      double z = Math.pow(a, b.real);
      c= Cmplx.exp(Cmplx.times(Cmplx.plusJay(), b.imag*Math.log(a)));
      c= Cmplx.times(z, c);
    }

    return c;

  }

  // Complex trigonometric functions

  // Sine of an Complex number
  public Cmplx sin(){
    return Cmplx.sin(this);
  }

  public static Cmplx sin(Cmplx aa ){
    Cmplx c = new Cmplx();
    double a = aa.real;
    double b = aa.imag;
    c.real = Math.sin(a)*Fmath.cosh(b);
    c.imag = Math.cos(a)*Fmath.sinh(b);
    return c;
  }

  // Cosine of an Complex number
  public Cmplx cos(){
    return Cmplx.cos(this);
  }

  public static Cmplx cos(Cmplx aa ){
    Cmplx c = new Cmplx();
    double a = aa.real;
    double b = aa.imag;
    c.real= Math.cos(a)*Fmath.cosh(b);
    c.imag= -Math.sin(a)*Fmath.sinh(b);
    return c;
  }

  // Secant of an Complex number
  public Cmplx sec(){
    return Cmplx.sec(this);
  }

  public static Cmplx sec(Cmplx aa ){
    Cmplx c = new Cmplx();
    double a = aa.real;
    double b = aa.imag;
    c.real= Math.cos(a)*Fmath.cosh(b);
    c.imag= -Math.sin(a)*Fmath.sinh(b);
    return c.inverse();
  }

  // Cosecant of an Complex number
  public Cmplx csc(){
    return Cmplx.csc(this);
  }

  public static Cmplx csc(Cmplx aa ){
    Cmplx c = new Cmplx();
    double a = aa.real;
    double b = aa.imag;
    c.real = Math.sin(a)*Fmath.cosh(b);
    c.imag = Math.cos(a)*Fmath.sinh(b);
    return c.inverse();
  }

  // Tangent of an Complex number
  public Cmplx tan(){
    return Cmplx.tan(this);
  }

  public static Cmplx tan(Cmplx aa ){
    Cmplx c = new Cmplx();
    double denom = 0.0D;
    double a = aa.real;
    double b = aa.imag;

    Cmplx x = new Cmplx(Math.sin(a)*Fmath.cosh(b), Math.cos(a)*Fmath.sinh(b));
    Cmplx y = new Cmplx(Math.cos(a)*Fmath.cosh(b), -Math.sin(a)*Fmath.sinh(b));
    c= Cmplx.over(x, y);
    return c;
  }

  // Cotangent of an Complex number
  public Cmplx cot(){
    return Cmplx.cot(this);
  }

  public static Cmplx cot(Cmplx aa ){
    Cmplx c = new Cmplx();
    double denom = 0.0D;
    double a = aa.real;
    double b = aa.imag;

    Cmplx x = new Cmplx(Math.sin(a)*Fmath.cosh(b), Math.cos(a)*Fmath.sinh(b));
    Cmplx y = new Cmplx(Math.cos(a)*Fmath.cosh(b), -Math.sin(a)*Fmath.sinh(b));
    c= Cmplx.over(y, x);
    return c;
  }

  // Exsecant of an Complex number
  public Cmplx exsec(){
    return Cmplx.exsec(this);
  }

  public static Cmplx exsec(Cmplx aa ){
    return Cmplx.sec(aa).minus(1.0D);
  }

  // Versine of an Complex number
  public Cmplx vers(){
    return Cmplx.vers(this);
  }

  public static Cmplx vers(Cmplx aa ){
    return Cmplx.plusOne().minus(Cmplx.cos(aa));
  }

  // Coversine of an Complex number
  public Cmplx covers(){
    return Cmplx.covers(this);
  }

  public static Cmplx covers(Cmplx aa ){
    return Cmplx.plusOne().minus(Cmplx.sin(aa));
  }

  // Haversine of an Complex number
  public Cmplx hav(){
    return Cmplx.hav(this);
  }

  public static Cmplx hav(Cmplx aa ){
    return Cmplx.vers(aa).over(2.0D);
  }

  // Hyperbolic sine of a Complex number
  public Cmplx sinh(){
    return Cmplx.sinh(this);
  }

  public static Cmplx sinh(Cmplx a ){
    Cmplx c = new Cmplx();
    c=a.times(plusJay());
    c=(Cmplx.minusJay()).times(Cmplx.sin(c));
    return c;
  }

  // Hyperbolic cosine of a Complex number
  public Cmplx cosh(){
    return Cmplx.cosh(this);
  }

  public static Cmplx cosh(Cmplx a ){
    Cmplx c = new Cmplx();
    c=a.times(Cmplx.plusJay());
    c= Cmplx.cos(c);
    return c;
  }

  // Hyperbolic tangent of a Complex number
  public Cmplx tanh(){
    return Cmplx.tanh(this);
  }

  public static Cmplx tanh(Cmplx a ){
    Cmplx c = new Cmplx();
    c = (Cmplx.sinh(a)).over(Cmplx.cosh(a));
    return c;
  }

  // Hyperbolic cotangent of a Complex number
  public Cmplx coth(){
    return Cmplx.coth(this);
  }

  public static Cmplx coth(Cmplx a ){
    Cmplx c = new Cmplx();
    c = (Cmplx.cosh(a)).over(Cmplx.sinh(a));
    return c;
  }

  // Hyperbolic secant of a Complex number
  public Cmplx sech(){
    return Cmplx.sech(this);
  }

  public static Cmplx sech(Cmplx a ){
    Cmplx c = new Cmplx();
    c = (Cmplx.cosh(a)).inverse();
    return c;
  }

  // Hyperbolic cosecant of a Complex number
  public Cmplx csch(){
    return Cmplx.csch(this);
  }

  public static Cmplx csch(Cmplx a ){
    Cmplx c = new Cmplx();
    c = (Cmplx.sinh(a)).inverse();
    return c;
  }


  // Inverse sine of a Complex number
  public Cmplx asin(){
    return Cmplx.asin(this);
  }

  public static Cmplx asin(Cmplx a ){
    Cmplx c = new Cmplx();
    c= Cmplx.sqrt(Cmplx.minus(1.0D, Cmplx.square(a)));
    c=(Cmplx.plusJay().times(a)).plus(c);
    c= Cmplx.minusJay().times(Cmplx.log(c));
    return c;
  }

  // Inverse cosine of a Complex number
  public Cmplx acos(){
    return Cmplx.acos(this);
  }

  public static Cmplx acos(Cmplx a ){
    Cmplx c = new Cmplx();
    c= Cmplx.sqrt(Cmplx.minus(Cmplx.square(a),1.0));
    c=a.plus(c);
    c= Cmplx.minusJay().times(Cmplx.log(c));
    return c;
  }

  // Inverse tangent of a Complex number
  public Cmplx atan(){
    return Cmplx.atan(this);
  }

  public static Cmplx atan(Cmplx a ){
    Cmplx c = new Cmplx();
    Cmplx d = new Cmplx();

    c= Cmplx.plusJay().plus(a);
    d= Cmplx.plusJay().minus(a);
    c=c.over(d);
    c= Cmplx.log(c);
    c= Cmplx.plusJay().times(c);
    c=c.over(2.0D);
    return c;
  }

  // Inverse cotangent of a Complex number
  public Cmplx acot(){
    return Cmplx.acot(this);
  }

  public static Cmplx acot(Cmplx a ){
    return Cmplx.atan(a.inverse());
  }

  // Inverse secant of a Complex number
  public Cmplx asec(){
    return Cmplx.asec(this);
  }

  public static Cmplx asec(Cmplx a ){
    return Cmplx.acos(a.inverse());
  }

  // Inverse cosecant of a Complex number
  public Cmplx acsc(){
    return Cmplx.acsc(this);
  }

  public static Cmplx acsc(Cmplx a ){
    return Cmplx.asin(a.inverse());
  }

  // Inverse exsecant of a Complex number
  public Cmplx aexsec(){
    return Cmplx.aexsec(this);
  }

  public static Cmplx aexsec(Cmplx a ){
    Cmplx c = a.plus(1.0D);
    return Cmplx.asin(c.inverse());
  }

  // Inverse versine of a Complex number
  public Cmplx avers(){
    return Cmplx.avers(this);
  }

  public static Cmplx avers(Cmplx a ){
    Cmplx c = Cmplx.plusOne().plus(a);
    return Cmplx.acos(c);
  }

  // Inverse coversine of a Complex number
  public Cmplx acovers(){
    return Cmplx.acovers(this);
  }

  public static Cmplx acovers(Cmplx a ){
    Cmplx c = Cmplx.plusOne().plus(a);
    return Cmplx.asin(c);
  }

  // Inverse haversine of a Complex number
  public Cmplx ahav(){
    return Cmplx.ahav(this);
  }

  public static Cmplx ahav(Cmplx a ){
    Cmplx c = Cmplx.plusOne().minus(a.times(2.0D));
    return Cmplx.acos(c);
  }

  // Inverse hyperbolic sine of a Complex number
  public Cmplx asinh(){
    return Cmplx.asinh(this);
  }

  public static Cmplx asinh(Cmplx a ){
    Cmplx c = new Cmplx(0.0D, 0.0D);
    c= Cmplx.sqrt(Cmplx.square(a).plus(1.0D));
    c=a.plus(c);
    c= Cmplx.log(c);

    return c;
  }

  // Inverse hyperbolic cosine of a Complex number
  public Cmplx acosh(){
    return Cmplx.acosh(this);
  }

  public static Cmplx acosh(Cmplx a ){
    Cmplx c = new Cmplx();
    c= Cmplx.sqrt(Cmplx.square(a).minus(1.0D));
    c=a.plus(c);
    c= Cmplx.log(c);
    return c;
  }

  // Inverse hyperbolic tangent of a Complex number
  public Cmplx atanh(){
    return Cmplx.atanh(this);
  }

  public static Cmplx atanh(Cmplx a ){
    Cmplx c = new Cmplx();
    Cmplx d = new Cmplx();
    c= Cmplx.plusOne().plus(a);
    d= Cmplx.plusOne().minus(a);
    c=c.over(d);
    c= Cmplx.log(c);
    c=c.over(2.0D);
    return c;
  }

  // Inverse hyperbolic cotangent of a Complex number
  public Cmplx acoth(){
    return Cmplx.acoth(this);
  }

  public static Cmplx acoth(Cmplx a ){
    Cmplx c = new Cmplx();
    Cmplx d = new Cmplx();
    c= Cmplx.plusOne().plus(a);
    d=a.plus(1.0D);
    c=c.over(d);
    c= Cmplx.log(c);
    c=c.over(2.0D);
    return c;
  }

  // Inverse hyperbolic secant of a Complex number
  public Cmplx asech(){
    return Cmplx.asech(this);
  }

  public static Cmplx asech(Cmplx a ){
    Cmplx c = a.inverse();
    Cmplx d = (Cmplx.square(a)).minus(1.0D);
    return Cmplx.log(c.plus(Cmplx.sqrt(d)));
  }

  // Inverse hyperbolic cosecant of a Complex number
  public Cmplx acsch(){
    return Cmplx.acsch(this);
  }

  public static Cmplx acsch(Cmplx a ){
    Cmplx c = a.inverse();
    Cmplx d = (Cmplx.square(a)).plus(1.0D);
    return Cmplx.log(c.plus(Cmplx.sqrt(d)));
  }




  // LOGICAL FUNCTIONS
  // Returns true if the Complex number has a zero imaginary part, i.e. is a real number
  public static boolean isReal(Cmplx a){
    boolean test = false;
    if(a.imag==0.0D)test = true;
    return test;
  }

  public boolean isReal(){
    boolean test = false;
    if(Math.abs(this.imag)==0.0D)test = true;
    return test;
  }

  // Returns true if the Complex number has a zero real and a zero imaginary part
  // i.e. has a zero modulus
  public static boolean isZero(Cmplx a){
    boolean test = false;
    if(Math.abs(a.real)==0.0D && Math.abs(a.imag)==0.0D)test = true;
    return test;
  }

  public boolean isZero(){
    boolean test = false;
    if(Math.abs(this.real)==0.0D && Math.abs(this.imag)==0.0D)test = true;
    return test;
  }

  // Returns true if either the real or the imaginary part of the Complex number
  // is equal to plus infinity
  public boolean isPlusInfinity(){
    boolean test = false;
    if(this.real==Double.POSITIVE_INFINITY || this.imag==Double.POSITIVE_INFINITY)test = true;
    return test;
  }

  public static boolean isPlusInfinity(Cmplx a){
    boolean test = false;
    if(a.real==Double.POSITIVE_INFINITY || a.imag==Double.POSITIVE_INFINITY)test = true;
    return test;
  }

  // Returns true if either the real or the imaginary part of the Complex number
  // is equal to minus infinity
  public boolean isMinusInfinity(){
    boolean test = false;
    if(this.real==Double.NEGATIVE_INFINITY || this.imag==Double.NEGATIVE_INFINITY)test = true;
    return test;
  }

  public static boolean isMinusInfinity(Cmplx a){
    boolean test = false;
    if(a.real==Double.NEGATIVE_INFINITY || a.imag==Double.NEGATIVE_INFINITY)test = true;
    return test;
  }


  // Returns true if either the real or the imaginary part of the Complex number
  // is equal to either infinity or minus plus infinity
  public static boolean isInfinite(Cmplx a){
    boolean test = false;
    if(a.real==Double.POSITIVE_INFINITY || a.imag==Double.POSITIVE_INFINITY)test = true;
    if(a.real==Double.NEGATIVE_INFINITY || a.imag==Double.NEGATIVE_INFINITY)test = true;
    return test;
  }

  public boolean isInfinite(){
    boolean test = false;
    if(this.real==Double.POSITIVE_INFINITY || this.imag==Double.POSITIVE_INFINITY)test = true;
    if(this.real==Double.NEGATIVE_INFINITY || this.imag==Double.NEGATIVE_INFINITY)test = true;
    return test;
  }

  // Returns true if the Complex number is NaN (Not a Number)
  // i.e. is the result of an uninterpretable mathematical operation
  public static boolean isNaN(Cmplx a){
    boolean test = false;
    if(a.real!=a.real || a.imag!=a.imag)test = true;
    return test;
  }

  public boolean isNaN(){
    boolean test = false;
    if(this.real!=this.real || this.imag!=this.imag)test = true;
    return test;
  }

  // Returns true if two Complex number are identical
  // Follows the Sun Java convention of treating all NaNs as equal
  // i.e. does not satisfies the IEEE 754 specification
  // but does let hashtables operate properly
  public boolean equals(Cmplx a){
    boolean test = false;
    if(this.isNaN()&&a.isNaN()){
      test=true;
    }
    else{
      if(this.real == a.real && this.imag == a.imag)test = true;
    }
    return test;
  }

  public boolean isEqual(Cmplx a){
    boolean test = false;
    if(this.isNaN()&&a.isNaN()){
      test=true;
    }
    else{
      if(this.real == a.real && this.imag == a.imag)test = true;
    }
    return test;
  }


  public static boolean isEqual(Cmplx a, Cmplx b){
    boolean test = false;
    if(isNaN(a)&&isNaN(b)){
      test=true;
    }
    else{
      if(a.real == b.real && a.imag == b.imag)test = true;
    }
    return test;
  }



  // returns true if the differences between the real and imaginary parts of two complex numbers
  // are less than fract mult the larger real and imaginary part
  public boolean equalsWithinLimits(Cmplx a, double fract){
    return isEqualWithinLimits(a, fract);
  }

  public boolean isEqualWithinLimits(Cmplx a, double fract){
    boolean test = false;

    double rt = this.getReal();
    double ra = a.getReal();
    double it = this.getImag();
    double ia = a.getImag();
    double rdn = 0.0D;
    double idn = 0.0D;
    double rtest = 0.0D;
    double itest = 0.0D;

    if(rt==0.0D && it==0.0D && ra==0.0D && ia==0.0D)test=true;
    if(!test){
      rdn=Math.abs(rt);
      if(Math.abs(ra)>rdn)rdn=Math.abs(ra);
      if(rdn==0.0D){
        rtest=0.0;
      }
      else{
        rtest=Math.abs(ra-rt)/rdn;
      }
      idn=Math.abs(it);
      if(Math.abs(ia)>idn)idn=Math.abs(ia);
      if(idn==0.0D){
        itest=0.0;
      }
      else{
        itest=Math.abs(ia-it)/idn;
      }
      if(rtest<fract && itest<fract)test=true;
    }

    return test;
  }

  public static boolean isEqualWithinLimits(Cmplx a, Cmplx b, double fract){
    boolean test = false;

    double rb = b.getReal();
    double ra = a.getReal();
    double ib = b.getImag();
    double ia = a.getImag();
    double rdn = 0.0D;
    double idn = 0.0D;

    if(ra==0.0D && ia==0.0D && rb==0.0D && ib==0.0D)test=true;
    if(!test){
      rdn=Math.abs(rb);
      if(Math.abs(ra)>rdn)rdn=Math.abs(ra);
      idn=Math.abs(ib);
      if(Math.abs(ia)>idn)idn=Math.abs(ia);
      if(Math.abs(ra-rb)/rdn<fract && Math.abs(ia-ia)/idn<fract)test=true;
    }

    return test;
  }

  // SOME USEFUL NUMBERS
  // returns the number zero (0) as a complex number
  public static Cmplx zero(){
    Cmplx c = new Cmplx();
    c.real=0.0D;
    c.imag=0.0D;
    return c;
  }

  // returns the number one (+1) as a complex number
  public static Cmplx plusOne(){
    Cmplx c = new Cmplx();
    c.real=1.0D;
    c.imag=0.0D;
    return c;
  }

  // returns the number minus one (-1) as a complex number
  public static Cmplx minusOne(){
    Cmplx c = new Cmplx();
    c.real=-1.0D;
    c.imag=0.0D;
    return c;
  }

  // returns plus j
  public static Cmplx plusJay(){
    Cmplx c = new Cmplx();
    c.real=0.0D;
    c.imag=1.0D;
    return c;
  }

  // returns minus j
  public static Cmplx minusJay(){
    Cmplx c = new Cmplx();
    c.real=0.0D;
    c.imag=-1.0D;
    return c;
  }

  // returns pi as a Complex number
  public static Cmplx pi(){
    Cmplx c = new Cmplx();
    c.real=Math.PI;
    c.imag=0.0D;
    return c;
  }

  // returns 2.pi.j
  public static Cmplx twoPiJay(){
    Cmplx c = new Cmplx();
    c.real=0.0D;
    c.imag=2.0D*Math.PI;
    return c;
  }

  // infinity + infinity.j
  public static Cmplx plusInfinity(){
    Cmplx c = new Cmplx();
    c.real=Double.POSITIVE_INFINITY;
    c.imag=Double.POSITIVE_INFINITY;
    return c;
  }

  // -infinity - infinity.j
  public static Cmplx minusInfinity(){
    Cmplx c = new Cmplx();
    c.real=Double.NEGATIVE_INFINITY;
    c.imag=Double.NEGATIVE_INFINITY;
    return c;
  }

  // PRIVATE METHODS
  // returns a Complex number raised to a double power
  // this method is used for calculation within this class file
  // see above for corresponding public method
  private static Cmplx powDouble(Cmplx a, double b){
    Cmplx z = new Cmplx();
    double re=a.real;
    double im=a.imag;

    if(a.isZero()){
      if(b==0.0){
        z = new Cmplx(1.0, 0.0);
      }
      else{
        if(b>0.0){
          z = new Cmplx(0.0, 0.0);
        }
        else{
          if(b<0.0){
            z = new Cmplx(Double.POSITIVE_INFINITY, 0.0);
          }
        }
      }
    }
    else{
      if(im==0.0D && re>0.0D){
        z.real=Math.pow(re, b);
        z.imag=0.0D;
      }
      else{
        if(re==0.0D){
          z= Cmplx.exp(Cmplx.times(b, Cmplx.log(a)));
        }
        else{
          double c=Math.pow(re*re+im*im, b/2.0D);
          double th=Math.atan2(im, re);
          z.real=c*Math.cos(b*th);
          z.imag=c*Math.sin(b*th);
        }
      }
    }
    return z;
  }

}
