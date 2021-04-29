package cn.nihility.mybatis.intercept;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

/**
 * 默认拦截器拦截顺序
 * 1. Executor
 * 2. ParameterHandler
 * 3. StatementHandler
 * 4. ResultSetHandler
 *
 * 支持的拦截类和方法
 * - {@link Executor} (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
 *    {@link org.apache.ibatis.executor.CachingExecutor}
 * - {@link ParameterHandler} (getParameterObject, setParameters)
 *    {@link org.apache.ibatis.scripting.defaults.DefaultParameterHandler}
 * - {@link ResultSetHandler} (handleResultSets, handleOutputParameters)
 *    {@link org.apache.ibatis.executor.resultset.DefaultResultSetHandler}
 * - {@link StatementHandler} (prepare, parameterize, batch, update, query)
 *    {@link org.apache.ibatis.executor.statement.RoutingStatementHandler}
 *
 * mybatis plugin 拦截处理
 * @see Configuration#newExecutor(Transaction, ExecutorType)
 * @see Configuration#newParameterHandler(MappedStatement, Object, BoundSql)
 * @see Configuration#newResultSetHandler(Executor, MappedStatement, RowBounds, ParameterHandler, ResultHandler, BoundSql)
 * @see Configuration#newStatementHandler(Executor, MappedStatement, Object, RowBounds, ResultHandler, BoundSql)
 *
 */
@Intercepts({
  @Signature(
    type = StatementHandler.class,
    method = "prepare",
    args = {Connection.class, Integer.class}
  )
})
public class StatementInterceptPlugin implements Interceptor {

  private static final Logger log = LoggerFactory.getLogger(StatementInterceptPlugin.class);

  /**
   * 拦截目标对象的目标方法的执行，将自定义逻辑写在该方法中
   */
  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    log.debug("statement intercept plugin method [{}]", invocation.getMethod());

    // MetaObject 是 Mybatis 提供的一个用于访问对象属性的对象
    MetaObject metaObject = SystemMetaObject.forObject(invocation);

    // RoutingStatementHandler

    log.debug("当前拦截到的对象 [{}]", metaObject.getValue("target"));
    String executeSql = String.valueOf(metaObject.getValue("target.delegate.boundSql.sql"));
    executeSql = executeSql.replaceAll("[\r\n]", "").replaceAll("\\s+", " ");
    log.debug("SQL 语句 [{}]", executeSql);
    log.debug("SQL 语句入参 [{}]", metaObject.getValue("target.delegate.parameterHandler.parameterObject"));
    log.debug("SQL 语句类型 [{}]", metaObject.getValue("target.delegate.parameterHandler.mappedStatement.sqlCommandType"));
    log.debug("Mapper 方法全路径名 [{}]", metaObject.getValue("target.delegate.parameterHandler.mappedStatement.id"));

    // 修改 SQL 语句，可以修改自定义 sql
    /*String newSQL = metaObject.getValue("target.delegate.boundSql.sql") + " limit 2";
    metaObject.setValue("target.delegate.boundSql.sql", newSQL);
    log.debug("修改后SQL语句：" + newSQL);*/

    // 返回执行结果
    return invocation.proceed();
  }

  /**
   * 为目标对象创建一个代理对象，使用 Plugin.wrap(target,this) 即可
   * @param target 上次包装的代理对象
   * @return 本次包装过的代理对象
   */
  @Override
  public Object plugin(Object target) {
    log.debug("statement intercept plugin [{}]", target);
    /*
    * 当前 wrapper （包装） 的对象
    * org.apache.ibatis.executor.CachingExecutor
    * org.apache.ibatis.scripting.defaults.DefaultParameterHandler
    * org.apache.ibatis.executor.resultset.DefaultResultSetHandler
    * org.apache.ibatis.executor.statement.RoutingStatementHandler
    * */
    return Plugin.wrap(target, this);
  }

  /**
   * 获取自定义配置参数
   * @param properties 属性配置
   */
  @Override
  public void setProperties(Properties properties) {
    log.debug("插件配置信息 [{}]", properties);
    log.debug("plugin property pluginProperty:[{}]", properties.get("pluginProperty"));
  }
}
