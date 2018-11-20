/*
 * uk.ac.hutton.ontodot: IndividualDiagram.java
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
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * <!-- IndividualDiagram -->
 * 
 * @author Gary Polhill
 */
public class IndividualDiagram extends AbstractDiagram {
  private Graph graph;
  private Ontology ontology;

  public IndividualDiagram(Graph graph, Ontology ontology) {
    this.graph = graph;
    this.ontology = ontology;
  }

  /**
   * <!-- buildGraph -->
   * 
   * @see uk.ac.hutton.ontodot.Diagram#buildGraph()
   */
  @Override
  public void buildGraph() {
    for(OWLOntology ont: search(ontology)) {
      for(OWLAxiom axiom: ont.axioms().collect(Collectors.toSet())) {
        if(axiom instanceof OWLObjectPropertyAssertionAxiom && !ignoring(axiom.signature().collect(Collectors.toSet()))){
          OWLObjectPropertyAssertionAxiom objAxiom = (OWLObjectPropertyAssertionAxiom)axiom;

          OWLIndividual in = objAxiom.getSubject();
          OWLIndividual out = objAxiom.getObject();

          if(in.isNamed() && out.isNamed()) {
            OWLObjectPropertyExpression expr = objAxiom.getProperty();

            if(expr.isAnonymous()) {
              graph.addEdge(getShortForm(out.asOWLNamedIndividual()), getShortForm(in.asOWLNamedIndividual()),
                  getShortForm(expr.getNamedProperty()));
            }
            else {
              graph.addEdge(getShortForm(in.asOWLNamedIndividual()), getShortForm(out.asOWLNamedIndividual()),
                  getShortForm(expr.asOWLObjectProperty()));
            }
          }
        }
      }
    }
  }

  public static void main(String[] args) {
    try {
      Ontology ont = new Ontology(IRI.create(new File(args[0])), new File(args[1]), true);

      DotGraph g = new DotGraph("G");

      g.setNodeDefault("shape", "record");
      g.setNodeDefault("fontname", "Helvetica");
      g.setNodeDefault("fontcolor", new Color(0xD0, 0x0, 0x0));
      g.setNodeDefault("color", new Color(0xD0, 0x0, 0x0));
      
      g.setEdgeDefault("color", new Color(0x10, 0x0, 0xD0));
      g.setEdgeDefault("labelfontname", "Helvetica");
      g.setEdgeDefault("fontname", "Helvetica");
      g.setEdgeDefault("labelfontcolor", new Color(0x10, 0x0, 0xD0));
      g.setEdgeDefault("fontcolor", new Color(0x10, 0x0, 0xD0));

      IndividualDiagram diag = new IndividualDiagram(g, ont);

      for(String prefix: args[2].split(",")) {
        String[] prefix_iri = prefix.split("=");
        diag.setPrefix(IRI.create(prefix_iri[1]), prefix_iri[0]);
      }

      for(String prefix: args[3].split(",")) {
			diag.ignoreOntology(prefix);
			System.out.println("Ignoring prefix: " + prefix);
      }

      diag.buildGraph();

      g.save(args[4]);
    }
    catch(OWLOntologyCreationException e) {
      e.printStackTrace();
    }
    catch(DotInvalidAttributeException e) {
      e.printStackTrace();
    }
    catch(IOException e) {
      e.printStackTrace();
    }

  }
}
