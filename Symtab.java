package Temp;

import java.util.*;

public class Symtab {
  public static final int ITYPE = 1, ATYPE = 2, UNDEF = -1;
  List<Ent> tbl = new ArrayList<Ent>();
  Stack<Integer> stk = new Stack<Integer>();
  int mark;

  private boolean duplCheck(String n) {
    for(int i = mark; i < tbl.size(); ++i) {
      Ent e = tbl.get(i);
      if(e.alive && e.name.equals(n)) { return true; }
    }
    return false;
  }
  public Ent addDef(String n, int t) {
    if(duplCheck(n)) {
      Log.pError("dublicate: "+n); return new Ent(n, UNDEF, 0);
    }
    Ent e = new Ent(n, t, tbl.size()); tbl.add(e); return e;
  }
  public void enterScope() { stk.push(mark); mark = tbl.size(); }
  public void exitScope() {
    for(int i = mark; i < tbl.size(); ++i) { tbl.get(i).alive = false; }
    mark = stk.pop();
  }
  public Ent lookup(String n) {
    for(int i = tbl.size()-1; i >= 0; --i) {
      Ent e = tbl.get(i);
      if(e.alive && e.name.equals(n)) { return e; }
    }
    return new Ent(n, UNDEF, 0);
  }
  public int getGsize() { return tbl.size(); }
  public void show() { for(Ent e: tbl) { System.out.println(" " + e); } }
  public static class Ent {
    public String name;
    public boolean alive = true;
    public int type, pos = 0;
    public Ent(String n, int t, int p) { name = n; type = t; pos = p; }
    public String toString() {
      return String.format("%s%s[%d %d]", name, alive?"_":"!", type, pos);
    }
  }
}
