package ooga.history;

import ooga.board.Piece;
import ooga.view.PieceView;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class Move {

    private Piece piece;
    private Point2D startLocation;
    private Point2D endLocation;
    private Map<Piece, Point2D> capturedPiecesAndLocations;
    private boolean isUndo;
    private boolean isPromote;

    public Move(Point2D startLocation, Point2D endLocation) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        capturedPiecesAndLocations = new HashMap<>();
        isUndo = false;
        isPromote = false;
    }

    public Move getReverseMove() {
        return new Move(endLocation, startLocation);
    }

    public Point2D getStartLocation() {
        return startLocation;
    }

    public Point2D getEndLocation() {
        return endLocation;
    }

    public void setUndo(boolean isUndo) {
        this.isUndo = isUndo;
    }

    public boolean isUndo() {
        return isUndo;
    }

    public boolean isPromote() {
        return isPromote;
    }

    @Override
    public String toString() {
        return String.format("%s from (%d, %d) to (%d, %d)", piece, (int) startLocation.getX(), (int) startLocation.getY(), (int) endLocation.getX(), (int) endLocation.getY());
    }

    public void addCapturedPieceAndLocation(Piece capturedPiece, Point2D capturedPieceLocation){
        capturedPiecesAndLocations.put(capturedPiece, capturedPieceLocation);
    }

    public Map<Piece, Point2D> getCapturedPiecesAndLocations() {
        return capturedPiecesAndLocations;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    public void setPromote(boolean isPromote) {
        this.isPromote = isPromote;
    }
}
