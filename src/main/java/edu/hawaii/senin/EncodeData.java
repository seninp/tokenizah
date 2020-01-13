package edu.hawaii.senin;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.newBufferedWriter;

public class EncodeData {

    private static final String SIMPLE_TOKENIZER = "opennlp_simple";
    private static final String WHITESPACE_TOKENIZER = "opennlp_whitespace";

    private static final String CORENLP_TOKENIZER = "corenlp";

    @Parameter(names = {"--tokenizer", "-t"})
    String tokenizer = CORENLP_TOKENIZER;
    @Parameter(names = {"--input", "-i"})
    String inFileName = "/mnt/md0/Altran/long_text.txt";
    @Parameter(names = {"--output", "-o"})
    String outFileName = "/mnt/md0/Altran/long_text_tokens_ONLPwhitespace.txt";

    public static void main(String... argv) {
        EncodeData main = new EncodeData();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run();
    }

    public void run() {
        System.out.printf("%s %s", tokenizer, inFileName);

        if (CORENLP_TOKENIZER.equalsIgnoreCase(tokenizer)) {
            // props.setProperty("tokenize.options", "untokenizable=allKeep");
            // The 6 options for untokenizable are:
            // noneDelete, firstDelete, allDelete, noneKeep, firstKeep, allKeep

//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//        Annotation annotation;
//        annotation = new Annotation(IOUtils.slurpFileNoExceptions(args[0]));
//
//        pipeline.annotate(annotation);
//        pipeline.prettyPrint(annotation, out);


            try {
                PrintWriter out = new PrintWriter(outFileName);

                // creates a StanfordCoreNLP object, with POS tagging, lemmatization,
                // NER, parsing, and coreference resolution
                Properties props = new Properties();

                // configure pipeline
                props.put(
                        "annotators",
                        "tokenize"
                );

                props.setProperty("tokenize.options", "untokenizable=allKeep");

                // The 6 options for untokenizable are:
                // noneDelete, firstDelete, allDelete, noneKeep, firstKeep, allKeep

                StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

                // Annotation annotation = new Annotation(IOUtils.slurpFileNoExceptions(inFileName));
                Annotation annotation = new Annotation("Kosgi Santosh sent an email to Stanford University. He didn't get a reply.");

                pipeline.annotate(annotation);

                pipeline.prettyPrint(annotation, out);

                // An Annotation is a Map and you can get and use the various analyses individually.
                // For instance, this gets the parse tree of the first sentence in the text.
                List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
                if (sentences != null && sentences.size() > 0) {
                    CoreMap sentence = sentences.get(0);
                    Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
                    out.println();
                    out.println("The first sentence parsed is:");
                    tree.pennPrint(out);
                }

                // pipeline.xmlPrint(annotation, xmlOut);

                // An Annotation is a Map and you can get and use the various analyses individually.
                // For instance, this gets the parse tree of the first sentence in the text.
//
//            List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
//
//            if (sentences != null && sentences.size() > 0) {
//                CoreMap sentence = sentences.get(0);
//                Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//                out.println();
//                out.println("The first sentence parsed is:");
//                tree.pennPrint(out);
//            }

                pipeline.annotate(annotation);
                pipeline.prettyPrint(annotation, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (WHITESPACE_TOKENIZER.equalsIgnoreCase(tokenizer) || SIMPLE_TOKENIZER.equalsIgnoreCase(tokenizer)) {
            // https://opennlp.apache.org/docs/1.8.2/apidocs/opennlp-tools/opennlp/tools/tokenize/TokenizerME.html

            Tokenizer operatingTokenizer = null;

            if (WHITESPACE_TOKENIZER.equalsIgnoreCase(tokenizer)) {
                operatingTokenizer = WhitespaceTokenizer.INSTANCE;
            } else if (SIMPLE_TOKENIZER.equalsIgnoreCase(tokenizer)) {
                operatingTokenizer = SimpleTokenizer.INSTANCE;
            }

            List<List<String>> tokenizedSections = new ArrayList<>();

            try (Stream<String> lines = Files.lines(Paths.get(inFileName))) {

                Tokenizer finalOperatingTokenizer = operatingTokenizer;
                tokenizedSections = lines
                        .map(s -> Arrays.asList(finalOperatingTokenizer.tokenize(s)))
                        .collect(Collectors.toList());

                PrintWriter pw = new PrintWriter(newBufferedWriter(
                        Paths.get(outFileName)));
                tokenizedSections.forEach(section -> section.forEach(pw::println));


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}