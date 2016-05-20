import java.time.LocalDateTime;

/**
 * Created by lanev_000 on 5.05.2016.
 */
public class VäliseMonitorigaArvuti extends Arvuti{
    public VäliseMonitorigaArvuti(String tootja, String tooKirjeldus, LocalDateTime date) {
        super(tootja, tooKirjeldus, date);
    }

    @Override
    public String arvutiToString() {
        return tootja + ";" + tooKirjeldus + ";" + "monitoriga" + "|" + date;
    }
}
