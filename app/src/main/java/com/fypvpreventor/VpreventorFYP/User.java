package com.fypvpreventor.VpreventorFYP;

public class User {

    public String fullname,age,email;

    public User() {
        // Required empty constructor for Firebase
    }

    public User(String fullname, String age, String email){
        this.fullname=fullname;
        this.age=age;
        this.email=email;
    }
}
