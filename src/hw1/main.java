package hw1;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Mounir Abderrahmani on 20.03.2016.
 */
public class main {

    static ArrayList<String> Reads = new ArrayList();
    static String[] mreads = new String[2000];

    private static String getSuperString(ArrayList<String> arr, int length) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < arr.size(); i++) {
            list.add(arr.get(i));
        }
        int size = list.size();
        while (list.size() > 1) {
            int maxLength = -1, index1 = 0, index2 = 0;
            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    int matchLength1 = getMatchedLength(list.get(i), list.get(j));
                    int matchLength2 = getMatchedLength(list.get(j), list.get(i));
                    if (maxLength < matchLength1) {
                        maxLength = matchLength1;
                        index1 = i;
                        index2 = j;
                    }
                    if (maxLength < matchLength2) {
                        maxLength = matchLength2;
                        index1 = j;
                        index2 = i;
                    }
                }
            }
            String merged = getMergeString(index1, index2, maxLength, list);
            String item1 = list.get(index1);
            String item2 = list.get(index2);
            list.remove(item1);
            list.remove(item2);
            list.add(merged);
            size--;
        }
        return list.get(0);
    }

    private static String getMergeString(int index1, int index2, int maxLength, List<String> list) {

        String result = "";
        result = result + list.get(index1);
        result = result + list.get(index2).substring(maxLength);
        return result;
    }

    private static int getMatchedLength(String s1, String s2) {

        int len = Math.min(s1.length(), s2.length());
        int count = 0;

        for (int i = len; i > 0; i--) {
            String substring = s2.substring(0, i);
            if (s1.endsWith(substring)) {

                count = substring.length();
                break;
            }
        }
        return count;
    }

    public static void main(String[] args) {


        Scanner in = new Scanner(System.in);
        System.out.println("Enter the file path to be read :");
        String FilePath = in.next();
        ReadFile(FilePath);
        
        // String[] array = new String[Reads.size()];

        String array[] = {"ump", "uic", "wn_f", "rown", "lazy_", "ox_ju", "the_l", "e_qui", "ck_bro", "n_fox_", "zy_dog", "e_lazy", "mps_ov", "er_the", "ps_ove", "the_qu"};

        String string = getSuperString( Reads, Reads.size());
        System.out.println(string);

    }


    public static void ReadFile(String FilePath) {

        String row = "";
        try {
            File programFile = new File(FilePath);
            FileReader file1 = new FileReader(FilePath);
            Scanner file = new Scanner(file1);

            while (file.hasNextLine()) {
                row = file.nextLine();
                Reads.add(row);
            }
            file.close();
        } catch (Exception e) {
            System.out.println("File does not exist!" + e);
        }
    }
}
