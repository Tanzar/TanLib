/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coders;

import tanlib.coders.RemainCoder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class RemainCoderTest {
    
    public RemainCoderTest() {
    }
    
    @Test
    public void testEncoding() {
        System.out.println("Testing RemainCoder, encoding");
        Integer code = 190910;
        String text = "Nagroda Darwina - nagroda, nazwana na cześć Karola Darwina, twórcy teorii ewolucji i doboru naturalnego, przyznawana przez internautów[1] za niewyjątkowe i niebagatelne odkrycia. Ci, którzy odeszli z tego świata w najgłupszy sposób, mogą liczyć na nagrodę, oczywiście pośmiertną. Każdy na YT stara się o tą nagrodę.";
        String result = RemainCoder.encode(code, text);
        assertNotEquals(text, result);
        
    }
    
    @Test
    public void testDecoding() {
        System.out.println("Testing RemainCoder, decoding");
        Integer code = 190910;
        String text = ".a]JoiaWfaJnl\"aW2W\"a]Joia-W\"a0na\"aW\"aW<0bśćWBaJotaWfaJnl\"a-W[nóJ<ZW[boJllWbnot$<~lWlWio/oJ$W\"a[$Jat\"b]o-WGJ0Z0\"ana\"aWGJ0b0Wl\"[bJ\"a$[ón#U!W0aW\"lbnZ~ą[VonbWlW\"lb/a]a[bt\"bWoiVJZ<lawWml-WV[óJ0ZWoib70tlW0W[b]oWśnla[aWnW\"a~]ł$G70ZW7Go7ó/-W+o]ąWtl<0ZćW\"aW\"a]Joię-Wo<0Znlś<lbWGoś+lbJ[\"ąwWBażiZW\"aWzhW7[aJaW7lęWoW[ąW\"a]Joięw";
        String expResult = "Nagroda Darwina - nagroda, nazwana na cześć Karola Darwina, twórcy teorii ewolucji i doboru naturalnego, przyznawana przez internautów[1] za niewyjątkowe i niebagatelne odkrycia. Ci, którzy odeszli z tego świata w najgłupszy sposób, mogą liczyć na nagrodę, oczywiście pośmiertną. Każdy na YT stara się o tą nagrodę.";
        String result = RemainCoder.decode(code, text);
        boolean check = expResult.equals(result);
        assertTrue(check);
        
    }
}
