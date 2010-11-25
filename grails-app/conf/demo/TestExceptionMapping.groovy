package demo

exceptionMapping = {
	
	"catch all" HumanRelatedException, {
		controller = "human"
		action = "problem"
	}
	
	"no more milk" NoMoreMilkException, {
		controller = "shoppinglist"
		action = "addItem"
		item = "milk"
	}
	
}