package uk.co.jaywayco;

import com.sun.javaws.exceptions.InvalidArgumentException;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.ejml.simple.SimpleMatrix;

import java.awt.*;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.function.Function;

/**
 * Created by james.conway on 29/01/2015.
 */
public class Parser {

    Properties pipelineProps;
    Properties tokenizerProps;
    StanfordCoreNLP tokenizer;
    StanfordCoreNLP pipeline;

    public Parser() {
        pipelineProps = new Properties();
        tokenizerProps = null;

        pipelineProps.setProperty("annotators", "parse, sentiment");
        pipelineProps.setProperty("enforceRequirements", "false");
        tokenizerProps = new Properties();
        tokenizerProps.setProperty("annotators", "tokenize, ssplit");

        tokenizer = (tokenizerProps == null) ? null : new StanfordCoreNLP(tokenizerProps);
        pipeline = new StanfordCoreNLP(pipelineProps);
    }

    private void setSentimentLabels(Tree tree) {
        if (tree.isLeaf()) {
            return;
        }

        for (Tree child : tree.children()) {
            setSentimentLabels(child);
        }

        Label label = tree.label();
        if (!(label instanceof CoreLabel)) {
            throw new IllegalArgumentException("Required a tree with CoreLabels");
        }
        CoreLabel cl = (CoreLabel) label;
        cl.setValue(Integer.toString(RNNCoreAnnotations.getPredictedClass(tree)));
    }

    public List<String> labelsToStrings(List<CoreLabel> labels) {
        List<String> stringLabels= new ArrayList<String>();

        for(CoreLabel label: labels){
            stringLabels.add(label.get(CoreAnnotations.ValueAnnotation.class));
        }
        return stringLabels;
    }

    private Integer GetScore(String nlpSentiment) {
        if(nlpSentiment.equals("Very positive")) {
            return 4;
        }
        else if (nlpSentiment.equals("Positive")) {
            return 3;
        }
        else if(nlpSentiment.equals("Neutral")) {
            return 2;
        }
        else if(nlpSentiment.equals("Negative")) {
            return 1;
        }
        else if(nlpSentiment.equals("Very negative")) {
            return 0;
        }
        else {
            throw new IllegalArgumentException(nlpSentiment);
        }
    }

    public Object processLine(String line){

        SentimentResult result = new SentimentResult();

        Annotation annotation = tokenizer.process(line);
        pipeline.annotate(annotation);

        result.tokens = labelsToStrings(annotation.get(CoreAnnotations.TokensAnnotation.class));

        List<CoreMap> list = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        Iterator<CoreMap> it = list.iterator();

        int acc = 0;

        while (it.hasNext()) {

            CoreMap map = it.next();

            Sentence sentence = new Sentence();

            Tree tree = map.get(SentimentCoreAnnotations.AnnotatedTree.class);
            Tree pennTree = tree.deepCopy();
            setSentimentLabels(pennTree);
            sentence.sentimentTree = pennTree.toString();

            sentence.tokens = labelsToStrings(map.get(CoreAnnotations.TokensAnnotation.class));

            String sentimentStr = map.get(SentimentCoreAnnotations.ClassName.class);
            sentence.sentiment = GetScore(sentimentStr);
            acc += sentence.sentiment;

            result.addSentence(sentence);
        }

        float avgSentiment = list.size() > 0 ? acc / list.size() : 2.0f;
        result.sentiment = Float.parseFloat(NUMBER_FORMAT.format(avgSentiment));

        return result;
    }



    private static final NumberFormat NF = new DecimalFormat("0.0000");
    static final NumberFormat NUMBER_FORMAT = new DecimalFormat("0.00");

    class SentimentResult{
        public List<Sentence> sentences = new ArrayList<uk.co.jaywayco.Parser.Sentence>();

        public List<String> tokens;

        public float sentiment;

        public void addSentence(Sentence sentence) {
            sentences.add(sentence);
        }
    }

    class Sentence {

        public List<String> tokens;

        public String sentimentTree;

        public int sentiment;
    }
}


