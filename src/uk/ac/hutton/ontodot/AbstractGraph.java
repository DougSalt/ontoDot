/* uk.ac.hutton.ontodot: AbstractGraph.java
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import uk.ac.macaulay.util.FileOpener;

/**
 * <!-- AbstractGraph -->
 *
 * @author Gary Polhill
 */
public abstract class AbstractGraph implements Graph {
  
  private HashMap<String, Node> nodes;
  private HashSet<Edge> edges;
  private HashSet<Graph> subgraphs;
  private final Graph superGraph;
  
  public AbstractGraph() {
    this(null);
  }
  
  public AbstractGraph(Graph superGraph) {
    nodes = new HashMap<String, Node>();
    edges = new HashSet<Edge>();
    subgraphs = new HashSet<Graph>();
    this.superGraph = superGraph;
  }
  
  @Override
  public boolean isSubgraph() {
    return superGraph != null;
  }
  
  @Override
  public Graph superGraph() {
    return superGraph;
  }
  

  /**
   * <!-- nodes -->
   *
   * @see uk.ac.hutton.ontodot.Graph#nodes()
   * @return
   */
  @Override
  public Set<Node> nodes() {
    return new HashSet<Node>(nodes.values());
  }

  /**
   * <!-- edges -->
   *
   * @see uk.ac.hutton.ontodot.Graph#edges()
   * @return
   */
  @Override
  public Set<Edge> edges() {
    return Collections.unmodifiableSet(edges);
  }

  /**
   * <!-- subgraphs -->
   *
   * @see uk.ac.hutton.ontodot.Graph#subgraphs()
   * @return
   */
  @Override
  public Set<Graph> subgraphs() {
    return Collections.unmodifiableSet(subgraphs);
  }

  /**
   * <!-- addNode -->
   *
   * @see uk.ac.hutton.ontodot.Graph#addNode(uk.ac.hutton.ontodot.Node)
   * @param node
   */
  @Override
  public void addNode(Node node) {
    nodes.put(node.label(), node);
  }
  
  @Override
  public Node getNode(String label) {
    return nodes.get(label);
  }

  /**
   * <!-- addEdge -->
   *
   * @see uk.ac.hutton.ontodot.Graph#addEdge(uk.ac.hutton.ontodot.Edge)
   * @param edge
   */
  @Override
  public void addEdge(Edge edge) {
    edges.add(edge);
  }

  /**
   * <!-- addSubgraph -->
   *
   * @see uk.ac.hutton.ontodot.Graph#addSubgraph(uk.ac.hutton.ontodot.Graph)
   * @param graph
   */
  @Override
  public void addSubgraph(Graph graph) {
    subgraphs.add(graph);
  }
  
  /**
   * <!-- save -->
   *
   * @see uk.ac.hutton.ontodot.Graph#save(java.lang.String)
   * @param location
   * @throws IOException
   */
  @Override
  public void save(String location) throws IOException {
    PrintWriter fp = FileOpener.write(location);
    write(fp);
    fp.close();
  }
  
  public abstract void write(PrintWriter fp) throws IOException;

  /**
   * <!-- directed -->
   *
   * @see uk.ac.hutton.ontodot.Graph#directed()
   * @return <code>true</code> if the graph contains at least one directed edge
   */
  @Override
  public boolean directed() {
    for(Edge edge: edges) {
      if(edge.directed()) return true;
    }
    return false;
  }

}
