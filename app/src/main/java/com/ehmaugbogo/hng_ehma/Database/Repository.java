package com.ehmaugbogo.hng_ehma.Database;

import android.content.Context;

import com.ehmaugbogo.hng_ehma.Model.User;

import java.util.List;

public class Repository {
    private UserDao userDao;

    public Repository(Context context) {
        UserDatabase userDatabase = UserDatabase.getInstance(context);
        userDao = userDatabase.userDao();
    }

    public Long insertUser(User user){
        return userDao.insertUser(user);
    }

    public void updateUser(User user){
        userDao.updateUser(user);
    }

    public void deleteUser(User user){
        userDao.deleteUser(user);
    }

    public List<User> getallUsers(){
        return userDao.getallUsers();
    }


}
