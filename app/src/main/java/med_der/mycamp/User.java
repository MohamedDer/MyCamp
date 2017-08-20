package med_der.mycamp;

import android.os.Parcel;
import android.os.Parcelable;


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

    protected User(Parcel in) {
        Id = in.readString();
        DisplayName = in.readString();
        PhotoURL = in.readString();
        Team = in.readString();
        Tel = in.readString();
        Email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
        dest.writeString(Id);
        dest.writeString(DisplayName);
        dest.writeString(PhotoURL);
        dest.writeString(Team);
        dest.writeString(Tel);
        dest.writeString(Email);
    }
}
