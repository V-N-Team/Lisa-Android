package ht.lisa.app.ui.registration.error;

import android.graphics.Bitmap;

public class OfflineLotto {
    private Bitmap logo;
    private String name;
    private String numbers;

    public OfflineLotto(Bitmap logo, String name, String numbers) {
        this.logo = logo;
        this.name = name;
        this.numbers = numbers;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}
