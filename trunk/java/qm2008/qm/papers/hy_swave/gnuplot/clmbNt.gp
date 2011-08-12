# http://www.gnuplot.info/
# dmitry.konovalov@jcu.edu.au 2010
reset
cd "C:\\dev\\physics\\papers\\output\\wf";

A = 'clmbNt.dat'
B = 'targetNt.dat'
idx =  24

set term postscript eps enhanced lw 2 size 16cm,16cm solid color 18 "fixed"; set out 'clmbNt.ps'

p [] [-2:2] A u 1:idx t 'clmbNt'   w l ls 1, \
                 B u 1:idx t 'targetNt' w l ls 2
