/*
 * uk.ac.hutton.ontodot: AbstractDiagram.java
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

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 * <!-- AbstractDiagram -->
 * 
 * @author Gary Polhill
 */
public abstract class AbstractDiagram implements Diagram {
  private Set<IRI> ignore;
  private DefaultPrefixManager prefix;
  private String defaultPrefix;

  protected AbstractDiagram() {
    ignore = new HashSet<IRI>();
    prefix = new DefaultPrefixManager();
    defaultPrefix = null;
  }

  public void setPrefix(IRI ontologyIRI, String namespace) {
    String ontologyURIstr = ontologyIRI.toURI().toString();
    if(!ontologyURIstr.endsWith("#")) {
      // ontologyURIstr = ontologyURIstr.substring(0, ontologyURIstr.length() -
      // 1);
      ontologyURIstr += "#";
    }
    prefix.setPrefix(namespace + ":", ontologyURIstr);
  }

  public void setDefaultPrefix(String namespace) {
    prefix.setDefaultPrefix(namespace + ":");
    defaultPrefix = namespace + ":";
  }

  public String getPrefix(IRI ontologyIRI) {
    String prefixWithColon = prefix.getShortForm(ontologyIRI);
    return prefixWithColon.substring(0, prefixWithColon.length() - 1);
  }

  public String getShortForm(IRI entityIRI) {
    String shortName = prefix.getShortForm(entityIRI);
    if(defaultPrefix != null && shortName.startsWith(defaultPrefix)) {
      return shortName.substring(defaultPrefix.length(), shortName.length());
    }
    return shortName;
  }

  public String getShortForm(OWLEntity entity) {
    return getShortForm(entity.getIRI());
  }
  
  public String getFragment(OWLEntity entity) {
  	return entity.getIRI().toURI().getFragment();
  }

  public void ignoreOntology(IRI ontologyIRI) {
    ignore.add(ontologyIRI);

    if(ontologyIRI.toURI().toString().endsWith("#")) {
      String iri = ontologyIRI.toURI().toString();
      ignore.add(IRI.create(iri.substring(0, iri.length() - 1)));
    }
    else {
      ignore.add(IRI.create(ontologyIRI.toURI().toString() + "#"));
    }
  }

  public void ignoreOntology(String namespace) {
    ignoreOntology(prefix.getIRI(namespace));
  }

  public void ignoreOntology(OWLOntology ontology) {
	 //ignoreOntology(ontology.getOntologyID().getOntologyIRI());
  }

  public void ignoreEntity(IRI entityIRI) {
    ignore.add(entityIRI);
  }

  public void ignoreEntity(OWLEntity entity) {
    ignoreEntity(entity.getIRI());
  }

  public boolean ignoring(IRI ontologyIRI) {
    if(ignore.contains(ontologyIRI)) return true;

    String iriStr = ontologyIRI.toURI().toString();
    String fragStr = ontologyIRI.getFragment();

    if(fragStr == null) return false;

    String ontStr = iriStr.substring(0, iriStr.length() - fragStr.length());

    if(ignore.contains(IRI.create(ontStr))) return true;
    return false;
  }

  public boolean ignoring(OWLEntity entity) {
    return ignoring(entity.getIRI());
  }

  public boolean ignoring(OWLOntology ontology) {
    return ignoring(ontology.getOntologyID().getOntologyIRI().get());
  }

  public boolean ignoring(Set<OWLEntity> entities) {
    for(OWLEntity entity: entities) {
      if(ignoring(entity)) return true;
    }
    return false;
  }

  public Set<OWLOntology> search(Ontology ont) {
    Set<OWLOntology> search = new HashSet<OWLOntology>();

    for(OWLOntology owlOnt: ont.getOntologies()) {
      if(!ignoring(owlOnt)) {
        search.add(owlOnt);
      }
    }

    return search;
  }

}
