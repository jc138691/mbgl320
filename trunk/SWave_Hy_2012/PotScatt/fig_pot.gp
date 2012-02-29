reset # http://www.gnuplot.info/
# dmitry.konovalov@jcu.edu.au 2012

JM_1    = '../../output/PotScattJM/PotScattJM_TCS_L0_LMBD1.0_N20.dat'
JM_2    = '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.0_N14.dat'
JM_3    = '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.0_N16.dat'
JM_4    = '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.0_N18.dat'

JM_2    = '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.0_N18.dat'
JM_3    = '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.1_N18.dat'
JM_4    = '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.2_N18.dat'

JM_2    = '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.0_N20.dat'
JM_3    = '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.01_N20.dat'
JM_4    = '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.15_N20.dat'
JM_5    = '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.2_N20.dat'
JM_6    = '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.25_N20.dat'

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
set style line 12 lt 7 lc -1  lw 1  pt 7 ps 0.5 

set logscale x
#set logscale y
set xrange [0.001:5.01]

#p JM_1 u ($1):($2) t 'JM' w l ls 11, \
#  JM_2 u ($1):($2) t 'JM2' w p ls 12, \
#  JM_3 u ($1):($2) t '1.1' w p ls 12, \
#  JM_4 u ($1):($2) t '1.15' w p ls 12, \
#  JM_5 u ($1):($2) t '1.2' w p ls 12, \
#  JM_6 u ($1):($2) t '1.25' w p ls 12  

p JM_1 u ($1):($2) t 'JM' w l ls 11, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.0_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.01_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.02_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.03_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.04_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.05_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.06_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.07_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.08_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.09_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.1_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.11_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.12_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.13_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.14_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.15_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.16_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.17_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.18_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.19_N20.dat' u ($1):($2) t '' w p ls 12, \
  '../../output/PotScattEA/PotScattEA_TCS_L0_LMBD1.2_N20.dat' u ($1):($2) t '' w p ls 12


#  JM_1 u ($1*scaleX):($2*scaleS1) t ' JM' w l ls 11

unset multiplot
pause -1 "Enter <return> to continue"

# !sed s/'50 50 305 560'/'50 40 305 565'/ fig1.ps > tmp;mv tmp fig1.ps



