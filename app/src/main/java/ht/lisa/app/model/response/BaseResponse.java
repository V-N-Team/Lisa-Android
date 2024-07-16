package ht.lisa.app.model.response;

import androidx.annotation.Nullable;

public class BaseResponse {

    private static final String UNTYPED_ERROR = "untyped error";
    private static final String EXECUTION_BLOCKED = "execution blocked";
    private static final String INCORRECT_TOKEN = "missing or incorrect token";
    private static final String NO_TOKEN_RIGHTS = "requested operation for which the token does not have rights";
    private static final String USER_IS_BLOCKED = "the user is blocked";
    private static final String PIN_CODE_NEEDED = "pin code authorization is needed";
    private static final String INVALID_PARAMETER = "missing or invalid ";

    private int state;
    @Nullable
    private String invalid;
    @Nullable
    private int delay;

    public int getState() {
        return state;
    }

    @Nullable
    public int getDelay() {
        return delay;
    }

    @Nullable
    public String getInvalid() {
        return invalid;
    }

    public String getBaseErrorMessage() {
        String errorMessage;
        switch (getState()) {

            case 1:
                errorMessage = UNTYPED_ERROR;
                break;

            case 2:
                errorMessage = EXECUTION_BLOCKED;
                break;

            case 3:
                errorMessage = INCORRECT_TOKEN;
                break;

            case 4:
                errorMessage = NO_TOKEN_RIGHTS;
                break;

            case 5:
                errorMessage = INVALID_PARAMETER + getInvalid();
                break;

            case 6:
                errorMessage = "Try in " + getDelay() + "sec";
                break;

            case 7:
                errorMessage = USER_IS_BLOCKED;
                break;

            case 8:
                errorMessage = PIN_CODE_NEEDED;
                break;

            default:
                errorMessage = "";
                break;

        }
        return errorMessage;
    }
}
