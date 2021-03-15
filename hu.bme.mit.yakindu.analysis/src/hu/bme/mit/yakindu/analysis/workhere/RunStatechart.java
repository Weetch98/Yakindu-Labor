package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;

public class RunStatechart {
	
	
	//Task 3.5 solution:
	public static void inputListener(IExampleStatemachine s) throws IOException {
		
		Method[] smethods = s.getSCInterface().getClass().getMethods();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		while(true) {
			String input = reader.readLine();
			input = input.toLowerCase();
			
			if(input.equals("exit")) {
				return;
			}else {
				String methodname = "raise"+input;
				
				for(Method m : smethods) {
					if(m.getName().toLowerCase().equals(methodname)) {
						try {
							m.invoke(s.getSCInterface());
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
						break;
					}
				}
			}
			print(s);
		}
	}
	
	//Task 3.5 solution:
	public static void inputListenerV2(IExampleStatemachine s) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		while(true) {
			
			String input = reader.readLine();
			
			if(input.toLowerCase().equals("exit")) {
				return;
			}
			
			if(input.toLowerCase().equals("white")) {
				s.getSCInterface().raiseWhite();
			}
			
			if(input.toLowerCase().equals("black")) {
				s.getSCInterface().raiseBlack();
			}
			
			if(input.toLowerCase().equals("start")) {
				s.getSCInterface().raiseStart();
			}
			
			print(s);
			
		}
		
	}

	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		inputListener(s);
		System.exit(0);
		
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
