package ru.captive.model;


import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDate;

@Entity
public class Account extends BaseModel {
    @Size(min = 5, groups = {Existing.class, New.class})
    @NotNull(
            groups = {Existing.class, New.class}
    )
    private String login;
    @Size(min = 6, groups = {Existing.class, New.class})
    @NotNull(
            groups = {Existing.class, New.class}
    )
    private String password;
    private Date date = Date.valueOf(LocalDate.now());

    public Account() {
        super();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Account{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", date=" + date +
                '}';
    }
}
