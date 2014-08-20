/*
 * uk.ac.hutton.ontodot: DotInvalidAttributeException.java
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

/**
 * <!-- DotInvalidAttributeException -->
 * 
 * @author Gary Polhill
 */
public class DotInvalidAttributeException extends Exception {

  /**
   * Serialisation ID
   */
  private static final long serialVersionUID = 4789974844537695328L;

  public DotInvalidAttributeException(Class<? extends DotAttribute> type, String label, String value) {
    super("Invalid DOT attribute for " + getTypeStr(type) + ": " + label + " = " + value);
  }

  public DotInvalidAttributeException(Class<? extends DotAttribute> type, String label, int value) {
    super("Invalid DOT attribute for " + getTypeStr(type) + ": " + label + " = " + value);
  }

  public DotInvalidAttributeException(Class<? extends DotAttribute> type, String label, double value) {
    super("Invalid DOT attribute for " + getTypeStr(type) + ": " + label + " = " + value);
  }

  public DotInvalidAttributeException(Class<? extends DotAttribute> type, String label, Color value) {
    super("Invalid DOT attribute for " + getTypeStr(type) + ": " + label + " = " + value);
  }

  private static String getTypeStr(Class<? extends DotAttribute> type) {
    String typeStr = "unknown DOT item";

    if(type.equals(DotNodeAttribute.class)) {
      typeStr = "node";
    }
    else if(type.equals(DotEdgeAttribute.class)) {
      typeStr = "edge";
    }
    else if(type.equals(DotGraphAttribute.class)) {
      typeStr = "graph";
    }
    return typeStr;
  }

}
