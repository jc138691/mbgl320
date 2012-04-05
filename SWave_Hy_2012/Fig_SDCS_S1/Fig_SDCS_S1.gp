# dmitry.konovalov@jcu.edu.au 2010
reset
JS2002 = 'JS2002_TABLE_SDCS_Singlet.dat'

JM_Nt30 = 'hy_poet_SDCS_S1_L0_LMBD1.0_N31_Nt30.dat'
JM_Nt = '../../output/HySWaveBasisHySDCS/HySWaveBasisHySDCS_SDCS_S1_L0_LMBD1.0_N31_Nt30.dat'
JM_Nt40 = 'hy_poet_SDCS_S1_L0_LMBD1.0_N41_Nt40.dat'
JM_Nt50 = 'hy_poet_SDCS_S1_L0_LMBD1.0_N51_Nt50.dat'
JM_Nt60 = 'hy_poet_SDCS_S1_L0_LMBD1.0_N61_Nt60.dat'
JM_Nt70 = 'hy_poet_SDCS_S1_L0_LMBD1.0_N71_Nt70.dat'
JM_Nt80 = 'hy_poet_SDCS_S1_L0_LMBD1.0_N81_Nt80.dat'
JM_Nt90 = 'hy_poet_SDCS_S1_L0_LMBD1.0_N91_Nt90.dat'

scaleS = 1. / 4. / pi;                      # scale due to spin
scaleJS2002 = 2.;   s10 = 10;  s100 = 100;  #scales

set term postscript eps enhanced lw 2 size 8cm,18cm solid color 18 "fixed"; set out 'Fig_SDCS_S1.ps'
set multiplot layout 3,1  scale 1.0, 1.0
set format y "%2.1f"; set format x "%2.1f"; 
#set xlabel ' ' 0,-1; set ylabel ' '

# pointsl
set style line 1 lt 1 lc 1 lw 1  pt 2 ps 1  # x
set style line 2 lt 3 lw 1   pt 7 ps 0.5    
#set style line 2 lt 3 lc 4 lw 1  pt 4 ps 1.2     # empty square
set style line 3 lt 4 lc 7 lw 1  pt 8 ps 1.2     # empty triangle
set style line 4 lt 7 lc 7 lw 1  pt 9 ps 1.2 

# lines
set style line 11 lt 1 lc -1   lw 1.5  pt 7 ps 1 
set style line 12 lt 7 lc -1  lw 1  pt 7 ps 0.7 


# http://www.gnuplot.info/

# http://newsgroups.derkeiler.com/Archive/Comp/comp.graphics.apps.gnuplot/2005-12/msg00100.html
#set style line 1 linetype 3 pointtype 5 linewidth 3
#set style line 2 linetype -1 pointtype 5 linewidth 3
# First plot get blue and black lines, both thick and with pt 5 

# S1 ---------------------------------------
E0 = 1;  E = E0 - 0.5; scaleX = 1. / E;  maxE = 1; 
set ytics 0.2;
set key right Left title 'E_0=27.2eV'
set ylabel "cross section x 0.1" 
p [0:maxE] [0:1.3] JS2002 u 1:($3*scaleJS2002*s10) t ' JS2002' w l ls 11,\
                 JM_Nt30 u ($1*scaleX):($2*scaleS*s10) t ' N_t=30old' w p ls 1, \
                 JM_Nt u ($1*scaleX):($2*scaleS*s10) t ' N_t=30' w p ls 3, \
                 JM_Nt90 u ($1*scaleX):($2*scaleS*s10) t ' N_t=90' w lp ls 2


E0 = 1.5;  E = E0 - 0.5; scaleX = 1. / E;
set ytics 1;
set key right Left title 'E_0=40.8eV'
set ylabel "cross section x 0.01" 
p [0:maxE] [0:6] JS2002 u 1:($4*scaleJS2002*s100) t ' JS2002' w l ls 11,\
                 JM_Nt30 u ($1*scaleX):($3*scaleS*s100) t ' N_t=30' w p ls 1, \
                 JM_Nt90 u ($1*scaleX):($3*scaleS*s100) t ' N_t=90' w lp ls 2


E0 = 2.;  E = E0 - 0.5; scaleX = 1. / E;
set ytics 0.5; 
set key right Left title 'E_0=54.4eV'
set ylabel "cross section x 0.01" 
set xlabel "Energy Fraction (e_{/Symbol b}/E)" 
p [0:maxE] [0:3.5] JS2002 u 1:($5*scaleJS2002*s100) t ' JS2002' w l ls 11,\
                 JM_Nt30 u ($1*scaleX):($4*scaleS*s100) t ' N_t=30' w p ls 1, \
                 JM_Nt90 u ($1*scaleX):($4*scaleS*s100) t ' N_t=90' w lp ls 2


unset multiplot
pause -1




