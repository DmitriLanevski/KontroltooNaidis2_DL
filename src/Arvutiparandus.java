import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class Arvutiparandus {

    private static int erindiPositsioon = -1;

    public static void main(String[] args) throws IOException {
        System.out.println("Sisesta faili nimi või veebiaadress:");
        try (BufferedReader sisse = new BufferedReader(new InputStreamReader(System.in))) {
            String s = sisse.readLine();
            if (s.contains("http://") || s.contains("https://")) {
                try (BufferedReader veebist = new BufferedReader(new InputStreamReader(new URL(s).openConnection().getInputStream()))) {
                    String tooKirjeldus;
                    while ((tooKirjeldus = veebist.readLine()) != null) {
                        System.out.println(tooKirjeldus);
                    }
                }
            } else {
                String path = ".\\src\\Failid\\" + s;
                try (BufferedReader failist = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
                    String tooKirjeldus;
                    while ((tooKirjeldus = failist.readLine()) != null) {
                        System.out.println(tooKirjeldus);
                        try {
                            Arvuti arvuti = loeArvuti(tooKirjeldus);
                            System.out.println(arvuti.arvutiToString());
                        } catch (FormaadiErind e) {
                            System.out.println(e.toString());
                            //throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

        //LocalDateTime registreerimiseKuupäev = LocalDateTime.parse("2016-04-21T14:22:42");
        //System.out.println(registreerimiseKuupäev);
    }


    private static Arvuti loeArvuti(String tooKirjeldus) {

        String tootja = null;
        String tööLiik = null;
        String monitoriga = null;
        LocalDateTime registreerimiseKuupäev = null;

        if (!tooKirjeldus.contains(";") && !tooKirjeldus.contains("|")){
            throw new FormaadiErind(tooKirjeldus, 1);
        }

        if (!tooKirjeldus.contains(";") && tooKirjeldus.contains("|")){

            String[] tööKijelduseOsad = tooKirjeldus.split("\\|");

            if (!tööKijelduseOsad[0].equals("kiirtöö") &&
                    !tööKijelduseOsad[0].equals("tavatöö") &&
                    !tööKijelduseOsad[0].equals("monitoriga") &&
                    !(tööKijelduseOsad[0].contains("-") && tööKijelduseOsad[0].contains("T")) &&
                    !tööKijelduseOsad[0].isEmpty()){
                tootja = tööKijelduseOsad[0];
                throw new FormaadiErind(tooKirjeldus, 2);
            } else {
                throw new FormaadiErind(tooKirjeldus, 1);
            }
        }

        if (tooKirjeldus.contains(";") && !tooKirjeldus.contains("|")){

            String[] tööKijelduseOsad = tooKirjeldus.split(";");

            if (tööKijelduseOsad.length > 0){
                if (!tööKijelduseOsad[0].equals("kiirtöö") &&
                        !tööKijelduseOsad[0].equals("tavatöö") &&
                        !tööKijelduseOsad[0].equals("monitoriga") &&
                        !(tööKijelduseOsad[0].contains("-") && tööKijelduseOsad[0].contains("T")) &&
                        !tööKijelduseOsad[0].isEmpty()){
                    tootja = tööKijelduseOsad[0];
                } else {
                    throw new FormaadiErind(tooKirjeldus, 1);
                }

                if (tööKijelduseOsad[1].equals("kiirtöö")){
                    tööLiik = "kiirtöö";
                } else if (tööKijelduseOsad[1].equals("tavatöö")){
                    tööLiik = "tavatöö";
                } else {
                    throw new FormaadiErind(tooKirjeldus, 2);
                }

                if (tööKijelduseOsad.length == 3){
                    if (tööKijelduseOsad[2].equals("monitoriga")){
                        monitoriga = "monitoriga";
                    } else {
                        throw new FormaadiErind(tooKirjeldus, 3);
                    }
                }
                if (tööKijelduseOsad.length > 3){
                    throw new FormaadiErind(tooKirjeldus, 3);
                }
            } else {
                throw new FormaadiErind(tooKirjeldus, 1);
            }

        }

        if (tooKirjeldus.contains(";") && tooKirjeldus.contains("|")){

            String[] tööKijelduseOsad = tooKirjeldus.split(";");

            if (!tööKijelduseOsad[0].equals("kiirtöö") &&
                    !tööKijelduseOsad[0].equals("tavatöö") &&
                    !tööKijelduseOsad[0].equals("monitoriga") &&
                    !(tööKijelduseOsad[0].contains("-") && tööKijelduseOsad[0].contains("T")) &&
                    !tööKijelduseOsad[0].isEmpty()){
                tootja = tööKijelduseOsad[0];
            } else {
                throw new FormaadiErind(tooKirjeldus, 1);
            }

            if (tööKijelduseOsad[1].contains("|")){

                String[] veelTööKijelduseOsad = tööKijelduseOsad[1].split("\\|");

                if (veelTööKijelduseOsad.length > 0){
                    if (veelTööKijelduseOsad[0].equals("kiirtöö")){
                        tööLiik = "kiirtöö";
                    } else if (veelTööKijelduseOsad[0].equals("tavatöö")){
                        tööLiik = "tavatöö";
                    } else {
                        throw new FormaadiErind(tooKirjeldus, 2);
                    }

                    if (veelTööKijelduseOsad.length > 1){
                        try{
                            registreerimiseKuupäev = LocalDateTime.parse(veelTööKijelduseOsad[1].trim());
                        } catch(DateTimeParseException e){
                            throw new FormaadiErind(tooKirjeldus, 3);
                        }
                    } else {
                        throw new FormaadiErind(tooKirjeldus, 3);
                    }
                } else {
                    throw new FormaadiErind(tooKirjeldus, 2);
                }

            } else {
                if (tööKijelduseOsad[1].equals("kiirtöö")){
                    tööLiik = "kiirtöö";
                } else if (tööKijelduseOsad[1].equals("tavatöö")){
                    tööLiik = "tavatöö";
                } else {
                    throw new FormaadiErind(tooKirjeldus, 2);
                }
            }

            if (tööKijelduseOsad.length > 2){

                if (tööKijelduseOsad[2].contains("|")) {

                    String[] veelTööKijelduseOsad = tööKijelduseOsad[2].split("\\|");

                    if (veelTööKijelduseOsad.length > 0){
                        if (veelTööKijelduseOsad[0].equals("monitoriga")){
                            monitoriga = "monitoriga";
                        } else {
                            throw new FormaadiErind(tooKirjeldus, 3);
                        }
                        if (veelTööKijelduseOsad.length > 1){
                            try{
                                registreerimiseKuupäev = LocalDateTime.parse(veelTööKijelduseOsad[1].trim());
                            } catch(DateTimeParseException e){
                                throw new FormaadiErind(tooKirjeldus, 4);
                            }
                        } else {
                            throw new FormaadiErind(tooKirjeldus, 4);
                        }
                    } else {
                        throw new FormaadiErind(tooKirjeldus, 3);
                    }

                } else {
                    if (tööKijelduseOsad[2].equals("monitoriga")){
                        monitoriga = "monitoriga";
                    } else {
                        throw new FormaadiErind(tooKirjeldus, 3);
                    }
                }
            }

            if (tööKijelduseOsad.length > 3){
                throw new FormaadiErind(tooKirjeldus, 4);
            }
        }

        if (monitoriga != null){
            return new VäliseMonitorigaArvuti(tootja, tööLiik, registreerimiseKuupäev);
        } else {
            return new Arvuti(tootja, tööLiik, registreerimiseKuupäev);
        }
    }
}
