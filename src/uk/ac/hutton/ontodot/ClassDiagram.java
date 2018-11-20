/**
 * ClassDiagram.java, uk.ac.hutton.ontodot
 *
 * Copyright (C) The James Hutton Institute 2015
 *
 * This file is part of ontoDot
 *
 * ontoDot is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * ontoDot is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * ontoDot. If not, see <http://www.gnu.org/licenses/>. 
 */

package uk.ac.hutton.ontodot;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;


/**
 * <!-- ClassDiagram -->
 * 
 * @author Gary Polhill
 */
public class ClassDiagram extends AbstractDiagram {

	private Graph graph;

	private Ontology ontology;


	/**
	 * <!-- ClassDiagram constructor -->
	 * 
	 */
	public ClassDiagram(Graph graph, Ontology ontology) {
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
		Map<OWLClass, Node> nodes = new HashMap<OWLClass, Node>();
		Map<OWLObjectProperty, Set<OWLClass>> domains = new HashMap<OWLObjectProperty, Set<OWLClass>>();
		Map<OWLObjectProperty, Set<OWLClass>> ranges = new HashMap<OWLObjectProperty, Set<OWLClass>>();

		for(OWLOntology ont: search(ontology)) {
			for(OWLAxiom axiom: ont.axioms().collect(Collectors.toSet())) {
				if(axiom instanceof OWLDataPropertyDomainAxiom) {
					OWLClassExpression cls = ((OWLDataPropertyDomainAxiom)axiom).getDomain();
					OWLDataPropertyExpression prop = ((OWLDataPropertyDomainAxiom)axiom).getProperty();
					if(!cls.isAnonymous() && !prop.isAnonymous()) {
						Node node;
						if(nodes.containsKey(cls.asOWLClass())) {
							node = nodes.get(cls.asOWLClass());
							System.out.println("Got node for class " + getShortForm(cls.asOWLClass()));
						}
						else {
							node = graph.addNode(getShortForm(cls.asOWLClass()));
							nodes.put(cls.asOWLClass(), node);
						}
						if(node instanceof DotNode) {
							((DotNode)node).addText(getShortForm(prop.asOWLDataProperty()));
							System.out.println("Added " + getShortForm(prop.asOWLDataProperty()) + " as attribute of node for " + getShortForm(cls.asOWLClass()) + ", text now " + ((DotNode)node).getTextString());
						}
					}
				}
				else if(axiom instanceof OWLObjectPropertyDomainAxiom) {
					OWLClassExpression cls = ((OWLObjectPropertyDomainAxiom)axiom).getDomain();
					OWLObjectPropertyExpression prop = ((OWLObjectPropertyDomainAxiom)axiom).getProperty();
					if(!cls.isAnonymous() && !prop.isAnonymous()) {
						if(!domains.containsKey(cls.asOWLClass())) {
							domains.put(prop.asOWLObjectProperty(), new HashSet<OWLClass>());
						}
						domains.get(prop.asOWLObjectProperty()).add(cls.asOWLClass());
					}
				}
				else if(axiom instanceof OWLObjectPropertyRangeAxiom) {
					OWLClassExpression cls = ((OWLObjectPropertyRangeAxiom)axiom).getRange();
					OWLObjectPropertyExpression prop = ((OWLObjectPropertyRangeAxiom)axiom).getProperty();
					if(!cls.isAnonymous() && !prop.isAnonymous()) {
						if(!ranges.containsKey(prop.asOWLObjectProperty())) {
							ranges.put(prop.asOWLObjectProperty(), new HashSet<OWLClass>());
						}
						ranges.get(prop.asOWLObjectProperty()).add(cls.asOWLClass());
					}
				}
				else if(axiom instanceof OWLSubClassOfAxiom) {
					OWLClassExpression sub = ((OWLSubClassOfAxiom)axiom).getSubClass();
					OWLClassExpression sup = ((OWLSubClassOfAxiom)axiom).getSuperClass();
					if(!sub.isAnonymous() && !sup.isAnonymous()) {
						Edge edge = graph.addEdge(getShortForm(sub.asOWLClass()), getShortForm(sup.asOWLClass()), "is-a",
																			Direction.UNIDIRECTIONAL);
						if(edge instanceof DotEdge) {
							try {
								((DotEdge)edge).setAttribute("arrowhead", "empty");
								((DotEdge)edge).setAttribute("color", new Color(0x80, 0x80, 0x80));
								((DotEdge)edge).setAttribute("fontcolor", new Color(0x80, 0x80, 0x80));
							}
							catch(DotInvalidAttributeException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		for(OWLObjectProperty prop: domains.keySet()) {
			if(ranges.containsKey(prop)) {
				for(OWLClass dcls: domains.get(prop)) {
					for(OWLClass rcls: ranges.get(prop)) {
						Edge edge = graph.addEdge(getShortForm(dcls), getShortForm(rcls), getShortForm(prop),
																			Direction.UNIDIRECTIONAL);
						if(edge instanceof DotEdge) {
							try {
								((DotEdge)edge).setAttribute("arrowhead", "open");
								// This could be done in the defaults, but the code is now here
								// to add attributes specific to each property (e.g.
								// colour-coding)
							}
							catch(DotInvalidAttributeException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * <!-- main -->
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Ontology ont = new Ontology(IRI.create(new File(args[0])), new File(args[1]), true);
//			Ontology ont = new Ontology(new File(args[0]));

			DotGraph g = new DotGraph("G");

			g.setNodeDefault("shape", "record");
			g.setNodeDefault("fontname", "Helvetica");
//			g.setNodeDefault("fontcolor", new Color(0xD0, 0x0, 0x0));
//			g.setNodeDefault("color", new Color(0xD0, 0x0, 0x0));
			g.setNodeDefault("fontsize", 14);

			g.setEdgeDefault("color", new Color(0x10, 0x0, 0xD0));
			g.setEdgeDefault("labelfontname", "Helvetica");
			g.setEdgeDefault("fontname", "Helvetica");
			g.setEdgeDefault("labelfontcolor", new Color(0x10, 0x0, 0xD0));
			g.setEdgeDefault("fontcolor", new Color(0x10, 0x0, 0xD0));
			g.setEdgeDefault("fontsize", 12);

			ClassDiagram diag = new ClassDiagram(g, ont);

			if(args.length >= 4) {
				for(String prefix: args[3].split(",")) {
					String[] prefix_iri = prefix.split("=");
					diag.setPrefix(IRI.create(prefix_iri[1]), prefix_iri[0]);
					System.out.println("Setting prefix " + prefix_iri[0] + " for " + prefix_iri[1]);
				}
			}

			if(args.length >= 5) {
				for(String prefix: args[4].split(",")) {
					diag.ignoreOntology(prefix);
					System.out.println("Ignoring prefix: " + prefix);
				}
			}

			diag.buildGraph();

			g.save(args[2]);
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
