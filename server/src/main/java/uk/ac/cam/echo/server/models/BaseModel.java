package uk.ac.cam.echo.server.models;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.annotate.JsonCreator;
import uk.ac.cam.echo.server.HibernateUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BaseModel {
    public void delete() {
        HibernateUtil.getTransaction().delete(this);
    }

    public BaseModel() {

    }
    @JsonCreator
    public BaseModel(Map<String, Object> props, String[] allowed) {
        sanitizieLong(props, "id");

        HibernateUtil.getTransaction().load(this, (Long) props.get("id"));

        for (String f: allowed) {
            if (!props.containsKey(f))
                continue;
            try {
                BeanUtils.setProperty(this, f, props.get(f));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void sanitizieLong(Map<String, Object> props, String key) {
        if (props.containsKey(key) && props.get(key) instanceof Integer) {
            props.put(key, ((Integer) props.get(key)).longValue());
        }
    }
}
