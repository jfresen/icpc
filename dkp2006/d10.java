package dkp2006;

// Colour sequence, by Andre Deutz
import java.io.*;

class TripleSequence {
	
	public TripleSequence(String givenSequence, String frontSequenceOfP, String backSequenceOfP){
		this.givenSequence = new String(givenSequence);
		this.frontSequenceOfP=new String(frontSequenceOfP);
		this.backSequenceOfP=new String(backSequenceOfP);
	}
	
	private String givenSequence, frontSequenceOfP, backSequenceOfP;
	
    
    private boolean isMatch(char c, char f, char b){
    	return ((c==f)||(c==b))||(('*'==f)&&('*'==b));
    }
    
    private int positionOfMatchInTail(char c, int beginFocus){
    	int result = -3;
    	int length = frontSequenceOfP.length();
    	for (int j=beginFocus; j<length; j++){
    		if(isMatch(c,frontSequenceOfP.charAt(j), backSequenceOfP.charAt(j))){
    			result = j;
    			break;
    		}
    	}
    	return result;
    }
    
    public boolean givenIsSubsequence(){
    	boolean result = false;
    	int length=givenSequence.length();
    	if (length==0) return true;
    	int position = 0;
    	position = positionOfMatchInTail(givenSequence.charAt(0), position);
    	result = (position>=0);
    	if (!result)return result;
        
        int beginFocus = 0;
        beginFocus = position + 1;
 
    	for (int i=1; i<length; i++ ){
    		position = positionOfMatchInTail(givenSequence.charAt(i), beginFocus);
                beginFocus = position + 1;
    		result = result && (position>0);
    		if (!result) break;
    	}
    	return result;
    }
}


        class MainD10 {

	public static void main(String[] args) {
		
                BufferedReader  stdin = new BufferedReader (new InputStreamReader(System.in));

		String givenSequence="", frontSequenceOfP="", backSequenceOfP="";
		String s;
		int numberOfTestCases=0;
		//read  numberOfTestCases;
		try {
			s = stdin.readLine();
			numberOfTestCases = Integer.parseInt(s);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(NumberFormatException ne){
			System.out.println("not numeric");
		}

		for (int i=0; i<numberOfTestCases; i++){
		    try {
			    givenSequence=stdin.readLine();	
			    frontSequenceOfP=stdin.readLine();
			    backSequenceOfP=stdin.readLine();
			    
		    } catch(IOException e){
			    e.printStackTrace();
		    }
		    TripleSequence tripleSequence=new TripleSequence(givenSequence,frontSequenceOfP, backSequenceOfP);
                    if (tripleSequence.givenIsSubsequence()) {
        	        System.out.println("win");
                    } else {
        	        System.out.println("lose");
                    }
		}
	
	}

}
