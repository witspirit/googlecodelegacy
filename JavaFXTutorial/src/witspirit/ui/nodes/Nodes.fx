package witspirit.ui.nodes;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.transform.Transform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Group;

Stage {
    title: "Nodes";
    scene : Scene {
        width : 220;
        height : 170;
        fill : Color.LIGHTBLUE;
        content : Group {
            translateX : 55;
            translateY : 10;
        	content : [ 
	        	Circle {
	        	    centerX: 50;
	        		centerY: 50;
	        	    radius: 50;
	        	    fill: Color.WHITE;
	        	    stroke : Color.YELLOW;
	        	},
	        	Text {
	        	    content: "Duke";
	        	    transforms : Transform.rotate(33, 10, 100);
	        	    
	        	},
	        	ImageView {
	        	    image : Image {
	        	        url : "{__DIR__}dukewave.png"; 
	        	    }
	        	}
	        ];
        }
    }
}

