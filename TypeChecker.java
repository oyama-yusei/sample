package Temp;
import Temp.analysis.*;
import Temp.node.*;
import java.io.*;
import java.util.*;

class TypeChecker extends DepthFirstAdapter {
  Symtab st;
  HashMap<Node,Integer> vtbl;
  public TypeChecker(Symtab tb, HashMap<Node,Integer> vt) { st = tb; vtbl = vt; }
  private void ckv(Node n, int t, int l, String s) {
    if(getOut(n) instanceof Integer && (Integer)getOut(n) == t) { return; }
    Log.pError(l+": "+s);
  }
  private int cki(String i, int t, int l) {
    Symtab.Ent e = st.lookup(i);
    if(e.type != t) { Log.pError(l+": identyfier is not of expexted type: "+i); }
    return e.pos;
  }
  @Override
  public void outAIdclStat(AIdclStat node) {
    st.addDef(node.getIdent().getText(), Symtab.ITYPE);
  }
  @Override
  public void outAAdclStat(AAdclStat node) {
    Symtab.Ent e = st.addDef(node.getIdent().getText(), Symtab.ATYPE);
    vtbl.put(node, e.pos);
  }
  @Override
  public void outAAssignStat(AAssignStat node) {
    int p = cki(node.getIdent().getText(), Symtab.ITYPE, node.getIdent().getLine());
    ckv(node.getExpr(), Symtab.ITYPE, node.getAssign().getLine(), "non-int val");
    vtbl.put(node, p);
  }
  @Override
  public void outAAassignStat(AAassignStat node) {
    int p = cki(node.getIdent().getText(), Symtab.ATYPE, node.getIdent().getLine());
    ckv(node.getIdx(), Symtab.ITYPE, node.getLsbr().getLine(), "non-int index");
    ckv(node.getExpr(), Symtab.ITYPE, node.getAssign().getLine(), "non-int val");
    vtbl.put(node, p);
  }
  @Override
  public void outAReadStat(AReadStat node) {
    int p = cki(node.getIdent().getText(), Symtab.ITYPE, node.getIdent().getLine());
    vtbl.put(node, p);
  }
  @Override
  public void outAPrintStat(APrintStat node) {
    ckv(node.getExpr(), Symtab.ITYPE, node.getPrint().getLine(), "non-int val");
  }
  @Override
  public void outAIfStat(AIfStat node) {
    ckv(node.getExpr(), Symtab.ITYPE, node.getLpar().getLine(), "non-int cond");
  }
  @Override
  public void outAWhileStat(AWhileStat node) {
    ckv(node.getExpr(), Symtab.ITYPE, node.getLpar().getLine(), "non-int cond");
  }
  @Override
  public void inABlockStat(ABlockStat node) { st.enterScope(); }
  @Override
  public void outABlockStat(ABlockStat node) { st.exitScope(); }
  @Override
  public void outAGtExpr(AGtExpr node) {
    ckv(node.getLeft(), Symtab.ITYPE, node.getGt().getLine(), "non-int val");
    ckv(node.getRight(), Symtab.ITYPE, node.getGt().getLine(), "non-int val");
    setOut(node, new Integer(Symtab.ITYPE));
  }
  @Override
  public void outALtExpr(ALtExpr node) {
    ckv(node.getLeft(), Symtab.ITYPE, node.getLt().getLine(), "non-int val");
    ckv(node.getRight(), Symtab.ITYPE, node.getLt().getLine(), "non-int val");
    setOut(node, new Integer(Symtab.ITYPE));
  }
  @Override
  public void outAOneExpr(AOneExpr node) { setOut(node, getOut(node.getNexp())); }
  @Override
  public void outAAddNexp(AAddNexp node) {
    ckv(node.getNexp(), Symtab.ITYPE, node.getAdd().getLine(), "non-int val");
    ckv(node.getTerm(), Symtab.ITYPE, node.getAdd().getLine(), "non-int val");
    setOut(node, new Integer(Symtab.ITYPE));
  }
  @Override
  public void outASubNexp(ASubNexp node) {
    ckv(node.getNexp(), Symtab.ITYPE, node.getSub().getLine(), "non-int val");
    ckv(node.getTerm(), Symtab.ITYPE, node.getSub().getLine(), "non-int val");
    setOut(node, new Integer(Symtab.ITYPE));
  }
  @Override
  public void outAOneNexp(AOneNexp node) { setOut(node, getOut(node.getTerm())); }
  @Override
  public void outAMulTerm(AMulTerm node) {
    ckv(node.getTerm(), Symtab.ITYPE, node.getAster().getLine(), "non-int val");
    ckv(node.getFact(), Symtab.ITYPE, node.getAster().getLine(), "non-int val");
    setOut(node, new Integer(Symtab.ITYPE));
  }
  @Override
  public void outADivTerm(ADivTerm node) {
    ckv(node.getTerm(), Symtab.ITYPE, node.getSlash().getLine(), "non-int val");
    ckv(node.getFact(), Symtab.ITYPE, node.getSlash().getLine(), "non-int val");
    setOut(node, new Integer(Symtab.ITYPE));
  }
  @Override
  public void outAOneTerm(AOneTerm node) { setOut(node, getOut(node.getFact())); }
  @Override
  public void outAIconstFact(AIconstFact node) {
    setOut(node, new Integer(Symtab.ITYPE));
  }
  @Override
  public void outAIdentFact(AIdentFact node) {
    Symtab.Ent e = st.lookup(node.getIdent().getText());
    setOut(node, new Integer(e.type)); vtbl.put(node, e.pos);
  }
  @Override
  public void outAArefFact(AArefFact node) {
    int p = cki(node.getIdent().getText(), Symtab.ATYPE, node.getIdent().getLine());
    ckv(node.getExpr(), Symtab.ITYPE, node.getLsbr().getLine(), "non-int index");
    setOut(node, new Integer(Symtab.ITYPE)); vtbl.put(node, p);
  }
  @Override
  public void outAOneFact(AOneFact node) { setOut(node, getOut(node.getExpr())); }
}
