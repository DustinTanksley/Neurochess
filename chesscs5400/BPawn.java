package chesscs5400;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

class BPawn extends CP {
    
    BPawn() {
        Random x=new Random();
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                zo[i][j]=x.nextInt();
                Zo[i][j]=x.nextInt();
            }
        }
        color = true;
        type = 2;
        basevalue = 4;
        am=.3;
        dm=.2;
        sm=.1;
         try {
            icon = ImageIO.read(Class.class.getResourceAsStream("/resource/BlackPawn.png"));
        } catch (IOException ex) {
            System.out.println("Image not Found");
        } catch (IllegalArgumentException ex){
            System.out.println("Image Directory not Found");
        }
    }

    @Override  // need to add promotion, and check attacks are in bound
    public ArrayList<Action> GenerateMoves(CP[][] board, int x, int y, int[] enpassant, int[] kingloc, boolean[] castle, Action PM,boolean sc) {
        ArrayList<Action> ret = new ArrayList();
//        CP[][] BTC;
        Action a;
        if (board[x][y + 1] == null) {
            a=new Action(x, y, x, y + 1, false);
//            BTC = PseudoBoard(board);
//            BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
//            BTC[a.xfrom][a.yfrom] = null;
            a.TempPiece=board[a.xto][a.yto];
            board[a.xto][a.yto] = board[a.xfrom][a.yfrom];
            board[a.xfrom][a.yfrom] = null;
            if (BetterCheckCheck(board, kingloc, color,PM,a,sc)) {
                a.EnPassant = null;
                a.kings = kingloc;
                a.castle = castle;
                a.special = false;
                ret.add(a);
                if(a.yto==7){
                    AddPromos(ret,a);
                }
            }
            board[a.xfrom][a.yfrom] = board[a.xto][a.yto];
            board[a.xto][a.yto] = a.TempPiece;
            if(y==1 && board[x][3]==null){
                a=new Action(x, y, x, 3, false);
//                BTC = PseudoBoard(board);
//                BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
//                BTC[a.xfrom][a.yfrom] = null;
                a.TempPiece=board[a.xto][a.yto];
                board[a.xto][a.yto] = board[a.xfrom][a.yfrom];
                board[a.xfrom][a.yfrom] = null;
                if (BetterCheckCheck(board, kingloc, color,PM,a,sc)) {
                    a.EnPassant = new int[]{x,2};
                    a.kings = kingloc;
                    a.castle = castle;
                    a.special = false;
                    ret.add(a);
                }
                board[a.xfrom][a.yfrom] = board[a.xto][a.yto];
                board[a.xto][a.yto] = a.TempPiece;
            }
        }
        if(x>0 && board[x-1][y+1]!=null && !board[x-1][y+1].color){
            a=new Action(x, y, x-1, y + 1, true);
//            BTC = PseudoBoard(board);
//            BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
//            BTC[a.xfrom][a.yfrom] = null;
            a.TempPiece=board[a.xto][a.yto];
            board[a.xto][a.yto] = board[a.xfrom][a.yfrom];
            board[a.xfrom][a.yfrom] = null;
            if (BetterCheckCheck(board, kingloc, color,PM,a,sc)) {
                a.EnPassant = null;
                a.kings = kingloc;
                a.castle = castle;
                a.special = false;
                ret.add(a);
                if(a.yto==7){
                    AddPromos(ret,a);
                }
            }
            board[a.xfrom][a.yfrom] = board[a.xto][a.yto];
            board[a.xto][a.yto] = a.TempPiece;
        }
        if(x<7 && board[x+1][y+1]!=null && !board[x+1][y+1].color){
            a=new Action(x, y, x+1, y + 1, true);
//            BTC = PseudoBoard(board);
//            BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
//            BTC[a.xfrom][a.yfrom] = null;
            a.TempPiece=board[a.xto][a.yto];
            board[a.xto][a.yto] = board[a.xfrom][a.yfrom];
            board[a.xfrom][a.yfrom] = null;
            if (BetterCheckCheck(board, kingloc, color,PM,a,sc)) {
                a.EnPassant = null;
                a.kings = kingloc;
                a.castle = castle;
                a.special = false;
                ret.add(a);
                if(a.yto==7){
                    AddPromos(ret,a);
                }
            }
            board[a.xfrom][a.yfrom]=board[a.xto][a.yto];
            board[a.xto][a.yto]=a.TempPiece;
        }
        if(enpassant!=null){
            if(Math.abs(x-enpassant[0])==1 && y==4){  //Enpassant requires move extensive check... well checks. Maybe all specials do. 
                a=new Action(x, y, enpassant[0], enpassant[1], false);
                a.TempPiece=board[a.xto][a.yfrom];
                board[a.xto][a.yto] = board[a.xfrom][a.yfrom];
                board[a.xfrom][a.yfrom] = null;
                board[a.xto][a.yfrom]=null;
                if (CheckCheck(board, kingloc, color)) { 
                    a.EnPassant = null;
                    a.kings = kingloc;
                    a.castle = castle;
                    a.special = true;
                    ret.add(a);
                }
                board[a.xfrom][a.yfrom] = board[a.xto][a.yto];
                board[a.xto][a.yto]=null;
                board[a.xto][a.yfrom] = a.TempPiece;
            }
        }
        return ret;
    }

    @Override
    public boolean ProcessAttack(CP[][] board, Action a, int[] enpassant, int[] kingloc, boolean[] castle) {
        if (Math.abs(a.xfrom - a.xto) == 1 && a.yfrom == a.yto - 1 && board[a.xto][a.yto].color != color) {
            CP[][] BTC = PseudoBoard(board);
            BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
            BTC[a.xfrom][a.yfrom] = null;
            if (CheckCheck(BTC, kingloc, color)) {
                a.EnPassant = null;
                a.kings = kingloc;
                a.castle = castle;
                a.special = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean ProcessMove(CP[][] board, Action a, int[] enpassant, int[] kingloc, boolean[] castle) {
        if (a.xfrom == a.xto) {
            if (board[a.xto][a.yto] == null) {
                if (a.yfrom == a.yto - 1) {
                    a.EnPassant = null;
                } else if (a.yfrom == a.yto - 2 && a.yfrom == 1 && board[a.xto][2] == null) {
                    a.EnPassant = new int[]{a.xfrom, 2};
                } else {
                    return false;
                }
                CP[][] BTC = PseudoBoard(board);
                BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
                BTC[a.xfrom][a.yfrom] = null;
                if (CheckCheck(BTC, kingloc, color)) {
                    a.kings = kingloc;
                    a.castle = castle;
                    a.special = false;
                    return true;
                }
            }
        } else if (enpassant != null && enpassant[0] == a.xto && enpassant[1] == a.yto && a.yfrom == 4 && Math.abs(a.xto - a.xfrom) == 1) {
            CP[][] BTC = PseudoBoard(board);
            BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
            BTC[a.xfrom][a.yfrom] = null;
            BTC[a.xto][a.yfrom] = null;
            if (CheckCheck(BTC, kingloc, color)) {
                a.EnPassant = null;
                a.kings = kingloc;
                a.castle = castle;
                a.special = true;
                return true;
            }
        }
        return false;
    }
    public void AddPromos(ArrayList<Action> A, Action a){
		a.promotion=0;
        Action temp=new Action(a);
        temp.promotion=1;
        A.add(temp);
        temp=new Action(a);
        temp.promotion=2;
        A.add(temp);
        temp=new Action(a);
        temp.promotion=3;
        A.add(temp);
        
    }

    @Override
    public int Evaluate(CP[][] board, int x, int y) {
        
        int[] pawnscore={50,52,54,55,60,65,75,90};
        int basescore=pawnscore[y];
            for (int k = y+1; k < 8; k++) {
            if (board[x][k]!=null && board[x][k].type == 2) {
                basescore += -2;
            }
        }
        boolean nopiece = true;
        for (int k = y+1; k < 8; k++) {
            if (board[x][k] != null && !board[x][k].color) {
                nopiece = false;
            }
        }
        if (nopiece) {
            basescore += 5;
        }
        if (x > 0 && board[x - 1][y + 1] != null) {

            if (board[x - 1][y + 1].type == 7 && board[x - 1][y + 1].color) {
                basescore += 10;
            } else if (board[x - 1][y + 1].color) {
                basescore += 1;
            } else {
                basescore += board[x - 1][y + 1].basevalue;
            }
        }
        if (x < 7 && board[x + 1][y + 1] != null) {
            if (board[x + 1][y + 1].type == 7 && board[x + 1][y + 1].color) {
                basescore += 10;
            } else if (board[x + 1][y + 1].color) {
                basescore += 1;
            } else {
                basescore += board[x + 1][y + 1].basevalue;
            }
        }
        if(x==3 || x==4){
			if(y==3 || y==4){
				basescore+=5;
			}
		}
        
        return -basescore;
    }
        
    
    
}
