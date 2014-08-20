/* uk.ac.hutton.ontodot: AbstractNode.java
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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * <!-- AbstractNode -->
 *
 * @author Gary Polhill
 */
public abstract class AbstractNode implements Node {
  private HashSet<Edge> in;
  private HashSet<Edge> out;
  private final String label;
  private final Graph graph;
  
  public AbstractNode(String label, Graph graph) {
    in = new HashSet<Edge>();
    out = new HashSet<Edge>();
    this.label = label;
    this.graph = graph;
  }

  @Override
  public Graph graph() {
    return graph;
  }
  
  /**
   * <!-- in -->
   *
   * @see uk.ac.hutton.ontodot.Node#in()
   * @return
   */
  @Override
  public Set<Edge> in() {
    return in;
  }

  /**
   * <!-- out -->
   *
   * @see uk.ac.hutton.ontodot.Node#out()
   * @return
   */
  @Override
  public Set<Edge> out() {
    return out;
  }

  /**
   * <!-- addInEdge -->
   *
   * @see uk.ac.hutton.ontodot.Node#addInEdge(uk.ac.hutton.ontodot.Edge)
   * @param edge
   */
  @Override
  public void addInEdge(Edge edge) {
    in.add(edge);
  }

  /**
   * <!-- addOutEdge -->
   *
   * @see uk.ac.hutton.ontodot.Node#addOutEdge(uk.ac.hutton.ontodot.Edge)
   * @param edge
   */
  @Override
  public void addOutEdge(Edge edge) {
    out.add(edge);
  }

  /**
   * <!-- label -->
   *
   * @see uk.ac.hutton.ontodot.Node#label()
   * @return
   */
  @Override
  public String label() {
    return label;
  }

  public abstract void write(PrintWriter fp) throws IOException;

}
