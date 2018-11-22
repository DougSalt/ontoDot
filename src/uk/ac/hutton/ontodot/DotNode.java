/*
 * uk.ac.hutton.ontodot: DotNode.java
 * 
 * Copyright (C) 2012 Macaulay Institute
 * 
 * This file is part of ontoDot.
 * 
 * ontoDot is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * ontoDot is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ontoDot. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: Gary Polhill Macaulay Institute, Craigiebuckler,
 * Aberdeen. AB15 8QH. UK. g.polhill@macaulay.ac.uk
 */

package uk.ac.hutton.ontodot;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uk.ac.macaulay.util.Bug;

/**
 * <!-- DotNode -->
 * 
 * A node for the DOT output format
 * 
 * @author Gary Polhill
 */
public class DotNode extends AbstractNode implements PropertyValueNode {

	private List<List<String>> text;

	private Map<String, DotNodeAttribute> attributes;

	private final DotGraph dotGraph;

	/**
	 * @param label
	 */
	public DotNode(String label, DotGraph graph) {
		super(label, graph);
		attributes = new HashMap<String, DotNodeAttribute>();
		text = new LinkedList<List<String>>();
		dotGraph = graph;
	}

	/**
	 * <!-- setAttribute -->
	 * 
	 * Set an attribute for the node
	 * 
	 * @param label
	 * @param value
	 * @throws DotInvalidAttributeException
	 */
	public void setAttribute(String label, String value) throws DotInvalidAttributeException {
		attributes.put(label, new DotNodeAttribute(label, value));
	}

	public void setAttribute(String label, int value) throws DotInvalidAttributeException {
		attributes.put(label, new DotNodeAttribute(label, value));
	}

	public void setAttribute(String label, double value) throws DotInvalidAttributeException {
		attributes.put(label, new DotNodeAttribute(label, value));
	}

	public void setAttribute(String label, Color value) throws DotInvalidAttributeException {
		attributes.put(label, new DotNodeAttribute(label, value));
	}

	/**
	 * <!-- getAttributeValue -->
	 * 
	 * @param label
	 * @return The attribute value of the <code>label</code> or <code>null</code>
	 *         if no value for that label has been defined.
	 */
	public String getAttributeValue(String label) {
		if(attributes.containsKey(label)) {
			return attributes.get(label).value();
		}
		return null;
	}

	/**
	 * <!-- addText -->
	 * 
	 * Add some text to the next row to display in the (record) node
	 * 
	 * @param row
	 *          A row of text to add
	 */
	public void addText(String... row) {
		List<String> list = new LinkedList<String>();

		for(String col: row) {
			list.add(col);
		}

		text.add(list);
	}

	/**
	 * <!-- setPropertyValue -->
	 * 
	 * @see uk.ac.hutton.ontodot.PropertyValueNode#setPropertyValue(java.lang.String,
	 *      java.lang.String)
	 * @param property
	 * @param value
	 */
	public void setPropertyValue(String property, String value) {
		addText(property, value);
	}

	/**
	 * <!-- setPropertyValues -->
	 * 
	 * @see uk.ac.hutton.ontodot.PropertyValueNode#setPropertyValues(java.lang.String,
	 *      java.lang.String[])
	 * @param property
	 * @param values
	 */
	public void setPropertyValues(String property, String... values) {
		for(int i = 0; i < values.length; i++) {
			addText(property, values[i]);
		}
	}

	/**
	 * <!-- escape -->
	 * 
	 * @param str
	 * @return A string suitable for inclusion in a record structure, with spaces,
	 *         etc. escaped
	 */
	public String escape(String str) {
		StringBuffer buff = new StringBuffer();

		char[] c = str.toCharArray();

		for(int i = 0; i < c.length; i++) {
			if(c[i] == '\n') {
				buff.append("\\n");
			}
			else {
				if(c[i] == ' ' || c[i] == '{' || c[i] == '}' || c[i] == '<' || c[i] == '>' || c[i] == '|') {
					buff.append('\\');
				}
				else if(c[i] == '\\' && i < c.length - 1 && c[i + 1] != 'n' && c[i + 1] != 'l' && c[i] != 'r'
						&& c[i + 1] != 'N' && c[i + 1] != 'G') {
					buff.append('\\');
				}
				buff.append(c[i]);
			}
		}

		return buff.toString();
	}

	/**
	 * <!-- getTextString -->
	 * 
	 * @return Formatting of <code>text</code> for record/Mrecord display
	 */
	public String getTextString() {
		if(text.size() == 0) return null;
		
		String nodelabel = attributes.containsKey("label") ? attributes.get("label").value() : label();
		
		StringBuffer buff = new StringBuffer("{" + escape(nodelabel));

		for(List<String> row: text) {
			buff.append("|{");

			boolean rowFirst = true;

			for(String col: row) {
				if(rowFirst) rowFirst = false;
				else {
					buff.append("|");
				}
				buff.append(escape(col));
			}
			buff.append("}");
		}

		buff.append("}");

		return buff.toString();
	}

	/**
	 * <!-- write -->
	 * 
	 * Write the node. This will happen if it has any attributes, or any text and
	 * a record shape.
	 * 
	 * @see uk.ac.hutton.ontodot.Node#write(java.io.PrintWriter)
	 * @param fp
	 * @throws IOException
	 */
	@Override
	public void write(PrintWriter fp) throws IOException {
		String textStr = getTextString();

		if(textStr != null
				&& ((attributes.containsKey("shape") && getAttributeValue("shape").endsWith("record"))
				|| (!attributes.containsKey("shape") && dotGraph.hasNodeDefault("shape") && dotGraph.getNodeDefault("shape")
						.endsWith("record")))) {
			try {
				setAttribute("label", "\"" + textStr + "\"");
			}
			catch(DotInvalidAttributeException e) {
				throw new Bug();
			}
		}
		if(attributes != null && attributes.size() > 0) {
			fp.print(((DotGraph)graph()).indent() + "\"" + label() + "\" [");
			DotAttribute.writeSet(attributes.values(), fp);
			fp.println("];");
		}
	}
}
