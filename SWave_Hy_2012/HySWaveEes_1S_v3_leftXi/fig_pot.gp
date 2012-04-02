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

p JM_1 u  1:2  t 'JM' w l ls 11, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.0_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.01_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.02_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.03_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.04_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.05_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.06_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.07_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.08_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.09_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.1_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.11_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.12_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.13_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.14_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.15_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.16_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.17_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.18_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.19_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.2_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.21_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.22_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.23_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.24_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.25_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.26_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.27_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.28_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.29_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.3_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.31_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.32_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.33_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.34_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.35_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.36_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.37_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.38_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.39_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.4_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.41_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.42_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.43_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.44_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.45_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.46_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.47_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.48_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.49_N21_Nt20.dat' u  1:2  t '' w p ls 12, \
  '../../output/HySWaveEesBasisV3/HySWaveEesBasisV3_TCS_S1_L0_LMBD1.5_N21_Nt20.dat' u  1:2  t '' w p ls 12

unset multiplot
pause -1 "Enter <return> to continue"

# !sed s/'50 50 305 560'/'50 40 305 565'/ fig1.ps > tmp;mv tmp fig1.ps



