package org.codehaus.groovy.grails.resolve

import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor
import org.apache.ivy.core.module.id.ModuleId
import org.apache.ivy.core.module.id.ArtifactId
import org.apache.ivy.core.module.descriptor.DefaultExcludeRule
import org.apache.ivy.core.module.id.ModuleRevisionId
import org.apache.ivy.plugins.matcher.ExactPatternMatcher

/**
 * Adds new methods to make access to this class Groovier
 *
 * @author Graeme Rocher
 * @since 1.2
 */

public class EnhancedDefaultDependencyDescriptor extends DefaultDependencyDescriptor{

    static final String WILDCARD = '*'


    String scope

    EnhancedDefaultDependencyDescriptor(ModuleRevisionId mrid, boolean force, String scope) {
        super(mrid, force);
        this.scope = scope
    }

    void excludes(Object... args) {
        for(arg in args) {
            if(arg instanceof String) {
                excludeForString(arg)
            }
            else if(arg instanceof Map) {
                excludeForMap(arg)
            }            
        }
    }

    private excludeForString (String dep) {
        def mid = ModuleId.newInstance(WILDCARD, dep)
        addRuleForModuleId(mid, scope)
    }


    private excludeForMap (Map args) {
        def mid = ModuleId.newInstance(args?.group ?: WILDCARD, args?.name ?: WILDCARD)
        addRuleForModuleId(mid, scope)
    }

    void setTransitive (boolean b) {
        this.@isTransitive = b
    }


    private addRuleForModuleId(ModuleId mid, String scope) {
        def id = new ArtifactId(mid, WILDCARD, WILDCARD, WILDCARD)
        
        def rule = new DefaultExcludeRule(id, ExactPatternMatcher.INSTANCE, null)
        addExcludeRule scope, rule
    }

}