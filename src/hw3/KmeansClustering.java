package hw3;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.util.*;
import java.util.List;

import static hw3.PrgMain.DrawGraph.createAndShowGui;

/* User: Mounir Abderrahmani
   Date: on 13.05.2016.


---------KmeansClustering--------
This class is the entry point for constructing Cluster Analysis objects.
Each instance of KmeansClustering object is associated with one or more clusters,
and a Vector of DataPoint objects.
@see DataPoint

**/

public class KmeansClustering {
    private Cluster[] clusters;
    private int miter;
    private Vector mDataPoints = new Vector();
    private double mSWCSS;

    public KmeansClustering(int k, int iter, Vector dataPoints) {  // k number of clusters
        clusters = new Cluster[k];
        for (int i = 0; i < k; i++) {
            clusters[i] = new Cluster("Cluster" + i);
        }
        this.miter = iter;
        this.mDataPoints = dataPoints;
    }

    private void calcSWCSS() {
        double temp = 0;
        for (int i = 0; i < clusters.length; i++) {
            temp = temp + clusters[i].getSumSqr();
        }
        mSWCSS = temp;
    }

    public void startAnalysis() {
        //set Starting centroid positions - Start of Step 1
        setInitialCentroids();
        int n = 0;
        //assign DataPoint to clusters
        loop1:
        while (true) {
            for (int l = 0; l < clusters.length; l++) {
                clusters[l].addDataPoint((DataPoint) mDataPoints.elementAt(n));
                n++;
                if (n >= mDataPoints.size())
                    break loop1;
            }
        }

        //calculate E for all the clusters
        calcSWCSS();

        //recalculate Cluster centroids - Start of Step 2
        for (int i = 0; i < clusters.length; i++) {
            clusters[i].getCentroid().calcCentroid();
        }

        //recalculate E for all the clusters
        calcSWCSS();

        for (int i = 0; i < miter; i++) {
            //enter the loop for cluster 1
            for (int j = 0; j < clusters.length; j++) {
                for (int k = 0; k < clusters[j].getNumDataPoints(); k++) {

                    //pick the first element of the first cluster
                    //get the current Euclidean distance
                    double tempEuDt = clusters[j].getDataPoint(k).getCurrentEuDt();
                    Cluster tempCluster = null;
                    boolean matchFoundFlag = false;

                    //call testEuclidean distance for all clusters
                    for (int l = 0; l < clusters.length; l++) {

                        //if testEuclidean < currentEuclidean then
                        if (tempEuDt > clusters[j].getDataPoint(k).testEuclideanDistance(clusters[l].getCentroid())) {
                            tempEuDt = clusters[j].getDataPoint(k).testEuclideanDistance(clusters[l].getCentroid());
                            tempCluster = clusters[l];
                            matchFoundFlag = true;
                        }
                        //if statement - Check whether the Last EuDt is > Present EuDt

                    }
//for variable 'l' - Looping between different Clusters for matching a Data Point.
//add DataPoint to the cluster and calcSWCSS

                    if (matchFoundFlag) {
                        tempCluster.addDataPoint(clusters[j].getDataPoint(k));
                        clusters[j].removeDataPoint(clusters[j].getDataPoint(k));
                        for (int m = 0; m < clusters.length; m++) {
                            clusters[m].getCentroid().calcCentroid();
                        }

//for variable 'm' - Recalculating centroids for all Clusters

                        calcSWCSS();
                    }

//if statement - A Data Point is eligible for transfer between Clusters.
                }
                //for variable 'k' - Looping through all Data Points of the current Cluster.
            }//for variable 'j' - Looping through all the Clusters.
        }//for variable 'i' - Number of iterations.
    }

    public Vector[] getClusterOutput() {
        Vector v[] = new Vector[clusters.length];
        for (int i = 0; i < clusters.length; i++) {
            v[i] = clusters[i].getDataPoints();
        }
        return v;
    }


    private void setInitialCentroids() {
        //kn = (round((max-min)/k)*n)+min where n is from 0 to (k-1).
        double cx = 0, cy = 0;
        for (int n = 1; n <= clusters.length; n++) {
            cx = (((getMaxXValue() - getMinXValue()) / (clusters.length + 1)) * n) + getMinXValue();
            cy = (((getMaxYValue() - getMinYValue()) / (clusters.length + 1)) * n) + getMinYValue();
            Centroid c1 = new Centroid(cx, cy);
            clusters[n - 1].setCentroid(c1);
            c1.setCluster(clusters[n - 1]);
        }
    }

    private double getMaxXValue() {
        double temp;
        temp = ((DataPoint) mDataPoints.elementAt(0)).getX();
        for (int i = 0; i < mDataPoints.size(); i++) {
            DataPoint dp = (DataPoint) mDataPoints.elementAt(i);
            temp = (dp.getX() > temp) ? dp.getX() : temp;
        }
        return temp;
    }

    private double getMinXValue() {
        double temp = 0;
        temp = ((DataPoint) mDataPoints.elementAt(0)).getX();
        for (int i = 0; i < mDataPoints.size(); i++) {
            DataPoint dp = (DataPoint) mDataPoints.elementAt(i);
            temp = (dp.getX() < temp) ? dp.getX() : temp;
        }
        return temp;
    }

    private double getMaxYValue() {
        double temp = 0;
        temp = ((DataPoint) mDataPoints.elementAt(0)).getY();
        for (int i = 0; i < mDataPoints.size(); i++) {
            DataPoint dp = (DataPoint) mDataPoints.elementAt(i);
            temp = (dp.getY() > temp) ? dp.getY() : temp;
        }
        return temp;
    }

    private double getMinYValue() {
        double temp = 0;
        temp = ((DataPoint) mDataPoints.elementAt(0)).getY();
        for (int i = 0; i < mDataPoints.size(); i++) {
            DataPoint dp = (DataPoint) mDataPoints.elementAt(i);
            temp = (dp.getY() < temp) ? dp.getY() : temp;
        }
        return temp;
    }

    public int getKValue() {
        return clusters.length;
    }

    public int getIterations() {
        return miter;
    }

    public int getTotalDataPoints() {
        return mDataPoints.size();
    }

    public double getSWCSS() {
        return mSWCSS;
    }

    public Cluster getCluster(int pos) {
        return clusters[pos];
    }
}

/*-----------------Cluster.java----------------*/


/**
 * This class represents a Cluster in a Cluster Analysis Instance. A Cluster is associated
 * with one and only one KmeansClustering Instance. A Cluster is related to more than one DataPoints and
 * one centroid.
 *
 * @author Shyam Sivaraman
 * @version 1.1
 * @see DataPoint
 * @see Centroid
 */


class Cluster {
    private String mName;
    private Centroid mCentroid;
    private double mSumSqr;
    private Vector mDataPoints;

    public Cluster(String name) {
        this.mName = name;
        this.mCentroid = null; //will be set by calling setCentroid()
        mDataPoints = new Vector();
    }

    public void setCentroid(Centroid c) {
        mCentroid = c;
    }

    public Centroid getCentroid() {
        return mCentroid;
    }

    public void addDataPoint(DataPoint dp) { //called from CAInstance
        dp.setCluster(this); //initiates a inner call to calcEuclideanDistance() in DP.
        this.mDataPoints.addElement(dp);
        calcSumOfSquares();
    }

    public void removeDataPoint(DataPoint dp) {
        this.mDataPoints.removeElement(dp);
        calcSumOfSquares();
    }

    public int getNumDataPoints() {
        return this.mDataPoints.size();
    }

    public DataPoint getDataPoint(int pos) {
        return (DataPoint) this.mDataPoints.elementAt(pos);
    }

    public void calcSumOfSquares() { //called from Centroid
        int size = this.mDataPoints.size();
        double temp = 0;
        for (int i = 0; i < size; i++) {
            temp = temp + ((DataPoint)
                    this.mDataPoints.elementAt(i)).getCurrentEuDt();
        }
        this.mSumSqr = temp;
    }

    public double getSumSqr() {
        return this.mSumSqr;
    }

    public String getName() { // no need for this one
        return this.mName;
    }

    public Vector getDataPoints() {
        return this.mDataPoints;
    }

}

/*---------------Centroid.java-----------------*/


/**
 * This class represents the Centroid for a Cluster. The initial centroid is calculated
 * using a equation which divides the sample space for each dimension into equal parts
 * depending upon the value of k.
 *
 * @author Mounir Abderrahmani
 * @version 1.0
 * @see Cluster
 */

class Centroid {
    private double mCx, mCy;
    private Cluster mCluster;

    public Centroid(double cx, double cy) {
        this.mCx = cx;
        this.mCy = cy;
    }

    public void calcCentroid() { //only called by CAInstance
        int numDP = mCluster.getNumDataPoints();
        double tempX = 0, tempY = 0;
        int i;
        //caluclating the new Centroid
        for (i = 0; i < numDP; i++) {
            tempX = tempX + mCluster.getDataPoint(i).getX();
            //total for x
            tempY = tempY + mCluster.getDataPoint(i).getY();
            //total for y
        }
        this.mCx = tempX / numDP;
        this.mCy = tempY / numDP;
        //calculating the new Euclidean Distance for each Data Point
        tempX = 0;
        tempY = 0;
        for (i = 0; i < numDP; i++) {
            mCluster.getDataPoint(i).calcEuclideanDistance();
        }
        //calculate the new Sum of Squares for the Cluster
        mCluster.calcSumOfSquares();
    }

    public void setCluster(Cluster c) {
        this.mCluster = c;
    }

    public double getCx() {
        return mCx;
    }

    public double getCy() {
        return mCy;
    }

    public Cluster getCluster() {
        return mCluster;
    }

}

/*----------------DataPoint.java----------------*/


/**
 * This class represents a candidate for Cluster analysis. A candidate must have
 * a name and two independent variables on the basis of which it is to be clustered.
 * A Data Point must have two variables and a name. A Vector of  Data Point object
 * is fed into the constructor of the KmeansClustering class. KmeansClustering and DataPoint are the only
 * classes which may be available from other packages.
 *
 * @author Mounir Abderrahmani
 * @version 1.0
 * @see KmeansClustering
 * @see Cluster
 */

class DataPoint {
    private double mX, mY;
    private String mObjName;
    private Cluster mCluster;
    private double mEuDt;

    public DataPoint(double x, double y, String name) {
        this.mX = x;
        this.mY = y;
        this.mObjName = name;
        this.mCluster = null;
    }

    public DataPoint(double x, double y) {
        this.mX = x;
        this.mY = y;

        this.mCluster = null;
    }

    public void setCluster(Cluster cluster) {
        this.mCluster = cluster;
        calcEuclideanDistance();
    }

    public void calcEuclideanDistance() {

        //called when DP is added to a cluster or when a Centroid is recalculated.
        mEuDt = Math.sqrt(Math.pow((mX - mCluster.getCentroid().getCx()), 2) + Math.pow((mY - mCluster.getCentroid().getCy()), 2));
    }

    public double testEuclideanDistance(Centroid c) {
        return Math.sqrt(Math.pow((mX - c.getCx()), 2) + Math.pow((mY - c.getCy()), 2));
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }

    public Cluster getCluster() {
        return mCluster;
    }

    public double getCurrentEuDt() {
        return mEuDt;
    }

    public String getObjName() {
        return mObjName;
    }

}


class PrgMain {
    static Scanner in = new Scanner(System.in);
    static Vector dataPoints = new Vector();

    public static void main(String args[]) {

        System.out.println("To input a data points from a file type '1' ,to generate Random Gaussian points type '0':");

        int choice = in.nextInt();
        while (choice != 0 && choice != 1) {
            System.out.println("please type '1' or '0':");
            choice = in.nextInt();
        }
        if (choice == 1)
            file();
        run(choice);
        System.out.println("type Plot to plot : ");
        String plot = in.next();
        if (plot.toLowerCase() == "plot") {
            plot();
        }
    }

    private static void plot() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }

    private static void run(int choice) {

        System.out.println("Enter the Number Of Clusters :");
        int k = in.nextInt();
        if (choice == 0)
            gaussian(k);
        KmeansClustering jca = new KmeansClustering(k, 1000, dataPoints);
        jca.startAnalysis();
        Vector[] v = jca.getClusterOutput();
        int line = 1;
        for (int i = 0; i < v.length; i++) {
            Vector tempV = v[i];
            System.out.println("-----------Cluster" + i + "---------");
            Iterator iter = tempV.iterator();
            while (iter.hasNext()) {
                DataPoint dpTemp = (DataPoint) iter.next();
                System.out.println(line++ + " [" + dpTemp.getX() + "," + dpTemp.getY() + "]");
            }
        }
    }

    private static void gaussian(int k) {
        double mean = 0;
        double variance = 0;

        for (int i = 1; i <= k; i++) {
            System.out.println("Enter the Mean of the Gaussian " + i + " :");
            mean = in.nextDouble();
            System.out.println("Enter the Variance of the Gaussian " + i + " :");
            variance = in.nextDouble();
        }
        System.out.println("Enter number of points n to be generated  :");
        int n = in.nextInt();
        for (int j = 0; j < k; j++) {
            RandomGaussian gaussian = new RandomGaussian();
            for (int s = 0; s < n; s++) {
                dataPoints.add(new DataPoint(gaussian.getGaussian(mean, variance), gaussian.getGaussian(mean, variance)));
            }
        }
    }

    private static void file() {
        System.out.println("Enter the path of the file :");
        String FilePath = in.next();
        ReadFile(FilePath);
    }

    public static void ReadFile(String FilePath) {

        String line;
        try {
            FileReader file1 = new FileReader(FilePath);
            Scanner file = new Scanner(file1);
            int i = 1;
            while (file.hasNextLine()) {
                i++;
                line = file.nextLine();
                try {
                    int space = line.indexOf(" ");
                    String x = line.substring(0, space).trim();
                    String y = line.substring(space, line.length()).trim();
                    dataPoints.add(new DataPoint(Double.parseDouble(x), Double.parseDouble(y)));
                } catch
                        (Exception e) {
                    System.out.println("The Format of the file should be the same as input.txt space between x and y fix the input at line " + i);
                }

            }
            file.close();
        } catch (Exception e) {
            System.out.println("File does not exist!" + e);
        }
    }

    static final class RandomGaussian {

        double MEAN = 100.0f;
        double VARIANCE = 5.0f;

        public RandomGaussian() {
        }

        public RandomGaussian(double mean, double variance) {
            this.MEAN = mean;
            this.VARIANCE = variance;
        }


        private Random fRandom = new Random();

        private double getGaussian(double aMean, double aVariance) {
            return aMean + fRandom.nextGaussian() * aVariance;
        }

        private void log(Object aMsg) {
            System.out.println(String.valueOf(aMsg));
        }
    }


    public static class DrawGraph extends JPanel {
        private static final int MAX_SCORE = 20;
        private static final int PREF_W = 800;
        private static final int PREF_H = 650;
        private static final int BORDER_GAP = 30;
        private static final int GRAPH_POINT_WIDTH = 12;
        private static final int Y_HATCH_CNT = 10;
        private List<Integer> scores;

        public DrawGraph(Vector dataPoint) {
            this.scores = new ArrayList<>(dataPoint);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double xScale = ((double) getWidth() - 2 * BORDER_GAP) / (scores.size() - 1);
            double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);

            List<Point> graphPoints = new ArrayList<Point>();
            for (int i = 0; i < scores.size(); i++) {
                int x1 = (int) (i * xScale + BORDER_GAP);
                int y1 = (int) ((MAX_SCORE - scores.get(i)) * yScale + BORDER_GAP);
                graphPoints.add(new Point(x1, y1));
            }

            // create x and y axes
            g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
            g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

            // create hatch marks for y axis.
            for (int i = 0; i < Y_HATCH_CNT; i++) {
                int x0 = BORDER_GAP;
                int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
                int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
                int y1 = y0;
                g2.drawLine(x0, y0, x1, y1);
            }

            // and for x axis
            for (int i = 0; i < scores.size() - 1; i++) {
                int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (scores.size() - 1) + BORDER_GAP;
                int x1 = x0;
                int y0 = getHeight() - BORDER_GAP;
                int y1 = y0 - GRAPH_POINT_WIDTH;
                g2.drawLine(x0, y0, x1, y1);
            }

            Stroke oldStroke = g2.getStroke();
            for (int i = 0; i < graphPoints.size() - 1; i++) {
                int x1 = graphPoints.get(i).x;
                int y1 = graphPoints.get(i).y;
                int x2 = graphPoints.get(i + 1).x;
                int y2 = graphPoints.get(i + 1).y;
                g2.drawLine(x1, y1, x2, y2);
            }

            g2.setStroke(oldStroke);
            for (int i = 0; i < graphPoints.size(); i++) {
                int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
                int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;
                ;
                int ovalW = GRAPH_POINT_WIDTH;
                int ovalH = GRAPH_POINT_WIDTH;
                g2.fillOval(x, y, ovalW, ovalH);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(PREF_W, PREF_H);
        }

        public static void createAndShowGui() {

            DrawGraph mainPanel = new DrawGraph(dataPoints);

            JFrame frame = new JFrame("DrawGraph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(mainPanel);
            frame.pack();
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
        }


    }
}
