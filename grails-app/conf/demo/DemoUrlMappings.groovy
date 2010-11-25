package demo


class DemoUrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
	
	static exceptionMappings = {
		
		"no more milk" NoMoreMilkException, {
			controller = "shoppingList"
			action = "addItem"
			item = "milk"
		}
		
		"human related exception" HumanRelatedException, {
			controller = "human"
			action = "problem"
		}
		
	}
	 
}
