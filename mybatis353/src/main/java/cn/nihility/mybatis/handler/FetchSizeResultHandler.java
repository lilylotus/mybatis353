package cn.nihility.mybatis.handler;

import cn.nihility.mybatis.entity.Flower;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

public class FetchSizeResultHandler implements ResultHandler<Flower> {

  @Override
  public void handleResult(ResultContext<? extends Flower> ctx) {
    Flower flower = ctx.getResultObject();
    int resultCount = ctx.getResultCount();
    System.out.println(resultCount + " " + flower);
  }

}
