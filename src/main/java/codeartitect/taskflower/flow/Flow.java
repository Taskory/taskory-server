package codeartitect.taskflower.flow;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Flow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
