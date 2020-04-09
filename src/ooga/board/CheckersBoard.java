package ooga.board;

import javafx.util.Pair;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckersBoard extends Board {


    public List<Point2D> validMoves;

    public CheckersBoard(Map<String, String> settings, Map<Point2D, String> locs, Map<String, Pair<String, Double>> pieces){
        super(settings, locs, pieces);
    }

    @Override
    public boolean checkWon() {
        return false;
    }

    @Override
    public List<Point2D> getValidMoves(int x, int y) {
        Piece currPiece = getPieceAt(x,y);
        if(currPiece==null){return null;}
        validMoves = new ArrayList<Point2D>();
        checkRight(x, y, currPiece);
        checkLeft(x, y, currPiece);
        return validMoves;
    }

    public boolean checkRight(int x, int y, Piece currPiece) {
        if (!isValidCell(x + 1, y + 1) || !isValidCell(x + 2, y + 2)) {
            return false;
        }

        Piece temp = getPieceAt(x + 1, y + 1);

        if (!(temp.getColor().equals(currPiece.getColor())) && (getPieceAt(x + 2, y + 2) == null)) {
            validMoves.add(new Point2D.Double(x + 2, y + 2));
        }

        return true;
    }


    @Override
    public Pair<Point2D, Double> doMove(int endX, int endY) {
        return null;
    }

    public boolean checkLeft(int x, int y, Piece currPiece){
        if(!isValidCell(x-1, y-1) || !isValidCell(x-2, y-2)){
            return false;
        }

        Piece temp = getPieceAt(x-1, y-1);

        if(!(temp.getColor().equals(currPiece.getColor())) && (getPieceAt(x-2, y-2) == null)){
            validMoves.add(new Point2D.Double(x-2, y-2));
        }

        return true;

    }

    public boolean goLeft(int x, int y, Piece currPiece){
        if(!isValidCell(x-1, y-1) || !isValidCell(x-2, y-2)){
            return false;
        }
        Piece temp = getPieceAt(x-1, y-1);

        if(!(temp.getColor().equals(currPiece.getColor())) && (getPieceAt(x-2, y-2) == null)){
            validMoves.add(new Point2D.Double(x-2, y-2));
        }

        return goLeft(x-2, y-2, currPiece);

    }

    public boolean goRight(int x, int y, Piece currPiece){
        if(!isValidCell(x+1, y+1) || !isValidCell(x+2, y+2)){
            return false;
        }
        Piece temp = getPieceAt(x+1, y+1);

        if(!(temp.getColor().equals(currPiece.getColor())) && (getPieceAt(x+2, y+2) == null)){
            validMoves.add(new Point2D.Double(x+2, y+2));
        }

        return goRight(x+2, y+2, currPiece);

    }

    public boolean isOppColor(int x, int y, String currPieceColor){
        return currPieceColor.equals(getPieceAt(x,y).getColor());
    }

}
