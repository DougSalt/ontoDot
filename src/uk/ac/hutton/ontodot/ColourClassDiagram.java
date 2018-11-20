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
public class ColourClassDiagram extends AbstractDiagram {

	private Graph graph;

	private Ontology ontology;


	/**
	 * <!-- ClassDiagram constructor -->
	 * 
	 */
	public ColourClassDiagram(Graph graph, Ontology ontology) {
		this.graph = graph;
		this.ontology = ontology;
	}
	
	private Node manifestNode(OWLClass cls, Map<OWLClass, Node> nodes, Palette palette) {
		if(nodes.containsKey(cls)) {
			return nodes.get(cls);
		}
		Node node = graph.addNode(getShortForm(cls));
		if(node instanceof DotNode) {
			DotNode dotNode = (DotNode)node;
			try {
				Color c = palette.getOntologyColour(cls.asOWLClass());
				dotNode.setAttribute("fillcolor", c);
				dotNode.setAttribute("label", getFragment(cls));
				// See http://24ways.org/2010/calculating-color-contrast 
				// which seems to be taken from http://www.w3.org/TR/AERT#color-contrast
				int yiq = ((c.getRed() * 299) + (c.getGreen() * 587) + (c.getBlue() * 144)) / 1000;
				if(yiq < 128) {
					dotNode.setAttribute("fontcolor", Color.WHITE);
				}
			}
			catch(DotInvalidAttributeException e) {
				e.printStackTrace();
			}
		}
		nodes.put(cls, node);
		return node;
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
		Palette palette = new Palette();

		for(OWLOntology ont: search(ontology).stream().collect(Collectors.toSet())) {
			for(OWLAxiom axiom: ont.axioms().collect(Collectors.toSet())) {
				if(axiom instanceof OWLDataPropertyDomainAxiom) {
					OWLClassExpression cls = ((OWLDataPropertyDomainAxiom)axiom).getDomain();
					OWLDataPropertyExpression prop = ((OWLDataPropertyDomainAxiom)axiom).getProperty();
					if(!cls.isAnonymous() && !prop.isAnonymous()) {
						Node node = manifestNode(cls.asOWLClass(), nodes, palette);
						if(node instanceof DotNode) {
							((DotNode)node).addText(getFragment(prop.asOWLDataProperty()));
							System.out.println("Added " + getFragment(prop.asOWLDataProperty()) + " as attribute of node for " + getFragment(cls.asOWLClass()) + ", text now " + ((DotNode)node).getTextString());
						}
					}
				}
				else if(axiom instanceof OWLObjectPropertyDomainAxiom) {
					OWLClassExpression cls = ((OWLObjectPropertyDomainAxiom)axiom).getDomain();
					OWLObjectPropertyExpression prop = ((OWLObjectPropertyDomainAxiom)axiom).getProperty();
					if(!cls.isAnonymous() && !prop.isAnonymous()) {
						manifestNode(cls.asOWLClass(), nodes, palette);
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
						manifestNode(cls.asOWLClass(), nodes, palette);
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
						manifestNode(sub.asOWLClass(), nodes, palette);
						manifestNode(sup.asOWLClass(), nodes, palette);
						Edge edge = graph.addEdge(getShortForm(sub.asOWLClass()), getShortForm(sup.asOWLClass()), "is-a",
																			Direction.UNIDIRECTIONAL);
						if(edge instanceof DotEdge) {
							try {
								((DotEdge)edge).setAttribute("arrowhead", "empty");
								((DotEdge)edge).setAttribute("color", new Color(0x80, 0x80, 0x80));
								((DotEdge)edge).setAttribute("fontcolor", new Color(0x80, 0x80, 0x80));
							}
							catch(DotInvalidAttributeException e) {
								throw new RuntimeException("Bug!");
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
						Edge edge = graph.addEdge(getShortForm(dcls), getShortForm(rcls), getFragment(prop),
																			Direction.UNIDIRECTIONAL);
						if(edge instanceof DotEdge) {
							try {
								((DotEdge)edge).setAttribute("arrowhead", "open");
								((DotEdge)edge).setAttribute("color", palette.getOntologyColour(prop));
								((DotEdge)edge).setAttribute("fontcolor", palette.getOntologyColour(prop));
							}
							catch(DotInvalidAttributeException e) {
								throw new RuntimeException("Bug!");
							}
						}
					}
				}
			}
		}
		
		for(String key: palette.getOntologyKeyString()) {
			System.out.println(key);
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

			g.setNodeDefault("shape", "Mrecord");
			g.setNodeDefault("style", "filled");
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

			ColourClassDiagram diag = new ColourClassDiagram(g, ont);

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
