package lt.viko.eif.vikoprint.Model;

import android.net.Uri;

public class Profile {
    private String email;
    private Uri imageURL;
    private int points;

    public Profile(String email, Uri imageURL, int points) {
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

    public Uri getImageURL() {
        return imageURL;
    }

    public void setImageURL(Uri imageURL) {
        this.imageURL = imageURL;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "email='" + email + '\'' +
                ", imageURL=" + imageURL +
                ", points=" + points +
                '}';
    }
}
