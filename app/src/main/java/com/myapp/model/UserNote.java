package com.myapp.model;

public class UserNote {
    int id;
    User user;
    EnWord enWord;
    String note;
    public UserNote() {
        this.user = new User();
        this.enWord = new EnWord();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EnWord getEnWord() {
        return enWord;
    }

    public void setEnWord(EnWord enWord) {
        this.enWord = enWord;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
