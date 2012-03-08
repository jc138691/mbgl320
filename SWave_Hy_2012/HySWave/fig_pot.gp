reset # http://www.gnuplot.info/
# dmitry.konovalov@jcu.edu.au 2012

JM_1    = '../../output/HySWaveBasisJm/HySWaveBasisJm_TCS_S1_L0_LMBD1.5_N21_Nt20.dat'


set term postscript eps enhanced lw 1 size 18cm,25cm solid color 18 "fixed"; set out 'fig_plot.ps'
set multiplot layout 2,1  scale 1.0, 1.1
#set format y "10^{%+-T}"; set format x "%g"; 
set format y "%g"; set format x "%g"; 

# points
set style line 1 lt 7 lw 1   pt 7 ps 0.75 
set style line 2 lt 3 lw 2   pt 7 ps 0.5  
set xlabel ' '
set ylabel ' '  

# lines
set style line 11 lt 1 lc 1  lw 1   pt 1 ps 1.2 pi 2   # blue line
set style line 12 lt 7 lc -1  lw 1  pt 7 ps 0.25 

#set logscale x
#set logscale y
set xrange [0.3:0.5]

p JM_1 u ($1):($2) t 'JM' w l ls 11, \
  '../../output/HySWaveEes/HySWaveEes_TCS_S1_L0_LMBD1.5_N20_Nt20.dat' u ($1):($2) t '' w p ls 12



unset multiplot
pause -1 "Enter <return> to continue"

# !sed s/'50 50 305 560'/'50 40 305 565'/ fig1.ps > tmp;mv tmp fig1.ps



