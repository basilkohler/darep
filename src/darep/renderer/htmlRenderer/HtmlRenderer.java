package darep.renderer.htmlRenderer;

import java.util.Arrays;

import darep.Helper;
import darep.renderer.Renderer;
import darep.storage.DataSet;
import darep.storage.DataSetDateComparator;
import darep.storage.Metadata;

public class HtmlRenderer implements Renderer {

	@Override
	public String render(DataSet[] dataSets) {
		StringBuilder html = new StringBuilder();
		
		html.append("<!doctype html>");
		html.append("<html><head>");
		html.append("<title>data-repository html list</title>");
		html.append("<meta charset=\"utf-8\">");
		html.append("</head><body>");
		
		html.append("<h1>data-repository</h1>");
	
		Arrays.sort(dataSets, new DataSetDateComparator());
		int totalSize = 0;
		
		html.append("<table>");
		html.append("<tr>");
		for (String s: Metadata.columnHeaders) {
			html.append("<td>");
			html.append(s);
			html.append("</td>");
		}
		html.append("</tr>");			
		for (DataSet ds : dataSets) {
			Metadata meta = ds.getMetadata();
			totalSize += meta.getFileSize();
			String[] values = meta.toStringArray();
			
			html.append("<tr>");
			for(String s:values) {
				html.append("<td>");
				html.append(s);
				html.append("</td>");
			}
			html.append("</tr>");
		}
		html.append("</table>");
		
		html.append("<p>");
		html.append("(" + dataSets.length + " data sets, ");
		html.append(totalSize + " bytes in total)");
		html.append("</p>");
		
		html.append("</body></html>");
		return html.toString();
	}
}
