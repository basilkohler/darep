package darep.renderer;

import darep.storage.DataSet;

/**
 * Used to generate a textual List of all DataSets in different formats
 * (e.g. tab separated or ascii-table)
 *
 */
public interface Renderer {
	
	/**
	 * Should return a string with a list of all dataSets.
	 * @param dataSets
	 * @return
	 */
	public String render(DataSet[] dataSets);
	
}
