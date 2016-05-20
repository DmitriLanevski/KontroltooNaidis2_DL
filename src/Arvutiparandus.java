import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Arvutiparandus {

    public static void main(String[] args) throws Exception {

        String valik = " ";

        Queue<Arvuti> kiirtöö = new LinkedList<>();
        Queue<Arvuti> tavatöö = new LinkedList<>();
        List<Arvuti> tehtudTööd = new ArrayList<>();
        HashMap<String, Double> nimedJaTasud = laadiNimedJaTasud();

        File erinditeLog = new File(".\\src\\Failid\\vigased_kirjeldused.txt");

        if (!erinditeLog.exists()) {
            erinditeLog.createNewFile();
        }

        System.out.println("Sisesta faili nimi või veebiaadress:");

        try (BufferedReader sisse = new BufferedReader(new InputStreamReader(System.in));
             FileWriter fw = new FileWriter(erinditeLog)) {
            String s = sisse.readLine();
            //s = "ootel_arvutid.txt";
            if (s.contains("http://") || s.contains("https://")) {
                try (BufferedReader veebist = new BufferedReader(new InputStreamReader(new URL(s).openConnection().getInputStream()))) {
                    String tooKirjeldus;
                    while ((tooKirjeldus = veebist.readLine()) != null) {
                        try {
                            Arvuti arvuti = loeArvuti(tooKirjeldus);
                            if (arvuti.onKiirtöö()) {
                                kiirtöö.add(arvuti);
                            } else {
                                tavatöö.add(arvuti);
                            }
                            //System.out.println(arvuti.arvutiToString());
                        } catch (FormaadiErind e) {
                            //System.out.println(e.toString());
                            fw.write("Rea sisu:" + tooKirjeldus + "\r\n" + e.toString() + "\r\n");
                        }
                    }
                }
            } else {
                String path = ".\\src\\Failid\\" + s;
                try (BufferedReader failist = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
                    String tooKirjeldus;
                    while ((tooKirjeldus = failist.readLine()) != null) {
                        //System.out.println(tooKirjeldus);
                        try {
                            Arvuti arvuti = loeArvuti(tooKirjeldus);
                            if (arvuti.onKiirtöö()) {
                                kiirtöö.add(arvuti);
                            } else {
                                tavatöö.add(arvuti);
                            }
                            //System.out.println(arvuti.arvutiToString());
                        } catch (FormaadiErind e) {
                            //System.out.println(e.toString());
                            fw.write("Rea sisu:" + tooKirjeldus + "\r\n" + e.toString() + "\r\n");
                        }
                    }
                }
            }

            while (!valik.equals("L")){
                System.out.print("Kas soovid parandada (P), uut tööd registreerida (R) või lõpetada (L) ? ");
                valik = sisse.readLine();
                if (valik.equals("P")){
                    if (!kiirtöö.isEmpty()){
                        Arvuti arvuti = kiirtöö.poll();
                        System.out.println("Arvuti info: " + arvuti.arvutiToString());
                        int kulunudAeg = -1;
                        while(kulunudAeg < 0){
                            try{
                                System.out.print("Sisesta parandamiseks kilunud aeg (täisminutites): ");
                                kulunudAeg = Integer.parseInt(sisse.readLine());
                            } catch (NumberFormatException e){
                                System.out.println("Sisestus pole number!");
                            }
                        }
                        String nimi = " ";
                        while (!nimi.equals("OK")){
                            System.out.print("Sisesta nimi: ");
                            nimi = sisse.readLine();
                            if (nimedJaTasud.containsKey(nimi)){
                                double baasHind = nimedJaTasud.get(nimi) * (kulunudAeg/60);
                                arvuti.lõpetaTöö(baasHind);
                                tehtudTööd.add(arvuti);
                                System.out.println("Töö tehtud, arve summa on " + arvuti.getArveSumma() + "\u20ac" +"!");
                                nimi = "OK";
                            } else {
                                System.out.println("Sellist töötajat pole olemas!");
                            }
                        }
                    } else {
                        if (!tavatöö.isEmpty()){
                            Arvuti arvuti = tavatöö.poll();
                            System.out.println("Arvuti info: " + arvuti.arvutiToString());
                            int kulunudAeg = -1;
                            while(kulunudAeg < 0){
                                try{
                                    System.out.print("Sisesta parandamiseks kilunud aeg (täisminutites): ");
                                    kulunudAeg = Integer.parseInt(sisse.readLine());
                                } catch (NumberFormatException e){
                                    System.out.println("Sisestus pole number!");
                                }
                            }
                            String nimi = " ";
                            while (!nimi.equals("OK")){
                                System.out.print("Sisesta nimi: ");
                                nimi = sisse.readLine();
                                if (nimedJaTasud.containsKey(nimi)){
                                    double baasHind = nimedJaTasud.get(nimi) * (kulunudAeg/60);
                                    arvuti.lõpetaTöö(baasHind);
                                    tehtudTööd.add(arvuti);
                                    System.out.println("Töö tehtud, arve summa on " + arvuti.getArveSumma() + "\u20ac" +"!");
                                    nimi = "OK";
                                } else {
                                    System.out.println("Sellist töötajat pole olemas!");
                                }
                            }
                        }
                    }
                }
                else if (valik.equals("R")){
                    System.out.print("Sisesta töö kirjeldus: ");
                    String kirjeldus = sisse.readLine();
                    while(!kirjeldus.equals("OK")){
                        try {
                            Arvuti arvuti = loeArvuti(kirjeldus);
                            arvuti.setDate(LocalDateTime.now());
                            if (arvuti.onKiirtöö()){
                                kiirtöö.add(arvuti);
                            } else {
                                tavatöö.add(arvuti);
                            }
                            kirjeldus = "OK";
                            //System.out.println(arvuti.arvutiToString());
                        } catch (FormaadiErind e) {
                            //System.out.println(e.toString());
                            System.out.println(e.toString());
                            fw.write("Rea sisu:" + kirjeldus + "\r\n" + e.toString() + "\r\n");
                            System.out.print("Sisesta töö kirjeldus: ");
                            kirjeldus = sisse.readLine();
                        }
                    }
                    System.out.println("Töö on registreeritud!");
                } else {
                    if (!valik.equals("L")){
                        System.out.println("Vale sisestus!");
                    }
                }
                System.out.println(" ");
            }
        }
        int ooteleJai = kiirtöö.size() + tavatöö.size();

        salvestaTehrud(tehtudTööd);
        salvestaOotel(kiirtöö,tavatöö);

        Double koguArveSumma = new Double(0);
        HashMap<String, Integer> tulemus = new HashMap<>();
        for (Arvuti arvuti : tehtudTööd){
            koguArveSumma += arvuti.getArveSumma();
            if (!tulemus.containsKey(arvuti.getTootja())){
                tulemus.put(arvuti.getTootja(), 1);
            } else {
                Integer tmp = tulemus.get((arvuti.getTootja()));
                tmp++;
                tulemus.replace(arvuti.getTootja(), tmp);
            }
        }

        System.out.println("Sessiooni kokkuvõtte:");
        System.out.println("Teenitud raha: " + koguArveSumma + "\u20ac");
        System.out.println("Parandatud arvutid:");
        for (String key : tulemus.keySet()){
            System.out.println("\t" + key + ": " + tulemus.get(key) + "tk");
        }
        System.out.println("Ootele jäi " + ooteleJai + " arvuti(t).");
    }


    private static HashMap<String, Double> laadiNimedJaTasud() throws  IOException{
        HashMap<String, Double> nimedJaTasud = new HashMap<>();
        String path = ".\\src\\Failid\\tunnitasud.dat";

        try (DataInputStream failist = new DataInputStream(new FileInputStream(path))){
            int töötajateArv = failist.readInt();
            for (int i = 0; i < töötajateArv; i++){
                String nimi = failist.readUTF();
                Double tasu = failist.readDouble();
                nimedJaTasud.put(nimi, tasu);
            }
        }

        return nimedJaTasud;
    }

    private static void salvestaTehrud(List<Arvuti> tehtudTööd) throws  IOException{
        String path = ".\\src\\Failid\\tehtud.dat";
        File tehtud = new File(path);
        if (!tehtud.exists()) {
            tehtud.createNewFile();
        }
        try(DataOutputStream dout = new DataOutputStream(new FileOutputStream(path))){
            dout.writeInt(tehtudTööd.size());
            for (Arvuti arvuti : tehtudTööd) {
                dout.writeUTF(arvuti.getTootja());
                dout.writeUTF(arvuti.getRegistreerimiseAeg().toString());
                dout.writeDouble(arvuti.getArveSumma());
            }
        }
    }

    private static void salvestaOotel(Queue<Arvuti> kiir, Queue<Arvuti> tava) throws Exception{
        String path = ".\\src\\Failid\\ootel.txt";
        File ootel = new File(".\\src\\Failid\\ootel.txt");
        if (!ootel.exists()) {
            ootel.createNewFile();
        }

        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ootel), "UTF-8"))){
            if (!kiir.isEmpty()){
                int kSize = kiir.size();
                for (int i = 0; i < kSize; i++){
                    bw.write(kiir.poll().arvutiToString());
                    bw.write("\r\n");
                }
            }
            if (!tava.isEmpty()){
                int tSize = tava.size();
                for (int k = 0; k < tSize; k++){
                    bw.write(tava.poll().arvutiToString());
                    bw.write("\r\n");
                }
            }
        }
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
