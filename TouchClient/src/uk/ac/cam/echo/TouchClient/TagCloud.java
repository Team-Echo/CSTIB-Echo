package uk.ac.cam.echo.TouchClient;

import javafx.application.Application;
import javafx.scene.text.Text;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import static javafx.application.Application.launch;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 *
 * @author Mona
 */
public class TagCloud extends Application {
    
    @Override
    public void start(Stage primaryStage)
    {
    }
    
    public static Group getTagGroup(HashMap map, double width, double height) {
        
        Group g = new Group();
        
        if (map.entrySet().isEmpty()) 
        {
            // behaviour for empty tag map, display blank screen
            return g;
        }
        
        double minweight = Double.POSITIVE_INFINITY;
        double maxweight = 0;
        Iterator it1 = map.entrySet().iterator();
        while( it1.hasNext() )
        {
            int weight = (Integer)((Map.Entry)it1.next()).getValue();
            double w = (double)weight;
            if ( w > maxweight)
            {
                maxweight = w;
            }
            if ( w < minweight)
            {
                minweight = w;
            }
        }
        
        Iterator it2 = map.entrySet().iterator();
        double x_coord = 0;
        double y_coord = 0;
        // max_height defines max height of any font on a line, 
        // used to leave an appropriate distance between lines.
        double max_height = 0;
        
        int min_value = Integer.MAX_VALUE;
        
        Text text;
        String word;
        
        while(y_coord < height)
        {
            while(x_coord < width && it2.hasNext())
            {
                Map.Entry pair = (Map.Entry)it2.next();
                int value = (Integer)pair.getValue();
                if (value < min_value) { min_value = value; }
                double weight = (double)value;
                word = (String)pair.getKey();
                text = new Text(x_coord,y_coord,word);
                text.setFont(setFontEmphasis(word,weight,maxweight,minweight));
                
                 /**
                 * Text colour calculation.
                 * calculated by first transforming elements in
                 * the range [minweight,maxweight] to elements in the range
                 * [0,9], then each this range is split into sub-bands which each 
                 * represent an different colour.
                 */
                double colour_double =  (9)*(weight - minweight)/(maxweight - minweight);
                int colour_index = (new Double(colour_double)).intValue();
                String c = "#2CB1E1"; // default tag colour is echo blue
                switch (colour_index) 
                {
                    case 9: 
                        c = "#1A6A87";
                        break;
                    case 8: 
                        c = "#1F7C9E";
                        break;
                    case 7: 
                        c = "#238EB4";
                        break;
                    case 6: 
                        c = "#289FCA";
                        break;
                    case 5: 
                        c = "#2CB1E1"; // echo blue
                        break;
                    case 4: 
                        c = "#41B9E4";
                        break;
                    case 3: 
                        c = "#56C1E7";
                        break;
                    case 2: 
                        c = "#6BC8EA";
                        break;
                    case 1: 
                        c = "#80D0ED";
                        break;
                    case 0: 
                        c = "#96D8F0";
                        break;
                }
                Paint p = Color.web(c);
                text.setFill(p);
                
                g.getChildren().add(text);
                
                // subject to change
                Bounds bounds = text.getLayoutBounds();
                x_coord += bounds.getWidth() + 10;
                double h = bounds.getHeight() / 2; // maybe divide this by two
                if ( h > max_height ) { max_height = h; }
            }
            // re-zero x_coord,for the beginning of a new line
            x_coord = 0;
            //set to 50 pixels between lines for now, subject to change
            y_coord += (max_height + 10);
            // re-zero max_height,for the beginning of a new line
            max_height = 0;
        }
        
        return g;
        
        /**
         * incremental tag replacement
         */
        
    }
        
    private static Font setFontEmphasis(String word, double weight, double maxweight, double minweight)
    {
        /**
         * Font size calculation.
         * Calculated via function that transforms
         * elements in a range, [min_weight,max_weight] to elements of
         * the range [min_size,max_size].
         */
        
        double min_weight = minweight;
        double max_weight = maxweight;
        double min_size = 16;
        double max_size = 64;
        double size =  (max_size - min_size)*(weight - min_weight)/(max_weight - min_weight) + min_size;
        Font font = new Font(size);
        return font;
       
    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
