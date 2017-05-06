/**
 * This is where you build your AI for the Chess game.
 */
package games.chess;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashSet;
import java.util.Random;
import joueur.BaseAI;
import chesscs5400.*;

/**
 * This is where you build your AI for the Chess game.
 */
public class AI extends BaseAI {

    CB MyGame; //My personal Data Structure to hold the game information.
    long Timer;
    HashSet<Action> HistoryTable = new HashSet();
    HashSet<Action> HistoryTable2 = new HashSet();

    /**
     * This is the Game object itself, it contains all the information about the
     * current game
     */
    public Game game;

    /**
     * This is your AI's player. This AI class is not a player, but it should
     * command this Player.
     */
    public Player player;

    /**
     * This returns your AI's name to the game server. Just replace the string.
     *
     * @return string of you AI's name
     */
    public String getName() {
        return "NeuroChess"; // REPLACE THIS WITH YOUR TEAM NAME!
    }

    /**
     * This is automatically called when the game first starts, once the Game
     * object and all GameObjects have been initialized, but before any players
     * do anything. This is a good place to initialize any variables you add to
     * your AI, or start tracking game objects.
     */
    public void start() {
        super.start();
    }

    /**
     * This is automatically called every time the game (or anything in it)
     * updates. If a function you call triggers an update this will be called
     * before that function returns.
     */
    public void gameUpdated() {
        super.gameUpdated();
    }

    /**
     * This is automatically called when the game ends. You can do any cleanup
     * of you AI here, or do custom logging. After this function returns the
     * application will close.
     *
     * @param won true if your player won, false otherwise
     * @param name reason">a string explaining why you won or lost
     */
    public void ended(boolean won, String reason) {
        super.ended(won, reason);
    }

    /**
     * This is called every time it is this AI.player's turn.
     *
     * @return Represents if you want to end your turn. True means end your
     * turn, False means to keep your turn going and re-call this function.
     */
    public boolean runTurn() {
        MyGame = new CB(game.fen);
        long start = System.nanoTime(); //finds current system time
        Timer = (long) this.player.timeRemaining; //gets time remaining and converts to long Interger
        Timer = Timer / 10; // divides Time remaining into 1/10th
        Timer += start; //Sets an end time that is 1/10th of remaining time in the future. Once this time is past, 
        //The best move found is returned
        Move last;
        Action[] threefold = new Action[8];
        if (game.moves.isEmpty()) {
            MyGame.PreviousMove = new Action(0, 0, 0, 0, false);
        } else {
            last = game.moves.get(game.moves.size() - 1);
            MyGame.PreviousMove = new Action((int) last.fromFile.charAt(0) - 'a', 8 - last.fromRank, (int) last.toFile.charAt(0) - 'a', 8 - last.toRank, false);
        }
        if (game.moves.size() < 8) {
            for (int i = 0; i < 8; i++) {
                threefold[i] = new Action(0, 0, 0, 0, false);
            }
        } else {
            for (int i = 1; i < 9; i++) {
                last = game.moves.get(game.moves.size() - i);
                threefold[8 - i] = new Action((int) last.fromFile.charAt(0) - 'a', 8 - last.fromRank, (int) last.toFile.charAt(0) - 'a', 8 - last.toRank, false);
            }
        }
        MyGame.PreviousMove.special = true; // This "assumes" the last move was a castle/enpassant, which causes a more through check for check. 
        //I could set this to true, if I am in check, but the performance difference (1 trial) is absolutely trival. 
        Action a = null;
        for (int i = 1; i < 6; i++) {
            a = AI(MyGame, !MyGame.turn, i, 4, threefold);// 5 ply search with possible 4 ply Quiescential search
            HistoryTable = HistoryTable2;
            HistoryTable2 = new HashSet();
        }
        //Also it is !MyGame.turn since turn is false for white, but should be true for the variable maximize (white is positive score)
        Piece selectedPiece = null;
        for (Piece p : this.player.pieces) { //parse the piece from, and select it
            if (p.file.charAt(0) == ((char) ('a' + a.xfrom)) && p.rank == 8 - a.yfrom) {
                selectedPiece = p;
                break;
            }
        }
        String selectedFile = "" + (char) (((int) 'a') + a.xto); //parse the destination
        int selectedRank = 8 - a.yto;


        if (a.promotion != -1) {  //if a promotion, send the needed promotion details.
            selectedPiece.move(selectedFile, selectedRank, parsePromo(a.promotion));
        } else {
            selectedPiece.move(selectedFile, selectedRank);  //Make the move
        }
        return true; // signify we are done with our turn.
    }

    public Action AI(CB s, boolean maximize, int depth, int depth2, Action[] three) { // Gives the chosen move, maximize allows choice of black/white move
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

    public int MaxP(CB s, int alpha, int beta, int depth, int depth2, boolean Quiescential, Action[] three) {
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

    public int MinP(CB s, int alpha, int beta, int depth, int depth2, boolean Quiescential, Action[] three) {
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

    public String parsePromo(int promo) {
        switch (promo) {
            case 0:
                return "Queen";
            case 1:
                return "Rook";
            case 2:
                return "Knight";
            case 3:
                return "Bishop";
        }
        return "";
    }

    public boolean ThreefoldCheck(Action[] acts) {
        return acts[0].equals(acts[4]) && acts[1].equals(acts[5]) && acts[2].equals(acts[6]) && acts[3].equals(acts[7]);
    }

    public boolean CheckTime() { //If Moved past the set end time, return true, else return false;
        return System.nanoTime() > Timer;
    }
}
