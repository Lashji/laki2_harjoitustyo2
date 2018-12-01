import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Harjoitustyo2 {

    public static final char[] merkit = {'#', '@', '&', '$', '%', 'x', '*', 'o', '|', '!', ';', ':', '\'', ',', '.', ' '};

    public static void main(String[] args) {


    }

    public static void tulostaKuva(String[][] kuva) {

        for (int i = 0; i < kuva.length; i++) {
            for (int j = 0; j < kuva[i].length; j++) {
                System.out.println(kuva[i][j]);
            }
        }
    }

    public static char[][] lataaKuva(String nimi) {

        try {
            File tiedosto = new File(nimi);
            Scanner sc = new Scanner(tiedosto);
            int kuvanLeveys = getTiedostonLeveys(sc);
            int kuvanKorkeus = getTiedostonKorkeus(sc);
            char[][] kuva = new char[kuvanLeveys][kuvanKorkeus];

            int indeksi = 0;
            while (sc.hasNextLine()) {

                lisaaRivi(kuva, indeksi, sc.nextLine());
                indeksi++;
            }


        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public static void lisaaRivi(char[][] kuva, int indeksi, String line) {
        char[] lineChars = getRivitMerkkeina(line);
        for (int i = 0; i < line.length(); i++) {
            kuva[indeksi][i] = lineChars[i];
        }
    }

    public static char[] getRivitMerkkeina(String line) {
        String[] lines = line.split("[ ]");
        char[] chars = new char[lines.length];

        for (int i = 0; i < lines.length; i++) {
            chars[i] = lines[i].charAt(0);

        }

    }

    public static int getTiedostonLeveys(Scanner sc) {
        int leveys = 0;
        leveys = sc.nextLine().length();
        sc.reset();
        return leveys;
    }

    public static int getTiedostonKorkeus(Scanner sc) {
        int korkeus = 0;

        while (sc.hasNextLine()) {
            sc.nextLine();
            korkeus++;
        }
        sc.reset();
        return korkeus;
    }

}
