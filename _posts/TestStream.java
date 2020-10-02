package _posts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TestStream {
    static class Person{
        int age;
        String name;
        boolean isFemail;
        public Person(String name, int age, boolean isFemale){
            this.name = name;
            this.age = age;
            this.isFemail = isFemale;
        }
        
        public Person(int age, boolean isFemale){
            this.age = age;
            this.isFemail = isFemale;
        }

        public boolean isFemale(){
            return this.isFemail;
        }

        public String getName(){
            return this.name;
        }
        public int getAge(){
            return this.age;
        }

        public void setAge(int age){
            this.age = age;
        }

        @Override
        public String toString(){
            return "name:" + name + " age: " + age + " isFemale: " + isFemale();
        }

        @Override
        public boolean equals(Object obj){
            if( obj == this)return true;
            if(!(obj instanceof Person))return false;

            Person p = (Person)obj;
            return p.getName().equals(this.name);
        }

        @Override
        public int hashCode(){
            return name.hashCode();
        }
    }

    public static void main(String[] args){
        Person p1 = new Person("A", 40, false);
        Person p2 = new Person("B", 50, true);
        Person p3 = new Person("A", 20, true);

        List<Person> persons = new ArrayList();
        persons.add(p1);
        persons.add(p2);
        persons.add(p3);


        boolean existFemale = persons.stream().anyMatch(e -> e.isFemale() );
        System.out.println(existFemale);

        boolean allFemale = persons.stream().allMatch(e -> e.isFemale());
        System.out.println(allFemale);

        boolean noFemale = persons.stream().noneMatch(e -> e.isFemale());
        System.out.println(noFemale);

        // Optional<Person> femalePerson = persons.stream().filter(e -> e.isFemale()).findAny();
        // if(femalePerson.isPresent()){
        //     System.out.println(femalePerson.get());
        // }

        List<Person> femalePersons = persons.stream()
                    .filter(e -> e.isFemale()).collect(Collectors.toList());

        for(Person female: femalePersons){
            System.out.println(female);
        }

        double average = persons.stream().mapToInt(e -> e.getAge()).average().getAsDouble();
        System.out.println("average age is : " + average);

        List<String> names = persons.stream().map(e -> e.getName()).collect(Collectors.toList());

        List<Person> result = persons.stream().map(e -> {
            Person p = new Person(e.getName(), e.getAge(), e.isFemale());
            p.setAge(p.getAge() + 10);
            return p;
        }).collect(Collectors.toList());

        for(Person person: result){
            System.out.println(person);
        }

        Predicate<Person> ageGreaterThan10 = new Predicate<Person>() {
            @Override
            public boolean test(Person p) {
                return p.getAge() > 10;
            }
        };

        List<Person> results = persons.stream()
                              .filter(ageGreaterThan10)
                              .collect(Collectors.toList());

        Comparator<Person> reverseComparator = new Comparator<Person>() {
            @Override
            public int compare(Person e1, Person e2) {
                return e1.getAge() > e2.getAge() ? -1 : 1;
            }
        }; 

        List<Person> sorted = persons.stream().sorted(reverseComparator).collect(Collectors.toList());
                            

        for(Person p : sorted){
            System.out.println(p);
        }


        long count = persons.stream().count();
        System.out.println(count);

        List<Person> limit = persons.stream().limit(1).collect(Collectors.toList());
        System.out.println(limit);

        List<Person> skip = persons.stream().skip(persons.size() - 1).collect(Collectors.toList());
        System.out.println(skip);

        List<Person> distinct = persons.stream().distinct().collect(Collectors.toList());
        System.out.println(distinct);

        int sum = persons.stream().map(e -> e.getAge()).reduce(0,(a,b) -> a + b);
        System.out.println(sum);

        
        String s = "number1 1 number2 2 number3 3";
        List<Integer> l = Arrays.stream(s.split(" ")).filter(e -> e.matches("\\d+")) //.peek(System.out::println)
                          .mapToInt(i -> Integer.valueOf(i)).boxed().collect(Collectors.toList());
        System.out.println(l);

    }
    


}