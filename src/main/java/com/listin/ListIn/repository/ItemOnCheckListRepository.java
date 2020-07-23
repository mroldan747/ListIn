package com.listin.ListIn.repository;

import com.listin.ListIn.entity.CheckList;
import com.listin.ListIn.entity.ItemList;
import com.listin.ListIn.entity.ItemOnCheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemOnCheckListRepository extends JpaRepository<ItemOnCheckList, Long> {

    public List<ItemOnCheckList> findAllByCheckList(CheckList checkList);
    Optional<ItemOnCheckList> findByCheckListAndItemList(CheckList checkList, ItemList itemList);

    @Query(value = "SELECT DISTINCT itOcheck.* FROM itemList as items " +
            "LEFT JOIN category ON category.id = items.category_id " +
            "LEFT JOIN item_on_checklist as itOcheck ON items.id = item_on_checklist.itemList_id " +
            "LEFT JOIN checklist ON checklist.id = item_on_checklist.checklist_id " +
            "WHERE checklist.id = :checklist " +
            "AND items.name LIKE %:search% " +
            "OR category.name LIKE %:search%", nativeQuery = true)
    List<ItemOnCheckList> findAllByCheckList(@Param("checklist") Long checklist, @Param("search") String search);
}
