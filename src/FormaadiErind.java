import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by lanev_000 on 5.05.2016.
 */
public class FormaadiErind extends RuntimeException {
    private String viganeRida;
    private int veaPositsioon;

    public FormaadiErind(String viganeRida, int veaPositsioon){
        super("Vigane sisend! " + "Viga " + veaPositsioon + ". valjas!");
        this.viganeRida = viganeRida;
        this.veaPositsioon = veaPositsioon;
    }

    public String getViganeRida() {
        return viganeRida;
    }

    public int getVeaPositsioon(){
        return veaPositsioon;
    }
}
