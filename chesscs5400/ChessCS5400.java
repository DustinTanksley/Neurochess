package chesscs5400;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChessCS5400 {

    static ChessGUI UI;
    //static HashMap<CB, Action> TranspositionTable = new HashMap();
    //static HashMap<CB, Action> NextTable = new HashMap();
    static HashSet<Action> HistoryTable= new HashSet();
    static HashSet<Action> HistoryTable2= new HashSet();

    public static void main(String[] args) {
        JFrame GUI = new JFrame();
        //This is setting to a specific fen
        CB test = new CB("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2");
        //This sets to a new board
        test = new CB();
        UI = new ChessGUI(test);
        GUI.add(UI);
        GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GUI.setSize(800, 600);
        GUI.setVisible(true);
        GUI.addMouseListener(MM());
        GUI.addMouseMotionListener(mousy());
        boolean GameOver = false;
		Action[] Threes=new Action[8];
        while (!GameOver) {
            ArrayList<Action> Moves = test.Actions();
            if (Moves.isEmpty()) {
                if (!test.turn && !test.Board[test.KingLoc[0]][test.KingLoc[1]].CheckCheck(test.Board, test.KingLoc, false)) {
                    JOptionPane.showMessageDialog(null, "Black Wins");
                    test = new CB();
                    GameOver = true;
                } else if (test.turn && !test.Board[test.KingLoc[2]][test.KingLoc[3]].CheckCheck(test.Board, test.KingLoc, true)) {
                    JOptionPane.showMessageDialog(null, "White Wins");
                    test = new CB();
                    GameOver = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Draw");
                    test = new CB();
                    GameOver = true;
                }
            }
            if (test.turn) {
                long start;
                Action a = null;
                for (int i = 1; i < 6; i++) {
                    HistoryTable=HistoryTable2;
                    HistoryTable2=new HashSet();
                    start = System.nanoTime();
                    a = AI(test, false, i, 5,Threes);
                    System.out.println(System.nanoTime() - start);
                }
                test.move(a);
                UI.repaint();
            }
//This code causes White to play
//            else{
//                Action a=AI(test,true,6);
//                //System.out.println(a.xfrom+" "+ a.yfrom+" "+ a.xto+" "+ a.yto);
//                TranspositionTable.clear();
//                test.move(a);
//                UI.repaint();
//            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChessCS5400.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static MouseListener MM() {
        MouseListener Mousey = new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                UI.ProcessPress();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                UI.ProcessRelease();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
        return Mousey;
    }

    static MouseMotionListener mousy() {
        MouseMotionListener listen = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                UI.ProcessMove();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        };
        return listen;
    }

    public static Action AI(CB s, boolean maximize, int depth, int depth2, Action[] three) { // Gives the chosen move, maximize allows choice of black/white move
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        Action chosen;
        ArrayList<Action> Moves = s.Actions();
        Action[] tf = new Action[8];
        for (int i = 0; i < 7; i++) {
            tf[i] = three[i + 1];
        }
        chosen = Moves.get(0);
        int temp;
        ArrayList<Action> NewList = new ArrayList(); //Moves best move to the front.
        for (Action a : Moves) {
            if (HistoryTable.contains(a)) {
                NewList.add(0, a);

            } else {
                NewList.add(a);
            }
        }
        Moves = NewList;
        for (Action a : Moves) {
            tf[7] = a;
            if (ThreefoldCheck(tf)) {
                a.score = 0;
                if (maximize && alpha < 0) {
                    chosen = a;
                    alpha = 0;
                }
                if (!maximize && beta > 0) {
                    chosen = a;
                    beta = 0;
                }
            } else if (maximize) {
                a.score = MinP(new CB(s, a), alpha, beta, depth - 1, depth2, a.Q, tf);
                if (a.score > alpha) {
                    chosen = a;
                    alpha = a.score;
                }
            } else {
                a.score = MaxP(new CB(s, a), alpha, beta, depth - 1, depth2, a.Q, tf);
                if (a.score < beta) {
                    chosen = a;
                    beta = a.score;
                }
            }
        }
        HistoryTable2.add(chosen);
        return chosen;
    }

    public static int MaxP(CB s, int alpha, int beta, int depth, int depth2, boolean Quiescential, Action[] three) {
        int temp;
        Action[] tf = new Action[8];
        for (int i = 0; i < 7; i++) {
            tf[i] = three[i + 1];
        }
		if (CheckTime()) { //if out of time, return a move that min player won't pick.
			System.out.println("Ran out of time");
            return Integer.MAX_VALUE;
        }
        else if ((depth <= 0 && (depth2 <= 0 || !(Quiescential || !s.QuiescentialCheckCheck())))) {
            return s.Evaluate();  //if depth limit reached, and not doing Quiescential, evaluate
        }  else if (depth <= 0) { // Quiescential search
            depth = 1;
            depth2 -= 1;
        }
        ArrayList<Action> Moves = s.Actions();
        if (Moves.isEmpty()) {
            if (!s.turn && !s.Board[s.KingLoc[0]][s.KingLoc[1]].CheckCheck(s.Board, s.KingLoc, false)) {
                return -1000000 - depth;
            } else if (s.turn && !s.Board[s.KingLoc[2]][s.KingLoc[3]].CheckCheck(s.Board, s.KingLoc, true)) {
                return 1000000 + depth;
            } else {
                return 0;
            }
        }
        for (Action a : Moves) {
            if (a.special || a.promotion != -1) { //if the board changes in a complex way, create a copy and change it
                tf[7] = a;//turns = 100 doesn't need checked, and Three folds either
                a.score = MinP(new CB(s, a), alpha, beta, depth - 1, depth2, a.Q, tf);
            } else { //Otherwise, make the move, then score, then undo.
                a.TempPiece = s.Board[a.xto][a.yto];
                s.Board[a.xto][a.yto] = s.Board[a.xfrom][a.yfrom];
                s.Board[a.xfrom][a.yfrom] = null;
                s.turn = !s.turn;
                temp = s.turns;
                s.turns += 1;
                if (a.attack || s.Board[a.xto][a.yto].basevalue == 1) {
                    s.turns = 0;
                }
                int[] tempK = s.KingLoc;
                s.KingLoc = a.kings;
                boolean[] tempC = s.canCastle;
                s.canCastle = a.castle;
                int[] TempENP = s.EnPassant;
                s.EnPassant = a.EnPassant;
                s.PreviousMove = a; //This doesn't need undone????
                tf[7] = a;
                if (s.turns == 100 || ThreefoldCheck(tf)) {
                    a.score = 0;
                } else {
                    a.score = MinP(s, alpha, beta, depth - 1, depth2, a.Q, tf);
                }
                s.Board[a.xfrom][a.yfrom] = s.Board[a.xto][a.yto];
                s.Board[a.xto][a.yto] = a.TempPiece;
                s.KingLoc = tempK;
                s.canCastle = tempC;
                s.EnPassant = TempENP;
                s.turn = !s.turn;
                s.turns = temp;
            }

            if (a.score >= beta) {
                return a.score;
            } else if (a.score > alpha) {
                alpha = a.score;
            }
        }
        return alpha;  // This is ok, because if no moves are greater then alpha, we have already found a move earlier that has scored alpha, and it will be selected

    }

    public static int MinP(CB s, int alpha, int beta, int depth, int depth2, boolean Quiescential, Action[] three) {
        int temp;
        Action[] tf = new Action[8];
        for (int i = 0; i < 7; i++) {
            tf[i] = three[i + 1];
        }
        if (CheckTime()) { //if out of time, return a move that max player won't pick.
            System.out.println("Ran out of Time");
            return Integer.MIN_VALUE;
        }
        else if ((depth <= 0 && (depth2 <= 0 || !(Quiescential || !s.QuiescentialCheckCheck())))) {
            return s.Evaluate();
        }  else if (depth <= 0) {
            depth = 1;
            depth2 -= 1;
        }
        ArrayList<Action> Moves = s.Actions();
        if (Moves.isEmpty()) {
            if (!s.turn && !s.Board[s.KingLoc[0]][s.KingLoc[1]].CheckCheck(s.Board, s.KingLoc, false)) {
                return -1000000 - depth;
            } else if (s.turn && !s.Board[s.KingLoc[2]][s.KingLoc[3]].CheckCheck(s.Board, s.KingLoc, true)) {
                return 1000000 + depth;
            } else {
                return 0;
            }
        }
        for (Action a : Moves) {
            if (a.special || a.promotion != -1) { //if the board changes in a complex way, create a copy and change it
                tf[7] = a;//turns = 100 doesn't need checked, and Three folds either
                a.score = MaxP(new CB(s, a), alpha, beta, depth - 1, depth2, a.Q, tf);
            } else { //Otherwise, make the move, then score, then undo.
                a.TempPiece = s.Board[a.xto][a.yto];
                s.Board[a.xto][a.yto] = s.Board[a.xfrom][a.yfrom];
                s.Board[a.xfrom][a.yfrom] = null;
                s.turn = !s.turn;
                temp = s.turns;
                s.turns += 1;
                if (a.attack || s.Board[a.xto][a.yto].basevalue == 1) {
                    s.turns = 0;
                }
                int[] tempK = s.KingLoc;
                s.KingLoc = a.kings;
                boolean[] tempC = s.canCastle;
                s.canCastle = a.castle;
                int[] TempENP = s.EnPassant;
                s.EnPassant = a.EnPassant;
                s.PreviousMove = a; //This doesn't need undone????
                tf[7] = a;
                if (s.turns == 100 || ThreefoldCheck(tf)) {
                    a.score = 0;
                } else {
                    a.score = MaxP(s, alpha, beta, depth - 1, depth2, a.Q, tf);
                }
                s.Board[a.xfrom][a.yfrom] = s.Board[a.xto][a.yto];
                s.Board[a.xto][a.yto] = a.TempPiece;
                s.KingLoc = tempK;
                s.canCastle = tempC;
                s.EnPassant = TempENP;
                s.turn = !s.turn;
                s.turns = temp;
            }
            if (a.score <= alpha) {
                return a.score;
            } else if (a.score < beta) {
                beta = a.score;
            }
        }
        return beta;  // This is ok, because if no moves are less then beta, we have already found a move earlier that has scored beta, and it will be selected
    }

	public static boolean ThreefoldCheck(Action[] acts) {
        return false;
    }

    public static boolean CheckTime() { //If Moved past the set end time, return true, else return false;
        return false;
    }
	
	}

