/*
*   Class   ComplexMatrix
*
*   Defines a complex matrix and includes the methods
*   needed for standard matrix manipulations, e.g. multiplation,
*   and related procedures, e.g. solution of complex linear
*   simultaneous equations
*
*   See class PhasorMatrix for phasor matrix manipulation
*   See class Complex for standard complex arithmetic
*
* 	WRITTEN BY: Dr Michael Thomas Flanagan
*
*   DATE:	    June 2002
*   UPDATES:    16 February 2006 Set methods corrected thanks to Myriam Servi?res, Equipe IVC , Ecole Polytechnique de l'universit? de Nantes, Laboratoire IRCCyN/UMR CNRS
*               7 March 2006
*               31 March 2006 Norm methods corrected thanks to Jinshan Wu, University of British Columbia
*               22 April 2006 getSubMatrix corrected thanks to Joachim Wesner
*               1 July 2007 dividsion and extra conversion methods added
*               19 April 2008 rowMatrix and columnMatrix added
*               8 October 2008 inverse methods updated
*               16 June 2009 timesEquals corrected thanks to Bjorn Nordstom, Ericsonn
*               5 November 2009 Reduced Row Echelon Form added
*               12 January 2010 SetSubMatrix method corrected thanks to Dr. Kay Nehrke, Philips Technologie, Hamburg
*
*
*   DOCUMENTATION:
*   See Michael Thomas Flanagan's Java library on-line web pages:
*   http://www.ee.ucl.ac.uk/~mflanaga/java/ComplexMatrix.html
*   http://www.ee.ucl.ac.uk/~mflanaga/java/
*
*   Copyright (c) 2002 - 2010 Michael Thomas Flanagan

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

package flanagan.complex;

//import flanagan.math.*;

public class ComplexMatrix{

  private int nrow = 0;               // number of rows
  private int ncol = 0;               // number of columns
  private Cmplx matrix[][] = null;  // 2-D Complex Matrix
  private int index[] = null;         // row permutation index
  private double dswap = 1.0D;        // row swap index
  private static final double TINY = 1.0e-30;

  /*********************************************************/

  // CONSTRUCTORS
  // Construct a nrow x ncol matrix of complex variables all equal to zero
  public ComplexMatrix(int nrow, int ncol){
    this.nrow = nrow;
    this.ncol = ncol;
    this.matrix = Cmplx.twoDarray(nrow, ncol);
    this.index = new int[nrow];
    for(int i=0;i<nrow;i++)this.index[i]=i;
    this.dswap=1.0;
  }

  // Construct a nrow x ncol matrix of complex variables all equal to the complex number const
  public ComplexMatrix(int nrow, int ncol, Cmplx constant){
    this.nrow = nrow;
    this.ncol = ncol;
    this.matrix = Cmplx.twoDarray(nrow, ncol, constant);
    this.index = new int[nrow];
    for(int i=0;i<nrow;i++)this.index[i]=i;
    this.dswap=1.0;
  }

  // Construct matrix with a copy of an existing nrow x ncol 2-D array of complex variables
  public ComplexMatrix(Cmplx[][] twoD){
    this.nrow = twoD.length;
    this.ncol = twoD[0].length;
    this.matrix = Cmplx.twoDarray(nrow, ncol);
    for(int i=0; i<nrow; i++){
      if(twoD[i].length!=ncol)throw new IllegalArgumentException("All rows must have the same length");
      for(int j=0; j<ncol; j++){
        this.matrix[i][j]= Cmplx.copy(twoD[i][j]);
      }
    }
    this.index = new int[nrow];
    for(int i=0;i<nrow;i++)this.index[i]=i;
    this.dswap=1.0;
  }

  // Construct matrix with a copy of an existing nrow x ncol 2-D array of double variables
  public ComplexMatrix(double[][] twoD){
    this.nrow = twoD.length;
    this.ncol = twoD[0].length;
    for(int i=0; i<nrow; i++){
      if(twoD[i].length!=ncol)throw new IllegalArgumentException("All rows must have the same length");
    }
    this.matrix = Cmplx.twoDarray(nrow, ncol);
    for(int i=0; i<nrow; i++){
      for(int j=0; j<ncol; j++){
        this.matrix[i][j] = new Cmplx(twoD[i][j], 0.0);
      }
    }
    this.index = new int[nrow];
    for(int i=0;i<nrow;i++)this.index[i]=i;
    this.dswap=1.0;
  }

  // Construct matrix with a copy of the complex matrix and permutation index of an existing ComplexMatrix bb.
  public ComplexMatrix(ComplexMatrix bb){
    this.nrow = bb.nrow;
    this.ncol = bb.ncol;
    this.matrix = (bb.copy()).matrix;
    this.index = bb.index;
    this.dswap = bb.dswap;
  }



  // SET VALUES
  // Set the matrix with a copy of an existing nrow x ncol 2-D matrix of complex variables
  public void setTwoDarray(Cmplx[][] aarray){
    if(this.nrow != aarray.length)throw new IllegalArgumentException("row length of this ComplexMatrix differs from that of the 2D array argument");
    if(this.ncol != aarray[0].length)throw new IllegalArgumentException("column length of this ComplexMatrix differs from that of the 2D array argument");
    for(int i=0; i<nrow; i++){
      if(aarray[i].length!=ncol)throw new IllegalArgumentException("All rows must have the same length");
      for(int j=0; j<ncol; j++){
        this.matrix[i][j]= Cmplx.copy(aarray[i][j]);
      }
    }
  }

  // Set the matrix with a copy of an existing nrow x ncol 2-D matrix of double variables
  public void setTwoDarray(double[][] aarray){
    if(this.nrow != aarray.length)throw new IllegalArgumentException("row length of this ComplexMatrix differs from that of the 2D array argument");
    if(this.ncol != aarray[0].length)throw new IllegalArgumentException("column length of this ComplexMatrix differs from that of the 2D array argument");
    for(int i=0; i<nrow; i++){
      if(aarray[i].length!=ncol)throw new IllegalArgumentException("All rows must have the same length");
      for(int j=0; j<ncol; j++){
        this.matrix[i][j]=new Cmplx(aarray[i][j]);
      }
    }
  }

  // Set an individual array element
  // i = row index
  // j = column index
  // aa = value of the element
  public void setElement(int i, int j, Cmplx aa){
    this.matrix[i][j]= Cmplx.copy(aa);
  }

  // Set an individual array element
  // i = row index
  // j = column index
  // aa = real part of the element
  // bb = imag part of the element
  public void setElement(int i, int j, double aa, double bb){
    this.matrix[i][j].reset(aa, bb);
  }

  // Set a sub-matrix starting with row index i, column index j
  // and ending with row index k, column index l
  // See SetSubMatrix below - this method has ben retained for compatibilityb purposes
  public void setSubMatrix(int i, int j, int k, int l, Cmplx[][] subMatrix){
    this.setSubMatrix(i, j, subMatrix);
  }

  // Set a sub-matrix starting with row index i, column index j
  public void setSubMatrix(int i, int j, Cmplx[][] subMatrix){
    int k = subMatrix.length;
    int l = subMatrix[0].length;
    if(i>k)throw new IllegalArgumentException("row indices inverted");
    if(j>l)throw new IllegalArgumentException("column indices inverted");
    int m = 0;
    int n = 0;
    for(int p=0; p<k; p++){
      n = 0;
      for(int q=0; q<l; q++){
        this.matrix[i+p][j+q] = Cmplx.copy(subMatrix[m][n]);
        n++;
      }
      m++;
    }
  }


  // Set a sub-matrix
  // row = array of row indices
  // col = array of column indices
  public void setSubMatrix(int[] row, int[] col, Cmplx[][] subMatrix){
    int n=row.length;
    int m=col.length;
    for(int p=0; p<n; p++){
      for(int q=0; q<m; q++){
        this.matrix[row[p]][col[q]] = Cmplx.copy(subMatrix[p][q]);
      }
    }
  }


  // SPECIAL MATRICES
  // Construct a complex identity matrix
  public static ComplexMatrix identityMatrix(int nrow){
    ComplexMatrix u = new ComplexMatrix(nrow, nrow);
    for(int i=0; i<nrow; i++){
      u.matrix[i][i]= Cmplx.plusOne();
    }
    return u;
  }

  // Construct a complex scalar matrix
  public static ComplexMatrix scalarMatrix(int nrow, Cmplx diagconst){
    ComplexMatrix u = new ComplexMatrix(nrow, nrow);
    Cmplx[][] uarray = u.getArrayReference();
    for(int i=0; i<nrow; i++){
      for(int j=i; j<nrow; j++){
        if(i==j){
          uarray[i][j]= Cmplx.copy(diagconst);
        }
      }
    }
    return u;
  }

  // Construct a complex diagonal matrix
  public static ComplexMatrix diagonalMatrix(int nrow, Cmplx[] diag){
    if(diag.length!=nrow)throw new IllegalArgumentException("matrix dimension differs from diagonal array length");
    ComplexMatrix u = new ComplexMatrix(nrow, nrow);
    Cmplx[][] uarray = u.getArrayReference();
    for(int i=0; i<nrow; i++){
      for(int j=i; j<nrow; j++){
        if(i==j){
          uarray[i][j]= Cmplx.copy(diag[i]);
        }
      }
    }
    return u;
  }

  // COLUMN MATRICES
  // Converts a 1-D array of Complex to a column  matrix
  public static ComplexMatrix columnMatrix(Cmplx[] darray){
    int nr = darray.length;
    ComplexMatrix pp = new ComplexMatrix(nr, 1);
    for(int i=0; i<nr; i++)pp.matrix[i][0] = darray[i];
    return pp;
  }

  // ROW MATRICES
  // Converts a 1-D array of Complex to a row matrix
  public static ComplexMatrix rowMatrix(Cmplx[] darray){
    int nc = darray.length;
    ComplexMatrix pp = new ComplexMatrix(1, nc);
    for(int i=0; i<nc; i++)pp.matrix[0][i] = darray[i];
    return pp;
  }

  // CONVERSIONS
  // Converts a 1-D array of Complex to a complex column matrix
  public static ComplexMatrix toComplexColumnMatrix(Cmplx[] carray){
    int nr = carray.length;
    ComplexMatrix cc = new ComplexMatrix(nr, 1);
    for(int i=0; i<nr; i++)cc.matrix[i][0] = carray[i].copy();
    return cc;
  }

  // Converts a 1-D array of doubles to a complex coumn matrix
  public static ComplexMatrix toComplexColumnMatrix(double[] darray){
    int nr = darray.length;
    ComplexMatrix cc = new ComplexMatrix(nr, 1);
    for(int i=0; i<nr; i++)cc.matrix[i][0].reset(darray[i], 0.0D);
    return cc;
  }

  // Converts a 1-D array of Complex to a complex row matrix
  public static ComplexMatrix toComplexRowMatrix(Cmplx[] carray){
    int nc = carray.length;
    ComplexMatrix cc = new ComplexMatrix(1, nc);
    for(int i=0; i<nc; i++)cc.matrix[0][i] = carray[i].copy();
    return cc;
  }

  // Converts a 1-D array of doubles to a complex row matrix
  public static ComplexMatrix toComplexRowMatrix(double[] darray){
    int nc = darray.length;
    ComplexMatrix cc = new ComplexMatrix(1, nc);
    for(int i=0; i<nc; i++)cc.matrix[0][i].reset(darray[i], 0.0D);
    return cc;
  }


  // Converts a 2D array of doubles to a complex matrix (ComplexMatix)
  public static ComplexMatrix toComplexMatrix(double[][] darray){
    int nr = darray.length;
    int nc = darray[0].length;
    for(int i=1; i<nr; i++){
      if(darray[i].length!=nc)throw new IllegalArgumentException("All rows must have the same length");
    }
    ComplexMatrix pp = new ComplexMatrix(nr, nc);
    for(int i=0; i<pp.nrow; i++){
      for(int j=0; j<pp.ncol; j++){
        pp.matrix[i][j].reset(darray[i][j], 0.0D);
      }
    }
    return pp;
  }

  // GET VALUES
  // Return the number of rows
  public int getNrow(){
    return this.nrow;
  }

  // Return the number of columns
  public int getNcol(){
    return this.ncol;
  }

  // Return a reference to the internal 2-D array
  public Cmplx[][] getArrayReference(){
    return this.matrix;
  }

  // Return a reference to the internal 2-D array
  public Cmplx[][] getArray(){
    return this.matrix;
  }

  // Return a reference to the internal 2-D array
  // included for backward compatibility with earlier incorrect documentation
  public Cmplx[][] getArrayPointer(){
    return this.matrix;
  }

  // Return a copy of the internal 2-D array
  public Cmplx[][] getArrayCopy(){
    Cmplx[][] c = new Cmplx[this.nrow][this.ncol];
    for(int i=0; i<nrow; i++){
      for(int j=0; j<ncol; j++){
        c[i][j]= Cmplx.copy(matrix[i][j]);
      }
    }
    return c;
  }

  // Return a single element of the internal 2-D array
  public Cmplx getElementReference(int i, int j){
    return this.matrix[i][j];
  }

  // Return a reference to a single element of the internal 2-D array
  // included for backward compatibility with earlier incorrect documentation
  public Cmplx getElementPointer(int i, int j){
    return this.matrix[i][j];
  }

  // Return a copy of a single element of the internal 2-D array
  public Cmplx getElementCopy(int i, int j){
    return Cmplx.copy(this.matrix[i][j]);
  }

  // Return a sub-matrix starting with row index i, column index j
  // and ending with row index k, column index l
  public ComplexMatrix getSubMatrix(int i, int j, int k, int l){
    if(i>k)throw new IllegalArgumentException("row indices inverted");
    if(j>l)throw new IllegalArgumentException("column indices inverted");
    int n=k-i+1, m=l-j+1;
    ComplexMatrix subMatrix = new ComplexMatrix(n, m);
    Cmplx[][] sarray = subMatrix.getArrayReference();
    for(int p=0; p<n; p++){
      for(int q=0; q<m; q++){
        sarray[p][q]= Cmplx.copy(this.matrix[i+p][j+q]);
      }
    }
    return subMatrix;
  }

  // Return a sub-matrix
  // row = array of row indices
  // col = array of column indices
  public ComplexMatrix getSubMatrix(int[] row, int[] col){
    int n = row.length;
    int m = col.length;
    ComplexMatrix subMatrix = new ComplexMatrix(n, m);
    Cmplx[][] sarray = subMatrix.getArrayReference();
    for(int i=0; i<n; i++){
      for(int j=0; j<m; j++){
        sarray[i][j]= Cmplx.copy(this.matrix[row[i]][col[j]]);
      }
    }
    return subMatrix;
  }

  // Return a reference to the permutation index array
  public int[]  getIndexReference(){
    return this.index;
  }

  // Return a reference to the permutation index array
  public int[]  getIndexPointer(){
    return this.index;
  }

  // Return a copy of the permutation index array
  public int[]  getIndexCopy(){
    int[] indcopy = new int[this.nrow];
    for(int i=0; i<this.nrow; i++){
      indcopy[i]=this.index[i];
    }
    return indcopy;
  }

  // Return the row swap index
  public double getSwap(){
    return this.dswap;
  }

  // COPY
  // Copy a ComplexMatrix [static method]
  public static ComplexMatrix copy(ComplexMatrix a){
    if(a==null){
      return null;
    }
    else{
      int nr = a.getNrow();
      int nc = a.getNcol();
      Cmplx[][] aarray = a.getArrayReference();
      ComplexMatrix b = new ComplexMatrix(nr,nc);
      b.nrow = nr;
      b.ncol = nc;
      Cmplx[][] barray = b.getArrayReference();
      for(int i=0; i<nr; i++){
        for(int j=0; j<nc; j++){
          barray[i][j]= Cmplx.copy(aarray[i][j]);
        }
      }
      for(int i=0; i<nr; i++)b.index[i] = a.index[i];
      return b;
    }
  }

  // Copy a ComplexMatrix [instance method]
  public ComplexMatrix copy(){
    if(this==null){
      return null;
    }
    else{
      int nr = this.nrow;
      int nc = this.ncol;
      ComplexMatrix b = new ComplexMatrix(nr,nc);
      Cmplx[][] barray = b.getArrayReference();
      b.nrow = nr;
      b.ncol = nc;
      for(int i=0; i<nr; i++){
        for(int j=0; j<nc; j++){
          barray[i][j]= Cmplx.copy(this.matrix[i][j]);
        }
      }
      for(int i=0; i<nr; i++)b.index[i] = this.index[i];
      return b;
    }
  }

  // Clone a ComplexMatrix
  public Object clone(){
    if(this==null){
      return null;
    }
    else{
      int nr = this.nrow;
      int nc = this.ncol;
      ComplexMatrix b = new ComplexMatrix(nr,nc);
      Cmplx[][] barray = b.getArrayReference();
      b.nrow = nr;
      b.ncol = nc;
      for(int i=0; i<nr; i++){
        for(int j=0; j<nc; j++){
          barray[i][j]= Cmplx.copy(this.matrix[i][j]);
        }
      }
      for(int i=0; i<nr; i++)b.index[i] = this.index[i];
      return (Object) b;
    }
  }

  // ADDITION
  // Add this matrix to matrix B.  This matrix remains unaltered [instance method]
  public ComplexMatrix plus(ComplexMatrix bmat){
    if((this.nrow!=bmat.nrow)||(this.ncol!=bmat.ncol)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }
    int nr=bmat.nrow;
    int nc=bmat.ncol;
    ComplexMatrix cmat = new ComplexMatrix(nr,nc);
    Cmplx[][] carray = cmat.getArrayReference();
    for(int i=0; i<nr; i++){
      for(int j=0; j<nc; j++){
        carray[i][j]=this.matrix[i][j].plus(bmat.matrix[i][j]);
      }
    }
    return cmat;
  }

  // Add this matrix to a Comlex 2-D array.  [instance method]
  public ComplexMatrix plus(Cmplx[][] bmat){
    int nr=bmat.length;
    int nc=bmat[0].length;
    if((this.nrow!=nr)||(this.ncol!=nc)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }
    ComplexMatrix cmat = new ComplexMatrix(nr,nc);
    Cmplx[][] carray = cmat.getArrayReference();
    for(int i=0; i<nr; i++){
      for(int j=0; j<nc; j++){
        carray[i][j]=this.matrix[i][j].plus(bmat[i][j]);
      }
    }
    return cmat;
  }


  // Add this matrix to a real 2-D array.  [instance method]
  public ComplexMatrix plus(double[][] bmat){
    int nr=bmat.length;
    int nc=bmat[0].length;
    if((this.nrow!=nr)||(this.ncol!=nc)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }

    ComplexMatrix cmat = new ComplexMatrix(nr,nc);
    Cmplx[][] carray = cmat.getArrayReference();
    for(int i=0; i<nr; i++){
      for(int j=0; j<nc; j++){
        carray[i][j]=this.matrix[i][j].plus(bmat[i][j]);
      }
    }
    return cmat;
  }

  // Add matrices A and B [static method]
  public static ComplexMatrix plus(ComplexMatrix amat, ComplexMatrix bmat){
    if((amat.nrow!=bmat.nrow)||(amat.ncol!=bmat.ncol)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }
    int nr=amat.nrow;
    int nc=amat.ncol;
    ComplexMatrix cmat = new ComplexMatrix(nr,nc);
    Cmplx[][] carray = cmat.getArrayReference();
    for(int i=0; i<nr; i++){
      for(int j=0; j<nc; j++){
        carray[i][j]=amat.matrix[i][j].plus(bmat.matrix[i][j]);
      }
    }
    return cmat;
  }

  // Add matrix B to this matrix [equivalence of +=]
  public void plusEquals(ComplexMatrix bmat){
    if((this.nrow!=bmat.nrow)||(this.ncol!=bmat.ncol)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }
    int nr=bmat.nrow;
    int nc=bmat.ncol;

    for(int i=0; i<nr; i++){
      for(int j=0; j<nc; j++){
        this.matrix[i][j].plusEquals(bmat.matrix[i][j]);
      }
    }
  }

  // SUBTRACTION
  // Subtract matrix B from this matrix.   This matrix remains unaltered [instance method]
  public ComplexMatrix minus(ComplexMatrix bmat){
    if((this.nrow!=bmat.nrow)||(this.ncol!=bmat.ncol)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }
    int nr=this.nrow;
    int nc=this.ncol;
    ComplexMatrix cmat = new ComplexMatrix(nr,nc);
    Cmplx[][] carray = cmat.getArrayReference();
    for(int i=0; i<nr; i++){
      for(int j=0; j<nc; j++){
        carray[i][j]=this.matrix[i][j].minus(bmat.matrix[i][j]);
      }
    }
    return cmat;
  }

  // Subtract  Comlex 2-D array from this matrix.  [instance method]
  public ComplexMatrix minus(Cmplx[][] bmat){
    int nr=bmat.length;
    int nc=bmat[0].length;
    if((this.nrow!=nr)||(this.ncol!=nc)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }
    ComplexMatrix cmat = new ComplexMatrix(nr,nc);
    Cmplx[][] carray = cmat.getArrayReference();
    for(int i=0; i<nr; i++){
      for(int j=0; j<nc; j++){
        carray[i][j]=this.matrix[i][j].minus(bmat[i][j]);
      }
    }
    return cmat;
  }


  // Subtract a real 2-D array from this matrix.  [instance method]
  public ComplexMatrix minus(double[][] bmat){
    int nr=bmat.length;
    int nc=bmat[0].length;
    if((this.nrow!=nr)||(this.ncol!=nc)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }

    ComplexMatrix cmat = new ComplexMatrix(nr,nc);
    Cmplx[][] carray = cmat.getArrayReference();
    for(int i=0; i<nr; i++){
      for(int j=0; j<nc; j++){
        carray[i][j]=this.matrix[i][j].minus(bmat[i][j]);
      }
    }
    return cmat;
  }


  // Subtract matrix B from matrix A [static method]
  public static ComplexMatrix minus(ComplexMatrix amat, ComplexMatrix bmat){
    if((amat.nrow!=bmat.nrow)||(amat.ncol!=bmat.ncol)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }
    int nr=amat.nrow;
    int nc=amat.ncol;
    ComplexMatrix cmat = new ComplexMatrix(nr,nc);
    Cmplx[][] carray = cmat.getArrayReference();
    for(int i=0; i<nr; i++){
      for(int j=0; j<nc; j++){
        carray[i][j]=amat.matrix[i][j].minus(bmat.matrix[i][j]);
      }
    }
    return cmat;
  }

  // Subtract matrix B from this matrix [equivlance of -=]
  public void minusEquals(ComplexMatrix bmat){
    if((this.nrow!=bmat.nrow)||(this.ncol!=bmat.ncol)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }
    int nr=bmat.nrow;
    int nc=bmat.ncol;

    for(int i=0; i<nr; i++){
      for(int j=0; j<nc; j++){
        this.matrix[i][j].minusEquals(bmat.matrix[i][j]);
      }
    }
  }

  // MULTIPLICATION
  // Multiply this complex matrix by a complex matrix.   [instance method]
  // This matrix remains unaltered.
  public ComplexMatrix times(ComplexMatrix bmat){
    if(this.ncol!=bmat.nrow)throw new IllegalArgumentException("Nonconformable matrices");

    ComplexMatrix cmat = new ComplexMatrix(this.nrow, bmat.ncol);
    Cmplx[][] carray = cmat.getArrayReference();
    Cmplx sum = new Cmplx();

    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<bmat.ncol; j++){
        sum= Cmplx.zero();
        for(int k=0; k<this.ncol; k++){
          sum.plusEquals(this.matrix[i][k].times(bmat.matrix[k][j]));
        }
        carray[i][j]= Cmplx.copy(sum);
      }
    }
    return cmat;
  }

  // Multiply this complex matrix by a complex 2-D array.   [instance method]
  public ComplexMatrix times(Cmplx[][] bmat){
    int nr=bmat.length;
    int nc=bmat[0].length;
    if(this.ncol!=nr)throw new IllegalArgumentException("Nonconformable matrices");

    ComplexMatrix cmat = new ComplexMatrix(this.nrow, nc);
    Cmplx[][] carray = cmat.getArrayReference();
    Cmplx sum = new Cmplx();

    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<nc; j++){
        sum= Cmplx.zero();
        for(int k=0; k<this.ncol; k++){
          sum.plusEquals(this.matrix[i][k].times(bmat[k][j]));
        }
        carray[i][j]= Cmplx.copy(sum);
      }
    }
    return cmat;
  }


  // Multiply this complex matrix by a real 2-D array.   [instance method]
  public ComplexMatrix times(double[][] bmat){
    int nr=bmat.length;
    int nc=bmat[0].length;
    if(this.ncol!=nr)throw new IllegalArgumentException("Nonconformable matrices");

    ComplexMatrix cmat = new ComplexMatrix(this.nrow, nc);
    Cmplx[][] carray = cmat.getArrayReference();
    Cmplx sum = new Cmplx();

    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<nc; j++){
        sum= Cmplx.zero();
        for(int k=0; k<this.ncol; k++){
          sum.plusEquals(this.matrix[i][k].times(bmat[k][j]));
        }
        carray[i][j]= Cmplx.copy(sum);
      }
    }
    return cmat;
  }

  // Multiply this complex matrix by a complex constant [instance method]
  // This matrix remains unaltered
  public ComplexMatrix times(Cmplx constant){
    ComplexMatrix cmat = new ComplexMatrix(this.nrow, this.ncol);
    Cmplx[][] carray = cmat.getArrayReference();

    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<this.ncol; j++){
        carray[i][j] = this.matrix[i][j].times(constant);
      }
    }
    return cmat;
  }

  // Multiply this complex matrix by a real (double) constant [instance method]
  // This matrix remains unaltered.
  public ComplexMatrix times(double constant){
    ComplexMatrix cmat = new ComplexMatrix(this.nrow, this.ncol);
    Cmplx[][] carray = cmat.getArrayReference();
    Cmplx cconstant = new Cmplx(constant, 0.0);

    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<this.ncol; j++){
        carray[i][j] = this.matrix[i][j].times(cconstant);
      }
    }
    return cmat;
  }

  // Multiply two complex matrices {static method]
  public static ComplexMatrix times(ComplexMatrix amat, ComplexMatrix bmat){
    if(amat.ncol!=bmat.nrow)throw new IllegalArgumentException("Nonconformable matrices");

    ComplexMatrix cmat = new ComplexMatrix(amat.nrow, bmat.ncol);
    Cmplx[][] carray = cmat.getArrayReference();
    Cmplx sum = new Cmplx();

    for(int i=0; i<amat.nrow; i++){
      for(int j=0; j<bmat.ncol; j++){
        sum= Cmplx.zero();
        for(int k=0; k<amat.ncol; k++){
          sum.plusEquals(amat.matrix[i][k].times(bmat.matrix[k][j]));
        }
        carray[i][j]= Cmplx.copy(sum);
      }
    }
    return cmat;
  }


  // Multiply a complex matrix by a complex constant [static method]
  public static ComplexMatrix times(ComplexMatrix amat, Cmplx constant){
    ComplexMatrix cmat = new ComplexMatrix(amat.nrow, amat.ncol);
    Cmplx[][] carray = cmat.getArrayReference();

    for(int i=0; i<amat.nrow; i++){
      for(int j=0; j<amat.ncol; j++){
        carray[i][j] = amat.matrix[i][j].times(constant);
      }
    }
    return cmat;
  }

  // Multiply a complex matrix by a real (double) constant [static method]
  public static ComplexMatrix times(ComplexMatrix amat, double constant){
    ComplexMatrix cmat = new ComplexMatrix(amat.nrow, amat.ncol);
    Cmplx[][] carray = cmat.getArrayReference();
    Cmplx cconstant = new Cmplx(constant, 0.0);

    for(int i=0; i<amat.nrow; i++){
      for(int j=0; j<amat.ncol; j++){
        carray[i][j] = amat.matrix[i][j].times(cconstant);
      }
    }
    return cmat;
  }

  // Multiply this matrix by a complex matrix [equivalence of *=]
  public void timesEquals(ComplexMatrix bmat){
    if(this.ncol!=bmat.nrow)throw new IllegalArgumentException("Nonconformable matrices");

    ComplexMatrix cmat = new ComplexMatrix(this.nrow, bmat.ncol);
    Cmplx[][] carray = cmat.getArrayReference();
    Cmplx sum = new Cmplx();

    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<bmat.ncol; j++){
        sum= Cmplx.zero();
        for(int k=0; k<this.ncol; k++){
          sum.plusEquals(this.matrix[i][k].times(bmat.matrix[k][j]));
        }
        carray[i][j]= Cmplx.copy(sum);
      }
    }

    this.nrow = cmat.nrow;
    this.ncol = cmat.ncol;
    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<this.ncol; j++){
        this.matrix[i][j] = cmat.matrix[i][j];
      }
    }
  }




  // Multiply this matrix by a complex constant [equivalence of *=]
  public void timesEquals(Cmplx constant){

    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<this.ncol; j++){
        this.matrix[i][j].timesEquals(constant);
      }
    }
  }

  // Multiply this matrix by a real (double) constant [equivalence of *=]
  public void timesEquals(double constant){
    Cmplx cconstant = new Cmplx(constant, 0.0);

    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<this.ncol; j++){
        this.matrix[i][j].timesEquals(cconstant);
      }
    }
  }

  // DIVISION
  // Divide this ComplexMatrix by a ComplexMatrix - instance method.
  public ComplexMatrix over(ComplexMatrix bmat){
    if((this.nrow!=bmat.nrow)||(this.ncol!=bmat.ncol)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }
    return this.times(bmat.inverse());
  }

  // Divide this matrix by a Complex 2-D array - instance method.
  public ComplexMatrix over(Cmplx[][] bmat){
    int nr=bmat.length;
    int nc=bmat[0].length;
    if((this.nrow!=nr)||(this.ncol!=nc)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }

    ComplexMatrix cmat = new ComplexMatrix(bmat);
    return this.times(cmat.inverse());
  }


  // Divide this ComplexMatrix by a 2D array of double - instance method.
  public ComplexMatrix over(double[][] bmat){
    ComplexMatrix pmat = ComplexMatrix.toComplexMatrix(bmat);
    return this.over(pmat);
  }

  // Divide this ComplexMatrix by a ComplexMatrix - static method.
  public ComplexMatrix over(ComplexMatrix amat, ComplexMatrix bmat){
    if((amat.nrow!=bmat.nrow)||(amat.ncol!=bmat.ncol)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }
    return amat.times(bmat.inverse());
  }

  // Divide this ComplexMatrix by a ComplexMatrix [equivalence of /=]
  public void overEquals(ComplexMatrix bmat){
    if((this.nrow!=bmat.nrow)||(this.ncol!=bmat.ncol)){
      throw new IllegalArgumentException("Array dimensions do not agree");
    }
    ComplexMatrix cmat = new ComplexMatrix(bmat);
    this.timesEquals(cmat.inverse());
  }

  // INVERSE
  // Inverse of a square complex matrix [instance method]
  public ComplexMatrix inverse(){
    int n = this.nrow;
    if(n!=this.ncol)throw new IllegalArgumentException("Matrix is not square");
    ComplexMatrix invmat = new ComplexMatrix(n, n);

    if(n==1){
      Cmplx[][] hold = this.getArrayCopy();
      if(hold[0][0].isZero())throw new IllegalArgumentException("Matrix is singular");
      hold[0][0] = Cmplx.plusOne().over(hold[0][0]);
      invmat = new ComplexMatrix(hold);
    }
    else{
      if(n==2){
        Cmplx[][] hold = this.getArrayCopy();
        Cmplx det = (hold[0][0].times(hold[1][1])).minus(hold[0][1].times(hold[1][0]));
        if(det.isZero())throw new IllegalArgumentException("Matrix is singular");

        Cmplx[][] hold2 = Cmplx.twoDarray(2,2);
        hold2[0][0] = hold[1][1].over(det);
        hold2[1][1] = hold[0][0].over(det);
        hold2[1][0] = hold[1][0].negate().over(det);
        hold2[0][1] = hold[0][1].negate().over(det);
        invmat = new ComplexMatrix(hold2);
      }
      else{
        Cmplx[] col = new Cmplx[n];
        Cmplx[] xvec = new Cmplx[n];
        Cmplx[][] invarray = invmat.getArrayReference();
        ComplexMatrix ludmat;

        ludmat = this.luDecomp();
        for(int j=0; j<n; j++){
          for(int i=0; i<n; i++)col[i]= Cmplx.zero();
          col[j]= Cmplx.plusOne();
          xvec=ludmat.luBackSub(col);
          for(int i=0; i<n; i++)invarray[i][j]= Cmplx.copy(xvec[i]);
        }
      }
    }
    return invmat;
  }

  // Inverse of a square complex matrix [static method]
  public static ComplexMatrix inverse(ComplexMatrix amat){
    int n = amat.nrow;
    if(n!=amat.ncol)throw new IllegalArgumentException("Matrix is not square");

    ComplexMatrix invmat = new ComplexMatrix(n, n);

    if(n==1){
      Cmplx[][] hold = amat.getArrayCopy();
      if(hold[0][0].isZero())throw new IllegalArgumentException("Matrix is singular");
      hold[0][0] = Cmplx.plusOne().over(hold[0][0]);
      invmat = new ComplexMatrix(hold);
    }
    else{
      if(n==2){
        Cmplx[][] hold = amat.getArrayCopy();
        Cmplx det = (hold[0][0].times(hold[1][1])).minus(hold[0][1].times(hold[1][0]));
        if(det.isZero())throw new IllegalArgumentException("Matrix is singular");

        Cmplx[][] hold2 = Cmplx.twoDarray(2,2);
        hold2[0][0] = hold[1][1].over(det);
        hold2[1][1] = hold[0][0].over(det);
        hold2[1][0] = hold[1][0].negate().over(det);
        hold2[0][1] = hold[0][1].negate().over(det);
        invmat = new ComplexMatrix(hold2);
      }
      else{
        Cmplx[] col = new Cmplx[n];
        Cmplx[] xvec = new Cmplx[n];
        Cmplx[][] invarray = invmat.getArrayReference();
        ComplexMatrix ludmat;

        ludmat = amat.luDecomp();
        for(int j=0; j<n; j++){
          for(int i=0; i<n; i++)col[i]= Cmplx.zero();
          col[j]= Cmplx.plusOne();
          xvec=ludmat.luBackSub(col);
          for(int i=0; i<n; i++)invarray[i][j]= Cmplx.copy(xvec[i]);
        }
      }
    }
    return invmat;
  }

  // TRANSPOSE
  // Transpose of a complex matrix [instance method]
  public ComplexMatrix transpose(){
    ComplexMatrix tmat = new ComplexMatrix(this.ncol, this.nrow);
    Cmplx[][] tarray = tmat.getArrayReference();
    for(int i=0; i<this.ncol; i++){
      for(int j=0; j<this.nrow; j++){
        tarray[i][j]= Cmplx.copy(this.matrix[j][i]);
      }
    }
    return tmat;
  }

  // Transpose of a complex matrix [static method]
  public static ComplexMatrix transpose(ComplexMatrix amat){
    ComplexMatrix tmat = new ComplexMatrix(amat.ncol, amat.nrow);
    Cmplx[][] tarray = tmat.getArrayReference();
    for(int i=0; i<amat.ncol; i++){
      for(int j=0; j<amat.nrow; j++){
        tarray[i][j]= Cmplx.copy(amat.matrix[j][i]);
      }
    }
    return tmat;
  }

  // COMPLEX CONJUGATE
  //Complex Conjugate of a complex matrix [instance method]
  public ComplexMatrix conjugate(){
    ComplexMatrix conj = ComplexMatrix.copy(this);
    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<this.ncol; j++){
        conj.matrix[i][j]=this.matrix[i][j].conjugate();
      }
    }
    return conj;
  }

  //Complex Conjugate of a complex matrix [static method]
  public static ComplexMatrix conjugate(ComplexMatrix amat){
    ComplexMatrix conj = ComplexMatrix.copy(amat);
    for(int i=0; i<amat.nrow; i++){
      for(int j=0; j<amat.ncol; j++){
        conj.matrix[i][j]=amat.matrix[i][j].conjugate();
      }
    }
    return conj;
  }

  // ADJOIN
  // Adjoin of a complex matrix [instance method]
  public ComplexMatrix adjoin(){
    ComplexMatrix adj = ComplexMatrix.copy(this);
    adj=adj.transpose();
    adj=adj.conjugate();
    return adj;
  }

  // Adjoin of a complex matrix [static method]
  public ComplexMatrix adjoin(ComplexMatrix amat){
    ComplexMatrix adj = ComplexMatrix.copy(amat);
    adj=adj.transpose();
    adj=adj.conjugate();
    return adj;
  }

  // OPPOSITE
  // Opposite of a complex matrix [instance method]
  public ComplexMatrix opposite(){
    ComplexMatrix opp = ComplexMatrix.copy(this);
    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<this.ncol; j++){
        opp.matrix[i][j]=this.matrix[i][j].times(Cmplx.minusOne());
      }
    }
    return opp;
  }

  // Opposite of a complex matrix [static method]
  public static ComplexMatrix opposite(ComplexMatrix amat){
    ComplexMatrix opp = ComplexMatrix.copy(amat);
    for(int i=0; i<amat.nrow; i++){
      for(int j=0; j<amat.ncol; j++){
        opp.matrix[i][j]=amat.matrix[i][j].times(Cmplx.minusOne());
      }
    }
    return opp;
  }

  // TRACE
  // Trace of a complex matrix [instance method]
  public Cmplx trace(){
    Cmplx trac = new Cmplx(0.0, 0.0);
    for(int i=0; i<Math.min(this.ncol,this.ncol); i++){
      trac.plusEquals(this.matrix[i][i]);
    }
    return trac;
  }

  // Trace of a complex matrix [static method]
  public static Cmplx trace(ComplexMatrix amat){
    Cmplx trac = new Cmplx(0.0, 0.0);
    for(int i=0; i<Math.min(amat.ncol,amat.ncol); i++){
      trac.plusEquals(amat.matrix[i][i]);
    }
    return trac;
  }

  // DETERMINANT
  //  Returns the determinant of a complex square matrix [instance method]
  public Cmplx determinant(){
    int n = this.nrow;
    if(n!=this.ncol)throw new IllegalArgumentException("Matrix is not square");
    Cmplx det = new Cmplx();
    ComplexMatrix ludmat;

    ludmat = this.luDecomp();
    det.reset(ludmat.dswap,0.0);
    for(int j=0; j<n; j++){
      det.timesEquals(ludmat.matrix[j][j]);
    }
    return det;
  }

  //  Returns the determinant of a complex square matrix [static method]
  public static Cmplx determinant(ComplexMatrix amat){
    int n = amat.nrow;
    if(n!=amat.ncol)throw new IllegalArgumentException("Matrix is not square");
    Cmplx det = new Cmplx();
    ComplexMatrix ludmat;

    ludmat = amat.luDecomp();
    det.reset(ludmat.dswap,0.0);
    for(int j=0; j<n; j++){
      det.timesEquals(ludmat.matrix[j][j]);
    }
    return det;
  }

  // Returns the log(determinant) of a complex square matrix [instance method].
  // Useful if determinant() underflows or overflows.
  public Cmplx logDeterminant(){
    int n = this.nrow;
    if(n!=this.ncol)throw new IllegalArgumentException("Matrix is not square");
    Cmplx det = new Cmplx();
    ComplexMatrix ludmat;

    ludmat = this.luDecomp();
    det.reset(ludmat.dswap,0.0);
    det= Cmplx.log(det);
    for(int j=0; j<n; j++){
      det.plusEquals(Cmplx.log(ludmat.matrix[j][j]));
    }
    return det;
  }

  // Returns the log(determinant) of a complex square matrix [static method].
  // Useful if determinant() underflows or overflows.
  public static Cmplx logDeterminant(ComplexMatrix amat){
    int n = amat.nrow;
    if(n!=amat.ncol)throw new IllegalArgumentException("Matrix is not square");
    Cmplx det = new Cmplx();
    ComplexMatrix ludmat;

    ludmat = amat.luDecomp();
    det.reset(ludmat.dswap,0.0);
    det= Cmplx.log(det);
    for(int j=0; j<n; j++){
      det.plusEquals(Cmplx.log(ludmat.matrix[j][j]));
    }
    return det;
  }

  // REDUCED ROW ECHELON FORM
  public ComplexMatrix reducedRowEchelonForm() {

    Cmplx[][] mat = Cmplx.twoDarray(this.nrow, this.ncol);
    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<this.ncol; j++){
        mat[i][j] = this.matrix[i][j];
      }
    }

    int leadingCoeff = 0;
    int rowPointer = 0;

    boolean testOuter = true;
    while(testOuter){
      int counter = rowPointer;
      boolean testInner = true;
      while(testInner && mat[counter][leadingCoeff].equals(Cmplx.zero())) {
        counter++;
        if(counter == this.nrow){
          counter = rowPointer;
          leadingCoeff++;
          if(leadingCoeff == this.ncol)testInner=false;
        }
      }
      if(testInner){
        Cmplx[] temp = mat[rowPointer];
        mat[rowPointer] = mat[counter];
        mat[counter] = temp;

        Cmplx pivot = mat[rowPointer][leadingCoeff];
        for(int j=0; j<this.ncol; j++)mat[rowPointer][j] =  mat[rowPointer][j].over(pivot);

        for(int i=0; i<this.nrow; i++){
          if (i!=rowPointer) {
            pivot = mat[i][leadingCoeff];
            for (int j=0; j<this.ncol; j++)mat[i][j] = mat[i][j].minus(pivot.times(mat[rowPointer][j]));
          }
        }
        leadingCoeff++;
        if(leadingCoeff>=this.ncol)testOuter = false;
      }
      rowPointer++;
      if(rowPointer>=this.nrow || !testInner)testOuter = false;
    }

    for(int i=0; i<this.nrow; i++){
      for(int j=0; j<this.ncol; j++){
        if(mat[i][j].getReal()==-0.0)mat[i][j].reset(0.0, mat[i][j].getImag());
        if(mat[i][j].getImag()==-0.0)mat[i][j].reset(mat[i][j].getReal(), 0.0);
      }
    }
    return new ComplexMatrix(mat);
  }


  // ONE NORM of a complex matrix
  public double oneNorm(){
    double norm=0.0D;
    double sum = 0.0D;
    for(int i=0; i<this.nrow; i++){
      sum=0.0D;
      for(int j=0; j<this.ncol; j++){
        sum+= Cmplx.abs(this.matrix[i][j]);
      }
      norm=Math.max(norm,sum);
    }
    return norm;
  }

  // INFINITY NORM of a complex matrix
  public double infinityNorm(){
    double norm=0.0D;
    double sum=0.0D;
    for(int i=0; i<this.nrow; i++){
      sum=0.0D;
      for(int j=0; j<this.ncol; j++){
        sum+= Cmplx.abs(this.matrix[i][j]);
      }
      norm=Math.max(norm,sum);
    }
    return norm;
  }


  // LU DECOMPOSITION OF COMPLEX MATRIX A
  // For details of LU decomposition
  // See Numerical Recipes, The Art of Scientific Computing
  // by W H Press, S A Teukolsky, W T Vetterling & B P Flannery
  // Cambridge University Press,   http://www.nr.com/
  // ComplexMatrix ludmat is the returned LU decompostion
  // int[] index is the vector of row permutations
  // dswap returns +1.0 for even number of row interchanges
  //       returns -1.0 for odd number of row interchanges
  public ComplexMatrix luDecomp(){
    if(this.nrow!=this.ncol)throw new IllegalArgumentException("A matrix is not square");
    int n=this.nrow;
    int imax=0;
    double dum=0.0D, temp=0.0D, big=0.0D;
    double[] vv = new double[n];
    Cmplx sum = new Cmplx();
    Cmplx dumm = new Cmplx();

    ComplexMatrix ludmat=ComplexMatrix.copy(this);
    Cmplx[][] ludarray = ludmat.getArrayReference();

    ludmat.dswap=1.0;
    for (int i=0;i<n;i++) {
      big=0.0;
      for (int j=0;j<n;j++){
        if ((temp= Cmplx.abs(ludarray[i][j])) > big) big=temp;
      }
      if (big == 0.0) throw new ArithmeticException("Singular matrix");
      vv[i]=1.0/big;
    }
    for (int j=0;j<n;j++) {
      for (int i=0;i<j;i++) {
        sum= Cmplx.copy(ludarray[i][j]);
        for (int k=0;k<i;k++) sum.minusEquals(ludarray[i][k].times(ludarray[k][j]));
        ludarray[i][j]= Cmplx.copy(sum);
      }
      big=0.0;
      for (int i=j;i<n;i++) {
        sum= Cmplx.copy(ludarray[i][j]);
        for (int k=0;k<j;k++){
          sum.minusEquals(ludarray[i][k].times(ludarray[k][j]));
        }
        ludarray[i][j]= Cmplx.copy(sum);
        if ((dum=vv[i]* Cmplx.abs(sum)) >= big) {
          big=dum;
          imax=i;
        }
      }
      if (j != imax) {
        for (int k=0;k<n;k++) {
          dumm= Cmplx.copy(ludarray[imax][k]);
          ludarray[imax][k]= Cmplx.copy(ludarray[j][k]);
          ludarray[j][k]= Cmplx.copy(dumm);
        }
        ludmat.dswap = -ludmat.dswap;
        vv[imax]=vv[j];
      }
      ludmat.index[j]=imax;

      if(ludarray[j][j].isZero()){
        ludarray[j][j].reset(TINY, TINY);
      }
      if(j != n-1) {
        dumm= Cmplx.over(1.0,ludarray[j][j]);
        for (int i=j+1;i<n;i++){
          ludarray[i][j].timesEquals(dumm);
        }
      }
    }
    return ludmat;
  }

  // Solves the set of n linear complex equations A.X=B using not A but its LU decomposition
  // Complex bvec is the vector B (input)
  // Complex xvec is the vector X (output)
  // index is the permutation vector produced by luDecomp()
  public Cmplx[] luBackSub(Cmplx[] bvec){
    int ii=0,ip=0;
    int n=bvec.length;
    if(n!=this.ncol)throw new IllegalArgumentException("vector length is not equal to matrix dimension");
    if(this.ncol!=this.nrow)throw new IllegalArgumentException("matrix is not square");
    Cmplx sum=new Cmplx();
    Cmplx[] xvec=new Cmplx[n];
    for(int i=0; i<n; i++){
      xvec[i]= Cmplx.copy(bvec[i]);
    }
    for (int i=0;i<n;i++) {
      ip=this.index[i];
      sum= Cmplx.copy(xvec[ip]);
      xvec[ip]= Cmplx.copy(xvec[i]);
      if (ii==0){
        for (int j=ii;j<=i-1;j++){
          sum.minusEquals(this.matrix[i][j].times(xvec[j]));
        }
      }
      else{
        if(sum.isZero()) ii=i;
      }
      xvec[i]= Cmplx.copy(sum);
    }
    for(int i=n-1;i>=0;i--) {
      sum= Cmplx.copy(xvec[i]);
      for (int j=i+1;j<n;j++){
        sum.minusEquals(this.matrix[i][j].times(xvec[j]));
      }
      xvec[i]= sum.over(this.matrix[i][i]);
    }
    return xvec;
  }

  // Solves the set of n linear complex equations A.X=B
  // Complex bvec is the vector B (input)
  // Complex xvec is the vector X (output)
  public Cmplx[] solveLinearSet(Cmplx[] bvec){
    ComplexMatrix ludmat;

    ludmat=this.luDecomp();
    return ludmat.luBackSub(bvec);
  }
}

