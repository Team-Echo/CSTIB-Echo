/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import uk.ac.cam.echo.data.Message;

/**
 *
 * @author Philip
 */
public class messageCellFactory implements Callback<ListView<String>, ListCell<String>>{
    @Override
    public ListCell<String> call(final ListView<String> list) {
        final ListCell cell = new ListCell() {
            private GridPane cellContents;
            private Text name;
            private Text text;

                @Override
                public void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!isEmpty()) {
                        Message msg = (Message)item;
                        String sender;
                        if (msg.getSender() == null) sender = "Anonymous";
                        else if (msg.getSender().getDisplayName() != null) sender = msg.getSender().getDisplayName();
                        else sender = msg.getSender().getUsername();
                        sender = sender.concat(" : ");
                        //String sender = (msg.getSender() == null ? "Anonymous" : msg.getSender().getDisplayName()).concat(" : ");
                        String message = "   ".concat(msg.getContents());
                        cellContents = new GridPane();
                        name = new Text(sender);
                        text = new Text(message);
                        text.setWrappingWidth(list.getPrefWidth()-30);
                        cellContents.add(name, 0, 0);
                        cellContents.add(text, 0, 1);
                        setGraphic(cellContents);
                    }
                }
         };

         return cell;
    }
}
