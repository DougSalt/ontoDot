/* uk.ac.hutton.ontodot: DotGraphAttribute.java
 *
 * Copyright (C) 2012  Macaulay Institute
 *
 * This file is part of ontoDot.
 *
 * ontoDot is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 *
 * ontoDot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with ontoDot. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Contact information:
 *   Gary Polhill
 *   Macaulay Institute, Craigiebuckler, Aberdeen. AB15 8QH. UK.
 *   g.polhill@macaulay.ac.uk
 */
package uk.ac.hutton.ontodot;

import java.awt.Color;
import java.util.Map;
import java.util.Set;

/**
 * <!-- DotGraphAttribute -->
 *
 * @author Gary Polhill
 */
public class DotGraphAttribute extends DotAttribute {

  private static DotAttributeValidator[] graphAttributes = new DotAttributeValidator[] {
    new DotAttributeValidator("bgcolor", Color.class),
    new DotAttributeValidator("center", String.class, "true", "false"),
    new DotAttributeValidator("clusterrank", String.class, "local", "global", "none"),
    new DotAttributeValidator("color", Color.class),
    new DotAttributeValidator("compound", String.class, "true", "false"),
    new DotAttributeValidator("concentrate", String.class, "true", "false"),
    new DotAttributeValidator("fillcolor", Color.class),
    new DotAttributeValidator("fontcolor", Color.class),
    new DotAttributeValidator("fontname", String.class, DotAttributeValidator.fontnames),
    new DotAttributeValidator("fontsize", Integer.class),
    new DotAttributeValidator("label", String.class),
    new DotAttributeValidator("labeljust", String.class, "centered", "l", "r"),
    new DotAttributeValidator("labelloc", String.class, "top", "t", "b"),
    new DotAttributeValidator("layers", String.class),
    new DotAttributeValidator("margin", Double.class),
    new DotAttributeValidator("mclimit", Double.class),
    new DotAttributeValidator("nodesep", Double.class),
    new DotAttributeValidator("nslimit", String.class),
    new DotAttributeValidator("nslimit1", String.class),
    new DotAttributeValidator("ordering", String.class, "out"),
    new DotAttributeValidator("orientation", String.class, "portrait", "landscape"),
    new DotAttributeValidator("page", String.class),
    new DotAttributeValidator("pagedir", String.class, "TR", "RT", "TL", "LT", "BR", "RB", "BL", "LB"),
    new DotAttributeValidator("quantum", Double.class),
    new DotAttributeValidator("rank", String.class, "same", "min", "max", "source", "sink"),
    new DotAttributeValidator("rankdir", String.class, "TB", "BT", "LR", "RL"),
    new DotAttributeValidator("ranksep", Double.class),
    new DotAttributeValidator("ratio", String.class),
    new DotAttributeValidator("remincross", String.class, "true", "false"),
    new DotAttributeValidator("rotate", Integer.class),
    new DotAttributeValidator("samplepoints", Integer.class),
    new DotAttributeValidator("searchsize", Integer.class),
    new DotAttributeValidator("size", Double.class),
    new DotAttributeValidator("style", String.class, "filled"),
    
    new DotAttributeValidator("layout", String.class, "neato", "circo", "twopi", "dot", "fdp", "sfdp", "patchwork"),
    new DotAttributeValidator("overlap", String.class, "false", "scalexy"),
    new DotAttributeValidator("splines", String.class, "true", "false"),
  };

  private static Map<String, DotAttributeValidator> attributeMap = DotAttributeValidator.map(graphAttributes);

  /**
   * @param label
   * @param value
   * @throws DotInvalidAttributeException 
   */
  public DotGraphAttribute(String label, String value) throws DotInvalidAttributeException {
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
  public DotGraphAttribute(String label, int value) throws DotInvalidAttributeException {
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
  public DotGraphAttribute(String label, double value) throws DotInvalidAttributeException {
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
  public DotGraphAttribute(String label, Color value) throws DotInvalidAttributeException {
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
