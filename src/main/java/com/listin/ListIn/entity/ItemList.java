package com.listin.ListIn.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "itemList")
public class ItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "itemList")
    private List<ItemOnCheckList> itemOnCheckLists;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

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
}
