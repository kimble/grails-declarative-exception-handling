Declarative Exception Handling for Grails
==============================

Experimental Grails plugin implementing declarative exception handling. 

I never got any feedback at the [mailinglist](http://grails.1312388.n4.nabble.com/Best-practice-for-dealing-with-exceptions-in-controller-actions-td3057273.html) when I asked for best practices regarding exception handling in controllers. The aim of this plugin is to reduce noise in controllers and make it easier to implement consistent error handling. 

Design goal: Consistent error handling (with less noise)
------------------------------------------------------------------------------

A large part of my love for Groovy and Grails comes from how successfully they reduce Java boilerplate code while at the same time increase readability. This plugin is an attempt to remove some redundant code from controllers and make it easier to implement _consistent_ error handling. 

Grails already supports [mapping of exceptions to views or controller actions](http://grails.org/doc/latest/guide/single.html#6.4.4%20Mapping%20to%20Response%20Codes), but it doesn't really do what I want. It only works for http 500 responses and all exceptions will be logged. There is no way (at least as far as I know) to actually _deal_ with exceptions that you're able to recover from. 

It's obviously possible to deal with this using try / catch, but it's noisy and you'll probably implement exactly the same error handling for the same exceptions over and over again so it's not very dry. 
 
    // Noisy, not dry
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


Exception mapping - example
-------------

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
            [ milk: milkFactory.getMilk() ]
        }
    
    }

### The name
Each mapping is defined by a name, an exception class and a closure. The name is only there for debugging and readability reasons. 

### Exception class
The rules will be checked in the order they are defined so make sure to define the most specific exceptions in your exception hierarchy first.

### Closure
The closure is executed when a rule matches an exception instance. You're expected to set the controller and action property. Any other properties you set in the closure will be made available for the controller action as properties in `params`.

The good, the bad and the ugly
---------------------------------------------

### The good parts

 * Controllers gets a lot less noisy. 
 * You only specify _once_ how you want to deal with certain exception. 

The last point is the biggest win as far as I'm concerned. Let's say your users has to accept an agreement to use certain features on your site. Instead of doing `if (user.hasAgreed())` everywhere you can just throw an `MissingUserAgreementException` and trust that the user will be redirected to the agreement page. 

### The bad parts

I don't know how likely this is to change in new versions of Grails, but as always when messing around with platform beans there is a chance that things might change in newer versions of Grails. 

Although if Grails 2.0 actually makes the switch from closure actions to methods things like these will be a lot easier to implement using aspects.  

### The ugly

None, that I'm aware of. Do tell me if I've overlooked some body traps!

Behind the scenes
-----------------

Grails will construct the `exceptionHandler` bean as usual. A bean post processor replaces it with this plugin's implementation of `HandlerExceptionResolver` and passes it a reference to the original handler. Whenever an exception occur that doesn't match any of the exception mapping rules it will be passed to the original handler. 

The plugin is piggybacking on the UrlMappings plugin. Changes to `*UrlMappings.groovy` will be picked up by this plugin as well. 

Bugs / roadmap
------------------------

* It sometimes stops working after a container reload, I haven't looked into this yet