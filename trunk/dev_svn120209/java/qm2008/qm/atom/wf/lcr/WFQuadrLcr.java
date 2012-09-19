package atom.wf.lcr;
import atom.wf.lcr.func.FuncLcr;
import atom.wf.lr.WFQuadrLr;
import math.vec.grid.StepGrid;
import math.vec.Vec;
import math.func.FuncVec;

import javax.utilx.log.Log;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 11:38:30
*/
public class WFQuadrLcr extends WFQuadrLr {
public static Log log = Log.getLog(WFQuadrLcr.class);
private WFQuadrLcr wCR;
private WFQuadrLcr wCR2;
private WFQuadrLcr wCR2DivR;
private WFQuadrLcr wCR2DivR2;
private final TransLcrToR lcrToR;

public WFQuadrLcr(StepGrid logCR) {
  super(logCR);
  lcrToR = new TransLcrToR(logCR);
  log.dbg("WFQuadrLcr()=", this);
}

@Override
public double calcWithDivR2(Vec wf, Vec wf2) {
  return getWithCR2DivR2().calc(wf, wf2);
}

@Override
public double calcWithDivR(Vec wf, Vec wf2, Vec wf3) {
  return getWithCR2DivR().calc(wf, wf2, wf3);
}

@Override
public double calcInt(Vec wf, Vec wf2) {
  return getWithCR2().calc(wf, wf2);
}
@Override
public double calcInt(Vec wf, Vec wf2, Vec wf3) {
  return getWithCR2().calc(wf, wf2, wf3);
}

public Vec getDivSqrtCR() {
  return lcrToR.getDivSqrtCR();
}

public WFQuadrLcr getWithCR2DivR() {
  if (wCR2DivR == null) {
    wCR2DivR = new WFQuadrLcr(getStepGrid());
    wCR2DivR.multSelf(new Vec(lcrToR.getCR2DivR()));
  }
  return wCR2DivR;
}
public WFQuadrLcr getWithCR2DivR2() {
  if (wCR2DivR2 == null) {
    wCR2DivR2 = new WFQuadrLcr(getStepGrid());
    wCR2DivR2.multSelf(new Vec(lcrToR.getCR2DivR2()));
  }
  return wCR2DivR2;
}

public TransLcrToR getLcrToR() {
  return lcrToR;
}

public WFQuadrLcr getWithCR2() {
  if (wCR2 == null) {
    wCR2 = new WFQuadrLcr(getStepGrid());    log.dbg("w=", wCR2);
    wCR2.multSelf(new Vec(lcrToR.getCR2()));     log.dbg("w*CR2=", wCR2);
  }
  return wCR2;
}
public WFQuadrLcr getWithCR() {
  if (wCR == null) {
    wCR = new WFQuadrLcr(getStepGrid());    log.dbg("w=", wCR);
    wCR.multSelf(new Vec(lcrToR.getCR()));      log.dbg("w*CR=", wCR);
  }
  return wCR;
}
@Override
public Vec getR() {
  return lcrToR;
}
public Vec getPowR(int k) {
  return lcrToR.getPowR(k);
}
public void transRToLCR(FuncVec f) {
  f.multSelf(getDivSqrtCR());
  f.setX(getX());
}

public FuncLcr getLcrToRFunc() {
  return lcrToR.getFunc();
}
}

