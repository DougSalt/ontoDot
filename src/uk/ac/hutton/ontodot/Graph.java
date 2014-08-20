/* uk.ac.hutton.ontodot: Graph.java
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
import java.util.Set;

/**
 * <!-- Graph -->
 *
 * @author Gary Polhill
 */
public interface Graph {
  public Set<Node> nodes();
  public Set<Edge> edges();
  public Set<Graph> subgraphs();
  public void addNode(Node node);
  public Node addNode(String node);
  public void addEdge(Edge edge);
  public Edge addEdge(String in, String out);
  public Edge addEdge(String in, String out, String label);
  public Edge addEdge(String in, String out, Direction dir);
  public Edge addEdge(String in, String out, String label, Direction dir);
  public Edge addEdge(String in, String out, String label, Direction dir, String inPortLabel, String outPortLabel);
  public void addSubgraph(Graph graph);
  public void save(String location) throws IOException;
  public boolean directed();
  public boolean isSubgraph();
  public Graph superGraph();
  public Node getNode(String label);
}
