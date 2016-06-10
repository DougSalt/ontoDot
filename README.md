# ontoDot
Renders OWL2 ontologies in dot files for Graphviz

This software is essentially a reimplementation of the OntoViz project for Protege 3, which seems not to be available for OWL 2 in later versions of Protege. It is, however, not (yet) implemented as a plug-in for Protege 5. One of the reasons we liked OntoViz rather than the now seemingly more popular 'spring'-based network visualisation approaches is that GraphViz (which underpins it) prints the graph on the size of page needed to implement it, with clear space between each node.

It takes an OWL ontology and renders a visualisation of it using the GraphViz DOT language. Various classes are implemented that provide different kinds of visualisations -- e.g. classes, individuals. The implementation is intended to allow for other visualisation languages (e.g. SVG or VRML) given suitable implementations of the interfaces. For now, only DOT is implemented.

It needs a better interface than the current command-line one.
