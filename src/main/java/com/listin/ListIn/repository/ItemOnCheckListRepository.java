package com.listin.ListIn.repository;

import com.listin.ListIn.entity.CheckList;
import com.listin.ListIn.entity.ItemList;
import com.listin.ListIn.entity.ItemOnCheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemOnCheckListRepository extends JpaRepository<ItemOnCheckList, Long> {

    public List<ItemOnCheckList> findAllByCheckList(CheckList checkList);
    Optional<ItemOnCheckList> findByCheckListAndItemList(CheckList checkList, ItemList itemList);
}
