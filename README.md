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
## Usage

An example configuration. (Very limited at present)
```
input {
    stdin {}
}
filter {
    nlp {
        source => "message"
    }
}
output {
    stdout {}
}
```

This will add 3 fields to your log:

###nlp.sentiment
This is the average sentiment of the entire phrase

###nlp.tokens
This is the phrase broken down into its token (mighty useful in elasticsearch)

###nlp.sentences
This is an array of sentences that were extracted from the phrase. For each sentence you will get:
 - sentiment: The sentiment of the sentence
 - tokens: The tokens in the sentence
 - sentimentTree: The sentence presented as a sentiment tree, an example:
 ```
 [{"tokens":["gammon","for","tea","and","very","thirsty","-RSB-"],"sentimentTree":"(1 (2 (2 (2 gammon) (2 (2 for) (2 tea))) (2 and)) (2 (2 very) (2 (2 thirsty) (2 -RSB-))))","sentiment":1}]
 ```
 
## The Stanford Natural Language Processing Group
Thanks to [The Stanford Natural Language Processing Group](http://nlp.stanford.edu/software/corenlp.shtml)

For more information on the Stanford NLP Core Toolkit set http://nlp.stanford.edu/pubs/StanfordCoreNlp2014.pdf
