/*
 * uk.ac.hutton.ontodot: Direction.java
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

import uk.ac.macaulay.util.Panic;

/**
 * <!-- Direction -->
 * 
 * @author Gary Polhill
 */
public enum Direction {
  UNDIRECTED, UNIDIRECTIONAL, BIDIRECTIONAL;

  public boolean directed() {
    switch(this) {
    case UNDIRECTED:
      return false;
    default:
      return true;
    }
  }

  public boolean bidirectional() {
    switch(this) {
    case BIDIRECTIONAL:
      return true;
    default:
      return false;
    }
  }

  public String getDOTDirection() {
    switch(this) {
    case UNDIRECTED:
      return "none";
    case UNIDIRECTIONAL:
      return "forward";
    case BIDIRECTIONAL:
      return "both";
    default:
      throw new Panic();
    }
  }
}
