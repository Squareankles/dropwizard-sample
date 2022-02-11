package com.example.db.dao;

import com.example.db.entity.FileSync;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

@JdbiRepository
public interface FileSyncDao {

  @InTransaction
  @SqlUpdate("insert into sync_operation_payload values (:getId.toString, :getOperationId.toString, :getLocation, :getFileKey, :getCreated)")
  void create(@BindMethods FileSync fileSync);

  @InTransaction
  @SqlQuery("select payload_id o_id, sync_operation_id o_operation_id, location_url o_location, file_key o_file_key, payload_sync_tmstp o_created from sync_operation_payload where sync_operation_id = :operationId ")
  @RegisterConstructorMapper(value = FileSync.class, prefix = "o")
  FileSync get(@Bind("operationId") String operationId);

}
