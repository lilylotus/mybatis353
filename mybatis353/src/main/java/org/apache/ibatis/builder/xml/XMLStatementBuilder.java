/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.builder.xml;

import java.util.List;
import java.util.Locale;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

/**
 * @author Clinton Begin
 */
public class XMLStatementBuilder extends BaseBuilder {

  private final MapperBuilderAssistant builderAssistant;
  /* mapper.xml 中的 SELECT|INSERT|UPDATE|DELETE 节点配置 */
  private final XNode context;
  private final String requiredDatabaseId;

  public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context) {
    this(configuration, builderAssistant, context, null);
  }

  public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context, String databaseId) {
    super(configuration);
    this.builderAssistant = builderAssistant;
    this.context = context;
    this.requiredDatabaseId = databaseId;
  }

  public void parseStatementNode() {
    String id = context.getStringAttribute("id");
    String databaseId = context.getStringAttribute("databaseId");

    if (!databaseIdMatchesCurrent(id, databaseId, this.requiredDatabaseId)) {
      return;
    }

    // select|delete|update|insert
    String nodeName = context.getNode().getNodeName();
    // UNKNOWN, INSERT, UPDATE, DELETE, SELECT, FLUSH
    SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
    boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
    // 是否刷新缓存，true : 只要语句被调用，都会导致本地缓存和二级缓存被清空
    // 没有配置，默认为 !isSelect -> false (select -> false, other -> true)
    boolean flushCache = context.getBooleanAttribute("flushCache", !isSelect);
    // 是否使用缓存， true : 本条语句的结果被二级缓存缓存起来
    // select -> true, other -> false
    boolean useCache = context.getBooleanAttribute("useCache", isSelect);
    /*
    * 仅针对嵌套结果 select 语句
    * true，将会假设包含了嵌套结果集或是分组，当返回一个主结果行时，就不会产生对前面结果集的引用。
    * 这就使得在获取嵌套结果集的时候不至于内存不够用。
    * */
    boolean resultOrdered = context.getBooleanAttribute("resultOrdered", false);

    // Include Fragments before parsing
    // <sql></sql>
    XMLIncludeTransformer includeParser = new XMLIncludeTransformer(configuration, builderAssistant);
    includeParser.applyIncludes(context.getNode());

    // 所传参数类型，别名/全限定类名。 mybatis 大多时候可以自动推断
    String parameterType = context.getStringAttribute("parameterType");
    Class<?> parameterTypeClass = resolveClass(parameterType);

    // 数据库厂商标识 ID
    String lang = context.getStringAttribute("lang");
    /* default driver class : org.apache.ibatis.scripting.xmltags.XMLLanguageDriver */
    LanguageDriver langDriver = getLanguageDriver(lang);

    // 针对 INSERT, UPDATE 主键字段值填充
    // <selectKey keyProperty="id" resultType="int" order="BEFORE" statementType="PREPARED">
    // Parse selectKey after includes and remove them.
    processSelectKeyNodes(id, parameterTypeClass, langDriver);

    // Parse the SQL (pre: <selectKey> and <include> were parsed and removed)
    KeyGenerator keyGenerator;
    String keyStatementId = id + SelectKeyGenerator.SELECT_KEY_SUFFIX;
    keyStatementId = builderAssistant.applyCurrentNamespace(keyStatementId, true);
    if (configuration.hasKeyGenerator(keyStatementId)) {
      keyGenerator = configuration.getKeyGenerator(keyStatementId);
    } else {
      // INSERT 才使用 useGeneratedKeys，默认 useGeneratedKeys -> false
      // default -> NoKeyGenerator.INSTANCE
      keyGenerator = context.getBooleanAttribute("useGeneratedKeys",
          configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType))
          ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
    }

    /* 获取到 SQL DML 源脚本配置
    * ${param} 动态配置 -> org.apache.ibatis.scripting.xmltags.DynamicSqlSource
    * #{param} -> org.apache.ibatis.scripting.defaults.RawSqlSource
    * */
    SqlSource sqlSource = langDriver.createSqlSource(configuration, context, parameterTypeClass);
    StatementType statementType = StatementType.valueOf(context.getStringAttribute("statementType", StatementType.PREPARED.toString()));
    Integer fetchSize = context.getIntAttribute("fetchSize");
    Integer timeout = context.getIntAttribute("timeout");
    String parameterMap = context.getStringAttribute("parameterMap");
    // 返回类型
    String resultType = context.getStringAttribute("resultType");
    Class<?> resultTypeClass = resolveClass(resultType);
    /* 手动配置的 <resultMap> </resultMap>, <select resultMap="map"></select> , 多个使用 , 逗号分隔
    * org.apache.ibatis.builder.MapperBuilderAssistant.getStatementResultMaps
    * */
    String resultMap = context.getStringAttribute("resultMap");
    String resultSetType = context.getStringAttribute("resultSetType");
    ResultSetType resultSetTypeEnum = resolveResultSetType(resultSetType);
    if (resultSetTypeEnum == null) {
      // org.apache.ibatis.mapping.ResultSetType
      resultSetTypeEnum = configuration.getDefaultResultSetType();
    }
    String keyProperty = context.getStringAttribute("keyProperty");
    String keyColumn = context.getStringAttribute("keyColumn");
    String resultSets = context.getStringAttribute("resultSets");

    builderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType,
        fetchSize, timeout, parameterMap, parameterTypeClass, resultMap, resultTypeClass,
        resultSetTypeEnum, flushCache, useCache, resultOrdered,
        keyGenerator, keyProperty, keyColumn, databaseId, langDriver, resultSets);
  }

  private void processSelectKeyNodes(String id, Class<?> parameterTypeClass, LanguageDriver langDriver) {
    // selectKey 可以有多个
    List<XNode> selectKeyNodes = context.evalNodes("selectKey");
    if (configuration.getDatabaseId() != null) {
      parseSelectKeyNodes(id, selectKeyNodes, parameterTypeClass, langDriver, configuration.getDatabaseId());
    }
    parseSelectKeyNodes(id, selectKeyNodes, parameterTypeClass, langDriver, null);
    removeSelectKeyNodes(selectKeyNodes);
  }

  private void parseSelectKeyNodes(String parentId, List<XNode> list, Class<?> parameterTypeClass, LanguageDriver langDriver, String skRequiredDatabaseId) {
    for (XNode nodeToHandle : list) {
      String id = parentId + SelectKeyGenerator.SELECT_KEY_SUFFIX;
      String databaseId = nodeToHandle.getStringAttribute("databaseId");
      if (databaseIdMatchesCurrent(id, databaseId, skRequiredDatabaseId)) {
        parseSelectKeyNode(id, nodeToHandle, parameterTypeClass, langDriver, databaseId);
      }
    }
  }

  private void parseSelectKeyNode(String id, XNode nodeToHandle, Class<?> parameterTypeClass, LanguageDriver langDriver, String databaseId) {
    String resultType = nodeToHandle.getStringAttribute("resultType");
    // TypeAliasRegistry typeAliasRegistry -> 获取对象类型 class
    Class<?> resultTypeClass = resolveClass(resultType);
    // STATEMENT, PREPARED, CALLABLE, default -> PREPARED
    StatementType statementType = StatementType.valueOf(nodeToHandle.getStringAttribute("statementType", StatementType.PREPARED.toString()));
    // resultType key field name
    String keyProperty = nodeToHandle.getStringAttribute("keyProperty");
    // select database data map resultType key field column
    String keyColumn = nodeToHandle.getStringAttribute("keyColumn");
    boolean executeBefore = "BEFORE".equals(nodeToHandle.getStringAttribute("order", "AFTER"));

    //defaults
    boolean useCache = false;
    boolean resultOrdered = false;
    KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
    Integer fetchSize = null;
    Integer timeout = null;
    boolean flushCache = false;
    String parameterMap = null;
    String resultMap = null;
    ResultSetType resultSetTypeEnum = null;

    // parameterTypeClass 入参值类型，创建 sql 执行语句
    SqlSource sqlSource = langDriver.createSqlSource(configuration, nodeToHandle, parameterTypeClass);
    SqlCommandType sqlCommandType = SqlCommandType.SELECT;

    builderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType,
        fetchSize, timeout, parameterMap, parameterTypeClass, resultMap, resultTypeClass,
        resultSetTypeEnum, flushCache, useCache, resultOrdered,
        keyGenerator, keyProperty, keyColumn, databaseId, langDriver, null);

    // currentNameSpace.id
    id = builderAssistant.applyCurrentNamespace(id, false);

    MappedStatement keyStatement = configuration.getMappedStatement(id, false);
    configuration.addKeyGenerator(id, new SelectKeyGenerator(keyStatement, executeBefore));
  }

  private void removeSelectKeyNodes(List<XNode> selectKeyNodes) {
    for (XNode nodeToHandle : selectKeyNodes) {
      nodeToHandle.getParent().getNode().removeChild(nodeToHandle.getNode());
    }
  }

  private boolean databaseIdMatchesCurrent(String id, String databaseId, String requiredDatabaseId) {
    if (requiredDatabaseId != null) {
      return requiredDatabaseId.equals(databaseId);
    }
    if (databaseId != null) {
      return false;
    }
    // currentNameSpace.id
    id = builderAssistant.applyCurrentNamespace(id, false);
    // Map<String, MappedStatement> mappedStatements -> contain this id
    if (!this.configuration.hasStatement(id, false)) {
      return true;
    }
    // skip this statement if there is a previous one with a not null databaseId
    MappedStatement previous = this.configuration.getMappedStatement(id, false); // issue #2
    return previous.getDatabaseId() == null;
  }

  private LanguageDriver getLanguageDriver(String lang) {
    Class<? extends LanguageDriver> langClass = null;
    if (lang != null) {
      langClass = resolveClass(lang);
    }
    return configuration.getLanguageDriver(langClass);
  }

}
