package ht.lisa.app.ui.main;

public class TopSheetDialogEvent {
    private final String message;

    public TopSheetDialogEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
