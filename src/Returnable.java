/**
 * Created by Michau on 06.06.2015.
 */

/**
 * Class used as a convenient way of returning multiple values with one method
 * May contain {@link Exception}, {@link String} and {@link Boolean} instance
 */
public class Returnable {
    /**
     * {@link Exception} instance used for storing function's exceptions
     */
    private Exception e;
    /**
     * {@link String} instance used for storing function's strings
     */
    private String s;
    /**
     * {@link Boolean} instance used for storing function's logical values
     */
    private boolean b;
    /**
     * Default {@link Returnable} constructor
     */
    public Returnable() {
        e = null;
        s = null;
        b = false;

    }

    /**
     * {@link Exception} setter
     * @param ex Function's exception
     */
    public void setE(Exception ex) {
        e = ex;
    }

    /**
     * {@link String} setter
     * @param ss Function's string
     */
    public void setS(String ss) {
        s = ss;
    }

    /**
     * {@link Boolean} setter
     * @param bb Function's logic value
     */
    public void setB(Boolean bb) {
        b=bb;
    }

    /**
     * {@link Exception} getter
     * @return Local {@link Exception} instance
     */
    public Exception getE() {
        return e;
    }
    /**
     * {@link String} getter
     * @return Local {@link String} instance
     */
    public String getS() {
        return s;
    }
    /**
     * {@link Boolean} getter
     * @return Local {@link Boolean} instance
     */
    public boolean getB() {
        return b;
    }
}