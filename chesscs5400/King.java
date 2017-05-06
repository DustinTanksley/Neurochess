package chesscs5400;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

class King extends CP {
    
    King(boolean color) {
        Random x=new Random();
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                zo[i][j]=x.nextInt();
                Zo[i][j]=x.nextInt();
            }
        }
        this.color = color;
        type = 7;
        basevalue = 0;
        dm=0;
        sm=1;
         try {
            if (color) {
                icon = ImageIO.read(Class.class.getResourceAsStream("/resource/BlackKing.png"));
            } else {
                icon = ImageIO.read(Class.class.getResourceAsStream("/resource/WhiteKing.png"));
            }
        } catch (IOException ex) {
            System.out.println("Image not Found");
        } catch (IllegalArgumentException ex){
            System.out.println("Image Directory not Found");
        }
    }

    @Override
    public ArrayList<Action> GenerateMoves(CP[][] board, int x, int y, int[] enpassant, int[] kingloc, boolean[] castle, Action PM,boolean sc) {
        ArrayList<Action> ret = new ArrayList();
        for(int i=x-1;i<=x+1;i++){ //this checks self, but same color, so fine.
            for(int j=y-1;j<=y+1;j++){ //king loc and castle handled by addValid
                if(CheckBounds(i,j) && board[i][j]==null){
                    addValid(ret,new Action(x,y,i,j,false),board,enpassant,kingloc,castle);
                }
                else if(CheckBounds(i,j) && board[i][j].color!=color){
                    addValid(ret,new Action(x,y,i,j,true),board,enpassant,kingloc,castle);
                }
            }
        }
        //Generate Castles, addValid not capable of handling
        Action a;
        if(color){
            if(castle[3] && board[7][y] != null && board[7][y].type == 5 && board[7][y].color){
                if (board[5][y] == null && board[6][y] == null) {
                    CP[][] BTCOne = PseudoBoard(board);
                    BTCOne[6][y] = BTCOne[x][y];
                    BTCOne[x][y] = null;
                    int[] copyOne = kingloc.clone();
                    copyOne[2] = 6;
                    copyOne[3] = y;
                    CP[][] BTCTwo = PseudoBoard(board);
                    BTCTwo[5][y] = BTCTwo[x][y];
                    BTCTwo[x][y] = null;
                    int[] copyTwo = kingloc.clone();
                    copyTwo[2] = 5;
                    copyTwo[3] = y;
                    if (CheckCheck(board, kingloc, color) && CheckCheck(BTCOne, copyOne, color) && CheckCheck(BTCTwo, copyTwo, color)) {
                        a=new Action(x,y,6,y,false);
                        a.special = true;
                        a.castle = castle.clone();
                        a.castle[2] = false;
                        a.castle[3] = false;
                        a.EnPassant = null;
                        a.kings = copyOne;
						a.Q=true;
                        ret.add(a);
                    }
                }
            }
            if(castle[2] && board[0][y] != null && board[0][y].type == 5 && board[0][y].color){
                if (board[1][y] == null && board[2][y] == null && board[3][y] == null) {
                    CP[][] BTCOne = PseudoBoard(board);
                    BTCOne[2][y] = BTCOne[x][y];
                    BTCOne[x][y] = null;
                    int[] copyOne = kingloc.clone();
                    copyOne[2] = 2;
                    copyOne[3] = y;
                    CP[][] BTCTwo = PseudoBoard(board);
                    BTCTwo[3][y] = BTCTwo[x][y];
                    BTCTwo[x][y] = null;
                    int[] copyTwo = kingloc.clone();
                    copyTwo[2] = 3;
                    copyTwo[3] = y;
                    if (CheckCheck(board, kingloc, color) && CheckCheck(BTCOne, copyOne, color) && CheckCheck(BTCTwo, copyTwo, color)) {
                        a=new Action(x,y,2,y,false);
                        a.special = true;
                        a.castle = castle.clone();
                        a.castle[2] = false;
                        a.castle[3] = false;
                        a.EnPassant = null;
                        a.kings = copyOne;
						a.Q=true;
                        ret.add(a);
                    }
                }
            }
        }
        else{
            if(castle[1] && board[7][y] != null && board[7][y].type == 5 && !board[7][y].color){
                if (board[5][y] == null && board[6][y] == null) {
                    CP[][] BTCOne = PseudoBoard(board);
                    BTCOne[6][y] = BTCOne[x][y];
                    BTCOne[x][y] = null;
                    int[] copyOne = kingloc.clone();
                    copyOne[0] = 6;
                    copyOne[1] = y;
                    CP[][] BTCTwo = PseudoBoard(board);
                    BTCTwo[5][y] = BTCTwo[x][y];
                    BTCTwo[x][y] = null;
                    int[] copyTwo = kingloc.clone();
                    copyTwo[0] = 5;
                    copyTwo[1] = y;
                    if (CheckCheck(board, kingloc, color) && CheckCheck(BTCOne, copyOne, color) && CheckCheck(BTCTwo, copyTwo, color)) {
                        a=new Action(x,y,6,y,false);
                        a.special = true;
                        a.castle = castle.clone();
                        a.castle[0] = false;
                        a.castle[1] = false;
                        a.EnPassant = null;
                        a.kings = copyOne;
						a.Q=true;
                        ret.add(a);
                    }
                }
            }
            if(castle[0] && board[0][y] != null && board[0][y].type == 5 && !board[0][y].color){
                if (board[1][y] == null && board[2][y] == null && board[3][y] == null) {
                    CP[][] BTCOne = PseudoBoard(board);
                    BTCOne[2][y] = BTCOne[x][y];
                    BTCOne[x][y] = null;
                    int[] copyOne = kingloc.clone();
                    copyOne[0] = 2;
                    copyOne[1] = y;
                    CP[][] BTCTwo = PseudoBoard(board);
                    BTCTwo[3][y] = BTCTwo[x][y];
                    BTCTwo[x][y] = null;
                    int[] copyTwo = kingloc.clone();
                    copyTwo[0] = 3;
                    copyTwo[1] = y;
                    if (CheckCheck(board, kingloc, color) && CheckCheck(BTCOne, copyOne, color) && CheckCheck(BTCTwo, copyTwo, color)) {
                        a=new Action(x,y,2,y,false);
                        a.special = true;
                        a.castle = castle.clone();
                        a.castle[0] = false;
                        a.castle[1] = false;
                        a.EnPassant = null;
                        a.kings = copyOne;
						a.Q=true;
                        ret.add(a);
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public boolean ProcessAttack(CP[][] board, Action a, int[] enpassant, int[] kingloc, boolean[] castle) {
        if (Math.abs(a.xfrom - a.xto) <= 1 && Math.abs(a.yfrom - a.yto) <= 1 && board[a.xto][a.yto].color != color) {
            CP[][] BTC = PseudoBoard(board);
            BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
            BTC[a.xfrom][a.yfrom] = null;
            int[] copy = kingloc.clone();
            if (color) {
                copy[2] = a.xto;
                copy[3] = a.yto;
            } else {
                copy[0] = a.xto;
                copy[1] = a.yto;
            }
            if (CheckCheck(BTC, copy, color)) {
                a.EnPassant = null;
                a.kings = copy;
                a.special = false;
                a.castle = castle.clone();
                if (color) {
                    a.castle[2] = false;
                    a.castle[3] = false;
                } else {
                    a.castle[0] = false;
                    a.castle[1] = false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean ProcessMove(CP[][] board, Action a, int[] enpassant, int[] kingloc, boolean[] castle) {
        if (Math.abs(a.xfrom - a.xto) <= 1 && Math.abs(a.yfrom - a.yto) <= 1) {
            CP[][] BTC = PseudoBoard(board);
            BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
            BTC[a.xfrom][a.yfrom] = null;
            int[] copy = kingloc.clone();
            if (color) {
                copy[2] = a.xto;
                copy[3] = a.yto;
            } else {
                copy[0] = a.xto;
                copy[1] = a.yto;
            }
            if (CheckCheck(BTC, copy, color)) {
                a.EnPassant = null;
                a.kings = copy;
                a.castle = castle.clone();
                a.special = false;
                if (color) {
                    a.castle[2] = false;
                    a.castle[3] = false;
                } else {
                    a.castle[0] = false;
                    a.castle[1] = false;
                }
                return true;
            }
        } else if (a.xto == 2 || a.xto == 6) {
            if (a.yfrom == a.yto) {
                if (color) {
                    if (a.xto == 2 && castle[2] && board[0][a.yfrom] != null && board[0][a.yfrom].type == 5 && board[0][a.yfrom].color) {
                        if (board[1][a.yfrom] == null && board[2][a.yfrom] == null && board[3][a.yfrom] == null) {
                            CP[][] BTCOne = PseudoBoard(board);
                            BTCOne[a.xto][a.yto] = BTCOne[a.xfrom][a.yfrom];
                            BTCOne[a.xfrom][a.yfrom] = null;
                            int[] copyOne = kingloc.clone();
                            copyOne[2] = a.xto;
                            copyOne[3] = a.yto;
                            CP[][] BTCTwo = PseudoBoard(board);
                            BTCTwo[3][a.yto] = BTCTwo[a.xfrom][a.yfrom];
                            BTCTwo[a.xfrom][a.yfrom] = null;
                            int[] copyTwo = kingloc.clone();
                            copyTwo[2] = 3;
                            copyTwo[3] = a.yto;
                            if (CheckCheck(board, kingloc, color) && CheckCheck(BTCOne, copyOne, color) && CheckCheck(BTCTwo, copyTwo, color)) {
                                a.special = true;
                                a.castle = castle.clone();
                                a.castle[2] = false;
                                a.castle[3] = false;
                                a.EnPassant = null;
                                a.kings = copyOne;
                                return true;
                            }
                        }
                        //                        return ProcessCastleBlackDirectOne(board,a,enpassant,kingloc,castle);
                    } else if (a.xto == 6 && castle[3] && board[7][a.yfrom] != null && board[7][a.yfrom].type == 5 && board[7][a.yfrom].color) {
                        if (board[5][a.yfrom] == null && board[6][a.yfrom] == null) {
                            CP[][] BTCOne = PseudoBoard(board);
                            BTCOne[a.xto][a.yto] = BTCOne[a.xfrom][a.yfrom];
                            BTCOne[a.xfrom][a.yfrom] = null;
                            int[] copyOne = kingloc.clone();
                            copyOne[2] = a.xto;
                            copyOne[3] = a.yto;
                            CP[][] BTCTwo = PseudoBoard(board);
                            BTCTwo[5][a.yto] = BTCTwo[a.xfrom][a.yfrom];
                            BTCTwo[a.xfrom][a.yfrom] = null;
                            int[] copyTwo = kingloc.clone();
                            copyTwo[2] = 5;
                            copyTwo[3] = a.yto;
                            if (CheckCheck(board, kingloc, color) && CheckCheck(BTCOne, copyOne, color) && CheckCheck(BTCTwo, copyTwo, color)) {
                                a.special = true;
                                a.castle = castle.clone();
                                a.castle[2] = false;
                                a.castle[3] = false;
                                a.EnPassant = null;
                                a.kings = copyOne;
                                return true;
                            }
                        }
                    }
                } else {
                    if (a.xto == 2 && castle[0] && board[0][a.yfrom] != null && board[0][a.yfrom].type == 5 && !board[0][a.yfrom].color) {
                        if (board[3][a.yfrom] == null && board[2][a.yfrom] == null && board[1][a.yfrom] == null) {
                            CP[][] BTCOne = PseudoBoard(board);
                            BTCOne[a.xto][a.yto] = BTCOne[a.xfrom][a.yfrom];
                            BTCOne[a.xfrom][a.yfrom] = null;
                            int[] copyOne = kingloc.clone();
                            copyOne[0] = a.xto;
                            copyOne[1] = a.yto;
                            CP[][] BTCTwo = PseudoBoard(board);
                            BTCTwo[3][a.yto] = BTCTwo[a.xfrom][a.yfrom];
                            BTCTwo[a.xfrom][a.yfrom] = null;
                            int[] copyTwo = kingloc.clone();
                            copyTwo[0] = 3;
                            copyTwo[1] = a.yto;
                            if (CheckCheck(board, kingloc, color) && CheckCheck(BTCOne, copyOne, color) && CheckCheck(BTCTwo, copyTwo, color)) {
                                a.special = true;
                                a.castle = castle.clone();
                                a.castle[0] = false;
                                a.castle[1] = false;
                                a.EnPassant = null;
                                a.kings = copyOne;
                                return true;
                            }
                        }
                    } else if (a.xto == 6 && castle[1] && board[7][a.yfrom] != null && board[7][a.yfrom].type == 5 && !board[7][a.yfrom].color) {
                        if (board[5][a.yfrom] == null && board[6][a.yfrom] == null) {
                            CP[][] BTCOne = PseudoBoard(board);
                            BTCOne[a.xto][a.yto] = BTCOne[a.xfrom][a.yfrom];
                            BTCOne[a.xfrom][a.yfrom] = null;
                            int[] copyOne = kingloc.clone();
                            copyOne[0] = a.xto;
                            copyOne[1] = a.yto;
                            CP[][] BTCTwo = PseudoBoard(board);
                            BTCTwo[5][a.yto] = BTCTwo[a.xfrom][a.yfrom];
                            BTCTwo[a.xfrom][a.yfrom] = null;
                            int[] copyTwo = kingloc.clone();
                            copyTwo[0] = 5;
                            copyTwo[1] = a.yto;
                            if (CheckCheck(board, kingloc, color) && CheckCheck(BTCOne, copyOne, color) && CheckCheck(BTCTwo, copyTwo, color)) {
                                a.special = true;
                                a.castle = castle.clone();
                                a.castle[0] = false;
                                a.castle[1] = false;
                                a.EnPassant = null;
                                a.kings = copyOne;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public void addValid(ArrayList<Action> acts,Action a,CP[][] board, int[] enpassant, int[] kingloc, boolean[] castle){
//        CP[][] BTC = PseudoBoard(board);
//        BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
//        BTC[a.xfrom][a.yfrom] = null;
        int[] copy = kingloc.clone();
        a.castle = castle.clone();
        if (color) {
            copy[2] = a.xto;
            copy[3] = a.yto;
            a.castle[2] = false;
            a.castle[3] = false;
        } else {
            copy[0] = a.xto;
            copy[1] = a.yto;
            a.castle[0] = false;
            a.castle[1] = false;
        }
        a.TempPiece=board[a.xto][a.yto];
        a.Q=a.attack;
        board[a.xto][a.yto] = board[a.xfrom][a.yfrom];
        board[a.xfrom][a.yfrom] = null;
        if (CheckCheck(board, copy, color)) {
            a.EnPassant = null;
            a.kings = copy;
            a.special = false;
			a.Q=true;
            acts.add(a);
        }
        board[a.xfrom][a.yfrom]=board[a.xto][a.yto];
        board[a.xto][a.yto]=a.TempPiece;
    }

    @Override
    public int Evaluate(CP[][] board, int x, int y) {
        return 0;
    }
}
