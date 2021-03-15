package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.base.types.Direction;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.sgraph.Vertex;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}

	public static void readModelStates(Statechart s) {
		// Reading model states
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				System.out.println(state.getName());
			}
		}
	}
	
	//Task 2.3 solution:
	public static void readModelTransitions(Statechart s) {
		// Reading model transitions
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof Transition) {
				Transition trans = (Transition) content;
				Vertex source = trans.getSource();
				Vertex target = trans.getTarget();
				System.out.println(source.getName() + " -> " + target.getName());
			}
		}
	}
	
	//Task 2.4 solution:
	public static void readModelTrapStates(Statechart s) {
		// Reading model trap states
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				if(state.getOutgoingTransitions().isEmpty()) {
					System.out.println("Trap: "+state.getName());
				}
			}
		}
	}
	
	//Task 2.5 solution:
	public static void findNoNameStates(Statechart s) {
		// Finding states without a name and suggesting a name
		int stateIndex = 1;
				
		ArrayList<String> state_names = new ArrayList<String>();
		ArrayList<State> noname_states = new ArrayList<State>();
				
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				if(state.getName() == "") {
					noname_states.add(state);
				}else {
					state_names.add(state.getName());
				}
			}
		}
				
		for(State state : noname_states) {
			System.out.println("State found with no name: "+state.toString());
			String suggested = "State"+stateIndex;
					
			while(state_names.contains(suggested)) {
				suggested = "State" + ++stateIndex;
			}
					
			System.out.println("Suggested name: "+suggested);
		}
	}
	
	//Task 4.3 solution:
	public static void readModelVariables(Statechart s) {
		// Reading model variables
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof VariableDefinition) {
				VariableDefinition var = (VariableDefinition)content;
				String name = var.getName();
				System.out.println("Var: "+name);
			}
		}
	}
	
	//Task 4.3 solution:
	public static void readModelInEvents(Statechart s) {
		// Reading model in events
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof EventDefinition) {
				EventDefinition event = (EventDefinition)content;
				if(event.getDirection() == Direction.IN) {
					String name = event.getName();
					System.out.println("In event: "+name);
				}
			}
		}
	}
	
	//Task 4.4 solution:
	public static void variablesToSource(Statechart s) {
		// Reading model variables and printing them in given format.
		
		//Function header
		System.out.println("public static void print(IExampleStatemachine s){");
		
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof VariableDefinition) {
				VariableDefinition var = (VariableDefinition)content;
				
				String name = var.getName();
				char firstLetter = var.getName().charAt(0);
				
				System.out.println("System.out.println(\""+firstLetter+" = \" + s.getSCInterface().get"+name+"());");
			}
		}
		
		//End of function
		System.out.println("}");
	}
	
	//Task 4.5 solution:
	public static void codeGenerator(Statechart s) {
		
		String code = "public static void inputListener(IExampleStatemachine s) throws IOException {\n"+
				"	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));\n" + 
				"	\n" + 
				"	while(true) {\n" + 
				"		\n" + 
				"		String input = reader.readLine();\n" + 
				"		\n" + 
				"		if(input.toLowerCase().equals(\"exit\")) {\n" + 
				"			return;\n" + 
				"		}\n";
		
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof EventDefinition) {
				EventDefinition event = (EventDefinition)content;
				if(event.getDirection() == Direction.IN) {
					String name = event.getName();
					String name_upper = name.substring(0, 1).toUpperCase()+name.substring(1);
					
					code += "		if(input.toLowerCase().equals(\"" + name.toLowerCase() + "\")) {\n" + 
							"			s.getSCInterface().raise" + name_upper + "();\n" + 
							"		}\n" +
							"		\n";
				}
			}
		}
		
		iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof VariableDefinition) {
				VariableDefinition var = (VariableDefinition)content;
				
				String name = var.getName().substring(0, 1).toUpperCase()+var.getName().substring(1);
				char firstLetter = var.getName().charAt(0);
				
				code += "		System.out.println(\""+firstLetter+" = \" + s.getSCInterface().get"+name+"());\n";
			}
		}
		code += "	}\n" +
				"}";
		
		System.out.println(code);
	}

	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		Statechart s = (Statechart) root;
				
		codeGenerator(s);
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
