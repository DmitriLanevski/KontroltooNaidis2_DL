import java.time.LocalDateTime;

/**
 * Created by lanev_000 on 5.05.2016.
 */
public class Arvuti {
    String tootja;
    String tooKirjeldus;
    LocalDateTime date;
    Double arveSumma = null;

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

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String arvutiToString(){
        return tootja + ";" + tooKirjeldus + "|" + date.toString();
    }

    public Double getArveSumma(){
        return arveSumma;
    }

    public void lõpetaTöö(double baasHind) {
        if (onKiirtöö()){
            arveSumma = baasHind + 10 +2;
        } else {
            arveSumma = baasHind + 2;
        }
        arveSumma = Math.floor(arveSumma*100)/100;
    }

}
