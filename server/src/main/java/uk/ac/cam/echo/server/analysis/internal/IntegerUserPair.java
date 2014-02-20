package uk.ac.cam.echo.server.analysis.internal;

import uk.ac.cam.echo.data.User;

/**
 Author: Petar 'PetarV' Veličković

 An internal data class for the data analysis module.
 It stores an Integer/User pair, used to speed up internal computations
 by eliminating redundant calculations.
*/
public class IntegerUserPair
{
    private Integer num;
    private User usr;

    public IntegerUserPair(Integer i, User u)
    {
        this.num = i;
        this.usr = u;
    }

    public Integer getInt() { return num; }
    public User getUser() { return usr; }
}
