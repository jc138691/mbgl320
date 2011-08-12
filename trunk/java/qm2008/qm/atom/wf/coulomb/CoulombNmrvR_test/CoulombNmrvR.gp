# http://www.gnuplot.info/
# dmitry.konovalov@jcu.edu.au 2010
reset
cd "C:\\dev\\physics\\papers\\output\\wf";

A = 'CoulombNmrvR.dat'
B = 'CoulombRegFunc.dat'
C = 'CoulombRegAsymptFunc.dat'

set term postscript eps enhanced lw 2 size 16cm,16cm solid color 18 "fixed"; set out 'CoulombNmrvR.ps'
#set term postscript eps enhanced lw 2 size 8cm,13cm solid color 18 "fixed"; set out 'CoulombNmrvR.ps'
#set multiplot layout 2,1  scale 1.0, 1.0
#set format y "%2.1f"; set format x "%2.0f";

#p [90:100] [-1.1:1.1] A u 1:2 t 'CoulombNmrvR'   w l, \

p [0:10] [-1.1:1.1] A u 1:2 t 'CoulombNmrvR'   w l ls 1, \
                 B u 1:2 t 'CoulombRegFunc' w l ls 2, \
                 C u 1:2 t 'CoulombRegAsymptFunc' w l ls 5
