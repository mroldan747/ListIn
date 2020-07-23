package com.listin.ListIn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "itemList")
public class ItemList implements Comparable<ItemList>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "itemList")
    private List<ItemOnCheckList> itemOnCheckLists = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


    public ItemList() {
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemOnCheckList> getItemOnCheckLists() {
        return itemOnCheckLists;
    }

    public void setItemOnCheckLists(List<ItemOnCheckList> itemOnCheckLists) {
        this.itemOnCheckLists = itemOnCheckLists;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    @Override
    public int compareTo(ItemList other){
        return category.getName().compareTo(other.getCategory().getName());
    }
}
