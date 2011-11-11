package darep.renderer.prettyRenderer;

import java.util.Arrays;

import darep.Helper;
import darep.renderer.Renderer;
import darep.storage.DataSet;
import darep.storage.DataSetDateComparator;
import darep.storage.Metadata;

public class PrettyRenderer implements Renderer {
	
	private final static boolean ALIGN_LEFT = Helper.ALIGN_LEFT;
	private final static boolean ALIGN_RIGHT = Helper.ALIGN_RIGHT;
	
	private String[] columnHeaders = new String[] {
			"Name",
			"Original Name",
			"Timestamp",
			"Number of Files",
			"Size",
			"Description"
	};
	
	private boolean[] alignment = new boolean[] {
			ALIGN_LEFT,
			ALIGN_LEFT,
			ALIGN_LEFT,
			ALIGN_RIGHT,
			ALIGN_RIGHT,
			ALIGN_LEFT
	};

	@Override
	public String render(DataSet[] dataSets) {
		
		Arrays.sort(dataSets, new DataSetDateComparator());
		
		int[] colWidths = calculateColumnWidths(dataSets);
		
		StringBuilder sb = new StringBuilder();
		sb.append(generateHeaderLine(colWidths));
		
		int totalSize = 0;
		for (DataSet ds : dataSets) {
			
			Metadata meta = ds.getMetadata();
			String[] values = makeStringValues(meta);
			
			totalSize += meta.getFileSize();
			
			for (int i = 0; i < values.length; i++) {
				sb.append(Helper.stringToLength(values[i], colWidths[i], alignment[i]) + "|");
			}
			sb.append("\n");
		}
		sb.append("(" + dataSets.length + " data sets, " + totalSize + " bytes in total)");
		
		
		return sb.toString();
	}

	private Object generateHeaderLine(int[] colWidths) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < columnHeaders.length; i++) {
			sb.append(Helper.stringToLength(columnHeaders[i], colWidths[i], ALIGN_LEFT) + "|");
		}
		sb.append("\n");
		
		for (int width: colWidths) {
			sb.append(Helper.stringTimes("-", width));
			sb.append("+");
		}
		sb.append("\n");
		
		return sb.toString();
	}

	private String[] makeStringValues(Metadata metadata) {
		String[] values = new String[columnHeaders.length];
		
		values[0] = metadata.getName();
		values[1] = metadata.getOriginalName();
		values[2] = metadata.getTimeStamp().toString();
		values[3] = String.valueOf(metadata.getNumberOfFiles());
		values[4] = String.valueOf(metadata.getFileSize());
		values[5] = metadata.getDescription();
		
		return values;
	}

	private int[] calculateColumnWidths(DataSet[] dataSets) {
		int[] colWidths = new int[columnHeaders.length];
		
		for (int i = 0; i < columnHeaders.length; i++) {
			colWidths[i] = columnHeaders[i].length();
		}
		
		Metadata meta;
		for (DataSet ds: dataSets) {
			meta = ds.getMetadata();
			colWidths[0] = Math.max(meta.getName().length(), colWidths[0]);
			colWidths[1] = Math.max(meta.getOriginalName().length(), colWidths[1]);
			colWidths[2] = Math.max(meta.getTimeStamp().toString().length(),
								colWidths[2]);
			colWidths[3] = Math.max(
					String.valueOf(meta.getNumberOfFiles()).length(),
					colWidths[3]);
			colWidths[4] = Math.max(String.valueOf(meta.getFileSize()).length(),
									colWidths[4]);
			colWidths[5] = Math.max(meta.getDescription().length(),
									colWidths[5]);
		}
		
		return colWidths;
	}

}
