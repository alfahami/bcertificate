import java.util.Objects;

public class Main {
  public static void main(String[] args) {
    String[] myStr = {"ALFAHAMI TOIHIR", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021"};
    System.out.println(Integer.toHexString(Objects.hashCode(myStr)));
  }
}
