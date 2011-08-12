package scatt.jm_1995;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 27/08/2008, Time: 15:29:26
 */
public class par_jm {
    //c     These parameters must be adjusted individually
    //c
    //c$$$      parameter (j_par = 701 )
    public static final int j_par = 1300;    //  parameter (j_par = 1300 )
    public static final int i_par = j_par; //  parameter (i_par = j_par)
    public static final int igm_par = 250; //  parameter (igm_par = 250 )
    public static final int nwf_par = 116; //  parameter (nwf_par = 116)
    public static final int n_par = 21; //  parameter (n_par = 21)
    public static final int nr_par = 701; //  parameter (nr_par = 701)
    public static final int Lmax_par = 13, Lt_par = 1, k_par = 4; //  parameter (Lmax_par = 13, Lt_par = 1, k_par = 4)
    public static final int ne_par = 241, ndiff_par = 2, nu_par = 4; //  parameter (ne_par = 241, ndiff_par = 2, nu_par = 4)
//c$$$      parameter (ne_par = 351, ndiff_par = 2, nu_par = 4)
    public static final int Nshells_par = 3, LSmax_par = 3; //  parameter (Nshells_par = 3, LSmax_par = 3)
//c
//c$$$      parameter (nyk2_par = (nwf_par * (nwf_par + 1)) / 2 )
    public static final int nchl_par = 10, nth_par = 91, ig_par = nwf_par; //  parameter (nchl_par = 10, nth_par = 91, ig_par = nwf_par)
    public static final int nfl_par = n_par + 2 * Lmax_par + 1; //  parameter (nfl_par = n_par + 2 * Lmax_par + 1)
}
/*
      integer Nshells_par, Lmax_par, Lt_par, LSmax_par, nchl_par, nth_par
      integer nwf_par, Nshells_par, n_par, k_par, nfl_par, ig_par,
     >   ne_par, ndiff_par, nr_par, igm_par, nu_par, j_par, i_par
c
c     These parameters must be adjusted individually
c
c$$$      parameter (j_par = 701 )
      parameter (j_par = 1300 )
      parameter (i_par = j_par)
      parameter (igm_par = 250 )
      parameter (nwf_par = 116)
      parameter (n_par = 21)
      parameter (nr_par = 701)
      parameter (Lmax_par = 13, Lt_par = 1, k_par = 4)
      parameter (ne_par = 241, ndiff_par = 2, nu_par = 4)
c$$$      parameter (ne_par = 351, ndiff_par = 2, nu_par = 4)
      parameter (Nshells_par = 3, LSmax_par = 3)
c
c$$$      parameter (nyk2_par = (nwf_par * (nwf_par + 1)) / 2 )
      parameter (nchl_par = 10, nth_par = 91, ig_par = nwf_par)
      parameter (nfl_par = n_par + 2 * Lmax_par + 1)
c
 */
