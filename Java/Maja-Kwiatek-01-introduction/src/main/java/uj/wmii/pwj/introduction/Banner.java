package uj.wmii.pwj.introduction;

import java.util.HashMap;
import java.util.Arrays;
import java.util.Locale;


public class Banner {
    HashMap<Character, String> map;
    Banner(){
        map = new HashMap<>();
        map.put('A', """
                  #   ;    
                 # #  ; 
                #   # ;  
               #     #;
               #######;
               #     #;
               #     #;
                """);
        map.put('B', """
                ###### ;
                #     #;
                #     #;
                ###### ;
                #     #;
                #     #;
                ###### ;
                """);
        map.put('C', """
                 ##### ;
                #     #;
                #      ;
                #      ;
                #      ;
                #     #;
                 ##### ;
                """);
        map.put('D', """
                ###### ;
                #     #;
                #     #;
                #     #;
                #     #;
                #     #;
                ###### ;
                """);
        map.put('E', """
                #######;
                #      ;
                #      ;
                #####  ;
                #      ;
                #      ;
                #######;
                """);
        map.put('F', """
                #######;
                #      ;
                #      ;
                #####  ;
                #      ;
                #      ;
                #      ;
                """);
        map.put('G', """
                 ##### ;
                #     #;
                #      ;
                #  ####;
                #     #;
                #     #;
                 ##### ;
                """);
        map.put('H', """
                #     #;
                #     #;
                #     #;
                #######;
                #     #;
                #     #;
                #     #;
                """);
        map.put('I', """
                ###;
                 # ;
                 # ;
                 # ;
                 # ;
                 # ;
                ###;
                """);
        map.put('J', """
                      #;
                      #;
                      #;
                      #;
                #     #;
                #     #;
                 ##### ;
                """);
        map.put('K', """
                #    #;
                #   # ;
                #  #  ;
                ###   ;
                #  #  ;
                #   # ;
                #    #;
                """);
        map.put('L', """
                #      ;
                #      ;
                #      ;
                #      ;
                #      ;
                #      ;
                #######;
                """);
        map.put('M', """
                #     #;
                ##   ##;
                # # # #;
                #  #  #;
                #     #;
                #     #;
                #     #;
                """);
        map.put('N', """
                #     #;
                ##    #;
                # #   #;
                #  #  #;
                #   # #;
                #    ##;
                #     #;
                """);
        map.put('O', """
                #######;
                #     #;
                #     #;
                #     #;
                #     #;
                #     #;
                #######;
                """);
        map.put('P', """
                ###### ;
                #     #;
                #     #;
                ###### ;
                #      ;
                #      ;
                #      ;
                """);
        map.put('Q', """
                 ##### ;
                #     #;
                #     #;
                #     #;
                #   # #;
                #    # ;
                 #### #;
                """);
        map.put('R', """
                ###### ;
                #     #;
                #     #;
                ###### ;
                #   #  ;
                #    # ;
                #     #;
                """);
        map.put('S', """
                 ##### ;
                #     #;
                #      ;
                 ##### ;
                      #;
                #     #;
                 ##### ;
                """);
        map.put('T', """
                #######;
                   #   ;
                   #   ;
                   #   ;
                   #   ;
                   #   ;
                   #   ;
                """);
        map.put('U', """
                #     #;
                #     #;
                #     #;
                #     #;
                #     #;
                #     #;
                 ##### ;
                """);
        map.put('V', """
                #     #;
                #     #;
                #     #;
                #     #;
                 #   # ;
                  # #  ;
                   #   ;
                """);
        map.put('W', """
                #     #;
                #  #  #;
                #  #  #;
                #  #  #;
                #  #  #;
                #  #  #;
                 ## ## ;
                """);
        map.put('X', """
                #     #;
                 #   # ;
                  # #  ;
                   #   ;
                  # #  ;
                 #   # ;
                #     #;
                """);
        map.put('Y', """
                #     #;
                 #   # ;
                  # #  ;
                   #   ;
                   #   ;
                   #   ;
                   #   ;
                """);
        map.put('Z', """
                #######;
                     # ;
                    #  ;
                   #   ;
                  #    ;
                 #     ;
                #######;
                """);
    }

   public String[] toBanner(String str){
        if(str == null) return new String[0];

        String[] out = new String[7];
        Arrays.fill(out, "");
       for(int i=0; i<7; ++i) {
            for (char letter : str.toUpperCase().toCharArray()) {
                if(letter == ' '){
                    out[i] += "   ";
                }else {
                    out[i] += map.get(letter).split(";\\n")[i] + " ";
                }
            }
            out[i] = out[i].substring(0,out[i].length()-1);
        }
        return out;
   }

}

