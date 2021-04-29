package cn.nihility.mybatis.intercept;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;

@Intercepts({
  @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
public class MyResultSetHandlerInterceptPlugin implements Interceptor {

  private final static Logger log = LoggerFactory.getLogger(MyResultSetHandlerInterceptPlugin.class);

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    log.debug("ResultSetHandler intercept plugin, method [{}], target [{}]",
      invocation.getMethod().getName(), invocation.getTarget().getClass().getName());
    return invocation.proceed();
  }

}
