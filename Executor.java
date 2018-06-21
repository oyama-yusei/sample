package Temp;
import Temp.analysis.*;
import Temp.node.*;
import java.io.*;
import java.util.*;

class Executor extends DepthFirstAdapter {
  Scanner sc = new Scanner(System.in);
  PrintStream pr = System.out;
  HashMap<Node,Integer> pos;
  Object[] vars;
  public Executor(HashMap<Node,Integer> p, int s) { pos = p; vars = new Object[s]; }
  @Override
  public void inAWindowStat(AWindowStat node) {
    Window Sample = new Window();
  }
  @Override
  public void outAAdclStat(AAdclStat node) {
    vars[pos.get(node)] = new Object[Integer.parseInt(node.getIconst().getText())];
  }
  @Override
  public void outAAssignStat(AAssignStat node) {
    vars[pos.get(node)] = getOut(node.getExpr());
  }
  @Override
  public void outAAassignStat(AAassignStat node) {
    Object[] a = (Object[])vars[pos.get(node)];
    a[(Integer)getOut(node.getIdx())] = getOut(node.getExpr());
  }
  @Override
  public void outAReadStat(AReadStat node) {
    String s = node.getIdent().getText().intern();
    pr.print(s + "> "); vars[pos.get(node)] = sc.nextInt(); sc.nextLine();
  }
  @Override
  public void outAPrintStat(APrintStat node) {
    pr.println(getOut(node.getExpr()).toString());
  }
  @Override
  public void caseAIfStat(AIfStat node) {
    node.getExpr().apply(this);
    if((Integer)getOut(node.getExpr()) != 0) { node.getStat().apply(this); }
  }
  @Override
  public void caseAWhileStat(AWhileStat node) {
    while(true) {
      node.getExpr().apply(this);
      if((Integer)getOut(node.getExpr()) == 0) { return; }
      node.getStat().apply(this);
    }
  }
  @Override
  public void outAGtExpr(AGtExpr node) {
    if((Integer)getOut(node.getLeft()) > (Integer)getOut(node.getRight())) {
      setOut(node, new Integer(1));
    } else {
      setOut(node, new Integer(0));
    }
  }
  @Override
  public void outALtExpr(ALtExpr node) {
    if((Integer)getOut(node.getLeft()) < (Integer)getOut(node.getRight())) {
      setOut(node, new Integer(1));
    } else {
      setOut(node, new Integer(0));
    }
  }
  @Override
  public void outAOneExpr(AOneExpr node) { setOut(node, getOut(node.getNexp())); }
  @Override
  public void outAAddNexp(AAddNexp node) {
    int v = (Integer)getOut(node.getNexp()) + (Integer)getOut(node.getTerm());
    setOut(node, new Integer(v));
  }
  @Override
  public void outASubNexp(ASubNexp node) {
    int v = (Integer)getOut(node.getNexp()) - (Integer)getOut(node.getTerm());
    setOut(node, new Integer(v));
  }
  @Override
  public void outAOneNexp(AOneNexp node) { setOut(node, getOut(node.getTerm())); }
  @Override
  public void outAMulTerm(AMulTerm node) {
    int v = (Integer)getOut(node.getTerm()) * (Integer)getOut(node.getFact());
    setOut(node, new Integer(v));
  }
  @Override
  public void outADivTerm(ADivTerm node) {
    int v = (Integer)getOut(node.getTerm()) / (Integer)getOut(node.getFact());
    setOut(node, new Integer(v));
  }
  @Override
  public void outAOneTerm(AOneTerm node) { setOut(node, getOut(node.getFact())); }
  @Override
  public void outAIconstFact(AIconstFact node) {
    setOut(node, new Integer(node.getIconst().getText()));
  }
  @Override
  public void outAIdentFact(AIdentFact node) {
    setOut(node, vars[pos.get(node)]);
  }
  @Override
  public void outAArefFact(AArefFact node) {
    Object[] a = (Object[])vars[pos.get(node)];
    setOut(node, a[(Integer)getOut(node.getExpr())]);
  }
  @Override
  public void outAOneFact(AOneFact node) { setOut(node, getOut(node.getExpr())); }
}
