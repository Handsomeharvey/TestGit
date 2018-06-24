package com.harvey.mvpandroid.login;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harvey on 2016/10/14 0014 09:08
 */
public class LoginModule implements Parcelable {
    String token;
    boolean is_password;
    String phone;
    String password;

    public LoginModule(String token, String phone, String password) {
        this.token = token;
        this.phone = phone;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean is_password() {
        return is_password;
    }

    public void setIs_password(boolean is_password) {
        this.is_password = is_password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeByte(this.is_password ? (byte) 1 : (byte) 0);
        dest.writeString(this.phone);
        dest.writeString(this.password);
    }

    public LoginModule() {
    }

    protected LoginModule(Parcel in) {
        this.token = in.readString();
        this.is_password = in.readByte() != 0;
        this.phone = in.readString();
        this.password = in.readString();
    }

    public static final Creator<LoginModule> CREATOR = new Creator<LoginModule>() {
        @Override
        public LoginModule createFromParcel(Parcel source) {
            return new LoginModule(source);
        }

        @Override
        public LoginModule[] newArray(int size) {
            return new LoginModule[size];
        }
    };
}
