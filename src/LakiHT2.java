import java.io.File;
import java.io.FileNotFoundException;
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

                boolean continueLoop = true;
                int[] bounds = countLines(fileName, sc);
                char[][] image = loadImage(fileName, sc, bounds);

                if (image != null && (image.length > 0 && image[0].length > 0)) {

                    while (continueLoop) {
                        continueLoop = doOperation(copyImage(image), bounds);
                    }

                    printEnd();
                } else {
                    printError();
                }
            }
        }
    }

    //    Prints image (printa)
    public static void printImage(int[][] image) {
        if (image != null) {

            for (int i = 0; i < image.length; i++) {
                for (int j = 0; j < image[i].length; j++) {
                    System.out.print(IMAGE_CHARS[image[i][j]]);
                }

                System.out.println();
            }
        }
    }

    //    Prints image in integers (printi)
    public static void printImageAsNumbers(int[][] image) {
        if (image != null) {

            for (int i = 0; i < image.length; i++) {
                for (int j = 0; j < image[i].length; j++) {
                    if (image[i][j] < 10) {
                        System.out.print(" ");
                    }
                    System.out.print(image[i][j] + (j + 1 < image[i].length ? " " : ""));
                }
                System.out.println();
            }
        } else {
            printError();
        }
    }

    // Takes user input and runs given operations
    public static boolean doOperation(char[][] image, int[] bounds) {

        if (image == null || bounds == null) {
            printError();
            return false;
        }

        boolean continueLoop = true;
        int[][] tmp = charactersToIntegers(image);

        while (continueLoop) {
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

                continueLoop = false;

            }

        }

        return false;
    }

    //    Prints image info
    public static void printInfo(int[][] image, int[] bounds) {
        if (image != null && bounds != null) {

            int x = bounds[0];
            int y = bounds[1];
            System.out.println(x + " x " + y);

            for (int i = 0; i < IMAGE_CHARS.length; i++) {
                int counter = countFrequency(image, IMAGE_CHARS[i]);
                System.out.println(IMAGE_CHARS[i] + " " + counter);

            }
        }

    }

    //  Finds out wanted filter length and returns it
    public static int getFilterLen(String userChoice) {

        if (userChoice == null) {
            return 0;
        }

        if (userChoice.equalsIgnoreCase("filter")) {
            return 3;
        }

        return Integer.parseInt(userChoice.substring(7));
    }

    //    Filters the array with given parameters
    public static int[][] filterImage(int[][] image, int filterSize) {
        if (image != null && filterSize != 0) {

            int[][] tmp = new int[image.length][image[0].length];

            for (int i = 0; i < tmp.length; i++) {
                for (int j = 0; j < tmp[i].length; j++) {

                    if (enoughSpaceForFilter(image, i, j, filterSize)) {

                        tmp[i][j] = countAverage(image, i, j, filterSize);

                    } else {
                        tmp[i][j] = image[i][j];
                    }
                }
            }


            return tmp;
        }
        return null;
    }

    //    Loads image from a text file and returns it
    public static char[][] loadImage(String name, Scanner sc, int[] bounds) {

        if (name.equals("") || sc == null || bounds == null) {
            return null;
        }

        try {

            File tiedosto = new File(name);
            sc = new Scanner(tiedosto);

            char[][] image = new char[bounds[0]][bounds[1]];

            int index = 0;

            while (sc.hasNextLine()) {

                image[index] = addRow(sc.nextLine());
                index++;

            }

            sc.close();
            return image;
        } catch (FileNotFoundException e) {
//            System.out.println(e.getMessage());
        } catch (Exception e) {
//            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return null;
    }

    //    Returns a String as char array
    public static char[] addRow(String line) {

        if (line.equals("")) {
            return null;
        }

        return convertLineToCharArray(line.split(""));
    }

    //    Converts a string to char array
    public static char[] convertLineToCharArray(String[] chars) {
        if (chars == null) {
            return null;
        }

        char[] tmp = new char[chars.length];

        for (int i = 0; i < chars.length; i++) {

            tmp[i] = chars[i].charAt(0);

        }

        return tmp;
    }

    // Returns filename from args
    public static String getFileName(String[] args) {
        if (args == null) {
            return null;
        }
        return args.length > 0 ? args[0] : null;
    }

    // Prints End message
    public static void printEnd() {
        System.out.println("Bye, see you soon.");
    }

    //    Prints hello message
    public static void printHello() {
        System.out.println("-------------------\n" +
                "| A S C I I A r t |\n" +
                "-------------------");
    }


    // Counts and returns average integer in the filtered area
    public static int countAverage(int[][] image, int x, int y, int filterSize) {

        if (image == null) {
            return 0;
        }

        double summa = 0;
        double arvoja = 0;
        int size = (int) Math.floor(filterSize / 2);
        int xstart = x - size;
        int xend = x + size;
        int ystart = y - size;
        int yend = y + size;

        for (int i = xstart; i <= xend; i++) {
            for (int j = ystart; j <= yend; j++) {
                if (i >= 0 && j >= 0) {

                    if (isInsideBounds(image, i, j)) {
                        summa += image[i][j];
                        arvoja++;
                    }

                }
            }

        }

        return (int) Math.round(summa / arvoja);
    }

    //    Copies the image array
    public static char[][] copyImage(char[][] image) {

        if (image == null) {
            return null;
        }

        char[][] tmp = new char[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {

            for (int j = 0; j < image[i].length; j++) {
                tmp[i][j] = image[i][j];
            }

        }
        return tmp;
    }

    //    Checks that given index is inside bounds
    public static boolean isInsideBounds(int[][] image, int x, int y) {
        if (image == null) {
            return false;
        }

        return (x >= 0 && y >= 0) && (x < image.length && y < image[0].length);
    }

    //    Operation reads file and counts how many rows and cols it has
//    [0] is rows and [1] is cols
    public static int[] countLines(String tiedostonimi, Scanner sc) {

        if (tiedostonimi.equals("") || sc == null) {
            return null;
        }


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

    //    Checks that given arguments are valid
    public static boolean validateArgs(String[] args) {

        if (args == null) {
            printError();
            return false;
        }

        if (args.length == 1) {

            return true;

        }

        return false;
    }

    //    Prints error message
    public static void printError() {
        System.out.println("Invalid command-line argument!\n" +
                "Bye, see you soon.");
    }


    //    Converts 2d array to 2d integer array
    public static int[][] charactersToIntegers(char[][] charList) {
        if (charList == null || charList.length < 1 || charList[0].length < 1) {
            return null;
        }
        int[][] tmp = new int[charList.length][charList[0].length];

        for (int i = 0; i < charList.length; i++) {
            for (int j = 0; j < charList[i].length; j++) {
                tmp[i][j] = convertCharToInteger(charList[i][j]);
            }
        }

        return tmp;
    }

    //    Converts char variable to integer and returns it
    public static int convertCharToInteger(char c) {
        if (c == '\u0000') {
            return 0;
        }

        for (int i = 0; i < IMAGE_CHARS.length; i++) {
            if (c == IMAGE_CHARS[i]) {
                return i;
            }
        }

        return 0;
    }

    //    Converts Integer array to Char array
    public static char[][] convertIntToCharArray(int[][] image) {

        if (image == null) {
            return null;
        }

        char[][] tmp = new char[image.length][image[0].length];

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {

                tmp[i][j] = IMAGE_CHARS[image[i][j]];

            }
        }

        return tmp;
    }


    //    Counts how many times given parameter c exists in array
    public static int countFrequency(int[][] image, char c) {
        if (image == null || c == '\u0000') {
            return 0;
        }

        int count = 0;
        char[][] tmp = convertIntToCharArray(image);


        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                if (tmp[i][j] == c) {
                    count++;
                }

            }
        }


        return count;

    }

    //    Checks if there is enough space for filter
    public static boolean enoughSpaceForFilter(int[][] image, int x, int y, int filterSize) {
        if (image == null) {
            return false;
        }
        int size = (int) Math.floor(filterSize / 2);

        for (int k = 0; k < filterSize; k++) {

            for (int i = x - size; i <= x + size; i++) {
                for (int j = y - size; j <= y + size; j++) {

                    if (!isInsideBounds(image, i, j)) {
                        return false;

                    }
                }
            }

        }
        return true;
    }


}
