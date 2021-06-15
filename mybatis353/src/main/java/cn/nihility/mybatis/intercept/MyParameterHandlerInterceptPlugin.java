package cn.nihility.mybatis.intercept;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.util.Properties;

@Intercepts({
  @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class}),
  @Signature(type = ParameterHandler.class, method = "getParameterObject", args = {})
})
public class MyParameterHandlerInterceptPlugin implements Interceptor {

  private static final Logger log = LoggerFactory.getLogger(MyParameterHandlerInterceptPlugin.class);

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    log.debug("ParameterHandler intercept plugin, method [{}], target [{}]",
      invocation.getMethod().getName(), invocation.getTarget().getClass().getName());
    return invocation.proceed();
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {

  }
}
