package chesscs5400;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

class Rook extends CP {
    
    Rook(boolean color) {
        Random x=new Random();
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                zo[i][j]=x.nextInt();
                Zo[i][j]=x.nextInt();
            }
        }
        this.color = color;
        type = 5;
        basevalue = 12;
        am=.2;
        dm=.1;
        sm=.6;
         try {
            if (color) {
                icon = ImageIO.read(Class.class.getResourceAsStream("/resource/BlackRook.png"));
            } else {
                icon = ImageIO.read(Class.class.getResourceAsStream("/resource/WhiteRook.png"));
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
        int dirx,diry,xtemp,ytemp;
        
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

    @Override
    public boolean ProcessAttack(CP[][] board, Action a, int[] enpassant, int[] kingloc, boolean[] castle) {
        return board[a.xto][a.yto].color != color && ProcessMove(board, a, enpassant, kingloc, castle);
    }

    @Override
    public boolean ProcessMove(CP[][] board, Action a, int[] enpassant, int[] kingloc, boolean[] castle) {
        int temp;
        if (a.xfrom == a.xto) {
            //moving up/down
            if (a.yfrom > a.yto) {
                for (temp = a.yfrom - 1; temp > a.yto; temp--) {
                    if (board[a.xfrom][temp] != null) {
                        return false;
                    }
                }
            } else {
                for (temp = a.yfrom + 1; temp < a.yto; temp++) {
                    if (board[a.xfrom][temp] != null) {
                        return false;
                    }
                }
            }
            CP[][] BTC = PseudoBoard(board);
            BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
            BTC[a.xfrom][a.yfrom] = null;
            if (CheckCheck(BTC, kingloc, color)) {
                a.EnPassant = null;
                a.kings = kingloc;
                a.special = false;
                a.castle = castle.clone();
                if (a.xfrom == 0 && color) {
                    a.castle[2] = false;
                } else if (a.xfrom == 7 && color) {
                    a.castle[3] = false;
                } else if (a.xfrom == 0 && !color) {
                    a.castle[0] = false;
                } else if (a.xfrom == 7 && !color) {
                    a.castle[1] = false;
                }
                return true;
            }
        } else if (a.yfrom == a.yto) {
            //moving left/right
            if (a.xfrom > a.xto) {
                for (temp = a.xfrom - 1; temp > a.xto; temp--) {
                    if (board[temp][a.yfrom] != null) {
                        return false;
                    }
                }
            } else {
                for (temp = a.xfrom + 1; temp < a.xto; temp++) {
                    if (board[temp][a.yfrom] != null) {
                        return false;
                    }
                }
            }
            CP[][] BTC = PseudoBoard(board);
            BTC[a.xto][a.yto] = BTC[a.xfrom][a.yfrom];
            BTC[a.xfrom][a.yfrom] = null;
            if (CheckCheck(BTC, kingloc, color)) {
                a.EnPassant = null;
                a.kings = kingloc;
                a.special = false;
                a.castle = castle.clone();
                if (a.xfrom == 0 && color) {
                    a.castle[2] = false;
                } else if (a.xfrom == 7 && color) {
                    a.castle[3] = false;
                } else if (a.xfrom == 0 && !color) {
                    a.castle[0] = false;
                } else if (a.xfrom == 7 && !color) {
                    a.castle[1] = false;
                }
                return true;
            }
        }
        return false;
    }

    public void addValid(ArrayList<Action> acts, Action a, CP[][] board, int[] enpassant, int[] kingloc, boolean[] castle,Action PM,boolean sc) {
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
            a.special = false;
            a.castle = castle.clone();
            if (a.xfrom == 0 && color) {
                a.castle[2] = false;
            } else if (a.xfrom == 7 && color) {
                a.castle[3] = false;
            } else if (a.xfrom == 0 && !color) {
                a.castle[0] = false;
            } else if (a.xfrom == 7 && !color) {
                a.castle[1] = false;
            }
            acts.add(a);
        }
        board[a.xfrom][a.yfrom]=board[a.xto][a.yto];
        board[a.xto][a.yto]=a.TempPiece;
    }
    @Override
    public int Evaluate(CP[][] board, int x, int y) {
        
        int basescore = 200;
        int i = x;
        int j = y + 1;
        while (j < 8 && board[i][j] == null) {
            basescore += 1;
            j += 1;
        }
        if (j < 8) {
            if (board[i][j].type == 7 && !board[i][j].color != color) {
                basescore += 10;
            } else if (board[i][j].color == color && board[i][j].basevalue>basevalue) {
                basescore += 2;
            } else {
                basescore += board[i][j].basevalue;
            }
        }

        i = x;
        j = y - 1;
        while (j >= 0 && board[i][j] == null) {
            basescore += 1;
            j += -1;
        }
        if (j >= 0) {
            if (board[i][j].type == 7 && !board[i][j].color != color) {
                basescore += 10;
            } else if (board[i][j].color == color && board[i][j].basevalue>basevalue) {
                basescore += 2;
            } else {
                basescore += board[i][j].basevalue;
            }
        }

        i = x - 1;
        j = y;
        while (i >= 0 && board[i][j] == null) {
            basescore += 1;
            i += -1;
        }
        if (i >= 0) {
            if (board[i][j].type == 7 && !board[i][j].color != color) {
                basescore += 10;
            } else if (board[i][j].color == color && board[i][j].basevalue>basevalue) {
                basescore += 2;
            } else {
                basescore += board[i][j].basevalue;
            }
        }

        i = x + 1;
        j = y;
        while (i < 8 && board[i][j] == null) {
            basescore += 1;
            i += 1;
        }
        if (i < 8) {
            if (board[i][j].type == 7 && board[i][j].color != color) {
                basescore += 10;
            } else if (board[i][j].color == color && board[i][j].basevalue>basevalue) {
                basescore += 2;
            } else {
                basescore += board[i][j].basevalue;
            }
        }

        return color ? -basescore : basescore;
    }
}
