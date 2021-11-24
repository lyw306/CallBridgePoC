package com.projectdrgn.callbridgepoc.Dao;

import com.projectdrgn.callbridgepoc.model.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordStatusRepository extends JpaRepository<RecordStatus, Long> {

}
