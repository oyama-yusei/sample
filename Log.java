package Temp;

public class Log {
  public static int err = 0;
  public static void pError(String s) { System.out.println(s); ++err; }
  public static int getError() { return err; }
}
