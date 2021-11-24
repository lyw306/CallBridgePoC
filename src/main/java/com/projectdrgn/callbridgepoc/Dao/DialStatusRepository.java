package com.projectdrgn.callbridgepoc.Dao;

import com.projectdrgn.callbridgepoc.model.DialStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DialStatusRepository extends JpaRepository<DialStatus, Long> {

}
