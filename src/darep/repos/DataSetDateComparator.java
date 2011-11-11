package darep.repos;

import java.util.Comparator;
import java.util.Date;

/**
 * This class can be user to sort DataSets by Date, newest Data Sets last.
 */
public class DataSetDateComparator implements Comparator<DataSet> {

	@Override
	public int compare(DataSet dataSet1, DataSet dataSet2) {
		Date d1 = dataSet1.getMetadata().getTimeStamp();
		Date d2 = dataSet2.getMetadata().getTimeStamp();
		
		return (int)(d1.getTime() - d2.getTime());
	}

}
