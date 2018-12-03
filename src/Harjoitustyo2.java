import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Harjoitustyo2 {

    public static final char[] IMAGE_CHARS = {'#', '@', '&', '$', '%', 'x', '*', 'o', '|', '!', ';', ':', '\'', ',', '.', ' '};
    public static final String SELECTION_TEXT = "printa/printi/info/filter [n]/reset/quit?";

    public static void main(String[] args) {
        printHello();
        String fileName = getFileName(args);

        if (fileName != null) {
            boolean jatka = true;

            int[][] image = loadImage(fileName);

            while (jatka) {

                System.out.println(SELECTION_TEXT);
                String userChoice =

            }

            printImage(image);
        }
        printEnd();

    }

    public static void printImage(int[][] kuva) {
        if (kuva != null) {

            for (int i = 0; i < kuva.length; i++) {
                for (int j = 0; j < kuva[i].length; j++) {
                    System.out.print(IMAGE_CHARS[kuva[i][j]]);
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

                kuva = addRow(kuva, indeksi, sc.nextLine());
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

    public static int[][] addRow(int[][] kuva, int indeksi, String line) {
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

        return kuva;
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

    public static void printHello() {
        System.out.println("-------------------\n" +
                "| A S C I I A r t |\n" +
                "------------------- ");
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

    public int laskeKeskiarvo(int[][] kuva, int x, int y, int filterXLen, int filterYLen) {
        double summa = 0;
        double arvoja = 0;
        int xkerroin = (int) Math.floor(filterXLen / 2);
        int ykerroin = (int) Math.floor(filterYLen / 2);

        for (int k = 0; k < filterXLen; k++) {

            for (int i = x - xkerroin; i < x + xkerroin; i++) {
                for (int j = y - ykerroin; j < y + ykerroin; j++) {
                    if (isInsideBounds(kuva, i, j)) {
                        summa += kuva[i][j];
                        arvoja++;
                    }
                }
            }

        }

        return (int) Math.round(summa / arvoja);
    }

    public static boolean isInsideBounds(int[][] kuva, int x, int y) {
        return (x >= 0 && y >= 0) && (x <= kuva.length && y <= kuva[0].length);
    }
}
