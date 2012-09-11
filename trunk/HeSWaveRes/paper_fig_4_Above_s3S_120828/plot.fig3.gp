reset; # dmitry.konovalov@jcu.edu.au 2012
xJM = 27.2116; yJM = 1.;   xCCC = 1.0;  yCCC = pi;

E_2S3 = 2.87902769 - 2.17426480; # from Phys. Rev. A 50, 3793–3808 (1994)
E_2S1 = 2.87902769 - 2.14418810; # from Phys. Rev. A 50, 3793–3808 (1994)
E_3S3 = 2.87902769 - 2.06849012; # from Phys. Rev. A 50, 3793–3808 (1994)
E_4S3 = 2.87902769 - 2.03643858; # Phys. Rev. A 50, 3793–3808 (1994)
E_4S1 = 2.87902769 - 2.03339181; # Phys. Rev. A 50, 3793–3808 (1994)
E_5S3 = 2.87902769 - 2.02258364; # Phys. Rev. A 50, 3793–3808 (1994)
E_5S1 = 2.87902769 - 2.02107923; # Phys. Rev. A 50, 3793–3808 (1994)
E_6S3 = 2.87902769 - 2.01535790; # Phys. Rev. A 50, 3793–3808 (1994)
E_6S1 = 2.87902769 - 2.01450740; # Phys. Rev. A 50, 3793–3808 (1994)
E_7S3 = 2.87902769 - 2.01111794; # Phys. Rev. A 50, 3793–3808 (1994)
E_7S1 = 2.87902769 - 2.01059097; # Phys. Rev. A 50, 3793–3808 (1994)

# -2.879028569120921, -2.1441972587315763, -2.060794037546864,
# -2.174264856287701, -2.0684901366080752, -2.0364341783218407,
E_grnd = 2.879028569120921; # from Nt50 lambda=4
E_2S3 = E_grnd - 2.174264856287701; # from Nt50 lambda=4
E_2S1 = E_grnd - 2.1441972587315763; # from Nt50 lambda=4
E_3S3 = E_grnd - 2.0684901366080752; # from Nt50 lambda=4
E_3S1 = E_grnd - 2.060794037546864; # from Nt50 lambda=4
E_ion = E_grnd - 2;
R_min = 0.70476-0.0003; R_max = 0.70476+0.0002;
E_min = E_3S1-0.002; E_max = E_ion;


JM_1    = '../../output/HeSWaveBasisHeIon/HeSWaveBasisHeIon_TCS_Nc7_L0_LMBD1.0_N100_Nt30.dat'
#-2.8789623026211353, -2.1441913928180427, -2.0607923564616435, -2.0333922030334937, -2.0210794228323463,
#-2.1742646178307643, -2.068490069685457, -2.036438559573639, -2.022583694613397, 
LABEL_1 = 'KFB';
SHIFT_1 = E_grnd -2.8789623026211353;   ;


JM_2    = '../../output/HeSWaveBasisHeIon/HeSWaveBasisHeIon_TCS_Nc1_L0_LMBD1.0_N100_Nt30.dat'
#-2.872506672905663, -2.143449321021511, -2.06057316103011, -2.033300705504141, -2.0210330065059448, 
#-2.1742455043163393, -2.0684846598208337, -2.0364363721136964, -2.022582608219962,
LABEL_2 = ' FC';
SHIFT_2 = E_grnd -2.872506672905663;   ;



CCC_1S   = '../results/ccc30_3_120817/s1S.s1S'; 
CCC_s2S  = '../results/ccc30_3_120817/s2S.s1S';  CCC_s3S  = '../results/ccc30_3_120817/s3S.s1S'
CCC_t2S  = '../results/ccc30_3_120817/t2S.s1S';  CCC_t3S  = '../results/ccc30_3_120817/t3S.s1S'
# Nc=3(LAMBDA=4), Nt=30(LAMBDA=1)
#-2.8784426986375284, -2.144149237806069, -2.0607804653782904
#-2.1742560849581736, -2.068487672081837,
CCC_SHIFT = E_grnd - 2.8784426986375284;



# lines/points
set style line 1 lt 1 lc -1  lw 1  pt 7  ps 0.6 
set style line 2 lt 7 lc 1   lw 1  pt 7  ps 0.6 
set style line 11 lt 1 lc 1   lw 2  pt 1 ps 1.2 pi 2   # lc 3 blue line; lc 1 red
set style line 12 lt 2 lc -1  lw 2  pt 7 ps 0.5 
set style line 13 lt 2 lc 3   lw 1  pt 7 ps 0.5 

set term postscript eps enhanced lw 1 size 8.6cm,18cm solid color 18 "fixed"; set out 'fig4.ps'
set termoption dashed;
set multiplot layout 4,1  scale 1.01, 1.01;
#set parametric;   
set format y "%3.0f"; 


R_min = R_min * xJM;
R_max = R_max * xJM;
E_min = E_min * xJM;
E_max = E_max * xJM;
E_2S3 = E_2S3 * xJM;
E_2S1 = E_2S1 * xJM;
E_3S3 = E_3S3 * xJM;
E_3S1 = E_3S1 * xJM;
E_4S3 = E_4S3 * xJM;
E_4S1 = E_4S1 * xJM;
E_5S3 = E_5S3 * xJM;
E_5S1 = E_5S1 * xJM;
E_6S3 = E_6S3 * xJM;
E_6S1 = E_6S1 * xJM;
E_7S3 = E_7S3 * xJM;
E_7S1 = E_7S1 * xJM;
SHIFT_1 = SHIFT_1 * xJM;
SHIFT_2 = SHIFT_2 * xJM;
CCC_SHIFT = CCC_SHIFT * xJM;



set arrow 1 nohead from E_3S3,graph 0 to E_3S3, graph 1 lt 0
set arrow 2 nohead from E_3S1,graph 0 to E_3S1, graph 1 lt 0
set arrow 3 nohead from E_4S3,graph 0 to E_4S3, graph 1 lt 0
set arrow 4 nohead from E_4S1,graph 0 to E_4S1, graph 1 lt 0
set arrow 5 nohead from E_5S3,graph 0 to E_5S3, graph 1 lt 0
set arrow 6 nohead from E_5S1,graph 0 to E_5S1, graph 1 lt 0
set arrow 7 nohead from E_6S3,graph 0 to E_6S3, graph 1 lt 0
set arrow 8 nohead from E_6S1,graph 0 to E_6S1, graph 1 lt 0
set arrow 9 nohead from E_7S3,graph 0 to E_7S3, graph 1 lt 0
set arrow 10 nohead from E_7S1,graph 0 to E_7S1, graph 1 lt 0


set xlabel ''; 
set ylabel ' '; 

set xrange [E_3S3:E_max];  set xtics 0.5;  set mxtics 5; set format x "%4.1f"; 
set yrange [4.5:5.5];          set ytics 0.5;  set mytics 5; set format y "%3.1f";   
set key left top Right title "(a) 2^3S [x0.01]"
p \
  CCC_t2S  u ($1*xCCC + CCC_SHIFT):($2*yCCC*100)    t 'CCC'   w p ls 1, \
  JM_2 u ($1*xJM + SHIFT_2):($3*yJM*100) t LABEL_2 w l ls 12, \
  JM_1 u ($1*xJM + SHIFT_1):($3*yJM*100) t LABEL_1 w l ls 11





set xlabel ''; 
set ylabel 'cross section (a.u.)' offset 0,-8; 

set yrange [1.2:1.47];           set ytics 0.1;  set mytics 10;  set format y "%3.1f";  
set key left top Right title "(b) 2^1S [x0.01]"
p \
  CCC_s2S  u ($1*xCCC + CCC_SHIFT):($2*yCCC*100)    t ''   w p ls 1, \
  JM_2 u ($1*xJM + SHIFT_2):($4*yJM*100) t '' w l ls 12,\
  JM_1 u ($1*xJM + SHIFT_1):($4*yJM*100) t '' w l ls 11



set xlabel ''; 
set ylabel ' ';

set yrange [3.0:7.2];           set ytics 1;  set mytics 5;  set format y "%3.0f";  
set key left top Right title "(c) 3^3S [x0.001]"
p \
  CCC_t3S  u ($1*xCCC + CCC_SHIFT):($2*yCCC*1000)    t ''   w p ls 1, \
  JM_2 u ($1*xJM + SHIFT_2):($5*yJM*1000) t '' w l ls 12,\
  JM_1 u ($1*xJM + SHIFT_1):($5*yJM*1000) t '' w l ls 11



set xlabel "incident energy (eV)"; 
set ylabel  ' ';  

set yrange [0:2];             set ytics 0.5;    set mytics 5;   set format y "%3.1f"; 
set key left top Right title "(d) 3^1S [x0.001]"
p \
  CCC_s3S  u ($1*xCCC + CCC_SHIFT):($2*yCCC*1000)    t ''   w p ls 1, \
  JM_2 u ($1*xJM + SHIFT_2):($6*yJM*1000) t '' w l ls 12, \
  JM_1 u ($1*xJM + SHIFT_1):($6*yJM*1000) t '' w l ls 11
 
 

unset multiplot
#pause -1 "Enter <return> to continue"
# !sed s/'50 50 305 560'/'50 40 305 565'/ fig1.ps > tmp;mv tmp fig1.ps



