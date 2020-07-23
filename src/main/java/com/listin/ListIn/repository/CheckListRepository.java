package com.listin.ListIn.repository;

import com.listin.ListIn.entity.CheckList;
import com.listin.ListIn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckListRepository extends JpaRepository<CheckList, Long> {

    public List<CheckList> findAllByUserAndName(User user, String name);
    public List<CheckList> findAllByUser(User user);

    @Query(value = "SELECT c.* FROM checklist AS c WHERE name LIKE %:name% AND user_id = :user", nativeQuery = true)
    public List<CheckList> findAllByNameLikeAndUser(@Param("name") String name, @Param("user") Long user);
}
