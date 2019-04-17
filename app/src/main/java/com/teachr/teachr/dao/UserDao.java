package com.teachr.teachr.dao;

import com.teachr.teachr.models.User;

import java.util.List;

public interface UserDao {

    User getUser(String id);
    User findUser(String key, String value);
    List<User> findUsers(String key, String value, int comparator);
    List<User> findUsers(List<SearchCriteria> searchCriteria);
    void addUser(User user);
    void deleteUser(String id);
    void updateUser(String id, User user);

}
