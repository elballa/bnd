package aQute.bnd.differ;

import org.apache.maven.plugins.annotations.Parameter;

import aQute.bnd.service.diff.Type;

public class Exclusion {

	/**
	 * The second part of the output line representing the type of the element.
	 * Examples: <b>CLASS</b>, <b>METHOD</b>, <b>ANNOTATED</b>.
	 */
	@Parameter(required = true)
	private Type	type;

	/**
	 * Get the third part of the output line representing the name of the
	 * element.
	 */
	@Parameter(required = true)
	private String	name;

	/**
	 * A single element of the exclusion list parameter.
	 */
	public Exclusion() {
		super();
	}

	/**
	 * Get the second part of the output line representing the type of the
	 * element. Examples: <b>CLASS</b>, <b>METHOD</b>, <b>ANNOTATED</b>.
	 * 
	 * @return the second part of the output line
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Set the second part of the output line representing the type of the
	 * element. Examples: <b>CLASS</b>, <b>METHOD</b>, <b>ANNOTATED</b>.
	 * 
	 * @param type the second part of the output line
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Get the third part of the output line representing the name of the
	 * element.
	 * 
	 * @return the third part of the output
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the third part of the output line representing the name of the
	 * element.
	 * 
	 * @param name the third part of the output
	 */
	public void setName(String name) {
		this.name = name;
	}

}
