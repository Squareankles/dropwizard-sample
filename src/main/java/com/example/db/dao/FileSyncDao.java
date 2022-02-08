package com.example.db.dao;

import com.example.db.entity.FileSync;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;

@JdbiRepository
public interface FileSyncDao {

  @SqlUpdate("insert into sync_operation_payload values (:getId.toString, :getOperationId.toString, :getLocation, :getCreated)")
  void create(@BindMethods FileSync fileSync);

}
