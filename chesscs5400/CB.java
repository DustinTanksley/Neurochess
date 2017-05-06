package chesscs5400;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;

public class CB { //ChessBoard
    public CP[][] Board;
    public boolean turn;  //0 means 
    public int turns;
    public boolean[] canCastle={true,true,true,true};
    public int[] EnPassant;
    public int[] KingLoc={4,7,4,0};
    int hash;
    public Action PreviousMove=new Action(0,0,0,0,false);
	public boolean Quiescential=false;
    public CB(){
        turn=false;
        turns=1;
        Board=new CP[8][8];
        Board[6][0]=Globals.n;Board[1][0]=Globals.n;
        Board[7][1]=Globals.p;Board[0][1]=Globals.p;
        Board[6][1]=Globals.p;Board[5][1]=Globals.p;
        Board[4][1]=Globals.p;Board[3][1]=Globals.p;
        Board[2][1]=Globals.p;Board[1][1]=Globals.p;
        Board[5][0]=Globals.b;Board[2][0]=Globals.b;
        Board[4][0]=Globals.k;Board[3][0]=Globals.q;
        Board[7][0]=Globals.r;Board[0][0]=Globals.r;
        Board[6][7]=Globals.N;Board[1][7]=Globals.N;
        Board[7][7]=Globals.R;Board[0][7]=Globals.R;
        Board[5][7]=Globals.B;Board[2][7]=Globals.B;
        Board[3][7]=Globals.Q;Board[4][7]=Globals.K;
        Board[7][6]=Globals.P;Board[0][6]=Globals.P;
        Board[6][6]=Globals.P;Board[5][6]=Globals.P;
        Board[4][6]=Globals.P;Board[3][6]=Globals.P;
        Board[2][6]=Globals.P;Board[1][6]=Globals.P;
        hashBoard();
    }
    public CB(String fen){ //should be done
        Board=new CP[8][8];
        String[] fields=fen.split(" ");
        String[] placement=fields[0].split("/");
        int counter;
        for(int i=0;i<8;i++){
            counter=0;
            for(int j=0;j<placement[i].length();j++)
            if(Character.isDigit(placement[i].charAt(j))){
                counter+=Integer.parseInt(placement[i].substring(j,j+1));
            }
            else {
                switch(placement[i].charAt(j)){
                    case 'n':
                        Board[counter][i]=Globals.n;
                        break;
                    case 'q':
                        Board[counter][i]=Globals.q;
                        break;
                    case 'b':
                        Board[counter][i]=Globals.b;
                        break;
                    case 'k':
                        Board[counter][i]=Globals.k;
                        KingLoc[2]=counter;
                        KingLoc[3]=i;
                        break;
                    case 'p':
                        Board[counter][i]=Globals.p;
                        break;
                    case 'r':
                        Board[counter][i]=Globals.r;
                        break;
                    case 'N':
                        Board[counter][i]=Globals.N;
                        break;
                    case 'Q':
                        Board[counter][i]=Globals.Q;
                        break;
                    case 'B':
                        Board[counter][i]=Globals.B;
                        break;
                    case 'P':
                        Board[counter][i]=Globals.P;
                        break;
                    case 'R':
                        Board[counter][i]=Globals.R;
                        break;
                    case 'K':
                        Board[counter][i]=Globals.K;
                        KingLoc[0]=counter;
                        KingLoc[1]=i;
                        break;
                    default:
                        System.out.println("ERROR");
                        break;
                }
                   counter++;
            }
        }
        turn="b".equals(fields[1]);
        canCastle=new boolean[4];
        if(fields[2].contains("Q")){
            canCastle[0]=true;
        }
        if(fields[2].contains("K")){
            canCastle[1]=true;
        }
        if(fields[2].contains("q")){
            canCastle[2]=true;
        }
        if(fields[2].contains("k")){
            canCastle[3]=true;
        }
        
        if(fields[3].equals("-")){
            EnPassant=null;
        }
        else{
            int x=7-(int)(fields[3].charAt(0)-'a');
            EnPassant=new int[2];
            EnPassant[0]=(int)(fields[3].charAt(0)-'a');
            EnPassant[1]=8-Character.digit(fields[3].charAt(1), 10);
        }
        turns=Integer.parseInt(fields[5]);
        
    }
    public CB(CB s){
        Board=new CP[8][8];
        for(int i=0;i<8;i++){
            System.arraycopy(s.Board[i], 0, Board[i], 0, 8);
        }
        turn=s.turn;
		turns=s.turns;
        PreviousMove=s.PreviousMove;
        System.arraycopy(s.KingLoc,0,KingLoc,0,4);
        System.arraycopy(s.canCastle,0,canCastle,0,4);
        if(s.EnPassant==null){
            EnPassant=null;
        }
        else{
            EnPassant=new int[2];
            System.arraycopy(s.EnPassant,0,EnPassant,0,2);
        }
        
    }
    public CB(CB s,Action a){
        Board=new CP[8][8];
        for(int i=0;i<8;i++){
            System.arraycopy(s.Board[i], 0, Board[i], 0, 8);
        }
        move(a);
        turn=!s.turn;
		turns=s.turns+1;
    }
    @Override
    public String toString(){  // gives the fen, last 2 fields are always 0 and 1, but that info is not as important
        String hold="";
        int counter=0;
        boolean nocastle=true;
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
                if(Board[x][y]==null){
                    counter++;
                }
                else{
                    if(counter!=0){
                        hold+=counter;
                        counter=0;
                    }
                    switch(Board[x][y].type){
                        case 1:
                            hold+="P";
                            break;
                        case 2:
                            hold+="p";
                            break;
                        case 3:
                            if(Board[x][y].color){
                                hold+="b";
                            }
                            else hold+="B";
                            break;
                        case 4:
                            if(Board[x][y].color){
                                hold+="n";
                            }
                            else hold+="N";
                            break;
                        case 5:
                            if(Board[x][y].color){
                                hold+="r";
                            }
                            else hold+="R";
                            break;
                        case 6:
                            if(Board[x][y].color){
                                hold+="q";
                            }
                            else hold+="Q";
                            break;
                        case 7:
                            if(Board[x][y].color){
                                hold+="k";
                            }
                            else hold+="K";
                            break;
                    }
                }
            }
            if(counter!=0){
                hold+=counter;
                counter=0;
            }
            hold+="/";
        }
        hold=hold.substring(0, hold.length()-1);
        if(turn){
            hold+=" b ";
        }
        else hold+= " w ";
        if(canCastle[1]){
            hold+="K";
            nocastle=false;
        }
        if(canCastle[0]){
            hold+="Q";
            nocastle=false;
        }
        if(canCastle[3]){
            hold+="k";
            nocastle=false;
        }
        if(canCastle[2]){
            hold+="q";
            nocastle=false;
        }
        if(nocastle){
            hold+="-";
        }
        hold+=" ";
        if(EnPassant==null){
            hold+="-";
        }
        else{
            hold+=(char)( EnPassant[0]+'a');
            hold+=8-EnPassant[1];
        }
        hold+=" 0 1";  //this is wrong, but has very little impact
        return hold;
    }
    public boolean getTurn(){
        return turn;
    }
    public void move(Action a){
        if(a.attack && Board[a.xto][a.yto].basevalue<= Board[a.xfrom][a.yfrom].basevalue) {Quiescential=true;}
        PreviousMove=a;
        Board[a.xto][a.yto]=Board[a.xfrom][a.yfrom];
        Board[a.xfrom][a.yfrom]=null;
        turn=!turn;
        KingLoc=a.kings;
        canCastle=a.castle;
        EnPassant=a.EnPassant;
        if(a.special){
            if(Board[a.xto][a.yto].type==7){
                if(a.xto==2){
                    Board[3][a.yto]=Board[0][a.yto];
                    Board[0][a.yto]=null;
                }
                else{
                    Board[5][a.yto]=Board[7][a.yto];
                    Board[7][a.yto]=null;
                }
            }
            else{
                if(turn){
                    Board[a.xto][a.yfrom]=null;
                }
                else{
                    Board[a.xto][a.yfrom]=null;
                }
            }
        }
        if(a.yto==7 && Board[a.xto][a.yto].type==2){
            switch(a.promotion){
                case 0:
                    Board[a.xto][a.yto]=Globals.q;
                    break;
                case 1:
                    Board[a.xto][a.yto]=Globals.r;
                    break;
                case 2:
                    Board[a.xto][a.yto]=Globals.n;
                    break;
                case 3:
                    Board[a.xto][a.yto]=Globals.b;
                    break;
            }
        }
        else if(a.yto==0 && Board[a.xto][a.yto].type==1){
            switch(a.promotion){
                case 0:
                    Board[a.xto][a.yto]=Globals.Q;
                    break;
                case 1:
                    Board[a.xto][a.yto]=Globals.R;
                    break;
                case 2:
                    Board[a.xto][a.yto]=Globals.N;
                    break;
                case 3:
                    Board[a.xto][a.yto]=Globals.B;
                    break;
            }
        }if(a.attack || Board[a.xto][a.yto].basevalue==1){
			turns=0;
		}
		
    }
    boolean validMove(Action act) {
        boolean valid = false;
        CP piece = Board[act.xfrom][act.yfrom];
        if (turn == piece.color) {
            if (act.attack) {
                valid = piece.ProcessAttack(Board, act,EnPassant,KingLoc,canCastle);
            } else {
                valid = piece.ProcessMove(Board, act,EnPassant,KingLoc,canCastle);
            }
        }
        if(valid){
            if((piece.type==1 && act.yto ==0) || (piece.type ==2 && act.yto==7)){
                act.promotion=JOptionPane.showOptionDialog(null, "Promote to?", "Promotion", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Queen","Rook","Knight","Bishop"}, 0);
                if(act.promotion==-1){
                    return false;
                }
            }
        }
        return valid;
    }
	public boolean QuiescentialCheckCheck(){
		int x=KingLoc[0];
        int y=KingLoc[1];
        if(turn){
            x=KingLoc[2];
            y=KingLoc[3];
        }
		return Board[x][y].CheckPTo(Board,x,y,turn,PreviousMove,KingLoc) && Board[x][y].CheckPFrom(Board,x,y,turn,PreviousMove);
	}
    public ArrayList<Action> Actions(){ //should return the strongest moves first for alpha/beta
        int x=KingLoc[0];
        int y=KingLoc[1];
        if(turn){
            x=KingLoc[2];
            y=KingLoc[3];
        }
        ArrayList<Action> acts=new ArrayList();
		boolean smartCheck=false;
		if(Board[x][y].CheckPTo(Board,x,y,turn,PreviousMove,KingLoc) && Board[x][y].CheckPFrom(Board,x,y,turn,PreviousMove)){
			smartCheck=true;
		}
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(Board[i][j]!=null && Board[i][j].color==turn){
                    acts.addAll(Board[i][j].GenerateMoves(Board, i, j, EnPassant, KingLoc, canCastle,PreviousMove,smartCheck));
                }
            }
        }
        Collections.sort(acts, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                Action one=(Action) o1;
                Action two=(Action) o2;
                if(one.attack){
                    if(two.attack){
                        int scoreone=Board[one.xto][one.yto].basevalue-Board[one.xfrom][one.yfrom].basevalue;
                        int scoretwo=Board[two.xto][two.yto].basevalue-Board[two.xfrom][two.yfrom].basevalue;
                        return scoretwo-scoreone;
                    }
                    return -1;
                }
                if(two.attack){
                    return 1;
                }
                return 0;
            }
            
        });
        return acts;
    }

    public int Evaluate() {
        int scored=0;
        for(int i=0;i<8;i++){
            for( int j=0;j<8;j++){
                if(Board[i][j]!=null){
                    scored+=Board[i][j].Evaluate(Board,i,j);
                }
            }
        }
        return scored;
    }
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!CB.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final CB other = (CB) obj;
        if(turn!=other.turn){
            return false;
        }
        for(int i=0;i<8;i++){
            for( int j=0;j<8;j++){
                if(Board[i][j]!=null || other.Board[i][j]!=null){
                    if(Board[i][j]==null || other.Board[i][j]==null){
                        return false;
                    }
                    else if(Board[i][j].type!=other.Board[i][j].type){
                        return false;
                    }
                    else if(Board[i][j].color!= other.Board[i][j].color){
                        return false;
                    }
                }
            }
        }
        return true;
        }
    @Override
    public int hashCode(){
        hash=0;
        hashBoard();
        return hash;
    }

    public void hashBoard(){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(Board[i][j]!=null){
                    hash=hash^Board[i][j].hashvalue(i,j);
                }
            }
        }
    }
    
}
