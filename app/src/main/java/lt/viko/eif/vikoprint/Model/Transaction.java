package lt.viko.eif.vikoprint.Model;


import com.google.firebase.auth.FirebaseUser;

public class Transaction {
    private String date;
    private int points;
    private int pageCount;
    private String firebaseUser;

    public Transaction(String date, int points, int pageCount, String firebaseUser) {
        this.date = date;
        this.points = points;
        this.pageCount = pageCount;
        this.firebaseUser = firebaseUser;
    }

    public Transaction(){}

    public String getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(String firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date='" + date + '\'' +
                ", points=" + points +
                ", pageCount=" + pageCount +
                ", firebaseUser=" + firebaseUser +
                '}';
    }
}
