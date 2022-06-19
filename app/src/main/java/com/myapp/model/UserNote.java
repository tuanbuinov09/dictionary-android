package com.myapp.model;

public class UserNote {
    Long id;
    User user;
    EnWord enWord;
    Long userId;
    Long wordId;
    String note;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWordId() {
        return wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public UserNote() {
        this.user = new User();
        this.enWord = new EnWord();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
