package darep.renderer.tabSeparatedRenderer;

import java.util.Arrays;

import darep.renderer.Renderer;
import darep.repos.DataSet;
import darep.repos.DataSetDateComparator;
import darep.repos.Metadata;

public class TabSeparatedRenderer implements Renderer {
	
	private DataSetDateComparator comp = new DataSetDateComparator();
	
	private final static String headLine =
		"Name	Original Name	Timestamp	Number of Files	Size	Description";

	@Override
	public String render(DataSet[] dataSets) {
		
		Arrays.sort(dataSets, comp);
		
		StringBuilder sb = new StringBuilder();
		sb.append(headLine + '\n');
		
		Metadata m;
		for (DataSet set: dataSets) {
			m = set.getMetadata();
			sb.append(m.getName() + '\t');
			sb.append(m.getOriginalName() + '\t');
			sb.append(m.getTimeStamp().toString() + '\t');
			sb.append(m.getNumberOfFiles() + '\t');
			sb.append(m.getFileSize() + '\t');
			sb.append(m.getDescription());
			sb.append('\n');
		}
		
		return sb.toString();
	}

}
