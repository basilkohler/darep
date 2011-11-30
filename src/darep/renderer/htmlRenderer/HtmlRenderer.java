package darep.renderer.htmlRenderer;

import darep.renderer.Renderer;
import darep.storage.DataSet;

public class HtmlRenderer implements Renderer {

	@Override
	public String render(DataSet[] dataSets) {
		StringBuilder html = new StringBuilder();
		
		html.append("<!doctype html>");
		html.append("<html><head>");
		html.append("<title>data-repository html list</title>");
		html.append("<meta charset=\"utf-8\">");
		html.append("</head><body>");
	
		// TODO insert html list here
		html.append("awesome list is awesome");
		
		html.append("</body></html>");
		return html.toString();
	}

}
