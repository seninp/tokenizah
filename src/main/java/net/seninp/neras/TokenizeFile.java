package net.seninp.neras;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;

public class TokenizeFile {

    @Parameter(names = {"--help", "-h"}, help = true)
    private boolean help = false;

    private static final String SIMPLE_TOKENIZER = "opennlp_simple";
    private static final String WHITESPACE_TOKENIZER = "opennlp_whitespace";

    private static final String CORENLP_TOKENIZER = "corenlp_ptbt";

    private static final String tokenizer_list = SIMPLE_TOKENIZER + ", " + WHITESPACE_TOKENIZER + ", " +
            CORENLP_TOKENIZER;
    @Parameter(names = {"--tokenizer", "-t"}, description = "the tokenizer key; supported at the moment :" +
            tokenizer_list)
    String tokenizer = SIMPLE_TOKENIZER;
    @Parameter(names = {"--input", "-i"}, description = "the relative/absolute path to the input file; assumed to be in UTF-8.")
    String inFileName = "data/concessions.txt";
    @Parameter(names = {"--output", "-o"}, description = "the output file name. will be overwritten.")
    String outFileName = "data/concessions_opnlp_simple.txt";

    public static void main(String... argv) {
        TokenizeFile main = new TokenizeFile();
        JCommander jct = JCommander.newBuilder().addObject(main).build();
        jct.parse(argv);
        if (main.isHelp()) {
            jct.usage();
            System.exit(0);
        }
        main.run();
    }

    public boolean isHelp() {
        return help;
    }

    public void run() {

        System.out.printf("Input: %s, output: %s, tokenizer: %s", inFileName, outFileName, tokenizer);

        TokenizerFactory tf = new TokenizerFactory();

        if (CORENLP_TOKENIZER.equalsIgnoreCase(tokenizer)) {
            TokenizerFactory.tokenizeOpenNLPPTBT(inFileName, outFileName);
        } else if (WHITESPACE_TOKENIZER.equalsIgnoreCase(tokenizer) || SIMPLE_TOKENIZER.equalsIgnoreCase(tokenizer)) {
            // https://opennlp.apache.org/docs/1.8.2/apidocs/opennlp-tools/opennlp/tools/tokenize/TokenizerME.html

            Tokenizer operatingTokenizer = null;

            if (WHITESPACE_TOKENIZER.equalsIgnoreCase(tokenizer)) {
                operatingTokenizer = WhitespaceTokenizer.INSTANCE;
            } else if (SIMPLE_TOKENIZER.equalsIgnoreCase(tokenizer)) {
                operatingTokenizer = SimpleTokenizer.INSTANCE;
            }

            TokenizerFactory.tokenizeOpenNLP(inFileName, outFileName, operatingTokenizer);

        }
    }

}