import edu.princeton.cs.algs4.In;
import java.util.Scanner;

public class Outcast {
    private SemanticAI semanticAI;

    // constructor takes a WordNet object
    public Outcast(SemanticAI wordnet) {
        semanticAI = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = "";
        int maxDistance = Integer.MIN_VALUE;
        for (String nounFrom : nouns) {
            int distanceNoun = 0;
            for (String nounTo : nouns) {
                int d = semanticAI.distance(nounFrom, nounTo);
                // StdOut.println(nounFrom + " - " + nounTo + " " + d);
                distanceNoun += d;
            }
            // StdOut.println(nounFrom + " total distance is " + distanceNoun);
            if (distanceNoun >= maxDistance) {
                maxDistance = distanceNoun;
                outcast = nounFrom;
            }
        }
        return outcast;
    }


    // see test client below
    public static void main(String[] args) {
        SemanticAI wordnet = new SemanticAI("\\test\\synsets.txt",
                                            "\\test\\hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        if (args[0].equals("-f")) {
            for (int t = 1; t < args.length; t++) {
                In in = new In("\\test\\" + args[t]);
                String[] nouns = in.readAllStrings();
                System.out.println("Outcast word in the textfile " +
                                           args[t] + " is: " + outcast.outcast(nouns));
            }
        }

        else if (args[0].equals("-w")) {
            String[] nouns = new String[args.length - 1];

            for (int t = 1; t < args.length; t++) {
                nouns[t - 1] = args[t];
            }
            System.out.println("The between the input words is: " + outcast.outcast(nouns));
        }
        new Scanner(System.in).nextLine();
    }
}