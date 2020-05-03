package machineproblem1;

import com.alee.laf.WebLookAndFeel;

public class Main {
    
    private void createAndShowGUI() {
        //declare and initialize jframe for sign up window
        signUpFrame signUp = new signUpFrame();
    }

    public static void main(String[] args) {
        //install custom look and feel
        //source: https://github.com/mgarin/weblaf
        WebLookAndFeel.install();
        
        new Main().createAndShowGUI();
    }
    
}
