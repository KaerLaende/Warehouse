package models;

import lombok.AllArgsConstructor;
import lombok.Data;


public enum Size {
    KIDS_VERY_SMALL("7-10"), KIDS_SMALL("11-14"), KIDS_MIDL("16-20"),KIDS_LARGE("22-24"),ADULT_SMALL("26-28"),ADULT_MIDL("30-32"),ADULT_LARGE("34-36");
    Size(String translation) {
    }
}
