package net.seninp.neras;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import opennlp.tools.tokenize.Tokenizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.newBufferedWriter;

public class TokenizerFactory {

    /**
     * Tokenizes a file using a provided OpenNLP tokenizer instance.
     *
     * @param inFileName         the absolute input file name.
     * @param outFileName        output file name.
     * @param operatingTokenizer the tokenizer instance to be used.
     */
    public static void tokenizeOpenNLP(String inFileName, String outFileName, Tokenizer operatingTokenizer) {
        try (Stream<String> lines = Files.lines(Paths.get(inFileName), StandardCharsets.ISO_8859_1)) {
            List<List<String>> tokenizedSections = new ArrayList<>();
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

    /**
     * Tokenizes the input file in a way we do in Skywise, i.e., with PTBTokenizer...
     * ... A fast, rule-based tokenizer implementation, which produces Penn Treebank style tokenization of English text.
     * It was initially written to conform to Penn Treebank tokenization conventions over ASCII text,
     * but now provides a range of tokenization options over a broader space of Unicode text.
     * It reads raw text and outputs tokens of classes that implement edu.stanford.nlp.trees.HasWord
     * (typically a Word or a CoreLabel). It can optionally return end-of-line as a token...
     *
     * @param inFileName  the input filename (absolute).
     * @param outFileName the output filename.
     */
    public static void tokenizeOpenNLPPTBT(String inFileName, String outFileName) {
        try {
            BufferedReader inputData = Files.newBufferedReader(Paths.get(inFileName), StandardCharsets.ISO_8859_1);
            BufferedWriter outputData = Files.newBufferedWriter(Paths.get(outFileName), StandardCharsets.ISO_8859_1);
            PTBTokenizer<CoreLabel> tokenizer = new PTBTokenizer<CoreLabel>(inputData, new CoreLabelTokenFactory(), "ptb3Escaping=false");
            while (tokenizer.hasNext()) {
                outputData.write(tokenizer.next().originalText());
                outputData.newLine();
            }
            inputData.close();
            outputData.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ***********************************
    // some old code...
    // ***********************************

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


    //            try {
    //                PrintWriter out = new PrintWriter(outFileName);

    // creates a StanfordCoreNLP object, with POS tagging, lemmatization,
    // NER, parsing, and coreference resolution
    //                Properties props = new Properties();

    // configure pipeline
    //                props.put(
    //                        "annotators",
    //                        "tokenize"
    //                );

    //                props.setProperty("tokenize.options", "untokenizable=allKeep");

    // The 6 options for untokenizable are:
    // noneDelete, firstDelete, allDelete, noneKeep, firstKeep, allKeep

    //                StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

    // Annotation annotation = new Annotation(IOUtils.slurpFileNoExceptions(inFileName));
    //                Annotation annotation = new Annotation("Kosgi Santosh sent an email to Stanford University. He didn't get a reply.");

    //                pipeline.annotate(annotation);

    //                pipeline.prettyPrint(annotation, out);

    // An Annotation is a Map and you can get and use the various analyses individually.
    // For instance, this gets the parse tree of the first sentence in the text.
    //                List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
    //                if (sentences != null && sentences.size() > 0) {
    //                    CoreMap sentence = sentences.get(0);
    //                    Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
    //                    out.println();
    //                    out.println("The first sentence parsed is:");
    //                    tree.pennPrint(out);
    //                }

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

    //                pipeline.annotate(annotation);
    //                pipeline.prettyPrint(annotation, out);
    //
    //            } catch (FileNotFoundException e) {
    //                e.printStackTrace();
    //           }}

}
