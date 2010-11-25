package demo

class ProblemController {

    def noMoreMilk = {
		throw new NoMoreMilkException("No more milk!!")
	}
	
}
