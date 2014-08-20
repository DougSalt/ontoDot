/*
 * uk.ac.hutton.ontodot: AbstractEdge.java
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

import java.io.IOException;
import java.io.PrintWriter;

/**
 * <!-- AbstractEdge -->
 * 
 * @author Gary Polhill
 */
public abstract class AbstractEdge implements Edge {
  private Direction direction;
  private String label;
  private String inPortLabel;
  private String outPortLabel;
  private final Node in;
  private final Node out;
  private final Graph graph;

  public AbstractEdge(Node in, Node out, Graph graph) {
    direction = null;
    label = null;
    inPortLabel = null;
    outPortLabel = null;
    this.in = in;
    this.out = out;
    this.graph = graph;
  }

  public AbstractEdge(Node in, Node out, String label, Graph graph) {
    this(in, out, graph);
    this.label = label;
  }

  public AbstractEdge(Node in, Node out, String label, Direction direction, Graph graph) {
    this(in, out, label, graph);
    this.direction = direction;
  }

  public AbstractEdge(Node in, Node out, String label, Direction direction, String inPortLabel, String outPortLabel,
      Graph graph) {
    this(in, out, label, direction, graph);
    this.inPortLabel = inPortLabel;
    this.outPortLabel = outPortLabel;
  }
  
  @Override
  public Graph graph() {
    return graph;
  }

  /**
   * <!-- directed -->
   * 
   * @see uk.ac.hutton.ontodot.Edge#directed()
   * @return
   */
  @Override
  public boolean directed() {
    return direction.directed();
  }

  /**
   * <!-- bidirectional -->
   * 
   * @see uk.ac.hutton.ontodot.Edge#bidirectional()
   * @return
   */
  @Override
  public boolean bidirectional() {
    return direction.bidirectional();
  }

  /**
   * <!-- direction -->
   * 
   * @see uk.ac.hutton.ontodot.Edge#direction()
   * @return
   */
  @Override
  public Direction direction() {
    return direction;
  }

  /**
   * <!-- unidirectional -->
   * 
   * @see uk.ac.hutton.ontodot.Edge#unidirectional()
   * @return
   */
  @Override
  public boolean unidirectional() {
    return direction == Direction.UNIDIRECTIONAL;
  }

  /**
   * <!-- in -->
   * 
   * @see uk.ac.hutton.ontodot.Edge#in()
   * @return
   */
  @Override
  public Node in() {
    return in;
  }

  /**
   * <!-- out -->
   * 
   * @see uk.ac.hutton.ontodot.Edge#out()
   * @return
   */
  @Override
  public Node out() {
    return out;
  }

  /**
   * <!-- label -->
   * 
   * @see uk.ac.hutton.ontodot.Edge#label()
   * @return
   */
  @Override
  public String label() {
    return label;
  }

  /**
   * <!-- inPortLabel -->
   * 
   * @see uk.ac.hutton.ontodot.Edge#inPortLabel()
   * @return
   */
  @Override
  public String inPortLabel() {
    return inPortLabel;
  }

  /**
   * <!-- outPortLabel -->
   * 
   * @see uk.ac.hutton.ontodot.Edge#outPortLabel()
   * @return
   */
  @Override
  public String outPortLabel() {
    return outPortLabel;
  }

  public abstract void write(PrintWriter fp) throws IOException;

}
