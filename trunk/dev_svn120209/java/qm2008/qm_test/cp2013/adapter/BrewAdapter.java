package cp2013.adapter;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 21/09/12, 3:43 PM
 */
public class BrewAdapter implements IButtonAction {
UCBrewController worker;
public BrewAdapter(UCBrewController w) {
  worker = w;

}
public void doSomething() {
   worker.run();
}
}
