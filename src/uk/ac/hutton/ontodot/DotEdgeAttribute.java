/*
 * uk.ac.hutton.ontodot: DotEdgeAttribute.java
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
import java.util.Map;
import java.util.Set;

/**
 * <!-- DotEdgeAttribute -->
 * 
 * @author Gary Polhill
 */
public class DotEdgeAttribute extends DotAttribute {
  private static String[] arrowstyles = new String[] { "normal", "dot", "odot", "inv", "invdot", "invodot", "none",
    "tee", "empty", "invempty", "diamond", "odiamond", "ediamond", "crow", "box", "obox", "open", "halfopen", "vee" };
  private static String[] ports = new String[] { "n", "ne", "e", "se", "s", "sw", "w", "nw" };
  private static DotAttributeValidator[] edgeAttributes = new DotAttributeValidator[] {
    new DotAttributeValidator("arrowhead", String.class, arrowstyles),
    new DotAttributeValidator("arrowsize", Double.class),
    new DotAttributeValidator("arrowtail", String.class, arrowstyles), new DotAttributeValidator("color", Color.class),
    new DotAttributeValidator("constraint", String.class, "true", "false"),
    new DotAttributeValidator("decorate", String.class, "true", "false"),
    new DotAttributeValidator("dir", String.class, "forward", "back", "both", "none"),
    new DotAttributeValidator("fontcolor", Color.class),
    new DotAttributeValidator("fontname", String.class, DotAttributeValidator.fontnames),
    new DotAttributeValidator("fontsize", Integer.class), new DotAttributeValidator("headlabel", String.class),
    new DotAttributeValidator("headport", String.class, ports), new DotAttributeValidator("label", String.class),
    new DotAttributeValidator("labelangle", Double.class), new DotAttributeValidator("labeldistance", Double.class),
    new DotAttributeValidator("labelfloat", String.class, "true", "false"),
    new DotAttributeValidator("labelfontcolor", Color.class),
    new DotAttributeValidator("labelfontname", String.class, DotAttributeValidator.fontnames),
    new DotAttributeValidator("labelfontsize", Integer.class), new DotAttributeValidator("layer", String.class),
    new DotAttributeValidator("lhead", String.class), new DotAttributeValidator("ltail", String.class),
    new DotAttributeValidator("minlen", Integer.class), new DotAttributeValidator("samehead", String.class),
    new DotAttributeValidator("sametail", String.class),
    new DotAttributeValidator("style", String.class, "solid", "dashed", "dotted", "bold", "invis", "tapered"),
    new DotAttributeValidator("taillabel", String.class), new DotAttributeValidator("tailport", String.class, ports),
    new DotAttributeValidator("weight", Integer.class) };

  private static Map<String, DotAttributeValidator> attributeMap = DotAttributeValidator.map(edgeAttributes);

  /**
   * @param label
   * @param value
   * @throws DotInvalidAttributeException
   */
  public DotEdgeAttribute(String label, String value) throws DotInvalidAttributeException {
    super(label, value);
    if(!valid()) {
      throw new DotInvalidAttributeException(getClass(), label, value);
    }
  }

  /**
   * @param label
   * @param value
   * @throws DotInvalidAttributeException
   */
  public DotEdgeAttribute(String label, int value) throws DotInvalidAttributeException {
    super(label, value);
    if(!valid()) {
      throw new DotInvalidAttributeException(getClass(), label, value);
    }
  }

  /**
   * @param label
   * @param value
   * @throws DotInvalidAttributeException
   */
  public DotEdgeAttribute(String label, double value) throws DotInvalidAttributeException {
    super(label, value);
    if(!valid()) {
      throw new DotInvalidAttributeException(getClass(), label, value);
    }
  }

  /**
   * @param label
   * @param value
   * @throws DotInvalidAttributeException
   */
  public DotEdgeAttribute(String label, Color value) throws DotInvalidAttributeException {
    super(label, value);
    if(!valid()) {
      throw new DotInvalidAttributeException(getClass(), label, value);
    }
  }

  /**
   * <!-- restricted -->
   * 
   * @see uk.ac.hutton.ontodot.DotAttribute#restricted()
   * @return
   */
  @Override
  public boolean restricted() {
    return restricted(label());
  }

  public static boolean restricted(String label) {
    if(attributeMap.containsKey(label)) {
      return attributeMap.get(label).restricted();
    }
    else {
      return false;
    }
  }

  /**
   * <!-- restrictions -->
   * 
   * @see uk.ac.hutton.ontodot.DotAttribute#restrictions()
   * @return
   */
  @Override
  public Set<String> restrictions() {
    return restrictions(label());
  }

  public static Set<String> restrictions(String label) {
    if(attributeMap.containsKey(label)) {
      return attributeMap.get(label).restrictions();
    }
    else {
      return null;
    }
  }

  /**
   * <!-- valid -->
   * 
   * @see uk.ac.hutton.ontodot.DotAttribute#valid()
   * @return
   */
  @Override
  public boolean valid() {
    if(attributeMap.containsKey(label())) {
      return attributeMap.get(label()).validate(this);
    }
    else {
      return false;
    }
  }

  /**
   * <!-- validType -->
   * 
   * @see uk.ac.hutton.ontodot.DotAttribute#validType()
   * @return
   */
  @Override
  public Class<?> validType() {
    return validType(label());
  }

  public static Class<?> validType(String label) {
    if(attributeMap.containsKey(label)) {
      return attributeMap.get(label).type();
    }
    else {
      return null;
    }
  }

}
