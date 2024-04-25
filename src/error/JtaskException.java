package error;

/**
 * Jtask specific exception class, extends Exception.
 */
public class JtaskException extends Exception {
    int code;
    ERR_TYPE type;
    ERR_ORIGIN origin;
    public JtaskException(ERR_TYPE type, ERR_ORIGIN origin, int code) {
        super();
        this.type = type;
        this.origin = origin;
        this.code = code;
        this.display();
        System.exit(code);
    }

    private void display() {
        System.out.println("Error: " + this.type + " in " + this.origin);
        System.out.println("Check the given origin for more information.");
        System.out.println("Code: 0x" + this.code);
    }
}