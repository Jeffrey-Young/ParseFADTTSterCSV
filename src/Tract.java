import java.util.ArrayList;

public class Tract {

  private String            _name;
  private ArrayList<Double> _arcLengths;
  private ArrayList<Double> _expectedBetas;
  private ArrayList<Double> _averageFAs;

  public Tract(String name) {
    String temp = "";
    int i = 0;
    while (!temp.equals("Betas")) {
      if (i == 1) {
        _name = temp;
      } else {
        _name += "_" + temp;
      }
      temp = name.split("_")[i];
      i++;
    }
    _arcLengths = new ArrayList<Double>();
    _expectedBetas = new ArrayList<Double>();
    _averageFAs = new ArrayList<Double>();
  }

  // throw away first index (label) and parse the rest into doubles
  public void setArcLengths(String[] arcLengths) {
    for (int i = 1; i < arcLengths.length; i++) { // start at i = 1 to throw
                                                  // away label
      _arcLengths.add(Double.parseDouble(arcLengths[i]));
    }
  }

  public void setExpectedBetas(String[] expectedBetas) {
    for (int i = 1; i < expectedBetas.length; i++) { // start at i = 1 to throw
                                                     // away label
      _expectedBetas.add(Double.parseDouble(expectedBetas[i]));
    }
  }

  public void computeAverageFA(String[] averageFA) {
    try {
    double average = 0;
    for (int i = 1; i < averageFA.length; i++) { // start at 1 to throw away
                                                 // label
      average += Double.parseDouble(averageFA[i]);
    }
    average /= (averageFA.length - 1);
    _averageFAs.add(average);
    }
    catch (Exception e){
      System.out.println(this.getName());
      e.printStackTrace();
    }
  }

  public String getName() {
    return _name;
  }

  public int getSize() {
    return _arcLengths.size();
  }

  public ArrayList<Double> getArcLengths() {
    return _arcLengths;
  }

  public ArrayList<Double> getAverageFAs() {
    return _averageFAs;
  }

  public ArrayList<Double> getExpectedBetas() {
    return _expectedBetas;
  }

}
