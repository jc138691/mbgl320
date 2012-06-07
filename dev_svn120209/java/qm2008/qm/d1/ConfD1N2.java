package d1;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 6/06/12, 12:09 PM
 */
// This is mainly for initial testing
public class ConfD1N2 extends ConfD1 {

// NOTE!!! ConfD1N2(int id) would be identical to ConfD1(int size)
public ConfD1N2(int id, boolean notUsed) {
  super(1);
  arrQ[0] = 2;
  arrId[0] = id;
}
public ConfD1N2(int id1, int id2) {
  super(2);
  arrQ[0] = 1;
  arrQ[1] = 1;
  arrId[0] = id1;
  arrId[1] = id2;
}
}
