import java.time.LocalDateTime;

/**
 * Created by lanev_000 on 5.05.2016.
 */
public class VäliseMonitorigaArvuti extends Arvuti{
    public VäliseMonitorigaArvuti(String tootja, String tooKirjeldus, LocalDateTime date) {
        super(tootja, tooKirjeldus, date);
    }

    @Override
    public void lõpetaTöö(double baasHind) {
        if (onKiirtöö()){
            arveSumma = baasHind + 10 + 3;
        } else {
            arveSumma = baasHind + 3;
        }
        arveSumma = Math.floor(arveSumma*100)/100;
    }

    @Override
    public String arvutiToString() {
        return tootja + ";" + tooKirjeldus + ";" + "monitoriga" + "|" + date;
    }
}
