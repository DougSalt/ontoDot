/* uk.ac.hutton.ontodot: DotGraph.java
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * <!-- DotGraph -->
 *
 * @author Gary Polhill
 */
public class DotGraph extends AbstractGraph {
  private Map<String, DotGraphAttribute> attributes;
  private Map<String, DotNodeAttribute> nodeDefaults;
  private Map<String, DotEdgeAttribute> edgeDefaults;
  private final String label;

  public DotGraph(String label) {
    this(label, null, false);
  }
  
  public DotGraph(String label, boolean cluster) {
    this(label, null, cluster);
  }
  
  public DotGraph(String label, Graph superGraph, boolean cluster) {
    super(superGraph);
    attributes = new HashMap<String, DotGraphAttribute>();
    nodeDefaults = new HashMap<String, DotNodeAttribute>();
    edgeDefaults = new HashMap<String, DotEdgeAttribute>();
    this.label = (cluster && !label.startsWith("cluster")) ? "cluster_" + label : label;
  }
  
  public boolean isCluster() {
    return label.startsWith("cluster");
  }
  
  public void setAttribute(String label, String value) throws DotInvalidAttributeException {
    attributes.put(label, new DotGraphAttribute(label, value));
  }
  
  public void setAttribute(String label, int value) throws DotInvalidAttributeException {
    attributes.put(label, new DotGraphAttribute(label, value));
  }
  
  public void setAttribute(String label, double value) throws DotInvalidAttributeException {
    attributes.put(label, new DotGraphAttribute(label, value));
  }
  
  public void setAttribute(String label, Color value) throws DotInvalidAttributeException {
    attributes.put(label, new DotGraphAttribute(label, value));
  }
  
  public void setNodeDefault(String label, String value) throws DotInvalidAttributeException {
    nodeDefaults.put(label, new DotNodeAttribute(label, value));
  }
  
  public void setNodeDefault(String label, int value) throws DotInvalidAttributeException {
    nodeDefaults.put(label, new DotNodeAttribute(label, value));
  }
  
  public void setNodeDefault(String label, double value) throws DotInvalidAttributeException {
    nodeDefaults.put(label, new DotNodeAttribute(label, value));
  }
  
  public boolean hasNodeDefault(String label) {
  	return nodeDefaults.containsKey(label);
  }
  
  public String getNodeDefault(String label) {
  	return nodeDefaults.get(label).value();
  }
  
  public Class<?> getNodeDefaultType(String label) {
  	return nodeDefaults.get(label).type();
  }
  
  public void setNodeDefault(String label, Color value) throws DotInvalidAttributeException {
    nodeDefaults.put(label, new DotNodeAttribute(label, value));
  }
  
  public void setEdgeDefault(String label, String value) throws DotInvalidAttributeException {
    edgeDefaults.put(label, new DotEdgeAttribute(label, value));
  }
  
  public void setEdgeDefault(String label, int value) throws DotInvalidAttributeException {
    edgeDefaults.put(label, new DotEdgeAttribute(label, value));
  }
  
  public void setEdgeDefault(String label, double value) throws DotInvalidAttributeException {
    edgeDefaults.put(label, new DotEdgeAttribute(label, value));
  }
  
  public void setEdgeDefault(String label, Color value) throws DotInvalidAttributeException {
    edgeDefaults.put(label, new DotEdgeAttribute(label, value));
  }
  
  public boolean hasEdgeDefault(String label) {
  	return edgeDefaults.containsKey(label);
  }
  
  public String getEdgeDefault(String label) {
  	return edgeDefaults.get(label).value();
  }
  
  public Class<?> getEdgeDefaultType(String label) {
  	return edgeDefaults.get(label).type();
  }
  
  public String indent() {
    if(isSubgraph() && superGraph() instanceof DotGraph) {
      return "  " + ((DotGraph)superGraph()).indent();
    }
    else {
      return "  ";
    }
  }

  /**
   * <!-- write -->
   *
   * @see uk.ac.hutton.ontodot.Graph#write(java.io.PrintWriter)
   * @param fp
   * @throws IOException
   */
  @Override
  public void write(PrintWriter fp) throws IOException {
    fp.println((!isSubgraph() ? "digraph " : "subgraph ") + label + " {");
    
    if(attributes.size() > 0) {
      fp.print(indent() + "graph [");
      DotAttribute.writeSet(attributes.values(), fp);
      fp.println("];");
    }
    if(nodeDefaults.size() > 0) {
      fp.print(indent() + "node [");
      DotAttribute.writeSet(nodeDefaults.values(), fp);
      fp.println("];");
    }
    if(edgeDefaults.size() > 0) {
      fp.print(indent() + "edge [");
      DotAttribute.writeSet(edgeDefaults.values(), fp);
      fp.println("];");
    }
    
    for(Node node: nodes()) {
      ((DotNode)node).write(fp);
    }
    
    for(Edge edge: edges()) {
      ((DotEdge)edge).write(fp);
    }
    
    for(Graph graph: subgraphs()) {
      ((DotGraph)graph).write(fp);
    }
    
    fp.println("}");
  }

  /**
   * <!-- addNode -->
   *
   * @see uk.ac.hutton.ontodot.Graph#addNode(java.lang.String)
   * @param node
   */
  @Override
  public Node addNode(String node) {
    Node n = new DotNode(node, this);
    addNode(n);
    return n;
  }
  
  private Node manifestNode(String node) {
    Node n = getNode(node);
    if(n == null) {
      n = addNode(node);
    }
    return n;
  }

  /**
   * <!-- addEdge -->
   *
   * @see uk.ac.hutton.ontodot.Graph#addEdge(java.lang.String, java.lang.String)
   * @param in
   * @param out
   * @return 
   */
  @Override
  public Edge addEdge(String in, String out) {
    Edge edge = new DotEdge(manifestNode(in), manifestNode(out), this);
    addEdge(edge);
    return edge;
  }

  /**
   * <!-- addEdge -->
   *
   * @see uk.ac.hutton.ontodot.Graph#addEdge(java.lang.String, java.lang.String, java.lang.String)
   * @param in
   * @param out
   * @param label
   */
  @Override
  public Edge addEdge(String in, String out, String label) {
    Edge edge = new DotEdge(manifestNode(in), manifestNode(out), label, this);
    addEdge(edge);
    return edge;
  }

  /**
   * <!-- addEdge -->
   *
   * @see uk.ac.hutton.ontodot.Graph#addEdge(java.lang.String, java.lang.String, uk.ac.hutton.ontodot.Direction)
   * @param in
   * @param out
   * @param dir
   */
  @Override
  public Edge addEdge(String in, String out, Direction dir) {
    return addEdge(in, out, null, dir);
  }

  /**
   * <!-- addEdge -->
   *
   * @see uk.ac.hutton.ontodot.Graph#addEdge(java.lang.String, java.lang.String, java.lang.String, uk.ac.hutton.ontodot.Direction)
   * @param in
   * @param out
   * @param label
   * @param dir
   */
  @Override
  public Edge addEdge(String in, String out, String label, Direction dir) {
    Edge edge = new DotEdge(manifestNode(in), manifestNode(out), label, dir, this);
    addEdge(edge);
    return edge;
  }

  /**
   * <!-- addEdge -->
   *
   * @see uk.ac.hutton.ontodot.Graph#addEdge(java.lang.String, java.lang.String, java.lang.String, uk.ac.hutton.ontodot.Direction, java.lang.String, java.lang.String)
   * @param in
   * @param out
   * @param label
   * @param dir
   * @param inPortLabel
   * @param outPortLabel
   */
  @Override
  public Edge addEdge(String in, String out, String label, Direction dir, String inPortLabel, String outPortLabel) {
    Edge edge = new DotEdge(manifestNode(in), manifestNode(out), label, dir, inPortLabel, outPortLabel, this);
    addEdge(edge);
    return edge;
  }

}
