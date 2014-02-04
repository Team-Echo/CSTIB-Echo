package uk.ac.cam.echo.server.models;

import org.apache.commons.beanutils.PropertyUtils;
import org.codehaus.jackson.annotate.JsonCreator;
import uk.ac.cam.echo.server.HibernateUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BaseModel {
    public void delete() {
        HibernateUtil.getTransaction().delete(this);
    }

    public void save() {
        HibernateUtil.getTransaction().save(this);
    }

    public BaseModel() {

    }
    @JsonCreator
    public BaseModel(Map<String, Object> props, String[] allowed) {
        // Update Method
        if (props.containsKey("id") && props.get("id") != null) {
            sanitizieLong(props, "id");

            HibernateUtil.getTransaction().load(this, (Long) props.get("id"));
        }

        for (String f: allowed) {
            sanitizieLong(props, f);
            if (!props.containsKey(f))
                continue;
            try {
                PropertyUtils.setProperty(this, f, props.get(f));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
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
