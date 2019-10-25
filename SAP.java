/* *****************************************************************************
 *  Name: Dumitru Hanciu
 *  Date: 14.01.2019
 *  Description: SAP
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class SAP {
    private Digraph D;
    private int V, ancestor, next, length;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        V = G.V();
        D = new Digraph(G);
        this.ancestor = -1;
        this.length = -1;
        this.next = -1;
    }

    // a common ancestor of v and w that participates
    // in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        length(v, w);
        return ancestor;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        length(v, w);
        return ancestor;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        ArrayList<Integer> vArray = new ArrayList<Integer>(Arrays.asList(v));
        ArrayList<Integer> wArray = new ArrayList<Integer>(Arrays.asList(w));
        return length(vArray, wArray);

    }



    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        validate(v);
        validate(w);
        for (int x : v)
            for (int y : w)
                if (x == y) {
                    ancestor = x;
                    return 0;
                }

        ancestor = -1;
        length = -1;

        Queue<Integer> queueV = new Queue<Integer>();
        boolean[] markedV = new boolean[V];
        int[] distToV = new int[V];

        Queue<Integer> queueW = new Queue<Integer>();
        boolean[] markedW = new boolean[V];
        int[] distToW = new int[V];

        for (int x : v) {
            markedV[x] = true;
            queueV.enqueue(x);
        }

        for (int y : w) {
            markedW[y] = true;
            queueW.enqueue(y);
        }


        while (!queueV.isEmpty() || !queueW.isEmpty()) {
            if (!queueV.isEmpty()) {
                int adjV = queueV.dequeue();
                for (int nextV : D.adj(adjV)) {
                    int distV = distToV[adjV] + 1;
                    if (!markedV[nextV] && (length >= distToV[adjV] || length == -1)) {
                        distToV[nextV] = distV;
                        markedV[nextV] = true;
                        queueV.enqueue(nextV);
                    }
                    if (markedW[nextV] && (length >= distV + distToW[nextV] || length == -1)) {
                        ancestor = nextV;
                        this.next = adjV;
                        length = distV + distToW[nextV];
                    }
                }
            }

            if (!queueW.isEmpty()) {
                int adjW = queueW.dequeue();
                for (int nextW : D.adj(adjW)) {
                    int distW = distToW[adjW] + 1;
                    if (!markedW[nextW] && (length >= distToW[adjW] || length == -1)) {
                        distToW[nextW] = distW;
                        markedW[nextW] = true;
                        queueW.enqueue(nextW);
                    }
                    if (markedV[nextW] && (length >= distW + distToV[nextW] || length == -1)) {
                        ancestor = nextW;
                        this.next = adjW;
                        length = distW + distToV[nextW];
                    }
                }
            }
        }
        return length;
    }


    private void validate(Iterable<Integer> i) {
        for (Integer v : i)
            if (v == null) throw new IllegalArgumentException();
            else validate(v);
    }

    private void validate(int v) {
        if (v < 0 || v >= V) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
