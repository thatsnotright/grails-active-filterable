package com.eschewobfuscation.util.grails;

import groovy.lang.ExpandoMetaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.apache.bcel.classfile.Modifiers;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.classgen.VariableScopeVisitor;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.grails.compiler.injection.GrailsASTUtils;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import java.lang.reflect.Modifier;

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
public class ActiveFilterableASTTransformer implements ASTTransformation {

    private static final Log LOG = LogFactory.getLog(ActiveFilterableASTTransformer.class);

    public void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        LOG.info("Modifying source unit "+sourceUnit.getName());
        ExpandoMetaClass.disableGlobally();
        for (ASTNode astNode : astNodes) {
            if (astNode instanceof ClassNode) {
                ClassNode theClass = (ClassNode) astNode;
                if (!GrailsASTUtils.hasOrInheritsProperty(theClass, "active")) {
                    LOG.info("Adding active field to class "+theClass.getName());
                    theClass.addProperty("active", Modifier.PUBLIC, ClassHelper.Boolean_TYPE, ConstantExpression.TRUE, null, null);
                }

                NamedArgumentListExpression nale = new NamedArgumentListExpression();
                nale.addMapEntryExpression(new MapEntryExpression(new ConstantExpression("condition"), new ConstantExpression("active=1")));
                nale.addMapEntryExpression(new MapEntryExpression(new ConstantExpression("default"), ConstantExpression.TRUE));

                MethodCallExpression mce = new MethodCallExpression(VariableExpression.THIS_EXPRESSION, "activeFilter", nale);
                Statement activeFilterExpression = new ExpressionStatement(mce);
                PropertyNode hibField = theClass.getProperty("hibernateFilters");
                if (hibField != null) {
                    LOG.info("Adding active filter to existing hibernateFilters closure for class "+theClass.getName());
                    if (hibField.getInitialExpression() instanceof ClosureExpression) {
                        ClosureExpression ce = (ClosureExpression) hibField.getInitialExpression();
                        ((BlockStatement) ce.getCode()).addStatement(activeFilterExpression);
                    } else {
                        LOG.error("Do not know how to add activeFilter expression to non ClosureExpression " + hibField.getInitialExpression());
                    }
                } else {
                    LOG.info("Adding active filter and hibernateFilters closure for class "+theClass.getName());
                    Statement[] activeFilterStatement = {activeFilterExpression};
                    BlockStatement closureBlock = new BlockStatement(activeFilterStatement, null);
                    ClosureExpression hibFilClosure = new ClosureExpression(null, closureBlock);
                    theClass.addProperty("hibernateFilters", Modifier.STATIC|Modifier.PUBLIC, ClassHelper.OBJECT_TYPE, hibFilClosure, null, null);
                }
                VariableScopeVisitor scopeVisitor = new VariableScopeVisitor(sourceUnit);
                scopeVisitor.visitClass(theClass);
            }
        }
        ExpandoMetaClass.enableGlobally();
    }
}
