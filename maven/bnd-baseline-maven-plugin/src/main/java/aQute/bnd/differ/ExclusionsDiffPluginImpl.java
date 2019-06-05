package aQute.bnd.differ;

import static aQute.bnd.service.diff.Delta.CHANGED;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

import aQute.bnd.osgi.Analyzer;
import aQute.bnd.service.diff.Tree;
import aQute.bnd.service.diff.Type;

public class ExclusionsDiffPluginImpl extends DiffPluginImpl {
	private List<Exclusion> exclusions;

	/**
	 * The list of elements to exclude from tree.
	 * 
	 * @return the list of elements to exclude from tree
	 */
	public List<Exclusion> getExclusions() {
		return exclusions;
	}

	/**
	 * Setter of the list of elements to exclude from tree.
	 * 
	 * @param exclusions the list of elements to exclude from tree
	 */
	public void setExclusions(List<Exclusion> exclusions) {
		this.exclusions = exclusions;
	}

	@Override
	public Tree tree(Analyzer analyzer) throws Exception {
		return bundleElement(analyzer);
	}

	/**
	 * Create an element representing a bundle from the Jar.
	 * 
	 * @param analyzer the analyzer generated from the Jar
	 * @return the elements that should be compared
	 * @throws Exception
	 */
	private Element bundleElement(Analyzer analyzer) throws Exception {
		List<Element> result = new ArrayList<>();

		Manifest manifest = analyzer.getJar()
			.getManifest();

		if (manifest != null) {
			Element apis = JavaElement.getAPI(analyzer);

			ElementWrapper purgedElement = new ElementWrapper(apis, exclusions);
			purgedElement = purgedElement.purge();
			result.add(purgedElement);

			result.add((Element) ReflectionUtil.callMethod("manifestElement", this, new Class[] {
				Manifest.class
			}, manifest));
		}
		result.add((Element) ReflectionUtil.callMethod("resourcesElement", this, new Class[] {
			Analyzer.class
		}, analyzer));
		return new Element(Type.BUNDLE, analyzer.getJar()
			.getName(), result, CHANGED, CHANGED, null);
	}

}
