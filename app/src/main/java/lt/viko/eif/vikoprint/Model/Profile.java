package lt.viko.eif.vikoprint.Model;

import android.net.Uri;

import org.jetbrains.annotations.NotNull;

public class Profile {
    private String email;
    private String imageURL;
    private int points;

    public Profile(String email, String imageURL, int points) {
        this.email = email;
        this.imageURL = imageURL;
        this.points = points;
    }
    public Profile(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @NotNull
    @Override
    public String toString() {
        return "Profile{" +
                "email='" + email + '\'' +
                ", imageURL=" + imageURL +
                ", points=" + points +
                '}';
    }
}
