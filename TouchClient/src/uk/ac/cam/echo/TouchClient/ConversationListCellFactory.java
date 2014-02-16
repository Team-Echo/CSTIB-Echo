/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.cam.echo.TouchClient;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import uk.ac.cam.echo.data.Conversation;

/**
 *
 * @author Philip
 */
class ConversationListCellFactory implements Callback<ListView<Conversation>, ListCell<String>> {

    public ConversationListCellFactory() {
    }

    @Override
    public ListCell<String> call(final ListView<Conversation> list) {
        final ListCell cell = new ListCell() {
            private Text text;

                @Override
                public void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!isEmpty()) {
                        text = new Text(((Conversation)item).getName());
                        text.setWrappingWidth(list.getPrefWidth()-30);
                        setGraphic(text);
                    }
                }
         };

         return cell;
    }
    
}
