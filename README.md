# args4j

Create simple command line arguments for any program with simple annotations in Java!

# annotations

All annotations have some shared parameters for further customisation.

**shortName** for argument short hands.

**description** for argument help text descriptions.

## Flag

When provided via CLI, this flips your corresponding boolean fields on (true).

**Argument example** `--example` **or** `-e`

**Code example**
```java
public class YourClass {
    @Flag
    private static boolean flag;

    public static void main(final String[] args) {
        new ArgumentBuilder().scan(YourClass.class, args);
    }
}
```

## Option

When provided via CLI, it reads the text given with the option and pushes that to the corresponding field.

**Argument example** `--example text` **or** `-e text`

**Code example**
```java
public class YourClass {
    @Option
    private static String option;

    public static void main(final String[] args) {
        new ArgumentBuilder().scan(YourClass.class, args);
    }
}
```
