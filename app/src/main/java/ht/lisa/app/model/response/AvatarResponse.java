package ht.lisa.app.model.response;

import java.util.List;

import ht.lisa.app.model.Avatar;

public class AvatarResponse extends BaseResponse {

    private List<Avatar> avatars;

    public AvatarResponse(List<Avatar> avatars) {
        this.avatars = avatars;
    }

    public List<Avatar> getAvatars() {
        return avatars;
    }

    public void setAvatars(List<Avatar> avatars) {
        this.avatars = avatars;
    }
}
