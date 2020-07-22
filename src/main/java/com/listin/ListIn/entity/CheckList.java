package com.listin.ListIn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "checklist")
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "checkList")
    @JsonIgnore
    private List<ItemOnCheckList> itemOnCheckLists = new ArrayList<>();

    public CheckList() {
    }

    public CheckList(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ItemOnCheckList> getItemOnCheckLists() {
        return itemOnCheckLists;
    }

    public void setItemOnCheckLists(List<ItemOnCheckList> itemOnCheckLists) {
        this.itemOnCheckLists = itemOnCheckLists;
    }
}
