package com.example.db.dao;

import com.example.db.entity.OperationStatus;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

@JdbiRepository
public interface SyncOperationStatusDao {

  @InTransaction
  @SqlUpdate("insert into sync_operation_status values (:getOperationStatusId.toString, (select sync_status_id from sync_status where name = :getStatus), :getOperationId.toString, :getCreated)")
  public void create(@BindMethods OperationStatus operationStatus);

  @InTransaction
  @SqlQuery("select os.sync_status_id o_operation_status_id, os.sync_operation_id o_operation_id, ss.name o_status, os.created o_created from sync_operation_status os left join sync_status ss on ss.sync_status_id = os.sync_status_id where os.sync_operation_id = :operationId order by os.created desc limit 1")
  @RegisterConstructorMapper(value = OperationStatus.class, prefix = "o")
  public OperationStatus get(@Bind("operationId") String operationId);

}
