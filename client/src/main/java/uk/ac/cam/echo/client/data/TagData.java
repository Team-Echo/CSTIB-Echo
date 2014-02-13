package uk.ac.cam.echo.client.data;

import uk.ac.cam.echo.data.Tag;

public class TagData extends BaseData implements Tag{

    private String name;
    protected void configureResource() {

    }
    public void configureResource(long conversationId) {
        setResource(getApi().conversationResource.getTagResource(conversationId));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
