import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Runner {

  public static final String STUDY = "Emory";
  // 5.26 for Emory
  // 3.04 for Wisconsin
  public static final double STUDY_CONSTANT = 5.26;
  
  public static void main(String[] args) {
    

    // read and parse Betas
    File csvDirectory = new File(STUDY + "/Betas");
    BufferedReader betaReader = null;
    BufferedReader rawReader = null;
    ArrayList<Tract> tracts = new ArrayList<Tract>();
    int index = 0;
    for (File csv : csvDirectory.listFiles()) {
      try {
        betaReader = new BufferedReader(new FileReader(csv));
        tracts.add(new Tract(csv.getName()));
        tracts.get(index).setArcLengths(betaReader.readLine().split(",")); // arc length
        betaReader.readLine(); // intercept
        betaReader.readLine(); // sex
        // betaReader.readLine(); // age
        tracts.get(index).setExpectedBetas(betaReader.readLine().split(","));
        // read from Raw Data
        rawReader = new BufferedReader(new FileReader(new File(STUDY + "/RawData/fa_" + tracts.get(index).getName() + ".csv")));
        rawReader.readLine(); // throw away header line
        while (rawReader.ready()) { // uhhh maybe this works?
          tracts.get(index).computeAverageFA(rawReader.readLine().split(","));
        }
        index++;
      } catch (Exception e) {
        tracts.get(index).getName();
        e.printStackTrace();
      }
    }
    // cleanup
    try {
      betaReader.close();
      rawReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // write output
    for (Tract tract : tracts) {
      try {
        FileWriter outputCSV = new FileWriter(new File(STUDY + "/Output/" + tract.getName() + ".csv"));
        // write header line
        outputCSV.write("ArcLength, Average, RelBeta, ExpCorreBetas, ExpBetas\n");

        // write line
        for (int i = 0; i < tract.getSize(); i++) {
          outputCSV.write(tract.getArcLengths().get(i) + "," + tract.getAverageFAs().get(i) + "," + (tract.getExpectedBetas().get(i) * STUDY_CONSTANT) / tract.getAverageFAs().get(i) + ","
              + (tract.getExpectedBetas().get(i) * STUDY_CONSTANT) + "," + tract.getExpectedBetas().get(i) + "\n");
        }
        outputCSV.flush();
        outputCSV.close();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }
}
