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
package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Jeff Butler
 *
 */
public class SelectAllSelectiveMethodGenerator extends AbstractJavaMapperMethodGenerator {

  @Override
  public void addInterfaceElements(Interface interfaze) {
    Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
    // 创建方法
    Method method = new Method();
    // 设置作用域
    method.setVisibility(JavaVisibility.PUBLIC);
    // 配置List返回类型
    FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
    FullyQualifiedJavaType listType;
    listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
    // 导入List类型
    importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
    // 导入List内泛型类型
    importedTypes.add(listType);
    // 设置参数List包含泛型为listType
    returnType.addTypeArgument(listType);

    // 设置返回类型
    method.setReturnType(returnType);

    method.setName(introspectedTable.getSelectAllSelectiveStatementId());

    // no primary key class - fields are in the base class
    // if more than one PK field, then we need to annotate the
    // parameters
    // for MyBatis3
    List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();

    FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
    importedTypes.add(parameterType);
    method.addParameter(new Parameter(parameterType, "record"));

    addMapperAnnotations(interfaze, method);

    context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

    if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable)) {
      addExtraImports(interfaze);
      interfaze.addImportedTypes(importedTypes);
      interfaze.addMethod(method);
    }
  }

  public void addMapperAnnotations(Interface interfaze, Method method) {
  }

  public void addExtraImports(Interface interfaze) {
  }
}
