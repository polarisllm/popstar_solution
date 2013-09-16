/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GameLogic;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author wssccc
 */
class SearchNode {

    GameState gs;
    Pt tapPoint;
    SearchNode parent;

    public SearchNode(GameState gs, Pt tapPoint, SearchNode parent) {
        this.gs = gs;
        this.tapPoint = tapPoint;
        this.parent = parent;
    }

    String path() {
        if (parent == null) {
            return tapPoint.toString();
        } else {
            return parent.path() + "\\" + tapPoint.toString();
        }
    }
}

public class GameSolver implements Runnable {

    public boolean running = true;
    GameState initGameState;

    public GameSolver(GameState initGameState) {
        this.initGameState = initGameState;
    }

    public void search() {

        Random r = new Random(System.currentTimeMillis());
        Stack<SearchNode> q = new Stack<>();
        ArrayList<Pt> ts = initGameState.getTaps();
        while (!ts.isEmpty()) {
            int i = r.nextInt(ts.size());
            q.push(new SearchNode(initGameState.clone(), ts.get(i), null));
            ts.remove(i);
        }

        int maxScore = 0;
        SearchNode bestScoreNode = null;
        int minLeft = Integer.MAX_VALUE;
        SearchNode bestLeftNode = null;

        while (!q.empty()) {
            if (!running) {
                return;
            }
            SearchNode node = q.pop();
            //apply tap
            Pt pt = node.tapPoint;
            node.gs.tap(pt.x, pt.y);

            //
            ArrayList<Pt> taps = node.gs.getTaps();
            if (taps.isEmpty())//finish game
            {
                if (node.gs.score > maxScore) {
                    maxScore = node.gs.score;
                    bestScoreNode = node;
                    System.out.println("s count = " + node.gs.count() + " score = " + node.gs.score + " " + node.path());
                }
                if (node.gs.count() < minLeft) {
                    minLeft = node.gs.count();
                    bestLeftNode = node;
                    System.out.println("l count = " + node.gs.count() + " score = " + node.gs.score + " " + node.path());
                }

            } else {
                if (node.gs.score + node.gs.evalMaxScore() > maxScore) {
                    while (!taps.isEmpty()) {
                        int i = r.nextInt(taps.size());
                        q.push(new SearchNode(node.gs.clone(), taps.get(i), node));
                        taps.remove(i);
                    }
                }
            }
        }
        System.out.println("best score" + bestScoreNode.path());
        System.out.println("best left" + bestLeftNode.path());

    }

    @Override
    public void run() {
        this.search();
    }
}
