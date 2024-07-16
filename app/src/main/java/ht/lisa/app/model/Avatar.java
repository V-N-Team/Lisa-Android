package ht.lisa.app.model;

import java.io.Serializable;
import java.util.List;

public class Avatar implements Serializable {

    private int id;
    private String url;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setAvatarChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public static String getAvatarUrl(List<Avatar> avatars, int id) {
        String url = null;
        for (Avatar avatar : avatars) {
            if (avatar.getId() == id) url = avatar.getUrl();
        }
        return url;
    }
}
