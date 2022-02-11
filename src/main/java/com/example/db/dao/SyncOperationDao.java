package com.example.db.dao;

import com.example.db.entity.DataSyncOperation;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

@JdbiRepository
public interface SyncOperationDao {

  @InTransaction
  @SqlUpdate("insert into sync_operation values (:getOperationId.toString, :getCustomerId, :getCreated)")
  public void create(@BindMethods DataSyncOperation syncOperation);

}
