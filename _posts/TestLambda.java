public class TestLambda{
    static class Person{
        public int age;
        public String name;
        public boolean isFemail;
    }


    public static void main(String[] args){
        TestLambda obj = new TestLambda();

        boolean result = obj.verify("Test Successful", s -> System.out.println(s));

        System.out.println(result);

    }
}
