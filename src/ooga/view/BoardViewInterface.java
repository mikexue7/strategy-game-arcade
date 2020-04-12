package ooga.view;

import javafx.scene.layout.StackPane;
import ooga.CellClickedInterface;

import java.awt.geom.Point2D;
import java.util.List;

public interface BoardViewInterface {

    void initialize();

    void checkeredColor();

    StackPane[] getCells();

    CellView getCell(int x, int y);

    void highlightValidMoves(List<Point2D> validMoves);

    void movePiece(int x, int y);

    void setOnPieceClicked(CellClickedInterface clicked);

    void setOnMoveClicked(CellClickedInterface clicked);

    void setSelectedLocation(int x, int y);
}
