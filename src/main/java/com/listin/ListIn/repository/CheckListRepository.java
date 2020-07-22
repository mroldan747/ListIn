package com.listin.ListIn.repository;

import com.listin.ListIn.entity.CheckList;
import com.listin.ListIn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckListRepository extends JpaRepository<CheckList, Long> {

    public List<CheckList> findAllByUserAndName(User user, String name);
    public List<CheckList> findAllByUser(User user);
}
