public class Runner {
    public static void main(String[] args) {
        try {
            System.out.println(LexicalAnalyser.analyse("0.7 + 11.8"));
        }
        catch (NumberException e) {
            System.out.println("Number exception!");
        }
        catch (ExpressionException e) {
            System.out.println("Expression exception!");
        }
    }
}
