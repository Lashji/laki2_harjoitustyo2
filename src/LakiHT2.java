import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class LakiHT2 {

    public static final char[] IMAGE_CHARS = {'#', '@', '&', '$', '%', 'x', '*', 'o',
            '|', '!', ';', ':', '\'', ',', '.', ' '};
    public static final String SELECTION_TEXT =
            "printa/printi/info/filter [n]/reset/quit?";


    public static void main(String[] args) {
        if (validateArgs(args)) {

            printHello();
            String fileName = getFileName(args);
            Scanner sc = new Scanner(System.in);

            if (fileName != null) {

                boolean jatka = true;
                int[] bounds = countLines(fileName, sc);
                char[][] image = loadImage(fileName, sc, bounds);

                if (image != null) {

                    while (jatka) {
                        jatka = doOperation(copyImage(image), bounds);
                    }

                }
                printEnd();
            }
        }
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
                for (int j = 0; j < kuva[j].length; j++) {
                    if (kuva[i][j] < 10) {
                        System.out.print(" ");
                    }
                    System.out.print(kuva[i][j] + (j + 1 < kuva[j].length ? " " : ""));
                }
                System.out.println();
            }
        } else {
            printError();
        }
    }

    public static boolean doOperation(char[][] image, int[] bounds) {
        boolean cont = true;
        int[][] tmp = charactersToIntegers(image);

        while (cont) {
            System.out.println(SELECTION_TEXT);

            String userChoice = In.readString();

            if (userChoice.equalsIgnoreCase("printa")) {
                printImage(tmp);
            } else if (userChoice.equalsIgnoreCase("printi")) {

                printImageAsNumbers(tmp);

            } else if (userChoice.equalsIgnoreCase("info")) {

                printInfo(tmp, bounds);

            } else if (userChoice.startsWith("filter")) {

                tmp = filterImage(tmp, getFilterLen(userChoice));

            } else if (userChoice.equalsIgnoreCase("reset")) {
                return true;

            } else if (userChoice.equalsIgnoreCase("quit")) {

                cont = false;

            }

        }

        return false;
    }

    public static void printInfo(int[][] image, int[] bounds) {
        int x = bounds[0];
        int y = bounds[1];
        System.out.println(x + " x " + y);

        for (int i = 0; i < IMAGE_CHARS.length; i++) {
            int counter = countFrequency(image, IMAGE_CHARS[i]);
            System.out.println(IMAGE_CHARS[i] + " " + counter);

        }

    }

    public static int getFilterLen(String userChoice) {
        if (userChoice.equalsIgnoreCase("filter")) {
            return 3;
        }

        int num = Character.getNumericValue(
                userChoice.charAt(userChoice.length() - 1));

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

    public static char[][] loadImage(String name, Scanner sc, int[] bounds) {

        try {
            File tiedosto = new File(name);
            sc = new Scanner(tiedosto);


            char[][] kuva = new char[bounds[0]][bounds[1]];

            int indeksi = 0;


            while (sc.hasNextLine()) {

                kuva[indeksi] = addRow(sc.nextLine());
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

    public static char[] addRow(String line) {
        return convertLineToCharArray(line.split(""));
    }

    public static char[] convertLineToCharArray(String[] chars) {
        char[] tmp = new char[chars.length];

        for (int i = 0; i < chars.length; i++) {

            tmp[i] = chars[i].charAt(0);

        }

        return tmp;
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
                "-------------------");
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


    public static char[][] copyImage(char[][] image) {
        char[][] tmp = new char[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {

            for (int j = 0; j < image[i].length; j++) {
                tmp[i][j] = image[i][j];
            }

        }
        return tmp;
    }

    //    Tarkistaa onko indeksi rajojen sisäpuolella
    public static boolean isInsideBounds(int[][] kuva, int x, int y) {
        return (x >= 0 && y >= 0) && (x < kuva.length && y < kuva[0].length);
    }

    //    Operaatio lukee tiedoston jonka jälkeen laskee kuinka monta riviä siitä löytyy.
    //    Lopulta se palauttaa rivien määrän int -arvona.
    public static int[] countLines(String tiedostonimi, Scanner sc) {
        int[] lines = new int[2];
        try {

            File tiedosto = new File(tiedostonimi);
            sc = new Scanner(tiedosto);
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                lines[1] = s.length();
                lines[0]++;
            }
            sc.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return lines;
    }

    public static boolean validateArgs(String[] args) {

        if (args.length == 1) {

            return true;

        }

        printError();
        return false;
    }

    public static void printError() {
        System.out.println("Invalid command-line argument!\n" +
                "Bye, see you soon.");
    }


    //    Muuttaa 2 ulotteisen char taulukon kaikki solut int arvoiksi
    public static int[][] charactersToIntegers(char[][] charList) {
        if (charList == null || charList.length < 1 || charList[0].length < 1) {
            return null;
        }
        int[][] tmp = new int[charList.length][charList[0].length];

        for (int i = 0; i < charList.length; i++) {
            for (int j = 0; j < charList[i].length; j++) {
                tmp[i][j] = convertToInteger(charList[i][j]);
            }
        }

        return tmp;
    }

    //    muuttaa char muuttujan int arvoksi
    public static int convertToInteger(char c) {
        final char t[] = {'#', '@', '&', '$', '%', 'x', '*', 'o', '|', '!', ';', ':', '\'', ',', '.', ' '};

        for (int i = 0; i < t.length; i++) {
            if (c == t[i]) {
                return i;
            }
        }
        return 0;
    }

    public static char[][] convertIntToCharArray(int[][] image){
        char[][] tmp = new char[image.length][image[0].length];

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {

                tmp[i][j] = IMAGE_CHARS[image[i][j]];

            }
        }

        return tmp;
    }

    //    Vertaa taulukkoa toiseen 2 -ulotteiseen taulukkoon ja laskee niiden solujen
//    esiintymät.
    public static int countFrequency(int[][] image, char c){
        int count = 0;
        char[][] tmp = convertIntToCharArray(image);


        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                if (tmp[i][j] == c){
                    count++;
                }

            }
        }


        return count;
    }
}
