import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Runner {
  
  
  public static void main(String[] args) {
	   Study study = new Study(5.835714, 9.136364, "Wisconsin"); // Wisconsin
	  //Study study = new Study(7.551149, 11.53155, "Emory"); // Emory


    // read and parse Betas
    File csvDirectory = new File(study.name + "/Betas");
    BufferedReader betaReader = null;
    BufferedReader rawReader = null;
    ArrayList<Tract> tracts = new ArrayList<Tract>();
    int index = 0;
    for (File csv : csvDirectory.listFiles()) {
      try {
        betaReader = new BufferedReader(new FileReader(csv));
        tracts.add(new Tract(csv.getName()));
        tracts.get(index).setArcLengths(betaReader.readLine().split(",")); // arc length
        tracts.get(index).addCovariate(betaReader.readLine().split(",")); // intercept
        tracts.get(index).addCovariate(betaReader.readLine().split(",")); // sex
       tracts.get(index).addCovariate(betaReader.readLine().split(",")); // age *****comment out this line for emory because it has one less covariate (no age)
        
        tracts.get(index).setBetas(betaReader.readLine().split(",")); //normalized dose
        // read from Raw Data
        rawReader = new BufferedReader(new FileReader(new File(study.name + "/RawData/fa_" + tracts.get(index).getName() + ".csv")));
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
        FileWriter outputCSV = new FileWriter(new File(study.name + "/Output/" + tract.getName() + ".csv"));
        // write header line
        outputCSV.write("ArcLength, AverageFA, Beta, RelBeta, HighExposureBeta\n");

        // write line
        for (int i = 0; i < tract.getSize(); i++) {
        	//System.out.println(tract.getCovariateSum(i));
          outputCSV.write(tract.getArcLengths().get(i) + "," + tract.getAverageFAs().get(i) + "," + tract.getBetas().get(i) + "," + (tract.getBetas().get(i) / tract.getCovariateSum(i)) + ","
              + ((tract.getBetas().get(i) / tract.getCovariateSum(i)) / study.normalizedTotalKetamineIsofluraneMeanEntireCohort * study.normalizedTotalKetamineIsofluraneHighExposure) + "\n");
        }
        outputCSV.flush();
        outputCSV.close();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }
}
