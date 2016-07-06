import java.util.ArrayList;
import java.util.List;

/**
 * Created by cylee on 16/6/13.
 */
public class Test {

    void test() {
        List<Integer> integers = new ArrayList<>();
        add(integers);
        add2(integers);
    }

    void add(List<? extends Number> strings) {

    }

    void add2(List<?> strings) {
        Number s = (Number) strings.get(0);
    }
}
