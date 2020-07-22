package com.listin.ListIn.repository;

import com.listin.ListIn.entity.ItemOnCheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemOnCheckListRepository extends JpaRepository<ItemOnCheckList, Long> {
}
