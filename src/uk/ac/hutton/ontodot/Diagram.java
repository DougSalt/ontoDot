/* uk.ac.hutton.ontodot: Diagram.java
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

import org.semanticweb.owlapi.model.IRI;

/**
 * <!-- Diagram -->
 *
 * @author Gary Polhill
 */
public interface Diagram {
  public void setPrefix(IRI ontologyIRI, String namespace);
  public void setDefaultPrefix(String namespace);
  public String getPrefix(IRI ontologyIRI);
  public String getShortForm(IRI entityIRI);
  public void ignoreOntology(IRI ontologyIRI);
  public void ignoreOntology(String namespace);
  public void ignoreEntity(IRI entityIRI);
  public boolean ignoring(IRI entityIRI);
  public void buildGraph();
}
