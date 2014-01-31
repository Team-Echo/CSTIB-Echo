package uk.ac.cam.echo.client.data;

import uk.ac.cam.echo.client.ClientApi;

public class BaseData {
    private ClientApi api;

    public ClientApi getApi() {
        return api;
    }

    public void setApi(ClientApi api) {
        this.api = api;
    }
}
