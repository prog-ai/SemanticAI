
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class SemanticAI {
    private ArrayList<ArrayList<String>> nounsArray;
    private HashSet<String> nounsSet;
    private SAP sap;

    // constructor takes the name of the two input files
    public SemanticAI(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        nounsSet = new HashSet<>();
        nounsArray = new ArrayList<>();

        In in = new In(synsets);
        while (!in.isEmpty()) {
            String[] s = in.readLine().split("\\,");
            ArrayList<String> nouns = new ArrayList<>(Arrays.asList(s[1].split(" ")));
            nounsArray.add(nouns);
            nounsSet.addAll(nouns);
        }

        Digraph G = new Digraph(nounsArray.size());

        in = new In(hypernyms);
        boolean[] notRoots = new boolean[nounsArray.size()];
        while (!in.isEmpty()) {
            String[] s = in.readLine().split("\\,");
            for (int i = 1; i < s.length; i++) {
                G.addEdge(Integer.parseInt(s[0]), Integer.parseInt(s[i]));
                notRoots[Integer.parseInt(s[0])] = true;
            }
        }
        int count = 0;
        for (boolean notRoot : notRoots) {
            if (!notRoot) count++;
        }
        if (count > 1 || count < 1) throw new IllegalArgumentException();
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounsSet;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        else return nounsSet.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        ArrayList<Integer> idA = getID(nounA);
        ArrayList<Integer> idB = getID(nounB);
        int length = sap.length(idA, idB);
        return length;
    }

    private ArrayList<Integer> getID(String noun) {
        if (noun == null) throw new IllegalArgumentException();
        ArrayList<Integer> id = new ArrayList<Integer>();
        for (int i = 0; i < nounsArray.size(); i++)
            for (String syn : nounsArray.get(i))
                if (syn.equals(noun))
                    id.add(i);
        if (id.size() == 0) throw new IllegalArgumentException();
        else return id;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        ArrayList<Integer> idA = getID(nounA);
        ArrayList<Integer> idB = getID(nounB);
        int ancestor = sap.ancestor(idA, idB);
        ArrayList<String> nouns = nounsArray.get(ancestor);
        StringBuilder nounsString = new StringBuilder();
        for (String noun : nouns)
            nounsString.append(noun + " ");
        return nounsString.deleteCharAt(nounsString.length() - 1).toString();
    }

    // see test client below
    public static void main(String[] args) {
        SemanticAI wordnet = new SemanticAI("\\test\\synsets.txt",
                                            "\\test\\hypernyms.txt");

        while (true) {
            Scanner in = new Scanner(System.in);
            System.out.print("Please input two nouns: ");
            String[] nouns = in.nextLine().split(" ");
            // String[] nouns = StdIn.readLine().split(" ");
            System.out.println("The common ancestors for the words " + nouns[0] + " and " + nouns[1] +
                                       " are: " + wordnet.sap(nouns[0], nouns[1]));
            System.out.println("The semantic distance between them is: " +
                                       wordnet.distance(nouns[0], nouns[1]));
            System.out.println();
            System.out.println("***********************************");
            System.out.println();

        }
    }
}
