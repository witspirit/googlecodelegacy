package witspirit.toddlerpuzzle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

public class Puzzle {
    public-init var rows : Integer;
    public-init var columns : Integer;
    public-init var image : Image; 
    public-init var playArea : Rectangle2D;
    public-read var preview : ImageView = ImageView {
        image : image;
    };
    
    public-read var pieces : Piece[];
    var pieceSize = Rectangle2D {
            	width: image.width / columns;
            	height: image.height / rows;
            }
    
    init {
        pieces = for (row in [0..rows]) {
            for (col in [0..columns]) {
         		Piece {
         		    image: image;
         		    part : Rectangle2D {
         		        minX: col*pieceSize.width;
         		        minY: row*pieceSize.height;
         		        width: pieceSize.width;
         		        height: pieceSize.height;
         		    };
         		}
            }
        }
        
        shuffle();
    }
    
    public function shuffle() : Void {
        for (piece in pieces) {
            piece.scatter(playArea);
        }
    }
    
}