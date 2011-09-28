class ActiveFilterableGrailsPlugin {
    // the plugin version
    def version = "0.9.7"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.0 > *"
    // the other plugins this plugin depends on
    def dependsOn = [hibernateFilter:"* > 0.1.8"]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // hibernate filter inspects the grails domain classes at load/startup, which means it can miss
    // our addition of the activeFilter and etc.
    def loadBefore = ["hibernate-filter"]


    // TODO Fill in these fields
    def author = "Robert Elsner"
    def authorEmail = "thatsnotright@gmail.com"
    def title = "Active property and Hibernate filter for easy deactivating of data"
    def description = '''\\
This plugin adds a boolean field 'active' to each object annotated as @ActiveFilterable.  It also supplies a Hibernate Filter, which is enabled by default, to add WHERE clause criteria for only active elements.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/active-filterable"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
