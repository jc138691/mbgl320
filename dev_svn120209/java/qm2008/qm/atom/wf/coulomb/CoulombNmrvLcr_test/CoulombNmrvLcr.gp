# http://www.gnuplot.info/
# dmitry.konovalov@jcu.edu.au 2010
reset
cd "C:\\dev\\physics\\papers\\output\\wf";

A = 'CoulombNmrvLcr.dat'
B = 'CoulombRegFunc.dat'
C = 'CoulombRegAsymptFunc.dat'

set term postscript eps enhanced lw 2 size 16cm,16cm solid color 18 "fixed"; set out 'CoulombNmrvLcr.ps'

p [0:10] [-1.1:1.1] A u 1:2 t 'CoulombNmrvLcr'   w l ls 1, \
                 B u 1:2 t 'CoulombRegFunc' w l ls 2, \
                 C u 1:2 t 'CoulombRegAsymptFunc' w l ls 5
