package com.demo.board.domain;

import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "ILDONG_BOARD")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank
    @NotNull
    private String title;

    @NotBlank
    @NotNull
    @Column(columnDefinition="CLOB")
    private String content;

    @Size(min = 5, max = 20)
    private String writer;

    private boolean deleted;

    @Email
    private String email;

    @Column(length = 20, nullable = false)
    private String password;

    public Content() {
    }

    @Builder
    public Content(String title, String content, String writer, String password, String email){
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.password = password;
        this.email = email;
        this.deleted = false;
    }

    public Content(Content content) {
    }


    public boolean matchId(Integer id){
        if(this.id == id){
            return false;
        }
        return id.equals(this.password);
    }


    public boolean matchPassword(String password){
        if(password == null){
            return false;
        }
        return password.equals(this.password);
    }

    public Integer getId() {
        return id;
    }

//    public String getPassword() {
//        return password;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String w) {
        this.writer = w;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
