package witspirit.toddlerpuzzle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

public class Piece {
    public-init var image: Image;
    public-init var part: Rectangle2D;
    
    public-read var view = ImageView {
 	    image: image;
 	    viewport : part;
 	} 
}