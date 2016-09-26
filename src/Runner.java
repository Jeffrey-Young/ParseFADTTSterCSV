import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Runner {

  public static void main(String[] args) {
    
    //read and parse Betas
    File csvDirectory = new File("/ParseFADTTSterCSV/Betas");
    BufferedReader betaReader = null;
    BufferedReader rawReader = null;
    ArrayList<Tract> tracts = new ArrayList<Tract>();
    int index = 0;
    for (File csv : csvDirectory.listFiles()) {
      try {
        betaReader = new BufferedReader(new FileReader(csv));
        tracts.add(new Tract(csv.getName().split("_")[0]));
        tracts.get(index).setArcLengths(betaReader.readLine().split(","));
        betaReader.readLine(); //intercept
        betaReader.readLine(); //sex
        betaReader.readLine(); //age
        tracts.get(index).setExpectedBetas(betaReader.readLine().split(","));
        
        //read from Raw Data
        rawReader = new BufferedReader(new FileReader(new File("/ParseFADTTSterCSV/RawData/" + tracts.get(index).getName() + "_RawData_FA.csv")));
        rawReader.readLine(); //throw away header line
        while(rawReader.ready()){ //uhhh maybe this works?
          tracts.get(index).computeAverageFA(rawReader.readLine().split(","));
        }        
        index++;
      } catch (Exception e){
        e.printStackTrace();
      }
    }
    //cleanup
    try {
      betaReader.close();
      rawReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    //write output
    for (Tract tract : tracts){
      try {
        FileWriter outputCSV = new FileWriter(new File("/ParseFADTTSterCSV/Output/" + tract.getName() + ".csv"));
        //write header line
        outputCSV.write("ArcLength, Average, RelBeta4Exp, ExpCorreBetas, ExpBetas");
        
        
        
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
  }
}
