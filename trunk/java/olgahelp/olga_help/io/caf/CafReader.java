package io.caf;

import dna.seq.Seq;
import dna.contig.ContigArr;
import dna.contig.Contig;

import javax.swingx.textx.TextView;
import javax.swing.*;
import javax.utilx.log.Log;
import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

import io.fasta.FastaQltyReader;
import io.fasta.FastaReader;
import io.fasta.DnaFileReader;
import olga.SnpStation;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 27/03/2009, 3:55:48 PM
 *
 * See http://www.sanger.ac.uk/Software/formats/CAF/
 */
public class CafReader extends DnaFileReader<ContigArr> {
  public static Log log = Log.getLog(CafReader.class);
  private static final String COMMENTS_TAG = "//";
  private static final String SEQUENCE_TAG = "Sequence";
  private static final String DNA_TAG = "DNA";
  private static final String BASE_QLTY_TAG = "BaseQuality";
  private static final String NAME_TAG = " : ";
  private static final int NAME_TAG_LEN = 3;

  private static final int IS_TYPE = 0;
  private static final int IS_STATE = 1;
  private static final int IS_TEMPLATE = 2;
  private static final int IS_STRAND = 3;
  private static final int IS_SCF_FILE = 4;
  private static final int IS_SEQ_VEC = 5;
  private static final int IS_CLIPPING = 6;
  private static final int IS_TAG = 7;
  private static final int IS_ALIGN_TO_SCF = 8;
  private static final int IS_ASSEMBLED_FROM = 9;
  private static HashMap<String, Integer> actionMap = new HashMap<String, Integer>();
  private static final String IS_READ = "Is_read";
  private static final String IS_CONTIG = "Is_contig";
  private static final String IS_GROUP = "Is_group";
  private static final String IS_ASSEMBLY = "Is_assembly";
  private static HashMap<String, Caf.TYPE> typeMap = new HashMap<String, Caf.TYPE>();

  private static final String TEMPLATE = "Template";
  private static final String PADDED = "Padded";
  private static final String UNPADDED = "Unpadded";
  private static HashMap<String, Boolean> stateMap = new HashMap<String, Boolean>();

  private static final String STRAND = "Strand";
  private static final String FORWARD = "Forward";
  private static final String REVERSE = "Reverse";
  private static final String SCF_FILE = "SCF_File";
  private static final String SEQ_VEC = "Seq_vec";
  private static final String CLIPPING = "Clipping";
  private static final String TAG = "Tag";
  private static final String ALIGN_TO_SCF = "Align_to_SCF";
  private static final String ASSEMBLED_FROM = "Assembled_from";
  private static final char NAME_BRACKET = '\"';

  private static boolean foundOne;

  static {
    actionMap.put(IS_READ, IS_TYPE);
    actionMap.put(IS_CONTIG, IS_TYPE);
    actionMap.put(IS_GROUP, IS_TYPE);
    actionMap.put(IS_ASSEMBLY, IS_TYPE);
    actionMap.put(PADDED, IS_STATE);
    actionMap.put(UNPADDED, IS_STATE);
    actionMap.put(TEMPLATE, IS_TEMPLATE);
    actionMap.put(STRAND, IS_STRAND);
    actionMap.put(SCF_FILE, IS_SCF_FILE);
    actionMap.put(SEQ_VEC, IS_SEQ_VEC);
    actionMap.put(CLIPPING, IS_CLIPPING);
    actionMap.put(TAG, IS_TAG);
    actionMap.put(ALIGN_TO_SCF, IS_ALIGN_TO_SCF);
    actionMap.put(ASSEMBLED_FROM, IS_ASSEMBLED_FROM);

    typeMap.put(IS_READ, Caf.TYPE.IS_READ);
    typeMap.put(IS_CONTIG, Caf.TYPE.IS_CONTIG);
    typeMap.put(IS_GROUP, Caf.TYPE.IS_GROUP);
    typeMap.put(IS_ASSEMBLY, Caf.TYPE.IS_ASSEMBLY);

    stateMap.put(PADDED, true);
    stateMap.put(UNPADDED, false);
    stateMap.put(FORWARD, true);
    stateMap.put(REVERSE, false);
  }

  public ContigArr read(SnpStation project,  TextView view) {  //log.setDbg();
    return super.read("CAF", view, project.getCafOpt());
  }
  public ContigArr makeResType() {
    return new ContigArr();
  }

  // todo: Cleanup??? This is getting messy/complicated
  public boolean readOne(BufferedReader from, TextView view, ContigArr contigArr) throws IOException {
    if (from == null)
      return false;
    CafDna cafDna = new CafDna();

    foundOne = false;
    for (int countIdx = 0;  !foundOne; countIdx++) {
      String s = from.readLine();    log.inl().dbg("#", countIdx).dbg(s).eol();  //view.println(s); //log.info(s);
      if (s == null)  {   // end of file
        loadIfReady(cafDna, contigArr);
        break;
      }
      s = FastaReader.removeCppComments(s);
      if (s.length() == 0) { // ignore empty lines
        continue;
      }
      if (s.indexOf(SEQUENCE_TAG) == 0) {
        if (!loadSeq(s, from, cafDna))
          return false;
      }
      else if (s.indexOf(DNA_TAG) == 0) {
        if (!loadDNA(s, from, cafDna))
          return false;
      }
      else if (s.indexOf(BASE_QLTY_TAG) == 0) {
        if (!loadQlty(s, from, cafDna))
          return false;
      }
      else {
        String error = "Missing 'DNA', 'BaseQuality' or 'Sequence' in the first non-blank line of this CAF file:\n" + s;
        log.error(error);
        JOptionPane.showMessageDialog(view, error);
        return false;
      }
      cafDna = loadIfReady(cafDna, contigArr);
    }
    return true;
  }

  private static boolean loadQlty(String entryTag, BufferedReader from, Seq res)  throws IOException {
    if (!loadId(BASE_QLTY_TAG, entryTag, res))
      return false;
    return FastaQltyReader.loadQlty(from, res);
  }

  private static boolean loadDNA(String entryTag, BufferedReader from, Seq res) throws IOException {
    if (!loadId(DNA_TAG, entryTag, res))
      return false;
    return FastaReader.loadBases(from, res);
  }

  private static boolean loadId(String type, String entryTag, Seq res) {
    int tagIdx = entryTag.indexOf(NAME_TAG);
    if (tagIdx == -1) {
      String error = "CAF format error: Expecting \n"+type+" : 'Name' \nbut got \n"+entryTag ;
      log.error(error);
      JOptionPane.showMessageDialog(null, error);
      return false;
    }
    String name = entryTag.substring(tagIdx + NAME_TAG_LEN).trim();
    name = removeBrakets(name);
    String currId = res.getId();
    if (currId == null  || currId.length() == 0) {
      res.setId(name);
    }
    else if (!name.equals(currId)) {
      String error = "CAF format error: Expecting \n"+type+" : "+currId + "\nbut got \n"+entryTag ;
      log.error(error);
      JOptionPane.showMessageDialog(null, error);
      return false;
    }
    return true;
  }

  private static String removeBrakets(String name) {
    if (name.length() == 0)
      return name;
    char c = name.charAt(0);
    if (c == NAME_BRACKET) { // start
      name = name.substring(1);
    }
    if (name.length() == 0)
      return name;
    if (name.charAt(name.length()-1) == NAME_BRACKET) {  // finish
      name = name.substring(0, name.length()-1);
    }
    return name;
  }

  private static boolean loadSeq(String entryTag, BufferedReader from, CafDna res)  throws IOException {
    if (!loadId(SEQUENCE_TAG, entryTag, res))
      return false;
    CafSeq cafInfo = new CafSeq();
    for (int countIdx = 0;  ; countIdx++) {
      String s = from.readLine();    log.dbg("#", countIdx).dbg(s);
      if (s == null)  {   // end of file
        res.setCafSeq(cafInfo);
        return true;
      }
      s = FastaReader.removeCppComments(s);
      if (s.length() == 0) { // empty line marks the end
        res.setCafSeq(cafInfo);
        return true;
      }
      if (!loadSeqData(s, res))
        return false;
    }
  }

  private static boolean loadSeqData(String s, CafDna res) {
    StringTokenizer tokens = new StringTokenizer(s, " \t", false);
    String token = tokens.nextToken().trim();
    Integer action = actionMap.get(token);
    if (action == null)
      return true;    // ignore unknown
    switch (action) {
      case IS_TYPE: return loadType(s, res);
      case IS_STATE: return loadState(s, res);
      case IS_TEMPLATE: return loadTemplate(res, tokens);
      case IS_STRAND: return loadStrand(res, tokens);
      case IS_SCF_FILE: return loadScfFileName(res, tokens);
      case IS_SEQ_VEC: return loadSeqVec(res, tokens);
      case IS_CLIPPING: return loadClipp(res, tokens);
      case IS_TAG: return loadTag(res, tokens);
      case IS_ALIGN_TO_SCF: return loadAlign(res, tokens);
      case IS_ASSEMBLED_FROM: return loadAssembled(res, tokens);
      default: return false;
    }
  }

  private static boolean loadSeqVec(CafDna res, StringTokenizer tokens) {
    try {
      String type = tokens.nextToken(); //ignore
      String x1 = tokens.nextToken().trim();
      String x2 = tokens.nextToken().trim();
      String text = null;
      if (tokens.hasMoreTokens())  // "Text" is optional
        text = tokens.nextToken().trim();
      res.addVec(Integer.parseInt(x1), Integer.parseInt(x2), text);
      return true;
    }
    catch (NoSuchElementException e) {
      return false;
    }
    catch (NumberFormatException e) {
      String error = e.toString() + "\n while parsing tokens >" + tokens + "<." ;
      log.error(error);
      JOptionPane.showMessageDialog(null, error);
      return false;
    }
  }

  private static boolean loadClipp(CafDna res, StringTokenizer tokens) {
    try {
      String type = tokens.nextToken();
      String x1 = tokens.nextToken().trim();
      String x2 = tokens.nextToken().trim();
      String text = null;
      if (tokens.hasMoreTokens())  // "Text" is optional
        text = tokens.nextToken().trim();
      res.addClipp(type, Integer.parseInt(x1), Integer.parseInt(x2), text);
      return true;
    }
    catch (NoSuchElementException e) {
      return false;
    }
    catch (NumberFormatException e) {
      String error = e.toString() + "\n while parsing tokens >" + tokens + "<." ;
      log.error(error);
      JOptionPane.showMessageDialog(null, error);
      return false;
    }
  }

  private static boolean loadTag(CafDna res, StringTokenizer tokens) {
    try {
      String type = tokens.nextToken();
      String x1 = tokens.nextToken().trim();
      String x2 = tokens.nextToken().trim();
      String text = null;
      if (tokens.hasMoreTokens())  // "Text" is optional
        text = tokens.nextToken().trim();
      res.addTag(type, Integer.parseInt(x1), Integer.parseInt(x2), text);
      return true;
    }
    catch (NoSuchElementException e) {
      return false;
    }
    catch (NumberFormatException e) {
      String error = e.toString() + "\n while parsing tokens >" + tokens + "<." ;
      log.error(error);
      JOptionPane.showMessageDialog(null, error);
      return false;
    }
  }

  private static boolean loadAlign(CafDna res, StringTokenizer tokens) {
    try {
      String x1 = tokens.nextToken().trim();
      String x2 = tokens.nextToken().trim();
      String y1 = tokens.nextToken().trim();
      String y2 = tokens.nextToken().trim();
      res.addAlign(Integer.parseInt(x1), Integer.parseInt(x2), Integer.parseInt(y1), Integer.parseInt(y2));
      return true;
    }
    catch (NoSuchElementException e) {
      return false;
    }
    catch (NumberFormatException e) {
      String error = e.toString() + "\n while parsing tokens >" + tokens + "<." ;
      log.error(error);
      JOptionPane.showMessageDialog(null, error);
      return false;
    }
  }

  /**
   see http://www.sanger.ac.uk/Software/formats/CAF/
   Assembled_from "Read" s1 s2 r1 r2       // Contigs only: Alignment of Read to contig.
   // Interval [r1, r2] in the read align with [s1,s2] in contig.
   // If s1 > s2 then align the reverse complement of [r1,r2] with [s1,s2].
   */
  private static boolean loadAssembled(CafDna res, StringTokenizer tokens) {
    try {
      String from = tokens.nextToken().trim();
      String s = tokens.nextToken().trim();
      String s2 = tokens.nextToken().trim();
      String r = tokens.nextToken().trim();
      String r2 = tokens.nextToken().trim();
      res.addAssembled(from, Integer.parseInt(s), Integer.parseInt(s2), Integer.parseInt(r), Integer.parseInt(r2));
      return true;
    }
    catch (NoSuchElementException e) {
      return false;
    }
    catch (NumberFormatException e) {
      String error = e.toString() + "\n while parsing tokens >" + tokens + "<." ;
      log.error(error);
      JOptionPane.showMessageDialog(null, error);
      return false;
    }
  }
  private static boolean loadScfFileName(CafDna res, StringTokenizer tokens) {
    try {
      String name = tokens.nextToken().trim();
      name = removeBrakets(name);
      res.setScfFile(name);
      return true;
    }
    catch (NoSuchElementException e) {
      return false;
    }
  }

  private static boolean loadTemplate(CafDna res, StringTokenizer tokens) {
    try {
      String name = tokens.nextToken().trim();
      name = removeBrakets(name);
      String currId = res.getId();
      if (currId == null  || currId.length() == 0) {
        res.setId(name);
      }
      else if (!name.equals(currId)) {
        String error = "CAF format error (in loadTemplate): Expecting \n" + currId + "\nbut got \n" + name;
        log.error(error);
        JOptionPane.showMessageDialog(null, error);
        return false;
      }
      res.setId(name);
      return true;
    }
    catch (NoSuchElementException e) {
      return false;
    }
  }

  private static boolean loadType(String s, CafDna res) {
    Caf.TYPE type = typeMap.get(s);
    if (type == null)
      return false;
//    if (type != Caf.TYPE.IS_READ) {
//      log.setDbg();
//    }
    res.setType(type);
    return true;
  }
  private static boolean loadState(String s, CafDna res) {
    Boolean v = stateMap.get(s);
    if (v == null)
      return false;
    res.setPadded(v);
    return true;
  }

  private static boolean loadStrand(CafDna res, StringTokenizer tokens) {
    try {
      Boolean v = stateMap.get(tokens.nextToken().trim());
      if (v == null)
        return false;
      res.setForward(v);
      return true;
    }
    catch (NoSuchElementException e) {
      return false;
    }
  }

  private static CafDna loadIfReady(CafDna cafDna, ContigArr contigArr) {
    if (!cafDna.isReady())
      return cafDna;
    if (cafDna.getType() == Caf.TYPE.IS_READ) {
      Seq read = CafToDnaFactory.makeReadFromCaf(cafDna);
      contigArr.addRead(read);   //
    }
    if (cafDna.getType() == Caf.TYPE.IS_CONTIG) {
      foundOne = true;
      Contig contig = CafToDnaFactory.makeContigFromCaf(cafDna);
      contigArr.addContig(contig);
    }
    return new CafDna(); // make fresh
  }


}
