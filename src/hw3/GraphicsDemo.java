package hw3;

import java.awt.*;
import javax.swing.*;

public class GraphicsDemo extends JFrame
{
  
  public GraphicsDemo()
  {
    JSplitPane sp = new JSplitPane();
    // not middle, just hasty setting
    sp.setDividerLocation(200);
    add(sp);
    
    sp.setLeftComponent(new CirclePanel());
    sp.setRightComponent(new JPanel());
    
    setSize(400,400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("DEMO");
    setVisible(true);
  }
  
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        new GraphicsDemo();
      }
    });
  }
  
  // inner class for demo, best not to use inner
  // for your purpose
  private class CirclePanel extends JPanel
  {
    public CirclePanel()
    {
      setBackground(Color.BLACK);
    }
    
    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      // smoother look
      Graphics2D g2 = (Graphics2D) g;
      g2.setColor(Color.RED);
      g2.fillOval(10,10,40,40);
    }
  }
  
}

