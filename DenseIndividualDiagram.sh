unset JAVA_HOME
JAVA=$(which java)
BASE=/home/ds42723/git/ontoDot
# This makes no fucking sense.
cp=$BASE/bin
cp+=:$BASE/lib/animal-sniffer-annotations-1.14.jar
cp+=:$BASE/lib/caffeine-2.6.1.jar
cp+=:$BASE/lib/commons-codec-1.10.jar
cp+=:$BASE/lib/commons-io-2.5.jar
cp+=:$BASE/lib/commons-rdf-api-0.5.0.jar
cp+=:$BASE/lib/error_prone_annotations-2.0.18.jar
cp+=:$BASE/lib/fluent-hc-4.5.5.jar
cp+=:$BASE/lib/guava-22.0.jar
cp+=:$BASE/lib/hppcrt-0.7.5.jar
cp+=:$BASE/lib/httpclient-4.5.2.jar
cp+=:$BASE/lib/httpclient-cache-4.5.2.jar
cp+=:$BASE/lib/httpclient-osgi-4.5.5.jar
cp+=:$BASE/lib/httpcore-4.4.4.jar
cp+=:$BASE/lib/httpcore-nio-4.4.5.jar
cp+=:$BASE/lib/httpcore-osgi-4.4.5.jar
cp+=:$BASE/lib/httpmime-4.5.5.jar
cp+=:$BASE/lib/j2objc-annotations-1.1.jar
cp+=:$BASE/lib/jackson-annotations-2.9.0.jar
cp+=:$BASE/lib/jackson-core-2.9.0.jar
cp+=:$BASE/lib/jackson-databind-2.9.0.jar
cp+=:$BASE/lib/javax.inject-1.jar
cp+=:$BASE/lib/jcl-over-slf4j-1.7.22.jar
cp+=:$BASE/lib/jsonld-java-0.12.0.jar
cp+=:$BASE/lib/jsr305-3.0.2.jar
cp+=:$BASE/lib/owlapi-distribution-5.1.6.jar
cp+=:$BASE/lib/rdf4j-model-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-api-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-binary-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-datatypes-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-jsonld-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-languages-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-n3-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-nquads-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-ntriples-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-rdfjson-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-rdfxml-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-trig-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-trix-2.3.2.jar
cp+=:$BASE/lib/rdf4j-rio-turtle-2.3.2.jar
cp+=:$BASE/lib/rdf4j-util-2.3.2.jar
cp+=:$BASE/lib/utils.jar
cp+=:$BASE/lib/xz-1.6.jar
# This next library was not in the dependency pack for owlapi, which makes me
# think that the owlapi bods do not what they are doing, or the tools for
# dependency resolution in java are complete crap. I spend so much of time
# tracking down jars of the correct version. Java is astonishingly brittle.
cp+=:$BASE/lib/slf4j-api-1.7.22.jar
cp+=:$BASE/lib/slf4j-simple-1.7.22.jar
set -xv
$JAVA -cp $cp uk.ac.hutton.ontodot.DenseIndividualDiagram \
    $HOME/git/diary/ontologies/mindmap.owl \
    $HOME/git/diary/ontologies \
    mm=http://www.semanticweb.org/ds42723/ontologies/2016/8/Mindmap,prov=http://www.w3.org/ns/prov \
    none \
    doug.dot
    
    
