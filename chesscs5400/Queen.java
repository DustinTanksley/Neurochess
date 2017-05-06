package chesscs5400;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

class Queen extends CP{
    Queen(boolean color){
        Random x=new Random();
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                zo[i][j]=x.nextInt();
                Zo[i][j]=x.nextInt();
            }
        }
        this.color=color;
        type=6;
        basevalue=16;
        am=.15;
        dm=.05;
        sm=.8;
         try {
            if(color){icon=ImageIO.read(Class.class.getResourceAsStream("/resource/BlackQueen.png"));}
            else
            icon=ImageIO.read(Class.class.getResourceAsStream("/resource/WhiteQueen.png"));
        
        } 
        catch (IOException ex) { System.out.println("Image not Found");}  
         catch (IllegalArgumentException ex){
            System.out.println("Image Directory not Found");
        }
    }
    @Override  //could do new Bishop(color).gen moves + new rook(color).gen moves? a little bit slower but....
    public ArrayList<Action> GenerateMoves(CP[][] board,int x, int y,int[] enpassant,int[] kingloc, boolean[] castle,Action PM,boolean sc){
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
        
        dirx=1;diry=0;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            addValid(ret, new Action(x,y,xtemp,ytemp,false),board,enpassant,kingloc,castle,PM,sc);
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color){
            addValid(ret, new Action(x,y,xtemp,ytemp,true),board,enpassant,kingloc,castle,PM,sc);
        }
        
        dirx=-1;diry=0;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            addValid(ret, new Action(x,y,xtemp,ytemp,false),board,enpassant,kingloc,castle,PM,sc);
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color){
            addValid(ret, new Action(x,y,xtemp,ytemp,true),board,enpassant,kingloc,castle,PM,sc);
        }
        
        dirx=0;diry=-1;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            addValid(ret, new Action(x,y,xtemp,ytemp,false),board,enpassant,kingloc,castle,PM,sc);
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color){
            addValid(ret, new Action(x,y,xtemp,ytemp,true),board,enpassant,kingloc,castle,PM,sc);
        }
        
        dirx=0;diry=1;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            addValid(ret, new Action(x,y,xtemp,ytemp,false),board,enpassant,kingloc,castle,PM,sc);
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color){
            addValid(ret, new Action(x,y,xtemp,ytemp,true),board,enpassant,kingloc,castle,PM,sc);
        }
        
        return ret;
    }
    @Override  // need to add everything
    public boolean ProcessAttack(CP[][] board, Action a, int[] enpassant, int[] kingloc,boolean[] castle){
        return board[a.xto][a.yto].color!=color && ProcessMove(board,a,enpassant,kingloc,castle);
    }
    @Override // need to add everything
    public boolean ProcessMove(CP[][] board, Action a, int[] enpassant, int[] kingloc,boolean[] castle){
        int offset=1;
        if(Math.abs(a.xto-a.xfrom)==Math.abs(a.yto-a.yfrom)){ //diagonal
            if(a.xfrom<a.xto){
                if(a.yfrom<a.yto){
                    while(a.xfrom+offset<a.xto){
                        if(board[a.xfrom+offset][a.yfrom+offset]!=null){return false;}
                        offset++;
                    }
                }
                else{
                    while(a.xfrom+offset<a.xto){
                        if(board[a.xfrom+offset][a.yfrom-offset]!=null){return false;}
                        offset++;
                    }
                }
            }
            else{
                if(a.yfrom<a.yto){
                    while(a.xfrom-offset>a.xto){
                        if(board[a.xfrom-offset][a.yfrom+offset]!=null){return false;}
                        offset++;
                    }
                }
                else{
                    while(a.xfrom-offset>a.xto){
                        if(board[a.xfrom-offset][a.yfrom-offset]!=null){return false;}
                        offset++;
                    }
                }
            }
        }
        else if(a.xto-a.xfrom!=0){ //horizontal
            if(a.yfrom==a.yto){
                if(a.xto>a.xfrom){
                    while(a.xfrom+offset<a.xto){
                        if(board[a.xfrom+offset][a.yto]!=null){
                            return false;
                        }
                        offset++;
                    }
                }
                else{
                    while(a.xfrom-offset>a.xto){
                        if(board[a.xfrom-offset][a.yto]!=null){
                            return false;
                        }
                        offset++;
                    }
                }
            }
            else return false;
        }
        else if (a.yto - a.yfrom != 0) { //vertical
            if (a.yto > a.yfrom) {
                while (a.yfrom + offset < a.yto) {
                    if (board[a.xto][a.yto-offset] != null) {
                        return false;
                    }
                    offset++;
                }
            } else {
                while (a.yfrom - offset > a.yto) {
                    if (board[a.xto][a.yto+offset] != null) {
                        return false;
                    }
                    offset++;
                }
            }
        }
        else return false;
        CP[][] BTC=PseudoBoard(board);
        BTC[a.xto][a.yto]=BTC[a.xfrom][a.yfrom];
        BTC[a.xfrom][a.yfrom]=null;
        if(CheckCheck(BTC,kingloc,color)){
            a.EnPassant=null;
            a.kings=kingloc;
            a.castle=castle;
            a.special=false;
            return true;
        }
        return false;
    }
    
    public void addValid(ArrayList<Action> acts,Action a,CP[][] board, int[] enpassant, int[] kingloc, boolean[] castle,Action PM,boolean sc){
        a.TempPiece=board[a.xto][a.yto];
        a.Q=a.attack;
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
        int basescore=360;
        return color? -basescore:basescore;
    }
}