import java.time.LocalDateTime;

/**
 * Created by lanev_000 on 5.05.2016.
 */
public class Arvuti {
    String tootja;
    String tooKirjeldus;
    LocalDateTime date;

    public Arvuti(String tootja, String tooKirjeldus, LocalDateTime date) {
        this.tootja = tootja;
        this.tooKirjeldus = tooKirjeldus;
        this.date = date;
    }

    String getTootja(){
        return tootja;
    }

    public boolean onKiirtöö(){
        if (tooKirjeldus.equals("kiirtöö")){
            return true;
        }
        return false;
    }

    public java.time.LocalDateTime getRegistreerimiseAeg(){
        return date;
    }

    public Double getArveSumma(){
        return null;
    }

    public String arvutiToString(){
        return tootja + ";" + tooKirjeldus + "|" + date.toString();
    }

}
