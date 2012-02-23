reset # http://www.gnuplot.info/
# dmitry.konovalov@jcu.edu.au 2012

JM_1    = '../../output/PotScattJM/PotScattJM_TCS_L0_LMBD1.0_N14.dat'
JM_2    = '../../output/PotScattJM/PotScattJM_TCS_L0_LMBD1.0_N16.dat'

set term postscript eps enhanced lw 1 size 9cm,18cm solid color 18 "fixed"; set out 'fig_plot.ps'
set multiplot layout 2,1  scale 1.0, 1.1
set format y "10^{%+-T}"; set format x "%g"; 

# points
set style line 1 lt 7 lw 1   pt 7 ps 0.75 
set style line 2 lt 3 lw 2   pt 7 ps 0.5  
set xlabel ' '
set ylabel ' '  

# lines
set style line 11 lt 1 lc 1  lw 1   pt 1 ps 1.2 pi 2   # blue line
set style line 12 lt 7 lc -1  lw 1  pt 7 ps 0.5 

#set logscale x
set logscale y

p JM_1 u ($1):($2) t 'JM' w l ls 11, \
  JM_2 u ($1):($2) t 'JM2' w l ls 12 


#  JM_1 u ($1*scaleX):($2*scaleS1) t ' JM' w l ls 11

unset multiplot
pause -1 "Enter <return> to continue"

# !sed s/'50 50 305 560'/'50 40 305 565'/ fig1.ps > tmp;mv tmp fig1.ps



