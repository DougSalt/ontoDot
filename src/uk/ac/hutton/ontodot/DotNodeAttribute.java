/*
 * uk.ac.hutton.ontodot: DotNodeAttribute.java
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
 * <!-- DotNodeAttribute -->
 * 
 * @author Gary Polhill
 */
public class DotNodeAttribute extends DotAttribute {
  private static DotAttributeValidator[] nodeAttributes = new DotAttributeValidator[] {
    new DotAttributeValidator("color", Color.class),
    new DotAttributeValidator("distortion", Double.class),
    new DotAttributeValidator("fillcolor", Color.class),
    new DotAttributeValidator("fixedsize", String.class, "true", "false"),
    new DotAttributeValidator("fontcolor", Color.class),
    new DotAttributeValidator("fontname", String.class, DotAttributeValidator.fontnames),
    new DotAttributeValidator("fontsize", Integer.class),
    new DotAttributeValidator("group", String.class),
    new DotAttributeValidator("height", Double.class),
    new DotAttributeValidator("label", String.class),
    new DotAttributeValidator("layer", String.class),
    new DotAttributeValidator("orientation", Double.class),
    new DotAttributeValidator("peripheries", Integer.class),
    new DotAttributeValidator("regular", String.class, "true", "false"),
    new DotAttributeValidator("shape", String.class, "box", "polygon", "ellipse", "oval", "circle", "point", "egg",
        "triangle", "diamond", "trapezium", "parallelogram", "house", "pentagon", "hexagon", "septagon", "octagon",
        "doublecircle", "doubleoctagon", "tripleoctagon", "invtriangle", "invtrapezium", "invhouse", "Mdiamond",
        "Msquare", "Mcircle", "rect", "rectangle", "square", "note", "tab", "folder", "box3d", "component", "record",
        "Mrecord", "plaintext"),
    new DotAttributeValidator("sides", Integer.class),
    new DotAttributeValidator("skew", Double.class),
    new DotAttributeValidator("style", String.class, "solid", "dashed", "dotted", "bold", "invis", "filled",
        "diagonals", "rounded"), new DotAttributeValidator("width", Double.class),
    new DotAttributeValidator("z", Double.class) };

  private static Map<String, DotAttributeValidator> attributeMap = DotAttributeValidator.map(nodeAttributes);

  /**
   * @param label
   * @param value
   * @throws DotInvalidAttributeException
   */
  public DotNodeAttribute(String label, String value) throws DotInvalidAttributeException {
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
  public DotNodeAttribute(String label, int value) throws DotInvalidAttributeException {
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
  public DotNodeAttribute(String label, double value) throws DotInvalidAttributeException {
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
  public DotNodeAttribute(String label, Color value) throws DotInvalidAttributeException {
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
