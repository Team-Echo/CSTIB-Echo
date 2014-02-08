package uk.ac.cam.echo.server.analysis.internal;

import uk.ac.cam.echo.data.Conversation;

/**
 Author: Petar 'PetarV' Veličković

 An internal data class for the data analysis module.
 It stores a Double/Conversation pair, used to speed up internal computations
 by eliminating redundant calculations.
*/
public class DoubleConversationPair
{
    private Double num;
    private Conversation cnv;

    public DoubleConversationPair(Double d, Conversation c)
    {
        this.num = d;
        this.cnv = c;
    }

    public Double getDouble() { return num; }
    public Conversation getConvo() { return cnv; }
}
