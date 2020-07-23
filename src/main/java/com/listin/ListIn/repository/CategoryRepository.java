package com.listin.ListIn.repository;

import com.listin.ListIn.entity.Category;
import com.listin.ListIn.entity.ItemList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT DISTINCT c.* FROM category as c " +
            "JOIN itemList ON itemList.category_id = c.id " +
            "JOIN user ON itemList.user_id = user.id " +
            "WHERE user.id = :id", nativeQuery = true)
    public List<Category> findByUserId(@Param("id") Long id);

}
