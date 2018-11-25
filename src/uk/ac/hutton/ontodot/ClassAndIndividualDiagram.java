/**
 * ClassAndIndividualDiagram.java, uk.ac.hutton.ontodot
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
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
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
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

/**
 * <!-- ClassAndIndividualDiagram -->
 * 
 * @author Doug Salt
 */
public class ClassAndIndividualDiagram extends AbstractDiagram {

	private Graph graph;

	private Ontology ontology;

    private Map<String,String> label = new HashMap<String, String>();

	/**
	 * <!-- ClassAndIndividualDiagram constructor -->
	 * 
	 */
	public ClassAndIndividualDiagram(Graph graph, Ontology ontology) {
		this.graph = graph;
		this.ontology = ontology;
	}

    /**
	 * <!-- ClassAndIndividualDiagram constructor -->
	 * 
	 */
    public String getShortForm(IRI iri) {
        String shortForm = super.getShortForm(iri);
        if (label.containsKey(shortForm))
            return label.get(shortForm);
        else
            return shortForm; 
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
		Map<OWLIndividual, Node> individuals = new HashMap<OWLIndividual, Node>();
		Palette palette = new Palette();

		for(OWLOntology ont: search(ontology).stream().collect(Collectors.toSet())) {
			for(OWLAxiom axiom: ont.axioms().collect(Collectors.toSet())) {
                if(axiom instanceof OWLAnnotationAssertionAxiom) {
                    OWLObject subject = ((OWLAnnotationAssertionAxiom)axiom).getSubject();
                    IRI iri;
                    if (subject.isIRI()) {
                        iri = (IRI)subject;
                        OWLAnnotationProperty prop = ((OWLAnnotationAssertionAxiom)axiom).getProperty();
                        if (prop.isLabel()) {
                            // Ye gods, Java is so bloody ugly...
                            OWLLiteral lit = (OWLLiteral)((OWLAnnotationAssertionAxiom)axiom).getValue();
                            if (lit.getLang() == "" || lit.getLang().indexOf("en") != -1) {
                                label.put(getShortForm(iri), lit.getLiteral());
                            }
                        }
                    }
                }
            }
        }
		for(OWLOntology ont: search(ontology).stream().collect(Collectors.toSet())) {
			for(OWLAxiom axiom: ont.axioms().collect(Collectors.toSet())) {
                //System.out.println("Axiom type = " + axiom.getClass());
                if(axiom instanceof OWLDataPropertyDomainAxiom) {
					OWLClassExpression cls = ((OWLDataPropertyDomainAxiom)axiom).getDomain();
					OWLDataPropertyExpression prop = ((OWLDataPropertyDomainAxiom)axiom).getProperty();
					if(!cls.isAnonymous() && !prop.isAnonymous()) {
						Node node;
						if(nodes.containsKey(cls.asOWLClass())) {
							node = nodes.get(cls.asOWLClass());
						}
						else {
							node = graph.addNode(getShortForm(cls.asOWLClass()));
							nodes.put(cls.asOWLClass(), node);
						}
						if(node instanceof DotNode) {
							((DotNode)node).addText(getShortForm(prop.asOWLDataProperty()));
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
				else if(axiom instanceof OWLClassAssertionAxiom) {
                    OWLClassExpression cls = ((OWLClassAssertionAxiom)axiom).getClassExpression();
                    OWLIndividual ind = ((OWLClassAssertionAxiom)axiom).getIndividual();
                    Node node;
                    if(individuals.containsKey(ind.asOWLNamedIndividual())) {
                        node = individuals.get(ind.asOWLNamedIndividual());
                    }
                    else {
                        node = graph.addNode(getShortForm(ind.asOWLNamedIndividual()));
                        individuals.put(ind.asOWLNamedIndividual(), node);
                    }
                    //if(ind.isNamed() && ind instanceof DotNode) {
                        try {
                            ((DotNode)node).setAttribute("shape","record");
							((DotNode)node).setAttribute("color", new Color(0x00, 0xFF, 0x00));
							((DotNode)node).setAttribute("fontcolor", new Color(0x00, 0xFF, 0x00));
                        }
                        catch(DotInvalidAttributeException e) {
                            e.printStackTrace();
                        }
                    //}
					if(!ind.isAnonymous() && !cls.isAnonymous()) {
						Edge edge = graph.addEdge(
                                getShortForm(ind.asOWLNamedIndividual()), 
                                getShortForm(cls.asOWLClass()), 
                                "instance-of",
								Direction.UNIDIRECTIONAL);
						if(edge instanceof DotEdge) {
							try {
								((DotEdge)edge).setAttribute("arrowhead", "empty");
								((DotEdge)edge).setAttribute("color", new Color(0x00, 0xFF, 0x00));
								((DotEdge)edge).setAttribute("fontcolor", new Color(0x00, 0xFF, 0x00));
							}
							catch(DotInvalidAttributeException e) {
								e.printStackTrace();
							}
						}
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
                else if (axiom instanceof OWLObjectPropertyAssertionAxiom){ 
                //&& !ignoring(axiom.signature().collect(Collectors.toSet()))){

                    OWLObjectPropertyAssertionAxiom objAxiom = (OWLObjectPropertyAssertionAxiom)axiom;

                    OWLIndividual in = objAxiom.getSubject();
                    OWLIndividual out = objAxiom.getObject();
     
                    Node inNode;
                    if(individuals.containsKey(in.asOWLNamedIndividual())) {
                        inNode = individuals.get(in.asOWLNamedIndividual());
                    }
                    else {
                        inNode = graph.addNode(getShortForm(in.asOWLNamedIndividual()));
                        individuals.put(in.asOWLNamedIndividual(), inNode);
                    }

                    Node outNode;
                    if(individuals.containsKey(out.asOWLNamedIndividual())) {
                        outNode = individuals.get(out.asOWLNamedIndividual());
                    }
                    else {
                        outNode = graph.addNode(getShortForm(out.asOWLNamedIndividual()));
                        individuals.put(out.asOWLNamedIndividual(), outNode);
                    }

                    if(in.isNamed() && out.isNamed()) {
						if(inNode instanceof DotNode) {
                            try {
                                ((DotNode)inNode).setAttribute("shape","record");
							    ((DotNode)inNode).setAttribute("color", new Color(0x00, 0xFF, 0x00));
							    ((DotNode)inNode).setAttribute("fontcolor", new Color(0x00, 0xFF, 0x00));
                            }
							catch(DotInvalidAttributeException e) {
								e.printStackTrace();
							}
                        }
						if(outNode instanceof DotNode) {
                            try {
                                ((DotNode)outNode).setAttribute("shape","record");
                                ((DotNode)outNode).setAttribute("shape","record");
							    ((DotNode)outNode).setAttribute("color", new Color(0x00, 0xFF, 0x00));
                            }
							catch(DotInvalidAttributeException e) {
								e.printStackTrace();
							}
                        }
                        OWLObjectPropertyExpression expr = objAxiom.getProperty();

                        if(expr.isAnonymous()) {
                            graph.addEdge(getShortForm(out.asOWLNamedIndividual()), getShortForm(in.asOWLNamedIndividual()),
                                getShortForm(expr.getNamedProperty()));
                        } else 
                            graph.addEdge(getShortForm(in.asOWLNamedIndividual()), getShortForm(out.asOWLNamedIndividual()),
                                getShortForm(expr.asOWLObjectProperty()));
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
        //for(String key: palette.getOntologyKeyString()) {
		//	System.out.println(key);
		//}

	}

	/**
	 * <!-- main -->
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
    try {
      Ontology ont = new Ontology(IRI.create(new File(args[0])), new File(args[1]), true);

      DotGraph g = new DotGraph("G");

      g.setAttribute("layout", "neato");
      g.setAttribute("overlap", "false");

      g.setNodeDefault("shape", "Mrecord");
      g.setNodeDefault("fontname", "Helvetica");
      g.setNodeDefault("fontsize", 8);
      
      g.setEdgeDefault("color", new Color(0x10, 0x0, 0xD0));
      g.setEdgeDefault("labelfontname", "Helvetica");
      g.setEdgeDefault("fontname", "Helvetica");
      g.setEdgeDefault("labelfontcolor", new Color(0x10, 0x0, 0xD0));
      g.setEdgeDefault("fontcolor", new Color(0x10, 0x0, 0xD0));
      g.setEdgeDefault("fontsize", 8);

      ClassAndIndividualDiagram diag = new ClassAndIndividualDiagram(g, ont);

      for(String prefix: args[2].split(",")) {
        String[] prefix_iri = prefix.split("=");
        diag.setPrefix(IRI.create(prefix_iri[1]), prefix_iri[0]);
      }

      for(String prefix: args[3].split(",")) {
		diag.ignoreOntology(prefix);
		System.out.println("Ignoring prefix: " + prefix);
      }

      diag.buildGraph();

      System.out.println("Writing out to: " + args[4]);
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
