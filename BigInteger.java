package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 *
 */
public class BigInteger {
    /**
     * True if this is a negative integer
     */
    boolean negative;

    /**
     * Number of digits in this integer
     */
    int numDigits;

    /**
     * Reference to the first node of this integer's linked list representation
     * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
     * For instance, the integer 235 would be stored as:
     *    5 --> 3  --> 2
     *
     * Insignificant digits are not stored. So the integer 00235 will be stored as:
     *    5 --> 3 --> 2  (No zeros after the last 2)
     */
    DigitNode front;

    /**
     * Initializes this integer to a positive number with zero digits, in other
     * words this is the 0 (zero) valued integer.
     */
    public BigInteger() {
        negative = false;
        numDigits = 0;
        front = null;
    }

    /**
     * Parses an input integer string into a corresponding BigInteger instance.
     * A correctly formatted integer would have an optional sign as the first
     * character (no sign means positive), and at least one digit character
     * (including zero).
     * Examples of correct format, with corresponding values
     *      Format     Value
     *       +0            0
     *       -0            0
     *       +123        123
     *       1023       1023
     *       0012         12
     *       0             0
     *       -123       -123
     *       -001         -1
     *       +000          0
     *
     * Leading and trailing spaces are ignored. So "  +123  " will still parse
     * correctly, as +123, after ignoring leading and trailing spaces in the input
     * string.
     *
     * Spaces between digits are not ignored. So "12  345" will not parse as
     * an integer - the input is incorrectly formatted.
     *
     * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
     * constructor
     *
     * @param integer Integer string that is to be parsed
     * @return BigInteger instance that stores the input integer.
     * @throws IllegalArgumentException If input is incorrectly formatted
     */
    public static BigInteger parse(String integer)
            throws IllegalArgumentException {
        if (integer == null)
            throw new NullPointerException("string is null");
        integer = integer.trim();
        if (integer.isEmpty())
            throw new IllegalArgumentException("string is empty");
        BigInteger Final = new BigInteger();
        if (integer.charAt(0) == '-') {
            Final.negative = true;
            integer = integer.substring(1);
        }
        else if (integer.charAt(0) == '+') {
            Final.negative = false;
            integer = integer.substring(1);
        }
        for (int i = 0; i < integer.length(); i++) {
            if (!Character.isDigit(integer.charAt(i)))
                throw new IllegalArgumentException("string has a value that is illegal");
        }
        for (int i = 0; i < integer.length(); i++) {
            if (integer.charAt(i) == 0)
                integer = integer.substring(i + 1);
        }
        while (integer.startsWith("0")) {
            integer = integer.substring(1);
        }
        if (integer.isEmpty() == true)
            return Final;
        Final.numDigits = 0;
        for (int i = 0; i < integer.length(); i++) {
            Final.front = new DigitNode(Integer.parseInt(integer.charAt(i) + ""), Final.front);
            Final.numDigits++;
        }
        return Final;
    }

    /**
     * Adds the first and second big integers, and returns the result in a NEW BigInteger object.
     * DOES NOT MODIFY the input big integers.
     *
     * NOTE that either or both of the input big integers could be negative.
     * (Which means this method can effectively subtract as well.)
     *
     * @param first First big integer
     * @param second Second big integer
     * @return Result big integer
     */
    public static BigInteger add(BigInteger first, BigInteger second) {
        if (first == null)
            throw new IllegalArgumentException("LL is empty");
        if (second == null)
            throw new IllegalArgumentException("LL is empty");
        if (first.numDigits==0 ) return second;
        if (second.numDigits==0 ) return first;
        BigInteger Final = new BigInteger();
        BigInteger temp1 = new BigInteger();
        boolean isNeg = false;
        if ((first.negative == false && second.negative == false) || (first.negative == true && second.negative == true)) {
            int digit = 0;
            int top = 0;
            int bottom = 0;
            int carry = 0;
            boolean t = false;
            boolean b = false;
            int op = 0;
            if (first.numDigits > second.numDigits) op = first.numDigits;
            if (first.numDigits < second.numDigits) op = second.numDigits;
            if (first.numDigits == second.numDigits) op = second.numDigits;
            if (first.negative == true && second.negative == true) isNeg = true;
            for (int i = 0; i < op; i++) {
                if (t == true) {
                    top = 0;
                } else {
                    top = first.front.digit;
                }
                if (b == true) {
                    bottom = 0;
                } else {
                    bottom = second.front.digit;
                }
                digit = top + bottom + carry;
                if (digit >= 10) {
                    carry = 1;
                    temp1.front = new DigitNode(digit - 10, temp1.front);
                    temp1.numDigits++;
                }
                if (digit < 10) {
                    carry = 0;
                    temp1.front = new DigitNode(digit, temp1.front);
                    temp1.numDigits++;
                }
                if (first.front.next == null) {
                    t = true;
                } else {
                    first.front = first.front.next;
                }
                if (second.front.next == null) {
                    b = true;
                } else {
                    second.front = second.front.next;
                }
                if (b == true && t == true) {
                    if (carry == 1) {
                        if (carry + top + bottom >= 10) {
                            temp1.front = new DigitNode(1, temp1.front);
                            temp1.numDigits++;
                        } else {
                            temp1.front = new DigitNode(carry + top + bottom, temp1.front);
                            temp1.numDigits++;
                        }
                        break;
                    }
                }
            }
        }
        else {
            int counter = 0, x = 0;
            BigInteger bigger;
            BigInteger smaller;
            boolean r = false;
            if (isGreater(first, second)) {
                bigger = second;
                smaller = first;
            } else {
                bigger = first;
                smaller = second;
            }
            if (bigger.negative == false && smaller.negative == true) {
                for (int i = 0; i < bigger.numDigits; i++) {
                    if (r) {
                        bigger.front.digit -= 1;
                        r = false;
                        if (bigger.front.digit == -1) {
                            bigger.front.digit += 10;
                            r = true;
                        }
                    }
                    x = bigger.front.digit - smaller.front.digit;
                    if (x < 0) {
                        r = true;
                        x = (10 + bigger.front.digit) - (smaller.front.digit);
                    }
                    counter++;
                    temp1.front = new DigitNode(x, temp1.front);
                    bigger.front = bigger.front.next;
                    if (smaller.numDigits <= counter) {
                        smaller.front.next = new DigitNode(0, smaller.front.next);
                    }
                    smaller.front = smaller.front.next;
                    temp1.numDigits++;
                }
            }
            if (bigger.negative == true && smaller.negative == false) {
                for (int i = 0; i < bigger.numDigits; i++) {
                    if (r) {
                        bigger.front.digit -= 1;
                        r = false;
                        if (bigger.front.digit == -1) {
                            bigger.front.digit += 10;
                            r = true;
                        }
                    }
                    x = bigger.front.digit - smaller.front.digit;
                    if (x < 0) {
                        r = true;
                        x = (10 + bigger.front.digit) - (smaller.front.digit);
                    }
                    counter++;
                    temp1.front = new DigitNode(x, temp1.front);
                    bigger.front = bigger.front.next;
                    if (smaller.numDigits <= counter) {
                        smaller.front.next = new DigitNode(0, smaller.front.next);
                    }
                    smaller.front = smaller.front.next;
                    temp1.numDigits++;
                }
                isNeg = true;
            }
        }
        while((temp1.front.digit == 0) && (temp1.front.next != null)){
            temp1.front = temp1.front.next;
            temp1.numDigits--;
        }
        Final.numDigits=temp1.numDigits;
        for (int i =0; i<temp1.numDigits; i++) {
            Final.front= new DigitNode(temp1.front.digit, Final.front);
            temp1.front= temp1.front.next;
        }
        if (Final.front.digit == 0 && Final.numDigits == 1) {Final.negative = false; isNeg = false;}
        if (isNeg == true) {Final.negative = true;}
        return Final;
    }

    private static boolean isGreater(BigInteger first, BigInteger second) {
        if(first.numDigits > second.numDigits) {
            return false;
        } else if(second.numDigits > first.numDigits) {
            return true;
        } else {
            DigitNode ptr1 = first.front, ptr2 = second.front;
            boolean greater = false;
            while(ptr1 != null && ptr2 != null) {
                if(ptr2.digit > ptr1.digit) {
                    greater = true;
                } else if(ptr2.digit < ptr1.digit) {
                    greater = false;
                }
                ptr1 = ptr1.next;
                ptr2 = ptr2.next;
            }
            return greater;
        }
    }
    /**
     * Returns the BigInteger obtained by multiplying the first big integer
     * with the second big integer
     *
     * This method DOES NOT MODIFY either of the input big integers
     *
     * @param first First big integer
     * @param second Second big integer
     * @return A new BigInteger which is the product of the first and second big integers
     */

    public static BigInteger multiply(BigInteger first, BigInteger second) {
        BigInteger product = new BigInteger();
        BigInteger temp = new BigInteger();
        BigInteger Final = new BigInteger();
        BigInteger mirror = new BigInteger();
        int fnum = first.numDigits;
        int snum = second.numDigits;
        boolean negative = false;
        int place = 0;
        int remain = 0;
        if((first.negative && !second.negative) || (!first.negative && second.negative)) {
            negative = true;
        }
        for(int i = 0; i<snum; i++){
            for (int k = 0; k<fnum; k++){
                int temp1 = (second.front.digit * first.front.digit) + remain;
                remain = temp1/10;
                if((temp1>=10)&& (k+1)==first.numDigits){
                    temp.front = new DigitNode(temp1%10, temp.front);
                    temp.front = new DigitNode(temp1/10, temp.front);
                    temp.numDigits += 2;
                }
                else{
                    temp.front = new DigitNode(temp1%10, temp.front);
                    temp.numDigits++;
                }
                mirror.front = new DigitNode(first.front.digit, mirror.front);
                mirror.numDigits++;
                first.front = first.front.next;
            }
            Final.numDigits=temp.numDigits;
            for (int h =0; h<temp.numDigits; h++) {
                Final.front= new DigitNode(temp.front.digit, Final.front);
                temp.front= temp.front.next;
            }
            for (int j = 0; j< place; j++){
                Final.front = new DigitNode(0, Final.front);
                Final.numDigits++;
            }
            product = add(product, Final);
            if((i+1) != snum){
                first.numDigits=mirror.numDigits;
                for (int m =0; m<mirror.numDigits; m++) {
                    first.front= new DigitNode(mirror.front.digit, first.front);
                    mirror.front= mirror.front.next;
                }
            }
            Final = new BigInteger();
            temp = new BigInteger();
            mirror = new BigInteger();
            place++;
            remain = 0;
            second.front = second.front.next;
        }
        if(negative) {
            product.negative = true;
        }
        return product;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (front == null) {
            return "0";
        }
        String retval = front.digit + "";
        for (DigitNode curr = front.next; curr != null; curr = curr.next) {
            retval = curr.digit + retval;
        }

        if (negative) {
            retval = '-' + retval;
        }
        return retval;
    }
}
