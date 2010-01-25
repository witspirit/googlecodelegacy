package witspirit.toddlerpuzzle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.scene.text.TextOrigin;
import javafx.scene.Node;
import javafx.geometry.Bounds;

import java.lang.Math;

public class Piece {
    public-init var puzzle: Puzzle;
    public-init var image: Image;
    public-init var part: Rectangle2D;
    var x : Number;
    var y : Number;
    var selected = bind puzzle.selectedPiece == this;
    var clickedOffsetX : Number;
    var clickedOffsetY : Number ;
    
    public-read var view = Group {
        var viewport : ImageView;
    	content: [
    		viewport = ImageView {
		 	    image: image;
		 	    viewport : part;
		 	    translateX : bind x;
		 	    translateY : bind y;
		 	    blocksMouse : true;
		 	    
		 	    effect: bind if(selected) {
		 	    	DropShadow {
		 	    	    radius: 20;
		 	    	    offsetX: 10;
		 	    	    offsetY: 10;
		 	    	}  
		 	    } else {
		 	    	null;
		 	    };
		 	    
		 	    onMouseClicked : function(event) {
		 	        puzzle.pieceClicked(this);
		 	        clickedOffsetX = event.sceneX-x;
		 	        clickedOffsetY = event.sceneY-y;
		 	    };
		 	    
		 	    onMouseMoved : function(event) {
		 	      	if (selected) {
		 	      	    x = event.sceneX - clickedOffsetX;
		 	      	    y = event.sceneY - clickedOffsetY;
		 	      	}  
		 	    };
		 	},
		 	Text {
		 	    content : bind logBounds(viewport);
		 	    translateX : bind x+10;
		 	    translateY : bind y+part.height;
		 	    textOrigin : TextOrigin.TOP;
		 	    visible: bind selected;
		 	}
    	]
    } 
    
    bound function logBounds(node : Node) : String {
        return "boundsInLocal = {relevantBounds(node.boundsInLocal)}\n"
               "boundsInParent({node.parent}) = {relevantBounds(node.boundsInParent)}\n"
               "layoutBounds = {relevantBounds(node.layoutBounds)}";
    }
    
    bound function relevantBounds(bounds : Bounds) : String {
        return "minX = {bounds.minX} minY={bounds.minY}";
    }
 	
 	public function scatter(bounds: Rectangle2D) : Void {
 	    x = Math.random() * (bounds.width - part.width);
 	    y = Math.random() * (bounds.height - part.height);
 	}
}
