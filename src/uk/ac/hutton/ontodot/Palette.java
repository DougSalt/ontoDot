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
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;


public class Palette {

	private Set<Color> palette;

	private int[] paletteCounters;

	private Map<String, Color> entityMap;

	private Map<String, Color> ontologyMap;

	public Palette() {
		palette = new HashSet<Color>();
		paletteCounters = new int[] { 0x00, 0x00, 0x00, 0x80 };
		entityMap = new HashMap<String, Color>();
		ontologyMap = new HashMap<String, Color>();
	}

	public Color newColour() {
		return newColour(palette, paletteCounters);
	}

	public Color getEntityColour(OWLEntity entity) {
		return getEntityColour(entity.getIRI());
	}

	public Color getEntityColour(IRI entityIRI) {
		return getEntityColour(entityIRI.toURI());
	}

	public Color getEntityColour(URI entityURI) {
		String uriStr = entityURI.toString();
		if(entityMap.containsKey(uriStr)) {
			return entityMap.get(uriStr);
		}
		Color colour = newColour();
		entityMap.put(uriStr, colour);
		return colour;
	}

	public Color getOntologyColour(OWLEntity entity) {
		return getOntologyColour(entity.getIRI());
	}

	public Color getOntologyColour(IRI entityIRI) {
		return getOntologyColour(entityIRI.toURI());
	}

	public Color getOntologyColour(URI entityURI) {
		String uriStr = entityURI.toString();
		int hashpos = uriStr.indexOf("#");
		if(hashpos != -1) {
			uriStr = uriStr.substring(0, hashpos);
		}
		if(ontologyMap.containsKey(uriStr)) {
			return ontologyMap.get(uriStr);
		}
		Color colour = newColour();
		ontologyMap.put(uriStr, colour);
		return colour;
	}

	public static Color newColour(Set<Color> palette, int[] counters) {
		Color newColour = null;

		for(int i = 0; i < counters.length; i++) {
			if(i < counters.length - 1) {
				counters[i] += counters[counters.length - 1];
				if(counters[i] < 0x100) {
					newColour = new Color(counters[0], counters[1], counters[2]);
					if(!palette.contains(newColour)) {
						break;
					}
				}
				else {
					counters[i] = 0x00;
				}
			}
			else {
				if(counters[i] == 1) {
					throw new RuntimeException("Run out of colours!");
				}
				counters[i] /= 2;
				for(int j = 0; j < i; j++) {
					counters[j] = 0x00;
				}
				i = -1;
			}
		}
		if(newColour == null) {
			throw new RuntimeException("Couldn't build a new colour!");
		}
		palette.add(newColour);
		return newColour;
	}

	public String[] getOntologyKeyString() {
		List<String> strl = new LinkedList<String>();

		strl.add("Ontology,R,G,B");

		addColourStrings(ontologyMap, strl);

		return strl.toArray(new String[0]);
	}

	public String[] getEntityKeyString() {
		List<String> strl = new LinkedList<String>();

		strl.add("Entity,R,G,B");

		addColourStrings(entityMap, strl);

		return strl.toArray(new String[0]);
	}

	private void addColourStrings(Map<String, Color> map, List<String> strl) {
		List<String> sortedKeys = new LinkedList<String>();
		sortedKeys.addAll(map.keySet());
		Collections.sort(sortedKeys);
		for(String key: sortedKeys) {
			Color c = map.get(key);
			strl.add(key + "," + Integer.toString(c.getRed()) + "," + Integer.toString(c.getGreen()) + ","
					+ Integer.toString(c.getBlue()));
		}
	}

}
