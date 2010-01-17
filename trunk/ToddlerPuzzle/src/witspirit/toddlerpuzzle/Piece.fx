package witspirit.toddlerpuzzle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;

import java.lang.Math;

public class Piece {
    public-init var image: Image;
    public-init var part: Rectangle2D;
    var x : Number;
    var y : Number;
    var selected = false;
    
    public-read var view = ImageView {
 	    image: image;
 	    viewport : part;
 	    x : bind x;
 	    y : bind y;
 	    
 	    effect: bind if(selected) {
 	    	DropShadow {
 	    	}  
 	    } else {
 	    	null;
 	    }
 	    
 	    onMouseClicked : function(event) {
 	        selected = not selected;
 	    };
 	} 
 	
 	public function scatter(bounds: Rectangle2D) : Void {
 	    x = Math.random() * (bounds.width - part.width) + bounds.minX;
 	    y = Math.random() * (bounds.height - part.height) + bounds.minY;
 	}
}