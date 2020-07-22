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
}
