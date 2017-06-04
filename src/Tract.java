import java.util.ArrayList;

public class Tract {

	private String _name;
	private ArrayList<Double> _arcLengths;
	private ArrayList<Double> _betas;
	private ArrayList<Double> _averageFAs;
	private ArrayList<ArrayList<Double>> _covariates;

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
		_covariates = new ArrayList<ArrayList<Double>>();
		_arcLengths = new ArrayList<Double>();
		_betas = new ArrayList<Double>();
		_averageFAs = new ArrayList<Double>();
	}

	// throw away first index (label) and parse the rest into doubles
	public void setArcLengths(String[] arcLengths) {
		for (int i = 1; i < arcLengths.length; i++) { // start at i = 1 to throw
														// away label
			_arcLengths.add(Double.parseDouble(arcLengths[i]));
		}
	}

	public void addCovariate(String[] covariate) {
		ArrayList<Double> toAdd = new ArrayList<Double>();
		for (int i = 1; i < covariate.length; i++) {
			toAdd.add(Double.parseDouble(covariate[i]));
		}
		_covariates.add(toAdd);
	}

	public void setBetas(String[] expectedBetas) {
		for (int i = 1; i < expectedBetas.length; i++) { // start at i = 1 to
															// throw
															// away label
			_betas.add(Double.parseDouble(expectedBetas[i]));
		}
		_covariates.add(_betas);
	}

	public void computeAverageFA(String[] averageFA) {
		try {
			double average = 0;
			int size = averageFA.length - 1; // -1 because first row is header
			for (int i = 1; i < averageFA.length; i++) { // start at 1 to throw
															// away
															// label
				if (averageFA[i].contains("nan")) { // skip nans
					size--;
					continue;
				}
				average += Double.parseDouble(averageFA[i]);
			}
			average /= size;
			_averageFAs.add(average);
		} catch (Exception e) {
			System.out.println(this.getName());
			e.printStackTrace();
		}
	}
	
	public double getCovariateSum(int index) {
		double sum = 0;
		for (ArrayList<Double> covariate : _covariates) {
			sum += covariate.get(index);
		}
		return sum;
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

	public ArrayList<Double> getBetas() {
		return _betas;
	}

}
