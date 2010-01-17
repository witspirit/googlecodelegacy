package witspirit.calculator;

var result;

function run(args: String[]) {

	def numOne = java.lang.Integer.parseInt(args[0]);
	def numTwo = java.lang.Integer.parseInt(args[1]);

	println("ADD = {add(numOne, numTwo)}");
	println("SUBTRACT = {subtract(numOne, numTwo)}");
	multiply(numOne,numTwo);
	divide(numOne, numTwo);

}

function add(v1: Integer, v2: Integer) : Integer {
     result = v1 + v2;
     println("{v1} + {v2} = {result}");
     return result;
}

function subtract(numOne: Integer, numTwo: Integer) : Integer {
     result = numOne - numTwo;
     println("{numOne} - {numTwo} = {result}");
     result;
}

function multiply(numOne: Integer, numTwo: Integer) {
     result = numOne * numTwo;
     println("{numOne} * {numTwo} = {result}");
}

function divide(numOne: Integer, numTwo: Integer) {
     result = numOne / numTwo;
     println("{numOne} / {numTwo} = {result}");
}