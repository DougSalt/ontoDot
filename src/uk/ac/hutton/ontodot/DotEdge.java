/*
 * uk.ac.hutton.ontodot: DotEdge.java
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
import java.util.Map;

import uk.ac.macaulay.util.Bug;

/**
 * <!-- DotEdge -->
 * 
 * @author Gary Polhill
 */
public class DotEdge extends AbstractEdge {
  private Map<String, DotEdgeAttribute> attributes = new HashMap<String, DotEdgeAttribute>();

  /**
   * @param in
   * @param out
   */
  public DotEdge(Node in, Node out, DotGraph graph) {
    super(in, out, graph);
  }

  /**
   * @param in
   * @param out
   * @param label
   */
  public DotEdge(Node in, Node out, String label, DotGraph graph) {
    super(in, out, label, graph);
  }

  /**
   * @param in
   * @param out
   * @param label
   * @param direction
   */
  public DotEdge(Node in, Node out, String label, Direction direction, DotGraph graph) {
    super(in, out, label, direction, graph);
  }

  /**
   * @param in
   * @param out
   * @param label
   * @param direction
   * @param inPortLabel
   * @param outPortLabel
   */
  public DotEdge(Node in, Node out, String label, Direction direction, String inPortLabel, String outPortLabel,
      DotGraph graph) {
    super(in, out, label, direction, inPortLabel, outPortLabel, graph);
  }

  /**
   * <!-- setAttribute -->
   * 
   * @param label
   * @param value
   * @throws DotInvalidAttributeException
   */
  public void setAttribute(String label, String value) throws DotInvalidAttributeException {
    attributes.put(label, new DotEdgeAttribute(label, value));
  }

  public void setAttribute(String label, int value) throws DotInvalidAttributeException {
    attributes.put(label, new DotEdgeAttribute(label, value));
  }

  public void setAttribute(String label, double value) throws DotInvalidAttributeException {
    attributes.put(label, new DotEdgeAttribute(label, value));
  }

  public void setAttribute(String label, Color value) throws DotInvalidAttributeException {
    attributes.put(label, new DotEdgeAttribute(label, value));
  }

  /**
   * <!-- getAttributeValue -->
   * 
   * @param label
   * @return
   */
  public String getAttributeValue(String label) {
    if(attributes.containsKey(label)) {
      return attributes.get(label).value();
    }
    else {
      return null;
    }
  }

  /**
   * <!-- write -->
   * 
   * @see uk.ac.hutton.ontodot.Edge#write(java.io.PrintWriter)
   * @param fp
   * @throws IOException
   */
  @Override
  public void write(PrintWriter fp) throws IOException {
    try {
      if(direction() != null && !unidirectional() && !attributes.containsKey("dir")) {
        setAttribute("dir", direction().getDOTDirection());
      }

      if(label() != null && !attributes.containsKey("label")) {
        setAttribute("label", "\"" + label() + "\"");
      }
      
      if(inPortLabel() != null && !attributes.containsKey("taillabel")) {
        setAttribute("taillabel", "\"" + inPortLabel() + "\"");
      }
      
      if(outPortLabel() != null && !attributes.containsKey("headlabel")) {
        setAttribute("headlabel", "\"" + outPortLabel() + "\"");
      }
    }
    catch(DotInvalidAttributeException e) {
      throw new Bug();
    }

    fp.print(((DotGraph)graph()).indent() + "\"" + in().label() + "\" -> \"" + out().label() + "\"");

    if(attributes.size() > 0) {
      fp.print(" [");
      DotAttribute.writeSet(attributes.values(), fp);
      fp.print("]");
    }

    fp.println(";");
  }

}
