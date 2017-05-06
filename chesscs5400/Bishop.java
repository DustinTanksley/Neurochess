package chesscs5400;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

class Bishop extends CP {
    
    Bishop(boolean color) {
        Random x=new Random();
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                zo[i][j]=x.nextInt();
                Zo[i][j]=x.nextInt();
            }
        }
        this.color = color;
        type = 3;
        basevalue = 8;
        am=.2;
        dm=.2;
        sm=.3;
          try {
            if (color) {
                icon = ImageIO.read(Class.class.getResourceAsStream("/resource/BlackBishop.png"));
            } else {
                icon = ImageIO.read(Class.class.getResourceAsStream("/resource/WhiteBishop.png"));
            }
        } catch (IOException ex) {
            System.out.println("Image not Found");
        }  catch (IllegalArgumentException ex){
            System.out.println("Image Directory not Found");
        }
    }

    @Override
    public ArrayList<Action> GenerateMoves(CP[][] board, int x, int y, int[] enpassant, int[] kingloc, boolean[] castle, Action PM,boolean sc) {
        ArrayList<Action> ret = new ArrayList();
        int dirx,diry,xtemp,ytemp;
        
        dirx=1;diry=1;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            addValid(ret, new Action(x,y,xtemp,ytemp,false),board,enpassant,kingloc,castle,PM,sc);
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color){
            addValid(ret, new Action(x,y,xtemp,ytemp,true),board,enpassant,kingloc,castle,PM,sc);
        }
        
        dirx=-1;diry=1;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            addValid(ret, new Action(x,y,xtemp,ytemp,false),board,enpassant,kingloc,castle,PM,sc);
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color){
            addValid(ret, new Action(x,y,xtemp,ytemp,true),board,enpassant,kingloc,castle,PM,sc);
        }
        
        dirx=1;diry=-1;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            addValid(ret, new Action(x,y,xtemp,ytemp,false),board,enpassant,kingloc,castle,PM,sc);
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color){
            addValid(ret, new Action(x,y,xtemp,ytemp,true),board,enpassant,kingloc,castle,PM,sc);
        }
        
        dirx=-1;diry=-1;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            addValid(ret, new Action(x,y,xtemp,ytemp,false),board,enpassant,kingloc,castle,PM,sc);
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color){
            addValid(ret, new Action(x,y,xtemp,ytemp,true),board,enpassant,kingloc,castle,PM,sc);
        }
        
        return ret;
    }

    @Override
    public boolean ProcessAttack(CP[][] board, Action a, int[] enpassant, int[] kingloc, boolean[] castle) {
        return board[a.xto][a.yto].color != color && ProcessMove(board, a, enpassant, kingloc, castle);
    }

    @Override
    public boolean ProcessMove(CP[][] board, Action a, int[] enpassant, int[] kingloc, boolean[] castle) {
        int offset = 1;
        if (Math.abs(a.xfrom - a.xto) == Math.abs(a.yfrom - a.yto)) {
            if (a.xfrom < a.xto) {
                if (a.yfrom < a.yto) {
                    while (a.xfrom + offset < a.xto) {
                        if (board[a.xfrom + offset][a.yfrom + offset] != null) {
                            return false;
                        }
                        offset++;
                    }
                } else {
                    while (a.xfrom + offset < a.xto) {
                        if (board[a.xfrom + offset][a.yfrom - offset] != null) {
                            return false;
                        }
                        offset++;
                    }
                }
            } else {
                if (a.yfrom < a.yto) {
                    while (a.xfrom - offset > a.xto) {
                        if (board[a.xfrom - offset][a.yfrom + offset] != null) {
                            return false;
                        }
                        offset++;
                    }
                } else {
                    while (a.xfrom - offset > a.xto) {
                        if (board[a.xfrom - offset][a.yfrom - offset] != null) {
                            return false;
                        }
                        offset++;
                    }
                }
            }
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
    
    public void addValid(ArrayList<Action> acts,Action a,CP[][] board, int[] enpassant, int[] kingloc, boolean[] castle, Action PM,boolean sc){
//        CP[][] BTC = PseudoBoard(board);
//        BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
//        BTC[a.xfrom][a.yfrom] = null;
        a.TempPiece=board[a.xto][a.yto];
        if(a.attack && a.TempPiece.basevalue<=basevalue){
            a.Q=true;
        }
        board[a.xto][a.yto] = board[a.xfrom][a.yfrom];
        board[a.xfrom][a.yfrom] = null;
        if (BetterCheckCheck(board, kingloc, color,PM,a,sc)) {
            a.EnPassant = null;
            a.kings = kingloc;
            a.castle = castle;
            a.special = false;
            acts.add(a);
        }
        board[a.xfrom][a.yfrom]=board[a.xto][a.yto];
        board[a.xto][a.yto]=a.TempPiece;
    }
    @Override
    public int Evaluate(CP[][] board, int x, int y) {
        
        
        int basescore=100;
        int i = x + 1;
        int j = y + 1;
        while (i < 8 && j < 8 && board[i][j] == null) {
            basescore += 1;
            i += 1;
            j += 1;
        }
        if (i < 8 && j < 8) {
            if (board[i][j].type == 7 && board[i][j].color != color) {
                basescore += 10;
            } else if (board[i][j].color == color && board[i][j].basevalue>basevalue) {
                basescore += 1;
            } else {
                basescore += board[i][j].basevalue;
            }
        }
        i = x - 1;
        j = y + 1;
        while (i >= 0 && j < 8 && board[i][j] == null) {
            basescore += 1;
            i += -1;
            j += 1;
        }
        if (i >= 0 && j < 8) {
            if (board[i][j].type == 7 && board[i][j].color != color) {
                basescore += 10;
            } else if (board[i][j].color == color && board[i][j].basevalue>basevalue) {
                basescore += 1;
            } else {
                basescore += board[i][j].basevalue;
            }
        }
        i = x - 1;
        j = y - 1;
        while (i >= 0 && j >= 0 && board[i][j] == null) {
            basescore += 1;
            i += -1;
            j += -1;
        }
        if (i >= 0 && j >= 0) {
            if (board[i][j].type == 7 && board[i][j].color != color) {
                basescore += 10;
            } else if (board[i][j].color == color && board[i][j].basevalue>basevalue) {
                basescore += 1;
            } else {
                basescore += board[i][j].basevalue;
            }
        }
                    
        i = x + 1;
        j = y - 1;
        while (i < 8 && j >= 0 && board[i][j] == null) {
            basescore += 1;
            i += 1;
            j += -1;
        }
        if (i < 8 && j >= 0) {
            if (board[i][j].type == 7 && board[i][j].color != color) {
                basescore += 10;
            } else if (board[i][j].color == color && board[i][j].basevalue>basevalue) {
                basescore += 1;
            } else {
                basescore += board[i][j].basevalue;
            }
        }
        
        return color? -basescore:basescore;
        
    }
}
