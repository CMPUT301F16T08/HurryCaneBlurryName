package hurrycaneblurryname.ryde;

/**
 * Created by pocrn_000 on 10/12/2016.
 */

public class User {

    private String username;
    private String phone;
    private String email;
    private RequestList requests;

    public User(String username){
        requests = new RequestList();
        this.username = username;
    }

    public void addRequest(Request request) {
        requests.addRequest(request);
    }

    public boolean hasRequest(Request request) {
        return requests.contains(request);
    }

    public Request getRequest(int i) {
        return requests.getRequest(i);
    }

    public void removeRequest(Request request) {
        requests.removeRequest(request);
    }

    public String getUsername() {
        return username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
