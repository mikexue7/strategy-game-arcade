package ooga.board;

import javafx.util.Pair;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckersBoard extends Board {


    public List<Point2D> validKillMoves = new ArrayList<Point2D>();
    public List<Point2D> validNonKillMoves = new ArrayList<Point2D>();

    /*To-DO Items
    Two move types: Coin and King; Coin allows restricted diagonal movement, King allows diagonal movement in all directions.
    Both can only move one cell or two cells
    1) Add support for both colors Red and Black (Top color can only move down initially, bottom color can only move up initially)
    2) Check upper diagonal neighbors and lower diagonal neighbors (depending on top or bottom), if piece @ that cell is null,
    add to list of validMoves
    3) check upper two diagonal neighbors and lower two diagonal neighbors (depending on top or bottom), if piece @ that cell is
    null and the immediate diagonal neighbor is of the opposite piece color, allow for a valid move and remove opposite color piece
    4) If a piece is captured, skip the other player/CPU turn and allow player to go again. This avoids recursion and a need
    to change the piece's move type
    5) Once a piece reaches the first/last row of the board, change the movetype of the piece to 'King'
    */

    public CheckersBoard(Map<String, String> settings, Map<Point2D, String> locs, Map<String, Pair<String, Double>> pieces){
        super(settings, locs, pieces);
    }

    @Override
    public String checkWon() {
        int numWhite = 0;
        int numBlack = 0;
        for(int i = 0; i<width; i++){
            for(int j = 0; j<height; j++){
                if(getPieceAt(i,j).getColor().equals("Red")){
                    numWhite++;
                }
                else if(getPieceAt(i,j).getColor().equals("Black")){
                    numBlack++;
                }
            }
        }
        if(numBlack==0 || numWhite==0){
            return "sdkfjhd";
        }
        return null;
    }

    @Override
    public List<Point2D> getValidMoves(int x, int y) {
        Piece piece = getPieceAt(x,y);
        String movPat = piece.getMovePattern();
        if(movPat.equals("P1")){
            //Employ upper methods
            p1(x,y);
        }
        else if(movPat.equals("P2")){
            //Employ down methods
            p2(x,y);
        }
        else if(movPat.equals("KING")){
            //Employ king methods (ALL)
            king(x, y);
        }
        else{
            return null;
        }

        validNonKillMoves.addAll(validNonKillMoves);
        return validNonKillMoves;
        /*if (piece == null) { return null; }
        System.out.println("Problem color " + color + " |  Problem piece" + piece + " | X, Y " + x + " " + y);
        System.out.println("problem map " + pieceColorMap);
        if (pieceColorMape.get(color).contains(piece)) {
            validMoves = new ArrayList<Point2D>();
            //checkRight(x, y, piece);
            //checkLeft(x, y, piece);
            return validMoves;
        }*/
    }


    public int doMove(int x_i, int y_i, int x_f, int y_f) {
        Piece currPiece = getPieceAt(x_i, y_i);
        Piece oppPiece = getPieceAt(x_f, y_f);
        placePiece(x_i, y_i, null);
        placePiece(x_f, y_f, currPiece);
        if(oppPiece == null){
            return 0;
        }
        else{
            return (int) oppPiece.getValue();
        }
    }

    //********START: Eight elements that make up the three possible move patterns of the pieces in the game.********
    public boolean up_left(int x, int y){
        if(isCellInBounds(x-1, y-1) && (getPieceAt(x-1, y-1)==null)){
            validNonKillMoves.add(new Point2D.Double(x-1, y-1));
            return true;
        }
        return false;
    }

    public boolean up_left_kill(int x, int y){
        Piece temp1 = getPieceAt(x-1, y-1);
        Piece temp2 = getPieceAt(x-2, y-2);
        boolean killConditions = isCellInBounds(x-1, y-1) && isCellInBounds(x-2, y-2) && temp1!=null && temp2==null && isOppColor(getPieceAt(x, y), temp1);
        if(!killConditions){
            return false;
        }
        else{
            validKillMoves.add(new Point2D.Double(x-2, y-2));
            return true;
        }
    }

    public boolean up_right(int x, int y){
        if(isCellInBounds(x-1, y+1) && (getPieceAt(x-1, y+1)==null)){
            validNonKillMoves.add(new Point2D.Double(x-1, y+1));
            return true;
        }
        return false;
    }

    public boolean up_right_kill(int x, int y){
        Piece temp1 = getPieceAt(x-1, y+1);
        Piece temp2 = getPieceAt(x-2, y+2);
        boolean killConditions = isCellInBounds(x-1, y+1) && isCellInBounds(x-2, y+2) && temp1!=null && temp2==null && isOppColor(getPieceAt(x, y), temp1);
        if(!killConditions){
            return false;
        }
        else{
            validKillMoves.add(new Point2D.Double(x-2, y+2));
            return true;
        }
    }

    public boolean down_left(int x, int y){
        if(isCellInBounds(x+1, y-1) && (getPieceAt(x+1, y-1)==null)){
            validNonKillMoves.add(new Point2D.Double(x+1, y-1));
            return true;
        }
        return false;
    }

    public boolean down_left_kill(int x, int y){
        Piece temp1 = getPieceAt(x+1, y-1);
        Piece temp2 = getPieceAt(x+2, y-2);
        boolean killConditions = isCellInBounds(x+1, y-1) && isCellInBounds(x+2, y-2) && temp1!=null && temp2==null && isOppColor(getPieceAt(x, y), temp1);
        if(!killConditions){
            return false;
        }
        else{
            validKillMoves.add(new Point2D.Double(x+2, y-2));
            return true;
        }
    }

    public boolean down_right(int x, int y){
        if(isCellInBounds(x+1, y+1) && (getPieceAt(x+1, y+1)==null)){
            validNonKillMoves.add(new Point2D.Double(x+1, y+1));
            return true;
        }
        return false;
    }

    public boolean down_right_kill(int x, int y){
        Piece temp1 = getPieceAt(x+1, y+1);
        Piece temp2 = getPieceAt(x+2, y+2);
        boolean killConditions = isCellInBounds(x+1, y+1) && isCellInBounds(x+2, y+2) && temp1!=null && temp2==null && isOppColor(getPieceAt(x, y), temp1);
        if(!killConditions){
            return false;
        }
        else{
            validKillMoves.add(new Point2D.Double(x+2, y+2));
            return true;
        }
    }

    //********END: Eight elements that make up the three possible move patterns of the pieces in the game.********

    //START: Three possible move patterns, one for player 1 (black checkers), one for player 2 (red checkers), and one for king checkers.

    private void p1(int x, int y){
        up_left(x, y);
        up_right(x, y);
        up_left_kill(x, y);
        up_right_kill(x, y);

        int sizeDiff = -1;
        while(sizeDiff != 0){
            int before_size = validKillMoves.size();
            for(Point2D point: validKillMoves){
                int i = (int)point.getX();
                int j = (int)point.getY();
                up_left_kill(i, j);
                up_right_kill(i, j);
            }
            sizeDiff = validKillMoves.size()-before_size;
        }
    }

    private void p2(int x, int y){
        down_left(x, y);
        down_right(x, y);
        down_left_kill(x, y);
        down_right_kill(x, y);

        int sizeDiff = -1;
        while(sizeDiff != 0){
            int before_size = validKillMoves.size();
            for(Point2D point: validKillMoves){
                int i = (int)point.getX();
                int j = (int)point.getY();
                down_left_kill(i, j);
                down_right_kill(i, j);
            }
            sizeDiff = validKillMoves.size()-before_size;
        }
    }

    private void king(int x, int y){
        up_left(x, y);
        up_right(x, y);
        up_left_kill(x, y);
        up_right_kill(x, y);
        down_left(x, y);
        down_right(x, y);
        down_left_kill(x, y);
        down_right_kill(x, y);

        int sizeDiff = -1;
        while(sizeDiff != 0){
            int before_size = validKillMoves.size();
            for(Point2D point: validKillMoves){
                int i = (int)point.getX();
                int j = (int)point.getY();
                up_left_kill(i, j);
                up_right_kill(i, j);
                down_left_kill(i, j);
                down_right_kill(i, j);
            }
            sizeDiff = validKillMoves.size()-before_size;
        }
    }

    public boolean isOppColor(Piece currPiece, Piece oppPiece){
        return !(oppPiece.getColor().equals(currPiece.getColor()));
    }

}
