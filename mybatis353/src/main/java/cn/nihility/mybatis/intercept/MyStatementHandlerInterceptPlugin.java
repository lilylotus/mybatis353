package cn.nihility.mybatis.intercept;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;

@Intercepts({
  @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
})
public class MyStatementHandlerInterceptPlugin implements Interceptor {

  private final static Logger log = LoggerFactory.getLogger(MyStatementHandlerInterceptPlugin.class);

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    log.debug("StatementHandle intercept plugin, method [{}], target [{}]",
      invocation.getMethod().getName(), invocation.getTarget().getClass().getName());
    return invocation.proceed();
  }

}
