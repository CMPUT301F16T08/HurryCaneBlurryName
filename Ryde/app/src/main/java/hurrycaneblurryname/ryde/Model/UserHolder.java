package hurrycaneblurryname.ryde.Model;

/**
 * Created by Zone on 2016/11/11.
 */

// http://stackoverflow.com/questions/4878159/whats-the-best-way-to-share-data-between-activities
public class UserHolder {
    private User user;
    public User getUser(){
        return user;
    }

    public void setUser(User userToStore){
        this.user = userToStore;
    }

    private static final UserHolder holder = new UserHolder();
    public static UserHolder getInstance() {return holder;}

}
