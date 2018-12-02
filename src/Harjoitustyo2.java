import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Harjoitustyo2 {

    public static final char[] imageChars = {'#', '@', '&', '$', '%', 'x', '*', 'o', '|', '!', ';', ':', '\'', ',', '.', ' '};

    public static void main(String[] args) {

        String fileName = getFileName(args);
        if (fileName != null) {

            int[][] image = loadImage(fileName);


            printImage(image);
        } else {
            printEnd();
        }

    }

    public static void printImage(int[][] kuva) {
        if (kuva != null) {

            for (int i = 0; i < kuva.length; i++) {
                for (int j = 0; j < kuva[i].length; j++) {
                    System.out.print(imageChars[kuva[i][j]]);
                }
                System.out.println();
            }
        }
    }

    public static void printImageAsNumbers(int[][] kuva) {
        if (kuva != null) {

            for (int i = 0; i < kuva.length; i++) {
                for (int j = 0; j < kuva[i].length; j++) {
                    System.out.print(kuva[i][j]);
                }
                System.out.println();
            }
        }
    }

    public static int[][] loadImage(String nimi) {

        try {

            File tiedosto = new File(nimi);
            Scanner sc = new Scanner(tiedosto);

            int[][] kuva = new int[10][10];

            int indeksi = 0;


            while (sc.hasNextLine()) {

                addRow(kuva, indeksi, sc.nextLine());
                indeksi++;

            }

            return kuva;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static void addRow(int[][] kuva, int indeksi, String line) {
        if (indeksi >= kuva.length) {

            kuva = copyArrayAndMakeItBigger(kuva, 0);

        }

        int[] nums = getRowsAsInt(line);
        for (int i = 0; i < nums.length; i++) {

            if (i >= kuva[indeksi].length) {
                kuva = copyArrayAndMakeItBigger(kuva, 1);
            }

            kuva[indeksi][i] = nums[i];

        }

    }

    public static int[] getRowsAsInt(String line) {
        String[] lines = line.split("[ ]");
        int[] nums = new int[lines.length];

        for (int i = 0; i < lines.length; i++) {
            nums[i] = Integer.parseInt(lines[i]);
        }

        return nums;
    }

    public static String getFileName(String[] args) {
        return args.length > 0 ? args[0] : null;
    }


    public static void printEnd() {
        System.out.println("Loppu");
    }

    public static int[][] copyArrayAndMakeItBigger(int[][] arr, int index) {

        int[][] copy = index == 0 ? new int[arr.length + 1][arr[0].length] :
                new int[arr.length][arr[0].length + 1];

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                copy[i][j] = arr[i][j];
            }
        }
        return copy;
    }

}
