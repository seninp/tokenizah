java -jar target\neras-1.0-SNAPSHOT-jar-with-dependencies.jar --tokenizer opennlp_simple --input data/concessions.txt --output data/concessions_opnlp_simple.txt
java -jar target\neras-1.0-SNAPSHOT-jar-with-dependencies.jar --tokenizer opennlp_simple --input data/nc.txt --output data/nc_opnlp_simple.txt

java -jar target\neras-1.0-SNAPSHOT-jar-with-dependencies.jar --tokenizer opennlp_whitespace --input data/concessions.txt --output data/concessions_opnlp_whitespace.txt
java -jar target\neras-1.0-SNAPSHOT-jar-with-dependencies.jar --tokenizer opennlp_whitespace --input data/nc.txt --output data/nc_opnlp_whitespace.txt

java -jar target\neras-1.0-SNAPSHOT-jar-with-dependencies.jar --tokenizer corenlp --input data/concessions.txt --output data/concessions_corenlp.txt
java -jar target\neras-1.0-SNAPSHOT-jar-with-dependencies.jar --tokenizer corenlp --input data/nc.txt --output data/nc_corenlp.txt