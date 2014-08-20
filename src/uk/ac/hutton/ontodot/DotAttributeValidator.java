/*
 * uk.ac.hutton.ontodot: DotAttributeValidator.java
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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <!-- DotAttributeValidator -->
 * 
 * @author Gary Polhill
 */
public class DotAttributeValidator {
  private final String label;
  private final boolean restricted;
  private final Class<?> type;
  private final Set<String> values;

  public static String[] fontnames = new String[] { "Times-Roman", "Times", "Helvetica", "Courier", "Symbol",
    "Times-Roman-Italic", "Times-Italic", "Helvetica-Italic", "Courier-Italic", "Symbol-Italic", "Times-Roman-Bold",
    "Times-Bold", "Helvetica-Bold", "Courier-Bold", "Symbol-Bold", "Times-Roman-Bold-Italic", "Times-Bold-Italic",
    "Helvetica-Bold-Italic", "Courier-Bold-Italic", "Symbol-Bold-Italic" };

  public DotAttributeValidator(String label, Class<?> type) {
    this.label = label;
    this.type = type;
    this.restricted = false;
    this.values = null;
  }

  public DotAttributeValidator(String label, Class<?> type, String... values) {
    this.label = label;
    this.type = type;
    this.restricted = true;
    this.values = new HashSet<String>();

    for(String value: values) {
      this.values.add(value);
    }
  }

  public String label() {
    return label;
  }

  public boolean restricted() {
    return restricted;
  }

  public Class<?> type() {
    return type;
  }

  public Set<String> restrictions() {
    return Collections.unmodifiableSet(values);
  }

  public boolean validate(DotAttribute value) {
    if(!value.label().equals(label)) return false;
    else if(!value.type().equals(type)) return false;
    else if(restricted) {
      return values.contains(value.value());
    }
    else {
      return true;
    }
  }

  public static Map<String, DotAttributeValidator> map(DotAttributeValidator... validators) {
    HashMap<String, DotAttributeValidator> map = new HashMap<String, DotAttributeValidator>();

    for(DotAttributeValidator validator: validators) {
      map.put(validator.label(), validator);
    }

    return map;
  }
}
