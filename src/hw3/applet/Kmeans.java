package hw3.applet;

import java.applet.Applet;
import java.util.*;
import java.awt.*;

/**
 * This Applet is pretty cool it runs K-means Clustering
 * and it also visualize the Algorithm
 *
 * from
 * @author Mounir Abderrahmani
 *The Documentation Translated froom German
 *credit to Original Owner Jens Seher
 */

public class Kmeans extends Applet implements Runnable {
   Vector  CrossList; /** Contains all feature vectors */
   Vector  Centroids; /** Does the focus of the Cluster*/
   Choice  SubsetChoice; /** control */
   Button StartButton,RestartButton,ResetButton,RunButton,DrawGButton; /** Buttons*/
   Checkbox history; /** Checkbox */
   
   Thread Go;  /** Thread for the Run mode. */
   int step;   /** Current step of the algorithm is */
   int subset; /** Number of clusters */
   Random rand; /** variate*/
   boolean abort; /**termination criterion */
   
   /** Create the Graphical User Interface (GUI). */
   public void init() {
      rand = new Random();
      Centroids = new Vector();
    
      StartButton = new Button("Start");
      add(StartButton);
      StartButton.setEnabled(false);
      
      RestartButton = new Button("New Start");
      add(RestartButton);
      RestartButton.setEnabled(false);
      
      ResetButton = new Button("Reset");
      add(ResetButton);
      ResetButton.setEnabled(false);
      
      RunButton = new Button("Run");
      add(RunButton);
      RunButton.setEnabled(false);
      
      DrawGButton = new Button("Draw Cluster");
      add(DrawGButton);
      
      CrossList = new Vector();
      
      SubsetChoice = new Choice();
      SubsetChoice.addItem("2");
      SubsetChoice.addItem("3");
      SubsetChoice.addItem("4");
      SubsetChoice.addItem("5");
      SubsetChoice.addItem("6");
      SubsetChoice.addItem("7");
      SubsetChoice.addItem("8");
      add(SubsetChoice);
      
      history = new Checkbox("Show History");
      add(history);
      
      subset = 2;
      step = -1;
   }
   
   
   /** Draws the text of the feature vectors and the cluster priorities.*/
   public void paint(Graphics g) 
   {
        g.setColor(Color.BLACK);
        g.drawRect(0, 50, 499, 300); 
      
        StringBuffer buffer;
        if (step == 1)    g.setColor(Color.red);
        else g.setColor(Color.black);
       buffer = new StringBuffer("Step 1: Place ranomly initial group centroids into the 2d space.");
	g.drawString(buffer.toString(),2, 370);
        
         if (step == 2)     g.setColor(Color.red);
        else g.setColor(Color.black);
	buffer = new StringBuffer("Step 2: Assign each object to the group that has the closest centroid.");	
	g.drawString(buffer.toString(),2, 385);
        
         if (step == 3)     g.setColor(Color.red);
        else g.setColor(Color.black);
	buffer = new StringBuffer("Step 3: Recalculate the positions of the centroids. ");	
	g.drawString(buffer.toString(),2, 400);
        
        if (step == 4)     g.setColor(Color.red);
        else g.setColor(Color.black);
	buffer = new StringBuffer("Step 4: If the positions of the centroids didn't change go to the next step, else go to Step 2.");	
	g.drawString(buffer.toString(),2, 415);
                
        if (step == 5)     g.setColor(Color.red);
        else g.setColor(Color.black);
	buffer = new StringBuffer("Step 5: End.");	
	g.drawString(buffer.toString(),2, 430);
        
        // Draws the feature vectors
        Cross s;
        int numShapes = CrossList.size();
        for (int i = 0; i < numShapes; i++) 
        {
            s = (Cross) CrossList.elementAt(i); 
            s.draw(g);  
        }
        // Draws the cluster priorities
        if (step != -1) 
        {
            Quad t = new Quad();
            int numCent = Centroids.size();
            for (int i = 0; i < numCent; i++) 
            {
                t = (Quad) Centroids.elementAt(i);
                t.hist = history.getState();
          
                t.draw(g);  
            }
        }
    
   }
   
   /** Creates a new feature vector by a mouse*/
   public boolean mouseUp(Event e, int x, int y) {
   
      if ((step == -1) && (allowedMousePosition(x,y)== true))
      {
          ResetButton.setEnabled(true);
          StartButton.setEnabled(true);
          RunButton.setEnabled(true);
          
          Cross s = new Cross();  
   
          s.color = Color.black;
          s.x = x;
          s.y = y;
          CrossList.addElement(s);

          repaint();
      }
      
      return true;
   }
   
   /**
    Checking if the current mouse position is allowed. */
   public boolean allowedMousePosition(int x, int y)
   {
       if ((x>=5)&&(y>=55)&&(x<595)&&(y<345)) return true;
       else return false;
   }
   
   /**
    Automatic passing through the k-means method. In a step
    lingers for about 100ms. */
   public void run() {
	while (true) {
                if      (step ==-1) this.step1();
                else if (step == 1) this.step2();
                else if (step == 2) this.step3();
                else if (step == 3) step = 4;
                else if ((step == 4) && (abort==true))
                {
                    RestartButton.setEnabled(true);
                    ResetButton.setEnabled(true);
                    step = 5;
                    repaint();
                    Go.stop();
                }
                else if ((step == 4) && (abort==false))    this.step2();   
                repaint();
		try {			// Thread requires the exception handler (try-catch clause)
		Thread.sleep(100);
		}
		catch (InterruptedException e) {
		}
	}
}
   
   /** Managing the Button Events */
   public boolean action(Event event, Object eventobject)
   {				
        if ((event.target==StartButton))
        {
            StartButton.setLabel("Step");
            RestartButton.setEnabled(true);
            if      (step ==-1) this.step1();
            else if (step == 1) this.step2();
            else if (step == 2) this.step3();
            else if (step == 3) step = 4;
            else if ((step == 4) && (abort==true))
            {
                StartButton.setEnabled(false);
                RunButton.setEnabled(false);
                step = 5;
            }
            else if ((step == 4) && (abort==false))    this.step2();   
            repaint();
            return true; 
        } 
         if ((event.target==RunButton))
        {
         
            Go = new Thread(this);
            Go.start();
            StartButton.setEnabled(false);
            RestartButton.setEnabled(false);
            ResetButton.setEnabled(false);
            RunButton.setEnabled(false);
         
            return true; 
        } 
        if ((event.target==DrawGButton))
        {
            if (CrossList.size()>0)  Reset();
           
            String SubsetString = SubsetChoice.getSelectedItem();
            if (SubsetString.equals("2")) subset = 2;
            if (SubsetString.equals("3")) subset = 3;
            if (SubsetString.equals("4")) subset = 4;
            if (SubsetString.equals("5")) subset = 5;
            if (SubsetString.equals("6")) subset = 6;
            if (SubsetString.equals("7")) subset = 7;
            if (SubsetString.equals("8")) subset = 8;
              
            // Create Gaussians
            Vector  GaussianList;
            GaussianList = new Vector();
            for (int i = 0; i<subset;i++)
            {
                Gaussian gaus = new Gaussian();  
                // Initialize expectation
                gaus.mux = 50 + Math.abs(rand.nextInt() % 450);
                gaus.muy = 75 + Math.abs(rand.nextInt() % 275);
                //  Initialize standard deviation
                gaus.sigma = 10 + Math.abs(30 * rand.nextDouble());
             
                GaussianList.addElement(gaus);
            }
            ResetButton.setEnabled(true);
            StartButton.setEnabled(true);
            RunButton.setEnabled(true);
      
            // Create the feature vectors
            for (int i = 0; i<subset;i++)
            {
                //Create the feature vectors
                Gaussian gaus;
                gaus = (Gaussian) GaussianList.elementAt(i);
                // Create the feature vectors for the selected cluster
                for (int j = 0;j<2800/subset;j++)
                {
                    //To increase performance no "real" Gaussian curve is used here.
                    double r = 5*gaus.sigma*Math.pow(rand.nextDouble(),2);
                    double alpha = 2*Math.PI*rand.nextDouble();
                    int x = gaus.mux + (int) Math.round(r*Math.cos(alpha));
                    int y = gaus.muy + (int) Math.round(r*Math.sin(alpha));
                    //Checking whether position is allowed ...
                    if (allowedMousePosition(x,y)==true)
                    {
                        // Add the feature vector of the Cross List.
                        Cross s = new Cross();  
                        s.color = Color.black;
                        s.x = x;
                        s.y = y;
                        CrossList.addElement(s);
                    }
                  }
            }

            repaint();
            return true; 
        } 
      
         if ((event.target==RestartButton)  && (step !=-1))
        {
            step = -1;
            abort = false;
            Centroids.removeAllElements();
            int numShapes = CrossList.size();
            Cross s;
            for (int i = 0; i < numShapes; i++) 
            {
                   s = (Cross) CrossList.elementAt(i); 
                   s.color = Color.black;
            }
            StartButton.setLabel("Start");
            StartButton.setEnabled(true);
            ResetButton.setEnabled(true);
            RunButton.setEnabled(true);
         
            this.repaint(); 
            return true; 
        } 
         if ((event.target==ResetButton))
        {
           Reset();
           return true; 
        } 
        return true;			
   }
  /** Resetting the applet by deleting all feature vectors and cluster priorities. */
  public void Reset()
  {
    step = -1;
    abort = false;
    Centroids.removeAllElements();
    int numShapes = CrossList.size();
    Cross s;
    for (int i = 0; i < numShapes; i++) 
    {
           s = (Cross) CrossList.elementAt(i); 
           s.color = Color.white;
    }
    StartButton.setLabel("Start");
    StartButton.setEnabled(false);
    RestartButton.setEnabled(false);
    ResetButton.setEnabled(false);
    RunButton.setEnabled(false);
    CrossList.removeAllElements();

    this.repaint();  
  }
  
 /** Distributed random cluster priorities in 2d feature space. */
   public void step1()
   {
       abort = false;
       String SubsetString = SubsetChoice.getSelectedItem();
       if (SubsetString.equals("2")) subset = 2;
       if (SubsetString.equals("3")) subset = 3;
       if (SubsetString.equals("4")) subset = 4;
       if (SubsetString.equals("5")) subset = 5;
       if (SubsetString.equals("6")) subset = 6;
       if (SubsetString.equals("7")) subset = 7;
       if (SubsetString.equals("8")) subset = 8;
       int numShapes = CrossList.size();
       boolean ch[] = new boolean[numShapes];
       for (int i = 0; i<numShapes;i++) ch[i]=false;
       for (int i = 0; i<subset;)
       {
           Cross s;
           Quad p = new Quad();
           int r = Math.abs(rand.nextInt() % numShapes);
           if (ch[r]==false)
           {
               s = (Cross) CrossList.elementAt(r); 
               p.x = s.x;
               p.y = s.y;
               if (i == 0) p.color = Color.green;
               else if (i == 1) p.color = Color.red;
               else if (i == 2) p.color = Color.blue;
               else if (i == 3) p.color = Color.yellow;
               else if (i == 4) p.color = Color.orange;
               else if (i == 5) p.color = Color.magenta;
               else if (i == 6) p.color = Color.cyan;
               else if (i == 7) p.color = Color.lightGray;
               else if (i == 8) p.color = Color.darkGray;
               p.History = new Vector();
               
               Centroids.addElement(p);
               ch[r] = true;
               i++;
           }
        }
       step = 1;  
   }
   
   /**
    Assignment of each feature vector to the next cluster centroid */
   public void step2()
   {
        Cross s;
        Quad p;
        int numShapes = CrossList.size();
        for (int i = 0; i < numShapes; i++) 
        {
            s = (Cross) CrossList.elementAt(i); 
            
            int numCent = Centroids.size();
            int min = 0;
            double dist_min = 99999999.9;
            for (int j = 0; j < numCent; j++) 
            {
                p = (Quad) Centroids.elementAt(j);
                
                double dist = Point.distance(s.x, s.y, p.x, p.y);
                if (dist < dist_min) 
                {
                    dist_min = dist;
                    min = j;
                }
            }
            p = (Quad) Centroids.elementAt(min);
            s.color = p.color;
        }
        step = 2;
   }
   
   /** Recalculation of cluster priorities. */
   public void step3()
   {
        Quad p;
        Cross s;
        Point m = new Point();
        double changes = 0.0;
        int numCent = Centroids.size();
        for (int j = 0; j < numCent; j++) 
        {
           p = (Quad) Centroids.elementAt(j);
           m.x = 0;
           m.y = 0;
           int Count = 0;
           int numShapes = CrossList.size();
           for (int i = 0; i < numShapes; i++) 
           {
               s = (Cross) CrossList.elementAt(i); 
               if (s.color == p.color) 
               {
                   m.x += s.x;
                   m.y += s.y;
                   Count++;
               }
           }
           if (Count>0)
           {
               changes += Point.distance(p.x,p.y,m.x/Count, m.y/Count);
               Point pt = new Point();
               pt.x = p.x;
               pt.y = p.y;
               p.History.addElement(pt);
               p.x = m.x / Count;
               p.y = m.y / Count;
           }
        }   
       if (changes<0.1)  abort = true;
       step = 3;       
   }
   
}

/**Contains information of cluster priorities, such as position and color, but
 a Draw method to draw the focus.
 In addition, the priorities of each loop are stored to
 the way the cluster priorities from iteration to iteration to travel,
 be able to represent.
 */
class Quad {
   static public final int shapeRadius = 12;
   Color color;
   Vector  History;
   int x;
   int y;
   boolean hist;
   
   void draw(Graphics g) {
      if ((hist==true) && (History.size()>0))
      {
          Point p1,p2;
          g.setColor(Color.black);
         for (int i = 0;i<History.size();i++)
          {
              p1 = (Point) History.elementAt(i);
              if (i+1!=History.size())  p2 = (Point) History.elementAt(i+1);
              else
              {
                  p2 = new Point();
                  p2.x = this.x;
                  p2.y = this.y;
              }
              g.drawLine(p1.x+6,p1.y+6,p2.x+6,p2.y+6);
          }
      }
      g.setColor(this.color);
      g.fillOval(this.x, this.y, shapeRadius, shapeRadius);
      g.setColor(Color.black);
      g.drawOval(this.x, this.y, shapeRadius, shapeRadius);
    
    }
}

/** Contains information of the feature vectors, and a method for drawing
 the vectors. */
class Cross {
   static public final int shapeRadius = 2;
   Color color;
   int x;
   int y;
   void draw(Graphics g) {
      g.setColor(this.color);
      g.drawLine(this.x - shapeRadius, this.y, this.x + shapeRadius, this.y);
      g.drawLine(this.x, this.y - shapeRadius, this.x, this.y + shapeRadius);
    }
}

/**Represents a Gauss curve. */
class Gaussian {
   int mux;
   int muy;
   double sigma;
   double function(int x, int y)
   {
       double ret = Math.exp(-0.5*((this.mux-x)*(this.mux-x)+(this.muy-y)*(this.muy-y))/(this.sigma*this.sigma));
       return ret/(Math.sqrt(2*Math.PI)*this.sigma);
   }
  
}