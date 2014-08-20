/*
 * uk.ac.hutton.ontodot: DotAttribute.java
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
import java.util.Collection;
import java.util.Set;

/**
 * <!-- DotAttribute -->
 * 
 * @author Gary Polhill
 */
public abstract class DotAttribute {
  private final String label;
  private final String value;
  private final Class<?> type;

  public DotAttribute(String label, String value) {
    this.label = label;
    this.value = value;
    this.type = String.class;
  }

  public DotAttribute(String label, int value) {
    this.label = label;
    this.value = Integer.toString(value);
    this.type = Integer.class;
  }

  public DotAttribute(String label, double value) {
    this.label = label;
    this.value = Double.toString(value);
    this.type = Double.class;
  }

  public DotAttribute(String label, Color value) {
    this.label = label;
    this.value =
      "\"#" + String.format("%02X", value.getRed())
        + String.format("%02X", value.getGreen()) + String.format("%02X", value.getBlue()) + "\"";
    this.type = Color.class;
  }

  public void write(PrintWriter fp) throws IOException {
    fp.print(label + " = " + value);
  }

  public String label() {
    return label;
  }

  public String value() {
    return value;
  }

  public Class<?> type() {
    return type;
  }

  public static void writeSet(Collection<? extends DotAttribute> attributes, PrintWriter fp) throws IOException {
    boolean first = true;

    for(DotAttribute attribute: attributes) {
      if(first) {
        first = false;
      }
      else {
        fp.print(", ");
      }
      attribute.write(fp);
    }
  }

  public abstract boolean restricted();

  public abstract Set<String> restrictions();

  public abstract boolean valid();

  public abstract Class<?> validType();

}
