package uk.ac.cam.echo.server.analysis.internal;

import uk.ac.cam.echo.data.Conversation;

/**
 Author: Petar 'PetarV' Veličković

 An internal data class for the data analysis module.
 It stores an Integer/Conversation pair, used to speed up internal computations
 by eliminating redundant calculations.
*/
public class IntegerConversationPair
{
    private Integer num;
    private Conversation cnv;

    public IntegerConversationPair(Integer i, Conversation c)
    {
        this.num = i;
        this.cnv = c;
    }

    public Integer getInt() { return num; }
    public Conversation getConvo() { return cnv; }
}
