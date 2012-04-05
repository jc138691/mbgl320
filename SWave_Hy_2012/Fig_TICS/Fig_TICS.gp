# http://www.gnuplot.info/
# dmitry.konovalov@jcu.edu.au 2010
reset
JS2002_S1 = 'JS2002_TABLE_TICS_Singlet.dat'
JS2002_S3 = 'JS2002_TABLE_TICS_Triplet.dat'

JM_Nt30_S1 = 'hy_poet_TICS_S1_L0_LMBD1.0_N31_Nt30.dat'
JM_Nt40_S1 = 'hy_poet_TICS_S1_L0_LMBD1.0_N41_Nt40.dat'
JM_Nt50_S1 = 'hy_poet_TICS_S1_L0_LMBD1.0_N51_Nt50.dat'
JM_Nt60_S1 = 'hy_poet_TICS_S1_L0_LMBD1.0_N61_Nt60.dat'
JM_Nt70_S1 = 'hy_poet_TICS_S1_L0_LMBD1.0_N71_Nt70.dat'
JM_Nt80_S1 = 'hy_poet_TICS_S1_L0_LMBD1.0_N81_Nt80.dat'
JM_Nt90_S1 = 'hy_poet_TICS_S1_L0_LMBD1.0_N91_Nt90.dat'

JM_Nt30_S3 = 'hy_poet_TICS_S3_L0_LMBD1.0_N31_Nt30.dat'
JM_Nt40_S3 = 'hy_poet_TICS_S3_L0_LMBD1.0_N41_Nt40.dat'
JM_Nt50_S3 = 'hy_poet_TICS_S3_L0_LMBD1.0_N51_Nt50.dat'
JM_Nt60_S3 = 'hy_poet_TICS_S3_L0_LMBD1.0_N61_Nt60.dat'
JM_Nt70_S3 = 'hy_poet_TICS_S3_L0_LMBD1.0_N71_Nt70.dat'
JM_Nt80_S3 = 'hy_poet_TICS_S3_L0_LMBD1.0_N81_Nt80.dat'
JM_Nt90_S3 = 'hy_poet_TICS_S3_L0_LMBD1.0_N91_Nt90.dat'

scaleS1 = 1. / 4. / pi;     scaleS3 = 3. / 4. / pi;                   # scale due to spin
s10 = 10;  s100 = 100;  s103 = 1000;  scaleX = 27.2; #scales

set term postscript eps enhanced lw 2 size 8cm,12cm solid color 18 "fixed"; set out 'Fig_TICS.ps'
set multiplot layout 2,1  scale 1.0, 1.0
set format y "%2.1f"; set format x "%2.0f"; 

# points
set style line 1 lt 7 lw 1   pt 7 ps 1.2 
set style line 2 lt 3 lw 2   pt 7 ps 0.5    

# lines
set style line 11 lt 1 lc 1  lw 1   pt 1 ps 1.2 pi 2   # blue line
set style line 12 lt 7 lc -1  lw 1  pt 7 ps 0.5 

minE = 0; maxE = 160;
set ytics 0.5;   set xtics 25;
set key right Left title 'Singlet'
set ylabel "cross section x 10^{-2}" 
p [minE:maxE] [0:2.5] JS2002_S1 u 1:($2*s100) t ' JS2002' w p ls 1, \
                  JM_Nt30_S1 u ($1*scaleX):($2*scaleS1*s100) t ' N_t=30' w l ls 11, \
                  JM_Nt90_S1 u ($1*scaleX):($2*scaleS1*s100) t ' N_t=90' w l ls 2

minE = 0; maxE = 160;
set ytics 0.5;   set xtics 25;
set key bottom right Left title 'Triplet'
set ylabel "cross section x 10^{-3}" 
set xlabel "Incident energy E_0 (eV)" 
p [minE:maxE] [0:3.5] JS2002_S3 u 1:($2*s103) t ' JS2002' w p ls 1, \
                  JM_Nt30_S3 u ($1*scaleX):($2*scaleS3*s103) t ' N_t=30' w l ls 11, \
                  JM_Nt90_S3 u ($1*scaleX):($2*scaleS3*s103) t ' N_t=90' w l ls 2



unset multiplot
pause -1




