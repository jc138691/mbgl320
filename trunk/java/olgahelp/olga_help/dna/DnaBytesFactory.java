package dna;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 27/04/2009, 1:23:14 PM
 */
public class DnaBytesFactory implements DnaFinals {

//  public static SeqBytes makeReverse(SeqBytes from) {
//    SeqBytes res = new SeqBytes();
//    int size = from.size();
//    byte[] baseArr = new byte[size];
//    try {
//      for (int i = 0; i < size; i++) {
//        baseArr[i] = makeReverse(from.getBaseByte(i));
//      }
//    }
//    catch (Exception e) {
//      int dbg = 1;
//    }
//    res.setSeq(baseArr);
//    res.setQltyArr(from.getQltyArr());
//    res.setId(from.getId());
//    return res;
//  }

  public static byte makeReverse(byte base) {
    switch (base) {
      case BASE_A : return BASE_A_COMP;
      case BASE_a : return BASE_a_COMP;
      case BASE_C : return BASE_C_COMP;
      case BASE_c : return BASE_c_COMP;
      case BASE_G : return BASE_G_COMP;
      case BASE_g : return BASE_g_COMP;
      case BASE_T : return BASE_T_COMP;
      case BASE_t : return BASE_t_COMP;
      default : return base;
    }
  }
}
