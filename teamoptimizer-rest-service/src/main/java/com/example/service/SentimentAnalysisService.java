package com.example.service;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Generics;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Service
public class SentimentAnalysisService {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SentimentAnalysisService.class);

    private StanfordCoreNLP tokenizer;
    private StanfordCoreNLP pipeline;

    static enum Output {
        PENNTREES, VECTORS, ROOT, PROBABILITIES
    }

    public SentimentAnalysisService() {
        init();
    }

    public void init() {
        Properties pipelineProps = new Properties();
        Properties tokenizerProps = null;
        pipelineProps.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
        pipelineProps.setProperty("enforceRequirements", "false");
        tokenizerProps = new Properties();
        tokenizerProps.setProperty("annotators", "tokenize, ssplit, tokenize, ssplit, pos, lemma");

        tokenizer = (tokenizerProps == null) ? null : new StanfordCoreNLP(tokenizerProps);
        pipeline = new StanfordCoreNLP(pipelineProps);

    }

    public Integer analyzeSentence(String tweet) {
        Annotation annotation = new Annotation(tweet);
        tokenizer.annotate(annotation);

        List<Annotation> annotations = Generics.newArrayList();
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Annotation nextAnnotation = new Annotation(sentence.get(CoreAnnotations.TextAnnotation.class));
            nextAnnotation.set(CoreAnnotations.SentencesAnnotation.class, Collections.singletonList(sentence));
            annotations.add(nextAnnotation);
        }

        //the total sentiment calculated by counting individual sentences in the tweet
        int totalSentiment = 0;

        //annotate all sentences one by one
        for (Annotation ann : annotations) {
            pipeline.annotate(ann);

            for (CoreMap sentence : ann.get(CoreAnnotations.SentencesAnnotation.class)) {
                //logger.info(sentence);
                //logger.info(sentence.get(SentimentCoreAnnotations.ClassName.class));

                String sentenceSentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
                int sentenceSentimentScore = 0;
                switch (sentenceSentiment) {
                    case "Negative": {
                        sentenceSentimentScore--;
                        break;
                    }
                    case "Very negative": {
                        sentenceSentimentScore = sentenceSentimentScore - 2;
                        break;
                    }
                    case "Positive": {
                        sentenceSentimentScore++;
                        break;
                    }
                    case "Very positive": {
                        sentenceSentimentScore = sentenceSentimentScore + 2;
                        break;
                    }
                }

//                if (sentenceSentiment.equals("Negative") || sentenceSentiment.equals("Very negative")) {
//                    totalSentiment--;
//                } else if (sentenceSentiment.equals("Positive") || sentenceSentiment.equals("Very positive")) {
//                    totalSentiment++;
//                }
                totalSentiment = totalSentiment + sentenceSentimentScore;
                logger.info(sentence + " - Sentiment:<" + sentence.get(SentimentCoreAnnotations.SentimentClass.class) + "> - Score:<" + sentenceSentimentScore + ">");
            }
        }

        return totalSentiment > 0 ? 1 : totalSentiment < 0 ? -1 : 0;
    }

    /**
     * Outputs a tree using the output style requested
     */
    static void outputTree(PrintStream out, CoreMap sentence, List<Output> outputFormats) {
        Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
        for (Output output : outputFormats) {
            switch (output) {
                case ROOT: {
                    out.println("  " + sentence.get(SentimentCoreAnnotations.SentimentClass.class));
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown output format " + output);
            }
        }
    }

    static final String DEFAULT_TLPP_CLASS = "edu.stanford.nlp.parser.lexparser.EnglishTreebankParserParams";

    public static void main23(String[] args) {
        SentimentAnalysisService sentimentAnalysisService = new SentimentAnalysisService();
        //sentimentAnalysisService.init();

        logger.info(sentimentAnalysisService.analyzeSentence("We were about to fail.\nBut then we found Stanford Sentiment Analyzer.\nNow we're back in track again, yay!").toString());
        logger.info(sentimentAnalysisService.analyzeSentence("This concert absolutely rocks!!!").toString());
        logger.info(sentimentAnalysisService.analyzeSentence("People aren’t against you; they are for themselves.").toString());
        logger.info(sentimentAnalysisService.analyzeSentence("Climb mountains not so the world can see you, but so you can see the world.").toString());
        logger.info(sentimentAnalysisService.analyzeSentence("Go where you’re celebrated, not where you’re tolerated.").toString());


    }

    public static void main(String[] args) throws IOException {
        String text = "They could have almost heard the yowling from the Oval Office and the Pentagon after Safer's 1965 expose of a U.S. military atrocity in Vietnam that played an early role in changing Americans' view of the war.\n" +
                "\n" +
                "They may have felt a flush of gratitude on learning that Safer's 1983 investigation of justice gone awry resulted in the release of a Texas man wrongfully sentenced to life in prison.";
        Properties props = new Properties();
        props.setProperty("annotators",
                "tokenize, ssplit, pos, lemma, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation annotation = pipeline.process(text);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            logger.info(sentiment + "\t" + sentence);
        }
    }

    public StanfordCoreNLP getPipeline() {
        return pipeline;
    }
}