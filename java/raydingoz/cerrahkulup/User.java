package raydingoz.cerrahkulup;

/**
 * Created by Rafi on 26.03.2017.
 */

public class User {

    public String isim;
    public String email;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }
    

    public User(String isim, String email) {
        this.isim = isim;
        this.email = email;
    }


}