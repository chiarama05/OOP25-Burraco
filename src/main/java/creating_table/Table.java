package creating_table;

import java.util.*;
import carta;

public class Table{

    private final List<Card> table = new ArrayList<>();
    private final String name;

    Table(final String name){
        this.name=name;
    }
}