/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import uk.ac.cam.echo.data.User;

/**
 *
 * @author Philip
 */
class avitarCellFactory implements Callback<ListView<User>, ListCell<String>> {

    public avitarCellFactory() {
    }

    @Override
    public ListCell<String> call(ListView<User> list) {
        final ListCell cell = new ListCell() {
            private GridPane cellContents;
            private Text name;
            private ImageView avitar;

                @Override
                public void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!isEmpty()) {         
                        User user = (User)item;
                        
                        name = (user.getUsername()==null?new Text("Anonymous"):new Text(user.getUsername()));
                        
                        avitar = new ImageView();
                        avitar.setImage(true? new Image("http://www.able-removals.co.uk/wp-content/uploads/2013/10/avatar-male-silhouette-hi.png"):
                                                new Image("http://www.able-removals.co.uk/wp-content/uploads/2013/10/avatar-male-silhouette-hi.png"));
                        
                        cellContents.add(avitar, 0, 0);
                        cellContents.add(name, 0, 1);
                        setGraphic(cellContents);
                    }
                }
         };

         return cell; 
    }
    
}
