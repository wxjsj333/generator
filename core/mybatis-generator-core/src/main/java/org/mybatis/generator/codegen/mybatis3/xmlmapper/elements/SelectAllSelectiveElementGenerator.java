/**
 *    Copyright 2006-2019 the original author or authors.
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
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * @author Jeff Butler
 */
public class SelectAllSelectiveElementGenerator extends AbstractXmlElementGenerator {

  public SelectAllSelectiveElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    // 设置xml的sql生成对象
    XmlElement answer = new XmlElement("select");

    // 设置ID，对应java的方法名
    answer.addAttribute(new Attribute("id", introspectedTable.getSelectAllSelectiveStatementId()));
    // 设置返回类型，BaseResultMap
    answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
    // 设置参数类型
    String parameterType = introspectedTable.getBaseRecordType();
    answer.addAttribute(new Attribute("parameterType", parameterType));

    context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();
    answer.addElement(new TextElement(sb.toString()));
    answer.addElement(getBaseColumnListElement());

    // 清空字符串buffer，并设置查询表名
    sb.setLength(0);
    sb.append("from ");
    sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
    sb.append("where 1=1");
    answer.addElement(new TextElement(sb.toString()));

    // 生成查询条件
    for (IntrospectedColumn introspectedColumn : ListUtilities
      .removeGeneratedAlwaysColumns(introspectedTable.getAllColumns())) {
      // 清空字符串buffer，设置 property ！= null
      sb.setLength(0);
      sb.append(introspectedColumn.getJavaProperty());
      sb.append(" != null");
      XmlElement isNotNullElement = new XmlElement("if");
      isNotNullElement.addAttribute(new Attribute("test", sb.toString()));

      // 清空字符串buffer，设置 条件连接符 AND property = #{property}
      sb.setLength(0);
      sb.append(" AND ");
      sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      sb.append(" = ");
      sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));

      isNotNullElement.addElement(new TextElement(sb.toString()));
      answer.addElement(isNotNullElement);
    }

    if (context.getPlugins().sqlMapSelectAllElementGenerated(answer, introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
