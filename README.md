#Intro

This is an attempt to plugin the Stanford NLP library into logstash and make its features available via a logstash filter. It is in very alpha condition at the minute but is useable.

The aim is for any field in any set of logs that are piped through logstash to be able to be parsed by the Stanford NLP. It is my aim for this filter to add fields that represent the following:
- All tokens
- All sentences
- Overall sentiment
- Sentence sentiment
- Sentence sentiment Tree

##Intallation
>This is pretty rudimental at present but should get me started
```
#cd to the logstash filters folder
cd /path/to/logstash/lib/logstash/filters
```
```
#create the nlp folder and download the neccesary dependencies (jars)
mkdir nlp
cd mkdir
wget http://central.maven.org/maven2/edu/stanford/nlp/stanford-corenlp/3.5.0/stanford-corenlp-3.5.0.jar
wget http://nlp.stanford.edu/software/stanford-corenlp-models-current.jar
wget https://github.com/jwconway/logstash-stanford-nlp/raw/master/lib/stanford_nlp_wrapper_jar/stanford-nlp-wrapper.jar
```
```
#download the filter into the filter folder
cd ..
wget https://raw.githubusercontent.com/jwconway/logstash-stanford-nlp/master/logstash/lib/filters/nlp.rb
```

An example configuration. (Very limited at present)
```
input {
    stdin {}
}
filter {
    nlp {}
}
output {
    stdout {}
}
```
## The Stanford Natural Language Processing Group
Thanks to [The Stanford Natural Language Processing Group](http://nlp.stanford.edu/software/corenlp.shtml)

For more information on the Stanford NLP Core Toolkit set http://nlp.stanford.edu/pubs/StanfordCoreNlp2014.pdf
