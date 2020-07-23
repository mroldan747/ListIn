package com.listin.ListIn.repository;

import com.listin.ListIn.entity.ItemList;
import com.listin.ListIn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemListRepository extends JpaRepository<ItemList, Long> {

    @Query(value = "SELECT * FROM itemList WHERE user_id = :userId AND name LIKE %:name%",
            nativeQuery = true)
    List<ItemList> findAllByUserAndNameLike(Long userId, String name);

    List<ItemList> findAllByUser(User user);

    @Query(value = "SELECT DISTINCT items.* FROM itemList as items " +
            "LEFT JOIN category ON category.id = items.category_id " +
            "LEFT JOIN item_on_checklist ON items.id = item_on_checklist.itemList_id " +
            "LEFT JOIN checklist ON checklist.id = item_on_checklist.checklist_id " +
            "WHERE checklist.id = :checklist " +
            "AND items.name LIKE %:search% " +
            "OR category.name LIKE %:search%", nativeQuery = true)
    List<ItemList> findAllByCheckList(@Param("checklist") Long checklist, @Param("search") String search);
}
