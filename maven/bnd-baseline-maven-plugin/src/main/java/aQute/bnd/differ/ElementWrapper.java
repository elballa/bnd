package aQute.bnd.differ;

import java.util.ArrayList;
import java.util.List;

import aQute.bnd.service.diff.Tree;
import aQute.bnd.service.diff.Type;

/**
 * @author Alessandro Ballarin
 */
public class ElementWrapper extends Element {

	ElementWrapper[]	childrenWrapper;
	Element[]			prova;
	List<Exclusion>		exclusions;

	public ElementWrapper(Element d, List<Exclusion> exclusions) {
		super(d.serialize());
		if (d.getChildren() != null) {
			List<ElementWrapper> childs = new ArrayList<>();
			for (Tree child : d.getChildren()) {
				childs.add(new ElementWrapper((Element) child, exclusions));
			}
			this.childrenWrapper = childs.toArray(new ElementWrapper[childs.size()]);
		} else {
			this.childrenWrapper = new ElementWrapper[0];
		}
		this.exclusions = exclusions;
	}

	private boolean isInExclusions(Type type, String name) {
		for (Exclusion exclusion : this.exclusions) {
			if (exclusion.getType()
				.equals(type)
				&& exclusion.getName()
					.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isToExclude() {
		if (isInExclusions(this.getType(), this.getName()) || this.getType()
			.equals(Type.VERSION)) {
			// it is in the exclusions or is a VERSION or is an UNCHANGED
			return true;
		}
		return false;
	}

	public ElementWrapper purge() {
		if (this.isToExclude()) {
			return null;
		}
		if (this.getChildren().length != 0) {
			List<ElementWrapper> newList = new ArrayList<>();
			for (ElementWrapper childDiff : this.getChildrenWrapper()) {
				ElementWrapper child = childDiff.purge();
				if (child != null) {
					newList.add(child);
				}
			}
			if (newList.isEmpty()) {
				return null;
			} else {
				this.setChildrenWrapper(newList.toArray(new ElementWrapper[newList.size()]));
				return this;
			}
		} else {
			this.setChildrenWrapper(new ElementWrapper[0]);
			return this;
		}
	}

	public ElementWrapper[] getChildrenWrapper() {
		return childrenWrapper;
	}

	public void setChildrenWrapper(ElementWrapper[] childrenWrapper) {
		this.childrenWrapper = childrenWrapper;
		superCopy(this.childrenWrapper);
	}

	private void superCopy(ElementWrapper[] b) {
		Element[] c = new Element[b.length];
		for (int i = 0; i < c.length; i++) {
			c[i] = b[i];
		}
		ReflectionUtil.setField(this, "children", c);
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
