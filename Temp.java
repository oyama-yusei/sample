package Temp;
import Temp.parser.*;
import Temp.lexer.*;
import Temp.node.*;
import java.io.*;
import java.util.*;

public class Temp {
  public static void main(String[] args) throws Exception {
    Parser p = new Parser(new Lexer(new PushbackReader(
      new InputStreamReader(new FileInputStream(args[0]), "JISAutoDetect"),
        1024)));
    Start tree = p.parse();
    Symtab st = new Symtab();
    HashMap<Node,Integer> vtbl = new HashMap<Node,Integer>();
    TypeChecker tck = new TypeChecker(st, vtbl); tree.apply(tck); //st.show();
    if(Log.getError() > 0) { return; }
    Executor exec = new Executor(vtbl, st.getGsize()); tree.apply(exec);
  }
}
