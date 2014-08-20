/* uk.ac.hutton.ontodot: Ontology.java
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

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

/**
 * <!-- Ontology -->
 *
 * @author Gary Polhill
 */
public class Ontology {
  private OWLOntologyManager manager;

  private Ontology() {
    manager = OWLManager.createOWLOntologyManager();
  }
  
  public Ontology(IRI physical) throws OWLOntologyCreationException {
    this(physical, false);
  }
  
  public Ontology(IRI physical, boolean ignoreImportFailures) throws OWLOntologyCreationException {
    this();
    manager.setSilentMissingImportsHandling(ignoreImportFailures);
    manager.loadOntologyFromOntologyDocument(physical);
  }
  
  public Ontology(File physical) throws OWLOntologyCreationException {
    this(physical, false);
  }
  
  public Ontology(File physical, boolean ignoreImportFailures) throws OWLOntologyCreationException {
    this();
    manager.setSilentMissingImportsHandling(ignoreImportFailures);
    manager.loadOntologyFromOntologyDocument(physical);
  }
  
  public Ontology(IRI logical, Map<IRI, IRI> ontologyLocations) throws OWLOntologyCreationException {
    this();
    for(IRI logic: ontologyLocations.keySet()) {
      manager.addIRIMapper(new SimpleIRIMapper(logic, ontologyLocations.get(logic)));
    }
    manager.loadOntology(logical);
  }
  
  public Ontology(IRI logical, File dir, boolean recursive) throws OWLOntologyCreationException {
    this();
    manager.addIRIMapper(new AutoIRIMapper(dir, recursive));
    manager.loadOntology(logical);
  }
    
  public Set<OWLIndividual> getAllIndividuals() {
    return getAllIndividuals(manager.getOntologies());
  }
  
  public Set<OWLDataProperty> getAllDataProperties() {
    return getAllDataProperties(manager.getOntologies());
  }
  
  public Set<OWLObjectProperty> getAllObjectProperties() {
    return getAllObjectProperties(manager.getOntologies());
  }
  
  public Set<OWLClass> getAllClasses() {
    return getAllClasses(manager.getOntologies());
  }
  
  public Set<OWLOntology> getOntologies() {
    return manager.getOntologies();
  }
  
  public Set<OWLIndividual> getAllIndividuals(Set<OWLOntology> search) {
    Set<OWLIndividual> inds = new HashSet<OWLIndividual>();
    for(OWLOntology ont: search) {
      inds.addAll(ont.getIndividualsInSignature());
    }
    return inds;
  }
  
  public Set<OWLDataProperty> getAllDataProperties(Set<OWLOntology> search) {
    Set<OWLDataProperty> props = new HashSet<OWLDataProperty>();
    for(OWLOntology ont: search) {
      props.addAll(ont.getDataPropertiesInSignature());
    }
    return props;
  }

  public Set<OWLObjectProperty> getAllObjectProperties(Set<OWLOntology> search) {
    Set<OWLObjectProperty> props = new HashSet<OWLObjectProperty>();
    for(OWLOntology ont: search) {
      props.addAll(ont.getObjectPropertiesInSignature());
    }
    return props;
  }

  public Set<OWLClass> getAllClasses(Set<OWLOntology> search) {
    Set<OWLClass> classes = new HashSet<OWLClass>();
    for(OWLOntology ont: search) {
      classes.addAll(ont.getClassesInSignature());
    }
    return classes;
  }
  
}
