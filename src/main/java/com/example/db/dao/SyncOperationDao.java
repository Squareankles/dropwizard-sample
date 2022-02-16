package com.example.db.dao;

import com.example.db.entity.DataSyncOperation;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

@JdbiRepository
public interface SyncOperationDao {

  @InTransaction
  @SqlUpdate("insert into sync_operation values (:getOperationId.toString, :getCustomerId, :getCreated)")
  void create(@BindMethods DataSyncOperation syncOperation);

  @InTransaction
  @SqlQuery("select sync_operation_id o_operation_id, customer_id o_customer_id, request_tmstp o_created from sync_operation where sync_operation_id = :operationId ")
  @RegisterConstructorMapper(value = DataSyncOperation.class, prefix = "o")
  DataSyncOperation get(@Bind("operationId") String operationId);

}
