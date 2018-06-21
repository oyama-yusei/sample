package Temp;
import javax.swing.JFrame;

class Window{
  public static void main(String args[]){
    JFrame frame = new JFrame("タイトル");
    frame.setBounds(200, 200, 200, 160);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
