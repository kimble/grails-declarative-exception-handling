Declarative Exception Handling
==============================

This is just an experimental Grails plugin. 
I never got any feedback at the [mailinglist](http://grails.1312388.n4.nabble.com/Best-practice-for-dealing-with-exceptions-in-controller-actions-td3057273.html) 
when I asked for best practices regarding exception handling in controllers. The aim of this plugin is to reduce noise in controllers and make it easier to implement consistent error handling. 

Goal: Make less noise
---------------------

    class FridgeController {
    
        def milkFactory // implements the interface Cow
 
        // This is a lot of noise
        def getMilk = {
            def milk = null
            try {
                milk = milkFactory.getMilk()
            } catch (NoMoreMilkException mex) {
                redirect controller: 'shoppingList', action: 'addItem', params: [ item: 'milk' ]
            } 
            
            [ milk: milk ]
        }
    
    }


Piggybacking on the UrlMappings plugin
--------------------------------------

The plugin will look for a static `exceptionMappings` closure in any of your `*UrlMappings` files. It will also pick up any changes in these files during development. 

    class UrlMappings {

        static mappings = {
            // Regular URL mappings still goes here..
        }

        static exceptionMappings = {
            "no more milk" NoMoreMilkException, { ex ->
                controller = "shoppingList"
                action = "addItem"
                item = "milk"
            }		
        }

    }
    
    // A lot less noisy, does the same thing
    class FridgeController {
    
        def milkFactory
 
        def getMilk = {
            def milk = milkFactory.getMilk()
            [ milk: milk ]
        }
    
    }

### The name
Each mapping is defined by a name, an exception class and a closure. The name is only there for debugging and readability reasons. 

### Exception class
The rules will be checked in the order they are defined so make sure to define the most specific exceptions in your exception hierarchy first.

### Closure
The closure is executed when a rule matches an exception instance. You're expected to set the controller and action property. Any other properties you set in the closure will be made available for the action method as properties in `params`.

Behind the scenes
-----------------

When the plugin is loaded (after the controllers plugin) it will replace the `exceptionHandler` bean. Any exceptions that doesn't match any of the rules will be sent to original exception handler. 

Bugs
----

It doesn't work after a container reload. I'm not sure why this happens. 