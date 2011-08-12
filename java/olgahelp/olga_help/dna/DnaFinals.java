package dna;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 06/05/2009, 5:05:26 PM
 */
public interface DnaFinals {
  public final static byte BASE_PAD = '-';
  public final static byte BASE_PAD_2 = '*';

  public final static byte BASE_A = 'A';
  public final static byte BASE_A_COMP = 'T';

  public final static byte BASE_a = 'a';
  public final static byte BASE_a_COMP = 't';

  public final static byte BASE_C = 'C';
  public final static byte BASE_C_COMP = 'G';

  public final static byte BASE_c = 'c';
  public final static byte BASE_c_COMP = 'g';

  public final static byte BASE_G = 'G';
  public final static byte BASE_G_COMP = 'C';

  public final static byte BASE_g = 'g';
  public final static byte BASE_g_COMP = 'c';

  public final static byte BASE_T = 'T';
  public final static byte BASE_T_COMP = 'A';

  public final static byte BASE_t = 't';
  public final static byte BASE_t_COMP = 'a';


  public final static int CODE_A = 1;   // 0001
  public final static int CODE_C = 2;   // 0010
  public final static int CODE_G = 4;   // 0100
  public final static int CODE_T = 8;   // 1000

  public final static char IUPAC_AC = 'M';
  public final static int CODE_AC = CODE_A | CODE_C;
  public final static char IUPAC_AG = 'R';
  public final static int CODE_AG = CODE_A | CODE_G;
  public final static char IUPAC_AT = 'W';
  public final static int CODE_AT = CODE_A | CODE_T;

  public final static char IUPAC_CG = 'S';
  public final static int CODE_CG = CODE_C | CODE_G;
  public final static char IUPAC_CT = 'Y';
  public final static int CODE_CT = CODE_C | CODE_T;

  public final static char IUPAC_GT = 'K';
  public final static int CODE_GT = CODE_G | CODE_T;

  public final static char IUPAC_AGT = 'D';
  public final static int CODE_AGT = CODE_A | CODE_G | CODE_T;
  public final static char IUPAC_ACT = 'H';
  public final static int CODE_ACT = CODE_A | CODE_C | CODE_T;
  public final static char IUPAC_ACG = 'V';
  public final static int CODE_ACG = CODE_A | CODE_C | CODE_G;
  public final static char IUPAC_CGT = 'B';
  public final static int CODE_CGT = CODE_C | CODE_G | CODE_T;

  public final static char IUPAC_ACGT = 'N';
  public final static int CODE_ACGT = CODE_A | CODE_C | CODE_G | CODE_T;
}
