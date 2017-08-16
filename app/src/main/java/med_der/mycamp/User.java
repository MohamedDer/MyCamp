package med_der.mycamp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohamed Derkaoui on 10/08/2017.
 */

public class User implements Parcelable {
    private String Id;
    private String DisplayName;
    private String PhotoURL;
    private String Team;
    private String Tel;
    private String Email;


    public User(String id,String name, String photoURL,String email, String tel, String team){
        this.Id=id;
        this.DisplayName=name;
        this.PhotoURL=photoURL;
        this.Email=email;
        this.Team=team;
        this.Tel=tel;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public String getTeam() {
        return Team;
    }

    public String getTel() {
        return Tel;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
