import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Harjoitustyo2 {

    public static final char[] IMAGE_CHARS = {'#', '@', '&', '$', '%', 'x', '*', 'o', '|', '!', ';', ':', '\'', ',', '.', ' '};
    public static final String SELECTION_TEXT = "printa/printi/info/filter [n]/reset/quit?";

    public static void main(String[] args) {
        printHello();
        String fileName = getFileName(args);
        Scanner sc = new Scanner(System.in);

        if (fileName != null) {
            boolean jatka = true;

            int[][] image = loadImage(fileName, sc);

            while (jatka) {

                jatka = doOperation(sc, image);
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
        } else {
            System.out.println("Kuva null");
        }
    }

    public static boolean doOperation(Scanner sc, int[][] image) {
        boolean cont = true;
        int[][] tmp = image;

        while (cont) {
            System.out.println(SELECTION_TEXT);

            String userChoice = sc.nextLine();
            switch (userChoice.toLowerCase()) {
                case "printa":
                    printImage(tmp);
                    break;
                case "printi":
                    printImageAsNumbers(tmp);
                    break;
                case "info":
                    printInfo();
                    break;
                case "filter":
                    tmp = filterImage(tmp, sc);
                    System.out.println("filtered");
                    break;
                case "reset":
                    return true;
                case "quit":
                    cont = false;
                    break;
            }
        }

        return false;
    }

    public static void printInfo() {

    }

    public static int[][] filterImage(int[][] image, Scanner sc) {
        sc = new Scanner(System.in);
        System.out.println("Give xlen");
        int xlen = Integer.parseInt(sc.nextLine());
        System.out.println("Give ylen");
        int ylen = Integer.parseInt(sc.nextLine());

        int[][] tmp = image;

        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                tmp[i][j] = countAverage(tmp, i, j, xlen, ylen);

            }
        }


        return tmp;

    }

    public static int[][] loadImage(String name, Scanner sc) {

        try {

            File tiedosto = new File(name);
            sc = new Scanner(tiedosto);


            int[][] kuva = new int[10][10];

            int indeksi = 0;


            while (sc.hasNextLine()) {

                kuva = addRow(kuva, indeksi, sc.nextLine());
                indeksi++;

            }
            sc.close();
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

    public static int countAverage(int[][] kuva, int x, int y, int filterXLen, int filterYLen) {
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
