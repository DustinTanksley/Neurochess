package chesscs5400;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public abstract class CP { //ChessPiece
    int type;
    public int basevalue;
    boolean color;
    double am;
    double dm;  //defended multiple
    double sm;  //skewer multiple
    Image icon;
    final int[][] KnightOffsets={{2,1},{2,-1},{1,2},{1,-2},{-1,2},{-1,-2},{-2,1},{-2,-1}};
    int[][] zo=new int[8][8];
    int[][] Zo=new int[8][8];
    CP(){
       icon=null;
       type=0;
    }
    CP(int t,int base,boolean c,String i){
        type=t;
        color=c;
        try {icon=ImageIO.read(new File(i));} 
        catch (IOException ex) { System.out.println("Image not Found");}
    }

    public Image getImage(){
        return icon;
    }
    public int getType(){
        return type;
    }
    public boolean getColor(){
        return color;
    }
    //Must be implemented in the specific chess piece
    //if true is returned, action must be updated to contain changes to data structures;
    public abstract boolean ProcessAttack(CP[][] board, Action a, int[] enpassant, int[] kingloc,boolean[] castle);
    //Must be implemented in the specific chess piece
    //if true is returned, action must be updated to contain changes to data structures;
    public abstract boolean ProcessMove(CP[][] board, Action a, int[] enpassant, int[] kingloc,boolean[] castle);
    //Generates all the possible moves for a chess piece
    public abstract ArrayList<Action> GenerateMoves(CP[][] board,int x, int y,int[] enpassant,int[] kingloc, boolean[] castle, Action PreviousMove,boolean sc);
    //returns the value of the piece
    public abstract int Evaluate(CP[][] board, int x, int y);
    public boolean CheckCheck(CP[][] board, int[] kingloc, boolean color){
        int x=kingloc[0];
        int y=kingloc[1];
        if(color){
            x=kingloc[2];
            y=kingloc[3];
        }
        if(CheckKnights(board,x,y,color)){
            if(CheckDiagonal(board,x,y,color)){
                if(CheckHoriVert(board,x,y,color)){
                    if(CheckPawns(board,x,y,color)){
                        return !(Math.abs(kingloc[0]-kingloc[2])<=1 && Math.abs(kingloc[1]-kingloc[3])<=1);
                    }
                }
            }
        }
        return false;
    }

    public boolean BetterCheckCheck(CP[][] board, int[] kingloc, boolean color, Action a, Action b,boolean sc){
        int x=kingloc[0];
        int y=kingloc[1];
        if(color){
            x=kingloc[2];
            y=kingloc[3];
        }
        if(a.special || b.special){ // special moves tend to mess things up
            return CheckCheck(board,kingloc,color);
        }
        if(sc || (CheckPTo(board, x, y, color, a,kingloc) && CheckPFrom(board, x, y, color, a))){
             // if these two conditions are checked beforehand (before move is made)
                //if no check they don't have to be checked again, only CheckPMove 
                //if either are true(actually false, they are false when check is happening), 
                //they should be checked again(technically only the one(s) true before needs to be)
                return CheckPMove(board, x, y, color, b);
            
        }
        return false;
        
        
                
    }
    public boolean CheckPTo(CP[][] board, int x, int y, boolean color, Action a,int[] kingloc) {
        int xdir=x-a.xto;
        int ydir=y-a.yto;
        if(CheckHoriDia(board,x,y,xdir,ydir,color)){
            if(CheckKnights(board,x,y,color)){
                if(CheckPawns(board,x,y,color)){
                        return !(Math.abs(kingloc[0]-kingloc[2])<=1 && Math.abs(kingloc[1]-kingloc[3])<=1);
                    }
            }
        }
        return false;
    }

    public boolean CheckPFrom(CP[][] board, int x, int y, boolean color, Action a) {
        int xdir=x-a.xfrom;
        int ydir=y-a.yfrom;
        return CheckHoriDia(board,x,y,xdir,ydir,color);
    }

    public boolean CheckPMove(CP[][] board, int x, int y, boolean color, Action a) {  // technically the same as CheckPFrom with different action
        int xdir=x-a.xfrom;
        int ydir=y-a.yfrom;
        return CheckHoriDia(board,x,y,xdir,ydir,color);
    }
    public boolean CheckHoriDia(CP[][] board, int x, int y, int xdir, int ydir, boolean color){
        if(xdir==0){
            if(ydir>0){
                // check negative y direct
                y--;
                while(y>=0 && board[x][y]==null){
                    y--;
                }
                if(y<0){
                    return true;
                }
                if((board[x][y].type==5 || board[x][y].type==6)&& board[x][y].color!=color){
                    return false; 
                }
            }
            else if(ydir<0){
                y++;
                while(y<8 && board[x][y]==null){
                    y++;
                }
                if(y>=8){
                    return true;
                }
                if((board[x][y].type==5 || board[x][y].type==6)&& board[x][y].color!=color){
                    return false; 
                }
            }
            else{
                System.out.println("Critical Error");
            }
        }
        else if(xdir>0){
            if(ydir>0){
                // check negative x,y direct
                y--;
                x--;
                while(y>=0 && x>=0 && board[x][y]==null){
                    y--;
                    x--;
                }
                if(y<0 || x<0){
                    return true;
                }
                if((board[x][y].type==3 || board[x][y].type==6)&& board[x][y].color!=color){
                    return false; 
                }
                
            }
            else if(ydir<0){
                // check negative x, positive y direct
                y++;
                x--;
                while(y<8 && x>=0 && board[x][y]==null){
                    y++;
                    x--;
                }
                if(y>=8 || x<0){
                    return true;
                }
                if((board[x][y].type==3 || board[x][y].type==6)&& board[x][y].color!=color){
                    return false; 
                }
            }
            else{
                x--;
                while(x>=0 && board[x][y]==null){
                    x--;
                }
                if(x<0){
                    return true;
                }
                if((board[x][y].type==5 || board[x][y].type==6)&& board[x][y].color!=color){
                    return false; 
                }
            }
        }
        else {
            if(ydir>0){
                // check positive x, negative y direct
                y--;
                x++;
                while(y>=0 && x<8 && board[x][y]==null){
                    y--;
                    x++;
                }
                if(y<0 || x>=8){
                    return true;
                }
                if((board[x][y].type==3 || board[x][y].type==6)&& board[x][y].color!=color){
                    return false; 
                }
            }
            else if(ydir<0){
                // check positive x, y direct
                y++;
                x++;
                while(y<8 && x<8 && board[x][y]==null){
                    y++;
                    x++;
                }
                if(y>=8 || x>=8){
                    return true;
                }
                if((board[x][y].type==3 || board[x][y].type==6)&& board[x][y].color!=color){
                    return false; 
                }
            }
            else{
                x++;
                while(x<8 && board[x][y]==null){
                    x++;
                }
                if(x>=8){
                    return true;
                }
                if((board[x][y].type==5 || board[x][y].type==6)&& board[x][y].color!=color){
                    return false; 
                }
            }
        }
        return true;
    }
    public boolean CheckKnights(CP[][] board, int x, int y, boolean color) {
        int xtemp,ytemp;
        for(int[] offsets : KnightOffsets){
            xtemp=offsets[0]+x;
            ytemp=offsets[1]+y;
            if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]!=null &&
            board[xtemp][ytemp].type==4 && board[xtemp][ytemp].color!=color){
                return false;
            }
        }
        return true;
    }

    public boolean CheckDiagonal(CP[][] board, int x, int y, boolean color) {
        int dirx,diry,xtemp,ytemp;
        dirx=1;diry=1;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color &&
        (board[xtemp][ytemp].type==3 || board[xtemp][ytemp].type==6)){
            return false;
        }
        dirx=-1;diry=1;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color &&
        (board[xtemp][ytemp].type==3 || board[xtemp][ytemp].type==6)){
            return false;
        }
        dirx=1;diry=-1;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color &&
        (board[xtemp][ytemp].type==3 || board[xtemp][ytemp].type==6)){
            return false;
        }
        dirx=-1;diry=-1;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color &&
        (board[xtemp][ytemp].type==3 || board[xtemp][ytemp].type==6)){
            return false;
        }
        return true;
    }

    public boolean CheckHoriVert(CP[][] board, int x, int y, boolean color) {
        int dirx,diry,xtemp,ytemp;
        dirx=1;diry=0;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color &&
        (board[xtemp][ytemp].type==5 || board[xtemp][ytemp].type==6)){
            return false;
        }
        dirx=-1;diry=0;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color &&
        (board[xtemp][ytemp].type==5 || board[xtemp][ytemp].type==6)){
            return false;
        }
        dirx=0;diry=1;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color &&
        (board[xtemp][ytemp].type==5 || board[xtemp][ytemp].type==6)){
            return false;
        }
        dirx=0;diry=-1;xtemp=x+dirx;ytemp=y+diry;
        while(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp]==null){
            xtemp+=dirx;ytemp+=diry;
        }
        if(CheckBounds(xtemp,ytemp) && board[xtemp][ytemp].color!=color &&
        (board[xtemp][ytemp].type==5 || board[xtemp][ytemp].type==6)){
            return false;
        }
        return true;
    }
    public boolean CheckBounds(int x,int y){
        return x>=0 && x<8 && y>=0 && y<8;
    }
    public CP[][] PseudoBoard(CP[][] board){
        CP[][] fake=new CP[8][8];
        for(int i=0;i<8;i++){
            System.arraycopy(board[i], 0, fake[i], 0, 8);
        }
        
        return fake;
    }

    private boolean CheckPawns(CP[][] board, int x, int y, boolean color) {
        if(color){
            if(CheckBounds(x-1,y+1)&&board[x-1][y+1]!=null&&board[x-1][y+1].type==1){
                return false;
            }
            else if(CheckBounds(x+1,y+1)&&board[x+1][y+1]!=null&&board[x+1][y+1].type==1){
                return false;
            }
        }
        else{
            if(CheckBounds(x-1,y-1)&&board[x-1][y-1]!=null&&board[x-1][y-1].type==2){
                return false;
            }
            else if(CheckBounds(x+1,y-1)&&board[x+1][y-1]!=null&&board[x+1][y-1].type==2){
                return false;
            }
        }
        return true;
    }

    int hashvalue(int x, int y) {
        if(color){return zo[x][y];}
        return Zo[x][y];
    }

    
}



