import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Mounir Abderrahmani on 23/04/16.
 */
public class TrainHMM {


    static boolean isLowerCase(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    static boolean isUpperCase(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    static ArrayList<String> Sequences = new ArrayList<>();


    static int Transitions = 0;

    static int ExonExon = 0;
    static int ExonIntron = 0;
    static int IntronIntron = 0;
    static int IntronExon = 0;

    static double ExonToExon = 0;
    static double ExonToIntron = 0;
    static double IntronToIntron = 0;
    static double IntronToExon = 0;
/*
    static int AtoA = 0;
    static int AtoT = 0;
    static int AtoC = 0;
    static int AtoG = 0;
    static int TtoA = 0;
    static int TtoT = 0;
    static int TtoC = 0;
    static int TtoG = 0;
    static int CtoA = 0;
    static int CtoT = 0;
    static int CtoC = 0;
    static int CtoG = 0;
    static int GtoA = 0;
    static int GtoT = 0;
    static int GtoC = 0;
    static int GtoG = 0;


    static int atoa = 0;
    static int atot = 0;
    static int atoc = 0;
    static int atog = 0;
    static int ttoa = 0;
    static int ttot = 0;
    static int ttoc = 0;
    static int ttog = 0;
    static int ctoa = 0;
    static int ctot = 0;
    static int ctoc = 0;
    static int ctog = 0;
    static int gtoa = 0;
    static int gtot = 0;
    static int gtoc = 0;
    static int gtog = 0;

    static int Atransitions = 0;
    static int Ttransitions = 0;
    static int Ctransitions = 0;
    static int Gtransitions = 0;

    static int atransitions = 0;
    static int ttransitions = 0;
    static int ctransitions = 0;
    static int gtransitions = 0;

*/

    private static String[] states = {"Exon", "Intron"};
    private static String[] observations = {"A", "T", "C", "G"};
    private static double[] start_probability = {1, 0};
    private static double[][] transition_probability = new double[2][2];
    private static double[][] emission_probability = new double[4][4];

    static double AProbability = 0;
    static double TProbability = 0;
    static double CProbability = 0;
    static double GProbability = 0;
    static double aProbability = 0;
    static double tProbability = 0;
    static double cProbability = 0;
    static double gProbability = 0;


    static double A = 0;
    static double T = 0;
    static double C = 0;
    static double G = 0;
    static double a = 0;
    static double t = 0;
    static double c = 0;
    static double g = 0;

    public static void main(String[] args) {
/*
        String line1 = "ATGATACTTgtccgagTATATAG";
        String line2 = "ATGTTTTgtggcagAAAGA";
        String line3 = "ATGAATgtcgcgagTTTTATAG";

        CalculateStateTransition(line1);
        CalculateStateTransition(line2);
        CalculateStateTransition(line3);

*/
        Scanner in = new Scanner(System.in);

        System.out.println("Hello first I need to train the real sequences:");

        System.out.println("Enter the Train file path to be read :");

        String FilePath = in.next();
        ReadFile(FilePath);

        int size = Sequences.size();
        for (int i = 0; i < size; i++)
            CalculateStateTransition(Sequences.get(i));

        ExonToExon = (double) ExonExon / Transitions;
        ExonToIntron = (double) ExonIntron / Transitions;
        IntronToIntron = (double) IntronIntron / Transitions;
        IntronToExon = (double) IntronExon / Transitions;


        transition_probability = new double[][]{{ExonToExon, ExonToIntron}, {IntronToExon, IntronToIntron}};
        System.out.println(" \n \n State Probabilities \n Exon To Intron : " + ExonToIntron + " \n Intron To Exon : " + IntronToExon);
        System.out.println("Exon To Exon : " + ExonToExon + " \n Intron To Intron : " + IntronToIntron);

        emission_probability = new double[][]{{A / size, T / size, C / size, G / size}, {a / size, t / size, c / size, g / size}};

        System.out.println("\nEmission Probability inside Exon State \n");
        System.out.println("\n A : " + A / size + "    T  " + T / size + "   C    :" + C / size + "    G   " + G / size);


        System.out.println("\nEmission Probability inside Intron State \n");
        System.out.println("\n a : " + a / size + "    t  " + t / size + "   c    :" + c / size + "    g   " + g / size);


        System.out.print("\nStates: ");
        for (int i = 0; i < states.length; i++) {
            System.out.print(states[i] + ", ");
        }
        System.out.print("\n\nObservations: ");
        for (int i = 0; i < observations.length; i++) {
            System.out.print(observations[i] + ", ");
        }
        System.out.print("\n\nStart probability: ");
        for (int i = 0; i < states.length; i++) {
            System.out.print(states[i] + ": " + start_probability[i] + ", ");
        }
        System.out.println("\n\nTransition probability:");
        for (int i = 0; i < states.length; i++) {
            System.out.print(" " + states[i] + ": {");
            for (int j = 0; j < states.length; j++) {
                System.out.print("  " + states[j] + ": " + transition_probability[i][j] + ", ");
            }
            System.out.println("}");
        }
        System.out.println("\n\nEmission probability:");
        for (int i = 0; i < states.length; i++) {
            System.out.print(" " + states[i] + ": {");
            for (int j = 0; j < observations.length; j++) {
                System.out.print("  " + observations[j] + ": " + emission_probability[i][j] + ", ");
            }
            System.out.println("}");
        }


        VeterbiTest();

        //      for (int i = 0; i < Sequences.size(); i++)
        //            CalculateEmissionProbabilities(Sequences.get(i));

        // Calculate State Transition Probabilities ...
/*
        ExonToExon = (double) ExonExon / Transitions;
        ExonToIntron = (double) ExonIntron / Transitions;
        IntronToIntron = (double) IntronIntron / Transitions;
        IntronToExon = (double) IntronExon / Transitions;


        System.out.println(" \n \n State Probabilities Of The first line : \n Exon To Intron : " + ExonToIntron + " \n Intron To Exon : " + IntronToExon);
        System.out.println("Exon To Exon : " + ExonToExon + " \n Intron To Intron : " + IntronToIntron);

        // print

        System.out.println(" \n \n State Probabilities: \n Exon To Intron : " + ExonToIntron + " \n Intron To Exon : " + IntronToExon);
        System.out.println("Exon To Exon : " + ExonToExon + " \n Intron To Intron : " + IntronToIntron);

        System.out.println(" \n\n Emission Probabilities :");

        double AA = ((double) AtoA / Atransitions);
        double AT = ((double) AtoT / Atransitions);
        double AC = ((double) AtoC / Atransitions);
        double AG = ((double) AtoC / Atransitions);

        double TA = -Math.log((double) TtoA / Ttransitions);
        double TT = -Math.log((double) TtoT / Ttransitions);
        double TC = -Math.log((double) TtoC / Ttransitions);
        double TG = -Math.log((double) TtoC / Ttransitions);

        double CA = -Math.log((double) CtoA / Ctransitions);
        double CT = -Math.log((double) CtoT / Ctransitions);
        double CC = -Math.log((double) CtoC / Ctransitions);
        double CG = -Math.log((double) CtoC / Ctransitions);

        double GA = -Math.log((double) GtoA / Gtransitions);
        double GT = -Math.log((double) GtoT / Gtransitions);
        double GC = -Math.log((double) GtoC / Gtransitions);
        double GG = -Math.log((double) GtoC / Gtransitions);

        double aa = -Math.log((double) atoa / atransitions);
        double at = -Math.log((double) atot / atransitions);
        double ac = -Math.log((double) atoc / atransitions);
        double ag = -Math.log((double) atog / atransitions);

        double ta = -Math.log((double) ttoa / ttransitions);
        double tt = -Math.log((double) ttot / ttransitions);
        double tc = -Math.log((double) ttoc / ttransitions);
        double tg = -Math.log((double) ttog / ttransitions);

        double ca = -Math.log((double) ctoa / ctransitions);
        double ct = -Math.log((double) ctot / ctransitions);
        double cc = -Math.log((double) ctoc / ctransitions);
        double cg = -Math.log((double) ctog / ctransitions);

        double ga = -Math.log((double) gtoa / gtransitions);
        double gt = -Math.log((double) gtot / gtransitions);
        double gc = -Math.log((double) gtoc / gtransitions);
        double gg = -Math.log((double) gtog / gtransitions);

        System.out.println("\nEmission Probability inside Exon State \n");
        System.out.println("\n From A To A : " + AA + "    From A To T  " + AT + "  From A To C    :" + AC + "    From A To G   " + AG);
        System.out.println("\n From T To A : " + TA + "    From T To T  " + TT + "  From T To C    :" + TC + "    From T To G   " + TG);
        System.out.println("\n From C To A : " + CA + "    From C To T  " + CT + "  From C To C    :" + CC + "    From C To G   " + CG);
        System.out.println("\n From G To A : " + GA + "    From G To T  " + GT + "  From G To C    :" + GC + "    From G To G   " + GG);


        System.out.println("\nEmission Probability inside Intron State \n");
        System.out.println("\n From a To a : " + aa + "    From a To t  " + at + "  From a To c    :" + ac + "    From a To g   " + ag);
        System.out.println("\n From t To a : " + ta + "    From t To t  " + tt + "  From t To c    :" + tc + "    From t To g   " + tg);
        System.out.println("\n From c To a : " + ca + "    From c To t  " + ct + "  From c To c    :" + cc + "    From c To g   " + cg);
        System.out.println("\n From g To a : " + ga + "    From g To t  " + gt + "  From g To c    :" + gc + "    From g To g   " + gg);
*/
    }

    private static void VeterbiTest() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the Test file path to be read :");

        String FilePath = in.next();
        ReadFile(FilePath);
        String[] new_observations;

                Veteerbi(observations, states, start_probability, transition_probability, emission_probability);

    }


    public static void ReadFile(String FilePath) {

        String row = "";
        try {
            File programFile = new File(FilePath);
            FileReader file1 = new FileReader(FilePath);
            Scanner file = new Scanner(file1);

            while (file.hasNextLine()) {
                row = file.nextLine();
                Sequences.add(row);
            }
            file.close();
        } catch (Exception e) {
            System.out.println("File does not exist!" + e);
        }
    }

    private static void CalculateStateTransition(String sequence) {
        boolean isLower = false;
        boolean isUpper = false;
        boolean donewithIntron = false;
        String EXON = "";
        String INTRON = "";

        for (int i = 0; i < sequence.length() - 1; i++) {

            char s = sequence.charAt(i);
            isLower = isLowerCase(s);
            isUpper = isUpperCase(s);


            if (isUpper && isUpperCase(sequence.charAt(i + 1))) {
                EXON = EXON + sequence.substring(i, i + 1);
                ExonExon++; // transition from Upper case to Upper case

            }
            if (isUpper && isLowerCase(sequence.charAt(i + 1))) {
                ExonIntron++;  // transition from Upper case to lower case !

            }
            if (isLower && isLowerCase(sequence.charAt(i + 1))) {
                IntronExon++; // transition from Lower case to lower case !
                INTRON = INTRON + sequence.substring(i, i + 1);

            }
            if (isLower && isUpperCase(sequence.charAt(i + 1))) {
                IntronIntron++; // transition from Lower case to Upper case !
                INTRON = INTRON + sequence.charAt(i);
                EXON = EXON + sequence.charAt(i + 1);
            }


            Transitions++;
        }
        EXON = EXON + sequence.charAt(sequence.length() - 1);
        //System.out.println("The exon : " + EXON);
        //System.out.println("The Intron : " + INTRON);

        EmissionProbability(EXON, "Exon");
        EmissionProbability(INTRON, "Intron");

    }


    private static void EmissionProbability(String subsequence, String state) {
        if (state == "Intron")
            subsequence = subsequence.toUpperCase();
        int size = subsequence.length();
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (subsequence.charAt(i) == 'A') {
                count++;
            }
        }
        if (state == "Exon") {
            AProbability = (double) count / size;
            A = A + AProbability;
        } else {
            aProbability = (double) count / size;
            a = a + aProbability;
        }
        count = 0;
        for (int i = 0; i < size; i++) {
            if (subsequence.charAt(i) == 'T') {
                count++;
            }
        }
        if (state == "Exon") {
            TProbability = (double) count / size;
            T = T + TProbability;
        } else {
            tProbability = (double) count / size;
            t = t + tProbability;
        }
        count = 0;
        for (int i = 0; i < size; i++) {
            if (subsequence.charAt(i) == 'C') {
                count++;
            }
        }
        if (state == "Exon") {
            CProbability = (double) count / size;
            C = C + CProbability;
        } else {
            cProbability = (double) count / size;
            c = c + cProbability;
        }
        count = 0;
        for (int i = 0; i < size; i++) {
            if (subsequence.charAt(i) == 'G') {
                count++;
            }
        }
        if (state == "Exon") {
            GProbability = (double) count / size;
            G = G + GProbability;
        } else {
            gProbability = (double) count / size;
            g = g + gProbability;
        }
    }
/*

    private static void CalculateEmissionProbabilities(String line, String state) {
        if (state == "Exon") {
            for (int i = 0; i < line.length() - 1; i++) {
                if (isUpperCase(line.charAt(i))) {
                    if (line.charAt(i) == 'A') {
                        switch (line.charAt(i + 1)) {
                            case 'A': {
                                AtoA++;
                                break;
                            }
                            case 'T': {
                                AtoT++;
                                break;
                            }
                            case 'C': {
                                AtoC++;
                                break;
                            }
                            case 'G': {
                                AtoG++;
                                break;
                            }
                        }
                        Atransitions++;
                    }
                    if (line.charAt(i) == 'T') {
                        switch (line.charAt(i + 1)) {
                            case 'A': {
                                TtoA++;
                                break;
                            }
                            case 'T': {
                                TtoT++;
                                break;
                            }
                            case 'C': {
                                TtoC++;
                                break;
                            }
                            case 'G': {
                                TtoG++;
                                break;
                            }
                        }
                        Ttransitions++;
                    }
                    if (line.charAt(i) == 'C') {
                        switch (line.charAt(i + 1)) {
                            case 'A': {
                                CtoA++;
                                break;
                            }
                            case 'T': {
                                CtoT++;
                                break;
                            }
                            case 'C': {
                                CtoC++;
                                break;
                            }
                            case 'G': {
                                CtoG++;
                                break;
                            }
                        }
                        Ctransitions++;
                    }
                    if (line.charAt(i) == 'G') {
                        switch (line.charAt(i + 1)) {
                            case 'A': {
                                GtoA++;
                                break;
                            }
                            case 'T': {
                                GtoT++;
                                break;
                            }
                            case 'C': {
                                GtoC++;
                                break;
                            }
                            case 'G': {
                                GtoG++;
                                break;
                            }
                        }
                        Gtransitions++;
                    }
                } else {
                    break;
                }
            }
        } else {
            for (int i = 0; i < line.length() - 1; i++) {
                if (isLowerCase(line.charAt(i))) {
                    if (line.charAt(i) == 'a') {
                        switch (line.charAt(i + 1)) {
                            case 'a': {
                                atoa++;
                                break;
                            }
                            case 't': {
                                atot++;
                                break;
                            }
                            case 'c': {
                                atoc++;
                                break;
                            }
                            case 'g': {
                                atog++;
                                break;
                            }
                        }
                        atransitions++;
                    }
                    if (line.charAt(i) == 't') {
                        switch (line.charAt(i + 1)) {
                            case 'a': {
                                ttoa++;
                                break;
                            }
                            case 't': {
                                ttot++;
                                break;
                            }
                            case 'c': {
                                ttoc++;
                                break;
                            }
                            case 'g': {
                                ttog++;
                                break;
                            }
                        }
                        ttransitions++;
                    }
                    if (line.charAt(i) == 'c') {
                        switch (line.charAt(i + 1)) {
                            case 'a': {
                                ctoa++;
                                break;
                            }
                            case 't': {
                                ctot++;
                                break;
                            }
                            case 'c': {
                                ctoc++;
                                break;
                            }
                            case 'g': {
                                ctog++;
                                break;
                            }
                        }
                        ctransitions++;
                    }
                    if (line.charAt(i) == 'g') {
                        switch (line.charAt(i + 1)) {
                            case 'a': {
                                gtoa++;
                                break;
                            }
                            case 't': {
                                gtot++;
                                break;
                            }
                            case 'c': {
                                gtoc++;
                                break;
                            }
                            case 'g': {
                                gtog++;
                                break;
                            }
                        }
                        gtransitions++;
                    }
                } else
                    break;
            }
        }
    }
    */

    private static class TNode {
        public double prob;
        public int[] v_path;
        public double v_prob;

        public TNode(double prob, int[] v_path, double v_prob) {
            this.prob = prob;
            this.v_path = copyIntArray(v_path);
            this.v_prob = v_prob;
        }
    }

    private static int[] copyIntArray(int[] ia) {
        int[] newIa = new int[ia.length];
        for (int i = 0; i < ia.length; i++) {
            newIa[i] = ia[i];
        }
        return newIa;
    }

    private static int[] copyIntArray(int[] ia, int newInt) {
        int[] newIa = new int[ia.length + 1];
        for (int i = 0; i < ia.length; i++) {
            newIa[i] = ia[i];
        }
        newIa[ia.length] = newInt;
        return newIa;
    }

    // Viterbi(observations, states, start_probability, transition_probability, emission_probability)
    public static void Veteerbi(String[] y, String[] X, double[] sp, double[][] tp, double[][] ep) {
        TNode[] T = new TNode[X.length];
        for (int state = 0; state < X.length; state++) {
            int[] intArray = new int[1];
            intArray[0] = state;
            T[state] = new TNode(sp[state], intArray, sp[state]);
        }

        for (int output = 0; output < y.length; output++) {
            TNode[] U = new TNode[X.length];
            for (int next_state = 0; next_state < X.length; next_state++) {
                double total = 0;
                int[] argmax = new int[0];
                double valmax = 0;
                for (int state = 0; state < X.length; state++) {
                    double prob = T[state].prob;
                    int[] v_path = copyIntArray(T[state].v_path);
                    double v_prob = T[state].v_prob;
                    double p = ep[state][output] * tp[state][next_state];
                    prob *= p;
                    v_prob *= p;
                    total += prob;
                    if (v_prob > valmax) {
                        argmax = copyIntArray(v_path, next_state);
                        valmax = v_prob;
                    }
                }
                U[next_state] = new TNode(total, argmax, valmax);
            }
            T = U;
        }
        // apply sum/max to the final states:
        double total = 0;
        int[] argmax = new int[0];
        double valmax = 0;
        for (int state = 0; state < X.length; state++) {
            double prob = T[state].prob;
            int[] v_path = copyIntArray(T[state].v_path);
            double v_prob = T[state].v_prob;
            total += prob;
            if (v_prob > valmax) {
                argmax = copyIntArray(v_path);
                valmax = v_prob;
            }
        }

        System.out.print(" Probability of the state:" + total + ".\n Viterbi path: [");
        for (int i = 0; i < argmax.length; i++) {
            System.out.print(states[argmax[i]] + ", ");
        }
        System.out.println("].\n Probability of the whole system: " + valmax);
        return;
    }
}
