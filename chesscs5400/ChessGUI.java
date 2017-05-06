package chesscs5400;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import javax.swing.JPanel;

public class ChessGUI extends JPanel{
    CP test;
    CB Board;
    Boolean Pressed=false;
    int xc=0;
    int yc=0;
    Image move;
    ChessGUI(CB bo){
        Board=bo;
    }
    @Override
    public void paintComponent(Graphics g){
        int r,R,x,y;
        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());
        if(getWidth()>getHeight()){r=getHeight()/8;R=getHeight();}
        else{r=getWidth()/8;R=getWidth();}
        g.setColor(Color.white);
        g.fillRect(0, 0, R, R);
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if((i+j)%2==0){
                    g.setColor(Color.red);
                    g.fillRect(i*r, j*r, r, r);
                }
                if(Board.Board[i][j]!=null){
                g.drawImage(Board.Board[i][j].getImage(), i*r, j*r,r,r, this);}
            }
        }
        if(Pressed){
            g.setColor(Color.blue);
            g.fillRect(xc*r, yc*r, r, r);
            if(this.getMousePosition()!=null){
                g.drawImage(move, getMousePosition().x-r/2, getMousePosition().y-r/2, r, r, this);
            }
        }
    }
    public void ProcessPress(){
        Point a;
        if(getMousePosition()!=null){
            a=getMousePosition();
        int x=a.x;
        int y=a.y;
        int r,R;
        if(getWidth()>getHeight()){r=getHeight()/8;R=getHeight();}
        else{r=getWidth()/8;R=getWidth();}
        if(x<R && y<R && x>0 && y>0){
            xc=x/r;
            yc=y/r;
            if(Board.Board[xc][yc]!=null && Board.getTurn()==Board.Board[xc][yc].getColor()){
                Pressed=true;
                move=Board.Board[xc][yc].getImage();
            }
        }
        
        }
        repaint();
    }
    public void ProcessRelease(){
        Point a;
        if(getMousePosition()!=null && Pressed){
            a=getMousePosition();
        int x=a.x;
        int y=a.y;
        int r,R;
        if(getWidth()>getHeight()){r=getHeight()/8;R=getHeight();}
        else{r=getWidth()/8;R=getWidth();}
        if(x<R && y<R && x>0 && y>0){
            int xto=x/r;
            int yto=y/r;
            Action act=new Action(xc,yc,xto,yto,Board.Board[xto][yto]!=null);
            if(Board.validMove(act)){
                Board.move(act);
                System.out.println(Board.toString());
            }
        }
        }
        Pressed=false;
        repaint();
    }
    public void ProcessMove(){
        repaint();
    }
    
    
}
