reset; # dmitry.konovalov@jcu.edu.au 2012
xJM = 27.2116; yJM = 1.;   xCCC = 1.0;  yCCC = pi;

# -2.879028569120921, -2.1441972587315763, -2.060794037546864,
# -2.174264856287701, -2.0684901366080752, -2.0364341783218407,
E_grnd = 2.879028569120921; # from Nt50 lambda=4
E_2S3 = 2.87902769 - 2.17426480; # from Phys. Rev. A 50, 3793�3808 (1994)
E_2S1 = 2.87902769 - 2.14418810; # from Phys. Rev. A 50, 3793�3808 (1994)
E_3S3 = 2.87902769 - 2.06849012; # from Phys. Rev. A 50, 3793�3808 (1994)
E_4S3 = 2.87902769 - 2.03643858; # Phys. Rev. A 50, 3793�3808 (1994)
E_5S3 = 2.87902769 - 2.02258364; # Phys. Rev. A 50, 3793�3808 (1994)
E_2S3 = E_grnd - 2.174264856287701; # from Nt50 lambda=4
E_2S1 = E_grnd - 2.1441972587315763; # from Nt50 lambda=4
E_3S3 = E_grnd - 2.0684901366080752; # from Nt50 lambda=4
E_3S1 = E_grnd - 2.060794037546864; # from Nt50 lambda=4
E_ion = E_grnd - 2;
R_min = 0.70476-0.0003; R_max = 0.70476+0.0002;
E_min = E_ion; E_max=40;


JM_1    = '../../output/HeSWaveBasisHeIon/HeSWaveBasisHeIon_TCS_Nc1_L0_LMBD1.0_N101_Nt30.dat'
TICS_1  = '../../output/HeSWaveBasisHeIon/HeSWaveBasisHeIon_TICS_Nc1_L0_LMBD1.0_N101_Nt30.dat'
LABEL_1 = 'JM(FC)';
SHIFT_1 = 0;

JM_2    = '../../output/HeSWaveBasisHeIon/HeSWaveBasisHeIon_TCS_Nc3_L0_LMBD1.0_N101_Nt30.dat'
TICS_2  = '../../output/HeSWaveBasisHeIon/HeSWaveBasisHeIon_TICS_Nc3_L0_LMBD1.0_N101_Nt30.dat'
LABEL_2 = 'JM(MC)';
SHIFT_2 = 0;

JM_3    = '../../output/HeSWaveBasisHeIon/HeSWaveBasisHeIon_TCS_Nc3_L0_LMBD0.95_avr_1.05_N100_Nt30.dat'
TICS_3  = '../../output/HeSWaveBasisHeIon/HeSWaveBasisHeIon_TICS_Nc3_L0_LMBD0.95_avr_1.05_N100_Nt30.dat'
LABEL_3 = '{/Symbol l}';
SHIFT_3 = 0;


JM_4    = '../../output/HeSWaveBasisHeIon/HeSWaveBasisHeIon_TCS_Nc3_L0_LMBD1.0_N101_Nt25_avr_35.dat'
TICS_4  = '../../output/HeSWaveBasisHeIon/HeSWaveBasisHeIon_TICS_Nc3_L0_LMBD1.0_N101_Nt25_avr_35.dat'
LABEL_4 = 'N_t';
SHIFT_4 = 0;




CCC_1S   = '../results/ccc30_3_120817/s1S.s1S';  CCC_TICS = '../results/ccc30_3_120817/TICS.s1S'
CCC_s2S  = '../results/ccc30_3_120817/s2S.s1S';  CCC_s3S  = '../results/ccc30_3_120817/s3S.s1S'
CCC_t2S  = '../results/ccc30_3_120817/t2S.s1S';  CCC_t3S  = '../results/ccc30_3_120817/t3S.s1S'
# Nc=3(LAMBDA=4), Nt=30(LAMBDA=1)
#-2.8784426986375284, -2.144149237806069, -2.0607804653782904
#-2.1742560849581736, -2.068487672081837,



# lines/points
set style line 1 lt 1 lc -1  lw 1  pt 7  ps 0.75 
set style line 11 lt 3 lc -1   lw 2  pt 1 ps 1 pi 2   # lc 3 blue line; lc 1 red
set style line 12 lt 2 lc 1    lw 2  pt 7 ps 1 
set style line 13 lt 1 lc 3    lw 3  pt 7 ps 1 

set term postscript eps enhanced lw 1 size 18cm,8cm solid color 18 "fixed"; set out 'fig6.ps'
set termoption dashed;
set multiplot layout 2,2  scale 1.01, 1.01;
set parametric;   
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



set xlabel ''; set ylabel ' ';


E_min = 39; E_max = 61; 
set xrange [E_min:E_max];  set xtics 5;    set mxtics 5;   set format x "%3.0f"; 
set xlabel  '';  
set ylabel  'cross section (a.u.)' offset 0,-8; 

set yrange [0.5:2.5];            set ytics 0.5;  set mytics 5;   set format y "%3.1f"; 
set key right top Right title "(a) 2^3S [x0.01]"
p \
  JM_1 u ($1*xJM):($3*yJM*100) t '' w l ls 11, \
  JM_2 u ($1*xJM):($3*yJM*100) t '' w l ls 12, \
  JM_3 u ($1*xJM):($3*yJM*100) t '' w l ls 13, \
  CCC_t2S  u ($1*xCCC):($2*yCCC*100)    t ''   w p ls 1





set xlabel ''; set ylabel ' ';  

set xrange [E_min:E_max];  
 
set yrange [0.8:1.45];           set ytics 0.2;  set mytics 4;   set format y "%3.1f"; 
set key right top Right title "(b) 2^1S [x0.01]"
p \
  JM_1 u ($1*xJM):($4*yJM*100) t '' w l ls 11, \
  JM_2 u ($1*xJM):($4*yJM*100) t '' w l ls 12,\
  JM_3 u ($1*xJM):($4*yJM*100) t '' w l ls 13, \
  CCC_s2S  u ($1*xCCC):($2*yCCC*100)    t ''   w p ls 1
 

set xlabel 'incident energy (eV)';  set ylabel ' ';  

set yrange [2:6];            set ytics 1;  set mytics 5;   set format y "%3.0f"; 
set key right top Right title "(c) 3^3S [x0.001]"
p \
  JM_1 u ($1*xJM):($5*yJM*1000) t LABEL_1 w l ls 11, \
  JM_2 u ($1*xJM):($5*yJM*1000) t LABEL_2 w l ls 12, \
  JM_3 u ($1*xJM):($5*yJM*1000) t LABEL_3 w l ls 13, \
  CCC_t3S  u ($1*xCCC):($2*yCCC*1000)    t 'CCC'   w p ls 1





set xlabel 'incident energy (eV)';  set ylabel ' ';  

set xrange [E_min:E_max];  
 
set yrange [2:3.3];           set ytics 0.5;  set mytics 5;   set format y "%3.1f"; 
set key right top Right title "(d) 3^1S [x0.001]"
p \
  JM_1 u ($1*xJM):($6*yJM*1000) t '' w l ls 11, \
  JM_2 u ($1*xJM):($6*yJM*1000) t '' w l ls 12,\
  JM_3 u ($1*xJM):($6*yJM*1000) t '' w l ls 13, \
  CCC_s3S  u ($1*xCCC):($2*yCCC*1000)    t ''   w p ls 1
 

unset multiplot
#pause -1 "Enter <return> to continue"
# !sed s/'50 50 305 560'/'50 40 305 565'/ fig1.ps > tmp;mv tmp fig1.ps



