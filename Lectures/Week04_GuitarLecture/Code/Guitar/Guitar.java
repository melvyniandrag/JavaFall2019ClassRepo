import java.util.*;

public class Guitar{
	private ArrayList<GuitarString> guitar = null;

	Guitar(){
		guitar = new ArrayList<GuitarString>();
		guitar.add(new GuitarString("E"));
		guitar.add(new GuitarString("A"));
		guitar.add(new GuitarString("D"));
		guitar.add(new GuitarString("G"));
		guitar.add(new GuitarString("B"));
		guitar.add(new GuitarString("E"));
	}

	public void CMajShape(int barreLocation){
		System.out.println("***************************************************************");
		System.out.println("At fret " + barreLocation + " the CMaj Shape plays the following notes:");
		System.out.println("A string plays: " + guitar.get(1).getFrettedNote(barreLocation + 3)); 
		System.out.println("D string plays: " + guitar.get(2).getFrettedNote(barreLocation + 2)); 
		System.out.println("G string plays: " + guitar.get(3).getFrettedNote(barreLocation)); 
		System.out.println("B string plays: " + guitar.get(4).getFrettedNote(barreLocation + 1)); 
		System.out.println("E string plays: " + guitar.get(5).getFrettedNote(barreLocation)); 
		System.out.println("***************************************************************");
		// return sorted, uniq array of notes played.
	}

	public void EMajShape(int barreLocation){
		System.out.println("***************************************************************");
		System.out.println("At fret " + barreLocation + " the EMaj Shape plays the following notes:");
		System.out.println("E string plays: " + guitar.get(0).getFrettedNote(barreLocation)); 
		System.out.println("A string plays: " + guitar.get(1).getFrettedNote(barreLocation + 2)); 
		System.out.println("D string plays: " + guitar.get(2).getFrettedNote(barreLocation + 2)); 
		System.out.println("G string plays: " + guitar.get(3).getFrettedNote(barreLocation + 1)); 
		System.out.println("B string plays: " + guitar.get(4).getFrettedNote(barreLocation)); 
		System.out.println("E string plays: " + guitar.get(5).getFrettedNote(barreLocation)); 
		System.out.println("***************************************************************");
	}
	
	public static void main(String[] args){
		Guitar gc = new Guitar();
		gc.EMajShape(0);
		gc.EMajShape(1);
		gc.EMajShape(3);
		gc.EMajShape(5);

		gc.CMajShape(0);
		gc.CMajShape(7);
	}
}

class GuitarString{
	public static String[] Notes = { 
		"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#",
		"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"
	};

	public String stringName;

	GuitarString(String _stringName ){
		stringName = _stringName;
	}

	public String getFrettedNote(int fret){
		return Notes[ArrayIndexOf(Notes, stringName) + fret];
	}

	private static int ArrayIndexOf(String[] notes, String value){
		for(int i = 0; i < notes.length; ++i){
			if ( notes[i] == value){
				return i;
			}
		}
		return -2 * Notes.length; // to ensure an error in the notes array if you try to access an element there.
	}
}

// Homework
class GuitarChordIdentifier{
	//https://en.wikipedia.org/wiki/Major_chord
	//Here is a reference for the notes in every chord. Or
	//you can devise them yourself using the math we did today.

	public static String chordIdentifier(String[] notes){
		return "A"; // or whatever the chord is played with the notes in `notes`.
	}
}
