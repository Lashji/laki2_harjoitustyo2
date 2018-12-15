import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
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
            if (image != null) {

                while (jatka) {
                    jatka = doOperation(copyImage(image));
                }

            }
        }
        printEnd();

    }

    public static void printImage(int[][] kuva) {
        if (kuva != null) {

            for (int i = 0; i < kuva.length; i++) {
                for (int j = 0; j < kuva[i].length; j++) {
                    if (String.valueOf(kuva[i][j]).length() == 1){
                        System.out.print(" ");
                    }
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

    public static boolean doOperation(int[][] image) {
        boolean cont = true;
        int[][] tmp = image;

        while (cont) {
            System.out.println(SELECTION_TEXT);

            String userChoice = In.readString();

            if (userChoice.equalsIgnoreCase("printa")) {
                printImage(tmp);
            } else if (userChoice.equalsIgnoreCase("printi")) {
                printImageAsNumbers(tmp);
            } else if (userChoice.equalsIgnoreCase("info")) {
                printInfo();
            } else if (userChoice.startsWith("filter")) {
                tmp = filterImage(tmp, getFilterLen(userChoice));

                System.out.println("filtered");
            } else if (userChoice.equalsIgnoreCase("reset")) {
                return true;
            } else if (userChoice.equalsIgnoreCase("quit")) {
                cont = false;
            }

        }

        return false;
    }

    public static void printInfo() {

    }

    public static int getFilterLen(String userChoice) {
            if (userChoice.equalsIgnoreCase("filter")){
                return 3;
            }

            int num = Character.getNumericValue(
                    userChoice.charAt(userChoice.length() - 1) );

            return num;
    }

    public static int[][] filterImage(int[][] image, int filterSize) {

        int[][] tmp = image;

        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                tmp[i][j] = countAverage(tmp, i, j, filterSize);

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
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }

    public static int[][] addRow(int[][] kuva, int indeksi, String line) {
        if (indeksi >= kuva.length) {

            kuva = copyArrayAndMakeItBigger(kuva, 0);

        }

        int[] nums = getRowAsInt(line);
        for (int i = 0; i < nums.length; i++) {

            if (i >= kuva[indeksi].length) {
                kuva = copyArrayAndMakeItBigger(kuva, 1);
            }

            kuva[indeksi][i] = nums[i];

        }

        return kuva;
    }

    public static int[] getRowAsInt(String line) {
        String[] lines = line.split("[\\s]+");
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
        System.out.println("Bye, see you soon.");
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

    public static int countAverage(int[][] kuva, int x, int y, int filterSize) {
        double summa = 0;
        double arvoja = 0;
        int size = (int) Math.floor(filterSize / 2);

        for (int k = 0; k < filterSize; k++) {

            for (int i = x - size; i < x + size; i++) {
                for (int j = y - size; j < y + size; j++) {
                    if (i >= 0 && y >= 0) {

                        if (isInsideBounds(kuva, i, j)) {
                            summa += kuva[i][j];
                            arvoja++;
                        }
                    }
                }
            }

        }

        return (int) Math.round(summa / arvoja);
    }


    public static int[][] copyImage(int[][] image) {
        int[][] tmp = new int[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {

            for (int j = 0; j < image[i].length; j++) {
                tmp[i][j] = image[i][j];
            }

        }
        return tmp;
    }

    public static boolean isInsideBounds(int[][] kuva, int x, int y) {
        return (x >= 0 && y >= 0) && (x < kuva.length && y < kuva[0].length);
    }


}
