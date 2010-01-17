package witspirit.toddlerpuzzle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

public class Puzzle {
    public-init var rows : Integer;
    public-init var columns : Integer;
    public-init var image : Image; 
    public-read var preview : ImageView = ImageView {
        image : image;
    };
    
    public-read var pieces : Piece[];
    
    init {
		var pieceSize = Rectangle2D {
        	width: image.width / columns;
        	height: image.height / rows;
        }
		        
        pieces = for (row in [0..rows]) {
            for (col in [0..columns]) {
         		Piece {
         		    image: image;
         		    part : Rectangle2D {
         		        minX: col*pieceSize.width;
         		        minY: row*pieceSize.height;
         		        width: pieceSize.width;
         		        height: pieceSize.height;
         		    }
         		}       
            }
        }        
    }
    
}