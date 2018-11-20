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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * <!-- IndividualDiagram -->
 * 
 * @author Gary Polhill
 */
public class DenseIndividualDiagram extends AbstractDiagram {

	private Graph graph;

	private Ontology ontology;

	public DenseIndividualDiagram(Graph graph, Ontology ontology) {
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

		// Build a map from individual to class -- null entries imply multiple
		// classes
		Map<OWLIndividual, OWLClass> clsMap = new HashMap<OWLIndividual, OWLClass>();
		for(OWLOntology ont: search(ontology)) {
			for(OWLAxiom axiom: ont.axioms().collect(Collectors.toSet())) {
				if(axiom instanceof OWLClassAssertionAxiom) {
					OWLClassAssertionAxiom clsAxiom = (OWLClassAssertionAxiom)axiom;

					OWLClassExpression clsexpr = clsAxiom.getClassExpression();

					if(!clsexpr.isAnonymous()) {
						OWLIndividual ind = clsAxiom.getIndividual();

						if(ind.isNamed()) {
							if(clsMap.containsKey(ind)) {
								clsMap.put(ind, clsexpr.asOWLClass());
							}
							else if(clsMap.get(ind) != null
									&& !clsMap.get(ind).equals(clsexpr.asOWLClass())) {
								clsMap.put(ind, null);
							}
						}
					}
				}
			}
		}

		// Prepare to build a palette of lots of colours

		Set<Color> palette = new HashSet<Color>();
		int[] paletteCounters = new int[] { 0x00, 0x00, 0x00, 0x80 };

		// Build the graph. We need to keep a key of colours for each edge and class

		Map<OWLClass, Color> clsClrs = new HashMap<OWLClass, Color>();
		Map<OWLObjectProperty, Color> propClrs = new HashMap<OWLObjectProperty, Color>();
		
		for(OWLOntology ont: search(ontology)) {
			for(OWLAxiom axiom: ont.axioms().collect(Collectors.toSet())) {
				if(axiom instanceof OWLObjectPropertyAssertionAxiom && !ignoring(axiom.signature().collect(Collectors.toSet()))) {
					OWLObjectPropertyAssertionAxiom objAxiom = (OWLObjectPropertyAssertionAxiom)axiom;

					OWLIndividual in = objAxiom.getSubject();
					OWLIndividual out = objAxiom.getObject();

					if(in.isNamed() && out.isNamed()) {
						OWLObjectPropertyExpression expr = objAxiom.getProperty();
						OWLObjectProperty prop = expr.isAnonymous() ? expr.getNamedProperty() : expr.asOWLObjectProperty();

						String instr = getShortForm(in.asOWLNamedIndividual());
						String outstr = getShortForm(out.asOWLNamedIndividual());

						if(expr.isAnonymous()) {
							String tmpstr = instr;
							instr = outstr;
							outstr = tmpstr;
						}

						// Add the edge with no label
						Edge edge = graph.addEdge(instr, outstr);

						if(edge instanceof DotEdge) {
							// Colour the nodes and edges

							DotEdge dedge = (DotEdge)edge;

							Node nin = graph.getNode(instr);
							Node nout = graph.getNode(outstr);

							if(clsMap.containsKey(in)) {

								OWLClass cls = clsMap.get(in);

								if(cls != null && nin instanceof DotNode) {
									// Colour the input node
									if(!clsClrs.containsKey(cls)) {
										clsClrs.put(cls, Palette.newColour(palette, paletteCounters));
									}

									DotNode dnin = (DotNode)nin;
									try {
										dnin.setAttribute("fillcolor", clsClrs.get(cls));
										dnin.setAttribute("color", clsClrs.get(cls));
									}
									catch(DotInvalidAttributeException e) {
										throw new RuntimeException(e);
									}
								}
							}
							if(clsMap.containsKey(out)) {

								OWLClass cls = clsMap.get(out);

								if(cls != null && nout instanceof DotNode) {
									// Colour the output node

									if(!clsClrs.containsKey(cls)) {
										clsClrs.put(cls, Palette.newColour(palette, paletteCounters));
									}

									DotNode dnout = (DotNode)nout;
									try {
										dnout.setAttribute("fillcolor", clsClrs.get(cls));
										dnout.setAttribute("color", clsClrs.get(cls));
									}
									catch(DotInvalidAttributeException e) {
										throw new RuntimeException(e);
									}
								}
							}

							// Colour the edge

							if(!propClrs.containsKey(prop)) {
								propClrs.put(prop, Palette.newColour(palette, paletteCounters));
							}

							try {
								dedge.setAttribute("color", propClrs.get(prop));
							}
							catch(DotInvalidAttributeException e) {
								throw new RuntimeException(e);
							}
						}
					}
				}
			}
		}

		// Print a CSV for the legend
		System.out.println("type,url,R,G,B");
		for(OWLClass cls: clsClrs.keySet()) {
			Color clr = clsClrs.get(cls);
			System.out.println("node," + cls.getIRI().getFragment() + "," + clr.getRed() + "," + clr.getGreen() + ","
					+ clr.getBlue());
		}
		for(OWLObjectProperty prop: propClrs.keySet()) {
			Color clr = propClrs.get(prop);
			System.out.println("edge," + prop.getIRI().getFragment() + "," + clr.getRed() + "," + clr.getGreen() + ","
					+ clr.getBlue());
		}
	}


	public static void main(String[] args) {
		try {
			Ontology ont = new Ontology(IRI.create(new File(args[0])), new File(args[1]), true);

			DotGraph g = new DotGraph("G");

			g.setNodeDefault("shape", "point");
			g.setNodeDefault("fontname", "Helvetica");

			g.setEdgeDefault("labelfontname", "Helvetica");
			g.setEdgeDefault("fontname", "Helvetica");

			DenseIndividualDiagram diag = new DenseIndividualDiagram(g, ont);

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
