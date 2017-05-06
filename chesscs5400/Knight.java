package chesscs5400;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

class Knight extends CP {
    
    Knight(boolean color) {
        Random x=new Random();
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                zo[i][j]=x.nextInt();
                Zo[i][j]=x.nextInt();
            }
        }
        this.color = color;
        type = 4;
        basevalue = 8;
        am=.25;
        dm=.2;
        sm=.3;
         try {
            if (color) {
                icon = ImageIO.read(Class.class.getResourceAsStream("/resource/BlackKnight.png"));
            } else {
                icon = ImageIO.read(Class.class.getResourceAsStream("/resource/WhiteKnight.png"));
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
//        CP[][] BTC;
        Action a;
        int xtemp,ytemp;
        for(int[] offsets : KnightOffsets){
            xtemp=offsets[0]+x;
            ytemp=offsets[1]+y;
            if(CheckBounds(xtemp,ytemp)){
                if(board[xtemp][ytemp]==null){
                    a=new Action(x, y, xtemp, ytemp, false);
//                    BTC = PseudoBoard(board);
//                    BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
//                    BTC[a.xfrom][a.yfrom] = null;
                    a.TempPiece=board[a.xto][a.yto];
                    board[a.xto][a.yto] = board[a.xfrom][a.yfrom];
                    board[a.xfrom][a.yfrom] = null;
                    if (BetterCheckCheck(board, kingloc, color,PM,a,sc)) {
                        a.EnPassant = null;
                        a.kings = kingloc;
                        a.castle = castle;
                        a.special = false;
                        ret.add(a);
                    }
                    board[a.xfrom][a.yfrom]=board[a.xto][a.yto];
                    board[a.xto][a.yto]=a.TempPiece;
                } // could combine, but lose the info on if the move was an attack, better not to.
                else if(board[xtemp][ytemp].color!=color){
                    a=new Action(x, y, xtemp, ytemp, true);
                    
//                    BTC = PseudoBoard(board);
//                    BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
//                    BTC[a.xfrom][a.yfrom] = null;
                    a.TempPiece=board[a.xto][a.yto];
                    if(a.TempPiece.basevalue<=basevalue){
                        a.Q=true;
                    }
                    board[a.xto][a.yto] = board[a.xfrom][a.yfrom];
                    board[a.xfrom][a.yfrom] = null;
                    if (BetterCheckCheck(board, kingloc, color,PM,a,sc)) {
                        a.EnPassant = null;
                        a.kings = kingloc;
                        a.castle = castle;
                        a.special = false;
                        ret.add(a);
                    }
                    board[a.xfrom][a.yfrom]=board[a.xto][a.yto];
                    board[a.xto][a.yto]=a.TempPiece;
                }
            }
        }
        return ret;
    }

    @Override
    public boolean ProcessAttack(CP[][] board, Action a, int[] enpassant, int[] kingloc, boolean[] castle) {
        return board[a.xto][a.yto].color != color && ProcessMove(board, a, enpassant, kingloc, castle);
    }

    @Override
    public boolean ProcessMove(CP[][] board, Action a, int[] enpassant, int[] kingloc, boolean[] castle) {
        int temp = Math.abs(a.xfrom - a.xto);
        if (temp == 2) {
            if (Math.abs(a.yfrom - a.yto) == 1) {
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
        } else if (temp == 1) {
            if (Math.abs(a.yfrom - a.yto) == 2) {
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
        }
        return false;
    }
    @Override
    public int Evaluate(CP[][] board, int x, int y) {
        
        int basescore = 80;
        int i, j;
        for (int[] offsets : KnightOffsets) {
            i = offsets[0] + x;
            j = offsets[1] + y;
            if (CheckBounds(i, j)) {
                basescore += 3;
                if (board[i][j] != null) {
                    if (board[i][j].type == 7 && board[i][j].color != color) {
                        basescore += 10;
                    } else if (board[i][j].color == color && board[i][j].basevalue>basevalue) {
                        basescore += 1;
                    } else {
                        basescore += board[i][j].basevalue;
                    }
                }
            }

        }


        return color? -basescore:basescore;
    }
}